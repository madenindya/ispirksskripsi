import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Exception;

import java.util.List;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import java.lang.Override;
import java.util.Collections;
import java.util.Comparator;


public class Trie {

	public TNode root;
	public Set<TNode> leafs;

	public Trie() {
		root = new TNode("^", null);
		leafs = new HashSet<>();
	}

	/**
	* ADD NEW SEQUENCE to tree
	*/
	public void addSequence(List<String> sequence, String sentence) {

		String hyponym = "";
		String hypernym = "";
		TNode node = root;

		for (int i = 0; i < sequence.size(); i++) {
			String rel = checkRel(sequence.get(i));
			if (rel.length() > 0) {
				String kata = getKata(sequence.get(i), rel);
				if (rel.equals("<hypernym>")) {
					hypernym = kata;
				} else if (rel.equals("<hyponym>")) {
					hyponym = kata;
				}

				if (node.childs.containsKey(rel)) {
					TNode cnode = node.childs.get(rel);
					cnode.incrementOccurance();
					cnode.addRel(kata);
					node = cnode;
				} else {
					TNode nnode = new TNode(rel, node, kata);
					node.childs.put(rel, nnode);
					node = nnode;
				}
			} else {
				if (node.childs.containsKey(sequence.get(i))) {
					TNode cnode = node.childs.get(sequence.get(i));
					cnode.incrementOccurance();
					node = cnode;
				} else {
					TNode nnode = new TNode(sequence.get(i), node);
					node.childs.put(sequence.get(i), nnode);
					node = nnode;
				}
			}
			if (node.isLastNode) {
				updateLeaf(node, hypernym, hyponym, sentence);
			}
		}

		node.isLastNode = true;
		updateLeaf(node, hypernym, hyponym, sentence);
		leafs.add(node);
	}

	private void updateLeaf(TNode node, String hypernym, String hyponym, String sentence) {
		if (hypernym.length() < 1 || hyponym.length() < 1) return;
		String krel = "("+hyponym+","+hypernym+")";
		node.seeds.add(krel);
		node.sentences.add(sentence);
	}


	/** 
	*	GET ALL PATTERN
	*/
	public List<MyPattern> getAllMyPatterns(int min) {
		List<MyPattern> result = new ArrayList<>();
		for (TNode node : leafs) {
			if (node == null) {
				continue;
			}

			String pprint = "";
			MyPattern npatt = new MyPattern();
			npatt.count = node.occurance;
			npatt.seeds = node.seeds;
			npatt.sentences = node.sentences;

			if (npatt.seeds.size() <= 1 || npatt.sentences.size() <= 1) {
				continue;
			}

			while (!node.name.equals("^")) {
				pprint = " " + node.name + pprint;
				node = node.parent;
			}

			if (isFiltered(npatt, min)) {
				continue;
			}

			if (pprint.length() > 1) {
				npatt.pattern = pprint.substring(1);				
				result.add(npatt);				
			}
		}
		return result;
	}

	private boolean isFiltered(MyPattern p, int min) {
		if (p.count < min) return true;
		if (p.getWeight() < 0.5) return true;	// jumlah seed unik dibandingkan dengan jumlah sentence unik harus seimbang
		if (p.getWeight2() < 0.2) return true;	// seeds sedikit unik gpp
		if (p.getWeight3() < 0.7) return true;	// sentence harus banyak unik
		return false;
	}

	/**
	*	ALL PRIVATE METHODS
	*/
	private String checkRel(String token) {
		// check relation here
		if (token.contains("hypernym")) {
			return "<hypernym>";
		}
		if (token.contains("hyponym")) {
			return "<hyponym>";
		}
		if (token.contains("meronym")) {
			return "<meronym>";
		}
		if (token.contains("holonym")) {
			return "<holonym>";
		}
		return "";
	}

	// get lemma from hypernym/hyponym tag
	private String getKata(String token, String rel) {
		String[] tokens = token.split(rel);
		String result = "";
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].length() > 0) {
				if (result.length() > 0) {
					result += " " + tokens[i];
				} else {
					result = tokens[i];
				}
			}
		}
		return result;
 	}
}

