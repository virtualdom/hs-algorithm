# HS Algorithm
A small simulation of the synchronous HS algorithm written using Java threads by Kevin Greenwald,
Dominic Joseph, and Sumeet Vandakudari.

## Installation
Simply clone the directory and compile using `javac`.
```
git clone git@github.com:virtualdom/hs-algorithm.git
cd hs-algorithm
javac hs/HSAlgorithm.java HSDriver.java
```

## Usage
To run the HS algorithm with `n` processes, create a file that contains only the value of `n`
(one has already been provided in the repository as `n.txt`). Create another file that contains
a whitespace-separated list of **nonnegative** integers of size `n` — these will be the IDs for
each process (one has already been provided in the repository as `ids.txt`). To use the files with
our implementation (named `n.txt` and `ids.txt`, respectively), execute the following:
```
java HSDriver n.txt ids.txt
```
