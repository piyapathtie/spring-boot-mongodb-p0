package com.techprimers.mongodb.springbootmongodbexample.document;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Set;

public class ObjectFile {

    @Id
    private ObjectId id;
    private String name;
    private String uuid;
    private Long created;
    private Long modified;
    private Boolean ticket;
    private String fileType;
    private HashMap objFileParts;
    private HashMap metaData;

    public ObjectFile(String name, String uuid, Long created, Long modified, Boolean ticket, HashMap objFileParts, HashMap metaData) {
        this.name = name;
        this.uuid = uuid;
        this.created = created;
        this.modified = modified;
        this.ticket = ticket;
        this.objFileParts = objFileParts;
        this.metaData = metaData;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getModified() {
        return modified;
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }

    public Boolean getTicket() {
        return ticket;
    }

    public void setTicket(Boolean ticket) {
        this.ticket = ticket;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public HashMap getObjFileParts() {
        return objFileParts;
    }

    public void setObjFilePair(HashMap objFileParts) {
        this.objFileParts = objFileParts;
    }

    public HashMap<String, Object> list() {
        HashMap<String , Object> hmap = new HashMap<>();
        hmap.put("name", name);
        hmap.put("created", created);
        hmap.put("modified", modified);

        return hmap;
    }


    public HashMap getMetaData() {
        return metaData;
    }

    public void setMetaData(HashMap metaData) {
        this.metaData = metaData;
    }

}
