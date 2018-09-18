package com.techprimers.mongodb.springbootmongodbexample.controller;

import com.techprimers.mongodb.springbootmongodbexample.document.Bucket;
import com.techprimers.mongodb.springbootmongodbexample.repository.ObjectFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.Instant;
import java.util.UUID;

@RestController
public class ObjectFileController {

    @Autowired
    private ObjectFileRepository objectFileRepository;

    @PostMapping(value = "/{bucketname}/{objectname}", params = "create")
    public ResponseEntity createObject(@PathVariable(name = "bucketname") String bucketname,
                                       @PathVariable(name = "objectname") String objectname
                                       ) {

//        try {
//
//            if (objectFileRepository.findOneByName(bucketname) == null){
//                String path = "storage/" + bucketname + objectname;
//                File theDir = new File(path);
//                theDir.mkdir();
//
//                UUID uuid = UUID.randomUUID();
//                String randomUUIDString = uuid.toString();
//
//                Long create = Instant.now().toEpochMilli();
//
//                Object object = new Bucket(bucketname, randomUUIDString, create, create);
//
//                objectFileRepository.save(object);
//
//                return ResponseEntity.ok().body(object);
//            }
//            else {
//                return ResponseEntity.badRequest().body("fail to create " + bucketname + ", name already exist");
//            }
//        }
//        catch (Exception e){
//            return ResponseEntity.badRequest().body("fail to create " + bucketname);
//        }
        return ResponseEntity.ok().body("");

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
