package com.minhden.minigraph.kernel.storage;

import java.util.Objects;

public class HeapPageId implements PageId {

    private String fileName;
    private int pageNumber;

    public HeapPageId(String fileName, int pageNumber) {
        this.fileName = fileName;
        this.pageNumber = pageNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public int getPageNumber() {
        return pageNumber;
    }

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
    public int[] serialize() {
        int[] data = new int[2];
        if (fileName.equals("properties.minig")) {
            data[0] = 2;
        } else if (fileName.equals("edges.minig")) {
            data[0] = 1;
        }
        data[1] = getPageNumber();

        return data;
    }
}
