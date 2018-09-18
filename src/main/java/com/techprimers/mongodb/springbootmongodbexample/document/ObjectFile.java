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
    private Set<String> objFilePair;

    public ObjectFile(String name, String uuid, Long created, Long modified, Boolean ticket) {
        this.name = name;
        this.uuid = uuid;
        this.created = created;
        this.modified = modified;
        this.ticket = ticket;
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

    public Set<String> getObjFilePair() {
        return objFilePair;
    }

    public void setObjFilePair(Set<String> objFilePair) {
        this.objFilePair = objFilePair;
    }

    public HashMap<String, Object> list() {
        HashMap<String , Object> hmap = new HashMap<>();
        hmap.put("name", name);
        hmap.put("created", created);
        hmap.put("modified", modified);

        return hmap;
    }

}
