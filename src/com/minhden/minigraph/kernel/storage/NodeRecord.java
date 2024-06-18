package com.minhden.minigraph.kernel.storage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class NodeRecord implements Record{
    public static final int STRING_MAX_LENGTH = 128;
    private static final int STRING_SIZE = STRING_MAX_LENGTH * Character.BYTES;
    public static int RECORD_LENGTH = 4 * Integer.BYTES + STRING_SIZE;

    int fileType;
    int pageNumber;
    int recordSlot;
    int nodeId;
    String nodeName;

    public NodeRecord(int nodeId, String nodeName) {
        this.fileType = 1;
        this.nodeId = nodeId;
        if (nodeName.length() > STRING_MAX_LENGTH) {
            throw new IllegalArgumentException("Node name has maximum of 128 characters.");
        }
        RECORD_LENGTH = getRecordLength();
    }

    public NodeRecord(int pageNumber, int recordSlot, int nodeId, String nodeName) {
        this.fileType = 1;
        this.pageNumber = pageNumber;
        this.recordSlot = recordSlot;
        this.nodeId = nodeId;
        if (nodeName.length() > STRING_MAX_LENGTH) {
            throw new IllegalArgumentException("Node name has maximum of 128 characters.");
        }
        this.nodeName = nodeName;
        RECORD_LENGTH = getRecordLength();
    }

    public NodeRecord(PageId pageId, int recordSlot, int nodeId, String nodeName) {
        this.nodeId = nodeId;
        this.fileType = pageId.serialize()[0];
        this.pageNumber = pageId.serialize()[1];
        this.recordSlot = recordSlot;
        if (nodeName.length() > STRING_MAX_LENGTH) {
            throw new IllegalArgumentException("Node name has maximum of 128 characters.");
        }
        this.nodeName = nodeName;
        RECORD_LENGTH = getRecordLength();
    }

    public NodeRecord(PageId pageId, int nodeId, String nodeName) {
        this.nodeId = nodeId;
        this.fileType = pageId.serialize()[0];
        this.pageNumber = pageId.serialize()[1];
        if (nodeName.length() > STRING_MAX_LENGTH) {
            throw new IllegalArgumentException("Node name has maximum of 128 characters.");
        }
        this.nodeName = nodeName;
        RECORD_LENGTH = getRecordLength();
    }

    public int getRecordLength() {
        return 4 * Integer.BYTES + STRING_SIZE;
    }

    @Override
    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getRecordLength());
        buffer.putInt(this.pageNumber);
        buffer.putInt(this.recordSlot);
        buffer.putInt(this.nodeId);
        byte[] stringBytes = this.nodeName.getBytes(StandardCharsets.UTF_16);
        buffer.put(stringBytes);
        // Padding if string is shorter than max length in order to align other fields
        if (stringBytes.length < STRING_SIZE) {
            buffer.put(new byte[STRING_SIZE - stringBytes.length]);
        }

        return buffer.array();
    }

    @Override
    public Record fromBytes(byte[] bytes) {
        return NodeRecord.fromByteArray(bytes);
    }

    private static boolean legitBytesIn(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                return true;
            }
        }

        return false;
    }

    public static NodeRecord fromByteArray(byte[] bytes) {
        if (bytes.length != RECORD_LENGTH) {
            System.out.println(bytes.length);
            System.out.println(RECORD_LENGTH);
            throw new IllegalArgumentException("Invalid bytes array length");
        }

        if (legitBytesIn(bytes)) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            int pageNumber = buffer.getInt();
            int recordSlot = buffer.getInt();
            int nodeId = buffer.getInt();
            byte[] stringBytes = new byte[STRING_SIZE];
            buffer.get(stringBytes);
            String nodeName = new String(stringBytes, StandardCharsets.UTF_16).trim();
            return new NodeRecord(pageNumber, recordSlot, nodeId, nodeName);
        } else return null;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getSlotNumber() {
        return recordSlot;
    }

    @Override
    public void setSlotNumber(int slot) {
        this.recordSlot = slot;
    }

    @Override
    public int getFileType() {
        return fileType;
    }

    @Override
    public String toString() {
        return "NodeRecord{" +
                "fileType=" + fileType +
                ", pageNumber=" + pageNumber +
                ", recordSlot=" + recordSlot +
                ", nodeId=" + nodeId +
                ", nodeName='" + nodeName + '\'' +
                '}';
    }
}
