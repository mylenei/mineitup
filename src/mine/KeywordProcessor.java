/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import java.util.Hashtable;

import org.xeustechnologies.googleapi.spelling.*;

/**
 * Definition: Class that process the keyword queried for efficiency
 * @author wella
 */
public class KeywordProcessor {
    public KeywordProcessor() {}

    /**
     * checks if the spelling of the words of the keyword is correct
     */
    public boolean spellCheck(String keyword) {
        boolean ok = true;
        String[] words = keyword.split(" ");
        rita.wordnet.RiWordnet wordnet = new rita.wordnet.RiWordnet();
        if(words != null) {
            for(String s : words) {
                if(!wordnet.exists(s)) {
                    ok = false;
                    break;
                }
            }
        }
        return ok;
    }

    /**
     * returns null if spelling is correct, otherwise returns the suggested spelling
     */
    public SpellCorrection[] getSpellingSuggestions(String keyword) {
        SpellChecker checker = new SpellChecker();
        SpellResponse spellResponse = checker.check(keyword);
        SpellCorrection[] corrections = spellResponse.getCorrections();
        return corrections;
    }

    /**
     * search also for the related words or synonyms of the given keyword
     * this is through the help of wordnet
     * returns a hashtable with the key being the keyword and the value being the similar words
     */
    public Hashtable getRelatedWords(String keyword) {
        Hashtable<String, String[]> dictionary = new Hashtable();
        rita.wordnet.RiWordnet wordnet = new rita.wordnet.RiWordnet();
        String[] words = keyword.split(" ");
        for(String s : words) {
            if(wordnet.exists(s)) {
                dictionary.put(s, wordnet.getAllSynonyms(keyword, wordnet.getBestPos(keyword))); //magbutang lang siguro ug para option sa user noh like max search or normal search. ang max search kay allsynonyms ang normal search kay allsynsets. :D
            }
        }
        return dictionary;
    }
}
