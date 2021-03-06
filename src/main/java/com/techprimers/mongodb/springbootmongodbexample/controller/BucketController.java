package com.techprimers.mongodb.springbootmongodbexample.controller;

import com.techprimers.mongodb.springbootmongodbexample.document.Bucket;
import com.techprimers.mongodb.springbootmongodbexample.document.ObjectFile;
import com.techprimers.mongodb.springbootmongodbexample.repository.BucketRepository;
import com.techprimers.mongodb.springbootmongodbexample.repository.ObjectFileRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.Instant;
import java.util.*;


@RestController
public class BucketController {

    @Autowired
    private ObjectFileRepository objectFileRepository;

    @Autowired
    private BucketRepository bucketRepository;

    @PostMapping(value = "/{bucketname}", params = "create")
    public ResponseEntity createBucket(@PathVariable(name = "bucketname") String bucketname) {

        String bnl = bucketname.toLowerCase();
        try {
            if (bucketRepository.findOneByName(bnl) == null){
                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();

                String path = "storage/" + randomUUIDString;
                File theDir = new File(path);
                theDir.mkdir();

                Long create = Instant.now().toEpochMilli();
                Set<ObjectFile> objectFileSet = Collections.emptySet();
                Bucket bucket = new Bucket(bnl, randomUUIDString, create, create, objectFileSet);

                bucketRepository.save(bucket);

                return ResponseEntity.ok().body(bucket);
            }
            else {
                return ResponseEntity.badRequest().body("fail to create " + bnl + ", name already exist");
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("fail to create " + bnl);
        }

    }

    @DeleteMapping(value = "/{bucketname}", params = "delete")
    public ResponseEntity deleteBucket(@PathVariable(name = "bucketname") String bucketname) {

        String bnl = bucketname.toLowerCase();
        try {
            Bucket bucket = bucketRepository.findOneByName(bnl);

            if (bucket != null){
//                for (ObjectFile element : bucket.getObjectFileSet()) {
//                    String n = element.list().get("name").toString().toLowerCase();
//                    ObjectFile objectFile = objectFileRepository.findOneByName(n);
//                    objectFileRepository.delete(objectFile);
//                }
                bucketRepository.delete(bucket);
//                String path = "storage/" + bnl;
//                File theDir = new File(path);
//                FileUtils.deleteDirectory(theDir);

                return ResponseEntity.ok().body("delete " + bnl );
            }
            else {
                return ResponseEntity.badRequest().body("fail to delete, bucket name does not exist ");
            }
        }
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body("fail to delete " + bnl);
        }

    }

    @GetMapping(value = "/{bucketname}", params = "list")
    public ResponseEntity listObjectInBucket(@PathVariable(name = "bucketname") String bucketname) {

        String bnl = bucketname.toLowerCase();
        try {
            Bucket bucket = bucketRepository.findOneByName(bnl);
            if (bucket != null){

//                System.out.println(bucket.list().toString());

                return ResponseEntity.ok().body(bucket.list().toString());
            }
            else {
                return ResponseEntity.badRequest().body("fail to list, bucket name does not exist ");
            }
        }
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body("fail to list " + bucketname);
        }

    }


}
