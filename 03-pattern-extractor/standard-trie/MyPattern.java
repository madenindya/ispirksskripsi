import java.util.Set;
import java.util.HashSet;

public class MyPattern {
    
    public String pattern;
    public int count;
    public Set<String> seeds;
    public Set<String> sentences;

    public MyPattern() {
        this.seeds = new HashSet<>();
        this.sentences = new HashSet<>();
        this.count = 0;
    }

    public String getStr() {
        // for (String s : seeds) {
        //     System.out.println(s);
        // }
        Double w1 = this.getWeight();
        Double w2 = this.getWeight2();
        Double w3 = this.getWeight3();
        String s1 = String.format("%.2f", w1);
        String s2 = String.format("%.2f", w2);
        String s3 = String.format("%.2f", w3);

        return pattern + " ; " + count + " " + seeds.size() + " " + sentences.size() + " " + s1 + " " + s2 + " " + s3;
    }

    public int cmprTo(MyPattern p2) {
        // berdasarkan sentence unik
        int c1 = this.sentences.size();
        int c2 = p2.sentences.size();
        if (c1 != c2) {
            return c2 - c1;
        }
        // berdasarkan weight pattern
        double w1 = this.getWeight();
        double w2 = p2.getWeight();
        if (w1 > w2) {
            return -1;
        }
        if (w2 > 1) {
            return 1;
        }
        // berdasarkan panjang pattern
        return this.pattern.split(" ").length - p2.pattern.split(" ").length;
    }

    public double getWeight() {
        return (1.0*this.seeds.size()) / (1.0*this.sentences.size());
    }

    public double getWeight2() {
        return (1.0*this.seeds.size()) / (1.0*this.count);
    }

    public double getWeight3() {
        return (1.0*this.sentences.size()) / (1.0*this.count);
    }

    public String getUnik() {
        String result = "";
        String[] tokens = pattern.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("<hypernym>") || tokens[i].equals("<hyponym>")) {
                result += " <relation>";
            } else {
                result += " " + tokens[i];
            }
        }
        return result.substring(1);
    }

    public void combinedPattern (MyPattern p2) {
        if (!this.pattern.equals(p2.pattern)) return;

        int count1 = 0;
        for (String s : p2.seeds) {
            if (this.seeds.add(s)) count1++;
        }
        int count2 = 0;
        for (String s : p2.sentences) {
            if (this.sentences.add(s)) count2++;
        }
        if (count1 > count2)
            this.count += count1;
        else
            this.count += count2;
    } // ?????????????????
}
