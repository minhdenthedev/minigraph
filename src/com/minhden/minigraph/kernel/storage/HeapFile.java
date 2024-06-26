package com.minhden.minigraph.kernel.storage;

import com.minhden.minigraph.kernel.BufferPool;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Logger;

public class HeapFile implements FileInterface {
    private static final Logger logger = Logger.getLogger(HeapFile.class.getName());

    RandomAccessFile file;
    String fileName;
    int numberOfPages;

    public HeapFile(String fileName) {
        this.fileName = fileName;
        try {
            this.file = new RandomAccessFile(fileName, "rw");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HeapFile createNewFile(String fileName) {
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            // New file always has 0 page inside and have 1 directory
            ByteBuffer buffer = ByteBuffer.allocate(PageDirectory.DIRECTORY_SIZE);
            buffer.putInt(0);
            raf.write(buffer.array());
            return new HeapFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int createNewDirectory() {
        // New directory is automatically append to the end of the file.
        try {
            // Add new directory
            long length = this.file.length();
            this.file.seek(length);
            ByteBuffer buffer = ByteBuffer.allocate(PageDirectory.DIRECTORY_SIZE);

            // Write changes to file
            this.file.write(buffer.array());

            // Update pointer of the last directory
            long lastPointerPosition = length - PageDirectory.DIRECTORY_SIZE - BufferPool.PAGE_SIZE;
            this.file.seek(lastPointerPosition);
            this.file.writeInt((int) length);
            this.file.seek(lastPointerPosition);
            return (int) length;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *
     * @return the offset of the first available directory.
     */
    private int getFirstAvailableDirectory() {
        int offset = 0;

        try {
            this.file.seek(offset);
            while (this.file.readInt() > PageDirectory.NUMBER_ENTRIES_DIRECTORY) {
                offset += PageDirectory.DIRECTORY_SIZE + BufferPool.PAGE_SIZE * PageDirectory.NUMBER_ENTRIES_DIRECTORY;

                // If this loop is the end of the file.
                if (offset == file.length()) {
                    break;
                } else {
                    this.file.seek(offset);
                }
            }
            return offset;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public HeapPage allocateNewPage() {
        int pageNumber = 0;
        int directoryOffset = getFirstAvailableDirectory();
        if (directoryOffset == -1) {
            directoryOffset = createNewDirectory();
        }
        try {
            // Seek the offset of the directory
            this.file.seek(directoryOffset);
            // Read the directory
            byte[] data = new byte[PageDirectory.DIRECTORY_SIZE];
            this.file.readFully(data);
            // Get the directory
            PageDirectory pageDirectory = new PageDirectory(data);
            // Create new page in the directory
            for (int i = 0; i < PageDirectory.NUMBER_ENTRIES_DIRECTORY; i++) {
                if (pageDirectory.getPageOffset(i) == 0) {
                    if (i == 0) {
                        pageDirectory.setPageOffset(i, directoryOffset + PageDirectory.DIRECTORY_SIZE);
                        byte[] directoryByteArray = pageDirectory.toByteArray();
                        this.file.seek(directoryOffset);
                        this.file.write(directoryByteArray);
                    } else {
                        int lastPageOffset = pageDirectory.getPageOffset(i - 1);
                        pageDirectory.setPageOffset(i, lastPageOffset + BufferPool.PAGE_SIZE);
                        byte[] directoryByteArray = pageDirectory.toByteArray();
                        this.file.seek(directoryOffset);
                        this.file.write(directoryByteArray);
                        pageNumber = i;
                    }
                    System.out.println(pageDirectory.getPageOffset(i));
                    break;
                }
            }

            HeapPageId heapPageID = new HeapPageId(fileName, pageNumber);
            this.numberOfPages++;
            return new HeapPage(heapPageID, new byte[BufferPool.PAGE_SIZE]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page readPage(PageId pageId) {
        int pageNumber = pageId.getPageNumber();
        try {
            this.file.seek((long) pageNumber * BufferPool.PAGE_SIZE + PageDirectory.DIRECTORY_SIZE);
            byte[] data = new byte[BufferPool.PAGE_SIZE];
            this.file.readFully(data);
            return new HeapPage((HeapPageId) pageId, data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getNumberOfPages() {
        return this.numberOfPages;
    }

    @Override
    public void flushPage(Page page) {
        int pageNumber = page.getId().getPageNumber();
        int directoryNumber = pageNumber / PageDirectory.NUMBER_ENTRIES_DIRECTORY;
        try {
            // Seeking page offset
            int directoryOffset = directoryNumber
                    * (PageDirectory.DIRECTORY_SIZE + BufferPool.PAGE_SIZE * PageDirectory.NUMBER_ENTRIES_DIRECTORY);
            int dataOffset = directoryOffset + PageDirectory.DIRECTORY_SIZE;
            int pageIndexInDirectory = pageNumber % PageDirectory.NUMBER_ENTRIES_DIRECTORY;
            int pageOffset = dataOffset + pageIndexInDirectory * BufferPool.PAGE_SIZE;
            this.file.seek(pageOffset);

            // Write the page
            this.file.write(page.getPageData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the first page with enough space.
     *
     */
    public Page getFirstEnoughSpacePage() {
        try {
            int directoryOffset = getFirstAvailableDirectory();
            // Seek to the directory
            this.file.seek(directoryOffset);
            byte[] directory = new byte[PageDirectory.DIRECTORY_SIZE];
            this.file.readFully(directory);
            PageDirectory pd = new PageDirectory(directory);
            int[] availablePage = pd.getFirstAvailablePage();
            int pageIndex = availablePage[0];
            int pageOffset = availablePage[1];
            if (pageOffset == -1) {
                return allocateNewPage();
            } else {
                // Get the page number
                int directoryIndex = directoryOffset /
                        (PageDirectory.DIRECTORY_SIZE + BufferPool.PAGE_SIZE * PageDirectory.NUMBER_ENTRIES_DIRECTORY);
                int pageNumber = directoryIndex * PageDirectory.NUMBER_ENTRIES_DIRECTORY + pageIndex;

                // Read the page data
                byte[] pageData = new byte[BufferPool.PAGE_SIZE];
                this.file.seek(pageOffset);
                this.file.readFully(pageData);
                HeapPageId pid = new HeapPageId(fileName, pageNumber);
                return new HeapPage(pid, pageData);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return allocateNewPage();
        }
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void insertRecord(Record record) {

    }

    @Override
    public Record getRecord(PageId pageId, int recordSlot) {
        return null;
    }

    @Override
    public void deleteRecord(Record record) {

    }

    @Override
    public Iterator<Record> iterator() {
        return null;
    }
}
