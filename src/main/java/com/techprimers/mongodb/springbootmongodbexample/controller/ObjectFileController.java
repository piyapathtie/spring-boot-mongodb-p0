package com.techprimers.mongodb.springbootmongodbexample.controller;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.techprimers.mongodb.springbootmongodbexample.document.Bucket;
import com.techprimers.mongodb.springbootmongodbexample.document.ObjectFile;
import com.techprimers.mongodb.springbootmongodbexample.document.ObjectFileParts;
import com.techprimers.mongodb.springbootmongodbexample.repository.BucketRepository;
import com.techprimers.mongodb.springbootmongodbexample.repository.ObjectFileRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.*;

@RestController
public class ObjectFileController {

    @Autowired
    private ObjectFileRepository objectFileRepository;

    @Autowired
    private BucketRepository bucketRepository;

    @PostMapping(value = "/{bucketname}/{objectname}", params = "create")
    public ResponseEntity createObject(@PathVariable(name = "bucketname") String bucketname,
                                       @PathVariable(name = "objectname") String objectname
                                       ) {

        String bnl = bucketname.toLowerCase();
        String onl = objectname.toLowerCase();
        Boolean newname = true;
        try {
            Bucket bucket = bucketRepository.findOneByName(bnl);
            if (bucket != null ){
                for (ObjectFile element : bucket.getObjectFileSet()) {
                    if (element.getName().equals(onl)){
                        newname = false;
                    }
                }
                if (newname){
                    UUID uuid = UUID.randomUUID();
                    String randomUUIDString = uuid.toString();

                    String path = "storage/" + bucket.getUuid() + "/"+ randomUUIDString;
                    File theDir = new File(path);
                    theDir.mkdir();

                    Long create = Instant.now().toEpochMilli();
                    HashMap<String, ObjectFileParts> hmap = new HashMap<>();

                    ObjectFile objectFile = new ObjectFile(onl, randomUUIDString, create, create, false, hmap);

                    bucket.setModified(create);
                    Set<ObjectFile> objectFileSet = bucket.getObjectFileSet();
                    objectFileSet.add(objectFile);
                    bucket.setObjectFileSet(objectFileSet);

                    bucketRepository.save(bucket);
//                    objectFileRepository.save(objectFile);

                    return ResponseEntity.ok().body(objectFile);
                }
                else{
                    return ResponseEntity.badRequest().body("fail to create " + onl + ", name is already used");
                }

            }
            else {
                return ResponseEntity.badRequest().body("fail to create " + onl + ", bucket does not exist");
            }
        }
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body("fail to create " + onl);
        }

    }

    public void upload(HttpServletRequest request, String filePath){

        try {
            FileOutputStream fout = new FileOutputStream(filePath);
            ServletInputStream inputStream;
            inputStream = request.getInputStream();
            IOUtils.copy(inputStream, fout);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @PutMapping(value = "/{bucketname}/{objectname}", params = "partNumber")
    public ResponseEntity uploadFilePart(
            HttpServletRequest request,
            @PathVariable(name = "bucketname") String bucketname,
            @PathVariable(name = "objectname") String objectname,
            @RequestParam(name = "partNumber") String partNumber,
            @RequestHeader(name = "Content-Length") String ContentLength,
            @RequestHeader(name = "Content-MD5") String ContentMD5
    ) {

        try {
            ObjectFile objectFile;
            String objuuid;
            Bucket bucket = bucketRepository.findOneByName(bucketname.toLowerCase());
            if (Integer.valueOf(partNumber) >= 1 && Integer.valueOf(partNumber) <= 10000){
                for (ObjectFile element : bucket.getObjectFileSet()) {
                    if (element.getName().equals(objectname)){
                        if (element.getTicket().equals(false)){
                            objectFile = element;
                            objuuid = element.getUuid();

                            bucket.getObjectFileSet();
                            String path = "storage/" + bucket.getUuid() + "/" + objuuid + "/" + objectname.toLowerCase() + "-" + partNumber;
                            ObjectFileParts objectFileParts = new ObjectFileParts(objectname.toLowerCase(), ContentMD5, Long.valueOf(ContentLength), partNumber);
                            objectFile.getObjFileParts().put(partNumber, objectFileParts);
                            upload(request, path);

                            bucketRepository.save(bucket);
                            HashMap<String , Object> hmap = new HashMap<>();
                            hmap.put("md5", ContentMD5);
                            hmap.put("length", ContentLength);
                            hmap.put("partNumber", partNumber);
                            return ResponseEntity.ok().body(hmap);
                        }
                    }
                }
            }
            System.out.println("error");
            return ResponseEntity.badRequest().body("error: ticketFlagged|InvalidPartNumber|InvalidObjectName|InvalidBucket");
        }
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body("error " + e);
        }

    }

    private static String calculateChecksumForMultipartUpload(List<String> md5s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String md5:md5s) {
            stringBuilder.append(md5);
        }

        String hex = stringBuilder.toString();
        byte raw[] = BaseEncoding.base16().decode(hex.toUpperCase());
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putBytes(raw);
        String digest = hasher.hash().toString();

        return digest + "-" + md5s.size();
    }


    @PostMapping(value = "/{bucketname}/{objectname}", params = "complete")
    public ResponseEntity CompleteMultiPartUpload(
            @PathVariable(name = "bucketname") String bucketname,
            @PathVariable(name = "objectname") String objectname
    ) {

        try {
            ObjectFile objectFile;
            Bucket bucket = bucketRepository.findOneByName(bucketname);
            if (bucket != null){
                for (ObjectFile element : bucket.getObjectFileSet()) {
                    if (element.getName().equals(objectname)){
                        objectFile = element;
                        Long length = Long.valueOf(0);
                        List md5s = new ArrayList();
                        for (Integer i = 1; i <= objectFile.getObjFileParts().size(); i++){
                            ObjectFileParts objectFileParts = (ObjectFileParts) objectFile.getObjFileParts().get(i.toString());
                            length += objectFileParts.getLength();
//                            System.out.println("---------" + objectFileParts.getMd5());
                            md5s.add(objectFileParts.getMd5());
                        }

                        HashMap<String , Object> hmap = new HashMap<>();
                        hmap.put("eTag", calculateChecksumForMultipartUpload(md5s));
                        hmap.put("length", length);
                        hmap.put("name", objectname.toLowerCase());
                        objectFile.setTicket(true);
                        bucketRepository.save(bucket);
                        System.out.println(hmap);
                        return ResponseEntity.ok().body(hmap);

                    }
                }
            }
            else {
                return ResponseEntity.badRequest().body("error: InvalidObjectName|InvalidBucket");
            }
        }
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body("error" + e);
        }
        return ResponseEntity.badRequest().body("error: InvalidObjectName|InvalidBucket");
    }


    @DeleteMapping(value = "/{bucketname}/{objectname}", params = "partNumber")
    public ResponseEntity deleteParts(
            @PathVariable(name = "bucketname") String bucketname,
            @PathVariable(name = "objectname") String objectname,
            @RequestParam(name = "partNumber") String partNumber
    ) {

        String bnl = bucketname.toLowerCase();
        try {
            Bucket bucket = bucketRepository.findOneByName(bnl);
            if (bucket != null){
                for (ObjectFile element : bucket.getObjectFileSet()) {
                    if (element.getName().equals(objectname.toLowerCase()) && element.getTicket().equals(false)){
                        if (element.getObjFileParts().get(partNumber) != null){
                            element.getObjFileParts().remove(partNumber);
                            bucketRepository.save(bucket);
                            System.out.println("delete " + objectname + " part " + partNumber);
                            return ResponseEntity.ok().body("delete " + objectname + " part " + partNumber );
                        }
                    }
                }
                System.out.println("fail to delete");
                return ResponseEntity.badRequest().body("fail to delete, part does not exist");
            }
            else {
                return ResponseEntity.badRequest().body("fail to delete");
            }
        }
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body("fail to delete: " + e);
        }

    }

    @DeleteMapping(value = "/{bucketname}/{objectname}", params = "delete")
    public ResponseEntity deleteObjectFile(
            @PathVariable(name = "bucketname") String bucketname,
            @PathVariable(name = "objectname") String objectname
    ) {

        String bnl = bucketname.toLowerCase();
        String ojn = objectname.toLowerCase();
        try {
            Bucket bucket = bucketRepository.findOneByName(bnl);
            if (bucket != null){

                for (ObjectFile element : bucket.getObjectFileSet()) {
                    if (element.getName().equals(ojn)){
                        bucket.getObjectFileSet().remove(element);
                        bucketRepository.save(bucket);
                        System.out.println("delete " + objectname);
                        return ResponseEntity.ok().body("delete " + objectname);
                    }
                }
//                bucket.getObjectFileSet().removeIf(x -> (x.getName().equals(objectname)));
//                bucketRepository.save(bucket);

            }
            return ResponseEntity.badRequest().body("fail to delete");
        }
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body("fail to delete: " + e);
        }

    }



}
