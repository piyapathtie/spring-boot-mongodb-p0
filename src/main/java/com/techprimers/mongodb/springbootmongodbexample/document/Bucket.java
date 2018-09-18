package com.techprimers.mongodb.springbootmongodbexample.document;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.*;


public class Bucket {

    @Id
    private ObjectId id;
    private String name;
    private String uuid;
    private Long created;
    private Long modified;
    private Set<ObjectFile> objectFileSet;

    public Bucket(String name, String uuid, Long created, Long modified, Set<ObjectFile> objectFileSet) {
        this.name = name;
        this.uuid = uuid;
        this.created = created;
        this.modified = modified;
        this.objectFileSet = objectFileSet;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }

    public Set<ObjectFile> getObjectFileSet() {
        return objectFileSet;
    }

    public void setObjectFileSet(Set<ObjectFile> objectFileSet) {
        this.objectFileSet = objectFileSet;
    }


    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public HashMap<String, Object> list() {
        HashMap<String , Object> hmap = new HashMap<>();

        hmap.put("created", this.created);
        hmap.put("modified", modified);
        hmap.put("name", name);
        ArrayList anArray = new ArrayList();
        Iterator itr = objectFileSet.iterator();
        while(itr.hasNext()) {
            ObjectFile element = (ObjectFile) itr.next();
            anArray.add(element.list().toString());
        }
        hmap.put("objects", anArray);
        return hmap;
    }


}
