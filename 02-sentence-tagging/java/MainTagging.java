import java.util.*;
import java.io.*;

public class MainTagging {
    
    // java MainTagging <seed_file_name> <output_folder_name>
    public static void main(String[] args) throws IOException {
    
        SeedLoader sl = new SeedLoader();

        String spath = args[0];
        Set<Seed> seeds = sl.loadAllSeeds(spath);
        printSetSeed(seeds);
    }

    private static void printSetSeed (Set<Seed> ss) {
        for (Seed s : ss) {
            System.out.println(s.getString());
        }
    }


}
