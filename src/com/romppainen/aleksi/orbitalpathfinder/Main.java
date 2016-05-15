package com.romppainen.aleksi.orbitalpathfinder;

/**
 *
 * @author  Aleksi Romppainen   <aromppa@gmail.com>
 */
public class Main {
    public static void main(String[] args) {
        //OrbitalPathfinder op = new OrbitalPathfinder();
        //op.calcPath("data.txt");
        
        if (args.length > 0) {
            OrbitalPathfinder op = new OrbitalPathfinder();
            op.calcPath(args[0]);
        } else {
            System.out.println("Usage: java -jar OrbitalPathfinder filename");
        }
    }
}
