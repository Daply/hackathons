package com.pizzacutter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import javax.swing.UIManager;

import com.pizzacutter.visualizer.PizzaVisualizationWindow;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
public class PizzaCutter {
    
    /**
     * data:
     * - whole pizza size and elements
     */
    private Element[][] pizza = null;
    private int rows = 0;
    private int cols = 0;
    
    /**
     * slice minimum and maximum conditions
     */
    private int minimumNumberOfEachElement = 0;
    private int maximumNumberOfCells = 0;
    
    /**
     * count elements by types
     */
    private int countMushrooms = 0;
    private int countTomatoes = 0;
    
    /**
     * element type, which occurences
     * on pizza less than others
     * (mushroom (0) or tomato (1))
     */
    private int minimalElement = 1;
    
    /**
     * All firstly gathered minimum type
     * elements
     */
    private List<Slice> minimalElements = null;

    /**
     * global result
     */
    private List<Slice> slicesResult = null;
    
    /**
     * total square of all result slices
     */
    private int totalSlicesSquare = 0;
    
    
    /**
     * for test 
     * visualization of algorithm
     */
    //private boolean visualize = false;
    //private PizzaVisualizationWindow visualizer = null;
    
    
    public PizzaCutter() {
        this.minimalElements = new ArrayList<Slice> ();
        this.slicesResult = new ArrayList<Slice> ();
    }
    
//    public void visualizeRun () throws IOException {  
//        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//        } catch (Exception evt) {}
//        // Create an instance of the test application
//        this.visualizer = new PizzaVisualizationWindow();
//        this.visualizer.setPizzaCutter(this);
//        this.visualizer.setSize(800, 800);
//        //mainFrame.pack();
//        this.visualizer.setVisible( true );
//        this.visualize = true;
//    }
    
    public void run (String fileName) throws IOException {
        clearData();
        readPizza(fileName);
        cutPizza();
    }
    
    public void printFileResult () throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("src//result.txt"));
        
        writer.write(this.slicesResult.size() + "\n");
        for (Slice slice: this.slicesResult) {
            writer.write(slice.getStartElement().getI() + " " +
                    slice.getStartElement().getJ() + " " +
                    slice.getEndElement().getI() + " " +
                    slice.getEndElement().getJ() + "\n");
        }
        
        writer.close();
    }
    
    public void printResult () throws IOException {
        System.out.println("Slices: ");
        System.out.println(getSlicesResult());
        System.out.println("Whole pizza square: " + getCols()*getRows());
        System.out.println("Slices square: " + getTotalSlicesSquare());
    }
    
    public void readPizza(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));       
        String[] data = new String[4];
        String st; 
        boolean readConditions = true;
        // current element
        Element curEl = null;     
        int i = 0;
        while ((st = br.readLine()) != null) {
            if (readConditions) {
               data = st.split("\\s");
               this.rows = Integer.parseInt(data[0]);
               this.cols = Integer.parseInt(data[1]);
               this.minimumNumberOfEachElement = Integer.parseInt(data[2]);
               this.maximumNumberOfCells = Integer.parseInt(data[3]);
               this.pizza = new Element[rows][cols];
               readConditions = false;
            }
            else {
                String pizzaRow = st;
                for (int j = 0; j < cols; j++) {
                    char el = pizzaRow.charAt(j);
                    if (el == 'M') {
                        curEl = new Element(i, j, 0);
                        pizza[i][j] = curEl;
                        this.countMushrooms++;
                    }
                    if (el == 'T') {
                        curEl = new Element(i, j, 1);
                        pizza[i][j] = curEl;
                        this.countTomatoes++;
                    }
                }
                i++;
            }
        } 
        
        br.close();
        
        int minimumOfElems = Math.min(this.countMushrooms, this.countTomatoes);
        if (minimumOfElems == this.countMushrooms)
            this.minimalElement = 0;
        
        // for test only
