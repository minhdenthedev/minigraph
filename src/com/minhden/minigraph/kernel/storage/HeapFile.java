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
            byte[] directory = new byte[PageDirectory.DIRECTORY_SIZE];
            this.file.readFully(directory);
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
            this.file.write(buffer.array());

            // Update pointer of the last directory
            long lastPointerPosition = length - PageDirectory.DIRECTORY_SIZE - BufferPool.PAGE_SIZE;
            this.file.seek(lastPointerPosition);
            this.file.writeInt((int) length);
            this.file.seek(lastPointerPosition);
            return this.file.readInt();
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
                offset += PageDirectory.DIRECTORY_SIZE;

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

    public Page allocateNewPage() {
        this.numberOfPages++;
        int directoryOffset = getFirstAvailableDirectory();
        try {
            this.file.seek(directoryOffset);
            byte[] data = new byte[PageDirectory.DIRECTORY_SIZE];
            this.file.readFully(data);
            PageDirectory pageDirectory = new PageDirectory(data);
            int[][] directory = pageDirectory.getDirectory();
            for (int i = 0; i < directory.length; i++) {
                if (directory[i][0] == 0) {
                    if (i == 0) {
                        directory[i][0] = directoryOffset + PageDirectory.DIRECTORY_SIZE;
                    } else {
                        directory[i][0] = directory[i - 1][0] + BufferPool.PAGE_SIZE;
                    }
                    directory[i][1] = 0;
                    break;
                }
                System.out.println(Arrays.toString(directory[i]));
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page readPage(PageId pageId) {
        return null;
    }

    @Override
    public int getNumberOfPages() {
        return this.numberOfPages;
    }

    @Override
    public void flushPage(Page page) {

    }

    /**
     * Get the first page with enough space required.
     * @param spaceRequired the required space
     * @return index of the first page with enough space. Return -1 if not found any. (full)
     */
    private int getFirstEnoughSpacePage(int spaceRequired) {
        return 0;
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
