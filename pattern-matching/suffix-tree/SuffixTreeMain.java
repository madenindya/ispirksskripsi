

public class SuffixTreeMain {
	
	public static void main(String[] args) {
		STree pohon = new STree();

		String sequences[] = {"<hyponym>ikan<hyponym>", "adalah", "<hypernym>binatang<hypernym>", "air"};
		pohon.addSequence(sequences);
		String sequences3[] = {"<hyponym>kelinici<hyponym>", "adalah", "<hypernym>hewan<hypernym>", "darat"};
		pohon.addSequence(sequences3);
		String sequences2[] = {"<hyponym>semut<hyponym>", "adalah", "sbuah", "<hypernym>animalia<hypernym>"};
		pohon.addSequence(sequences2);
		String sequences5[] = {"seekor", "<hyponym>semut<hyponym>", "adalah", "sbuah", "<hypernym>animalia<hypernym>"};
		pohon.addSequence(sequences5);

		String sequences6[] = {"a", "b", "c"};
		pohon.addSequence(sequences6);
		String sequencese[] = {"a", "b", "d", "e"};
		pohon.addSequence(sequencese);

		pohon.printTree();
	}

}