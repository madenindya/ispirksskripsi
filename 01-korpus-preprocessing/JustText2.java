import java.util.*;
import java.io.*;
import java.util.regex.Pattern;

public class JustText2 {

    // input folder wiki-ind-s-postag
    public static void main(String[] args) throws IOException {

        String path = "../00-data/wiki/wiki-ind-s-postag"; // folder of folders

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


    private static void cleanAll(String ipath, int count) throws IOException {
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
            if (p.lemma.contains("_")) {
                
                String[] hearr = p.lemma.split("_");
                int begin = 0;
                int end = hearr.length;
                if (end == 0) continue;
                if (hearr[end-1].equals("-rrb-") || hearr[end-1].equals("asal") ||
                    hearr[end-1].equals("saat")) {
                    end--;
                }
                if (hearr[0].equals("jenis") || hearr[0].equals("sejenis") || hearr[0].equals("sekelompok") ||
                    hearr[0].equals("sekumpulan") || hearr[0].equals("semacam") || hearr[0].equals("seperangkat")) {
                    begin = 1;
                }
                String he = hearr[begin];
                for (int i = begin + 1; i < end; i++) {
                    he += "_" + hearr[i];
                }

                if (begin == 0 && end == hearr.length) {
                    s += " " + p.lemma;
                } else {
                    if (begin != 0) {
                        s += " " + hearr[0];
                    } 
                    if (begin != 0 || end != hearr.length) {
                        s += " " + he;
                    }
                    if (end != hearr.length) {
                        s += " " + hearr[hearr.length-1];
                    }   
                }
            } else {
                s += " " + p.lemma;   
            }
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
        if (p.lemma.length() == 1) return true;
        if (p.lemma.equals("\'\'")) return true;
        // check apakah ada alphanumeric alphanumerical
        Pattern pt = Pattern.compile(".*[a-zA-Z0-9].*");
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