//        if (this.visualize) {
//            this.visualizer.setPizza(this.pizza);
//        }
    }
 
    private void addSurroundings() {
        // set nodes connection
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (j > 0) {
                    this.pizza[i][j].setLeft(this.pizza[i][j-1]);
                    if (this.pizza[i][j-1].getElement() == 0)
                        this.pizza[i][j].incrementCountSurroundElementMushroom();
                    if (this.pizza[i][j-1].getElement() == 1)
                        this.pizza[i][j].incrementCountSurroundElementTomato();
                }
                if (j < this.cols - 1) {
                    this.pizza[i][j].setRight(this.pizza[i][j+1]);
                    if (this.pizza[i][j+1].getElement() == 0)
                        this.pizza[i][j].incrementCountSurroundElementMushroom();
                    if (this.pizza[i][j+1].getElement() == 1)
                        this.pizza[i][j].incrementCountSurroundElementTomato();
                }
                if (i > 0) {
                    this.pizza[i][j].setUp(this.pizza[i-1][j]);
                    if (this.pizza[i-1][j].getElement() == 0)
                        this.pizza[i][j].incrementCountSurroundElementMushroom();
                    if (this.pizza[i-1][j].getElement() == 1)
                        this.pizza[i][j].incrementCountSurroundElementTomato();
                }
                if (i < this.rows - 1) {
                    this.pizza[i][j].setDown(this.pizza[i+1][j]);
                    if (this.pizza[i+1][j].getElement() == 0)
                        this.pizza[i][j].incrementCountSurroundElementMushroom();
                    if (this.pizza[i+1][j].getElement() == 1)
                        this.pizza[i][j].incrementCountSurroundElementTomato();
                }
                // creating slices from minimal elements
                if (this.pizza[i][j].getElement() == this.minimalElement) {
                    Slice slice = new Slice(this.pizza[i][j], this.pizza[i][j],
                            minimumNumberOfEachElement, maximumNumberOfCells, minimalElement);
                    slice.incrementCells();
                    if (slice.getStartElement().getElement() == 0)
                        slice.incrementNumMushrooms();
                    if (slice.getStartElement().getElement() == 1)
                        slice.incrementNumTomatoes();
                    this.minimalElements.add(slice);
                }
            }
        }
        
        // if minimal number of each element is more than one, 
        // it needs to create minimal slices which contain
        // minimal number of minimal element
        // ... TODO
    }
    
    public void cutPizza() {
        addSurroundings();
        
        // creating result list and queue
        //Set<Slice> result = new HashSet<Slice> ();
        ArrayList<Slice> result = null;
        Set<Slice> used = null;
        Queue<Slice> queueSlices = null;
        int sliceMark = 0;
        for (Slice qslice: this.minimalElements) {
            result = new ArrayList<Slice> ();
            used = new HashSet<Slice> ();
            queueSlices = new LinkedList<Slice> ();
            if (qslice.getStartElement().getSliceMark() == -1) {
                queueSlices.add(qslice);
                while (!queueSlices.isEmpty()) {
                    Slice slice = queueSlices.remove();
                    
                    if (!slice.checkMaximumConditions()) {
                        Element elLeftStart = slice.getStartElement().getLeft();
                        Element elRightStart = slice.getStartElement().getRight();
                        Element elUpStart = slice.getStartElement().getUp();
                        Element elDownStart = slice.getStartElement().getDown();
                        
                        Element elLeftEnd = slice.getEndElement().getLeft();
                        Element elRightEnd = slice.getEndElement().getRight();
                        Element elUpEnd = slice.getEndElement().getUp();
                        Element elDownEnd = slice.getEndElement().getDown();
                        
                        // slice when added right element
                        if (elLeftStart != null && !slice.containsElement(elLeftStart) &&
                                (elLeftStart.getSliceMark() == sliceMark ||
                                 elLeftStart.getSliceMark() == -1)) {
                            Slice leftSlice = createLeftSlice(slice, elLeftStart, sliceMark);
                            // adding to queue
                            if (leftSlice != null && !used.contains(leftSlice)) {
                                queueSlices.add(leftSlice);
                                used.add(leftSlice);
                            }
                        }
                        
                        // slice when added right element
                        if (elRightStart != null && !slice.containsElement(elRightStart) &&
                                (elRightStart.getSliceMark() == sliceMark ||
                                 elRightStart.getSliceMark() == -1)) {
                            Slice rightSlice = createRightSlice(slice, elRightStart, sliceMark);
                            // adding to queue
                            if (rightSlice != null && !used.contains(rightSlice)) {
                                queueSlices.add(rightSlice);
                                used.add(rightSlice);
                            }
                        }
                        
                        // slice when added down element
                        if (elUpStart != null && !slice.containsElement(elUpStart) &&
                                (elUpStart.getSliceMark() == sliceMark ||
                                 elUpStart.getSliceMark() == -1)) {
                            Slice upSlice = createUpSlice(slice, elUpStart, sliceMark);
                            // adding to queue
                            if (upSlice != null && !used.contains(upSlice)) {
                                queueSlices.add(upSlice);
                                used.add(upSlice);
                            }
                        }
                        
                        // slice when added down element
                        if (elDownStart != null && !slice.containsElement(elDownStart) &&
                                (elDownStart.getSliceMark() == sliceMark ||
                                 elDownStart.getSliceMark() == -1)) {
                            Slice downSlice = createDownSlice(slice, elDownStart, sliceMark);
                            // adding to queue
                            if (downSlice != null && !used.contains(downSlice)) {
                                queueSlices.add(downSlice);
                                used.add(downSlice);
                            }
                        }
                        
                        // slice when added right element
                        if (elLeftEnd != null && !slice.containsElement(elLeftEnd) &&
                                (elLeftEnd.getSliceMark() == sliceMark || 
                                 elLeftEnd.getSliceMark() == -1)) {
                            Slice leftSlice = createLeftSlice(slice, elLeftEnd, sliceMark);
                            // adding to queue
                            if (leftSlice != null && !used.contains(leftSlice)) {
                                queueSlices.add(leftSlice);
                                used.add(leftSlice);
                            }
                        }
                        
                        // slice when added right element
                        if (elRightEnd != null && !slice.containsElement(elRightEnd) &&
                                (elRightEnd.getSliceMark() == sliceMark || 
                                 elRightEnd.getSliceMark() == -1)) {
                            Slice rightSlice = createRightSlice(slice, elRightEnd, sliceMark);
                            // adding to queue
                            if (rightSlice != null && !used.contains(rightSlice)) {
                                queueSlices.add(rightSlice);
                                used.add(rightSlice);
                            }
                        }
                        
                        // slice when added down element
                        if (elUpEnd != null && !slice.containsElement(elUpEnd) &&
                                (elUpEnd.getSliceMark() == sliceMark ||
                                 elUpEnd.getSliceMark() == -1)) {
                            Slice upSlice = createUpSlice(slice, elUpEnd, sliceMark);
                            // adding to queue
                            if (upSlice != null && !used.contains(upSlice)) {
                                queueSlices.add(upSlice);
                                used.add(upSlice);
                            }
                        }
                        
                        // slice when added down element
                        if (elDownEnd != null && !slice.containsElement(elDownEnd) &&
                                (elDownEnd.getSliceMark() == sliceMark || 
                                 elDownEnd.getSliceMark() == -1)) {
                            Slice downSlice = createDownSlice(slice, elDownEnd, sliceMark);
                            // adding to queue
                            if (downSlice != null && !used.contains(downSlice)) {
                                queueSlices.add(downSlice);
                                used.add(downSlice);
                            }
                        }
                        if (slice.checkMinimumConditions() && !result.contains(slice)) {
                            result.add(slice); 
                        }
                    }
                    else {
                        if (!result.contains(slice)) {
                            result.add(slice);      
                        }
                    }
                }
            }
            
            // get the most optimal slice from result set
            Collections.sort(result, new SliceComparator());
            if (!result.isEmpty()) {
                Slice optimalSlice = result.get(result.size()-1);
                this.slicesResult.add(optimalSlice);
                
                for (int i = optimalSlice.getStartElement().getI(); 
                        i <= optimalSlice.getEndElement().getI(); i++) {
                    for (int j = optimalSlice.getStartElement().getJ();
                            j <= optimalSlice.getEndElement().getJ(); j++) {
                        pizza[i][j].setSliceMark(sliceMark);
                    }
                }
                
                // for test only
//                if (this.visualize) {
//                    this.visualizer.redraw();
//                }
            }
            sliceMark++;
        }
        //System.out.println(this.slicesResult);
        countTotalSlicesSquare();
    }
    
    public void countTotalSlicesSquare() {
        for (Slice slice: this.slicesResult) {
            this.totalSlicesSquare += slice.getCountCells();
        }
    }
    
    
    private Slice createLeftSlice(Slice slice, Element elLeft, int sliceMark) {
        int imin = Math.min(slice.getStartElement().getI(), elLeft.getI());
        int jmin = Math.min(slice.getStartElement().getJ(), elLeft.getJ());
        Element startElem = this.pizza[imin][jmin];
        int imax = Math.max(slice.getEndElement().getI(), elLeft.getI());
        int jmax = Math.max(slice.getEndElement().getJ(), elLeft.getJ());
        Element endElem = this.pizza[imax][jmax];
        
        if (startElem.getSliceMark() == sliceMark ||
                    startElem.getSliceMark() == -1) {
            Slice leftSlice = new Slice(startElem, endElem, 
                    minimumNumberOfEachElement, maximumNumberOfCells, minimalElement);
            // add all previous slice values
            leftSlice.setCountCells(slice.getCountCells());
            leftSlice.setCountNumMushrooms(slice.getCountNumMushrooms());
            leftSlice.setCountNumTomatoes(slice.getCountNumTomatoes());
            // if shape of taken slice is like (s - start, e - end)
            // and new left element is l:
            //   l s * * e
            // than just adding new cell elements to new slice
            if (leftSlice.getSliceShape().getWidth() == 1) {
                leftSlice.incrementCells();
                if (elLeft.getElement() == 0)
                    leftSlice.incrementNumMushrooms();
                if (elLeft.getElement() == 1)
                    leftSlice.incrementNumTomatoes();
            }
            // if shape of taken slice is like (s - start, e - end)
            // and new left element is l:
            //  l e   or   s
            //    *        *
            //    *        *
            //    s      l e
            // than adding all left column to the slice 
            else {
                int startI = leftSlice.getStartElement().getI();
                int endI = leftSlice.getEndElement().getI();
                for (int i = startI; i <= endI; i++) {
                    leftSlice.incrementCells();
                    if (pizza[i][elLeft.getJ()].getElement() == 0)
                        leftSlice.incrementNumMushrooms();
                    if (pizza[i][elLeft.getJ()].getElement() == 1)
                        leftSlice.incrementNumTomatoes();
                    if (pizza[i][elLeft.getJ()].getSliceMark() != -1 &&
                            pizza[i][elLeft.getJ()].getSliceMark() != sliceMark) {
                        return null;
                    }
                }
            }
            return leftSlice;
        }
        return null;
    }
    
    private Slice createRightSlice(Slice slice, Element elRight, int sliceMark) {
        int imin = Math.min(slice.getStartElement().getI(), elRight.getI());
        int jmin = Math.min(slice.getStartElement().getJ(), elRight.getJ());
        Element startElem = this.pizza[imin][jmin];
        int imax = Math.max(slice.getEndElement().getI(), elRight.getI());
        int jmax = Math.max(slice.getEndElement().getJ(), elRight.getJ());
        Element endElem = this.pizza[imax][jmax];
        
        if (startElem.getSliceMark() == sliceMark ||
                    startElem.getSliceMark() == -1) {
            Slice rightSlice = new Slice(startElem, endElem, 
                    minimumNumberOfEachElement, maximumNumberOfCells, minimalElement);
            // add all previous slice values
            rightSlice.setCountCells(slice.getCountCells());
            rightSlice.setCountNumMushrooms(slice.getCountNumMushrooms());
            rightSlice.setCountNumTomatoes(slice.getCountNumTomatoes());
            // if shape of taken slice is like (s - start, e - end)
            // and new right element is r:
            //   s * * e r
            // than just adding new cell elements to new slice
            if (rightSlice.getSliceShape().getWidth() == 1) {
                rightSlice.incrementCells();
                if (elRight.getElement() == 0)
                    rightSlice.incrementNumMushrooms();
                if (elRight.getElement() == 1)
                    rightSlice.incrementNumTomatoes();
            }
            // if shape of taken slice is like (s - start, e - end)
            // and new right element is r:
            //  e r  or   s
            //  *         *
            //  *         *
            //  s         e r
            // than adding all right column to the slice 
            else {
                int startI = rightSlice.getStartElement().getI();
                int endI = rightSlice.getEndElement().getI();
                for (int i = startI; i <= endI; i++) {
                    rightSlice.incrementCells();
                    if (pizza[i][elRight.getJ()].getElement() == 0)
                        rightSlice.incrementNumMushrooms();
                    if (pizza[i][elRight.getJ()].getElement() == 1)
                        rightSlice.incrementNumTomatoes();
                    if (pizza[i][elRight.getJ()].getSliceMark() != -1 &&
                            pizza[i][elRight.getJ()].getSliceMark() != sliceMark) {
                        return null;
                    }
                }
            }
            return rightSlice;
        }
        return null;
    }
    
    private Slice createUpSlice(Slice slice, Element elUp, int sliceMark) {
        int imin = Math.min(slice.getStartElement().getI(), elUp.getI());
        int jmin = Math.min(slice.getStartElement().getJ(), elUp.getJ());
        Element startElem = this.pizza[imin][jmin];
        int imax = Math.max(slice.getEndElement().getI(), elUp.getI());
        int jmax = Math.max(slice.getEndElement().getJ(), elUp.getJ());
        Element endElem = this.pizza[imax][jmax];
        
        if (startElem.getSliceMark() == sliceMark ||
                    startElem.getSliceMark() == -1) {
            Slice upSlice = new Slice(startElem, endElem,
                    minimumNumberOfEachElement, maximumNumberOfCells, minimalElement);
            // add all previous slice values
            upSlice.setCountCells(slice.getCountCells());
            upSlice.setCountNumMushrooms(slice.getCountNumMushrooms());
            upSlice.setCountNumTomatoes(slice.getCountNumTomatoes());
            // if shape of taken slice is like (s - start, e - end)
            // and new up element is u:
            //  u
            //  e
            //  *
            //  *
            //  s
            // than just adding new cell elements to new slice
            if (upSlice.getSliceShape().getLength() == 1) {
                upSlice.incrementCells();
                if (elUp.getElement() == 0)
                    upSlice.incrementNumMushrooms();
                if (elUp.getElement() == 1)
                    upSlice.incrementNumTomatoes();
            }
            // if shape of taken slice is like (s - start, e - end)
            // and new up element is u:
            //  u         or        u
            //  s * * e       s * * e
            // than adding all up row to the slice 
            else {
                int startJ = upSlice.getStartElement().getJ();
                int endJ = upSlice.getEndElement().getJ();
                for (int j = startJ; j <= endJ; j++) {
                    upSlice.incrementCells();
                    if (pizza[elUp.getI()][j].getElement() == 0)
                        upSlice.incrementNumMushrooms();
                    if (pizza[elUp.getI()][j].getElement() == 1)
                        upSlice.incrementNumTomatoes();
                    if (pizza[elUp.getI()][j].getSliceMark() != -1 &&
                            pizza[elUp.getI()][j].getSliceMark() != sliceMark) {
                        return null;
                    }
                }
            }
            return upSlice;
        }
        return null;
    }

    private Slice createDownSlice(Slice slice, Element elDown, int sliceMark) {
        int imin = Math.min(slice.getStartElement().getI(), elDown.getI());
        int jmin = Math.min(slice.getStartElement().getJ(), elDown.getJ());
        Element startElem = this.pizza[imin][jmin];
        int imax = Math.max(slice.getEndElement().getI(), elDown.getI());
        int jmax = Math.max(slice.getEndElement().getJ(), elDown.getJ());
        Element endElem = this.pizza[imax][jmax];
        
        if (startElem.getSliceMark() == sliceMark ||
                    startElem.getSliceMark() == -1) {
            Slice downSlice = new Slice(startElem, endElem,
                    minimumNumberOfEachElement, maximumNumberOfCells, minimalElement);
            // add all previous slice values
            downSlice.setCountCells(slice.getCountCells());
            downSlice.setCountNumMushrooms(slice.getCountNumMushrooms());
            downSlice.setCountNumTomatoes(slice.getCountNumTomatoes());
            // if shape of taken slice is like (s - start, e - end)
            // and new down element is d:
            //  s
            //  *
            //  *
            //  e
            //  d
            // than just adding new cell elements to new slice
            if (downSlice.getSliceShape().getLength() == 1) {
                downSlice.incrementCells();
                if (elDown.getElement() == 0)
                    downSlice.incrementNumMushrooms();
                if (elDown.getElement() == 1)
                    downSlice.incrementNumTomatoes();
            }
            // if shape of taken slice is like (s - start, e - end)
            // and new down element is d:
            //  e * * s  or  s * * e
            //  d                  d
            // than adding all down row to the slice 
            else {
                int startJ = downSlice.getStartElement().getJ();
                int endJ = downSlice.getEndElement().getJ();
                for (int j = startJ; j <= endJ; j++) {
                    downSlice.incrementCells();
                    if (pizza[elDown.getI()][j].getElement() == 0)
                        downSlice.incrementNumMushrooms();
                    if (pizza[elDown.getI()][j].getElement() == 1)
                        downSlice.incrementNumTomatoes();
                    if (pizza[elDown.getI()][j].getSliceMark() != -1 &&
                            pizza[elDown.getI()][j].getSliceMark() != sliceMark) {
                        return null;
                    }
                }
            }
            return downSlice;
        }
        return null;
    }
    
    private Element getMaximumElement (Element e1, Element e2) {
        if ((e1.getI() > e2.getI()) ||
                (e1.getI() == e2.getI() &&
                        e1.getJ() > e2.getJ()))
            return e1;
        else 
            return e2;
    }
    
    private Element getMinimumElement (Element e1, Element e2) {
        if ((e1.getI() < e2.getI()) ||
                (e1.getI() == e2.getI() &&
                        e1.getJ() < e2.getJ()))
            return e1;
        else 
            return e2;
    }
    
    public Element[][] getPizza() {
        return pizza;
    }

    public void setPizza(Element[][] pizza) {
        this.pizza = pizza;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getMinimumNumberOfEachElement() {
        return minimumNumberOfEachElement;
    }

    public void setMinimumNumberOfEachElement(int minimumNumberOfEachElement) {
        this.minimumNumberOfEachElement = minimumNumberOfEachElement;
    }

    public int getMaximumNumberOfCells() {
        return maximumNumberOfCells;
    }

    public void setMaximumNumberOfCells(int maximumNumberOfCells) {
        this.maximumNumberOfCells = maximumNumberOfCells;
    }

    public int getCountMushrooms() {
        return countMushrooms;
    }

    public void setCountMushrooms(int countMushrooms) {
        this.countMushrooms = countMushrooms;
    }

    public int getCountTomatoes() {
        return countTomatoes;
    }

    public void setCountTomatoes(int countTomatoes) {
        this.countTomatoes = countTomatoes;
    }

    public int getMinimalElement() {
        return minimalElement;
    }

    public void setMinimalElement(int minimalElement) {
        this.minimalElement = minimalElement;
    }

    public List<Slice> getMinimalElements() {
        return minimalElements;
    }

    public void setMinimalElements(List<Slice> minimalElements) {
        this.minimalElements = minimalElements;
    }

    public List<Slice> getSlicesResult() {
        return slicesResult;
    }

    public void setSlicesResult(List<Slice> slicesResult) {
        this.slicesResult = slicesResult;
    }

    public int getTotalSlicesSquare() {
        return totalSlicesSquare;
    }

    public void setTotalSlicesSquare(int totalSlicesSquare) {
        this.totalSlicesSquare = totalSlicesSquare;
    }

//    public PizzaVisualizationWindow getVisualizer() {
//        return visualizer;
//    }
//
//    public void setVisualizer(PizzaVisualizationWindow visualizer) {
//        this.visualizer = visualizer;
//        this.visualize = true;
//    }
    
    public void clearData() {
        this.pizza = null;
        this.rows = 0;
        this.cols = 0;
        this.minimumNumberOfEachElement = 0;
        this.maximumNumberOfCells = 0;
        this.countMushrooms = 0;
        this.countTomatoes = 0;
        this.minimalElement = 0;
        this.totalSlicesSquare = 0;
        this.minimalElements = new ArrayList<Slice> ();
        this.slicesResult = new ArrayList<Slice> ();
    }
    
}
