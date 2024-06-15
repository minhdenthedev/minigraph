package com.minhden.minigraph.kernel.transaction;

/**
 * This class representing requested permission to a file.
 * There are two levels that are READ_ONLY and READ_WRITE.
 */
public class Permissions {
    int permLevel;

    private Permissions(int permLevel) {
        this.permLevel = permLevel;
    }

    @Override
    public String toString() {
        if (permLevel == 0) {
            return "READ_ONLY";
        } else if (permLevel == 1) {
            return "READ_WRITE";
        } else {
            return "UNKNOWN";
        }
    }

    public static final Permissions READ_ONLY = new Permissions(0);
    public static final Permissions READ_WRITE = new Permissions(1);
}
