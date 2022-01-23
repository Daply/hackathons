package com.pizzacutter;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
public class Slice{
    private int countNumTomatoes = 0;
    private int countNumMushrooms = 0;
    private int countCells = 0;
    
    private Element startElement = null;
    private Element endElement = null;
    
    /**
     * shape of slice
     * - always must be a rectangle
     */
    private SliceShape sliceShape = new SliceShape();
    
    /**
     * total cost of all elements of slice
     */
    private int totalElementsCost = 0;
    
    /**
     * minimal condition of slice
     */
    private int minimumNumberOfEachElement = 0;
    private int maximumNumberOfCells = 0;

    /**
     * mushroom (0) or tomato (1)
     */
    private int minimalElement = 0;
    
    public Slice(Element startElement, Element endElement, 
            int minimumNumberOfEachElement, 
            int maximumNumberOfCells, int minimalElement) {
        super();
        this.startElement = startElement;
        this.endElement = endElement;
        this.minimumNumberOfEachElement = minimumNumberOfEachElement;
        this.maximumNumberOfCells = maximumNumberOfCells;
        this.minimalElement = minimalElement;
        this.sliceShape.length = this.endElement.getJ() - this.startElement.getJ() + 1;
        this.sliceShape.width = this.endElement.getI() - this.startElement.getI() + 1;
    }
    
    public boolean intersects(Slice slice) {
        if ((this.endElement.getI() >= slice.getStartElement().getI() &&
                this.endElement.getJ() >= slice.getStartElement().getJ()))
            return true;
        else 
            return false;
    }
    
    public boolean containsElement(Element el) {
        int iStart = this.startElement.getI();
        int jStart = this.startElement.getJ(); 
        int iEnd = this.endElement.getI();
        int jEnd = this.endElement.getJ(); 
        int iEl = el.getI();
        int jEl = el.getJ(); 
        if (iEl >= iStart && iEl <= iEnd &&
                jEl >= jStart && jEl <= jEnd)
            return true;
        else 
            return false;
    }
    
    @Override
    public String toString() {
        return "Slice [countNumTomatoes=" + countNumTomatoes + ", countNumMushrooms=" + countNumMushrooms
                + ", countCells=" + countCells + ", startElement=" + startElement + ", endElement=" + endElement + "]";
    }

    @Override
    public int hashCode() {
        return (this.startElement.getI() +
                this.startElement.getJ() +
                this.endElement.getI() +
                this.endElement.getJ() +
                this.countNumMushrooms +
                this.countNumTomatoes +
                this.sliceShape.length +
                this.sliceShape.width)*31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Slice other = (Slice) obj;
        if (this.startElement.getI() == other.startElement.getI() &&
                this.startElement.getJ() == other.startElement.getJ() &&
                this.endElement.getI() == other.endElement.getI() &&
                this.endElement.getJ() == other.endElement.getJ()) 
            return true;
        return false;
    }

    public boolean checkMinimumConditions() {
        if (this.countNumTomatoes >= this.minimumNumberOfEachElement &&
                this.countNumMushrooms >= this.minimumNumberOfEachElement &&
                this.countCells <= this.maximumNumberOfCells) 
            return true;
        else 
            return false;
    }
    
    public boolean checkMaximumConditions() {
        if (checkMinimumConditions() &&
                countCells == maximumNumberOfCells) 
            return true;
        else 
            return false;
    }
    
    public int getCountNumTomatoes() {
        return countNumTomatoes;
    }
    public void setCountNumTomatoes(int countNumTomatoes) {
        this.countNumTomatoes = countNumTomatoes;
    }
    public void incrementNumTomatoes() {
        this.countNumTomatoes++;
    }
    public int getCountNumMushrooms() {
        return countNumMushrooms;
    }
    public void setCountNumMushrooms(int countNumMushrooms) {
        this.countNumMushrooms = countNumMushrooms;
    }
    public void incrementNumMushrooms() {
        this.countNumMushrooms++;
    }
    public int getCountCells() {
        return countCells;
    }
    public void setCountCells(int countCells) {
        this.countCells = countCells;
    }
    public void incrementCells() {
        this.countCells++;
    }
    public Element getStartElement() {
        return startElement;
    }
    public void setStartElement(Element startElement) {
        this.startElement = startElement;
    }
    public Element getEndElement() {
        return endElement;
    }
    public void setEndElement(Element endElement) {
        this.endElement = endElement;
    }
    
    public SliceShape getSliceShape() {
        return sliceShape;
    }

    public void setSliceShape(SliceShape sliceShape) {
        this.sliceShape = sliceShape;
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

    public int getMinimalElement() {
        return minimalElement;
    }

    public void setMinimalElement(int minimalElement) {
        this.minimalElement = minimalElement;
    }

    public int getTotalElementsCost() {
        return totalElementsCost;
    }

    public void setTotalElementsCost(int totalElementsCost) {
        this.totalElementsCost = totalElementsCost;
    }

    class SliceShape {

        /**
         * example:
         * length = 5, width = 3
         *    * * * * * 
         *    * * * * * 
         *    * * * * * 
         */
        
        private int length = 0;
        private int width = 0;
        
        public SliceShape() {
            
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
        
        
    }
    
    public int compareMinimalityOfMinimalElement(Slice o1, Slice o2) {
        // mushrooms
        if (this.minimalElement == 0) {
            if (o1.countNumMushrooms < o2.countNumMushrooms) {
                return 1;
            }
            if (o1.countNumMushrooms > o2.countNumMushrooms) {
                return -1;
            }
            if (o1.countNumMushrooms == o2.countNumMushrooms) {
                if (o1.countNumTomatoes > o2.countNumTomatoes) {
                    return 1;
                }
                if (o1.countNumTomatoes < o2.countNumTomatoes) {
                    return -1;
                }
            }
        }
        // tomatoes
        else {
            if (o1.countNumTomatoes < o2.countNumTomatoes) {
                return 1;
            }
            if (o1.countNumTomatoes > o2.countNumTomatoes) {
                return -1;
            }
            if (o1.countNumTomatoes == o2.countNumTomatoes) {
                if (o1.countNumMushrooms > o2.countNumMushrooms) {
                    return 1;
                }
                if (o1.countNumMushrooms < o2.countNumMushrooms) {
                    return -1;
                }
            }
        } 
        return 0;
    }
    
    public int compareMaximalityOfMinimalElement(Slice o1, Slice o2) {
        // mushrooms
        if (this.minimalElement == 0) {
            if (o1.countNumMushrooms > o2.countNumMushrooms) {
                return 1;
            }
            if (o1.countNumMushrooms < o2.countNumMushrooms) {
                return -1;
            }
            if (o1.countNumMushrooms == o2.countNumMushrooms) {
                if (o1.countNumTomatoes > o2.countNumTomatoes) {
                    return 1;
                }
                if (o1.countNumTomatoes < o2.countNumTomatoes) {
                    return -1;
                }
            }
        }
        // tomatoes
        else {
            if (o1.countNumTomatoes > o2.countNumTomatoes) {
                return 1;
            }
            if (o1.countNumTomatoes < o2.countNumTomatoes) {
                return -1;
            }
            if (o1.countNumTomatoes == o2.countNumTomatoes) {
                if (o1.countNumMushrooms > o2.countNumMushrooms) {
                    return 1;
                }
                if (o1.countNumMushrooms < o2.countNumMushrooms) {
                    return -1;
                }
            }
        } 
        return 0;
    }
}
