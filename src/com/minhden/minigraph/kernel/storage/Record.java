package com.minhden.minigraph.kernel.storage;

public interface Record {

    /**
     * Convert record to bytes array for optimized write operation.
     * @return bytes array
     */
    byte[] toByteArray();

    /**
     *
     * @return the length of this type of record (in bytes).
     */
    int getRecordLength();

    Record fromBytes(byte[] bytes);

    int getPageNumber();
    int getSlotNumber();
    void setSlotNumber(int slot);
    int getFileType();
}
