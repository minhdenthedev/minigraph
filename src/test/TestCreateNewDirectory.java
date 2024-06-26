package test;

import com.minhden.minigraph.kernel.storage.HeapFile;
import com.minhden.minigraph.manager.StorageManager;

import java.io.File;

public class TestCreateNewDirectory {
    public static void main(String[] args) {
        StorageManager manager = StorageManager.getInstance();
        String filePath = manager.getDatabaseDirectory("testDB");
        filePath = filePath + File.separator + "nodes.minig";
        HeapFile file = new HeapFile(filePath);
        file.allocateNewPage();
    }
}
