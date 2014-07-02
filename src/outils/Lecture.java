package outils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Lecture {
    String chemin = "/source/entre.txt";
    private File file = new File("/source/entre.txt");
    private Scanner scan;
    List <String> liste = new ArrayList<>();
    public Lecture(){
        
    }
    
    public  throws FileNotFoundException {
        scan = new Scanner(chemin);
        
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] split = line.split(" ");
            for (int i = 0; i < split.length; i++) {
                words.add(split[i]);
            }
        }
        return words;
    }
    public static void main(String[] args) throws FileNotFoundException {
       Lecture l = new Lecture();
       l.read();
       System.out.println(words);
    }
}
