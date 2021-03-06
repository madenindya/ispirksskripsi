import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// 03
public class SuffixTree {
    
    TNode root;
    public SuffixTree () {
        this.root = new TNode("^", "");
    }

    /**
    *   FIND MATCH with A pattern sequence
    *   RETURN SEED
    */
    Map<String, Seed> resultSeed;
    public Map<String, Seed> findMatch(List<String> pseq) {
        resultSeed = new HashMap<>();
        for (String key : root.childs.keySet()) {
            recursiveFind(pseq, 0, root.childs.get(key), "", "", "", "");
        }
        return resultSeed;
    }
    private void recursiveFind(List<String> pseq, int i, TNode node, String he, String ho, String heTag, String hoTag) {
        if (i >= pseq.size() && (he.length() > 0 && ho.length() > 0)) {
            // filter seed
            if (he.equalsIgnoreCase(ho)) return;
            // if (bannedSeed(he, ho)) return;
            // new seeds
            Seed nseed = new Seed();
            if (!nseed.addName(he, ho)) return;
            nseed.addTag(heTag, hoTag);
            nseed.count = 1;

            String key = nseed.getKey();
            if (resultSeed.containsKey(key)) {
                resultSeed.get(key).count += 1;
            } else {
                resultSeed.put(key, nseed);                
            }
            return;
        }
        String cek = pseq.get(i);        
        // harus merupakan noun || noun phrase
        if (cek.equals("<hypernym>") && (node.postag.equals("NN") || node.postag.equals("NNP"))) { 
            he = node.name;
            heTag =  node.postag;
        } else if (cek.equals("<hyponym>") && (node.postag.equals("NN") || node.postag.equals("NNP"))) {
            ho = node.name;
            hoTag =  node.postag;
        } 
        else {
            if (!cek.equalsIgnoreCase(node.name)) {
                return;
            }
        }
        for (String key : node.childs.keySet()) {
            recursiveFind(pseq, i+1, node.childs.get(key), he, ho, heTag, hoTag);
        }
    }
    // private boolean bannedSeed(String hypernym, String hyponym) {
    //     // pattern '<start> <hyponym> adalah <hypernym>'
    //     if (hyponym.equalsIgnoreCase("berikut")) {}
    //     if (hypernym.equalsIgnoreCase("daftar") || hypernym.contains("daftar ")) {}
    //     if (hypernym.equalsIgnoreCase("bagian") || hypernym.contains("bagian ")) {}
    //     return false;
    // }

    /**
    *   BUILD TREE for a SENTENCE
    */
    public void build(String sentence) {
        List<Pair> ftokens = buildSequence(sentence);
        for (int i = 0; i < ftokens.size(); i++) {
            addSequence(ftokens, i);
        }
    }
    private void addSequence(List<Pair> seq, int i) {
        TNode node = root;
        for (int j = i; j < seq.size(); j++) {
            // check i
            String key = seq.get(j).lemma;
            if (node.childs.containsKey(key)) {
                node = node.childs.get(key);
            } else {
                TNode nnode = new TNode(key, seq.get(j).postag);
                node.childs.put(seq.get(j).lemma, nnode);
                node = node.childs.get(key);
            }
        }
    }
    private List<Pair> buildSequence(String sentence) {
        List<Pair> sequence = new ArrayList<>();
        // for postagged words;
        String[] tokens = sentence.split(" ");
        String prevTag = "";
        for (int i = 0; i < tokens.length; i++) {
            // special char
            if (tokens[i].contains("<start>")) {
                sequence.add(new Pair("<start>", "XX"));
                prevTag = "XX";
            } else if (tokens[i].contains("<end>")) {
                sequence.add(new Pair("<end>", "XX"));
                prevTag = "XX";
            } else if (tokens[i].contains("</doc>")) {
                sequence.add(new Pair("</doc>", "XX"));
                prevTag = "XX";
            } else {
                Pair cpair = splitTag(tokens[i]);
                if (filteredPair(cpair)) {
                    //prevTag = cpair.postag; // tetap update prevTag-nya
                }
                else if (cpair.postag.equals(prevTag)) {
                    Pair pvpair = sequence.get(sequence.size() - 1);
                    pvpair.lemma += " " + cpair.lemma;
                } else {
                    sequence.add(cpair);
                    prevTag = cpair.postag;
                }
            }
        }
        return sequence;
    }
    // INPUT Kalimat POSTAG
    // OUTPUT Kalimat yang udah berdasarkan POSTAG
    public String getSequence(String sentence) {
        List<Pair> ftokens = buildSequence(sentence);
        if (ftokens.size() > 0) {
            String result = ""; 
            int i = 0;
            for (Pair p : ftokens) {
                String part = "";
                String lemma = p.lemma;
                if (lemma.equals("<start>") || lemma.equals("<end>") || lemma.equals("</doc>")) continue;
                if (lemma.contains("<start>")) {
                    System.out.println(lemma);
                }
                if (lemma.contains(" ")) {
                    String[] lemmatokens = lemma.split(" ");
                    part = lemmatokens[0];
                    for (int j = 1; j < lemmatokens.length; j++) {
                        part += "_" + lemmatokens[j];
                    }
                } else {
                    part = lemma;
                }
                if (i == 0) {
                    result = part;
                } else {
                    result += " " + part;
                }
                i++;
            }
            return result;
        } else return "";
    }

    /**
    * PRINT ALL SEQUENCE
    */
    List<String> sortList;
    public List<String> printAll() {
        sortList = new ArrayList<>();
        recursivePrint(root, "");
        return sortList;
    }
    private void recursivePrint(TNode node,  String prev) {
        // update prev
        prev += " " + node.name;
        if (node.childs.size() < 1) {
            sortList.add(prev.substring(1));
        } 
        else {
            for (TNode tn : node.childs.values()) {
                recursivePrint(tn, prev);
            }
        }
    }

    /**
    *  ALL Related to PAIR
    */
    private static Pair splitTag(String lemma) {
        int n = lemma.lastIndexOf("_");
        String pos = lemma.substring(n+1);
        String l = lemma.substring(0, n).toLowerCase();
        // if (!pos.equals("NNP")) { // bukan ProperNoun -> lower case
        //     l = l.toLowerCase();
        // }
        return new Pair(l, pos);
    }

    private static boolean filteredPair(Pair p) {
        if (p.lemma.length() == 1) return true;
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
