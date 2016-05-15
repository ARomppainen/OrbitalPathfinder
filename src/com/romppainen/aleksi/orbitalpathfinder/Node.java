package com.romppainen.aleksi.orbitalpathfinder;

import java.util.HashSet;
import java.util.Objects;

/**
 * Nodes represent point in search graph.
 * 
 * @author  Aleksi Romppainen   <aromppa@gmail.com>
 */
public class Node implements Comparable<Node> {
    
    public String id;
    public Vector3 coords;
    public HashSet<Connection> connections;
    
    public String predecessor;
    public double g;
    public double h;
    
    /**
     * 
     * @param id     node id(name)
     * @param coords coordinates
     */
    public Node(String id, Vector3 coords) {
        this.id = id;
        this.coords = coords;
        connections = new HashSet<>();
        
        predecessor = null;
        g = 0;
        h = 0;
    }
    
    /**
     * Adds a connection to the node.
     * 
     * @param target target node's id
     * @param distance the distance between nodes
     */
    public void addConnection(String target, double distance) {
        connections.add(new Connection(target, distance));
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        
        if (obj instanceof Node) {
            Node n = (Node)obj;
            
            if (this.id.equals(n.id)) {
                result = true;
            }
        }
        
        return result;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public int compareTo(Node o) {
        double f = (this.g + this.h) - (o.g + o.h);
        double delta = 0.0001;
        
        if (f < delta && f > -delta) {
            return 0;
        } else if (f < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
