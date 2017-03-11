import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class STree {
	
	public TNode root;
	public BufferedWriter bw;

	public STree() {
		root = new TNode("^", null, "");
	}

	public void printTree() throws IOException{
		bw = new BufferedWriter(new FileWriter("tmp_tree"));

		recursivePrintTree("", root, true);

		bw.close();
	}
	
	private void recursivePrintTree(String prefix, TNode node, boolean isLast)throws IOException {
		if (node == null) {
			return;
		}

		String printNama = node.name;
		if(node.isRelation) {
			printNama += "  ";
			for (String k : node.relationList) {
				printNama += ","+k+"";
			}
		} 

		if (isLast) {
			// System.out.println(prefix + "`-" + printNama + " ("+node.occurance+")");
			bw.write(prefix + "`-" + printNama + " ("+node.occurance+")\n");
		} else {
			// System.out.println(prefix + "|-" + printNama + " ("+node.occurance+")");
			bw.write(prefix + "|-" + printNama + " ("+node.occurance+")\n");
		}

		int size = node.childs.size();

		int count = 1;

		for (TNode v : node.childs.values()) {
			if (count < size) {
				recursivePrintTree(prefix + " " ,v, false);				
			} else {
				recursivePrintTree(prefix + "  ",v, true);
			}
		}
	} 

	public void addSequence(String[] sequence) {
		
		// check rel
		String rel = checkRel(sequence[0]);

		TNode node;

		if (rel.length() > 0) {
			String kata = getKata(sequence[0], rel);

			if (root.childs.containsKey(rel)){
				node = root.childs.get(rel);
				node.incrementOccurance();
				node.addRel(kata);
			} else {
				node  = new TNode(rel, root, kata);
				root.childs.put(rel, node);
			}	

		} else {
			if (root.childs.containsKey(sequence[0])){
				node = root.childs.get(sequence[0]);
				node.incrementOccurance();
			} else {
				node  = new TNode(sequence[0], root);
				root.childs.put(sequence[0], node);
			}	
		}

		for (int i = 1; i < sequence.length; i++) {

			// check rel
			rel = checkRel(sequence[i]);
			if (rel.length() > 0) {
				String kata = getKata(sequence[i], rel);

				if (node.childs.containsKey(rel)){
					node = node.childs.get(rel);
					node.incrementOccurance();
					node.addRel(kata);
				} else {
					TNode newNode = new TNode(rel, node, kata);
					node.childs.put(rel, newNode);
					node = newNode;
				}	


			} else {
				if (node.childs.containsKey(sequence[i])){
					node = node.childs.get(sequence[i]);
					node.incrementOccurance();
				} else {
					TNode newNode = new TNode(sequence[i], node);
					node.childs.put(sequence[i], newNode);
					node = newNode;
				}
			}

		}
			// printTree();
	}

	private String checkRel(String token) {
		// check relation here
		if (token.contains("hypernym")) {
			return "hypernym";
		}
		if (token.contains("hyponym")) {
			return "hyponym";
		}
		if (token.contains("meronym")) {
			return "meronym";
		}
		if (token.contains("holonym")) {
			return "holonym";
		}
		return "";
	}

	private String getKata(String token, String rel) {
		int base = rel.length()+2;
		int n = token.length();
		if (n > ((base)*2)) {
			return token.substring(base, n-base);
		}
		return "kata-" + rel;
	}

}