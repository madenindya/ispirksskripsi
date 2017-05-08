import java.util.*;
import java.io.*;

public class MainABC {

    static SuffixTree st;

    public static void main(String [] args) throws Exception {
        st = new SuffixTree();
        String path = "../../00-data/wiki/wiki-ind-s-postag";
        String opath = "../../00-data/wiki/wiki-ind-js-postag/";

        File dir = new File(path);
        String[] sdir = dir.list();
        for (int i = 0; i < sdir.length; i++) {
            String pathsubdir = path + "/" + sdir[i];
            File subdir = new File(pathsubdir);
            System.out.println("Working with " + pathsubdir);
            BufferedWriter bw = new BufferedWriter(new FileWriter(opath + sdir[i] + ".korpus"));
            for (File f : subdir.listFiles()) {
                 String filepath = pathsubdir + "/" + f.getName();

                 workthis(filepath, bw);
            }
            bw.close();
        }
    }

    public static void workthis(String ipath, BufferedWriter bw) throws Exception {

        BufferedReader bf = new BufferedReader(new FileReader(ipath));
        String line = bf.readLine();
        while (line != null) {
            String sentence = st.getSequence(line);
            bw.write(sentence+"\n");
            line = bf.readLine();
        }
        bf.close();

    }
}
