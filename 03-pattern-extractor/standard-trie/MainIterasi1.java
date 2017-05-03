import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.io.*;

import java.lang.Override;
import java.util.Collections;
import java.util.Comparator;


// java MainSTreeIterasi1 tmpattern/nama_file
// korpustag hypernym-hyponym no postag
public class MainIterasi1 {

    static SeqBuilder sb;
    static Map<String, MyPattern> unik;
    static Set<String> tmpunik;

    public static void main(String[] args) throws IOException {

        // default
        int min = 3;
        
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String opath;
        try {
            opath = args[0];
        } catch(Exception e) {
            System.out.println("output file:");
            opath = bf.readLine();    
        }

        // START - samelemma
        System.out.println("\nSameLemma");
        sb = new SeqBuilder();
        unik = new HashMap<>();
        tmpunik = new HashSet<>();     

        String path = "../../00-data/wiki/wiki-ind-tagged-sameLemma/hh";

        // in-between builder
        System.out.println("Find in-between pattern");
        List<MyPattern> hasil = findPattern(path, 0, 0, min);
        unikfy(hasil);
        // n-before/after builder
        int count = 1;
        while (tmpunik.size() > 0 && count < 3) {
            System.out.println("Find "+count+"-before/after pattern");
            List<MyPattern> hasilBefore = findPattern(path, 1, count, min);
            List<MyPattern> hasilAfter = findPattern(path, 2, count, min);
            List<MyPattern> hasilBaru = cekNeeded(hasilBefore, hasilAfter);
            unikfy(hasilBaru);
            count++;
        }
        // END - samelemma

        // simpan dalam temporary
        Map<String, MyPattern> hasil1 = new HashMap<>();
        for (String k : unik.keySet()) {
            hasil1.put(k, unik.get(k));
        }

        // START - strict
        System.out.println("\nStrict");
        sb = new SeqBuilder();
        unik = new HashMap<>();
        tmpunik = new HashSet<>();     

        path = "../../00-data/wiki/wiki-ind-tagged-strict/hh";

        // in-between builder
        System.out.println("Find in-between pattern");
        hasil = findPattern(path, 0, 0, min);
        unikfy(hasil);
        // n-before/after builder
        count = 1;
        while (tmpunik.size() > 0 && count < 3) {
            System.out.println("Find "+count+"-before/after pattern");
            List<MyPattern> hasilBefore = findPattern(path, 1, count, min);
            List<MyPattern> hasilAfter = findPattern(path, 2, count, min);
            List<MyPattern> hasilBaru = cekNeeded(hasilBefore, hasilAfter);
            unikfy(hasilBaru);
            count++;
        }
        // END - strict


        // COMBINE sameLemma - strict
        System.out.println("SameLemma -> " + hasil1.size());
        System.out.println("Strict -> " + unik.size());
        List<MyPattern> ppatterns = new ArrayList<>();
        for (String k : unik.keySet()) {
            if (hasil1.containsKey(k)) {
                if (hasil1.get(k).cmprTo(unik.get(k)) > 0) {
                    ppatterns.add(unik.get(k));
                } else {
                    ppatterns.add(hasil1.get(k));
                }
            }
        }

        // HASIL
        Set<String> filteredPair = new HashSet<>();
        List<String> filteredPattern = new ArrayList<>();
        Collections.sort(ppatterns, new Comparator<MyPattern>() {
            @Override
            public int compare(MyPattern p1, MyPattern p2) {
                return p1.cmprTo(p2);
            }
        });
        // write to file
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath+".pattern"));
        int ccount = 0;
        for (MyPattern mp : ppatterns) {
            bw.write(mp.getStr() + "\n");
            // simpan seed yang TOP 5
            if (ccount < 5) {
                for (String fs : mp.seeds) {
                    filteredPair.add(fs);
                }
                System.out.println(mp.getStr());
                filteredPattern.add(mp.pattern);
                ccount++;
            }
        }
        bw.close();

        bw = new BufferedWriter(new FileWriter("../../00-data/korpus-kata/iterasi-0.korpus"));
        for (String fp : filteredPair) {
            bw.write(fp+"\n");
        }
        bw.close();
        bw = new BufferedWriter(new FileWriter(opath+"-selected.pattern"));
        for (String fp : filteredPattern) {
            bw.write(fp+"\n");
        }
        bw.close();
    }

    public static List<MyPattern> cekNeeded(List<MyPattern> befores, List<MyPattern> afters) {
        List<MyPattern> needed = new ArrayList<>();
        Set<String> done = new HashSet<>();
        for (MyPattern bp : befores) {
            String bu = bp.getUnik();
            for (String tmpu : tmpunik) {
                if (bu.contains(tmpu)) {
                    needed.add(bp);
                    done.add(tmpu);
                }
            }
        }
        for (MyPattern ap : afters) {
            String au = ap.getUnik();
            for (String tmpu : tmpunik) {
                if (au.contains(tmpu)) {
                    needed.add(ap);
                    done.add(tmpu);
                }
            }
        }
        for (String d : done) {
            tmpunik.remove(d);
        }
        return needed;
    }

    public static void unikfy(List<MyPattern> patterns) {
        System.out.println("NewPattern " + patterns.size());
        System.out.println("before " + unik.size() + " " + tmpunik.size());
        for (MyPattern mp : patterns) {
            String pu = mp.getUnik();
            if (tmpunik.contains(pu)) continue;
            if (unik.containsKey(pu)) {
                unik.remove(pu);
                tmpunik.add(pu);
            } else {
                unik.put(pu, mp);
            }
        }
        System.out.println("after " + unik.size() + " " + tmpunik.size());
    }

    public static List<MyPattern> findPattern(String path, int m, int n, int min) throws IOException {
        Trie tree = new Trie();

        File dir = new File(path);
        for (File file : dir.listFiles()) {
            String filepath = dir + "/" + file.getName();
            System.out.println("Read " + filepath);

            BufferedReader bff = new BufferedReader(new FileReader(filepath));
            String sentence = bff.readLine();
            while (sentence != null && sentence.length() > 0) {
                // build array
                List<String> tokens = sb.tokenizeSentence(sentence);
                Pair p;
                if (m == 1) p = sb.getIndexwPrev(tokens, n);
                else if (m == 2) p = sb.getIndexwFollow(tokens, n);
                else p = sb.getIndexInBetween(tokens); 
                List<String> sequences = sb.getFilteredSequences(tokens, p);

                // put in tree
                tree.addSequence(sequences, sentence);
        
                // update sentence
                sentence = bff.readLine();
            }
            bff.close();
        }
        
        return tree.getAllMyPatterns(min);
    }
}
