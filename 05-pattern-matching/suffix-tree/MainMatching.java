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

public class MainMatching {

    static Map<String, Seed> resultAll;
    static PatternMatching pm;

    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        resultAll = new HashMap<>();

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
        printMap(resultAll, opath);
    }

    public static void findMatching(String fpath) throws IOException {
        BufferedReader bff = new BufferedReader(new FileReader(fpath));
        String sentence = bff.readLine(); int cnt = 0;
        while (sentence != null && sentence.length() > 0) {
            cnt++;
            if (cnt % 1000 == 0) { System.out.println(cnt); }

            Map<String, Seed> resultOne = pm.matchAll(sentence);
            for (String k : resultOne.keySet()) {
                Seed cseed = resultOne.get(k);
                
                if (resultAll.containsKey(k)) {
                    Seed eseed = resultAll.get(k);
                    eseed.count += eseed.count;
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

    public static void printMap(Map<String, Seed> m, String opath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath));
        for (String k : m.keySet()) {
            String printed = m.get(k).printAll();
            bw.write(printed + "\n");
        }
        bw.close();
    }
}
