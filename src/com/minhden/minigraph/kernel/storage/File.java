package com.minhden.minigraph.kernel.storage;

public interface File {
    /**
     * Read the specific page from disk.
     * @param pageId PageId instance of the Page to be read.
     * @return Page instance
     */
    Page read(PageId pageId);

    /**
     * Write the specific Page to disk.
     * @param page Page to be written.
     */
    void flush(Page page);

    /**
     * Get the file name of this file.
     * @return File name
     */
    String getFileName();
}
