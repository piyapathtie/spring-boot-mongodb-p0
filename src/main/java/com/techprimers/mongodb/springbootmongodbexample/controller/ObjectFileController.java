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
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
            String objuuid = "";
            Bucket bucket = bucketRepository.findOneByName(bucketname.toLowerCase());
            System.out.println(objectname);
            for (ObjectFile element : bucket.getObjectFileSet()) {
                System.out.println(element.getUuid());
                if (element.getName().equals(objectname)){
                    objectFile = element;
                    objuuid = element.getUuid();

                    bucket.getObjectFileSet();
                    String path = "storage/" + bucket.getUuid() + "/" + objuuid + "/" + objectname.toLowerCase() + "-" + partNumber;
                    ObjectFileParts objectFileParts = new ObjectFileParts(objectname.toLowerCase(), ContentMD5, ContentLength, partNumber);
                    objectFile.getObjFileParts().put(partNumber, objectFileParts);
                    upload(request, path);

                    bucketRepository.save(bucket);
                    System.out.println("done");
                    return ResponseEntity.ok().body("");

                }
            }
            return ResponseEntity.badRequest().body("");
        }
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body("");
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

//
//    @GetMapping(value = "/{bucketname}", params = "list")
//    public ResponseEntity listObjectInBucket(@PathVariable(name = "bucketname") String bucketname) {
//
//        try {
//            Bucket bucket = bucketRepository.findOneByName(bucketname);
//            if (bucket != null){
//                System.out.println(bucketRepository.findAll());
//
//                return ResponseEntity.ok().body("list " + bucketname );
//            }
//            else {
//                return ResponseEntity.badRequest().body("fail to list, bucket name does not exist ");
//            }
//        }
//        catch (Exception e){
//            System.out.println(e);
//            return ResponseEntity.badRequest().body("fail to list " + bucketname);
//        }
//
//    }



}
