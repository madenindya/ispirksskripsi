import java.util.*;
import java.io.*;

public class TidifyPostagResult {

    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Folder of folders input name: ");
        String dpath = bf.readLine();
        System.out.println("Folder of folders output name: ");
        String rpath = bf.readLine();


        // iterate dir of dir
        File dir = new File(dpath);
        String[] sdir = dir.list();
        for (int i = 0; i < sdir.length; i++) {
            String pathsubdir = dpath + "/" + sdir[i];
            File subdir = new File(pathsubdir);
            System.out.println("Matching with " + pathsubdir);
            for (File f : subdir.listFiles()) {
                 String filepath = pathsubdir + "/" + f.getName();
                 String opath = rpath + "/" + sdir[i] + "/" + f.getName();
                 processFile(filepath, opath);
            }
        }

    }

    public static void processFile(String fpath, String opath) throws IOException {
        BufferedReader bff = new BufferedReader(new FileReader(fpath));
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath));

        String line = bff.readLine();
        while(line != null && line.length() > 0) {
            String[] tokens = line.split(" ");

            for (int i = 0; i < tokens.length; i++) {
                // special
                if (tokens[i].contains("<start>")) {
                    bw.write(tokens[i]);
                } else if (tokens[i].contains("<end>")) {
                    bw.write(" " + tokens[i] + "\n");
                } else if (tokens[i].contains("</doc>")) {
                    bw.write(tokens[i] + "\n");
                } // commoners
                else {
                    bw.write(" " + tokens[i]);
                }                 
            }

            line = bff.readLine();
        }

        bw.close();
    }
}
