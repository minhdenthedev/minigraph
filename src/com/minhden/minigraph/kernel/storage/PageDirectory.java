package com.minhden.minigraph.kernel.storage;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PageDirectory {
    public static final int DIRECTORY_ENTRY_SIZE = 2 * Integer.BYTES;
    public static final int NUMBER_ENTRIES_DIRECTORY = 100;
    /**
     * 100 pages per directory. If one directory is full, create new directory. The last 8-byte
     * is the pointer (byte offset) to the next directory.
     */
    public static final int DIRECTORY_SIZE =
            Integer.BYTES + NUMBER_ENTRIES_DIRECTORY * DIRECTORY_ENTRY_SIZE + Integer.BYTES;

    int numPages;
    int[][] directory;
    int nextDirectoryOffset;

    public PageDirectory(byte[] data) {
        if (data.length != DIRECTORY_SIZE) {
            throw new IllegalArgumentException("Invalid data for directory of pages.");
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        this.numPages = buffer.getInt();
        this.directory = new int[NUMBER_ENTRIES_DIRECTORY][2];
        for (int i = 0; i < NUMBER_ENTRIES_DIRECTORY; i++) {
            int pageOffset = buffer.getInt();
            int freeSlots = buffer.getInt();
            directory[i][0] = pageOffset;
            directory[i][1] = freeSlots;
        }
        this.nextDirectoryOffset = buffer.getInt();
    }

    public int[] getFirstAvailablePage() {
        if (isFull()) {
            return null;
        }
        int[] result = new int[2];
        for (int i = 0; i < NUMBER_ENTRIES_DIRECTORY; i++) {
            if (directory[i][1] == 0) {
                result[0] = i;
                result[1] = directory[i][0];
                return result;
            }
        }

        return null;
    }

    public int getPageOffset(int pageNumber) {
        return directory[pageNumber][0];
    }

    public void setPageOffset(int pageNumber, int offset) {
        directory[pageNumber][0] = offset;
    }

    public int[][] getDirectory() {
        return directory;
    }

    public boolean isFull() {
        return numPages == NUMBER_ENTRIES_DIRECTORY;
    }

    public int getNumPages() {
        return this.numPages;
    }

    public int getNextDirectoryOffset() {
        return this.nextDirectoryOffset;
    }

    public void setNextDirectoryOffset(int offset) {
        this.nextDirectoryOffset = offset;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(DIRECTORY_SIZE);
        buffer.putInt(numPages);
        for (int i = 0; i < NUMBER_ENTRIES_DIRECTORY; i++) {
            buffer.putInt(directory[i][0]);
            buffer.putInt(directory[i][1]);
        }
        buffer.putInt(nextDirectoryOffset);
        return buffer.array();
    }

    public static List<PageDirectory> createList(byte[] data) {
        List<PageDirectory> directories = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte[] bytes = buffer.slice(0, DIRECTORY_SIZE).array();
        PageDirectory head = new PageDirectory(bytes);
        directories.add(head);
        while (head.getNextDirectoryOffset() != 0) {
            bytes = buffer.slice(head.getNextDirectoryOffset(), DIRECTORY_SIZE).array();
            head = new PageDirectory(bytes);
            directories.add(head);
        }

        return directories;
    }

    @Override
    public String toString() {
        return "PageDirectory{" +
                "nextDirectoryOffset=" + nextDirectoryOffset +
                ", numPages=" + numPages +
                '}';
    }
}
