import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

// 02
public class PatternMatching {

    List<List<String>> patterns;
    List<String> strpatterns;

    public PatternMatching(String ipath) throws IOException {
        loadPatterns(ipath);
    }

    // match all pattern
    public Map<String, Seed> matchAll(String sentence) {
        // BUILD tree
        SuffixTree stree = new SuffixTree();
        stree.build(sentence);
        // MATCH pattern
        Map<String, Seed> fresult = new HashMap<>();
        for (int i = 0; i < patterns.size(); i++) {
            Map<String, Seed> seeds = matchOne(stree, patterns.get(i));
            String patternStr = strpatterns.get(i);
            for (String k : seeds.keySet()) {
                Seed eseed = seeds.get(k);
                if (fresult.containsKey(k)) {
                    fresult.get(k).count += eseed.count;
                    fresult.get(k).patterns.add(patternStr);
                    fresult.get(k).sentences.add(sentence);
                } else {
                    eseed.patterns.add(patternStr);
                    eseed.sentences.add(sentence);
                    fresult.put(k, eseed);
                }
            }
        }
        return fresult;
    }

    // match one pattern
    private Map<String, Seed> matchOne (SuffixTree stree, List<String> pattern) {
        // find NEW SEEDS
        Map<String, Seed> seeds = stree.findMatch(pattern);
        return seeds;
    }

    private void loadPatterns(String ipath) throws IOException {
        patterns = new ArrayList<>();
        strpatterns = new ArrayList<>();

        BufferedReader bf = new BufferedReader(new FileReader(ipath));

        String line = bf.readLine();
        while (line != null && line.length() > 0) {
            String[] tokens = line.split(" ");
            List<String> npatt = new ArrayList<>();
            for (int i = 0; i < tokens.length; i++) {
                npatt.add(tokens[i]);
            }
            // add to array
            patterns.add(npatt);
            strpatterns.add(line);
            // update
            line = bf.readLine();
        }

        bf.close();
    }
}
