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

            String s1 = line.split(" ; ")[0];
            s1 = s1.substring(1, s1.length() - 1);
            String[] tokens = s1.split(",");
            int n = tokens[0].lastIndexOf('_');
            String hyponym = tokens[0].substring(0,n);
            n = tokens[0].lastIndexOf('_');
            String hypernym = tokens[1].substring(0,n);

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
