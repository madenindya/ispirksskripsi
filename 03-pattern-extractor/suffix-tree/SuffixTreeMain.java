import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class SuffixTreeMain {
	
	public static void main(String[] args) throws IOException, Exception {
		
		// variables
		STree pohon = new STree();
		HashMap<String, List<String>> index = new HashMap<>();

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

		// costumize input
		System.out.println("input folder (ex: /home/madenindya):");
		String path = bf.readLine();

		System.out.println("method:");
		System.out.println("1. Pattern in between relation");
		System.out.println("2. Pattern with n word before");
		System.out.println("3. Pattern with n word after");
		String pilihan = bf.readLine();

		int n = 0;
		if (pilihan.equals("2") || pilihan.equals("3")) {
			System.out.println("Jumlah n:");
			n  = Integer.parseInt(bf.readLine());
		}

		System.out.println("Building tree..");

		// process all data
		File dir = new File(path);
		for (File afile : dir.listFiles()) {
			String filepath = dir + "/" + afile.getName();
			System.out.println("Read " + filepath);

			BufferedReader bff = new BufferedReader(new FileReader(filepath));
			String line = "";
			int count = 0;
			do {
				line = bff.readLine();
				if (line == null || line.length() <= 0) {
					continue;
				}

				count++;
				if (count % 1000 == 0) {
					System.out.println(count);
				}

				// ambil index berdasarkan kemunculan kata berelasi
				String[] sequences = line.split(" ");
				Pair idx = new Pair(-1, -1);
				if (pilihan.equals("1")) {
					idx = getIndexInBetween(sequences);
				} else if (pilihan.equals("2")) {
					idx = getIndexwPrev(sequences, n);					
				} else if (pilihan.equals("3")) {
					idx = getIndexwFollow(sequences, n);
				}
				sequences = getFilteredSequences(sequences, idx);

			    // kalo gada isinya, skip
				if (sequences.length <= 0) {
					continue;
				}

				// FILTER: check udah pernah ada di tree belom
				String sentence = buildSentence(sequences);
				if (index.containsKey(idx.key)) {
					List<String> lama = index.get(idx.key);
					if (lama.contains(sentence)) {
						// kalo udah ada, skip
						continue;
					} else {
						index.get(idx.key).add(sentence);
					}
				} else {
					List<String> baru = new ArrayList<>();
					baru.add(sentence);
					index.put(idx.key, baru);
				}

				// add new sequence to Tree
				pohon.addSequence(sequences);		

			} while (line != null && line.length() > 0);

			bff.close();				
		}
		System.out.println("Done :D \n");


		// menu pilihan
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
				System.out.println("min pattern occurance:");
				int min = Integer.parseInt(bf.readLine());
				pohon.getPattern(min, opath);
			} 
		}

		// end
	}




//////////////////////////////////////////////////////////////////////////////////////////////////
////	PRIVATE METHOD																			//
//////////////////////////////////////////////////////////////////////////////////////////////////
	
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

		// if no word between relation, reset
		if (pair.end - pair.begin <= 2) {
			pair.begin = -1;
			pair.end = -1;
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
		pair.end = ((pair.end + n) < sequences.length) ? pair.end + n : sequences.length;
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
