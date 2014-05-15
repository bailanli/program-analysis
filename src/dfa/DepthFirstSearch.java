package dfa;

import java.util.ArrayList;
import java.util.Collections;

import graph.Graph;

public class DepthFirstSearch {
	private boolean[] marked;
	private int[] pre;
	private int[] post;
	private ArrayList<Integer> preorder;
	private ArrayList<Integer> postorder;
	private int preCounter;
	private int postCounter;

	public DepthFirstSearch(Graph G) {
		pre = new int[G.V()];
		post = new int[G.V()];
		postorder = new ArrayList<Integer>();
		preorder = new ArrayList<Integer>();
		marked = new boolean[G.V()];

		for (int v = 0; v < G.V(); v++)
			if (!marked[v])
				dfs(G, v);
	}

	private void dfs(Graph G, int v) {
		marked[v] = true;
		pre[v] = preCounter++;
		preorder.add(v);
		for (int w : G.getAdj(v)) {
			if (!marked[w]) {
				dfs(G, w);
			}
		}
		postorder.add(v);
		post[v] = postCounter++;
	}

	public int[] getPost() {
		return post;
	}

	public int[] getPre() {
		return pre;
	}

	public ArrayList<Integer> getTopologicalOrder() {
		ArrayList<Integer> reverse = new ArrayList<Integer>(postorder);
		Collections.reverse(reverse);
		return reverse;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		String NEWLINE = System.getProperty("line.separator");
		s.append("Preorder: ");
		for (int v : getPre()) {
			s.append(v + " ");
		}
		s.append(NEWLINE);
		s.append("Postorder: ");
		for (int v : getPost()) {
			s.append(v + " ");
		}
		s.append(NEWLINE);
		return s.toString();
	}
}
