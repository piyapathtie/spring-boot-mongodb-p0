package com.techprimers.mongodb.springbootmongodbexample.controller;

import com.techprimers.mongodb.springbootmongodbexample.document.Bucket;
import com.techprimers.mongodb.springbootmongodbexample.repository.BucketRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;


@RestController
public class BucketController {

    @Autowired
    private BucketRepository bucketRepository;

    @PostMapping(value = "/{bucketname}", params = "create")
    public ResponseEntity createBucket(@PathVariable(name = "bucketname") String bucketname) {

        try {

            if (bucketRepository.findOneByName(bucketname) == null){
                String path = "storage/" + bucketname;
                File theDir = new File(path);
                theDir.mkdir();

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();

                Long create = Instant.now().toEpochMilli();

                Bucket bucket = new Bucket(bucketname, randomUUIDString, create, create);

                bucketRepository.save(bucket);

                return ResponseEntity.ok().body(bucket);
            }
            else {
                return ResponseEntity.badRequest().body("fail to create " + bucketname + ", name already exist");
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("fail to create " + bucketname);
        }

    }

    @DeleteMapping(value = "/{bucketname}", params = "delete")
    public ResponseEntity deleteBucket(@PathVariable(name = "bucketname") String bucketname) {

        try {
            Bucket bucket = bucketRepository.findOneByName(bucketname);
            if (bucket != null){
                bucketRepository.delete(bucket);
                System.out.println("here");
                String path = "storage/" + bucketname;
                File theDir = new File(path);
                FileUtils.deleteDirectory(theDir);

                System.out.println("hello");



                System.out.println("here 2 ");

                return ResponseEntity.ok().body("delete " + bucketname );
            }
            else {
                return ResponseEntity.badRequest().body("fail to delete, bucket name does not exist ");
            }
        }
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body("fail to delete " + bucketname);
        }

//        return ResponseEntity.badRequest().body("fail to create ");

    }


}
