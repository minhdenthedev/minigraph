package com.minhden.minigraph.kernel.storage;

public interface PageId {
    /**
     * Return a representation of this PageId object as collection of integers (used for logging).
     * This class MUST have a constructor that accepts n integer parameters,
     * where n is the number of integers returned in the array from serialize.
     */
    int[] serialize();

    /**
     * The returned value of this function is used in hash table of BufferPool.
     * @return a hash code for this page
     */
    int hashCode();

    /**
     * Compares one PageId to another.
     *
     * @param o The object to be compared against (another PageId instance).
     * @return true if they are equal, and false otherwise.
     */
    boolean equals(Object o);

    int getPageNumber();

    String getFileName();
}
