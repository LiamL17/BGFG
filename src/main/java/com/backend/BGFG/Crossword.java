package com.backend.BGFG;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Crossword {

    public String[][] crossword;
    public boolean[][] crosswordPlaced;
    public int size;

    public boolean debug;

    private int MAX_LENGTH_OF_WORD;
    private int WORDS_COUNT;

    public Crossword() {
    }

    public Crossword(boolean debug) {
        this.debug = debug;
    }

    public void generateCrossword(Difficulty difficulty) {
        switch (difficulty) {
            case EASY -> {
                this.MAX_LENGTH_OF_WORD = 5;
                this.WORDS_COUNT = 10;
                this.size = 10;
                generateEasyCrossword(); // Left to right, top to bottom
            }
            case MEDIUM -> {
                this.MAX_LENGTH_OF_WORD = 7;
                this.WORDS_COUNT = 12;
                this.size = 14;
                generateMediumCrossword(); // Vertical, horizontal
            }
            case HARD -> {
                this.MAX_LENGTH_OF_WORD = 10;
                this.WORDS_COUNT = 14;
                this.size = 16;
                generateHardCrossword(); // Diagonals
            }
            case EXPERT -> {
                this.MAX_LENGTH_OF_WORD = 15;
                this.WORDS_COUNT = 16;
                this.size = 20;
                generateExpertCrossword(); // Reverse diagonal
            }
        }
    }

    public void initializeGrid() {
        this.crossword = new String[size][size];
        this.crosswordPlaced = new boolean[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Random r = new Random();
                this.crossword[i][j] = String.valueOf((char) (r.nextInt(26) + 'A'));
                this.crosswordPlaced[i][j] = false;
            }
        }
    }

    /*
     * Allow vertical and horizontal with left to right and top to bottom.
     */
    private void generateEasyCrossword() {
        initializeGrid();
        List<String> words = getWords(new Words().getWords(), MAX_LENGTH_OF_WORD, WORDS_COUNT);
        List<WordDirection> directions = new ArrayList<>(EnumSet.of(WordDirection.Vertical, WordDirection.Horizontal));
        placeWords(words, directions);
    }

    /*
     * Allow vertical and horizontal with left to right and top to bottom.
     */
    private void generateMediumCrossword() {
        initializeGrid();
        List<String> words = getWords(new Words().getWords(), MAX_LENGTH_OF_WORD, WORDS_COUNT);
        List<WordDirection> directions = new ArrayList<>(
                EnumSet.complementOf(
                        EnumSet.of(WordDirection.Diagonal, WordDirection.DiagonalReverse)
                )
        );
        placeWords(words, directions);
    }

    /*
     * Allow vertical and horizontal with left to right and top to bottom.
     */
    private void generateHardCrossword() {
        initializeGrid();
        List<String> words = getWords(new Words().getWords(), MAX_LENGTH_OF_WORD, WORDS_COUNT);
        List<WordDirection> directions = new ArrayList<>(EnumSet.complementOf(EnumSet.of(WordDirection.DiagonalReverse)));
        placeWords(words, directions);
    }

    /*
     * Allow vertical and horizontal with left to right and top to bottom.
     */
    private void generateExpertCrossword() {
        initializeGrid();
        List<String> words = getWords(new Words().getWords(), MAX_LENGTH_OF_WORD, WORDS_COUNT);
        List<WordDirection> directions = new ArrayList<>(EnumSet.allOf(WordDirection.class));
        placeWords(words, directions);
    }

    public void placeWords(List<String> words, List<WordDirection> directions) {
        Random random = new Random();
        for (String word : words) {
            WordDirection direction = directions.get(random.nextInt(directions.size()));
            boolean isValid = false;
            while (!isValid) {
                if (EnumSet.of(WordDirection.VerticalReverse, WordDirection.HorizontalReverse, WordDirection.DiagonalReverse)
                                .contains(direction)) {
                    word = new StringBuilder(word).reverse().toString();
                }
                switch (direction) {
                    case Vertical, VerticalReverse -> {
                        // Select the col
                        int col = random.nextInt(0, this.size);
                        int row = random.nextInt(0, this.size - word.length());
                        if (canPlaceVertical(row, col, word.length())) {
                            placeWordVertical(word, row, col);
                            isValid = true;
                        }
                    }
                    case Horizontal, HorizontalReverse -> {
                        // Select the row
                        int row = random.nextInt(0, this.size);
                        int col = random.nextInt(0, this.size - word.length());
                        if (canPlaceHorizontal(row, col, word.length())) {
                            placeWordHorizontal(word, row, col);
                            isValid = true;
                        }
                    }
                    case Diagonal, DiagonalReverse -> {
                        isValid = true;
                    }
                }
            }
        }


        String b = "a";
    }

    public void placeWordHorizontal(String word, int row, int col) {
        for (int i = 0; i < word.length(); ++i) {
            String character = String.valueOf(word.charAt(i)).toUpperCase();
            if (debug) {
                this.crossword[row][col + i] = ANSIColor.ANSI_GREEN + character + ANSIColor.ANSI_RESET;
            } else {
                this.crossword[row][col + i] = character;
            }
            this.crosswordPlaced[row][col + i] = true;
        }
    }

    public void placeWordVertical(String word, int row, int col) {
        for (int i = 0; i < word.length(); ++i) {
            String character = String.valueOf(word.charAt(i)).toUpperCase();
            if (debug) {
                this.crossword[row + i][col] = ANSIColor.ANSI_GREEN + character + ANSIColor.ANSI_RESET;
            } else {
                this.crossword[row + i][col] = character;
            }
            this.crosswordPlaced[row + i][col] = true;
        }
    }

    private boolean canPlaceHorizontal(int row, int col, int wordLength) {
        for (int i = col; i < col + wordLength; ++i) {
            if (this.crosswordPlaced[row][i]) {
                return false;
            }
        }

        return true;
    }

    private boolean canPlaceVertical(int row, int col, int wordLength) {
        for (int i = row; i < row + wordLength; ++i) {
            if (this.crosswordPlaced[i][col]) {
                return false;
            }
        }

        return true;
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

    public void prettyPrintCrossword() {
        StringBuilder horizontal = new StringBuilder();
        horizontal.append(" ".repeat(5));
        for (int i = 0; i < crossword.length; ++i) {
            horizontal.append((char) ('A' + i)).append(" ");
        }
        System.out.println(horizontal);
        System.out.println(" ".repeat(5) + "_ ".repeat(crossword.length));

        for (int i = 0; i < crossword.length; ++i) {
            System.out.print((i + 1) + " ".repeat(2 - ((i + 1) / 10)) + "| ");
            for (String item : crossword[i]) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }

    public void prettyPrintCrosswordPlaced() {
        StringBuilder horizontal = new StringBuilder();
        horizontal.append(" ".repeat(5));
        for (int i = 0; i < crosswordPlaced.length; ++i) {
            horizontal.append((char) ('A' + i)).append(" ");
        }
        System.out.println(horizontal);
        System.out.println(" ".repeat(5) + "_ ".repeat(crosswordPlaced.length));

        for (int i = 0; i < crosswordPlaced.length; ++i) {
            System.out.print((i + 1) + " ".repeat(2 - ((i + 1) / 10)) + "| ");
            for (boolean item : crosswordPlaced[i]) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }
}
