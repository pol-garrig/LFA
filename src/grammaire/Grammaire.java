package grammaire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.omg.PortableServer.POA;

import outils.Lecture;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import com.sun.org.apache.bcel.internal.generic.L2D;
import com.sun.org.apache.xpath.internal.axes.ChildIterator;
import com.sun.xml.internal.ws.wsdl.writer.document.Port;

/**
 * Created by Fernando on 03/07/2014 for the project LFA.
 */
public class Grammaire {

    // Liste des symboles non terminaux.
    private List<String> nonTerminaux;

    // Liste des symboles terminaux.
    private List<String> terminaux;

    // Les productions possibles.
    private Map<String, String> productions;

    // L'axiome
    // Précondition : doit appartenir aux symboles non terminaux.
    private String axiome;

    public Grammaire() {
        nonTerminaux = new ArrayList<>();
        terminaux = new ArrayList<>();
        productions = new HashMap<>();
        axiome = "S";

    }

    /**
     * Methode pour supprimer les inaccesibles
     */
    public void suppressionInaccesible() {
        List<String> temp = new ArrayList<>();
        String prod;
        int t = 1;
        // On regarde les non terminaux qu'il y a dans les productions de
        // l'axiome
        if (nonTerminaux.contains(axiome)) {
            temp.add(axiome);
            prod = productions.get(axiome + " ");
            for (int i = 0; i < nonTerminaux.size(); i++) {
                // On regarde si les productions de l'axiome contiennent des non
                // terminaux
                if (prod.contains(nonTerminaux.get(i))) {
                    temp.add(nonTerminaux.get(i));
                }
            }

            // On regarde les
            while (t != nonTerminaux.size() - 1) {
                if (temp.size() > t) {
                    prod = productions.get(temp.get(t) + " ");
                    if (prod != null) {
                        for (int k = 0; k < nonTerminaux.size(); k++) {
                            if (prod.contains(nonTerminaux.get(k))
                                    && !temp.contains(nonTerminaux.get(k))) {
                                temp.add(nonTerminaux.get(k));
                            }
                        }
                    }
                }
                t++;
            }
        } else {
            temp.add("L'axiome n'existe pas.");
        }

        nonTerminaux = temp;
    }

    /**
     * Methode pour suppimer les improductifs
     */
    public void suppressionImproductifs() {
        System.out.println(productions);
        List<String> p1 = new ArrayList<>();
        List<String> p2 = new ArrayList<>();
        // temp liste de non terminaux temporaire
        List<String> temp = new ArrayList<>();
        // List de les non terminaux qui sont des productions
        List<String> p = new ArrayList<>();
        // fin indique quand il faut s'arreter
        boolean fin = false;

        // je regarde s'il y a des productions pour tous les nonTerminaux, et je
        // mets a chaque fois les non terminaux avec producitons dans p
        for (int g = 0; g < nonTerminaux.size(); g++) {

            if (productions.get(nonTerminaux.get(g) + " ") != null) {

                p.add(nonTerminaux.get(g));
            }
        }
        // je regarde tous les productions de chaque non Terminaux et cherche
        // les p1 et je les garde dans la liste p1
        for (int i = 0; i < p.size(); i++) {
            // On garde dans un arrayList tous les productions d'un non terminal
            // Ex : A > A a | a = [A a , a]
            temp = produtions(productions.get(p.get(i) + " "));

            for (int j = 0; j < temp.size(); j++) {

                // On regarde s'il y a des terminaux dans les producition et des
                // non terminaux
                if (containsTerminaux(temp.get(j))
                        && !containsNonTerminaux(temp.get(j))
                        && !p1.contains(p.get(i))) {
                    // si on est seulement des terminaux on le garde en p1
                    p1.add(p.get(i));
                }
            }

        }

        // On regarde p2
        while (!fin) {
            // on copie les non terminaux vers p2
            p2 = copieList(p1);
            // on cherche les elementes des p2
            for (int i = 0; i < p.size(); i++) {
                for (int j = 0; j < p1.size(); j++) {
                    // si avec un production on arrive a un elemente de p1
                    // on le garde dans p2
                    if (p.get(i).contains(p1.get(j)) && !p2.contains(p.get(i))) {
                        p2.add(p.get(i));
                    }
                }
            }
            // on regarde si p1 et p2 sont egaux
            if (p1.equals(p2)) {
                // si sont egaux on fini
                fin = true;
            } else {
                // sinon on copie p2 dans p1 et on continue a cherche p3 dans p2
                p1 = copieList(p2);
            }
        }
        // et pour finir on copie les productifs dans l'arraylist des non
        // terminaux
        nonTerminaux = copieList(p2);
    }

