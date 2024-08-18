package com.wordsuggester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/java/com/wordsuggester/words.txt";
        HashSet<String> wordsSet = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordsSet.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        WordSuggestionEngine wordsSuggester = new WordSuggestionEngine(wordsSet);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a word to check suggestions (type 'EXIT' to quit):");

        while (true) {
            System.out.print("Word: ");
            String word = scanner.nextLine();

            if (word.equals("EXIT")) {
                break;
            }
            if (wordsSuggester.wordExistInDictionary(word)) {
                System.out.println("Based on dictionary, \"" + word + "\" is a correct word.");
            } else {
                System.out.println("\nSuggestions for word \"" + word + "\": " + wordsSuggester.suggestAllWords(word));
            }
            System.out.println();
        }
        scanner.close();
    }
}