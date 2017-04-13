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

import java.lang.Override;
import java.util.Collections;
import java.util.Comparator;

public class MainMatching {

    static Map<String, Seed> resultAll;
    static PatternMatching pm;
    static int cnt;

    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        resultAll = new HashMap<>();
        cnt = 0;

        System.out.println("Pattern file name: ");
        String ipath = bf.readLine();
        pm = new PatternMatching(ipath);

        System.out.println("Folder of folders to be read: ");
        String path = bf.readLine();
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

        System.out.println("File to be written: ");
        String opath = bf.readLine();
        printSortMap(resultAll, opath);
    }

    public static void findMatching(String fpath) throws IOException {
        BufferedReader bff = new BufferedReader(new FileReader(fpath));
        String sentence = bff.readLine();
        while (sentence != null && sentence.length() > 0) {

            // match one sentence
            Map<String, Seed> resultOne = pm.matchAll(sentence);
            for (String k : resultOne.keySet()) {
                Seed cseed = resultOne.get(k);
                if (resultAll.containsKey(k)) {
                    Seed eseed = resultAll.get(k);
                    eseed.count += cseed.count;
                    eseed.patterns.addAll(cseed.patterns);
                    eseed.sentences.addAll(cseed.sentences);
                } else {
                    resultAll.put(k, cseed);
                }
            }
            sentence = bff.readLine();
        }
        bff.close();
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
        }
        bw.close();
    }
}
