package com.minhden.minigraph.kernel.transaction;

import java.io.IOException;
import java.util.logging.Logger;

public class Transaction {
    private static final Logger logger
            = Logger.getLogger(Transaction.class.getName());

    private final TransactionId tid;

    volatile boolean started = false;

    public Transaction() {
        tid = new TransactionId();
    }

    /**
     * Start the transaction running
     */
    public void start() {
        started = true;
        logger.info("Transaction start");
        // TODO: real implement
    }

    public TransactionId getId() {
        return tid;
    }

    public void commit() throws IOException {
        transactionComplete(false);
    }

    public void abort() throws IOException {
        transactionComplete(true);
    }

    public void transactionComplete(boolean abort) {
        if (started) {
            if (abort) {
                logger.info("Transaction aborted");
                // TODO: implement rollback
            } else {
                logger.info("Transaction committed");
                // TODO: implement flush dirty pages to disk
            }

            try {
                logger.info("Release lock");
                // TODO: implement release lock
            } catch (Exception e) {
                logger.severe(e.toString());
            }
        }
    }
}
