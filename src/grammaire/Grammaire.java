package grammaire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fernando on 03/07/2014 for the project LFA.
 */
public class Grammaire {

    // Liste des symboles non terminaux.
    private List<Character> nonTerminaux;

    // Liste des symboles terminaux.
    private List<Integer> terminaux;

    // Les productions possibles.
    private Map<Character, String> productions;

    // L'axiome
    // Précondition : doit appartenir aux symboles non terminaux.
    private Character axiome;

    /*
     * Constructeur par defaut.
     * Permet d'initialiser les différentes variables.
     */
    public Grammaire () {
        nonTerminaux = new ArrayList<>();
        terminaux = new ArrayList<>();
        productions = new HashMap<>();
    }
}
