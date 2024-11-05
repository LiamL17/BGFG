package com.backend.BGFG;

import org.junit.jupiter.api.Test;

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
        Crossword crossword = new Crossword(10);
        crossword.generateEasyCrossword(10);
        crossword.prettyPrint();
//        assert(false);
    }

    @Test
    void getWords() {
    }
}