package com.romppainen.aleksi.orbitalpathfinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * OrbitalPathfinder constructs a node graph from data file and finds a path
 * from start to goal using A* search algorithm.
 * 
 * @author  Aleksi Romppainen   <aromppa@gmail.com>
 */
public class OrbitalPathfinder {

    public static final double EARTH_RADIUS = 6371.0; // kilometers
    public static final String START = "START";
    public static final String GOAL = "GOAL";
    
    private HashMap<String, Node> graph;
    private HashMap<String, Vector3> points;
    
    public OrbitalPathfinder() {
        graph = new HashMap<>();
        points = new HashMap<>();
    }
    
    /**
     * Constructs a node graph and finds a path from start to goal. Prints the
     * intermediate hops across satellites from start to goal or informs the
     * user if path could not be found.
     * 
     * @param filename filename for data file
     */
    public void calcPath(String filename) {
        if (!parseData(filename)) {
            buildGraph();
            //printGraph();
            
            String result = findPath(graph.get(START), graph.get(GOAL));
            
            if (result != null) {
                System.out.println(result);
            } else {
                System.out.println("Path not found!");
            }
        }
    }
    
    /**
     * Reads the data file and calculates the coordinates for satellites.
     * 
     * @param filename filename for data file
     * @return true if file was not found or IOException occurred
     */
    private boolean parseData(String filename) {
        boolean error = false;
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            br.readLine(); // skip the first line containing the seed
            
            String line;
            
            do {
                line = br.readLine();
                
                if (line != null) {
                    String[] values = line.split(",");
                    
                    if (values.length == 4) { // satellite
                        double latitude = Double.parseDouble(values[1]);
                        double longitude = Double.parseDouble(values[2]);
                        double altitude = Double.parseDouble(values[3]);
                        
                        points.put(values[0], new Vector3(latitude, longitude, EARTH_RADIUS, altitude));
                        
                    } else if (values.length == 5) { // route
                        double latitude = Double.parseDouble(values[1]);
                        double longitude = Double.parseDouble(values[2]);
                        
                        // cheat a bit and put start and goal 1 meter above surface
                        // for better performance =)
                        double cheat = 0.001;
                        
                        points.put(START, new Vector3(latitude, longitude, EARTH_RADIUS, cheat));
                        
                        latitude = Double.parseDouble(values[3]);
                        longitude = Double.parseDouble(values[4]);
                        
                        points.put(GOAL, new Vector3(latitude, longitude, EARTH_RADIUS, cheat));
                    }
                }
            } while (line != null);
            
        } catch (FileNotFoundException e) {
            error = true;
            System.err.println("File " + filename + " was not found.");
        } catch (IOException e) {
            error = true;
            System.err.println("Oops, something went wrong!");
        }
        
        return error;
    }
    
    /**
     * Builds a node graph using the calculated coordinate data.
     */
    private void buildGraph() {
        for (String s1: points.keySet()) {
            Vector3 v1 = points.get(s1);
            
            Node n = new Node(s1, v1);
            
            for(String s2: points.keySet()) {
                Vector3 v2 = points.get(s2);
                
                if (v1 != v2) {
                    if (testLineOfSight(v1, v2)) {
                        Vector3 path = new Vector3(v1, v2);
                        n.addConnection(s2, path.length());
                    }
                }
            }
            
            graph.put(s1, n);
        }
    }
    
    /**
     * Tests line of sight using ray marching algorithm.
     * 
     * @param p1 starting point
     * @param p2 end point
     * @return true if line of sight was found
     */
    private boolean testLineOfSight(Vector3 p1, Vector3 p2) {
        boolean result = true;
        
        Vector3 path = new Vector3(p1, p2);
        double length = path.length();
        path.normalize();
        
        Vector3 p = new Vector3(p1);
        double traveled = 0;
        double minStep = 0.0001;
        
        while(true) {
            double distToEarth = p.length() - EARTH_RADIUS;
            
            if (traveled + distToEarth >= length) {
                break;
            } else if (distToEarth < 0) {
                result = false;
                break;
            }
            
            double step = Math.max(distToEarth, minStep);
            
            p.scaleAndAdd(path, step);
            traveled += step;
        }
        
        return result;
    }

    /**
     * Finds a path from start to goal using A* search algorithm.
     * 
     * @param start starting node
     * @param goal goal node
     * @return a comma separated string with the intermediate hops between start and goal
     */
    public String findPath(Node start, Node goal) {
        boolean pathFound = false;
        String path = null;
        
        HashSet<Node> foundNodes = new HashSet<>();
        foundNodes.add(start);
        
        PriorityQueue<Node> pq = new PriorityQueue<>();
        start.h = Vector3.distance(start.coords, goal.coords);
        pq.add(start);
        
        while(!pq.isEmpty()) {
            Node from = pq.poll();
            
            if (from.equals(goal)) {
                pathFound = true;
                break;
            }
            
            for (Connection c: from.connections) {
                Node target = graph.get(c.to);
                double distance = from.g + c.distance;
                
                if (!foundNodes.contains(target)) {
                    target.predecessor = from.id;
                    target.g = distance;
                    target.h = Vector3.distance(target.coords, goal.coords);
                    pq.add(target);
                    foundNodes.add(target);
                    
                } else if (target.g > distance) {
                    target.g = distance;
                    target.predecessor = from.id;
                }
            }
        }
        
        if (pathFound) {
            path = "";
            Node n = goal;
            ArrayList<String> list = new ArrayList<>();
            list.add(n.id);
            
            while(n.predecessor != null) {
                n = graph.get(n.predecessor);
                list.add(n.id);
            }
            
            // don't add start and goal
            for (int i = list.size() - 2; i >= 1; i--) {
                path += list.get(i);
                
                if (i != 1) {
                    path += ",";
                }
            }
        }
        
        return path;
    }
    
    /**
     * Prints the graph, used for testing purposes.
     */
    private void printGraph() {
        for (String s1: graph.keySet()) {
            System.out.println(s1 + " " + graph.get(s1).coords);
                
            for (Connection c: graph.get(s1).connections) {
                System.out.println("   " + c.to);
            }
        }
    }
}