package com.minhden.minigraph.entities;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEntity {
    int internalId;
    String id;
    String name;
    Map<Object, Object> properties;

    public AbstractEntity(int internalId, String name) {
        this.internalId = internalId;
        if (name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("Must define name for entity.");
        }
        this.name = name;
        this.properties = new HashMap<>();
    }

    public int getInternalId() {
        return internalId;
    }

    public void setInternalId(int internalId) {
        this.internalId = internalId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Object, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<Object, Object> properties) {
        this.properties = properties;
    }

    public void addProperty(Object key, Object value) throws IllegalArgumentException {
        if (this.properties.containsKey(key))  {
            throw new IllegalArgumentException("This property has already exists.");
        }
        this.properties.put(key, value);
    }

    public void removeProperty(Object key, Object value) {
        this.properties.remove(key);
    }

    public void updateProperty(Object key, Object newValue) throws IllegalArgumentException {
        if (!this.properties.containsKey(key))  {
            throw new IllegalArgumentException("This property is not exists.");
        }
        this.properties.put(key, newValue);
    }
}
