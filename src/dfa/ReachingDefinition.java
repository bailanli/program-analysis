package dfa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReachingDefinition {
	private Set<Definition>[] in;
	private Set<Definition>[] out;
	private Set<Definition>[] gen;
	private Set<String>[] kill;
	private int count;
	private ControlFlowGraph cfg;
	private int iteration;
	private ArrayList<Integer> topo;

	@SuppressWarnings("unchecked")
	public ReachingDefinition(ControlFlowGraph cfg) {
		count = cfg.V();
		this.cfg = cfg;
		DepthFirstSearch dfs = new DepthFirstSearch(cfg);
		topo = dfs.getTopologicalOrder();
		in = new Set[count];
		gen = new Set[count];
		kill = new Set[count];
		out = new Set[count];
		for (int i = 0; i < count; i++) {
			in[i] = new HashSet<Definition>();
			gen[i] = new HashSet<Definition>();
			kill[i] = new HashSet<String>();
			for (String name : cfg.getDef(i)) {
				gen[i].add(new Definition(name, i));
				kill[i].add(name);
			}
			out[i] = new HashSet<Definition>();
		}
		iteration = 0;
		iterate(cfg);
		System.out.println(iteration);
	}

	public int count() {
		return count;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		String NEWLINE = System.getProperty("line.separator");
		for (int v = 0; v < count; v++) {
			s.append(cfg.getMapping().getSourceFromVertex(v) + ": " + NEWLINE);
			s.append("In: ");
			for (Definition d : in[v]) {
				s.append(d.toString() + " ");
			}
			s.append(NEWLINE);
			s.append("Out: ");
			for (Definition d : out[v]) {
				s.append(d.toString() + " ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	public String DUChainDotty() {
		TreeSet<String> set = new TreeSet<String>();
		for (int v = 0; v < cfg.V(); v++) {
			for (String name : cfg.getUse(v)) {
				for (Definition d : in[v])
					if (d.getVariable().equals(name)) {
						set.add(String.format("  \"%s, %s\" -> %s;", d
								.getVariable(), cfg.getMapping()
								.getSourceStringFromVertex(d.getPosition()),
								cfg.getMapping().getSourceStringFromVertex(v)));
					}
			}
		}
		StringBuilder s = new StringBuilder();
		String NEWLINE = System.getProperty("line.separator");
		s.append("digraph control_flow_graph {" + NEWLINE);
		s.append("  node [shape = rectangle];" + NEWLINE);
		for (String ss : set) {
			s.append(ss + NEWLINE);
		}
		s.append("}" + NEWLINE);
		return s.toString();
	}

	private boolean isOutNode(int v) {
		int source = cfg.getMapping().getSourceFromVertex(v);
		for (int w : cfg.getAdj(v)) {
			if (source != cfg.getMapping().getSourceFromVertex(w))
				return true;
		}
		return false;
	}

	public String RDString() {
		TreeMap<Integer, Set<String>> rd = new TreeMap<Integer, Set<String>>();
		for (int v = 0; v < cfg.V(); v++) {
			if (v == Mapping.VERTEX_ENTRY)
				continue;
			if (isOutNode(v)) {
				int source = cfg.getMapping().getSourceFromVertex(v);

				if (!rd.containsKey(source)) {
					TreeSet<String> set = new TreeSet<String>();
					rd.put(source, set);
				}
				for (Definition d : out[v]) {
					rd.get(source).add(
							String.format(
									"<%s, %s>",
									d.getVariable(),
									cfg.getMapping().getSourceStringFromVertex(
											d.getPosition())));
				}
			}
		}
		StringBuilder s = new StringBuilder();
		String NEWLINE = System.getProperty("line.separator");
		for (Integer source : rd.keySet()) {
			s.append(source);
			for (String def : rd.get(source)) {
				s.append("\t" + def);
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	public void iterate(ControlFlowGraph G) {
		boolean changed = false;
		iteration++;
		for (Integer v : topo) {
			HashSet<Definition> tempin = new HashSet<Definition>();
			for (Integer w : G.getPred(v))
				tempin.addAll(out[w]);
			if (!in[v].equals(tempin)) {
				in[v] = tempin;
				changed = true;
			}
			HashSet<Definition> tempout = new HashSet<Definition>();
			tempout.addAll(in[v]);
			HashSet<Definition> temptempout = new HashSet<Definition>(tempout);
			for (Definition d : temptempout) {
				if (kill[v].contains(d.getVariable())) {
					tempout.remove(d);
				}
			}
			tempout.addAll(gen[v]);
			if (!out[v].equals(tempout)) {
				out[v] = tempout;
				changed = true;
			}
		}
		if (changed)
			iterate(G);
	}

	public boolean reachable(int u, int v) {
		if (u < 0 || u >= count)
			throw new IndexOutOfBoundsException();
		if (v < 0 || v >= count)
			throw new IndexOutOfBoundsException();
		return in[u].contains(v);
	}

}