package com.minhden.minigraph.kernel.storage;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class HeapPage implements Page {
    final HeapPageId pid;
    int recordLength;
    byte[] data;
    int pinCount;
    boolean isDirty;
    boolean[] usedSlot;
    int numSlots;
    Record[] records;

    public HeapPage(HeapPageId pid, byte[] data) {
        this.pid = pid;
        this.data = data;
        this.pinCount = 0;
        this.isDirty = false;
        ByteBuffer buffer = ByteBuffer.wrap(data);
        switch (this.pid.serialize()[0]) {
            case 1:
                this.recordLength = NodeRecord.RECORD_LENGTH;
                this.numSlots = data.length / recordLength;
                this.records = new NodeRecord[numSlots];
                for (int i = 0; i < numSlots; i++) {
                    byte[] bytes = new byte[NodeRecord.RECORD_LENGTH];
                    buffer.get(bytes, 0, NodeRecord.RECORD_LENGTH);
                    records[i] = NodeRecord.fromByteArray(bytes);
                }
                break;
            case 2:
                // TODO: implement
                break;
            case 3:
                // TODO: implement
                break;
        }
        this.usedSlot = new boolean[numSlots];
    }

    /**
     * Get the first unused slot for inserting record. Return -1 if no more empty slot.
     * @return first unused slot index.
     */
    public int getFirstUnusedSlot() {
        for (int i = 0; i < usedSlot.length; i++) {
            if (!usedSlot[i]) return i;
        }

        return -1;
    }

    @Override
    public PageId getId() {
        return pid;
    }

    @Override
    public byte[] getPageData() {
        return data;
    }

    @Override
    public void setPageData(byte[] data) {
        this.data = data;
    }

    @Override
    public int pinCount() {
        return this.pinCount;
    }

    @Override
    public void unPin() {
        this.pinCount--;
    }

    @Override
    public void pin() {
        this.pinCount++;
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void setDirty(boolean b) {
        isDirty = b;
    }

    @Override
    public void insertRecord(Record record) {
        // Make changes to in-memory data structure
        if (record.getFileType() != this.pid.serialize()[0]) {
            throw new IllegalArgumentException("Invalid type of record insertion");
        }
        int slot = getFirstUnusedSlot();
        if (slot < 0) {
            throw new IllegalArgumentException("Page has no empty slot for record.");
        }
        record.setSlotNumber(slot);
        this.records[slot] = record;
        this.usedSlot[slot] = true;
        setDirty(true);

        // Make changes to the byte array
        int offset = slot * recordLength;
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.put(offset, record.toByteArray(), 0, recordLength);
        setPageData(buffer.array());
    }

    @Override
    public void deleteRecord(Record record) {
        // Make changes to in-memory structure
        int slot = record.getSlotNumber();
        records[slot] = null;
        usedSlot[slot] = false;
        setDirty(true);

        // Make changes to byte array
        int offset = slot * recordLength;
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.put(offset, new byte[recordLength], 0, recordLength);
        setPageData(buffer.array());
    }

    @Override
    public Record getRecord(int recordSlot) {
        return records[recordSlot];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("File: ").append(getId().getFileName()).append("\n");
        sb.append("Page number: ").append(getId().getPageNumber()).append("\n");
        for (int i = 0; i < numSlots; i++) {
            if (records[i] == null) {
                sb.append("Empty slot ...").append("\n");
            } else {
                sb.append(records[i].toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
