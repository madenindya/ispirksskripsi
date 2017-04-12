import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.FileReader;

import java.util.List;
import java.util.ArrayList;


public class PatternGeneralization {
    
    public static void main(String[] args) throws IOException {
        
        String filename = "";
        List<String> arrPattern = new ArrayList<>();

        // 1. baca pattern dari file
        BufferedReader bff = new BufferedReader(new FileReader(filename));
        String line = bff.readLine();
        while(line != null && line.length() > 0) {
        // 2. simpan ke array
            arrPattern.add(line);
            line = bff.readLine();  
        }

        List<String> results = generalize(arrPattern);

        // 4. simpan generate pattern
    }

    public static List<String> generalize(List<String> arrPattern) {

        List<String> result = new ArrayList<>();

        for (int i = 0; i < arrPattern.size(); i++) {
            for (int j = 0; j < arrPattern.size(); j++) {

                findGeneralizedPattern(arrPattern.get(i), arrPattern.get(j));

            }
        }

    }



    static class Pattern {
        String pattern;
        int value;

        public Pattern (String s, int n) {
            this.pattern = s;
            this.value = n;
        }
    }
}
