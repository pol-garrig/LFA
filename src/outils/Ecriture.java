package outils;

import grammaire.Grammaire;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Ecriture {

    public static final String sorti = "sorti.txt";
    private Grammaire grammaire;

    public Ecriture() {
        grammaire = new Grammaire();
    }

    public void Ecrir(Grammaire grammaire) {
        File fichier = new File(sorti);
        try {
            // Creation du fichier
            fichier.createNewFile();
            // creation d'un writer (un écrivain)
            FileWriter writer = new FileWriter(fichier);
            writer.write("Grammaire = \n");

            try {
                for (int i = 0; i < grammaire.getProcutions().size(); i++) {
                    
                    writer.write(grammaire.getNonTerminaux().get(i)
                            + grammaire.getProcutions().get(
                                    grammaire.getNonTerminaux().get(i) + " ")
                            + "\n");
                }

                writer.write("ceci est un texte\n");
            } finally {
                // quoiqu'il arrive, on ferme le fichier
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier");
        }
    }

    public static void main(String[] args) throws IOException {
        Lecture la = new Lecture();
        la.lecture();
        Ecriture e = new Ecriture();
        la.getGrammaire().suppressionImproductifs();
        //la.getGrammaire().suppressionInaccesible();
        System.out.println(la.getGrammaire().getProcutions());
        //la.getGrammaire().suppressionImproductifs();
        //la.getGrammaire().suppressionInaccesible();
       // la.getGrammaire().suppressionEpsilons();
        e.Ecrir(la.getGrammaire());
    }
}
