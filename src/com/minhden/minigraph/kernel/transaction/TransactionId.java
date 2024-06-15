package com.minhden.minigraph.kernel.transaction;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class contains the identifier of a transaction.
 * The ID is auto-incremented as new Transaction instance is being instantiated.
 */
public class TransactionId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    static AtomicLong counter = new AtomicLong(0);
    final long id;

    public TransactionId() {
        id = counter.getAndIncrement();
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof TransactionId that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
