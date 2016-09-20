package hs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class HSAlgorithm {
  int n = -1;
  int [] ids;
  int [] leader;

  ArrayList<ArrayBlockingQueue<String>> toLeft;
  ArrayList<ArrayBlockingQueue<String>> toRight;
  ArrayList<ArrayBlockingQueue<String>> toMaster;
  ArrayList<ArrayBlockingQueue<String>> toProcess;

  public class Process extends Thread {
    int id = -9999;
    int left = -1;
    int right = -1;
    int index = -1;

    public Process (int index, int id, int left, int right) {
      this.id = id;
      this.left = left;
      this.right = right;
      this.index = index;
    }

    private void writeToLeft (String message) {
      try {
        toLeft.get(left).put(message);
      } catch (InterruptedException e) {
        System.err.println("Write from process " + index
          + " to process " + left + " failed.");
        System.exit(0);
      }
    }

    private void writeToRight (String message) {
      try {
        toRight.get(right).put(message);
      } catch (InterruptedException e) {
        System.err.println("Write from process " + index
          + " to process " + right + " failed.");
        System.exit(0);
      }
    }

    private void writeToMaster (String message) {
      try {
        toMaster.get(index).put(message);
      } catch (InterruptedException e) {
        System.err.println("Write from process " + index
          + " to master failed.");
        System.exit(0);
      }
    }

    private String readFromLeft () {
      try {
        return toRight.get(index).take();
      } catch (InterruptedException e) {
        System.err.println("Read by process " + index
          + " from process " + left + " failed.");
        System.exit(0);
        return "";
      }
    }

    private String readFromRight () {
      try {
        return toLeft.get(index).take();
      } catch (InterruptedException e) {
        System.err.println("Read by process " + index
          + " from process " + right + " failed.");
        System.exit(0);
        return "";
      }
    }

    private String readFromMaster () {
      try {
        return toProcess.get(index).take();
      } catch (InterruptedException e) {
        System.err.println("Read by process " + index
          + " from process master failed.");
        System.exit(0);
        return "";
      }
    }

    @Override
    public void run () {
      // process code goes here!
      // use (readFrom/writeTo)Left,
      // (readFrom/writeTo)Right, and
      // (readFrom/writeTo)Master functions
      // to do things!
      // i.e. wait for the starting signal
      // String startingSignal = readFromMaster();
    }
  }

  public HSAlgorithm (int n, int [] ids) {
    this.n = n;
    this.ids = ids;
    this.leader = new int [n];

    toLeft = new ArrayList<ArrayBlockingQueue<String>>(n);
    toRight = new ArrayList<ArrayBlockingQueue<String>>(n);
    toMaster = new ArrayList<ArrayBlockingQueue<String>>(n);
    toProcess = new ArrayList<ArrayBlockingQueue<String>>(n);

    for (int i = 0; i < n; i++) {
      leader[i] = -9999;

      toLeft.add(new ArrayBlockingQueue<String>(1));
      toRight.add(new ArrayBlockingQueue<String>(1));
      toMaster.add(new ArrayBlockingQueue<String>(1));
      toProcess.add(new ArrayBlockingQueue<String>(1));
    }
  }

  public void start () {
    Thread [] processes = new Thread [n];
    int left, right;

    for (int i = 0; i < n; i++) {
      left = i == 0 ? n - 1 : i - 1;
      right = i == n - 1 ? 0 : i + 1;
      processes[i] = new Process(i, ids[i], left, right);
      processes[i].start();
    }

    // master code goes here!
    // at this point, processes all exist!

    // to read from process i, use
    // toMaster.get(i).take(); // returns a string
    // to write to process i, use
    // toProcess.get(i).put(data); // for some string `data` parameter
  }

  public int getLeaderId () {
    for (int i = 0; i < n - 1; i++) {
      if (leader[i] != leader[i + 1]) {
        System.err.println("Multiple leaders elected! HS algorithm failed.");
        return -9999;
      }
    }

    return leader[0];
  }
}

