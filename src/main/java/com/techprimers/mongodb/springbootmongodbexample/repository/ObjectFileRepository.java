package com.techprimers.mongodb.springbootmongodbexample.repository;

import com.techprimers.mongodb.springbootmongodbexample.document.Bucket;
import com.techprimers.mongodb.springbootmongodbexample.document.ObjectFile;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ObjectFileRepository extends MongoRepository<ObjectFile, Integer> {
    Bucket findOneByName(String bucketname);
    Bucket deleteBucketByName(String bucketname);
}