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
	  
	  //Set up variables
	  String MessageForLeft = "", MessageForRight = "", ID = Integer.toString(this.id);
	  String[] StrArrayFromLeft, StrArrayFromRight;
	  int phase = 0;

	  //First round to get started
	  readFromMaster();

	  MessageForLeft =  ID + " out " + Integer.toString(1);
	  MessageForRight = MessageForLeft;
	  
	  writeToLeft(MessageForLeft);
	  writeToRight(MessageForRight);
	  writeToMaster("-1");
	  
	  //Until Termination section
	  
	  while(true)
	  {
		  readFromMaster();
		  MessageForLeft = "";
		  MessageForRight = "";
		  StrArrayFromLeft = readFromLeft().split(" ");
		  StrArrayFromRight = readFromRight().split(" ");
		  
		  if(StrArrayFromLeft.length > 1 && StrArrayFromLeft[1].equals("l"))
		  {
			  writeToRight(StrArrayFromLeft[0] + " l ");
			  writeToMaster(StrArrayFromLeft[0]);
			  return;
		  }
		  
		  if(StrArrayFromRight.length > 1 && StrArrayFromRight[1].equals("l"))
		  {
			  writeToLeft(StrArrayFromRight[0] + " l ");
			  writeToMaster(StrArrayFromRight[0]);
			  return;
		  }	  
		  
		  if(StrArrayFromLeft.length > 1 && StrArrayFromLeft[1].equals("out"))
		  {
			  if(Integer.parseInt(StrArrayFromLeft[0]) < this.id && Integer.parseInt(StrArrayFromLeft[2]) > 1)
			  {
				  MessageForRight = StrArrayFromLeft[0] + " out " + (Integer.parseInt(StrArrayFromLeft[2])-1);
			  }
			  else if (Integer.parseInt(StrArrayFromLeft[0]) < this.id && Integer.parseInt(StrArrayFromLeft[2]) == 1)
			  {
				  MessageForLeft = StrArrayFromLeft[0] + " in ";
			  }
			  else if (Integer.parseInt(StrArrayFromLeft[0]) == this.id)
			  {
				  writeToRight(ID + " l ");
				  writeToLeft(ID + " l ");
				  writeToMaster(ID);
				  return;
			  }
		  }
		  
		  if(StrArrayFromRight.length > 1 && StrArrayFromRight[1].equals("out"))
		  {
			  if(Integer.parseInt(StrArrayFromRight[0]) < this.id && Integer.parseInt(StrArrayFromRight[2]) > 1)
			  {
				  MessageForLeft = StrArrayFromRight[0] + " out " + (Integer.parseInt(StrArrayFromRight[2])-1);
			  }
			  else if (Integer.parseInt(StrArrayFromRight[0]) < this.id && Integer.parseInt(StrArrayFromRight[2]) == 1)
			  {
				  MessageForRight = StrArrayFromRight[0] + " in ";
			  }
			  else if (Integer.parseInt(StrArrayFromRight[0]) == this.id)
			  {
				  writeToRight(ID + " l ");
				  writeToLeft(ID + " l ");
				  writeToMaster(ID);
				  return;
			  }
		  }
		  
		  if(StrArrayFromLeft.length > 1 && StrArrayFromLeft[1].equals("in") && Integer.parseInt(StrArrayFromLeft[0]) < this.id)
			  MessageForRight = StrArrayFromLeft[0] + " " + StrArrayFromLeft[1] + " ";
		  
		  if(StrArrayFromRight.length > 1 && StrArrayFromRight[1].equals("in") && Integer.parseInt(StrArrayFromRight[0]) < this.id)
			  MessageForLeft = StrArrayFromRight[0] + " " + StrArrayFromRight[1] + " ";
		  
		  if(StrArrayFromLeft.length > 1 && StrArrayFromRight.length > 1 && StrArrayFromLeft[0].equals(ID) && StrArrayFromRight[0].equals(ID))
		  {
			  phase++;
			  MessageForRight = ID + " out " + Math.pow(2, phase);
			  MessageForLeft = MessageForRight;
		  }
		  
		  writeToRight(MessageForRight);
		  writeToLeft(MessageForLeft);
		  writeToMaster("-1");
		  
	  }
	  
	  
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

