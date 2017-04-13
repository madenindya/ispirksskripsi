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

    long count;
    Set<String> sentences;
    Set<String> patterns;

    public Seed() {
        sentences = new HashSet<>();
        patterns = new HashSet<>();
        count = 0;
    }

    public void printSeed() {
        System.out.println(this.getKey());
    }

    public String printAll() {
         return (this.getKey() + " ; " + count + " " + sentences.size() + " " + patterns.size());
    }

    public String getKey() {
        String key = "("+hyponym+"_"+hypoTag+","+hypernym+"_"+hypeTag+")";
        return key;
    }

    public int cmprTo(Seed s2) {
        int n1 = this.sentences.size();
        int n2 = s2.sentences.size();
        return n2 - n1;
    }
}
