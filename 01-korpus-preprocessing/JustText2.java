import java.util.*;
import java.io.*;
import java.util.regex.Pattern;

public class JustText2 {

    // input folder wiki-ind-s-postag
    public static void main(String[] args) throws IOException {

        String path = args[0]; // folder of folders

        File dir = new File(path);
        String[] sdir = dir.list();
        int count = 0;
        for (int i = 0; i < sdir.length; i++) {
            String pathsubdir = path + "/" + sdir[i];
            File subdir = new File(pathsubdir);
            System.out.println("Cleaning " + pathsubdir);
            for (File f : subdir.listFiles()) {
                 String filepath = pathsubdir + "/" + f.getName();

                 cleanAll(filepath, count);
                 count++;
            }
        }
    }


    private static void cleanAll(String ipath   , int count) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(ipath));        
        String opath = "../00-data/wiki/wiki-ind-js2-lowcase/wiki_";
        if (count < 10) {
            opath += "00" + count;
        } else if(count < 100) {
            opath += "0" + count;
        } else {
            opath += count;
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath));

        String line = bf.readLine();
        while (line != null) {
            List<Pair> tokens = buildSequence(line);
            printSentence(tokens, bw);

            line = bf.readLine();
        }

        bw.close();
        bf.close();
    }

    private static void printSentence2(String s, BufferedWriter bw) throws IOException {
        s = s.toLowerCase();
        if (s.length() > 0) {
            bw.write(s + "\n");
        }
    }

    private static void printSentence(List<Pair> ls, BufferedWriter bw) throws IOException {
        String s = "";
        for (Pair p : ls) {
            s += " " + p.lemma;
        }
        if (s.length() > 0) {
            bw.write(s.substring(1) + "\n");
        }
    }

    private static List<Pair> buildSequence(String sentence) {
        List<Pair> sequence = new ArrayList<>();
        // for postagged words;
        String[] tokens = sentence.split(" ");
        String prevTag = "";
        for (int i = 0; i < tokens.length; i++) {
            // special char
            if (tokens[i].contains("<start>") || tokens[i].contains("<end>") || tokens[i].contains("</doc>")) {
                continue;
            } 
            // others
            else {
                Pair cpair = splitTag(tokens[i]);
                if (cpair.postag.equals(prevTag)) {
                    Pair pvpair = sequence.get(sequence.size() - 1);
                    if (filteredPair(pvpair) || filteredPair(cpair)) {
                        pvpair.lemma += " " + cpair.lemma;
                    } else {
                        pvpair.lemma += "_" + cpair.lemma;
                    }
                } else {
                    sequence.add(cpair);
                    prevTag = cpair.postag;
                }
            }
        }
        return sequence;
    }

    /**
    *  ALL Related to PAIR
    */
    private static Pair splitTag(String lemma) {
        int n = lemma.lastIndexOf("_");
        String pos = lemma.substring(n+1);
        String l = lemma.substring(0, n).toLowerCase(); //
        return new Pair(l, pos);
    }

    private static boolean filteredPair(Pair p) {
        // check apakah ada alphanumeric alphanumerical
        Pattern pt = Pattern.compile("[a-zA-Z0-9]");
        boolean nonAlphanumerical = !(pt.matcher(p.lemma).find());
        return nonAlphanumerical;
    }

    static class Pair {
        String lemma;
        String postag;
        public Pair (String lemma, String postag) {
            this.lemma = lemma;
            this.postag = postag;
        }
    }
}
