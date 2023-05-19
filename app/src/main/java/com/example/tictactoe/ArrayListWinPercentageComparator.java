package com.example.tictactoe;

import java.util.Comparator;

// Comparator for sorting the data by their win percentages
public class ArrayListWinPercentageComparator  implements Comparator<Score> {
    @Override
    public int compare(Score s1, Score s2) {
        return Double.compare(s1.getWinPercentage(), s2.getWinPercentage());
    }
}
