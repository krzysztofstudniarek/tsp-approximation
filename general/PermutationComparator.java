/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import java.util.Comparator;

/**
 *
 * @author Krzysiek
 */
public class PermutationComparator implements Comparator<Permutation> {

    @Override
    public int compare(Permutation o1, Permutation o2) {
        
        return (int)(((Permutation)o1).getStrength() - ((Permutation)o2).getStrength());
    
    }
    
}
