import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class SuffixTreeMain {
	
	public static void main(String[] args) throws IOException, Exception {
		STree pohon = new STree();

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		HashMap<String, List<String>> index = new HashMap<>();

		

		System.out.println("input folder (ex: /home/madenindya):");
		String path = bf.readLine();

		System.out.println("method:");
		System.out.println("1. Pattern in between relation");
		String pilihan = bf.readLine();

		System.out.println("Processing..");
		File dir = new File(path);
		for (File afile : dir.listFiles()) {
			String filepath = dir + "/" + afile.getName();
			System.out.println("Read " + filepath);

			BufferedReader bff = new BufferedReader(new FileReader(filepath));
			String line = bff.readLine();
			int count = 0;
			while(line != null && line.length() > 0) {
				count++;
				String[] sequences = line.split(" ");

				Pair idx = new Pair(-1, -1);
				if (pilihan.equals("1")) {
					idx = getIndexInBetween(sequences);
					sequences = getFilteredSequences(sequences, idx);
				}

				if (sequences.length > 0) {

					System.out.println("CHECK => " + idx.key);
					String sentence = buildSentence(sequences);
					if (index.containsKey(idx.key)) {
						List<String> lama = index.get(idx.key);
						if (lama.contains(sentence)) {

							// update line, then next
							line = bff.readLine();
							continue;	// kalo udah ada, skip
						} else {
							index.get(idx.key).add(sentence);
						}
					} else {
						List<String> baru = new ArrayList<>();
						baru.add(sentence);
						index.put(idx.key, baru);
					}

					System.out.println("PASS => " + idx.key);
					// add new sequence to Tree
					pohon.addSequence(sequences);		
				}
		
				line = bff.readLine();
				
				if (count % 10000 == 0) {
					System.out.println(count);
				}
			}
			bff.close();
		}
		System.out.println("Done :D \n");


		String c = "";
		while (!c.equals("9")) {
			System.out.println("What's next?");
			System.out.println("1. Print Tree");
			System.out.println("2. Find Pattern");
			System.out.println("9. Exit");

			c = bf.readLine();

			if (c.equals("9")) break;

			System.out.println("output file:");
			String opath = bf.readLine();

			if (c.equals("1")) {
				pohon.printTree(opath);
			} else if (c.equals("2")) {

				System.out.println("min occurance:");
				int min = Integer.parseInt(bf.readLine());
				pohon.getPattern(min, opath);
			} 

		}
	}

	// ambil yang in-between ajah
	private static Pair getIndexInBetween(String[] sequences) {
		Pair pair = new Pair(-1, -1);

		boolean start = false;

		String prevRel = "";
		for (int i = 0; i < sequences.length; i++)  {
			
			String rel = checkRel(sequences[i]);
			if (rel.length() > 0) {

				// suka ada yg berulang -> gak boleh hypo-hypo atau hype-hype
				if (start && prevRel.equals(rel)) {
					// format ulang
					pair.begin = i;
					pair.key = sequences[i];		// new
				}

				if (start) {
					pair.end = i + 1;
					pair.key += sequences[i];		// update
					break;
				} else {
					start = true;
					prevRel = rel;
					pair.begin = i;
					pair.key = sequences[i];		// new 
				}
			}
		}

		return pair;
	}

	// ambil n previous
	private static Pair getIndexwPrev(String[] sequences, int n) {
		Pair pair = getIndexInBetween(sequences);
		if (pair.begin == -1 || pair.end == -1) {
			return pair; // kalo salah dibiarin aja. bakal ke-filter nanti
		}
		pair.begin = ((pair.begin - n) > -1) ? pair.begin - n : 0;
		return pair;
	}

	// ambil n follower
	private static Pair getIndexwFollow(String[] sequences, int n) {
		Pair pair = getIndexInBetween(sequences);
		if (pair.begin == -1 || pair.end == -1) {
			return pair; // kalo salah dibiarin aja. bakal ke-filter nanti
		}
		pair.end = ((pair.end + n) < sequences.length) ? pair.end + n : sequences.length - 1;
		return pair;
	}

	// check if relation here
	private static String checkRel(String token) {
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

	// ambil array sequence berdasarkan Pair begin, end
	private static String[] getFilteredSequences(String[] sequences, Pair pair) {
		int len = pair.end - pair.begin;
		String[] resultSequences = (len > 2) ? new String[len] : new String[0];

		int j = 0; 
		if (len > 2) {
			for (int i = pair.begin; i < pair.end; i++) {
				resultSequences[j] = sequences[i];
				j++;
			}
		}
		return resultSequences;
	}

	private static String buildSentence(String[] sequences) {
		String result = "";
		if (sequences.length > 0) {
			result = sequences[0];

			for (int i = 1; i < sequences.length; i++) {
				result += " " + sequences[i];
			}
		}
		return result;
	}


	static class Pair {

		int begin;
		int end;
		String key;

		public Pair (int b, int e) {
			this.begin = b;
			this.end = e;
		}
	}

}
