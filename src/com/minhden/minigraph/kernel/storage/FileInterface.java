package com.minhden.minigraph.kernel.storage;

import java.util.Iterator;

/**
 * Interface for interacting with logical file on disk.
 */
public interface FileInterface {
    /**
     * Read the specific page from disk.
     * @param pageId PageId instance of the Page to be read.
     * @return Page instance
     */
    Page readPage(PageId pageId);

    /**
     * Write the specific Page to disk.
     * @param page Page to be written.
     */
    void flushPage(Page page);

    /**
     * Get the file name of this file.
     * @return File name
     */
    String getFileName();

    /**
     * Destroy this file
     */
    void destroy();

    int getNumberOfPages();

    void insertRecord(Record record);

    void deleteRecord(Record record);

    Record getRecord(PageId pageId, int recordSlot);

    Iterator<Record> iterator();
}
