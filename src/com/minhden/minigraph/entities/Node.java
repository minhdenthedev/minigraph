package com.minhden.minigraph.entities;

import java.util.Set;
import java.util.HashSet;

public class Node extends AbstractEntity {
    Set<String> labels;
    Set<Edge> edges;

    public Node(int internalId, String name) {
        super(internalId, name);
        labels = new HashSet<>();
        edges = new HashSet<>();
    }

    public Set<String> getLabels() {
        return labels;
    }

    public Set<Edge> getRelationships() {
        return edges;
    }

    public void addLabel(String label) {
        if (!labels.add(label)) {
            throw new IllegalArgumentException("Label has already exists.");
        }
    }

    public void addEdge(Edge edge) {
        if (!edges.add(edge)) {
            throw new IllegalArgumentException("Relationship has already exists.");
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "internalId=" + internalId +
                ", name='" + name + '\'' +
                '}';
    }
}
