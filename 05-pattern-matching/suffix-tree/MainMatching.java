import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import java.lang.Override;
import java.util.Collections;
import java.util.Comparator;

// 01
// java MainMatching ../../03-pattern-extractor/standard-trie/tmpattern/iterasi-1-selected.pattern ../../00-data/wiki/wiki-ind-s-postag  tmpseedspool/iterasi-1
public class MainMatching {

    static Map<String, Seed> resultAll;
    static PatternMatching pm;
    static int cnt;

    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        resultAll = new HashMap<>();
        cnt = 0;

        String ipath;
        try {
            ipath = args[0];
        } catch (Exception e) {
            System.out.println("Pattern file name: ");
            ipath = bf.readLine();
        }
        pm = new PatternMatching(ipath);

        String path;
        try {
            path = args[1];
        } catch (Exception e) {
            System.out.println("Folder of folders to be read: ");
            path = bf.readLine();           
        }

        // pattern matching
        File dir = new File(path);
        String[] sdir = dir.list();
        for (int i = 0; i < sdir.length; i++) {
            String pathsubdir = path + "/" + sdir[i];
            File subdir = new File(pathsubdir);
            System.out.println("Matching with " + pathsubdir);
            for (File f : subdir.listFiles()) {
                 String filepath = pathsubdir + "/" + f.getName();

                 findMatching(filepath);
            }
        }

        String opath;
        try {
            opath = args[2];
        } catch (Exception e) {
            System.out.println("File to be written: ");
            opath = bf.readLine();           
        }
        printSortMap(resultAll, opath);
    }

    // pattern matching a document 
    public static void findMatching(String fpath) throws IOException {
        BufferedReader bff = new BufferedReader(new FileReader(fpath));
        String sentence = bff.readLine();

        Set<Seed> tmpSeeds = new HashSet<>();
        while (sentence != null && sentence.length() > 0) {
            if (sentence.contains("</doc>")) {
                updateCountDocSeeds(tmpSeeds);
                tmpSeeds = new HashSet<>();
            } 
            else {
                // match one sentence to all pattern
                Map<String, Seed> resultOne = pm.matchAll(sentence);
                for (String k : resultOne.keySet()) {
                    Seed cseed = resultOne.get(k);
                    if (resultAll.containsKey(k)) {
                        Seed eseed = resultAll.get(k);
                        eseed.count += cseed.count;
                        eseed.patterns.addAll(cseed.patterns);
                        eseed.sentences.addAll(cseed.sentences);
                        tmpSeeds.add(eseed);
                    } else {
                        resultAll.put(k, cseed);
                        tmpSeeds.add(cseed);
                    }
                }
            }
            // update sentence
            sentence = bff.readLine();
        }
        bff.close();
    }

    private static void updateCountDocSeeds(Set<Seed> lseed) {
        for (Seed s : lseed) {
            s.countDoc += 1;
        }
    }

    public static void printSortMap(Map<String, Seed> m, String opath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath+".seed"));
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(opath+"-filter-1ruleBased.seed"));
        List<Seed> sortSeeds = new ArrayList<>();
        for (String k : m.keySet()) {
            sortSeeds.add(m.get(k));
        }
        Collections.sort(sortSeeds, new Comparator<Seed>() {
            @Override
            public int compare(Seed s1, Seed s2) {
                return s1.cmprTo(s2);
            }
        });
        for (Seed s : sortSeeds) {
            bw.write(s.printAll() + "\n");
            // FILTER: Rule-based 1 -> harus dibentuk > 1 pattern
            if (s.patterns.size() > 1) {
                bw2.write(s.printAll() + "\n");
            }
        }
        bw.close();
        bw2.close();
    }
}
