package test;

import com.minhden.minigraph.kernel.storage.HeapPageId;
import com.minhden.minigraph.kernel.storage.NodeRecord;
import com.minhden.minigraph.kernel.storage.PageId;

import java.util.Arrays;

public class TestNodeRecord {
    public static void main(String[] args) {
        PageId pageId = new HeapPageId("nodes.minig", 1);
        NodeRecord record = new NodeRecord(pageId, 1, 1, "Test1");
        byte[] bytes = record.toByteArray();
        NodeRecord record2 = NodeRecord.fromByteArray(bytes);
        System.out.println(record2);
    }
}
