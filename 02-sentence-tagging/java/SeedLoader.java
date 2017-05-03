import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class SeedLoader {
    
    public Set<Seed> loadAllSeeds(String spath) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(spath));
        Set<Seed> result = new HashSet<>();

        String line = bf.readLine();
        // (Sukorejo_NNP,desa_NN) ; 31 31 1
        while(line != null) {

            String s1 = line.substring(1, line.length() - 1);
            String[] tokens = s1.split(";");
            // hyponym
            String hyponym = tokens[0];
            // hypernym
            String hypernym = tokens[1];

            Seed nseed = new Seed(hypernym, hyponym);
            if (nseed.evaluateSeed()) {
                result.add(nseed);
            }

            line = bf.readLine();
        }
        bf.close();

        return result;
    }
}
