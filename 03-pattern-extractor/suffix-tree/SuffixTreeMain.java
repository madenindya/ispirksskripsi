import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.io.*;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class SuffixTreeMain {

	static STree pohon;
	static HashMap<String, List<String>> index;
	
	public static void main(String[] args) throws IOException, Exception {
		
		// initialize new variables
		pohon = new STree();
		index = new HashMap<>();

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

		// costumize input
		System.out.println("input folder (ex: /home/madenindya):");
		String path = bf.readLine();

		System.out.println("method:");
		System.out.println("1. Pattern in between relation");
		System.out.println("2. Pattern with n word before");
		System.out.println("3. Pattern with n word after");
		System.out.println("4. Unique pattern");
		String pilihan = bf.readLine();

		if (!pilihan.equals("4")) {
			int n = 0;
			if (pilihan.equals("2") || pilihan.equals("3")) {
				System.out.println("Jumlah n:");
				n  = Integer.parseInt(bf.readLine());
			}

			// build tree
			findPatternTree(pilihan, path, n);

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
		} else {

			HashMap<String, String> unik = new HashMap<>();
			Map<String, List<String>> tmpunik = new HashMap<>();

			// coba relasi in-between
			System.out.println("Find in-between pattern");
			findPatternTree("1", path, 0);
			pohon.getPattern(3, "tmpuniqpattern");
			BufferedReader bff = new BufferedReader(new FileReader("tmpuniqpattern"));
			String line = bff.readLine();
			while(line != null && line.length() > 0) {
				String unikStr = getUnikStr(line);
				if (tmpunik.containsKey(unikStr)) {
					// add to array
					tmpunik.get(unikStr).add(line);
				} else {
					List<String> newArr = new ArrayList<>();
					newArr.add(line);
					tmpunik.put(unikStr, newArr);
				}
				line = bff.readLine();
			}
			bff.close();
			List<String> removeKey = new ArrayList<>();
			for (String key : tmpunik.keySet()) {
				if (tmpunik.get(key).size() == 1) {
					unik.put(key, tmpunik.get(key).get(0));
					removeKey.add(key);
				}
			}
			for (String rk : removeKey) {
				tmpunik.remove(rk);
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter("tmp1"));
			for (String key : unik.keySet()) {
				bw.write(unik.get(key) + "\n");
			}
			bw.write("\n\n\n");
			for (String key : tmpunik.keySet()) {
				bw.write(key + "\n");	
			}
			bw.close();


			// --- 1-before-after pattern
			if (tmpunik.size() > 0) {
				System.out.println("Find 1-before-after pattern");
				HashMap<String, HashMap<String, List<String>>> tmpcheck;
				// coba 1-before
				System.out.println("Find 1-before pattern");
				tmpcheck = getTmpCheck(path, "2", 1, tmpunik);
				for (String s1 : tmpcheck.keySet()) {
					for (String s2 : tmpcheck.get(s1).keySet()) {
						List<String> now = tmpcheck.get(s1).get(s2);
						if (now.size() == 1) {
							unik.put(s2, now.get(0));
						} else {
							tmpunik.put(s2, now);
						}
					}
				}
				// coba 1-after
				System.out.println("Find 2-after pattern");
				tmpcheck = getTmpCheck(path, "3", 1, tmpunik);
				for (String s1 : tmpcheck.keySet()) {
					for (String s2 : tmpcheck.get(s1).keySet()) {
						List<String> now = tmpcheck.get(s1).get(s2);
						if (now.size() == 1) {
							unik.put(s2, now.get(0));
						} else {
							tmpunik.put(s2, now);
						}
					}
				}
				//// print print
				System.out.println("Delete unecessary");
				for (String s1 : tmpcheck.keySet()) {
					tmpunik.remove(s1);
				}
				System.out.println("Star print");
				bw = new BufferedWriter(new FileWriter("tmp2"));
				for (String key : unik.keySet()) {
					bw.write(unik.get(key) + "\n");
				}
				bw.write("\n\n\n");
				for (String key : tmpunik.keySet()) {
					bw.write(key + "\n");	
				}
				bw.close();				
			}

			// --- 2-before-after pattern
			if (tmpunik.size() > 0) {
				System.out.println("Find 2-before-after pattern");
				HashMap<String, HashMap<String, List<String>>> tmpcheck;
				// coba 2-before
				System.out.println("Find 2-before pattern");
				tmpcheck = getTmpCheck(path, "2", 2, tmpunik);
				for (String s1 : tmpcheck.keySet()) {
					for (String s2 : tmpcheck.get(s1).keySet()) {
						List<String> now = tmpcheck.get(s1).get(s2);
						if (now.size() == 1) {
							unik.put(s2, now.get(0));
						} else {
							tmpunik.put(s2, now);
						}
					}
				}
				// coba 2-after
				System.out.println("Find 2-after pattern");
				tmpcheck = getTmpCheck(path, "3", 2, tmpunik);
				for (String s1 : tmpcheck.keySet()) {
					for (String s2 : tmpcheck.get(s1).keySet()) {
						List<String> now = tmpcheck.get(s1).get(s2);
						if (now.size() == 1) {
							unik.put(s2, now.get(0));
						} else {
							tmpunik.put(s2, now);
						}
					}
				}
				//// print print
				System.out.println("Delete unecessary");
				for (String s1 : tmpcheck.keySet()) {
					tmpunik.remove(s1);
				}
				System.out.println("Star print");
				bw = new BufferedWriter(new FileWriter("tmp3"));
				for (String key : unik.keySet()) {
					bw.write(unik.get(key) + "\n");
				}
				bw.write("\n\n\n");
				for (String key : tmpunik.keySet()) {
					bw.write(key + "\n");	
				}
				bw.close();				
			}
		}
		// end
	}

	private static HashMap<String, HashMap<String, List<String>>> getTmpCheck(
		String path, String pilihan, int n, Map<String, List<String>> tmpunik) throws Exception {
		HashMap<String, HashMap<String, List<String>>> tmpcheck  = new HashMap<>();
		System.out.println("Find " + n + "-" + pilihan + " pattern");
		pohon = new STree();
		index = new HashMap<>();
		findPatternTree(pilihan, path, n);
		pohon.getPattern(3, "tmpuniqpattern");
		BufferedReader bff = new BufferedReader(new FileReader("tmpuniqpattern"));
		String line = bff.readLine();
		while(line != null && line.length() > 0) {
			String unikStr = getUnikStr(line);
			for (String key : tmpunik.keySet()) {
				if (unikStr.contains(key)) {
					if (tmpcheck.containsKey(key)) {
						if (tmpcheck.get(key).containsKey(unikStr)) {
							tmpcheck.get(key).get(unikStr).add(line);
						} else {
							List<String> lbaru = new ArrayList<>();
							lbaru.add(line);
							tmpcheck.get(key).put(unikStr, lbaru);
						}
					} else {
						List<String> lbaru = new ArrayList<>();
						lbaru.add(line);
						HashMap<String, List<String>> hbaru = new HashMap<>();
						hbaru.put(unikStr, lbaru);
						tmpcheck.put(key, hbaru);
					}
				} 
			}
			line = bff.readLine();	
		}
		bff.close();
		return tmpcheck;
	}

	private static String getUnikStr(String line) {
		String unikStr = "";
		String[] tokens = line.split(" ");
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].length() < 1) {
				continue;
			}
			if (tokens[i].equals("--")) {
				break;
			}
			if (tokens[i].equals("<hypernym>") || tokens[i].equals("<hyponym>")) {
				unikStr += " <relation>";
			} else {
				unikStr += " " + tokens[i];
			}			
		}
		return unikStr;
	}


