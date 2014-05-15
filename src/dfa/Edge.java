package dfa;

public class Edge {
	private int u;
	private int v;

	Edge(int u, int v) {
		this.u = u;
		this.v = v;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Edge)) {
			return false;
		} else {
			Edge edge = (Edge) obj;
			return (this.u == edge.u) && (this.v == edge.v);
		}
	}

	public int hashCode() {
		return u & v;
	}

	public String toString() {
		return "<" + u + ", " + v + ">";
	}

}
