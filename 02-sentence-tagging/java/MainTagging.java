import java.util.*;
import java.io.*;

public class MainTagging {
    
    // java MainTagging <seed_file_name> <input_folder_name> <output_folder_name>
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