///////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void findPatternTree(String pilihan, String path, int n) throws Exception {

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
				String[] sequences = line.split(" ");

				// FILTER: ambil index berdasarkan kemunculan kata berelasi
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

		// FILTER: if no word between relation, reset
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

	// check what relation here
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

	// check if relation here
	private static boolean hasRel(String token) {
		if (token.contains("hypernym") || token.contains("hyponym") ||
			token.contains("meronym") || token.contains("holonym")) {
			return true;
		}
		return false;
	}

	// ambil array sequence berdasarkan Pair begin, end
	private static String[] getFilteredSequences(String[] sequences, Pair pair) {
		int len = pair.end - pair.begin;

		// FILTER: yang cuma mengandung 1 karakter, abaikan
		int begin = -1;
		int end = -1;
		ArrayList<String> tmpResult = new ArrayList<String>();		
		if (len > 2) {
			for (int i = pair.begin; i < pair.end; i++) {
				if (sequences[i].length() <= 1) {
					continue;
				} 
				if (hasRel(sequences[i])) {
					if (begin != -1) {
						end = tmpResult.size();
					} else {
						begin = tmpResult.size();
					}
				}
				tmpResult.add(sequences[i]);		
			}
		}

		// FILTER: yang cuma mengandung <hyp> <hyp> abaikan
		if ((end - begin) <= 1) {
			return new String[0];
		}

		String[] resultSequences = new String[tmpResult.size()];
		for (int i = 0; i < tmpResult.size(); i++) {
			resultSequences[i] = tmpResult.get(i);
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
