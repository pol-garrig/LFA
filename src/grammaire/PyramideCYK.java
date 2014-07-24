package grammaire;

import java.util.ArrayList;
import java.util.List;

public class PyramideCYK {

    private class Case { // petite classe pour modéliser une case de la pyramide

        private ArrayList<String> productions;

        public Case() {
            productions = new ArrayList<>();
        }

        public void addListProductions(ArrayList<String> list) {
            if (list.isEmpty())
                return;

            for (int i = 0; i < list.size(); i++) {
                if (!productions.contains(list.get(i))) // Si la production n'y
                                                        // est pas déjà
                    productions.add(list.get(i));
            }

        }

        public void addProduction(String p) {
            if (!productions.contains(p)) // Si la production n'y est pas déjà
                productions.add(p);
        }

        public ArrayList<String> getListProductions() {

            return productions;
        }

        public String toString() {
            String tmp = "";

            for (int i = 0; i < productions.size(); i++) {
                tmp += productions.get(i) + " ";
            }

            return tmp;
        }
    }

    private List<Case[]> lignesDeLaPyramide;

    // Initialisation de la liste et aussi de toutes les cases
    public PyramideCYK(int tailleMot) {
        lignesDeLaPyramide = new ArrayList<>();

        for (int i = 0; i < tailleMot; i++) {
            lignesDeLaPyramide.add(new Case[tailleMot - i]);
            for (int j = 0; j < tailleMot - i; j++) {
                lignesDeLaPyramide.get(i)[j] = new Case();
            }
        }
    }

    public void addlistProductions(ArrayList<String> list, int numeroLigne,
            int numeroCase) {
        if (numeroLigne < lignesDeLaPyramide.size()
                && numeroCase < lignesDeLaPyramide.get(numeroLigne).length)
            lignesDeLaPyramide.get(numeroLigne)[numeroCase]
                    .addListProductions(list);
        else
            System.out
                    .println("Erreur accès à une case incorrecte de la pyramide : ligne "
                            + numeroLigne + " positionMot " + numeroCase);
    }

    public ArrayList<String> getListProductions(int numeroLigne, int numeroCase) {

        if (numeroLigne < lignesDeLaPyramide.size()
                && numeroCase < lignesDeLaPyramide.get(numeroLigne).length)
            return lignesDeLaPyramide.get(numeroLigne)[numeroCase]
                    .getListProductions();
        else
            System.out
                    .println("Erreur accès à une case incorrecte de la pyramide : ligne "
                            + numeroLigne + " positionMot " + numeroCase);
        return lignesDeLaPyramide.get(numeroLigne)[numeroCase]
                .getListProductions();

    }

    public String toString() {
        String tmp = "";
        for (int i = 0; i < lignesDeLaPyramide.size(); i++) {
            for (int j = 0; j < lignesDeLaPyramide.get(i).length; j++) {
                tmp += lignesDeLaPyramide.get(i)[j].toString() + "|";
            }
            tmp += "\n";

        }
        return tmp;
    }
}
