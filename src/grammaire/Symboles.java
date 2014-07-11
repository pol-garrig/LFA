package grammaire;

public class Symboles {
    // Liste des symboles non terminaux.
    private char[] nonTerminaux = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z' };

    // Liste des symboles terminaux.
    // & = epsilon
    private char[] terminaux = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'ε', '1', '0' };
    // L'axiome
    // Précondition : doit appartenir aux symboles non terminaux.
    private char axiome = 'S';

    public Symboles() {

    }

    public char getAxiome() {
        return axiome;
    }

    public void setAxiome(char axiome) {
        this.axiome = axiome;
    }

    public char[] getTerminaux() {
        return terminaux;
    }

    public String getTerminal(int i) {
        return "" + terminaux[i];
    }

    public String getNonTerminal(int i) {
        return "" + nonTerminaux[i];
    }

    public void setTerminaux(char[] terminaux) {
        this.terminaux = terminaux;
    }

    public char[] getNonTerminaux() {
        return nonTerminaux;
    }

    public void setNonTerminaux(char[] nonTerminaux) {
        this.nonTerminaux = nonTerminaux;
    }
}
