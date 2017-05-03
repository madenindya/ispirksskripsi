import java.util.*;
import java.io.*;

// java MainTagging ../../00-data/korpus-kata/iterasi-1.korpus ../../00-data/wiki/wiki-ind-s ../../00-data/wiki/wiki-ind-tagged-iterasi-2
public class MainTagging {
    
    public static void main(String[] args) throws IOException {
    
        SeedLoader sl = new SeedLoader();
        SentenceTagging st = new SentenceTagging();

        // build seed
        String spath = args[0];
        Set<Seed> seeds = sl.loadAllSeeds(spath);
        // printSetSeed(seeds);

        // tag sentence
        String dirpath = args[1];
        String odir = args[2];
        File dir = new File(dirpath);
        for (File file : dir.listFiles()) {
            st.tagDir(dirpath, file.getName(), seeds, odir);
        }

    }

    private static void printSetSeed (Set<Seed> ss) {
        for (Seed s : ss) {
            System.out.println(s.getString());
        }
    }

}
