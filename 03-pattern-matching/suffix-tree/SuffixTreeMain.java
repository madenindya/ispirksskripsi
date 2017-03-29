
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SuffixTreeMain {
	
	public static void main(String[] args) throws IOException, Exception {
		STree pohon = new STree();

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("input file:");
		String path = bf.readLine();
		System.out.println("method:");
		System.out.println("1. Pattern in between relation");
		String pilihan = bf.readLine();

		System.out.println("Processing..");
		BufferedReader bff = new BufferedReader(new FileReader(path));
		String line = bff.readLine();
		int count = 0;
		while(line != null && line.length() > 0) {

			String[] sequences = line.split(" ");

			if (pilihan.equals("1")) {
				Pair idx = getIndexInBetween(sequences);
			}

			if (sequences.lenght > 0) {
				pohon.addSequence(sequences);		
			}
	
			line = bff.readLine();
			
			if (count % 10000 == 0) {
				System.out.println(count);
			}
			count++;
		}
		bff.close();
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
				}

				if (start) {
					pair.end = i;
					break;
				} else {
					start = true;
					prevRel = rel;
					pair.begin = i;
				}
			}
		}

		return pair;
	}

	// ambil n previous
	private static String[] getIndexwPrev(String[] sequences, int n) {
		Pair pair = getIndexInBetween(sequences);
		if (pair.begin == -1 || pair.end == -1) {
			return pair; // kalo salah dibiarin aja. bakal ke-filter nanti
		}
		pair.begin = ((pair.begin - n) > -1) ? pair.begin - n : 0;
		return pair
	}

	// ambil n follower
	private static String[] getIndexwFollow(String[] sequences, int n) {
		Pair pair = getIndexInBetween(sequences);
		if (pair.begin == -1 || pair.end == -1) {
			return pair; // kalo salah dibiarin aja. bakal ke-filter nanti
		}
		pair.end = ((pair.end + n) < sequences.length) ? pair.end + n : sequences.length - 1;
		return pair
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
		String[] resultSequences = (len > 1) ? new String[len] : new String[0];

		int j = 0; 
		for (int i = pair.begin; i < pair.end; i++) {
			resultSequences[j] = sequences[i];
			j++;
		}
		return resultSequences;
	}


	class Pair {

		int begin;
		int end;

		public Pair (b, e) {
			this.begin = b;
			this.e = e;
		}
	}

}