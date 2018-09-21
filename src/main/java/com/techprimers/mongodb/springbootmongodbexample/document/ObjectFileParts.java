package com.techprimers.mongodb.springbootmongodbexample.document;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Set;

public class ObjectFileParts {

    @Id
    private ObjectId id;
    private String name;
    private String md5;
    private Long length;
    private String partNumber;
    private String fileType;

    public ObjectFileParts(String name, String md5, long length, String partNumber) {
        this.name = name;
        this.md5 = md5;
        this.length = length;
        this.partNumber = partNumber;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
