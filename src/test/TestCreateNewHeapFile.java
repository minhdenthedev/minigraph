package test;

import com.minhden.minigraph.kernel.storage.HeapFile;
import com.minhden.minigraph.kernel.storage.Page;
import com.minhden.minigraph.manager.StorageManager;

public class TestCreateNewHeapFile {
    public static void main(String[] args) {
        String directory = StorageManager.getInstance().getDatabaseDirectory("testDB");
        HeapFile file = HeapFile.createNewFile(directory + "/test.minig");
        file.allocateNewPage();
    }
}
