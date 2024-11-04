package com.backend.BGFG

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WordScramblerTest {

    val guesser = WordScrambler()

    @Test
    fun `success single letter word`() {
        val scramble = guesser.shuffleSingleWord("o")
        assertThat(scramble).isEqualTo("o")
    }

    @Test
    fun `success two letter word`() {
        val scramble = guesser.shuffleSingleWord("ab")
        assertThat(scramble).matches("ab|ba")
    }

    @Test
    fun `success three letter word`() {
        val scramble = guesser.shuffleSingleWord("abc")
        assertThat(scramble).matches("abc|acb|bac|bca|cab|cba")
    }

    @Test
    fun `success any word`() {
        val word = "ThisIsALongStretchedWordWithFoxAndQueen"
        val scramble = guesser.shuffleSingleWord(word)
        assertThat(scramble.toList())
            .hasSameSizeAs(word.toList())
            .containsExactlyInAnyOrder(*word.toList().toTypedArray())
    }

    @Test
    fun `success for length of difficulties`() {
        assertThat(guesser.scrambleWithDifficulty(Difficulty.EASY).length)
            .isLessThan(7)

        assertThat(guesser.scrambleWithDifficulty(Difficulty.MEDIUM).length)
            .isStrictlyBetween(4, 10)

        assertThat(guesser.scrambleWithDifficulty(Difficulty.HARD).length)
            .isGreaterThan(6)

        assertThat(guesser.scrambleWithDifficulty(Difficulty.EXPERT).length)
            .isGreaterThan(9)
    }

    @Test
    fun `success anagram`() {
        val original = "Jesus"
        val scramble = guesser.scrambleWord(original)
        assertThat(guesser.anagramCheck(original, scramble))
            .isTrue()
    }

    @Test
    fun `success easy choice`() {
        val easyScramble = guesser.scrambleWithDifficulty(Difficulty.EASY, listOf("Jesus"))
        assertThat(easyScramble)
            .startsWith("J")
            .endsWith("s")
            .hasSizeLessThan(7)
    }

    @Test
    fun `success easy choice with short word`() {
        val easyScramble = guesser.scrambleWithDifficulty(Difficulty.EASY, listOf("God"))
        assertThat(easyScramble)
            .doesNotStartWith("G")
            .doesNotEndWith("d")
            .hasSize(3)
    }

}