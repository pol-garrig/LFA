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

import outils.Ecriture;
import outils.Lecture;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import com.sun.org.apache.bcel.internal.generic.L2D;
import com.sun.org.apache.xpath.internal.axes.ChildIterator;

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
        System.out.println("p = " + p);
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
        System.out.println("prod e ))= " + prodEpsilon);
        for (int i = 0; i < p.size(); i++) {
            for (int j = 0; j < prodEpsilon.size(); j++) {
                if (productions.get(p.get(i) + " ")
                        .contains(prodEpsilon.get(j))
                        && !cheminEpsilon.contains(p.get(i))) {
                    cheminEpsilon.add(p.get(i));
                }
            }
        }
        System.out.println("Chemin e = " + cheminEpsilon);
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
                // pas bonne
                System.out.println("chemin = " + cheminEpsilon);
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
        for (int i = 0; i < productions.size(); i++) {
            for (int j = 0; j < nonTerminaux.size(); j++) {
                if (productions.get(nonTerminaux.get(i) + " ").contains(
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
        System.out.println(l);
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
            System.out.println(t);
        }
        System.out.println(temp);
        System.out.println(productions);
        System.out.println(nonTerminaux);
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

   

    /*
     * Ajoute une règle.
     *
     * @param nonTerminal le symbole non-terminal de la nouvelle règle
     * @param production la ou les productions associées
     */
    private void ajouterRegle(String nonTerminal, String production) {
        if(productions.get(nonTerminal) != null) {
            ajouteProd(nonTerminal, production);
        }
        else {
            productions.put(nonTerminal, production);
            nonTerminaux.add(nonTerminal);
        }
    }

    /**
     * Nettoie la grammaire.
     */
    public void nettoyer() {
        suppressionImproductifs();
        suppressionInaccesible();
    }

    /**
     * Met la grammaire sous forme normale de Chomsky.
     */
    public void Chomsky() {
        nettoyer();
        suppressionEpsilons();
        // TODO Fernando
        //suppressionRenomage();
        traiterTerminauxChomsky();
        traiterReglesChomsky();
    }

    /**
     * Crée une règle par terminal et les remplace dans les autres règles.
     */
    private void traiterTerminauxChomsky() {
        Set<String> keys = productions.keySet();
        Iterator<String> it = keys.iterator();
        String key, prod;

        // Remplace dans les règles déjà existantes
        while(it.hasNext()) {
            key = it.next();
            prod = productions.get(key);
            for (String term: terminaux) {
                prod = prod.replace(term, "C" + term);
            }
            productions.put(key, prod);
        }

        // Crée les règles
        for (String term: terminaux) {
            ajouterRegle("C" + term + " "," > " + term);
        }
    }

    /**
     * Traite les règles pour les mettre sous FNC.
     */
    private void traiterReglesChomsky() {
        Map<String, String> temp = mapCopy(productions);
        Set<String> keys = temp.keySet();
        Iterator<String> it = keys.iterator();
        String key;
        char nonTerminal = 'M';
        int i, l;

        while(it.hasNext()) {
            i = 0;
            key = it.next();
            l = produtions(productions.get(key)).size() - 1;
            for (String prod: produtions(productions.get(key))) {
                if(charOccur(prod, ' ') > 2) {
                    if(i == 0 && i == l)
                        traiterRegleChomsky(prod, key, "" + nonTerminal, 1, 0);
                    else if(i == 0)
                        traiterRegleChomsky(prod, key, "" + nonTerminal, 1, 1);
                    else if(i == l)
                        traiterRegleChomsky(prod, key, "" + nonTerminal, 1, 2);
                    else traiterRegleChomsky(prod, key, "" + nonTerminal, 1, 3);
                    nonTerminal++;
                }
                i++;
            }
        }
    }

    /**
     * Traite récursivement une règle pour la mettre sous FNC.
     *
     * @param prod la production à traiter
     * @param nonTerminal1 le symbole non-terminal correspondant à la règle
     * @param nonTerminal2 le symbole non-terminal des règles engendrées par le traitement
     * @param cnt numéro de la prochaine règle à créer
     * @param cas numéro correspondant au cas relatif à la position de la production dans la règle
     */
    private ArrayList<String> getProductionsQuiContiennentLaVariable(String variable)
    {
    	ArrayList<String> retour = new ArrayList<>();
    	System.out.println("parcours des productions qui contiennent " + variable);
    	
    	
		Set<String> keys = productions.keySet();
		Iterator<String> it = keys.iterator();
		String key;

		while (it.hasNext()) 
		{
			key = it.next();
			System.out.print(key+"-");
			// Parcours des productions
			for (String prod : produtions(productions.get(key))) 
			{

				if(prod.contains(variable) && !retour.contains(variable))
				{
					System.out.print("->BONNE!");
					String result = key.replaceAll(" ","");
					retour.add(result);
					
					
					break; // Sortie forcée du for 
				}
			}
			System.out.println();
		}
    		
    	System.out.print("Liste des productions bonnes :");
    	for(int i = 0 ; i < retour.size() ; i++)
    	{
    		System.out.print(retour.get(i) + " ");
    	}
    	System.out.println("\nfin de parcours");
		return retour;
    	
    }

    
    
    
    
    public void formeNormaleGreibach()
    {
    	// Sans renommage
    	// Sans E-production
    	
    	String[] tabProductions = null;
    	
    	
		Set<String> keys = productions.keySet();
		Iterator<String> it = keys.iterator();
		String key;

		while (it.hasNext()) 
		{
			key = it.next();
			
			// Parcours des productions
			for (String prod : produtions(productions.get(key))) 
			{
				// Pour chaque production, on regarde s'il y a des recursivités gauches et on les enlève
				
			}
		}
    	
			// Puis ensuite on fait commencer toute règle par un terminal
    	
    }
    
    
    
    /**
     * Supprime toutes les recursivités gauches
     *  Pré-requis : doit être au bon format pour retirer (sans renommage ni e-production)
     */
    public void supRecursiviteGauche()
    {
    	
    	// Création d'une copie temporaire des productions où on construit notre nouvelle grammaire
    	Map<String, String> tmpProductions = new HashMap<>();
    	//tmpProductions.putAll(productions);
    	
		
		String[] variablesDeLaProduction = null;
		ArrayList<String> recursivitesGauchesDeLaProduction = new ArrayList<>();
		ArrayList<String> terminauxDeLaProduction = new ArrayList<>();
		// La regle, si a des recursivités gauches, sera coupé en deux règles.
		String regle1 = "", regle2 = "", keyregle1 = "", keyregle2 = "", tmpProduction = "";

		// Parcours des productions
		Set<String> keys = productions.keySet();
		Iterator<String> it = keys.iterator();
		String key;
		
		
		while (it.hasNext()) 
		{
			key = it.next();
			
			System.out.println("===========================Production " + key);
			// Parcours des productions
			//for (String prod : produtions(productions.get(key))) 
			//{
				tmpProduction = productions.get(key);
				System.out.println("Production : " + tmpProduction );
				
				
				// On supprime les éventuels espaces indésirables dû à la lecture de la grammaire, ou fleches
				tmpProduction = tmpProduction.replaceAll(">","");
				tmpProduction = tmpProduction.replaceAll(" ","");
				
				System.out.println("Production : " + tmpProduction );
				
				
				// On sépare chaque variable. et on les stocke dans un tableau
				variablesDeLaProduction = tmpProduction.split("\\|");
				
				System.out.println("Splitté");
				for(int i = 0 ; i < variablesDeLaProduction.length ; i++)
				{
					System.out.print(variablesDeLaProduction[i] + "-");
				}
				System.out.println();
				
				// Pour chaque variable on regarde si elle finit par un terminal, ou si elle est un terminal.
				// On sépare ces deux types afin de reformer la grammaire :
				for(int i = 0 ; i < variablesDeLaProduction.length ; i++)
				{
					// Si elle finit par un terminal : forme Aa (deux caracteres et le dernier caractère appartient aux terminaux
					if(variablesDeLaProduction[i].length() == 2 && terminaux.contains(variablesDeLaProduction[i].substring(1))  )
					{
						System.out.println("Recursivité gauche : " + variablesDeLaProduction[i]);
						recursivitesGauchesDeLaProduction.add(variablesDeLaProduction[i]);
					}
					// Sinon si c'est un terminal
					else if (terminaux.contains(variablesDeLaProduction[i]))
					{
						System.out.println("Terminal : " + variablesDeLaProduction[i]);
						terminauxDeLaProduction.add(variablesDeLaProduction[i]);
					}
					// S'il y a d'autres types, alors la grammaire n'était pas prête à subir l'algorithme de suppression 
					// De la récursivité gauche
					else
					{
						System.out.println("ERREUR : Recursivité gauche : echec d'identification d'un élément"
								+ " de la grammaire. forme incorrecte : ->" + variablesDeLaProduction[i] + " <-");
						return;
					}				
				}	

			// S'il y a des recursivités gauches à retirer
			if(!recursivitesGauchesDeLaProduction.isEmpty())
			{
				System.out.println("Ajout à la nouvelle production");
				// On dispose maintenant de deux listes qui séparent les éléments à séparer.
				// A -> Aa1 | Aa2 | ... | b1 | b2 | ...
				// On applique l'algorithme de suppression
				
				// On remplace A par 
				// A -> b.A' | b1A' | ... 
				// En utilisant regle1 comme intermediaire
				for(int i = 0 ; i < terminauxDeLaProduction.size() ; i++)
				{
					regle1 += (terminauxDeLaProduction.get(i) + key.replaceAll(" ", "") + "'");
					
					// Ajout d'un séparateur, sauf pour le dernier cas
					if(i < terminauxDeLaProduction.size()-1)
						regle1 += " | ";
				}
				
				System.out.println ("REGLE 1 : " + keyregle1 + "__" + regle1 );
				
				// A' -> epsilon | aA' | a2A'
				regle2 += "ε" + " | "; // Ajout du epsilon
				for(int i = 0 ; i < recursivitesGauchesDeLaProduction.size() ; i++)
				{
					regle2 += ( recursivitesGauchesDeLaProduction.get(i).substring(1) + key.replaceAll(" ", "") + "'" );
					
					// Ajout d'un séparateur, sauf pour le dernier cas
					if(i <= recursivitesGauchesDeLaProduction.size()-2)
						regle2 += " | ";  
				}
				System.out.println ("REGLE 2 : " + keyregle2 + "__" + regle2 );
				// Enfin, ajout  des règles dans la hashmap temporaire.
				tmpProductions.remove(key); // Suppression de l'ancienne règle, si présente.
				keyregle1 = key.replaceAll(" ","");
				tmpProductions.put(keyregle1, regle1);
				
				keyregle2 = ( key.replaceAll(" ","") + "'" );
				System.out.println("1" + tmpProductions);
				// Si la regle A' existe déjà, on rajoute un ' jusqu'à ce que la règle soit disponible
				while(terminaux.contains(keyregle2))
				{
					keyregle2 += "'";
				}
				
				// Puis on l'ajoute
				tmpProductions.put(( keyregle2 + " > " ), regle2);			
				// On ajoute le nouveay terminal créé
				terminaux.add( ( keyregle2 ));
				
				System.out.println("tmpProductions : " + tmpProductions);
				
				System.out.println("--------------");
			}	
			System.out.println("1" + tmpProductions);
			// On réinitialise les variables qu'on utilise, et on ajoute nos productions à la hash
			regle1 = "";
			regle2 = "";
			keyregle2 = "";
			keyregle1 = "";
			tmpProduction = "";
			terminauxDeLaProduction.clear();
			recursivitesGauchesDeLaProduction.clear();

			
		}	
	
		// On met la nouvelle grammaire dans production
		productions.clear();
		
		
		System.out.println("production vide :" + productions);
		System.out.println("tmpProductions : " + tmpProductions);
		productions.putAll(tmpProductions);

		System.out.println("production re-remplie : " + productions);
		System.out.println("fini");
    }
    
    private void traiterRegleChomsky(String prod, String nonTerminal1, String nonTerminal2, int cnt, int cas) {
        String newProd, oldProd;

        if(charOccur(prod, ' ') > 2) {
            int i = prod.indexOf(' ');

            prod = prod.substring(0, prod.length() - 1);
            oldProd = prod.substring(0, i);
            oldProd += " " + nonTerminal2 + cnt + " ";
            newProd = prod.substring(i + 1, prod.length()) + " ";
            if(cas == 0)
                productions.put(nonTerminal1, productions.get(nonTerminal1).replaceAll(prod, oldProd));
            if(cas == 1)
                productions.put(nonTerminal1, productions.get(nonTerminal1).replaceAll(prod + "\\s\\x7C", oldProd + "|"));
            if(cas == 3)
                productions.put(nonTerminal1, productions.get(nonTerminal1).replaceAll("\\x7C\\s" + prod + "\\s\\x7C", "| " + oldProd + "|"));
            if(cas == 2)
                productions.put(nonTerminal1, productions.get(nonTerminal1).replaceAll("\\x7C\\s" + prod + "\\z", "| " + oldProd));
            ajouterRegle(nonTerminal2 + cnt + " ", " > " + newProd);
            traiterRegleChomsky(newProd, nonTerminal2 + cnt + " ", nonTerminal2, cnt + 1, 0);
        }
    }

    /**
     * Calcule le nombre d'occurences d'un caractère dans une chaîne.
     *
     * @param str la chaîne dans laquelle chercher le caractère
     * @param c le caractère à rechercher
     * @return le nombre d'occurences
     */
    private static int charOccur(String str, char c) {
        int i = -1, cnt = 0;

        while((i = str.indexOf(c, i + 1)) != -1) {
            cnt++;
        }
        return cnt;
    }

    /**
     * Calcule l'index d'un n-ième caractère répété dans une chaîne.
     *
     * @param str la chaîne dans laquelle chercher le caractère
     * @param c le caractère à rechercher
     * @param n la n-ième appartition du caractère à rechercher
     * @return l'index
     */
    private static int nCharIndex(String str, char c, int n) {
        int i = -1, cnt = 0;

        while((i = str.indexOf(c, i + 1)) != -1 && cnt < n) {
            cnt++;
        }
        return i;
    }

    /**
     * Copie une HashMap<String, String>.
     *
     * @param src la map à copier
     * @return la copie
     */
    private static HashMap<String, String> mapCopy(Map<String, String> src) {
        HashMap<String, String> copy = new HashMap<String, String>();
        Set<String> keys = src.keySet();
        Iterator<String> it = keys.iterator();
        String key;

        while(it.hasNext()) {
            key = it.next();
            copy.put(key, src.get(key));
        }

        return copy;
    }

    public static void main(String[] args) throws IOException {
        Lecture lp = new Lecture();
        //Ecriture ec = new Ecriture();
        lp.lecture();
        Grammaire g = lp.getGrammaire();
        
        
        //System.out.println(g.algorithmeCYK("aabbab"));

        
        g.supRecursiviteGauche();
        
        
        
        System.out.println(g.productions);
        // System.out.println(g.nonTerminaux);
        // System.out.println(g.nonTerminaux);
        // g.suppressionRenomage();
        // g.suppressionInaccesible();
        // g.suppressionImproductifs();
        /*
        g.suppressionEpsilons();
        // System.out.println(g.nonTerminaux);
        System.out.println(g.productions);
        System.out.println(g.algorithmeCYK("aabbab"));
        */

        // System.out.println(g.productions);
        // System.out.println(g.nonTerminaux);
        // g.suppressionRenomage();
/*
        System.out.println(g.productions);
        g.traiterTerminauxChomsky();
        System.out.println(g.productions);
        g.traiterReglesChomsky();
        System.out.println(g.productions);*/
    }
}
