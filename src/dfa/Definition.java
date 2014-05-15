package dfa;

public class Definition {
	private String variable;

	public String getVariable() {
		return variable;
	}

	private Integer position;

	public Integer getPosition() {
		return position;
	}

	Definition(String v, Integer p) {
		this.variable = v;
		this.position = p;
	}

	public String toString() {
		return String.format("<%s, %d>", variable, position);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Definition)) {
			return false;
		} else {
			Definition d = (Definition) obj;
			return (this.variable.equals(d.variable) && (this.position == d.position));
		}
	}

	public int hashCode() {
		return this.variable.hashCode() & this.position;
	}

}
