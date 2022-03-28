package domain;

import java.util.Comparator;

class CostComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        Cell s1 = (Cell) o1;
        Cell s2 = (Cell) o2;

        if (s1.getFinalCost() == s2.getFinalCost()) {
            if (s1.getCellY() > s2.getCellY())
                return -1;
            else if (s1.getCellY() < s2.getCellY())
                return 1;
            else
                return 0;
        } else if (s1.getFinalCost() > s2.getFinalCost())
            return 1;
        else
            return -1;
    }

}
