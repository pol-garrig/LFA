package grammaire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.PortableServer.POA;

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

    public void setNonTerminaux(String i) {
        if (!nonTerminaux.contains(i)) {
            nonTerminaux.add(i);
        }
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
    public Map<String, String> getProcutions(){
        return productions;
    }
}
