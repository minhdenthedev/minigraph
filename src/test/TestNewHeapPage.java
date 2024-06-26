package test;

import com.minhden.minigraph.kernel.storage.*;
import com.minhden.minigraph.kernel.storage.Record;

import java.util.Arrays;

public class TestNewHeapPage {
    public static void main(String[] args) {
        String fileName = "/home/hminh/Work/minigraph/databases/testDB/nodes.minig";
        HeapFile file = new HeapFile(fileName);
        System.out.println(file.readPage(new HeapPageId(fileName, 1)));
        System.out.println(file.readPage(new HeapPageId(fileName, 0)));
    }
}
