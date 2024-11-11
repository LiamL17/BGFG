package com.backend.BGFG;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Stack;

class CrosswordTest {

    @Test
    void generateCrossword() {

    }

    @Test
    void initializeGrid() {
    }

    @Test
    void generateCrosswordWithSize() {
    }

    @Test
    void generateEasyCrossword() {
        Crossword crossword = new Crossword(true);
        crossword.generateCrossword(Difficulty.HARD);
        crossword.prettyPrintCrossword();
        String a = "";
//        assert(false);
    }

    @Test
    void getWords() {
    }
}