import java.io.*;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;

import java.lang.Override;
import java.util.Collections;
import java.util.Comparator;

public class SortPattern {

    public static void main(String[] args) throws IOException {
        
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Input file");
        String ipath = bf.readLine();
        System.out.println("Output file");
        String opath = bf.readLine();

        List<MyNode> nodes = new ArrayList<>();
                
        BufferedReader bff = new BufferedReader(new FileReader(ipath));
        String line = bff.readLine();
        while (line != null || line.length() < 1) {
            if (line.length() < 1) {
                break;
            }
            String[] tokens = line.split(" ");
            String last = tokens[tokens.length - 1];
            last = last.substring(1, last.length() - 1);

            int valast = Integer.parseInt(last);
            MyNode nnode = new MyNode(line, valast);
            nodes.add(nnode);

            line = bff.readLine();
        }

        Collections.sort(nodes, new Comparator<MyNode>() {
            @Override
            public int compare(MyNode o1, MyNode o2) {
                if(o1.count == o2.count)
                    return o2.sentence.split(" ").length - o1.sentence.split(" ").length;
                else
                    return o2.count - o1.count;
            }
        });

        System.out.println("CHECKPOINT");
        BufferedWriter bw = new BufferedWriter(new FileWriter(opath));
        for (MyNode cnode : nodes) {
            bw.write(cnode.sentence + "\n");
        }
        bw.close();

    }

    static class MyNode {
        String sentence;
        int count;

        public MyNode(String line, int sum) {
            this.sentence = line;
            this.count = sum;
        }
    }

}
