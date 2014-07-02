package outils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.sun.org.apache.regexp.internal.recompile;

public class Lecture {
    String chemin = "/source/entre.txt";
    // private File file = new File("/source/entre.txt");
    private Scanner scan;
    private HashMap<String, String> responseMap;
    
    List<String> liste = new ArrayList<>();
    

    public Lecture() {
        responseMap = new HashMap<String, String>();
    }

    private void lecture() throws IOException {
        String fichier = "entre.txt";
        String resp = "";
        try {
            InputStream ips = new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null) {

                String ta[] = ligne.split("=");
                String ta2[] = ta[1].split("RE");
                for (int i = 0; i < ta2.length; i++) {
                    resp += ta2[i] + "\n";
                }
                responseMap.put(ta[0], resp);
                resp = "";
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier de lecture n'a pas pu être trouvé.");
            throw e;
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier");
            throw e;
        }

    }

    public static void main(String[] args) throws IOException {
        Lecture l = new Lecture();
        l.lecture();
        System.out.println(l.responseMap);
    }
}
