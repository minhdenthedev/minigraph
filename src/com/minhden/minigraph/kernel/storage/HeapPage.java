package com.minhden.minigraph.kernel.storage;

public class HeapPage implements Page {
    final HeapPageId pid;
    byte[] header;
    int numSlots;


    public HeapPage(HeapPageId pid, byte[] data) {
        this.pid = pid;

    }

    @Override
    public PageId getId() {
        return null;
    }

    @Override
    public byte[] getPageData() {
        return new byte[0];
    }

    @Override
    public void setPageData(byte[] data) {

    }
}
