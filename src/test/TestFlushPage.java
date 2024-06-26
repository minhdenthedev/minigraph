package test;

import com.minhden.minigraph.kernel.storage.*;
import com.minhden.minigraph.kernel.storage.Record;

public class TestFlushPage {
    public static void main(String[] args) {
        HeapFile file = new HeapFile("/home/hminh/Work/minigraph/databases/testDB/nodes.minig");
        Page page = file.allocateNewPage();
        Record record1 = new NodeRecord(page.getId(), 1, "test" + 1);
        Record record2 = new NodeRecord(page.getId(), 2, "test2");
        page.insertRecord(record1);
        page.insertRecord(record2);
        file.flushPage(page);
        Page page2 = file.allocateNewPage();
        page2.insertRecord(record2);
        page2.insertRecord(record1);
        file.flushPage(page2);
    }
}