    /**
     * Methode pour supprimer les Epsilons du productions
     */
    public void suppressionEpsilons() {
        // liste des non terminaux avec epsilons
        List<String> prodEpsilon = new ArrayList<>();
        // liste des non terminaux avec non terminaux avec epsilons
        List<String> cheminEpsilon = new ArrayList<>();
        // liste des productions
        List<String> p = new ArrayList<>();
        // Les productions possibles.
        Map<String, String> productionsSansEpsilons = new HashMap<>();

        // je regarde s'il y a des productions pour tous les nonTerminaux, et je
        // mets a chaque fois les non terminaux avec producitons dans p
        for (int g = 0; g < nonTerminaux.size(); g++) {

            if (productions.get(nonTerminaux.get(g) + " ") != null) {

                p.add(nonTerminaux.get(g));
            }
        }
        // On regarde tous les productions avec Epsilons
        // et on le garde dans prodEpsilon
        for (int i = 0; i < p.size(); i++) {
            if (productions.get(p.get(i) + " ").contains("ε")) {
                prodEpsilon.add(p.get(i));
            }
        }
        // On regarde les non terminaux pour savoir
        // ce que vont vers les epsilon
        cheminEpsilon = copieList(prodEpsilon);
        for (int i = 0; i < p.size(); i++) {
            for (int j = 0; j < prodEpsilon.size(); j++) {
                cheminEpsilon.addAll(chemin(prodEpsilon.get(j)));
            }
        }
        // On va remplacer les epsilons ...
        for (int i = 0; i < prodEpsilon.size(); i++) {

            p = produtions(productions.get(prodEpsilon.get(i) + " "));
            // On cherche les productions avec epsilons et on elimine les
            // Epsilons
            for (int j = 0; j < p.size(); j++) {
                if (p.get(j).contains("ε")) {
                    p.remove(p.get(j));
                    // On garde les producitons sans les epsilons
                    productions.remove(prodEpsilon.get(i) + " ");
                    productions.put(prodEpsilon.get(i) + " ", " "
                            + productionsString(p));
                }
            }
        }
        // s'il y a des epsilons...
        if (prodEpsilon.size() != 0) {

            // On va remplacer les non terminaux pour les epsilons
            String s = "";
            String f = "";
            for (int i = 0; i < productions.size(); i++) {

                f = productions.get(cheminEpsilon.get(i) + " ");
                for (int j = 0; j < cheminEpsilon.size(); j++) {
                    // On reemplace les epsilons pour les productions
                    s = reemplaceEpsilons(f, cheminEpsilon.get(j));
                    // On ajoute les nouvelles productions
                    f = ajouteProd(f, s);
                    // On garde la nouvel production
                    productionsSansEpsilons.remove(cheminEpsilon.get(i) + " ");
                    productionsSansEpsilons.put(cheminEpsilon.get(i) + " ", f);
                }
            }
            // si S (l'axiome) arrive jusqu'à epsilons il faut l'ajouter
            if (cheminEpsilon.contains("S")) {
                f = ajouteProd(productionsSansEpsilons.get("S "), " ε ");
                productionsSansEpsilons.remove("S ");
                productionsSansEpsilons.put("S ", f);
            }
            productions = productionsSansEpsilons;
            System.out.println(productions);
        }
    }

    public void suppressionRenomage() {
        Map<String, List<String>> ren = new HashMap<>();
        List<String> r = new ArrayList<>();
        List<String> t = new ArrayList<>();
        List<String> te = new ArrayList<>();

        String temp = "";
        // On cherche ren0 ,ren1 ,etc ..
        System.out.println();
        for (int i = 0; i < productions.size(); i++) {
            for (int j = 0; j < nonTerminaux.size(); j++) {
                System.out.println("prod = "
                        + productions.get(nonTerminaux.get(i) + " "));
                System.out.println("nonter = " + nonTerminaux.get(j));

                if (productions.get(nonTerminaux.get(i) + " ") != null
                        && productions.get(nonTerminaux.get(i) + " ").contains(
                                nonTerminaux.get(j))) {
                    if (!r.contains(nonTerminaux.get(i))) {
                        r.add(nonTerminaux.get(i));
                    }
                    if (!r.contains(nonTerminaux.get(j))) {
                        r.add(nonTerminaux.get(j));
                    }
                    t = copieList(r);
                    ren.remove(nonTerminaux.get(i) + " ");
                    ren.put(nonTerminaux.get(i) + " ", t);
                } else {
                    if (!r.contains(nonTerminaux.get(i))) {
                        r.add(nonTerminaux.get(i));
                    }
                    t = copieList(r);
                    ren.remove(nonTerminaux.get(i) + " ");
                    ren.put(nonTerminaux.get(i) + " ", t);
                }
            }

            r.clear();
        }
        System.out.println(ren);
        // On cherche les Rem 2 ,3 ...
        for (int i = 0; i < ren.size(); i++) {
            t = ren.get(nonTerminaux.get(i) + " ");
            for (int j = 0; j < t.size(); j++) {
                te = ren.get(t.get(j) + " ");
                if (!t.containsAll(te)) {
                    t.addAll(te);
                    suppressionDupliProd(t);
                }
            }
        }
        System.out.println(ren);

    }

