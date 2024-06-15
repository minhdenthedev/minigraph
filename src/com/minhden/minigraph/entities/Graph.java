package com.minhden.minigraph.entities;

import com.minhden.minigraph.util.BTree;
import com.minhden.minigraph.util.Entry;

import java.util.List;

public class Graph extends AbstractEntity {
    BTree nodes;
    BTree edges;

    public Graph(int internalId, String name) {
        super(internalId, name);
        this.nodes = new BTree(10);
        this.edges = new BTree(10);
    }

    public void addNode(Node node) {
        nodes.insert(new Entry<>(node.internalId, node));
    }

    public void addEdge(Edge r) {
        edges.insert(new Entry<>(r.internalId, r));
    }

    public void addAllNodes(List<Node> nodes) {
        for (Node node : nodes) {
            this.nodes.insert(new Entry<>(node.internalId, node));
        }
    }

    public void addAllEdge(List<Edge> edges) {
        for (Edge edge : edges) {
            this.edges.insert(new Entry<>(edge.internalId, edge ));
        }
    }
}
