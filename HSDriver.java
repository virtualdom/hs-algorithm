import hs.HSAlgorithm;
import java.util.Scanner;
import java.io.*;

public class HSDriver {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Correct usage:");
      System.err.println("Usage: java HSDriver <filename for n> <filename for ids>");
      return;
    }

    int leader = -9999;
    int n = 0;
    int [] ids;
    Scanner nScanner;
    Scanner idScanner;

    try {
      nScanner = new Scanner(new File(args[0]));
      idScanner = new Scanner(new File(args[1]));
    } catch (FileNotFoundException e) {
      System.err.println("Input file(s) not found!");
      return;
    }

    if (nScanner.hasNextInt()) n = nScanner.nextInt();
    else {
      System.err.println(args[0] + " should only contain the value n, the number of processes.");
      return;
    }

    ids = new int [n];

    for (int i = 0; i < n; i++) {
      if (idScanner.hasNextInt()) ids[i] = idScanner.nextInt();
      else {
        System.err.println(args[1] + " should contain " + n + " (specified in " + args[0] + ") contiguous whitespace-separated integer IDs");
        return;
      }
    }

    HSAlgorithm hs = new HSAlgorithm(n, ids);
    hs.start();
    // leader = hs.getLeaderId();
    // System.out.println(leader + " is the elected leader.");
  }
}
