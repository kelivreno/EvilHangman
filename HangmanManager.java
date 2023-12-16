/**
* Evil Hangman manager for the programming logic
* @author CS122 Kelangit Hakim
* @version 1.0 (11/12/2023)
* @see (include related class here) */


import java.util.*;
import java.io.*;

public class HangmanManager {
   
   // The number of attempts the player has left to choose the correct pattern
   private int guessesRemaining;
   
   // The current pattern of the secret word to be guessed. Initialized as an empty string.
   private String secretWordCurrentPattern = "";
   
   // Set that contains the words currently available to be used in guessing
   private Set<String> wordSet = new TreeSet<>();
   
   // Set of characters that have been guessed already
   private Set<Character> guessSet = new TreeSet<>();
   
   // Map that matches patterns to sets of available words that match the pattern
   private Map <String, Set<String>> patternMap = new TreeMap<>();
   
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      // prevent the user from passing certain values to the constructor
      if(length < 1 || max < 0) {
         throw new IllegalArgumentException();
      } else {
         // initialize the fields
         this.guessesRemaining = max;

         // initialize the string to guess pattern as all dashes with spaces in between
         for(int i = 0; i < length; i++) this.secretWordCurrentPattern += "-";
         
         for (String word: dictionary) 
            if (word.length() == length) wordSet.add(word);
      }
   }
   
   /**
   * returns a set of the remaining possbile words in the dictionary
   * @return Set<String>: list of possible words
   */
   
   public Set<String> words() {
        return wordSet;
    }
    
   /** 
   *  returns the number of guesses remaining in the game
   *  @return int: number of guesses
   */
    public int guessesLeft() {
        return guessesRemaining;
    }
    /** 
    * returns a sorted set of the user's letter guesses
    * @return SortedSet<Character>: a sorted list of the user's guesses
    */
    
    public Set<Character> guesses() {
        return guessSet;
    }
    
   /**
   * returns a string representation of possible target words taking into 
   * account the user's previous guesses
   * @return String: current pattern of the target word based on the user's guesses
   */
    
    public String pattern() {
        return secretWordCurrentPattern;
    }
   
    /**
    * It takes the user's guess to adjust the list of remaining words
    * For the upcoming turns of the game, it modifies the remaining guess count,
    * then provide the number of times that the letter being guessed appears
    * @param character guess: a character that user guesses
    * @return integer: will return the many times that the char is being guessed in the new family pattern
    */
    public int record(char guess) {
    if (wordSet.isEmpty() || guessesRemaining == 0)
        throw new IllegalStateException();
    else if (!wordSet.isEmpty() && guessSet.contains(guess))
        throw new IllegalArgumentException();

    Map<String, Set<String>> patternFamilies = new TreeMap<>();
    String initialPattern = this.secretWordCurrentPattern;
    guessSet.add(guess);
    updateWordList(patternFamilies);

    if (this.secretWordCurrentPattern.equals(initialPattern))
        guessesRemaining--;

    return countMatches(this.secretWordCurrentPattern, guess);
}      // add the pattern 
   
   /** 
   * returns a string of a given word with the unknown characters
   * (those which have not been guessed) being replaced with strips/dashes
   * @param String word: desired String to be changed 
   * @return String: pattern created from given word
   */
   
   private String pattern(String word) {
   StringBuilder builder = new StringBuilder();
   for (int i = 0; i < word.length(); i++) {  
      if (guessSet.contains(word.charAt(i)))
         builder.append(word.charAt(i));
      else
         builder.append("-");
   }
   return builder.toString();
   }

   // find out the largestSet
   /** 
   * returns the String key from the largest set of words inputed
   * @param families: a Map with String keys representing sets of possible words
   * @return String: key linked to the largest Set of possible words
   */
   
   private String findLargest(Map<String, Set<String>> map) {
    String largestKey = "";
    int largestSize = 0;

    for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
        if (entry.getValue().size() > largestSize) {
            largestSize = entry.getValue().size();
            largestKey = entry.getKey();
        }
    }

    System.out.println("Key with the largest set: " + largestKey);
    return largestKey;
}      
   /** 
   * update the wordSet (clear first) with largestSet
   * addAll method in the set
   * updates the set of possible words to the largest subset
   * word families
   * @param families: Map of family pattern and Set of words in the family
   */
   
   private void updateWordList(Map<String, Set<String>> families) {
    for (String word : wordSet) {
        String currentPattern = pattern(word);
        if (!families.containsKey(currentPattern))
            families.put(currentPattern, new TreeSet<String>());
        families.get(currentPattern).add(word);
    }

    String largestPattern = findLargest(families); // Get the largest pattern
    secretWordCurrentPattern = largestPattern; // Update secretWordCurrentPattern

    wordSet.clear();
    wordSet.addAll(families.get(largestPattern));
}

   /** 
   * returns the number of times a character is in a given pattern
   * @param String pattern: String representation of a family of words
   * @param cbaracter guess: character guessed by the user
   * @return integer: number of occurences of a char within the given pattern
   */
      
   private int countMatches (String pattern, char guess) {
      int matches = 0;
      for (int i = 0; i < pattern.length(); i++) {
         if (pattern.charAt(i) == guess)
            matches++;
      }
      return matches;
   }   
   


}