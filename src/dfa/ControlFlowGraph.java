package dfa;

import java.util.HashSet;
import java.util.Set;

import graph.Graph;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GotoInstruction;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.JsrInstruction;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Select;
import org.apache.bcel.generic.StoreInstruction;

public class ControlFlowGraph extends Graph {

	private Mapping mapping;

	private Set<String>[] def;

	public Set<String> getDef(int vertex) {
		return def[vertex];
	}

	public void addDef(String name, int vertex) {
		def[vertex].add(name);
	}

	private Set<String>[] use;

	public Set<String> getUse(int vertex) {
		return use[vertex];
	}

	public void addUse(String name, int vertex) {
		use[vertex].add(name);
	}

	@SuppressWarnings("unchecked")
	void initDU(int v) {
		def = new Set[v];
		use = new Set[v];
		for (int i = 0; i < v; i++) {
			def[i] = new HashSet<String>();
			use[i] = new HashSet<String>();
		}
	}

	public ControlFlowGraph(int v) {
		super(v);
		initDU(v);
	}

	public ControlFlowGraph(Mapping mapping) {
		this(mapping.getVertexNumber());
		setMapping(mapping);
		// Add edge from entry to the first node
		addEdge(Mapping.VERTEX_ENTRY, 1);
		// System.out.println("DEF: "
		// + mapping.getLocalVariableName(Mapping.VERTEX_ENTRY));
		addDef(mapping.getLocalVariableName(Mapping.VERTEX_ENTRY),
				Mapping.VERTEX_ENTRY);
		for (InstructionHandle ih = mapping.getInstructionlist().getStart(); ih != null; ih = ih
				.getNext()) {
			int fromnode = mapping.getVertexFromByte(ih.getPosition());
			Instruction i = ih.getInstruction();
			if (i instanceof BranchInstruction) {
				BranchInstruction bi = (BranchInstruction) i;
				if (bi instanceof GotoInstruction) {
					int tonode = mapping.getVertexFromByte(bi.getTarget()
							.getPosition());
					addEdge(fromnode, tonode);
				} else if (bi instanceof IfInstruction) {
					int tonode = mapping.getVertexFromByte(bi.getTarget()
							.getPosition());
					addEdge(fromnode, tonode);
					tonode = mapping.getVertexFromByte(ih.getNext()
							.getPosition());
					addEdge(fromnode, tonode);
				} else if (bi instanceof JsrInstruction) {
					int tonode = mapping.getVertexFromByte(bi.getTarget()
							.getPosition());
					addEdge(fromnode, tonode);
				} else if (bi instanceof Select) {
					Select s = (Select) bi;
					int tonode = mapping.getVertexFromByte(bi.getTarget()
							.getPosition());
					addEdge(fromnode, tonode);
					for (InstructionHandle target : s.getTargets()) {
						tonode = mapping
								.getVertexFromByte(target.getPosition());
						addEdge(fromnode, tonode);
					}
				}
			} else if (i instanceof ReturnInstruction) {
				int tonode = mapping.getVertexFromByte(Mapping.CODE_EXIT);
				addEdge(fromnode, tonode);
			} else {
				if (i instanceof StoreInstruction) {
					StoreInstruction si = (StoreInstruction) i;
					// System.out.println("DEF: "
					// + mapping.getLocalVariableName(si.getIndex()));
					addDef(mapping.getLocalVariableName(si.getIndex()),
							fromnode);
				} else if (i instanceof LoadInstruction) {
					LoadInstruction li = (LoadInstruction) i;
					// System.out.println("USE: "
					// + mapping.getLocalVariableName(li.getIndex()));
					addUse(mapping.getLocalVariableName(li.getIndex()),
							fromnode);
				} else if (i instanceof IINC) {
					IINC inc = (IINC) i;
					addDef(mapping.getLocalVariableName(inc.getIndex()),
							fromnode);
					addUse(mapping.getLocalVariableName(inc.getIndex()),
							fromnode);
				}
				int tonode = mapping.getVertexFromByte(ih.getNext()
						.getPosition());
				addEdge(fromnode, tonode);
			}
		}
	}

	public void addEdge(int v, int w) {
		if (v != w) {
			super.addEdge(v, w);
		}
	}

	public Mapping getMapping() {
		return mapping;
	}

	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		String NEWLINE = System.getProperty("line.separator");
		for (int v = 0; v < V; v++) {
			if (!def[v].isEmpty()) {
				for (String name : def[v])
					s.append(String.format("%d: DEF %s" + NEWLINE, v, name));
			}
			if (!use[v].isEmpty()) {
				for (String name : use[v])
					s.append(String.format("%d: USE %s" + NEWLINE, v, name));
			}
		}

		return super.toString() + s.toString();
	}
}
