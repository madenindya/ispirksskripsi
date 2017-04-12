
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

public class TNode {
	public String name;
	public int occurance;

	public HashMap<String, TNode> childs;
	public TNode parent;

	public boolean isRelation;
	public ArrayList<String> relationList;

	public boolean isLastNode;
	public Set<String> seeds;
	public Set<String> sentences;

	public TNode (String name, TNode parent) {
		this.name = name;
		this.parent = parent;
		this.occurance = 1;
		this.childs = new HashMap<String, TNode>();
		this.isRelation = false;
		this.isLastNode = false;
		this.seeds = new HashSet<>();
		this.sentences = new HashSet<>();
	}

	public TNode(String name, TNode parent, String nameRel) {
		this.name = name;
		this.parent = parent;
		this.occurance = 1;
		this.childs = new HashMap<String, TNode>();
		this.isRelation = true;
		this.relationList = new ArrayList<String>();
		this.relationList.add(nameRel);
		this.isLastNode = false;
		this.seeds = new HashSet<>();
		this.sentences = new HashSet<>();
	}

	public void incrementOccurance() {
		this.occurance += 1;
	}

	public void addRel(String nameRel) {
		// occurance update not in here
		relationList.add(nameRel);
	}

	public boolean isExistRelLemma(String nameRel) {
		// check if current lemma already in relationList
		return relationList.contains(nameRel);
	}
}
