package com.pizzacutter;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
public class Element{

    /**
     * mushroom (0) or tomato (1)
     */
    private int element = 0;
    
    /**
     * surrounding elements count (mushroom (0) or tomato (1))
     */
    private int countSurroundElementMushroom = 0;
    private int countSurroundElementTomato = 0;
    
    /**
     * coordinates
     */
    private int i = 0;
    private int j = 0;
    
    private Element left = null;
    private Element right = null;
    private Element up = null;
    private Element down = null;
    
    /**
     * element cost
     * - depends on its surrounding elements
     * - counts as: 
     */
    private int elementCost = 0;
    
    /**
     * mark, that means to which slice this
     * element belongs
     * - 1 - no slice, > 0 - slice number
     */
    private int sliceMark = -1;
    
    public Element(int i, int j, int element) {
        this.i = i;
        this.j = j;
        this.element = element;
    }
    
    @Override
    public String toString() {
        return "Element [element=" + element + ", i=" + i + ", j=" + j + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Element other = (Element) obj;
        if (down == null) {
            if (other.down != null)
                return false;
        } else if (!down.equals(other.down))
            return false;
        if (element != other.element)
            return false;
        if (i != other.i)
            return false;
        if (j != other.j)
            return false;
        if (right == null) {
            if (other.right != null)
                return false;
        } else if (!right.equals(other.right))
            return false;
        return true;
    }

    public int getElement() {
        return element;
    }

    public void setElement(int element) {
        this.element = element;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public Element getRight() {
        return right;
    }

    public void setRight(Element right) {
        this.right = right;
    }

    public Element getDown() {
        return down;
    }

    public void setDown(Element down) {
        this.down = down;
    }

    public Element getLeft() {
        return left;
    }

    public void setLeft(Element left) {
        this.left = left;
    }

    public Element getUp() {
        return up;
    }

    public void setUp(Element up) {
        this.up = up;
    }

    public int getCountSurroundElementMushroom() {
        return countSurroundElementMushroom;
    }

    public void setCountSurroundElementMushroom(int countSurroundElementMushroom) {
        this.countSurroundElementMushroom = countSurroundElementMushroom;
    }
    
    public void incrementCountSurroundElementMushroom() {
        this.countSurroundElementMushroom++;
    }

    public int getCountSurroundElementTomato() {
        return countSurroundElementTomato;
    }

    public void setCountSurroundElementTomato(int countSurroundElementTomato) {
        this.countSurroundElementTomato = countSurroundElementTomato;
    }
    
    public void incrementCountSurroundElementTomato() {
        this.countSurroundElementTomato++;
    }

    public int getSliceMark() {
        return sliceMark;
    }

    public void setSliceMark(int sliceMark) {
        this.sliceMark = sliceMark;
    }

    public int getElementCost() {
        return elementCost;
    }

    public void setElementCost(int elementCost) {
        this.elementCost = elementCost;
    }
    
}
