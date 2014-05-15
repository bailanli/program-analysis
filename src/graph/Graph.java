package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Graph {
	protected int V;
	protected int E;
	protected ArrayList<Integer>[] adj;
	protected ArrayList<Integer>[] pred;

	public ArrayList<Integer> getPred(int v) {
		if (v < 0 || v >= V)
			throw new IndexOutOfBoundsException("vertex " + v
					+ " is not between 0 and " + (V - 1));

		return pred[v];
	}

	public ArrayList<Integer> getAdj(int v) {
		if (v < 0 || v >= V)
			throw new IndexOutOfBoundsException("vertex " + v
					+ " is not between 0 and " + (V - 1));

		return adj[v];
	}

	@SuppressWarnings("unchecked")
	public Graph(int V) {
		if (V < 0)
			throw new IllegalArgumentException(
					"Number of vertices in a Digraph must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = (ArrayList<Integer>[]) new ArrayList[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new ArrayList<Integer>();
		}
		pred = (ArrayList<Integer>[]) new ArrayList[V];
		for (int v = 0; v < V; v++) {
			pred[v] = new ArrayList<Integer>();
		}
	}

	@SuppressWarnings("unchecked")
	public Graph(File file) {
		try {
			Scanner scanner = new Scanner(file);
			this.V = scanner.nextInt();
			if (V < 0)
				throw new IllegalArgumentException(
						"Number of vertices in a Digraph must be nonnegative");
			adj = (ArrayList<Integer>[]) new ArrayList[V];
			for (int v = 0; v < V; v++) {
				adj[v] = new ArrayList<Integer>();
			}
			pred = (ArrayList<Integer>[]) new ArrayList[V];
			for (int v = 0; v < V; v++) {
				pred[v] = new ArrayList<Integer>();
			}
			int E = scanner.nextInt();
			if (E < 0)
				throw new IllegalArgumentException(
						"Number of edges in a Digraph must be nonnegative");
			for (int i = 0; i < E; i++) {
				int v = scanner.nextInt();
				int w = scanner.nextInt();
				addEdge(v, w);
			}
		} catch (NoSuchElementException e) {
			throw new InputMismatchException(
					"Invalid input format in Graph constructor");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	public void addEdge(int v, int w) {
		if (v < 0 || v >= V)
			throw new IndexOutOfBoundsException("vertex " + v
					+ " is not between 0 and " + (V - 1));
		if (w < 0 || w >= V)
			throw new IndexOutOfBoundsException("vertex " + w
					+ " is not between 0 and " + (V - 1));
		adj[v].add(w);
		pred[w].add(v);
		E++;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		String NEWLINE = System.getProperty("line.separator");
		s.append(V + " vertices, " + E + " edges " + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(String.format("%d: ", v));
			for (int w : adj[v]) {
				s.append(String.format("%d ", w));
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	public static void main(String[] args) {
		Graph G = new Graph(new File("test/testG.txt"));
		System.out.println(G);
	}
}
