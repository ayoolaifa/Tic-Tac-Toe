package com.example.tictactoe;

import java.util.Comparator;
import java.util.Map;

// Comparator for sorting the data by their player score
public class ArrayListComparator implements Comparator<Score> {
    @Override
    public int compare(Score s1, Score s2) {
        return Integer.compare(s1.getPscore(), s2.getPscore());
    }
}
