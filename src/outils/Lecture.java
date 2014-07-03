package outils;

import grammaire.Grammaire;
import grammaire.Symboles;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.sun.org.apache.regexp.internal.recompile;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class Lecture {
    private Grammaire grammaire;
    private Symboles symboles;

    public Lecture() {
        grammaire = new Grammaire();
        symboles = new Symboles();
    }

    private void lecture() throws IOException {
        String fichier = "entre.txt";
        String key = "";
        String value = "";
        try {
            InputStream ips = new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String ta2[] = ligne.split(" ");
                for (int i = 0; i < ta2.length; i++) {
                    key = ta2[0] + " ";
                    value = "";
                    for (int j = 0; j < symboles.getNonTerminaux().length; j++) {
                        if (ta2[i].endsWith(symboles.getNonTerminal(j))
                                || ta2[i].endsWith(symboles.getNonTerminal(j))) {
                            grammaire.setNonTerminaux(symboles
                                    .getNonTerminal(j));
                        }
                    }
                    for (int k = 0; k < symboles.getTerminaux().length; k++) {

                        if (ta2[i].endsWith(symboles.getTerminal(k))
                                || ta2[i].endsWith(symboles.getTerminal(k))) {
                            grammaire.setTerminal(symboles.getTerminal(k));
                        }
                    }
                }
                for (int x = 1; x < ta2.length; x++) {
                    value = value + " " + ta2[x];
                    grammaire.setProductions(key, value);
                }
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
        System.out.println("Non Terminaux = " + l.grammaire.getNonTerminaux());
        System.out.println("Terminaux = " + l.grammaire.getTerminaux());
        System.out.println("Producitons = " + l.grammaire.getProcutions());
    }
}
