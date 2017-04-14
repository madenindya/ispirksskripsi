public class Seed {
    
    /**
    *   - all seed is in LoweCase
    */
    String hypernym;
    String hyponym;

    public Seed (String he, String ho) {
        this.hypernym = he.toLowerCase();
        this.hyponym = ho.toLowerCase();
    }

    public boolean evaluateSeed() {
        String[] h1 = this.hyponym.split(" ");
        String[] h2 = this.hypernym.split(" ");
        
        int n1 = h1.length;
        int n2 = h2.length;
        
        if (n1 == n2) return true;
        
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
                if (h1[i].equalsIgnoreCase(h2[j])) return false;
            }
        }
        return true;
    }

    public String getSeed() {
        return "("+this.hyponym+","+this.hypernym+")";
    }
}
