import java.util.Map;
import java.util.HashMap;

public class TNode {
    String name;
    String postag;
    Map<String, TNode> childs;

    public TNode (String name, String postag) {
        this.name = name;
        this.postag = postag;
        this.childs = new HashMap<>();
    }
}
