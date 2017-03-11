
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SuffixTreeMain {
	
	public static void main(String[] args) throws IOException{
		STree pohon = new STree();

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("input file:");
		String path = bf.readLine();

		BufferedReader bff = new BufferedReader(new FileReader(path));

		System.out.println("Processing..");
		String line = bff.readLine();
		while(line != null && line.length() > 0) {
			String[] sequences = line.split(" ");

			pohon.addSequence(sequences);

			line = bff.readLine();
		}

		bff.close();

		// String sequences6[] = {"a", "b", "c"};
		// pohon.addSequence(sequences6);
		// String sequencese[] = {"a", "b", "d", "e"};
		// pohon.addSequence(sequencese);

		pohon.printTree();
	}

}