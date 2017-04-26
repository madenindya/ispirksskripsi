import java.util.*;
import java.io.*;

public class IrisanSeed {
    
    static Map<String, Integer> combined;

    public static void main(String[] args) throws Exception {

        combined = new HashMap<>();

        System.out.println("SameLemma");
        buildMap("seed_sameLemma_v2_11");

        System.out.println("SimilarityScore");
        buildMap("seed_similarityScore_v2_11");

        System.out.println("Strict");
        buildMap("seed_superStrict_v2_11");

        printToFile("seed_combined_11");


    }

    public static void buildMap (String filename) throws IOException {

        BufferedReader bf = new BufferedReader(new FileReader(filename));

        String line = bf.readLine();
        while (line != null) {
            if (line.length() <= 0) {
                continue;
            }
            if (combined.containsKey(line)) {
                int curr = combined.get(line);
                combined.put(line, curr+1);
            } else {
                combined.put(line, 1);
            }

            line = bf.readLine();       
        }


        bf.close();

    }

    public static void printToFile(String filename) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(filename));

        List<String> keys = new ArrayList<>();
        for (String k : combined.keySet()) {
            if (combined.get(k) == 3) {
                keys.add(k);
            }
        }
        Collections.sort(keys);
        for (String k : keys) {
            bf.write(k+"\n");
            
        }

        bf.close();
    }
}
