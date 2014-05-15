import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import dfa.ControlFlowGraph;
import dfa.Mapping;
import dfa.ReachingDefinition;

public class RDDU {
	final static String TEST_CLASS = "test/Example1.class";

	public static void run(Method mainMethod, PrintWriter rdOutput,
			PrintWriter duOutput) {
		Mapping mapping = new Mapping(mainMethod);
		ControlFlowGraph cfg = new ControlFlowGraph(mapping);
		// System.out.println(cfg);
		ReachingDefinition rd = new ReachingDefinition(cfg);
		rdOutput.println(rd.RDString());
		rdOutput.close();
		duOutput.print(rd.DUChainDotty());
		duOutput.close();
	}

	public static void main(String[] args) {
		if (args.length < 3) {
			args = new String[3];
			args[0] = TEST_CLASS;
			args[1] = "Example1.dotty";
			args[2] = "Example1.rd.text";
		}
		JavaClass cls = null;
		try {
			cls = (new ClassParser(args[0])).parse();
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.err.println("Error while parsing " + args[0] + ".");
			System.exit(1);
		}

		Method mainMethod = null;
		for (Method m : cls.getMethods()) {
			if ("main".equals(m.getName())) {
				mainMethod = m;
				break;
			}
		}
		if (mainMethod == null) {
			System.err.println("No main method found in " + args[0] + ".");
			System.exit(1);
		}
		try {
			run(mainMethod, new PrintWriter(args[2]), new PrintWriter(args[1]));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}