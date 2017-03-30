import java.util.HashMap;
import java.util.ArrayList;

public class TNode {
	public String name;
	public int occurance;

	public HashMap<String, TNode> childs;
	public TNode parent;

	public boolean isRelation;
	public ArrayList<String> relationList;

	public TNode (String name, TNode parent) {
		this.name = name;
		this.occurance = 1;
		this.childs = new HashMap<String, TNode>();
		this.isRelation = false;
	}

	public TNode(String name, TNode parent, String nameRel) {
		this.name = name;
		this.parent = parent;
		this.occurance = 1;
		this.childs = new HashMap<String, TNode>();
		this.isRelation = true;
		this.relationList = new ArrayList<String>();
		this.relationList.add(nameRel);
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
