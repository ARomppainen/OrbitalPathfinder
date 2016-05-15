package com.romppainen.aleksi.orbitalpathfinder;

import java.util.Objects;

/**
 *  Represents a connection from one Node to another.
 * 
 * @author  Aleksi Romppainen   <aromppa@gmail.com>
 */
public class Connection {
    
    public String to;
    public double distance;
    
    /**
     * 
     * @param to       target node's id
     * @param distance distance between nodes
     */
    public Connection(String to, double distance) {
        this.to = to;
        this.distance = distance;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        
        if (obj instanceof Connection) {
            Connection c = (Connection)obj;
            
            if (this.to.equals(c.to)) {
                result = true;
            }
        }
        
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.to);
        return hash;
    }
}
