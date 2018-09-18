package com.techprimers.mongodb.springbootmongodbexample.repository;

import com.techprimers.mongodb.springbootmongodbexample.document.Bucket;
import com.techprimers.mongodb.springbootmongodbexample.document.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BucketRepository extends MongoRepository<Bucket, Integer> {
    Bucket findOneByName(String bucketname);
    Bucket deleteBucketByName(String bucketname);
}
