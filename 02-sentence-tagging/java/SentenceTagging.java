import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SentenceTagging {


    /**
    *   Tag all folder
    *   - iDir = folder of folders
    *   - oDir = folder of files
    */ 
    public void tagDir(String iDir, String nDir, Set<Seed> ss, String oDir) {
        String dpath = iDir + "/" + nDir;
        File dir = new File(dpath);

        String opath = oDir + "/tagged_" + nDir;
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath));

        for (File file : dir.listFiles()) {
            fpath = dpath + "/" + file.getName;
            tagFile(fpath, ss, bw);
        }

        bw.close();
    }

    /**
    *   Tag a file
    */ 
    public void tagFile(String ipath, Set<Seed> ss, BufferedWriter bw) throws IOException {
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
        for (Seed s : ss) {
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
                    conHe = true;
                    String[] noHe = noHo[i].split(hype);
                    tmpHe = noHe[0];
                    for (int j = 1; j < noHe.length; j++) {
                        // 4. simpan hypernym
                        tmpHe += " <hypernym>"+s.hypernym+"<hypernym> ";
                        tmpHe += noHe[i];
                    }
                } 

                // 5. simpan hyponym
                if (i == 0 && conHe) {
                    // specila case --> masukin tmpHe duluan
                    hasil.add(tmpHe);
                } else {
                    if (i != 0) 
                        hasil.add("<hyponym>"+s.hyponym+"<hyponym>");

                    if (conHe) hasil.add(tmpHe);
                    else hasil.add(noHo[i]);
                }
            }
        }

        String fhasil = hasil.get(0);
        for (int i = 1; i < hasil.size(); i++) {
            fhasil += " " + hasil.get(i);
        }
        // print
        if (fhasil.contains("<hypernym>") && fhasil.contains("<hyponym>")) {
            bw.write(fhasil+"\n");
        }
    }
}
