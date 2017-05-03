import java.util.*;
import java.io.*;

public class SortKorpus {
    public static void main(String[] args) throws Exception {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        String input;
        try {
            input = args[0];
        } catch (Exception e) {
            System.out.println("Filename:");
            input = bf.readLine();
        }
        bf.close();


        List<Pair> pairs = new ArrayList<>();

        bf = new BufferedReader(new FileReader(input+".korpus"));
        String line = bf.readLine();
        while (line != null) {
            pairs.add(new Pair(line.trim()));
            line = bf.readLine();
        }
        bf.close();

        Collections.sort(pairs, new Comparator<Pair>() {
            @Override
            public int compare(Pair s1, Pair s2) {
                int a = s1.hypernym.compareTo(s2.hypernym);
                System.out.print(a + " ");
                return a;
            }
        });

        BufferedWriter bw = new BufferedWriter(new FileWriter(input+"-sorted.korpus"));
        for (Pair p : pairs) {
            bw.write(p.pair+"\n");
        }
        bw.close();

    }
}

class Pair {
    String pair;
    String hypernym;

    public Pair (String p) {
        this.pair = p;
        String chype = p.split(";")[1];
        hypernym = chype.substring(0,chype.length()-1);
    }
}
