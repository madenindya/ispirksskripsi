import java.util.List;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

public class MainCoba {
    public static void main(String[] args) {

        Trie tree = new Trie();

        String[] sequence1 = {"<start>", "<hyponym>anjing gils<hyponym>", "adalah", "<hypernym>serigala<hypernym>"};
        String[] sequence2 = {"<start>", "<hyponym>harimau<hyponym>", "adalah", "<hypernym>kucing<hypernym>"};
        String[] sequence3 = {"<start>", "<hyponym>kerbau<hyponym>", "adalah", "<hypernym>binatang<hypernym>", "yang"};

        tree.addSequence(sequence1, "made");
        tree.addSequence(sequence2, "made");
        tree.addSequence(sequence3, "nityasya");

        // for (int i = 0; i < tree.leafs.size(); i++) {
        //     System.out.println(tree.leafs.get(i).name);
        // }

        List<MyPattern> hasil = tree.getAllMyPatterns();
        for (MyPattern p : hasil) {
            System.out.println(p.getStr());
        }

    }
}
