/**
 ** Java Program to implement Longest Common Subsequence Algorithm
 **/

 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

 

/** Class  LongestCommonSubsequence **/
public class  LongestCommonSubsequences {    

    /** function lcs **/
    public String lcs(String str1, String str2) {

        String[] lstr1 = str1.split(" ");
        String[] lstr2 = str2.split(" ");

        int l1 = lstr1.length;
        int l2 = lstr2.length;
        int[][] arr = new int[l1 + 1][l2 + 1];
        for (int i = l1 - 1; i >= 0; i--) {

            for (int j = l2 - 1; j >= 0; j--) {

                if (lstr1[i] == lstr2[j])
                    arr[i][j] = arr[i + 1][j + 1] + 1;
                else 
                    arr[i][j] = Math.max(arr[i + 1][j], arr[i][j + 1]);
            }
        }

        int i = 0, j = 0;
        String sb = "";
        for (int a = 0; a < l1; a++) {
            System.out.println(a + " -> " + lstr1[a]);
        }

        while (i < l1 && j < l2) {

            if (lstr1[i].length() == 0) {
                i++; continue;
            }
            if (lstr1[j].length() == 0) {
                j++; continue;
            }

            if (lstr1[i].equals(lstr2[j])) {
                sb += lstr1[i] + " ";
                i++;
                j++;
            }
            else if (arr[i + 1][j] >= arr[i][j + 1]) 
                i++;
            else
                j++;

        }

        return sb;
    }

 
    /** Main Function **/
    public static void main(String[] args) throws IOException {    

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Longest Common Subsequence Algorithm Test\n");
        
        System.out.println("\nEnter string 1");
        String str1 = br.readLine();

        System.out.println("\nEnter string 2");
        String str2 = br.readLine();

        LongestCommonSubsequences obj = new LongestCommonSubsequences(); 
        
        String result = obj.lcs(str1, str2);

        System.out.println("\nLongest Common Subsequence : "+ result);
    }
}
