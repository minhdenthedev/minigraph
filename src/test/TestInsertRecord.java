package test;

import com.minhden.minigraph.entities.Node;
import com.minhden.minigraph.kernel.storage.HeapFile;
import com.minhden.minigraph.kernel.storage.NodeRecord;

public class TestInsertRecord {
    public static void main(String[] args) {
        HeapFile file = new HeapFile("/home/hminh/Work/minigraph/databases/testDB/testNumberOfPages.minig");
        file.insertRecord(new NodeRecord(12, "Test1"));
    }
}
