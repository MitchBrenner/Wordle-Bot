import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// Added removedWords list to allow for undo method
// Updated letterNotPresent method so you cann enter a string rather than single char
// TODO: Add prevGuess to allow for shorter no command


/**
 * Main class containing methods and main for Wordle Helper
 */
public class Wordle {
  private static ArrayList<String> possibleWords = new ArrayList<>();
  private static ArrayList<String> removedWords = new ArrayList<>();
  private static String prevGuess = "";

  /**
   * Main class that calls methods based on user commands
   *
   * @param args arugments if any
   * @throws FileNotFoundException if file not found
   */
  public static void main(String[] args) throws FileNotFoundException {

    restart();
    boolean quit = false;


    while(!quit){
      System.out.println();
      System.out.print("Enter Command: ");
      System.out.print("Guess (g) ");
      System.out.print("- Letter not present (no char)");
      System.out.print("- Letter present (yes char index)");
      System.out.print("- Letter Correct (lc char index)");
      System.out.println("- Undo (u)");
      System.out.print(" - Quit (quit)");
      System.out.println(" - Restart (r)");
      System.out.println();

      Scanner scan = new Scanner(System.in);
      if(scan.hasNext("g")){
        guess();
      }
      else if(scan.hasNext("no")){
        scan.next();
        letterNotPresent(scan.next());
      }
      else if(scan.hasNext("yes")){
        scan.next();
        letterPresent(scan.next().charAt(0), scan.nextInt() - 1);
      } else if(scan.hasNext("dub")){
        scan.next();
        doubleLetter(scan.next().charAt(0));
      }
      else if(scan.hasNext("nodub")){
        scan.next();
        noDoubleLetter(scan.next().charAt(0));
      }
      else if(scan.hasNext("lc")){
        scan.next();
        letterCorrect(scan.next().charAt(0), scan.nextInt() - 1);
      }
      else if(scan.hasNext("u")){
        undo();
      }
      else if (scan.hasNext("quit")){
        quit = true;
      } else if (scan.hasNext("r")){
        restart();
      }
      else {
        System.out.println("INVALID COMMAND");
      }
    }
  }

  /**
   * Prints out random words from possibleWords array
   */
  private static void guess(){
    if(possibleWords.size() == 0){
      System.out.println("NO POSSIBLE GUESSES - Check for typos");
    }

    if(possibleWords.size() <= 3){
      for (String possibleWord : possibleWords) {
        System.out.print(possibleWord + "  ");
      }
      System.out.println();
      return;
    }

    Random randGen = new Random();
    prevGuess = possibleWords.get(randGen.nextInt(possibleWords.size()));
    System.out.println(prevGuess);
  }

  /**
   * Removes all words from possibleWords array that contain any char from letters
   *
   * @param letters String of chars not present in word
   */
  private static void letterNotPresent(String letters){
    removedWords.clear();
    for(int i = 0; i < possibleWords.size(); i++){
      String temp = possibleWords.get(i);
      for(int j = 0; j < letters.length(); j++){
        if(temp.contains(Character.toString(letters.charAt(j)))){
          removedWords.add(possibleWords.get(i));
          possibleWords.remove(i);
          i--;
          break;
        }
      }
    }
  }

  /**
   * Removes all words form possibleWords array that do not contain char letter
   * Removes words with letter at index
   *
   * @param letter char present in word
   * @param index index of char letter
   */
  private static void letterPresent(char letter, int index){
    removedWords.clear();

    boolean isPresent;
    for(int i = 0; i < possibleWords.size(); i++) {
      isPresent = false;
      String temp = possibleWords.get(i);

      for (int j = 0; j < temp.length(); j++) {
        if (temp.charAt(j) == letter) {
          isPresent = true;
        }
      }
      if(!isPresent){
        removedWords.add(possibleWords.get(i));
        possibleWords.remove(i);
        i--;
      }
      if(temp.charAt(index) == letter){
        removedWords.add(possibleWords.get(i));
        possibleWords.remove(i);
        i--;
      }
    }
  }

  /**
   * Removes all words from possibleWords array that do not have char letter at posstion index
   *
   * @param letter char in word
   * @param index index of char
   */
  private static void letterCorrect(char letter, int index){
    removedWords.clear();

    for(int i = 0; i < possibleWords.size(); i++){
      if(possibleWords.get(i).charAt(index) != letter){
        removedWords.add(possibleWords.get(i));
        possibleWords.remove(i);
        i--;
      }
    }
  }

  /**
   * Removes all words from possibleWords array that do not two or more of char letter
   *
   * @param letter char that appears twice in word
   */
  private static void doubleLetter(char letter){
    removedWords.clear();

    int lettercount;
    for (int i = 0; i < possibleWords.size(); i++){
      lettercount = 0;
      String temp = possibleWords.get(i);
      for(int j = 0; j < temp.length(); j++){
        if(temp.charAt(j) == letter){
          lettercount++;
        }
      }
      if(lettercount < 2){
        removedWords.add(possibleWords.get(i));
        possibleWords.remove(i);
        i--;
      }
    }
  }

  /**
   * Removes all words from possibleWords array that contain char letter twice
   *
   * @param letter char to be checked for double appearance
   */
  private static void noDoubleLetter(char letter){
    removedWords.clear();

    int lettercount;
    for (int i = 0; i < possibleWords.size(); i++){
      lettercount = 0;
      String temp = possibleWords.get(i);
      for(int j = 0; j < temp.length(); j++){
        if(temp.charAt(j) == letter){
          lettercount++;
        }
      }
      if(lettercount >= 2){
        removedWords.add(possibleWords.get(i));
        possibleWords.remove(i);
        i--;
      }
    }
  }

  /**
   * Resets the possible arraylist
   *
   * @throws FileNotFoundException if file not found
   */
  private static void restart() throws FileNotFoundException {
    File file = new File("./src/words.txt");
    Scanner scan = new Scanner(file);
    while(scan.hasNext()){
      possibleWords.add(scan.next());
    }
  }

  /**
   * Adds removedWords to possible words
   */
  private static void undo(){
    possibleWords.addAll(removedWords);
  }

}
