package com.pizzacutter;

import java.util.Comparator;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
public class SliceComparator implements Comparator<Slice>{
    @Override
    public int compare(Slice o1, Slice o2) {
        
        // if both slices do not satisfy minimal conditions
        if (!o1.checkMinimumConditions() &&
                !o2.checkMinimumConditions()) {
            
            // choose o1 or o2 depeding on
            // maximal quantity of minimal element
            
            return o1.compareMaximalityOfMinimalElement(o1, o2);
        }
        
        if (o1.checkMinimumConditions() &&
                !o2.checkMinimumConditions()) {
            return 1;
        }
        if (o2.checkMinimumConditions() &&
                !o1.checkMinimumConditions()) {
            return -1;
        }  
        
        
        // if both slices satisfy minimal conditions
        if (o1.checkMinimumConditions() &&
                o2.checkMinimumConditions()) {
            
            // choose o1 or o2 depeding on
            // minimal quantity of minimal element
            // (because minimal element can be taken for
            //  other slice)
            
            return o1.compareMinimalityOfMinimalElement(o1, o2);
        }

        
        return 0;
    }
}
