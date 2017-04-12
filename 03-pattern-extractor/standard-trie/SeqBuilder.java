import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.io.*;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class SeqBuilder {

    public SeqBuilder() {}

    public List<String> tokenizeSentence(String sentence) {
        List<String> result = new ArrayList<>();
        String[] tokens = sentence.split(" ");
        String rel = "";
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].length() <= 1) {
                continue; // harus merupakan kata.
            }

            if (rel.length() > 0) {
                if (tokens[i].contains("<hypernym>") || tokens[i].contains("<hyponym>")) {
                    rel += " " + tokens[i];
                    result.add(rel);
                    rel = "";
                } else {
                    rel += " " + tokens[i];                    
                }
            } else {
                if (tokens[i].contains("<hypernym>")) {
                    int sr = tokens[i].split("<hypernym>").length;
                    if (tokens[i].substring(tokens[i].length() - 10).equals("<hypernym>")) {
                        result.add(tokens[i]);
                    } else {
                        rel = tokens[i];
                    }
                } else if (tokens[i].contains("<hyponym>")) {
                    int sr = tokens[i].split("<hyponym>").length;
                    if (tokens[i].substring(tokens[i].length() - 9).equals("<hyponym>")) {
                        result.add(tokens[i]);
                    } else {
                        rel = tokens[i];
                    }
                } else {
                    result.add(tokens[i]);
                }
            }
        }

        return result;
    }

    public Pair getIndexInBetween(List<String> sequences) {
        Pair pair = new Pair(-1, -1);

        boolean start = false;
        String prevRel = "";
        for (int i = 0; i < sequences.size(); i++)  {
            
            String rel = checkRel(sequences.get(i));
            if (rel.length() > 0) {
                // suka ada yg berulang -> gak boleh hypo-hypo atau hype-hype
                if (start && prevRel.equals(rel)) {
                    pair.begin = i;                     // format ulang
                    pair.key = sequences.get(i);        // new
                    continue;
                }
                if (start) {
                    pair.end = i;
                    pair.key += sequences.get(i);       // update
                    break;
                } else {
                    start = true;
                    prevRel = rel;
                    pair.begin = i;
                    pair.key = sequences.get(i);        // new 
                }
            }
        }
        return pair;
    }

    // ambil n previous
    public Pair getIndexwPrev(List<String> sequences, int n) {
        Pair pair = getIndexInBetween(sequences);
        if (pair.begin == -1) {
            return pair; // kalo salah dibiarin aja. bakal ke-filter nanti
        }
        pair.begin = ((pair.begin - n) > -1) ? pair.begin - n : 0;
        return pair;
    }

    // ambil n follower
    public Pair getIndexwFollow(List<String> sequences, int n) {
        Pair pair = getIndexInBetween(sequences);
        if (pair.end == -1) {
            return pair; // kalo salah dibiarin aja. bakal ke-filter nanti
        }
        pair.end = ((pair.end + n) < sequences.size()) ? pair.end + n : sequences.size() - 1;
        return pair;
    }

    // ambil array sequence berdasarkan Pair begin, end
    public List<String> getFilteredSequences(List<String> sequences, Pair pair) {
        int len = pair.end - pair.begin;
        if (len <= 1) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();
        for (int i = pair.begin; i <= pair.end; i++) {
            result.add(sequences.get(i));
        }
        return result;
    }

    // check what relation here
    public String checkRel(String token) {
        if (token.contains("hypernym"))
            return "hypernym";
        if (token.contains("hyponym")) 
            return "hyponym";
        return "";
    }

}