    /**
     * 
     * @param C
     */
    public List<String> chemin(String c) {
        Set<String> keys = productions.keySet();
        Iterator<String> it = keys.iterator();
        String key;
        List<String> temp = new ArrayList<>();
        List<String> t = new ArrayList<>();

        String prod;
        while (it.hasNext()) {
            key = it.next();
            prod = productions.get(key);
            if (prod.contains(c) && !temp.contains(key)) {
                temp.add(key.substring(0, key.length() - 1));
            }
        }
        for (int i = 0; i < productions.size(); i++) {
            for (int j = 0; j < temp.size(); j++) {
                for (int j2 = 0; j2 < nonTerminaux.size(); j2++) {
                    prod = productions.get(nonTerminaux.get(j2) + " ");
                    if (prod != null && prod.contains(temp.get(j))
                            && !t.contains(nonTerminaux.get(j2))) {
                        t.add(nonTerminaux.get(j2));
                    }
                }
            }
        }
        temp = copieList(t);
        return temp;
    }

    /**
     * methode qui renvoit true si dans la production il y a des terminaux
     * 
     * @param s
     * @return true s'il y a des terminaux
     */
    public boolean containsTerminaux(String s) {
        boolean contains = false;
        for (int i = 0; i < terminaux.size(); i++) {
            if (s.contains(terminaux.get(i))) {
                contains = true;
            }
        }
        return contains;
    }

    /**
     * Methode qui chercher un String dans un autre et elimine le caracter
     * 
     * @param s
     * @param s2
     * @return
     */
    public String reemplaceEpsilons(String s, String s2) {

        String temp = "";

        if (s.endsWith(" ")) {
            temp = s + "|";
        } else {
            temp = s + " | ";
        }
        String[] t = s.split(" ");

        for (int j = 0; j < t.length; j++) {
            if (t[j].contains(s2)) {
                t[j] = "&";
                for (int i = 0; i < t.length; i++) {
                    if (!t[i].contains("&") && !t[i].contains(">")) {
                        temp += t[i] + " ";
                    }
                }
            }

            if (t[j].contains("&") && !t[j].contains(" ")) {
                temp += "| ";
                t[j] = s2;
            }

        }
        for (int j = 0; j < t.length; j++) {
            if (t[j].contains(s2)) {
                t[j] = "&";
                for (int i = 0; i < t.length; i++) {
                    if (!t[i].contains("&") && !t[i].contains(">")) {
                        temp += t[i] + " ";
                    }
                }
            }

            if (t[j].contains("&") && !t[j].contains(" ")) {
                temp += "| ";
            }

        }
        if (temp.length() != 0) {
            temp = temp.substring(0, temp.length() - 1);

        }
        List<String> te = produtions(temp);
        temp = suppressionDupliProd(te);
        return temp;

    }

    /**
     * Methode qui ajoute des productions a les productions existent
     * 
     * @param s
     * @return
     */
    public String ajouteProd(String s, String s2) {
        List<String> l = new ArrayList<>();
        String temp = "";
        l = produtions(s);
        l.addAll(produtions(s2));
        temp = suppressionDupliProd(l);
        return temp;
    }

    /**
     * Methode qui renvoit true si un String (ou sub string) d'une Liste est
     * contenu dans une autre Liste
     * 
     * @param s
     * @param s2
     * @return
     */
    public boolean containsString(List<String> s, List<String> s2) {
        boolean contains = false;
        for (int i = 0; i < s.size(); i++) {
            for (int j = 0; j < s2.size(); j++) {
                if (s.get(i).contains(s2.get(j))) {
                    contains = true;
                }
            }
        }
        return contains;
    }

    /**
     * Methode qui recoit une Liste et supprime les duplications et renvoit un
     * String
     * 
     * @param l
     * @return
     */
    public String suppressionDupliProd(List<String> l) {

        String temp = " > ";
        // Creation du hashSet pour la supression des dupliqué
        HashSet hs = new HashSet();
        hs.addAll(l);
        l.clear();
        l.addAll(hs);
        // On genere un String avec les elementes de la Liste
        for (int i = 0; i < l.size(); i++) {
            temp += l.get(i).substring(0, l.get(i).length()) + "| ";
        }
        temp = temp.substring(0, temp.length() - 2);
        return temp;
    }

