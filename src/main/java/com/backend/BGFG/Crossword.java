package com.backend.BGFG;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Crossword {

    public String[][] crossword;
    public boolean[][] crosswordPlaced;

    public Crossword(int size) {
        crossword = new String[size][size];
        crosswordPlaced = new boolean[size][size];
    }

    public void generateCrossword(Difficulty difficulty) {
        switch (difficulty) {
            case EASY -> generateCrosswordWithSize(10); // Left to right, top to bottom
            case MEDIUM -> generateCrosswordWithSize(14); // Vertical, horizontal
            case HARD -> generateCrosswordWithSize(16); // Diagonals
            case EXPERT -> generateCrosswordWithSize(20); // Reverse diagonal
        }
    }

    public String[][] initializeGrid(int size) {
        String[][] grid = new String[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Random r = new Random();
                grid[i][j] = String.valueOf((char) (r.nextInt(26) + 'A'));
            }
        }
        return grid;
    }

    public void generateCrosswordWithSize(int size) {
       this.crossword = initializeGrid(size);
    }

    /*
     * Allow vertical and horizontal with left to right and top to bottom.
     */
    public void generateEasyCrossword(int size) {
        this.crossword = initializeGrid(size);
        List<String> words = getWords(new Words().getWords(), 5, 10);
        Map<String, WordDirection> wordDirectionMap = new HashMap<>();

        List<WordDirection> easyDirections = new ArrayList<>(EnumSet.complementOf(EnumSet.of(WordDirection.Diagonal)));
        Random random = new Random();

        for (String word : words) {
            WordDirection randomDirection = easyDirections.get(random.nextInt(easyDirections.size()));
            wordDirectionMap.put(word, randomDirection);
        }



        String b = "a";
    }

    public List<String> getWords(List<String> words, int length, int count) {
       List<String> filtered = words.stream()
               .filter(it -> it.length() <= length)
               .toList();

       return IntStream.generate(() -> new Random().nextInt(filtered.size()))
                .distinct()
                .limit(count)
                .mapToObj(filtered::get)
                .collect(Collectors.toList());
    }

    public void prettyPrint() {
        StringBuilder horizontal = new StringBuilder();
        StringBuilder spacer = new StringBuilder();
        horizontal.repeat(" ", 4);
        for (int i = 0; i < crossword.length; ++i) {
            horizontal.append((char) ('A' + i)).append(" ");
        }
        System.out.println(horizontal);
        System.out.println(spacer.repeat("_ ", crossword.length + 2));

        for (int i = 0; i < crossword.length; ++i) {
            System.out.print((i + 1) + String.valueOf(new StringBuilder().repeat(" ", crossword.length / 10 + 1)) + "| ");
            for (String item : crossword[i]) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }
}
