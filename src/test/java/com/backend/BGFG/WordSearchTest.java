package com.backend.BGFG;

import org.junit.jupiter.api.Test;

class WordSearchTest {

    @Test
    void generateEasyWordSearch() {
        WordSearch wordSearch = new WordSearch(true);
        wordSearch.generateWordSearch(Difficulty.HARD);
//        wordSearch.prettyPrintWordSearch();
        wordSearch.prettyPrintWordSearchPlaced();
        String a = "";
//        assert(false);
    }

    @Test
    void getWords() {
    }
}