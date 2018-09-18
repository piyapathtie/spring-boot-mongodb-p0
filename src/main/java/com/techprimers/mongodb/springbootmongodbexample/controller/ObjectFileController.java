package com.techprimers.mongodb.springbootmongodbexample.controller;

import com.techprimers.mongodb.springbootmongodbexample.document.Bucket;
import com.techprimers.mongodb.springbootmongodbexample.document.ObjectFile;
import com.techprimers.mongodb.springbootmongodbexample.repository.BucketRepository;
import com.techprimers.mongodb.springbootmongodbexample.repository.ObjectFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.Instant;
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
        try {
            if (bucketRepository.findOneByName(bnl) != null ){
                if (objectFileRepository.findOneByName(onl) == null){
                    String path = "storage/" + bnl + "/"+ onl;
                    File theDir = new File(path);
                    theDir.mkdir();

                    UUID uuid = UUID.randomUUID();
                    String randomUUIDString = uuid.toString();

                    Long create = Instant.now().toEpochMilli();

                    ObjectFile objectFile = new ObjectFile(onl, randomUUIDString, create, create, false);

                    Bucket bucket = bucketRepository.findOneByName(bnl);
                    bucket.setModified(create);
                    Set<ObjectFile> objectFileSet = bucket.getObjectFileSet();
                    objectFileSet.add(objectFile);
                    bucket.setObjectFileSet(objectFileSet);

                    bucketRepository.save(bucket);
                    objectFileRepository.save(objectFile);

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
//
//    @DeleteMapping(value = "/{bucketname}", params = "delete")
//    public ResponseEntity deleteBucket(@PathVariable(name = "bucketname") String bucketname) {
//
//        try {
//            Bucket bucket = bucketRepository.findOneByName(bucketname);
//            if (bucket != null){
//                bucketRepository.delete(bucket);
//                String path = "storage/" + bucketname;
//                File theDir = new File(path);
//                FileUtils.deleteDirectory(theDir);
//
//                return ResponseEntity.ok().body("delete " + bucketname );
//            }
//            else {
//                return ResponseEntity.badRequest().body("fail to delete, bucket name does not exist ");
//            }
//        }
//        catch (Exception e){
//            System.out.println(e);
//            return ResponseEntity.badRequest().body("fail to delete " + bucketname);
//        }
//
////        return ResponseEntity.badRequest().body("fail to create ");
//
//    }
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
