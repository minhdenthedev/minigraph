package com.minhden.minigraph.kernel.storage;

import java.util.Objects;

public record HeapPageId(String fileName, int pageNumber) implements PageId {
    public static final int NODES_FILE = 1;
    public static final int EDGES_FILE = 2;
    public static final int PROPS_FILE = 3;

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + fileName.hashCode();
        hash = hash * 31 + Integer.hashCode(pageNumber);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeapPageId that)) return false;
        return pageNumber == that.pageNumber && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public int[] serialize() {
        int[] data = new int[2];
        if (fileName.equals("properties.minig")) {
            data[0] = PROPS_FILE;
        } else if (fileName.equals("edges.minig")) {
            data[0] = EDGES_FILE;
        } else {
            data[0] = NODES_FILE;
        }
        data[1] = pageNumber();

        return data;
    }
}
