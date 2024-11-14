package com.backend.BGFG;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WordSearch {

    public String[][] board;
    public boolean[][] boardPlaced;
    public int size;

    public boolean debug;

    private int MAX_LENGTH_OF_WORD;
    private int WORDS_COUNT;

    /**
     * Basic empty constructor.
     */
    public WordSearch() {
    }

    /**
     * Constructor with debug boolean flag used to pretty print.
     * @param debug used as a flag for output
     */
    public WordSearch(boolean debug) {
        this.debug = debug;
    }

    /**
     * Generate a word search based on [Difficulty]. Easy of the difficulties
     * has a different set of rules followed. These are:
     * Easy:
     *  Max length of word: 5
     *  Words to search: 10
     *  Size of grid: 10x10
     *  Directions: Vertical and horizontal, no reverse
     * Medium:
     *  Max length of word: 7
     *  Words to search: 12
     *  Size of grid: 14x14
     *  Directions: Vertical and horizontal, can be reversed
     * Hard:
     *  Max length of word: 10
     *  Words to search: 14
     *  Size of grid: 16x16
     *  Directions: Vertical, horizontal, and horizontal. No reverse.
     * Expert:
     *  Max length of word: 15
     *  Words to search: 16
     *  Size of grid: 20x20
     *  Directions: Any direction. Can be reversed.
     *
     * @param difficulty to determine how the board is constructed
     */
    public void generateWordSearch(Difficulty difficulty) {
        switch (difficulty) {
            case EASY -> {
                this.MAX_LENGTH_OF_WORD = 5;
                this.WORDS_COUNT = 10;
                this.size = 10;
                generateEasyWordSearch(); // Left to right, top to bottom
            }
            case MEDIUM -> {
                this.MAX_LENGTH_OF_WORD = 7;
                this.WORDS_COUNT = 12;
                this.size = 14;
                generateMediumWordSearch(); // Vertical, horizontal
            }
            case HARD -> {
                this.MAX_LENGTH_OF_WORD = 10;
                this.WORDS_COUNT = 14;
                this.size = 16;
                generateHardWordSearch(); // Diagonals
            }
            case EXPERT -> {
                this.MAX_LENGTH_OF_WORD = 15;
                this.WORDS_COUNT = 16;
                this.size = 20;
                generateExpertWordSearch(); // Reverse diagonal
            }
        }
    }

    /**
     * Sets each cell on the Word Search board to a random letter along with
     * setting another grid of booleans to false.
     * The second grid is used to determine where words are placed.
     */
    public void initializeGrid() {
        this.board = new String[size][size];
        this.boardPlaced = new boolean[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Random r = new Random();
                this.board[i][j] = String.valueOf((char) (r.nextInt(26) + 'A'));
                this.boardPlaced[i][j] = false;
            }
        }
    }

    /**
     * Generates an easy Word Search which has predefined config values. See
     * [generateWordSearch] function. This function also dictates the direction
     * of words that will be placed. In this case, only vertical and horizontal
     * without reversing the words.
     */
    private void generateEasyWordSearch() {
        initializeGrid();
        List<String> words = getWords(new Words().getWords(), MAX_LENGTH_OF_WORD, WORDS_COUNT);
        List<WordDirection> directions = new ArrayList<>(EnumSet.of(WordDirection.Vertical, WordDirection.Horizontal));
        placeWords(words, directions);
    }

    /**
     * Generates a medium Word Search which has predefined config values. See
     * [generateWordSearch] function. This function also dictates the direction
     * of words that will be placed. In this case no diagonal, but reverse
     * allowed.
     */
    private void generateMediumWordSearch() {
        initializeGrid();
        List<String> words = getWords(new Words().getWords(), MAX_LENGTH_OF_WORD, WORDS_COUNT);
        List<WordDirection> directions = new ArrayList<>(
                EnumSet.complementOf(
                        EnumSet.of(WordDirection.Diagonal, WordDirection.DiagonalReverse)
                )
        );
        placeWords(words, directions);
    }

    /**
     * Generates a hard Word Search which has predefined config values. See
     * [generateWordSearch] function. This function also dictates the direction
     * of words that will be placed. In this case all but diagonal reversed.
     */
    private void generateHardWordSearch() {
        initializeGrid();
        List<String> words = getWords(new Words().getWords(), MAX_LENGTH_OF_WORD, WORDS_COUNT);
        List<WordDirection> directions = new ArrayList<>(EnumSet.complementOf(EnumSet.of(WordDirection.DiagonalReverse)));
        placeWords(words, directions);
    }

    /**
     * Generates an expert Word Search which has predefined config values. See
     * [generateWordSearch] function. This function also dictates the direction
     * of words that will be placed. In this case any direction.
     */
    private void generateExpertWordSearch() {
        initializeGrid();
        List<String> words = getWords(new Words().getWords(), MAX_LENGTH_OF_WORD, WORDS_COUNT);
        List<WordDirection> directions = new ArrayList<>(EnumSet.allOf(WordDirection.class));
        placeWords(words, directions);
    }

    /**
     * Place words on the [board] using the [boardPlaced] to avoid overlaps.
     * This function uses the direction of a word and places it on the board.
     *
     * @param words the words to place
     * @param directions the directions of the words
     */
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
                        int col = random.nextInt(0, this.size);
                        int row = random.nextInt(0, this.size - word.length());
                        if (canPlaceVertical(row, col, word.length())) {
                            placeWordVertical(word, row, col);
                            isValid = true;
                        }
                    }
                    case Horizontal, HorizontalReverse -> {
                        int row = random.nextInt(0, this.size);
                        int col = random.nextInt(0, this.size - word.length());
                        if (canPlaceHorizontal(row, col, word.length())) {
                            placeWordHorizontal(word, row, col);
                            isValid = true;
                        }
                    }
                    case Diagonal, DiagonalReverse -> {
                        int row = random.nextInt(0, this.size - word.length());
                        int col = random.nextInt(0, this.size - word.length());
                        if (canPlaceDiagonal(row, col, word.length())) {
                            placeWordDiagonal(word, row, col);
                            isValid = true;
                        }
                    }
                }
            }
        }
    }

    /**
     *  Place a word horizontally on the board. This will keep the row constant
     *  and move along the columns to place.
     *  If the debug flag is true it will print a placed letter in green.
     *
     * @param word the word to place
     * @param row the row to start placement
     * @param col the column to start placement
     */
    public void placeWordHorizontal(String word, int row, int col) {
        for (int i = 0; i < word.length(); ++i) {
            String character = String.valueOf(word.charAt(i)).toUpperCase();
            if (debug) {
                this.board[row][col + i] = ANSIColor.ANSI_GREEN + character + ANSIColor.ANSI_RESET;
            } else {
                this.board[row][col + i] = character;
            }
            this.boardPlaced[row][col + i] = true;
        }
    }

    /**
     *  Place a word vertically on the board. This will keep the column
     *  constant and move along the rows to place.
     *  If the debug flag is true it will print a placed letter in green.
     *
     * @param word the word to place
     * @param row the row to start placement
     * @param col the column to start placement
     */
    public void placeWordVertical(String word, int row, int col) {
        for (int i = 0; i < word.length(); ++i) {
            String character = String.valueOf(word.charAt(i)).toUpperCase();
            if (debug) {
                this.board[row + i][col] = ANSIColor.ANSI_GREEN + character + ANSIColor.ANSI_RESET;
            } else {
                this.board[row + i][col] = character;
            }
            this.boardPlaced[row + i][col] = true;
        }
    }

    /**
     *  Place a word diagonally on the board. Both the row and column will move
     *  as the word is placed.
     *  If the debug flag is true it will print a placed letter in green.
     *
     * @param word the word to place
     * @param row the row to start placement
     * @param col the column to start placement
     */
    public void placeWordDiagonal(String word, int row, int col) {
        for (int i = 0; i < word.length(); ++i) {
            String character = String.valueOf(word.charAt(i)).toUpperCase();
            if (debug) {
                this.board[row + i][col + i] = ANSIColor.ANSI_GREEN + character + ANSIColor.ANSI_RESET;
            } else {
                this.board[row + i][col + i] = character;
            }
            this.boardPlaced[row + i][col + i] = true;
        }
    }

    /**
     * Validate if a word can be placed horizontally. This function uses the
     * starting row and col parameters and moves along the columns checking
     * if a cell is false. This false value implies that no letter has been
     * placed on this cell yet, making it available to be placed.
     *
     * @param row starting row
     * @param col starting column
     * @param wordLength length of the word
     * @return true if a word can be placed, false otherwise
     */
    private boolean canPlaceHorizontal(int row, int col, int wordLength) {
        for (int i = col; i < col + wordLength; ++i) {
            if (this.boardPlaced[row][i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validate if a word can be placed vertically. This function uses the
     * starting row and col parameters and moves along the rows checking
     * if a cell is false. This false value implies that no letter has been
     * placed on this cell yet, making it available to be placed.
     *
     * @param row starting row
     * @param col starting column
     * @param wordLength length of the word
     * @return true if a word can be placed, false otherwise
     */
    private boolean canPlaceVertical(int row, int col, int wordLength) {
        for (int i = row; i < row + wordLength; ++i) {
            if (this.boardPlaced[i][col]) {
                return false;
            }
        }

        return true;
    }


    /**
     * Validate if a word can be placed horizontally. This function uses the
     * starting row and col parameters and moves along the both checking
     * if a cell is false. This false value implies that no letter has been
     * placed on this cell yet, making it available to be placed.
     *
     * @param row starting row
     * @param col starting column
     * @param wordLength length of the word
     * @return true if a word can be placed, false otherwise
     */
    private boolean canPlaceDiagonal(int row, int col, int wordLength) {
        for (int i = row, j = col; i < row + wordLength; ++i, ++j) {
            if (this.boardPlaced[i][j]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Retrieve a random amount of words from the provided list of words. Each
     * word must be less than or equal to the provided length.
     *
     * @param words list of words to choose from
     * @param length maximum length of each word
     * @param count amount of words
     * @return random words filtered by length
     */
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

    /**
     * Print the Word Search board. This function prints a top line of letters
     * and a left column with numbers for the rows. Much like a chess board.
     */
    public void prettyPrintWordSearch() {
        StringBuilder horizontal = new StringBuilder();
        horizontal.append(" ".repeat(5));
        for (int i = 0; i < board.length; ++i) {
            horizontal.append((char) ('A' + i)).append(" ");
        }
        System.out.println(horizontal);
        System.out.println(" ".repeat(5) + "_ ".repeat(board.length));

        for (int i = 0; i < board.length; ++i) {
            System.out.print((i + 1) + " ".repeat(2 - ((i + 1) / 10)) + "| ");
            for (String item : board[i]) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }

    /**
     * Print the Word Search placed board. This function prints a top line of
     * letters and a left column with numbers for the rows. Much like a chess
     * board.
     */
    public void prettyPrintWordSearchPlaced() {
        StringBuilder horizontal = new StringBuilder();
        horizontal.append(" ".repeat(5));
        for (int i = 0; i < boardPlaced.length; ++i) {
            horizontal.append((char) ('A' + i)).append(" ");
        }
        System.out.println(horizontal);
        System.out.println(" ".repeat(5) + "_ ".repeat(boardPlaced.length));

        for (int i = 0; i < boardPlaced.length; ++i) {
            System.out.print((i + 1) + " ".repeat(2 - ((i + 1) / 10)) + "| ");
            for (boolean placed : boardPlaced[i]) {
                if (placed) {
                    System.out.print(ANSIColor.ANSI_GREEN + "X" + ANSIColor.ANSI_RESET + " ");
                } else {
                    System.out.print("O ");
                }
            }
            System.out.println();
        }
    }
}