    /**
     * methode qui renvoit true si dans la production il y a des non terminaux
     * 
     * @param s
     * @return true s'il y a des non terminaux
     */
    public boolean containsNonTerminaux(String s) {
        boolean contains = false;
        for (int i = 0; i < nonTerminaux.size(); i++) {
            if (s.contains(nonTerminaux.get(i))) {
                contains = true;
            }
        }
        return contains;
    }

    /**
     * Elimine toutes les repetitions d'un non terminal
     * 
     * @param s
     */
    public void supUnImprod(String s) {
        List<String> temp = new ArrayList<>();
        String t = "";
        for (int i = 0; i < productions.size(); i++) {

            t = productions.get(nonTerminaux.get(i) + " ");

            if (t == null) {
                nonTerminaux.remove(nonTerminaux.get(i));
                for (int j = 0; j < temp.size(); j++) {
                    if (temp.get(j).contains(nonTerminaux.get(i))) {
                        temp.remove(j);
                    }
                }
            } else {
                temp = produtions(t);
            }
        }
    }

    public void setNonTerminaux(String i) {
        if (!nonTerminaux.contains(i)) {
            nonTerminaux.add(i);
        }
    }

    public List<String> copieList(List<String> l2) {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < l2.size(); i++) {
            l.add("" + l2.get(i));
        }
        return l;
    }

    public List<String> getNonTerminaux() {
        return nonTerminaux;
    }

    public void setTerminal(String i) {
        if (!terminaux.contains(i)) {
            terminaux.add(i);
        }
    }

    public void setProductions(String i, String e) {
        if (!productions.containsValue(e)) {
            productions.put(i, e);
        }
    }

    public List<String> getTerminaux() {
        return terminaux;
    }

    public Map<String, String> getProcutions() {
        return productions;
    }

    /**
     * Methode pour la descomposition des differentes productions
     */
    public List<String> produtions(String s) {
        List<String> t = new ArrayList<>();
        int pos = 1;
        int aux = s.length();
        String a = "";

        while (pos != aux) {
            a += s.charAt(pos);
            if (a.startsWith(" ") || a.startsWith(">")) {
                a = "";
            }
            if (a.contains("|") || (pos == aux - 1) || a.contains(">")
                    && a.contains("| |")) {
                // supression du dernier caractere |
                if (a.endsWith("|")) {
                    a = a.substring(0, a.length() - 1);
                }
                // On met un space toujours a la fin
                if (!a.endsWith(" ")) {
                    a += " ";
                }
                if (!a.equals(" ")) {
                    t.add(a);
                }

                a = "";
            }
            pos++;
        }

        return t;
    }

    /**
     * Methode qui transforme une arrayliste des String en une String
     * 
     * @param l
     * @return String
     */
    public String productionsString(List<String> l) {
        String s = "> ";

        for (int i = 0; i < l.size(); i++) {
            s += l.get(i);
        }
        return s;
    }

    public Map<String, String> suppressionProductions(List<String> l) {
        Map<String, String> temp = new HashMap<>();
        String prod;
        List<String> t = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            prod = productions.get(l.get(i) + " ");
            temp.put(l.get(i) + " ", prod);

        }
        for (int j = 0; j < l.size(); j++) {
            System.out.println(j);
            prod = productions.get(l.get(j) + " ");
            t = produtions(prod);
            System.out.println(produtions(prod));
            for (int k = 0; k < t.size(); k++) {
                
            }
            System.out.println("l = "+l.get(j));

        }
        System.out.println("temp = " + temp);
        // productions = temp;
        return temp;
    }

    /**
     * @param mot
     *            , le mot qu'on teste pour savoir s'il appartient à la
     *            grammaire
     * @return si c'est bon on peut faire le mot, ou pas.
     */
    public boolean algorithmeCYK(String mot) {

        System.out
                .println("=============================================CYK sur "
                        + mot);
        int tailleMot = mot.length();
        PyramideCYK pyramide = new PyramideCYK(tailleMot);
        // Liste temporaire utilisée pour clarifier le code.
        ArrayList<String> tmp = null;

        // Parcours de la première ligne de la pyramide
        for (int i = 0; i < tailleMot; i++) {
            // On regarde si on peut obtenir la lettre avec la grammaire == un
            // terminal égal à la lettre
            tmp = getProductionsQuiContiennentLaVariable(mot
                    .substring(i, i + 1));

            pyramide.addlistProductions(tmp, 0, i);
            // Vidage de tmp
            tmp = null;
        }

        // Parcours des AUTRES lignes de la pyramide, et completion de la
        // pyramide au fur et à mesure
        for (int numeroLigne = 1; numeroLigne < tailleMot; numeroLigne++) {

            System.out
                    .println("========================CHANGEMENT DE LIGNE - LIGNE "
                            + numeroLigne);
            // Parcours des cases de cette ligne
            for (int numeroCase = 0; numeroCase < tailleMot - numeroLigne; numeroCase++) {
                System.out
                        .println("========================CHANGEMENT DE Case - Case "
                                + numeroCase);
                // System.out.println("Numero Ligne" + numeroLigne +
                // "Numero case : " + numeroCase );

                // à chaque fois, numeroLigne cas possibles de combinaisons de
                // cases. (sachant qu'oncommence à 0)
                for (int i = 0; i < numeroLigne; i++) {
                    System.out.println("Case " + (0 + i) + ","
                            + (0 + numeroCase) + " et " + (numeroLigne - 1 - i)
                            + "," + (tailleMot - numeroLigne));
                    List<String> gauche = pyramide.getListProductions(0 + i,
                            0 + numeroCase);
                    List<String> droite = pyramide.getListProductions(
                            numeroLigne - 1 - i, numeroCase + 1 + i);

                    // ici ça pue

                    // On regarde s'il existe une produciton qui contient la
                    // combinaison de deux productions des deux cases en dessous
                    for (int k = 0; k < gauche.size(); k++) {
                        for (int l = 0; l < droite.size(); l++) {

                            // Recherche des productions qui contiennent le non
                            // terminal
                            tmp = getProductionsQuiContiennentLaVariable(""
                                    .concat(gauche.get(k))
                                    .concat(droite.get(l)));
                            // Ajout des productions trouvées à la case de la
                            // pyramide
                            pyramide.addlistProductions(tmp, numeroLigne,
                                    numeroCase);

                            tmp = null;
                        }
                    }

                }

            }
        }

        System.out.println(pyramide);
        System.out.println("AXIOME : " + axiome);
        System.out.print("Liste des productions de la dernière case : ");

        for (int i = 0; i < pyramide.getListProductions(tailleMot - 1, 0)
                .size(); i++) {
            System.out.println(pyramide.getListProductions(tailleMot - 1, 0)
                    .get(i));

            System.out.println("valeur comparee : "
                    + pyramide.getListProductions(tailleMot - 1, 0).get(i));
            if (pyramide.getListProductions(tailleMot - 1, 0).get(i)
                    .contains(axiome)) {
                return true;
            }

        }
        return false;
    }

    /**
     * @return un tableau contenant le nom des productions qui contiennent le
     *         terminal en particulier
     */
    private ArrayList<String> getProductionsQuiContiennentLaVariable(
            String variable) {
        ArrayList<String> retour = new ArrayList<>();
        System.out.println("parcours des productions qui contiennent "
                + variable);

        Set<String> keys = productions.keySet();
        Iterator<String> it = keys.iterator();
        String key;

        while (it.hasNext()) {
            key = it.next();
            System.out.print(key + "-");
            // Parcours des productions
            for (String prod : produtions(productions.get(key))) {

                if (prod.contains(variable) && !retour.contains(variable)) {
                    System.out.print("->BONNE!");
                    String result = key.replaceAll(" ", "");
                    retour.add(result);

                    break; // Sortie forcée du for
                }
            }
            System.out.println();
        }

        System.out.print("Liste des productions bonnes :");
        for (int i = 0; i < retour.size(); i++) {
            System.out.print(retour.get(i) + " ");
        }
        System.out.println("\nfin de parcours");
        return retour;

    }

    public static void main(String[] args) throws IOException {
        Lecture lp = new Lecture();
        lp.lecture();
        Grammaire g = lp.getGrammaire();
        // System.out.println(g.nonTerminaux);
        // System.out.println(g.nonTerminaux);
        g.suppressionInaccesible();
        g.suppressionImproductifs();
        // g.suppressionRenomage();
        g.suppressionProductions(g.nonTerminaux);
        System.out.println(g.productions);
        System.out.println(g.nonTerminaux);
        // g.suppressionEpsilons();
        // System.out.println(g.nonTerminaux);
        // System.out.println(g.chemin("C"));
        // System.out.println(g.algorithmeCYK("aa"));
        // System.out.println(g.productions);
        // System.out.println(g.nonTerminaux);
        // g.suppressionRenomage();

    }
}
