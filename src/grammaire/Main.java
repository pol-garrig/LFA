package grammaire;

import java.io.IOException;
import java.util.Scanner;

import outils.Lecture;

/**
 * Created by Fernando on 30/06/2014 for the project LFA.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Lecture lp = new Lecture();
        Scanner lectureClavier = new Scanner(System.in);
        boolean grammaireModifie;
        String chemin = "";
        System.out.print("Indiquez le chemin relatif du fichier grammaire\n-->");
        
        
        lp.lecture(lectureClavier.nextLine());
        
        Grammaire g = lp.getGrammaire();

        while (true) {
        	
            System.out.println("Grammaire :\n" + g);
            System.out.println("Appuyez sur ENTRÉE...");
            lectureClavier.nextLine();

            grammaireModifie = true;
            System.out.println("########## Opérations possibles ##########");
            System.out.println("Choisissez une opération");
            System.out.println("1. Supprimer les improductifs\n"
                    + "2. Supprimer les inaccessibles\n"
                    + "3. Supprimer les e-productions\n"
                    + "4. Supprimer les renommages\n"
                    + "5. Forme normale de Chomsky\n"
                    + "6. Forme de Greibach\n" + "7. Algorithme CYK");
            System.out.print("Votre choix :");
            switch (lectureClavier.nextLine()) {
            case "1":
                g.suppressionImproductifs();
                break;
            case "2":
                g.suppressionInaccesible();
                break;
            case "3":
                g.suppressionEpsilons();
                break;
            case "4":
                g.suppressionRenomage();
                break;
            case "5":
                g.chomsky();
                System.out.println(g.getNonTerminaux());
                break;
            case "6":
                System.out
                        .println("Greibach : algorithme non terminé.\nS'arrête après suppression de la recursivité gauche");
                g.supRecursiviteGauche();
                break;
            case "7":
                grammaireModifie = false;
                System.out.print("Choisissez un mot à tester :");
                if (g.algorithmeCYK(lectureClavier.nextLine())) {
                    System.out.println("Le mot est engendré par la grammaire");
                } else {
                    System.out
                            .println("Le mot n'est pas engendré par la grammaire");
                }

                break;
            default:
                System.out.println("Choix incorrect");
                break;

            }
            
 
            System.out.println("Grammaire :\n" + g);
            System.out.println(g);
            System.out.println("Appuyez sur ENTRÉE...");
            lectureClavier.nextLine();
            
            System.out.println("Changer de grammaire ? (y/n)");
            
            switch(lectureClavier.nextLine())
            {
            	case "y":
        		    System.out.print("Indiquez le chemin relatif du fichier grammaire\n-->");
        		    lp = new Lecture();
        	        lp.lecture(lectureClavier.nextLine());
                    g = new Grammaire();
                    g = lp.getGrammaire();
                    break;   	
            }
        }
    }
}
