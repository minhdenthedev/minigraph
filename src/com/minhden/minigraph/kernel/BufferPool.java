package com.minhden.minigraph.kernel;

import com.minhden.minigraph.kernel.storage.HeapPage;
import com.minhden.minigraph.kernel.storage.Page;
import com.minhden.minigraph.kernel.storage.PageId;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BufferPool {
    HashMap<PageId, Page> buffer;
    public static final int PAGE_SIZE = 4096;
    private int pageSize = PAGE_SIZE;

    public static final int DEFAULT_PAGES = 100;
    private int numPages = DEFAULT_PAGES;

    private Queue<PageId> lruQueue;

    public BufferPool(int pageSize, int numPages) {
        this.pageSize = pageSize;
        this.buffer = new HashMap<>(numPages);
        lruQueue = new LinkedList<>();
    }

    public BufferPool(int numPages) {
        this.buffer = new HashMap<>(numPages);
        lruQueue = new LinkedList<>();
    }

    public BufferPool() {
        this.buffer = new HashMap<>(DEFAULT_PAGES);
        lruQueue = new LinkedList<>();
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getNumPages() {
        return numPages;
    }

    public void releasePage(PageId pageId) {
        Page page = buffer.get(pageId);
        page.unPin();
        if (page.pinCount() == 0) {
            lruQueue.offer(pageId);
        }
    }

    public Page getPage(PageId pageId) {
        Page page = buffer.get(pageId);
        if (page == null) {
            if (buffer.size() == numPages) {
                evictPage();
            }
            page = fetchPage(pageId);
            buffer.put(pageId, page);
            page.pin();
            lruQueue.remove(pageId);
        } else {
            page.pin();
            lruQueue.remove(pageId);
        }

        return page;
    }

    private Page fetchPage(PageId pageId) {
        // TODO: implement
        return null;
    }

    private void evictPage() {
        // TODO: implement
        // First have to check whether the page has any pin
    }

    private void flushPage(PageId pageId) {
        // TODO: implement
        // Use when evicting "dirty" page.
    }
}
