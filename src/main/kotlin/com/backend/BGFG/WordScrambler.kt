package com.backend.BGFG

enum class Difficulty {
    EASY,
    // Rules:
    // First and last letter, <7 letters

    MEDIUM,
    // Rules:
    // First or last letter, <9 letters

    HARD,
    // No letter, <7 letters

    EXPERT
    // No letter
}

class WordScrambler {
    private val WORDS = listOf(
        "God",
        "Jesus",
        "Holy Spirit",
        "Jesus",
        "Moses",
        "Israel",
        "Jerusalem",
        "David",
        "Bethlehem",
        "Paul",
        "Galilee",
        "Peter",
        "Solomon",
        "Abraham",
        "Messiah",
        "Angel",
        "Cross",
        "Jordan",
        "Grace",
        "Covenant",
        "Temple",
        "Sin",
        "Salvation",
        "Exodus",
        "Prayer",
        "Gospel",
        "Disciples",
        "Prophets",
        "Tabernacle",
        "Sabbath",
        "Resurrection",
        "Faith",
        "Commandments",
        "Eden",
        "Elijah",
        "Judah",
        "Pharaoh",
        "Jacob",
        "Revelation",
        "Apostles",
        "Spirit",
        "Paradise",
        "Manna",
        "Trinity",
        "Samson",
        "Gentiles",
        "Isaiah",
        "Apostle",
        "Law",
        "Crucifixion",
        "Ephraim",
        "Peace",
        "Kingdom",
        "Jerusalem",
        "Galatians",
        "Zechariah",
        "Covenant",
        "Bethlehem",
        "Redemption",
        "Revelation",
        "Crucifixion",
        "Righteous",
        "Salvation",
        "Wilderness",
        "Sacrifice",
        "Commandment",
        "Abundance",
        "Repentance",
        "Tabernacle",
        "Resurrection",
        "Apostleship",
        "Sanctified",
        "Passover",
        "Leviticus",
        "Obedience",
        "Salvation",
        "Discipleship",
        "Anointment",
        "Fellowship",
        "Prodigal Son",
        "Ephesians",
        "Colossians",
        "Faithfulness",
        "Zephaniah",
        "Incarnation",
        "Everlasting",
        "Messianic",
        "Forgiveness",
        "Prophecies",
        "Persecution",
        "Pharisees",
        "Reconciliation",
        "Holy Spirit",
        "Genesis",
        "Revelation",
        "Revelation",
        "Kingdom",
        "Intercession",
        "Consecration",
        "Revelation",
        "Multitudes",
        "Ministry",
        "Repentance",
    )

    /**
     * Call the associated scramble function given a difficulty from provided
     * list of words.
     *
     * @param difficulty a difficulty from the [Difficulty] enum
     * @param words a list of strings
     *
     * @return a single scrambled word
     */
    fun scrambleWithDifficulty(difficulty: Difficulty, words: List<String> = WORDS): String {
        return when (difficulty) {
            Difficulty.EASY -> scrambleEasy(words)
            Difficulty.MEDIUM -> scrambleMedium(words)
            Difficulty.HARD -> scrambleHard(words)
            Difficulty.EXPERT -> scrambleExpert(words)
        }
    }

    /**
     * Scramble a word from the provided list. The selection is a single random
     * word of length less than 7 with its first and last letter kept as is.
     * Furthermore, if the word is length of 3 or less the whole word is
     * shuffled.
     *
     * @param words lis of words
     *
     * @return a single scrambled word
     */
    private fun scrambleEasy(words: List<String>): String {
        val answer = words
            .filter { it.length < 7 }
            .random()
        val length = answer.length

        if (length <= 3) {
            return shuffleSingleWord(answer)
        }

        return answer.first() + scrambleWord(answer.substring(1, length - 1)) + answer.last()
    }

    /**
     * Scramble a word from the provided list. The selection is a single random
     * word of length between 5 and 9, both inclusive. The first and last letters
     * are kept as is.
     *
     * @param words lis of words
     *
     * @return a single scrambled word of length 10 or more
     */
    private fun scrambleMedium(words: List<String>): String {
        val answer = words
            .filter { it.length in 5..9 }
            .random()
        val length = answer.length

        return answer.first() + scrambleWord(answer.substring(1, length - 1)) + answer.last()
    }

    /**
     * Scramble a word from the provided list. The selection is a single random
     * word of length between 7 and 12, both inclusive.
     *
     * @param words lis of words
     *
     * @return a single scrambled word of length 10 or more
     */
    private fun scrambleHard(words: List<String>): String {
        val answer = words
            .filter { it.length in 7..12 }
            .random()

        return scrambleWord(answer)
    }

    /**
     * Scramble a word from the provided list. The selection is a single random
     * word of length 10 or more.
     *
     * @param words lis of words
     *
     * @return a single scrambled word of length 10 or more
     */
    private fun scrambleExpert(words: List<String>): String {
        val answer = words
            .filter { it.length > 9 }
            .random()

        return scrambleWord(answer)
    }

    /**
     * Scramble a word which might contain a space. The spaces are added back.
     *
     * @param chosen the word to scramble
     *
     * @return the scrambled word with spaces kept as is
     */
    fun scrambleWord(chosen: String): String {
        val words = chosen.split(" ")
        return words.joinToString(" ") { shuffleSingleWord(it) }
    }

    /**
     * Shuffle a word of length more than one. If a string is of length one,
     * itself is returned. This also ensures that the original and shuffled
     * word aren't the same.
     *
     * @param word The word to shuffle.
     *
     * @return the shuffled word
     */
    fun shuffleSingleWord(word: String): String {
        if (word.length == 1) return word
        var ans: String;
        do {
            ans = word.toList().shuffled().joinToString("")
        } while (ans == word)
        return ans
    }

    /**
     * Validate that two words are anagrams of one another.
     * Two words are anagrams of one another if they use the same and same amount of letter.
     *
     * @param original the original string
     * @param compare the string comparing to
     *
     * @return true if anagram, false otherwise
     */
    fun anagramCheck(original: String, compare: String): Boolean {
        val originalMap = HashMap<Char, Int>()
        for (char in original) {
            originalMap[char] = originalMap.getOrDefault(char, 0) + 1
        }

        val compareMap = HashMap<Char, Int>()
        for (char in compare) {
            compareMap[char] = compareMap.getOrDefault(char, 0) + 1
        }

        return originalMap == compareMap
    }
}