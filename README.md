# algorithms
This repository contains implementations of some common algorithms in Java. 

This is meant to just be practice. The programs assume Integer data structures (i.e graphs are all implemented with Integer vertices)

In each directory contains two .jar files, one containing the core java files (libraries not included), the other suffixed with 'Runnable', which is executable. In addition, each directory has the core files included in them in a folder with the algorithm name where you can view the code.

## Dijkstras shortest path
[Dijkstras shortest path algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) implemented with a custom Minimum Heap/PQ that allows for removal of direct removal of keys:value pairs by key.

To run: 'java -jar dijkstrasRunnable.jar arg0 arg1 arg2...'; where arg0 is a file that contains a graph represented by Integers where each row is an adjacency list:
:  vertex destination:weight destination:weight destination:weight etc.

Arguments beyond the first are verticies for which the user wishes to calculate the shortest path to from vertex 1.

## Strongly connected components

Strongly connected components are defined by a sets of vertices in a directed graph where each vertex can travel to every other vertex in the set. This program calculates the largest strongly connected components in a graph using [kosarju's two pass algorithm](https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm).

Arguments to the program:
:  0 - Directed graph represented by adjacency list where rows are in the format: vertex toVertex toVertex...
:  1 - The minimum Integer value of a vertex
:  2 - The maximum Integer value of a vertex

## Minimum cut

A minimum cut is defined by least amount of crossing edges between two distinct sets of vertices in a graph. This program implements [Karger's minimum cut](https://en.wikipedia.org/wiki/Karger%27s_algorithm). The algorithm involves contraction of random edges between vertices until there are two nodes left.


Arguments to the program:
:  0 - Undirected graph represented by adjacency list where rows are in the format: vertex vertex vertex... the first vertex is the vertex where all other vertices in the row are connected to.
:  1 - The number of times to iterate the algorithm (since the probability of success is relatively low)

## Counting inversions

Counts the number of times inversions occurs in an unsorted array. A divide and conquer algorithm using a mergesort algorithm and counting the number of inversions. 