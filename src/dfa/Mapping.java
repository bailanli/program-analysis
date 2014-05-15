package dfa;

import java.util.HashMap;

import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NOP;

public class Mapping {
	public static final int CODE_ENTRY = -1;
	public static final int CODE_EXIT = -2;
	public static final int VERTEX_ENTRY = 0;

	private String classname;
	private String methodname;
	// Map associating line number with instruction.
	private InstructionList instructionList;
	private LocalVariableTable localVariableTable;
	private LineNumberTable lineNumberTable;

	// Byte code Position to source code position map
	private HashMap<Integer, Integer> bsmap;

	// Byte code Position to vertex map
	private HashMap<Integer, Integer> bvmap;

	// Vertex to Source code position map
	private HashMap<Integer, Integer> vsmap;

	// Source code position to Vertex map
	private HashMap<Integer, Integer> svmap;

	public Mapping(Method method) {
		this.setInstructionlist(new InstructionList(method.getCode().getCode()));
		this.setLocalVariableTable(method.getLocalVariableTable());
		this.setLineNumberTable(method.getLineNumberTable());
		this.generateMapping();
	}

	public Mapping(String classname, Method method) {
		this.setClassname(classname);
		this.setMethodname(method.getName());
		this.setInstructionlist(new InstructionList(method.getCode().getCode()));
		this.setLocalVariableTable(method.getLocalVariableTable());
		this.setLineNumberTable(method.getLineNumberTable());
		this.generateMapping();
	}

	private void generateMapping() {
		bsmap = new HashMap<Integer, Integer>();
		bvmap = new HashMap<Integer, Integer>();
		vsmap = new HashMap<Integer, Integer>();
		svmap = new HashMap<Integer, Integer>();
		// entry node
		bsmap.put(CODE_ENTRY, CODE_ENTRY);
		bvmap.put(CODE_ENTRY, VERTEX_ENTRY);
		vsmap.put(VERTEX_ENTRY, CODE_ENTRY);
		svmap.put(CODE_ENTRY, VERTEX_ENTRY);
		int vertex = 1;
		for (InstructionHandle ih = instructionList.getStart(); ih != null; ih = ih
				.getNext()) {
			int source = getLineNumberTable().getSourceLine(ih.getPosition());
			bsmap.put(ih.getPosition(), source);
//			if (!vsmap.containsValue(source)) {
				vsmap.put(vertex, source);
				svmap.put(source, vertex);
				vertex++;
//			}
			bvmap.put(ih.getPosition(), svmap.get(source));
		}
		// exit node
		bsmap.put(CODE_EXIT, CODE_EXIT);
		bvmap.put(CODE_EXIT, vertex);
		vsmap.put(vertex, CODE_EXIT);
		svmap.put(vertex, CODE_EXIT);
	}

	public int getByteCodeNumber() {
		return bsmap.size();
	}

	public String getClassname() {
		return classname.split("\\.")[0];
	}

	public Instruction getInstructionFromVertex(int v) {
		if (v == VERTEX_ENTRY)
			return new NOP();
		else if (v == getVertexNumber() - 1)
			return new NOP();
		else
			return instructionList.getInstructions()[v - 1];
	}

	public InstructionList getInstructionlist() {
		return instructionList;
	}

	public LineNumberTable getLineNumberTable() {
		return lineNumberTable;
	}

	public String getLocalVariableName(int pos) {
		return localVariableTable.getLocalVariable(pos).getName();
	}

	// Generate Byte Code Position to Source Code Position Map

	public LocalVariableTable getLocalVariableTable() {
		return localVariableTable;
	}

	public String getMethodname() {
		return methodname;
	}

	public int getSourceFromByte(int pos) {
		return bsmap.get(pos);
	}

	public String getSourceStringFromVertex(int v) {
		Integer source = vsmap.get(v);
		switch (source) {
		case CODE_ENTRY:
			return "Entry";
		case CODE_EXIT:
			return "Exit";
		default:
			return source.toString();
		}
	}

	public int getSourceFromVertex(int v) {
		return vsmap.get(v);
	}

	public int getVertexFromByte(int pos) {
		return bvmap.get(pos);
	}

	public int getVertexNumber() {
		return vsmap.size();
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public void setInstructionlist(InstructionList instructionlist) {
		this.instructionList = instructionlist;
	}

	public void setLineNumberTable(LineNumberTable lineNumberTable) {
		this.lineNumberTable = lineNumberTable;
	}

	public void setLocalVariableTable(LocalVariableTable localVariableTable) {
		this.localVariableTable = localVariableTable;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
}
