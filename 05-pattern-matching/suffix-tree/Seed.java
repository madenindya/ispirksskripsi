import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Seed {

    String hypernym;
    String hypeTag;
    String hyponym;
    String hypoTag;

    int count;
    Set<String> sentences;
    Set<String> patterns;
    int countDoc;

    // belum dipake
    double minPosInDoc;
    double maxPosInDoc;
    double avgPosInDoc;

    public Seed() {
        sentences = new HashSet<>();
        patterns = new HashSet<>();
        count = 0;
        countDoc = 0;
    }

    public void printSeed() {
        System.out.println(this.getKey());
    }

    public String printAll(String[] patarrs) {
        String pvec = "";
        for (int i = 0; i < patarrs.length; i++) {
            if(patterns.contains(patarrs[i])) {
                pvec += " 1"; 
            } else {
                pvec += " 0";
            }
        }
        return (this.getKey() + " # " + this.count + " " + sentences.size() + " " + patterns.size()) + " " + this.countDoc + pvec;
    }

    public String getKey() {
        String key = "("+hyponym+"_"+hypoTag+";"+hypernym+"_"+hypeTag+")";
        return key;
    }

    public boolean addName(String he, String ho) {

        // FILTER RULE-BASED: exclude jika tidak memenuhi syarat dibawah
        // hypernym
        if (he.length() > 3 && he.substring(he.length()-3).equals("nya")) return false;
        if (!Pattern.matches(".*[a-z][a-z][a-z].*", he)) return false;
        // hyponym
        if (ho.length() > 3 && ho.substring(ho.length()-3).equals("nya")) return false;
        if (!Pattern.matches(".*[a-z][a-z][a-z].*", ho)) return false;
        
        this.hypernym = he;
        this.hyponym = ho;
        return true;
    }

    public void addTag(String heTag, String hoTag) {
        this.hypeTag = heTag;
        this.hypoTag = hoTag;
    }

    // compareTo for Sorting
    public int cmprTo(Seed s2) {
        int n1 = this.patterns.size();
        int n2 = s2.patterns.size();
        if (n2 != n1) return n2 - n1;
        
        n1 = this.sentences.size();
        n2 = s2.sentences.size();
        return n2 - n1;
    }

    public void updatePosInDoc(double pos) {

    }
}
