package com.minhden.minigraph.manager;

import com.minhden.minigraph.entities.Graph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class StorageManager {

    private final static Logger logger = Logger.getLogger(StorageManager.class.getName());

    private static StorageManager instance;

    private final String storageDir;

    private List<String> databases;

    Preferences systemPrefs;

    private StorageManager() {
        this.databases = new ArrayList<>();
        this.systemPrefs = Preferences.systemRoot().node(this.getClass().getName());
        this.storageDir = System.getProperty("user.dir") + File.separator + "databases";
        File storage = new File(storageDir);
        if (!storage.exists()) {
            if (storage.mkdirs()) {
                logger.log(Level.INFO, "Successfully created storage directory.");
            } else {
                logger.log(Level.SEVERE, "Failed to create storage directory.");
            }
        } else {
            logger.log(Level.INFO, "Storage directory found.");
        }
    }

    public static StorageManager getInstance() {
        if (instance == null) {
            instance = new StorageManager();
        }

        return instance;
    }

    public String getStorageDirectory() {
        return this.storageDir;
    }

    public String getDatabaseDirectory(String databaseName) throws IllegalArgumentException{
        File file = new File(this.storageDir + File.separator + databaseName);
        if (!file.exists()) {
            throw new IllegalArgumentException("Database [" + databaseName + "] does not exists.");
        }
        return this.storageDir + File.separator + databaseName;
    }

    public void createDatabaseDirectory(String databaseName) {
        File file = new File(this.storageDir, databaseName);
        if (file.exists()) {
            throw new IllegalArgumentException("Database has already exists");
        } else {
            if (file.mkdir()) {
                logger.log(Level.INFO, "Successfully created storage for database [" + databaseName + "]");
                databases.add(databaseName);
            } else {
                logger.log(Level.SEVERE, "Failed to create storage for database [" + databaseName + "]");
            }
        }
    }
}
