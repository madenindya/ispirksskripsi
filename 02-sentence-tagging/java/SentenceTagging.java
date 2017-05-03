import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class SentenceTagging {


    /**
    *   Tag all folder
    *   - iDir = folder of folders
    *   - oDir = folder of files
    */ 
    public void tagDir(String iDir, String nDir, Set<Seed> ss, String oDir) throws IOException {
        String dpath = iDir + "/" + nDir;
        File dir = new File(dpath);

        String opath = oDir + "/tagged_" + nDir;
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath));

        for (File file : dir.listFiles()) {
            String fpath = dpath + "/" + file.getName();
            tagFile(fpath, ss, bw);
        }

        bw.close();
    }

    /**
    *   Tag a file
    */ 
    public void tagFile(String ipath, Set<Seed> ss, BufferedWriter bw) throws IOException {
        System.out.println("Start tagging " + ipath + " ...");
        BufferedReader bf = new BufferedReader(new FileReader(ipath));
        String line = bf.readLine();
        while (line != null) {
            tagSentenceSeeds(line, ss, bw);
            line = bf.readLine();
        }
        bf.close();
    }

    /**
    *   Tag 1 sentence with all seed
    *   - sentence NO TAG
    */
    private void tagSentenceSeeds(String line, Set<Seed> ss, BufferedWriter bw) throws IOException {
        String sentence = line.toLowerCase();
        // System.out.println(sentence);
        for (Seed s : ss) {
            // System.out.println(s.hypernym + " " + s.hyponym);
            tagSentence(sentence, s, bw);
        }
    }

    /**
    *   - Tag 1 sentence with 1 seed
    *   - sentence in lower case
    */        
    private void tagSentence(String sentence, Seed s, BufferedWriter bw) throws IOException {

        // sentence --> <start> ... <end> --> bellow should be safe 
        String hype = " " + s.hypernym + " ";
        String hypo = " " + s.hyponym + " ";

        List<String> hasil = new ArrayList<>();

        if (sentence.contains(hype) && sentence.contains(hypo)) {
            // 1. split hyponym 
            String[] noHo = sentence.split(hypo);
            for (int i = 0; i < noHo.length; i++) {

                // 2. format ulang
                if (i == 0) noHo[i] += " ";
                else if (i == noHo.length-1) noHo[i] = " " + noHo[i] + " ";
                else noHo[i] = noHo[i] + " ";

                boolean conHe = false;
                String tmpHe = "";
                // 3. split hypernym
                if (noHo[i].contains(hype)) {
                    // cek apakah ada di last
                    boolean isInLast = false;
                    if (noHo[i].substring(noHo[i].length() - hype.length()).equals(hype)) {
                        isInLast = true;
                    }
                    // System.out.println(isInLast);

                    conHe = true;
                    String[] noHe = noHo[i].split(hype);
                    tmpHe = noHe[0];
                    for (int j = 1; j < noHe.length; j++) {
                        // 4. simpan hypernym
                        tmpHe += " <hypernym>"+s.hypernym+"<hypernym> ";
                        tmpHe += noHe[j];
                    }

                    // 5. special case kalo ada di terakhir untuk hypernym
                    // kalo hyponym gak mungkin terjadi karena ada <end>
                    if (isInLast) {
                        tmpHe += " <hypernym>"+s.hypernym+"<hypernym> ";
                    }
                } 

                // 5. simpan hyponym
                if (i == 0 && conHe) {
                    // System.out.println("1 --> " + tmpHe);
                    // specila case --> masukin tmpHe duluan
                    hasil.add(tmpHe);
                } else {
                    if (i != 0) {
                        // System.out.println("2 --> " + s.hyponym);
                        hasil.add("<hyponym>"+s.hyponym+"<hyponym>");
                    }

                    if (conHe) {
                        // System.out.println("3 --> " + tmpHe);
                        hasil.add(tmpHe);
                    }
                    else {
                        // System.out.println("4 --> " + noHo[i]);
                        hasil.add(noHo[i]);
                    }
                }
            }
        }

        if (hasil.size() > 0) {
            String fhasil = hasil.get(0);
            for (int i = 1; i < hasil.size(); i++) {
                fhasil += hasil.get(i);
            }
            // print
            if (fhasil.contains("<hypernym>") && fhasil.contains("<hyponym>")) {
                bw.write(fhasil+"\n");
            }  
        }
    }
}
