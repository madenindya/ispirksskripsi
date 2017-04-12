
public class Pair {
    
    int begin;
    int end;
    String key;
    
    public Pair (int b, int e) {
        this.begin = b;
        this.end = e;
    }
    
    public String getStr() {
        return "("+begin+","+end+") --> " + key;
    }
}
