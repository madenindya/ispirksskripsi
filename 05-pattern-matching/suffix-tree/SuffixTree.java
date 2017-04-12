import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SuffixTree {
    
    TNode root;
    public SuffixTree () {
        this.root = new TNode("^", "");
    }

    /**
    *   BUILD TREE per SENTENCE
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
                sequence.add(new Pair("<start>", ""));
            } else if (tokens[i].contains("<end>")) {
                sequence.add(new Pair("<end>", ""));
            } else if (tokens[i].contains("</doc>")) {
                sequence.add(new Pair("</doc>", ""));
            } 
            // others
            else {
                Pair cpair = splitTag(tokens[i]);
                if (filteredPair(cpair)) continue;
                if (cpair.postag.equals(prevTag)) {
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
    *   FIND MATCH
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
        if (i >= pseq.size()) {
            Seed nseed = new Seed();
            nseed.hypernym = he;
            nseed.hypeTag = heTag;
            nseed.hyponym = ho;
            nseed.hypoTag = hoTag;

            String key = nseed.getKey();
            if (resultSeed.containsKey(key)) {
                resultSeed.get(key).count += 1;
            } else {
                nseed.count += 1;
                resultSeed.put(key, nseed);                
            }
            return;
        }
        String cek = pseq.get(i);        
        if (cek.equals("<hypernym>") && node.postag.contains("NN")) { // harus merupakan noun
            he = node.name;
            heTag =  node.postag;
        } else if (cek.equals("<hyponym>") && node.postag.contains("NN")) {
            ho = node.name;
            hoTag =  node.postag;
        } else {
            if (!cek.equals(node.name)) {
                return;
            }
        }
        for (String key : node.childs.keySet()) {
            recursiveFind(pseq, i+1, node.childs.get(key), he, ho, heTag, hoTag);
        }
    }

    /**
    *  ALL Related to PAIR
    */
    private static Pair splitTag(String lemma) {
        int n = lemma.lastIndexOf("_");
        String l = lemma.substring(0, n);
        String pos = lemma.substring(n+1);
        return new Pair(l, pos);
    }

    private static boolean filteredPair(Pair p) {
        if (p.lemma.length() == 1) return true;
        // tambahin non alphanumerical
        return false;
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
