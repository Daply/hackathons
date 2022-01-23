package com.pizzacutter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
public class Solver {
    
    public static void solve(String fileName) throws IOException {
        PizzaCutter pizzaCutter = new PizzaCutter();
        pizzaCutter.run(fileName);
        pizzaCutter.printFileResult();
    }
    
//    public static void visualization() throws IOException {
//        PizzaCutter pizzaCutter = new PizzaCutter();
//        pizzaCutter.visualizeRun();
//    }

    public static String getFileName(String file) throws IOException {
        if (file.toLowerCase().trim().equals("example")) {
            return "src//com//pizzacutter//data//a_example.in";
        }
        if (file.toLowerCase().trim().equals("small")) {
            return "src//com//pizzacutter//data//b_small.in";
        }
        if (file.toLowerCase().trim().equals("medium")) {
            return "src//com//pizzacutter//data//c_medium.in";
        }
        if (file.toLowerCase().trim().equals("big")) {
            return "src//com//pizzacutter//data//d_big.in";
        }
        return "";
    }


    public static void solveFromFile() throws IOException {
        String fileName = "";
        String st; 
        BufferedReader br = new BufferedReader(new FileReader(new File("src//input_filename.txt"))); 
        int countLines = 0;
        while ((st = br.readLine()) != null) {
            if (countLines == 1) {
                fileName = getFileName(st);
            }
            countLines++;
        }
        br.close();
        
        if (!fileName.isEmpty()) {
            solve(fileName);
        }
        else {
            System.out.println("Wrong file name!");
        }
    }
    
    public static void main(String args[]) throws Exception {
		// time
		long startTime = System.currentTimeMillis();
		
		
        // example file
        //solve("src//com//pizzacutter//data//a_example.in");
        // small file
        //solve("src//com//pizzacutter//data//b_small.in");
        // medium file
        //solve("src//com//pizzacutter//data//c_medium.in");
        // big file
        //solve("src//com//pizzacutter//data//d_big.in");
        //visualization();
        
        
        solveFromFile();
		
		// time 
		long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        double seconds = (double)elapsedTime/1000;
        double minutes = 0;
        double hours = 0;
        if (seconds > 60) {
            minutes = (double)seconds/60;
            seconds = seconds%60;
        }
        if (minutes > 60) {
            hours = (double)minutes/60;
        }
        System.out.println("Elapsed Time: " + (int)hours + " hours " + 
                            (int)minutes + " minutes " + 
                            (int)seconds + " seconds");
    }
    
    
}
