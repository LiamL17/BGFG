package com.backend.BGFG;

import org.junit.jupiter.api.Test;

class WordSearchTest {

    @Test
    void generateEasyWordSearch() {
        WordSearch wordSearch = new WordSearch(true);
        wordSearch.generateWordSearch(Difficulty.HARD);
        wordSearch.prettyPrintCrossword();
        String a = "";
//        assert(false);
    }

    @Test
    void getWords() {
    }
}