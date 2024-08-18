package com.wordsuggester;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class WordSuggestionEngine {
    private HashSet<String> dictionary;

    public WordSuggestionEngine() {
        this.dictionary = new HashSet<>(Arrays.asList("name", "run", "fun", "sun", "hello"));
    }

    public WordSuggestionEngine(HashSet<String> dictionary) {
        this.setDictionary(dictionary);
    }

    public void setDictionary(HashSet<String> dictionary) {
        this.dictionary = dictionary;
    }

    public boolean wordExistInDictionary(String word) {
        return this.dictionary.contains(word);
    }

    public HashSet<String> getDictionaryWithWordsOfLength(int expectedLength) {
        if (expectedLength < 0) {
            throw new IllegalArgumentException("ExpectedLength argument cannot be less than zero.");
        }
        HashSet<String> result = new HashSet<>();

        for (String word : this.dictionary) {
            if (word.length() == expectedLength) {
                result.add(word);
            }
        }
        return result;
    }

    public HashSet<String> suggestAllWords(String word) {
        long start = System.currentTimeMillis();
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Argument cannot be null or empty.");
        }
        HashSet<String> result = new HashSet<>();
        result.addAll(this.suggestWordsWithSingleLetterDifference(word));
        result.addAll(this.suggestWordsWithOneMissingLetter(word));
        result.addAll(this.suggestWordsWithExtraLetter(word));
        result.addAll(this.suggestWordsWithSwappedAdjacentLetters(word));
        long end = System.currentTimeMillis();
        System.out.println("Execution time of suggestAllWords:                        " + (end - start) + " ms");
        return result;
    }

    // user thought about "name" and typed: "nama"
    // method creates variants -> *ama, n*ma, na*a, nam* and match with asterisks variants from dict
    public HashSet<String> suggestWordsWithSingleLetterDifference(String word) {
        long start = System.currentTimeMillis();
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Argument cannot be null or empty.");
        }
        HashMap<String, HashSet<String>> dictionaryVariants = new HashMap<>();
        HashSet<String> trimmedDictionary = this.getDictionaryWithWordsOfLength(word.length());

        for (String dictWord : trimmedDictionary) {
            dictionaryVariants.put(dictWord.toLowerCase(),
            WordVariancesUtils.generateVariantsForExistingLetter(dictWord.toLowerCase()));
        }
        HashSet<String> wordVariants = WordVariancesUtils.generateVariantsForExistingLetter(word.toLowerCase());
        HashSet<String> result = WordVariancesUtils.matchWordVariantsWithAsterisks(wordVariants, dictionaryVariants);
        long end = System.currentTimeMillis();
        System.out.println("Execution time of suggestWordsWithSingleLetterDifference: " + (end - start) + " ms");
        return result;
    }

    // user thought about "name" and typed: "nam"
    // method creates variants -> *nam, n*am, na*m, nam* and match with asterisks variants from dict
    public HashSet<String> suggestWordsWithOneMissingLetter(String word) {
        long start = System.currentTimeMillis();
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Argument cannot be null or empty.");
        }
        HashMap<String, HashSet<String>> dictionaryVariants = new HashMap<>();
        HashSet<String> trimmedDictionary = this.getDictionaryWithWordsOfLength(word.length() + 1);

        for (String dictWord : trimmedDictionary) {
            dictionaryVariants.put(dictWord.toLowerCase(),
            WordVariancesUtils.generateVariantsForExistingLetter(dictWord.toLowerCase()));
        }
        HashSet<String> wordVariants = WordVariancesUtils.generateVariantsForMissingLetter(word.toLowerCase());
        HashSet<String> result = WordVariancesUtils.matchWordVariantsWithAsterisks(wordVariants, dictionaryVariants);
        long end = System.currentTimeMillis();
        System.out.println("Execution time of suggestWordsWithOneMissingLetter:       " + (end - start) + " ms");
        return result;
    }

    // user thought about "name" and typed: "naame"
    // method creates variants -> *aame, n*ame, na*me, naa*e, naam* and match with asterisks variants from dict
    public HashSet<String> suggestWordsWithExtraLetter(String word) {
        long start = System.currentTimeMillis();
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Argument cannot be null or empty.");
        }
        HashMap<String, HashSet<String>> dictionaryVariants = new HashMap<>();
        HashSet<String> trimmedDictionary = this.getDictionaryWithWordsOfLength(word.length() - 1);

        for (String dictWord : trimmedDictionary) {
            dictionaryVariants.put(dictWord.toLowerCase(),
            WordVariancesUtils.generateVariantsForMissingLetter(dictWord.toLowerCase()));
        }
        HashSet<String> wordVariants = WordVariancesUtils.generateVariantsForExistingLetter(word.toLowerCase());
        HashSet<String> result = WordVariancesUtils.matchWordVariantsWithAsterisks(wordVariants, dictionaryVariants);
        long end = System.currentTimeMillis();
        System.out.println("Execution time of suggestWordsWithExtraLetter:            " + (end - start) + " ms");
        return result;
    }

    // user thought about "name" and typed: "nmae"
    // method creates variants -> mnae, name, nmea and match with variants from dic
    public HashSet<String> suggestWordsWithSwappedAdjacentLetters(String word) {
        long start = System.currentTimeMillis();
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Argument cannot be null or empty.");
        }
        char[] letters = word.toCharArray();
        HashSet<String> swappedWordVariants = new HashSet<>();
        HashSet<String> trimmedDictionary = this.getDictionaryWithWordsOfLength(word.length());

        for (int i = 0; i < letters.length - 1; i++) {
            char[] cloneLetters = letters.clone();
            char temp = cloneLetters[i];
            cloneLetters[i] = cloneLetters[i + 1];
            cloneLetters[i + 1] = temp;
            swappedWordVariants.add(new String(cloneLetters));
        }
        HashSet<String> result = WordVariancesUtils.matchWordsVariances(swappedWordVariants, trimmedDictionary);
        long end = System.currentTimeMillis();
        System.out.println("Execution time of suggestWordsWithSwappedAdjacentLetters: " + (end - start) + " ms");
        return result;
    }
}
