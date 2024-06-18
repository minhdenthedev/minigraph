package test;

import com.minhden.minigraph.kernel.storage.HeapPage;
import com.minhden.minigraph.kernel.storage.HeapPageId;
import com.minhden.minigraph.kernel.storage.NodeRecord;
import com.minhden.minigraph.kernel.storage.Record;

public class TestNewHeapPage {
    public static void main(String[] args) {
        HeapPage page = new HeapPage(new HeapPageId("nodes.minig", 0), new byte[4096]);
        for (int i = 0; i < 15; i++) {
            page.insertRecord(new NodeRecord(page.getId(), i, "test" + i));
        }

        page.deleteRecord(page.getRecord(3));
    }
}
