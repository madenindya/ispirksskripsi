
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
		// while (count < 5) {
		// 	String line = "tujuan <hyponym>latihan<hyponym> ini adalah untuk meningkatkan kemampuan satuan dan <hypernym>membentuk<hypernym> prajurit raider yang profesional, handal dan tangguh.";
		// 	if (count == 3) {
		// 		line = "tujuan <hyponym>latihan<hyponym> ini adalah untuk meningkatkan kemampuan satuan atau <hypernym>membentuk<hypernym> prajurit raider yang profesional, handal dan tangguh.";	
		// 	}

			String[] sequences = line.split(" ");

			if (pilihan.equals("1")) {
				sequences = getFormatSequence1(sequences);
			}

			pohon.addSequence(sequences);

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


	private static String[] getFormatSequence1(String[] sequences) {
		String hasil = "";

		boolean start = false;

		for (int i = 0; i < sequences.length; i++)  {
			
			if (checkRel(sequences[i]).length() > 0) {
				hasil += " " + sequences[i];
				if (start) {
					break;
				} else {
					start = true;
				}
			} else {
				if (start) {
					hasil += " " + sequences[i];
				}				
			}
		}

		// System.out.println(hasil);
		return hasil.substring(1).split(" ");
	}

	private static String checkRel(String token) {
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

}