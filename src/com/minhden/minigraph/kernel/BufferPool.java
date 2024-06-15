package com.minhden.minigraph.kernel;

import com.minhden.minigraph.kernel.storage.Page;
import com.minhden.minigraph.kernel.storage.PageId;
import com.minhden.minigraph.kernel.transaction.Permissions;
import com.minhden.minigraph.kernel.transaction.TransactionId;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class BufferPool {

    private static final Logger logger = Logger.getLogger(BufferPool.class.getName());

    private static final int PAGE_SIZE = 4096;

    private static int pageSize = PAGE_SIZE;

    /**
     * Default number of pages passed to the constructor. This is used by
     * other classes. BufferPool should use the numPages argument to the
     * constructor instead.
     */
    public static final int DEFAULT_PAGES = 50;

    private HashMap<PageId, Page> bufferPoolHashMap;

    /**
     * Create a BufferPool that caches up to numPages pages.
     * @param numPages number of pages to be cached.
     */
    public BufferPool(int numPages) {
        bufferPoolHashMap = new HashMap<>(numPages);
    }

    public static int getPageSize() {
        return pageSize;
    }

    // ONLY USE FOR TESTING
    public static void setPageSize(int pageSize) {
        BufferPool.pageSize = pageSize;
    }

    // ONLY USE FOR TESTING
    public static void resetPageSize(int pageSize) {
        BufferPool.pageSize = PAGE_SIZE;
    }

    /**
     * Get the specific page with the associated permission.
     * Will acquire a lock and may block if that lock is held by another transaction.
     * <p>The retrieved page should be looked up in the buffer pool first. If presents,
     * it will be returned. Otherwise, it should be added to the buffer pool and returned.
     * If there is no more space for new page, one page is evicted.</p>
     * @param tid the ID of the Transaction requesting the Page
     * @param pid the PageId requested.
     * @param perm requested Permissions on this page.
     * @return Page instance
     */
    public Page getPage(TransactionId tid, PageId pid, Permissions perm) {
        if (!(bufferPoolHashMap.containsKey(pid))) {
            if (bufferPoolHashMap.size() >= getPageSize()) {

            }
            return null;
        } else {
            return bufferPoolHashMap.get(pid);
        }
    }

    /**
     * Release the lock on a Page.
     * Calling this is very risky and may result in wrong behavior.
     * @param tid the ID of the transaction requesting the lock.
     * @param pid the ID of the Page to unlock.
     */
    public void releasePage(TransactionId tid, PageId pid) {
        logger.info(tid + "released lock on " + pid);
    }

    /**
     * @param tid TransactionId
     * @param pid PageId
     * @return true if the specified transaction has a lock on the specific page
     */
    public boolean holdsLock(TransactionId tid, PageId pid) {
        // TODO: implement
        return false;
    }

    /**
     * Release all locks associated with a given transaction.
     * @param tid the ID of the Transaction holding the locks.
     * @throws IOException not defined
     */
    public void transactionComplete(TransactionId tid) throws IOException {
        // TODO: implement
    }

    /**
     * Commit or abort a given transaction; release all locks associated to
     * the transaction.
     *
     * @param tid the ID of the transaction requesting unlock
     * @param commit a flag indicating whether we should commit or abort
     */
    public void transactionComplete(TransactionId tid, boolean commit)
            throws IOException {
        // TODO: implement
    }

    /**
     * Add a tuple to the specified table on behalf of transaction tid.  Will
     * acquire a write lock on the page the tuple is added to and any other
     * pages that are updated. May block if the lock(s) cannot be acquired.
     * Marks any pages that were dirtied by the operation as dirty by calling
     * their markDirty bit, and adds versions of any pages that have
     * been dirtied to the cache (replacing any existing versions of those pages) so
     * that future requests see up-to-date pages.
     *
     * @param tid the transaction adding the tuple
     * @param tableId the table to add the tuple to
     * @param t the tuple to add
     */
    public void insertTuple(TransactionId tid, int tableId, Record t) {
        // some code goes here
        // not necessary for lab1
    }

    private synchronized void evictPage() {
        logger.info("Evicted a page.");
        // TODO: implement
    }
}
