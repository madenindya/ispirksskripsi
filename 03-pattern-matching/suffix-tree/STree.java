import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Exception;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.lang.Override;
import java.util.Collections;
import java.util.Comparator;

public class STree {
	
	public TNode root;
	public BufferedWriter bw;
	public List<TmpNode> sortList;
	public HashMap<String, List<String>> index;

	public STree() {
		root = new TNode("^", null, "");
		index = new HashMap<>();
	}


	/**
	* ADD NEW SEQUENCE to tree
	*/
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
	}


	/**
	* PRINT TREE
	*/
	public void printTree(String ofile) throws IOException{
		bw = new BufferedWriter(new FileWriter(ofile));

		recursivePrintTree("", root, true);

		bw.close();
	}
	
	private void recursivePrintTree(String prefix, TNode node, boolean isLast)throws IOException {
		if (node == null) {
			return;
		}

		String printNama = node.name;

		if (isLast) {
			bw.write(prefix + "`-" + printNama + " ("+node.occurance+")\n");
			prefix += "  ";
		} else {
			bw.write(prefix + "|-" + printNama + " ("+node.occurance+")\n");
			prefix += "| ";
		}

		int size = node.childs.size();

		int count = 1;

		for (TNode v : node.childs.values()) {
			if (count < size) {
				recursivePrintTree(prefix,v, false);				
			} else {
				recursivePrintTree(prefix,v, true);
			}
			count++;
		}
	} 


	/**
	* PRINT PATTERN
	*/
	public void getPattern(int minOccurance, String ofile) throws Exception {
		bw = new BufferedWriter(new FileWriter(ofile));
		sortList = new ArrayList<>();

		for (TNode tn : root.childs.values()) {
			recursiveGetPattern(tn, minOccurance, "");			
		}

		Collections.sort(sortList, new Comparator<TmpNode>() {
			@Override
			public int compare(TmpNode o1, TmpNode o2) {
		        if(o1.count == o2.count)
		            return o2.sentence.split(" ").length - o1.sentence.split(" ").length;
		        else
		            return o2.count - o1.count;
		    	}
		});

		for (TmpNode pr : sortList) {
			bw.write(pr.sentence + " -- (" + pr.count + ")\n");
		}
		
		bw.close();
	}

	private void recursiveGetPattern(TNode node, int min, String prev) throws Exception {
		if (node.occurance < min) {
			return;
		} 

		// update prev
		prev += " " + node.name;

		if (node.childs.size() < 1) {
			sortList.add(new TmpNode(prev, node.occurance));
		} 
		else {
			for (TNode tn : node.childs.values()) {
				recursiveGetPattern(tn, min, prev);
			}
		}
	}



	// ----- PRIVATE METHOD -----
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

	// get lemma from hypernym/hyponym tag
	// TODO change this to use regex
	private String getKata(String token, String rel) {
		int base = rel.length()+2;
		int n = token.length();
		if (n > ((base)*2)) {
			return token.substring(base, n-base);
		}
		return "kata-" + rel;
	}

	class TmpNode {
		String sentence;
		int count;

		public TmpNode(String s, int c) {
			this.sentence = s;
			this.count = c;
		}

	}

}
