import java.util.*;
import java.io.*;

// java MainFilterPair ../tmpresult/iterasi-1-filter-2wordEmbedd.seed ../tmpresult/iterasi-1-filter-3score.seed 1
public class MainFilterPair {
    
    private static int max_pattern = 5;
    private static int iterasike = 0;

    public static void main(String[] args) throws Exception {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        String ipath;
        try {
            ipath = args[0];
        } catch (Exception e) {
            System.out.println("Input file (embeded pair) : ");
            ipath = bf.readLine();
        }

        String opath;
        try {
            opath = args[1];
        } catch (Exception e) {
            System.out.println("Output file (scored pair) : ");
            opath = bf.readLine();
        }

        try {
            iterasike = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.out.println("iterasi ke : ");
            iterasike = Integer.parseInt(args[2]);
        }
        max_pattern += iterasike - 1;

        List<Seed> scoringSeed = readAllSeed(ipath, max_pattern);
        Collections.sort(scoringSeed, new Comparator<Seed>() {
            @Override
            public int compare(Seed s1, Seed s2) {
                if (s1.score > s2.score) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        printSeeds(scoringSeed, opath);

        bf.close();
    }

    private static void printSeeds(List<Seed> seeds, String opath) throws IOException {
        Set<String> fseeds = new HashSet<>();
    
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath));
        BufferedWriter bww = new BufferedWriter(new FileWriter("../tmpresult/iterasi-"+iterasike+"-selected.seed"));
        for (Seed s : seeds) {
            if (s != null) {
                bw.write(s.getStr()+"\n");
            }
            if (s.acceptSeed()) {
                bww.write(s.getStr()+"\n");
                fseeds.add(s.getKey());
            }
        }
        bww.close();
        bw.close();

        // MERGE with OLD CORPUS
        BufferedReader bff = new BufferedReader(new FileReader("../../00-data/korpus-kata/iterasi-" + (iterasike-1) + ".korpus"));
        String line = bff.readLine();
        while(line != null) {
            line = line.trim();
            fseeds.add(line);
            line = bff.readLine();
        }
        bff.close();

        List<Pair> ppairs = new ArrayList<>();
        for (String s : fseeds) {
            ppairs.add(new Pair(s));
        }
        Collections.sort(ppairs, new Comparator<Pair>() {
            @Override
            public int compare(Pair s1, Pair s2) {
                int a = s1.hypernym.compareTo(s2.hypernym);
                return a;
            }
        });
        bw = new BufferedWriter(new FileWriter("../../00-data/korpus-kata/iterasi-" + iterasike + ".korpus"));
        for (Pair p : ppairs) {
            bw.write(p.pair+"\n");
        }
        bw.close();

    }

    private static List<Seed> readAllSeed(String ipath, int max) throws IOException {
        List<Seed> resultSeed = new ArrayList<>();

        BufferedReader bff = new BufferedReader(new FileReader(ipath));
        String line = bff.readLine();
        while (line != null) {
            Seed baru = formatString(line, max);
            if (baru != null) {
                resultSeed.add(formatString(line, max));
            }
            line = bff.readLine();
        }
        bff.close();

        return resultSeed;
    } 

    private static Seed formatString(String line, int max) {
        try {
            String[] tmp1 = line.split("#");

            String pair = tmp1[0].substring(1,tmp1[0].length()-2);
            String[] words = pair.split(";");
            String hypo = words[0].substring(0, words[0].lastIndexOf("_"));
            String hype = words[1].substring(0, words[1].lastIndexOf("_"));
            if (hypo.equals(hype)) return null;
            
            String[] vect = tmp1[1].split(" ");
            int count = Integer.parseInt(vect[1]);
            int countSentence = Integer.parseInt(vect[2]);
            int countPattern = Integer.parseInt(vect[3]);
            int countDoc = Integer.parseInt(vect[4]);
            double wordEmbedding = Double.parseDouble(vect[vect.length-1]);

            Seed nseed = new Seed(pair, hypo+";"+hype, count, countSentence, countPattern, countDoc, wordEmbedding, max);
            return nseed;
        } catch (Exception e) {
            System.out.println(e.toString() + " -> " + line);
            return null;
        }
    }
}

class Seed {
    String name;
    String pname;

    int count;
    int countSentence;
    int countPattern;
    int countDoc;
    double wordEmbedding;

    double score;

    public Seed(String n, String pn, int c, int cs, int cp, int cd, double we, int max) {
        this.name = n;
        this.pname = pn;
        this.count = c;
        this.countSentence = cs;
        this.countPattern = cp;
        this.countDoc = cd;
        this.wordEmbedding = we;

        this.score = calculateScore(max);
    }

    private double calculateScore(int maxCountPattern) {
        double patternScore = (1.0 * this.countPattern) / (1.0 * maxCountPattern);
        return (patternScore + wordEmbedding) / 2;
        // double patternScore = (1.0 * this.countPattern) / (1.0 * maxCountPattern);
        // return (0.6 * patternScore + 0.4 * wordEmbedding);
    }

    public String getStr() {
        return this.getKey() + " # " + this.score;
    }

    public String getKey() {
        return "("+this.pname+")";
    }

    public boolean acceptSeed() {
        boolean f1 = (this.score > 0.6);
        return f1;
    }


}

class Pair {
    String pair;
    String hypernym;

    public Pair (String p) {
        this.pair = p;
        String chype = p.split(";")[1];
        hypernym = chype.substring(0,chype.length()-1);
    }
}
