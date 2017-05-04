import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.regex.*;
import java.io.*;

import java.lang.Override;
import java.util.Collections;
import java.util.Comparator;


// java MainIterasiN ../../00-data/wiki/wiki-ind-tagged-iterasi-2 tmpattern/iterasi-2 2
// korpustag hypernym-hyponym no postag
public class MainIterasiN {

    static SeqBuilder sb;
    static Map<String, MyPattern> unik;
    static Set<String> tmpunik;

    public static void main(String[] args) throws IOException {

        sb = new SeqBuilder();
        unik = new HashMap<>();
        tmpunik = new HashSet<>();     

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String path;
        try {
            path = args[0];
        } catch (Exception e) {
            System.out.println("input folder (ex: /home/madenindya):");
            path = bf.readLine(); 
        }

        String opath;
        try {
            opath = args[1];
        } catch (Exception e) {
            System.out.println("output file:");
            opath = bf.readLine();
        }

        int iterasike;
        try {
            iterasike = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.out.println("iterasi ke: ");
            iterasike = Integer.parseInt(bf.readLine());
        }

        // default
        int min = 3;

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


        // ==== HASIL ==== 
        // load old selected pattern
        Set<String> patterns = new HashSet<>();
        BufferedReader bff = new BufferedReader(new FileReader("tmpattern/iterasi-"+(iterasike-1)+"-selected.pattern"));
        String line = bff.readLine();
        while (line != null) {
            patterns.add(line.trim());
            line = bff.readLine();
        }
        bff.close();
        System.out.println("Unik size " + unik.size());
        // load old all pattern & masukan ke all pattern baru
        bff = new BufferedReader(new FileReader("tmpattern/iterasi-"+(iterasike-1)+".pattern"));
        line = bff.readLine();
        int dup = 0;
        int newunik = 0;
        while (line != null) {
            MyPattern npat = new MyPattern(line); // buat pattern baru
            Pattern p1 = Pattern.compile("hypernym|hyponym");
            Matcher m1 = p1.matcher(npat.pattern);
            String unikey = m1.replaceAll("relation");
            if (unik.containsKey(unikey)) {
                dup++;
                if (unik.get(unikey).cmprTo(npat) > 0) { // masukan yg ranking lebih tertinggi                
                    unik.put(unikey, npat);
                }
            } else {
                newunik++;
                unik.put(npat.pattern, npat);
            }
            line = bff.readLine();
        }
        bff.close();
        System.out.println("Duplicate " + dup);
        System.out.println("Newunik " + newunik);
        System.out.println("Unik size after " + unik.size());
        // ranking all old & new pattern
        List<MyPattern> ppatterns = new ArrayList<>();
        for (String k : unik.keySet()) {
            ppatterns.add(unik.get(k));
        }
        Collections.sort(ppatterns, new Comparator<MyPattern>() {
            @Override
            public int compare(MyPattern p1, MyPattern p2) {
                return p1.cmprTo(p2);
            }
        });
        System.out.println(ppatterns.size());
        // print to file all old & new pattern
        String newpat = null;
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath+".pattern"));
        for (MyPattern mp : ppatterns) {
            bw.write(mp.getStr() + "\n");
        // +1 pattern
            if (newpat == null) {
                System.out.println("Try pattern --> " + mp.pattern);
                boolean success = true;
                for (String sp : patterns) {
                    if (sp.contains(mp.pattern)) { // pattern baru gak boleh substring dari salah satu pattern lama.
                        success = false;
                    }
                }
                if (success) {
                    newpat = mp.pattern;                    
                }
            }
        }
        bw.close();
        patterns.add(newpat);
        // print selected pattern
        bw = new BufferedWriter(new FileWriter(opath+"-selected.pattern"));
        for (String p : patterns) {
            bw.write(p+"\n");
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
