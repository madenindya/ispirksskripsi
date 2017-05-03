import java.util.Set;
import java.util.HashSet;

public class MyPattern {
    
    public String pattern;
    public int count;
    public Set<String> seeds;
    public Set<String> sentences;

    public int countSeed;
    public int countSentence;

    public MyPattern() {
        this.seeds = new HashSet<>();
        this.sentences = new HashSet<>();
        this.count = 0;
    }

    public MyPattern(String s) {
        s = s.trim();
        
        this.pattern = s.split(" ; ")[0];
        
        String[] vec = s.split(" ; ")[1].split(" ");
        this.count = Integer.parseInt(vec[0]);
        this.countSeed = Integer.parseInt(vec[1]);
        this.countSentence = Integer.parseInt(vec[2]);
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

        int size;
        try {
            size = seeds.size();
        } catch (Exception e) {
            size = countSeed;
        }
        int sentence;
        try {
            sentence = sentences.size();
        } catch (Exception e) {
            sentence = countSentence;
        }

        return pattern + " ; " + count + " " + size + " " + sentence + " " + s1 + " " + s2 + " " + s3;
    }

    public int cmprTo(MyPattern p2) {
        // berdasarkan sentence unik
        int c1;
        try {
            c1 = this.sentences.size();
        } catch (Exception e) {
            c1 = this.countSentence;
        }
        int c2;
        try {
            c2 = p2.sentences.size();
        } catch (Exception e) {
            c2 = p2.countSentence;
        }
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
        int s1;
        try {
            s1 = this.seeds.size();
        } catch (Exception e) {
            s1 = this.countSeed;
        }
        int s2;
        try {
            s2 = this.sentences.size();
        } catch (Exception e) {
            s2 = this.countSentence;
        }
        return (1.0*s1) / (1.0*s2);
    }

    public double getWeight2() {
        int s1;
        try {
            s1 = this.seeds.size();
        } catch (Exception e) {
            s1 = this.countSeed;
        }
        return (1.0*s1) / (1.0*this.count);
    }

    public double getWeight3() {
        int s1;
        try {
            s1 = this.sentences.size();
        } catch (Exception e) {
            s1 = this.countSentence;
        }
        return (1.0*s1) / (1.0*this.count);
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

    // public void combinedPattern (MyPattern p2) {
    //     if (!this.pattern.equals(p2.pattern)) return;

    //     int count1 = 0;
    //     for (String s : p2.seeds) {
    //         if (this.seeds.add(s)) count1++;
    //     }
    //     int count2 = 0;
    //     for (String s : p2.sentences) {
    //         if (this.sentences.add(s)) count2++;
    //     }
    //     if (count1 > count2)
    //         this.count += count1;
    //     else
    //         this.count += count2;
    // } // ?????????????????
}
