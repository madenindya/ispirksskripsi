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
// java MainMatching tmppattern/pattern01 ../../00-data/wiki/wiki-ind-s-postag  tmpseedspool/seedpattern01-1
public class MainMatching {

    static Map<String, Seed> resultAll;
    static PatternMatching pm;
    static int cnt;

    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        resultAll = new HashMap<>();
        cnt = 0;

        String ipath;
        if (args.length < 1) {
            System.out.println("Pattern file name: ");
            ipath = bf.readLine();
        } else {
            ipath = args[0];
        }
        pm = new PatternMatching(ipath);

        String path;
        if (args.length < 2) {
            System.out.println("Folder of folders to be read: ");
            path = bf.readLine();
        } else {
            path = args[1];
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
        if (args.length < 3) {
            System.out.println("File to be written: ");
            opath = bf.readLine();
        } else {
            opath = args[2];
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
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath));
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
            if (s.hypernym.equals("negara") && s.hyponym.equals("amerika serikat")) {
                for (String str : s.patterns) {
                    System.out.println(str);
                }
            }
        }
        bw.close();
    }
}
