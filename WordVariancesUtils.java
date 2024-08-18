package com.wordsuggester;

import java.util.HashMap;
import java.util.HashSet;

public class WordVariancesUtils {

    private WordVariancesUtils() {
        throw new UnsupportedOperationException("This utility class cannot be instantiate");
    }

    // for example word: "name", method will generate set of words: "*ame", "n*me", "na*e", "nam*"
    public static HashSet<String> generateVariantsForExistingLetter(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Argument cannot be be null");
        }
        if (word.isEmpty()) {
            throw new IllegalArgumentException("Argument cannot be empty.");
        }
        HashSet<String> variants = new HashSet<>();

        for (int i = 0; i < word.length(); i++) {
            StringBuilder sb = new StringBuilder(word);
            sb.setCharAt(i, '*');  // set "*" under index instead of letter
            variants.add(sb.toString());
        }
        return variants;
    }

    // for example word: "nam", method will generate set of words: "*nam", "n*am", "na*m", "nam*"
    public static HashSet<String> generateVariantsForMissingLetter(String word) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Argument cannot be null or empty.");
        }
        HashSet<String> variants = new HashSet<>();

        for (int i = 0; i < word.length() + 1; i++) {
            StringBuilder sb = new StringBuilder(word);
            sb.insert(i, '*');  // set "*" under index with shifting existing letters
            variants.add(sb.toString());
        }
        return variants;
    }

    // regular intersection of words from two sets
    public static HashSet<String> matchWordsVariances(HashSet<String> wordVariants, HashSet<String> dictionary) {
        if (wordVariants == null || dictionary == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }
        wordVariants.retainAll(dictionary);
        return wordVariants;
    }

    // intersect word asterisks variants with values of provided map variants for example:
    // wordVariants = {"*ama", "n*ma", "na*a", "nam*"}
    // intersects with values of map dictVariance:
    // {   "name": {"*ame", "n*me", "na*e", "nam*"},
    //     "fun":  {"*un", "f*n", "*un"},  ... }
    public static HashSet<String> matchWordVariantsWithAsterisks(HashSet<String> wordVariants,
                                                                 HashMap<String, HashSet<String>> dictionaryVariants) {
        if (wordVariants == null || dictionaryVariants == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }
        HashSet<String> result = new HashSet<>();

        for (String dictWord : dictionaryVariants.keySet()) {
            HashSet<String> tempSet = new HashSet<>(wordVariants);
            tempSet.retainAll(dictionaryVariants.get(dictWord));
            if (!tempSet.isEmpty()) {
                result.add(dictWord);
            }
        }
        return result;
    }
}
