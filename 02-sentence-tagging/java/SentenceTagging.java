import java.io.BufferedWriter;
import java.util.List;
import java.util.ArrayList;

public class SentenceTagging {

    /**
    *   - Tag 1 sentence with 1 seed
    *   - sentence NO TAG
    *   - sentence in lower case
    */        
    public void tagSentence(String sentence, Seed s, BufferedWriter bw) {

        // sentence --> <start> ... <end> --> bellow should be safe 
        String hype = " " + s.hypernmy + " ";
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
