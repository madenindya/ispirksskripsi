import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public String printAll() {
         return (this.getKey() + " ; " + this.count + " " + sentences.size() + " " + patterns.size()) + " " + this.countDoc;
    }

    public String getKey() {
        String key = "("+hyponym+"_"+hypoTag+","+hypernym+"_"+hypeTag+")";
        return key;
    }

    public void addName(String he, String ho) {
        this.hypernym = he;
        this.hyponym = ho;
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
