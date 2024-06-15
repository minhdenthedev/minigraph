package com.minhden.minigraph.entities;

public class Edge extends AbstractEntity {
    Node source;
    Node target;

    public Edge(int internalId, String name, Node source, Node target)
            throws IllegalArgumentException {
        super(internalId, name);
        this.source = source;
        this.source.addEdge(this);
        this.target = target;
        this.target.addEdge(this);
    }

    public Edge(int internalId, String name) {
        super(internalId, name);
        this.source = null;
        this.target = null;
    }

    public void setSource(Node source) {
        this.source = source;
        this.source.addEdge(this);
    }

    public void setTarget(Node target) {
        this.target = target;
        this.target.addEdge(this);
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

    public Node[] getNodes() {
        Node[] nodes = new Node[2];
        nodes[0] = source;
        nodes[1] = target;

        return nodes;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "internalId=" + internalId +
                ", name='" + name + '\'' +
                '}';
    }
}
