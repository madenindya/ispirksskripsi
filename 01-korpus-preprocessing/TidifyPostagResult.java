import java.util.*;
import java.io.*;

public class TidifyPostagResult {

    public static void main(String[] args) throws IOException {
        // java TidifyPostagResult /media/madenindya/42C0191EC01919AD/Skripsi-fix-data/wiki-ind-s-postag-1-messy ../00-data/wiki/wiki-ind-s-postag

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Folder of folders input name: ");
        // String dpath = bf.readLine();
        String dpath = args[0];
        System.out.println("Folder of folders output name: ");
        // String rpath = bf.readLine();
        String rpath = args[1];


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
                String token = fixTag(tokens[i], i); // fix some tags
                // special
                if (token.contains("<start>")) {
                    bw.write(token);
                } else if (token.contains("<end>")) {
                    bw.write(" " + token + "\n");
                } else if (token.contains("</doc>")) {
                    bw.write(token + "\n");
                } // commoners
                else if (token.length() > 0) {
                    bw.write(" " + token);
                }                 
            }

            line = bff.readLine();
        }

        bw.close();
    }


    // RULE BASED to FIX TAGs
    public static String fixTag(String term, int pos) {
        int last = term.lastIndexOf('_');
        if (last == -1 ) {
            System.out.println(term);
            return "";
        }
        String word = term.substring(0, last);
        String tag = term.substring(last+1);

        if (word.equalsIgnoreCase("-rrb-")) {
            return "";
        }
        if (word.equalsIgnoreCase("<start>") || word.equalsIgnoreCase("<end>") || word.equalsIgnoreCase("</doc>")) {
            word += "_X";
        } else if (word.length() > 5 && word.substring(0,3).equalsIgnoreCase("ber")) {
            if ((word.charAt(0) == 'b' && tag.equalsIgnoreCase("NNP")) 
                || tag.equalsIgnoreCase("NN")) {
                word += "_VB"; 
            } else {
                word = term;
            }
        } else if (word.equalsIgnoreCase("jenis") || word.equalsIgnoreCase("sejenis") || word.equalsIgnoreCase("jenis-jenis") 
                || word.equalsIgnoreCase("seorang") || word.equalsIgnoreCase("contoh")) {
            word += "_NND";
        } else if (word.equalsIgnoreCase("seseorang")) {
            word += "_PRP";
        } else if (word.equalsIgnoreCase("semacam")) {
            word += "_IN";
        } else if (word.equalsIgnoreCase(":") || word.equalsIgnoreCase(",")) {
            word += "_Z";
        }
        else {
            word = term;
        }

        return word;
    }
}
