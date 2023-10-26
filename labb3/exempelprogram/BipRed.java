import java.util.ArrayList;
import java.util.List;

/**
 * Exempel på in- och utdatahantering för maxflödeslabben i kursen
 * ADK.
 *
 * Använder Kattio.java för in- och utläsning.
 * Se http://kattis.csc.kth.se/doc/javaio
 *
 * @author: Per Austrin
 */

public class BipRed {
    Kattio io;
    int V;
	int E;
	List<Edge> edges = new ArrayList<>();

	class Edge {
		int from, to;
	
		Edge(int from, int to) {
			this.from = from;
			this.to = to;
		}
	}


    void readBipartiteGraph() {
	
	System.out.println("Bipartit graf:");
	// Läs antal hörn och kanter
	int x = io.getInt();
	int y = io.getInt();
	V = x + y;
	E = io.getInt();
	System.out.println("X: " + x);
	System.out.println("Y: " + y);
	System.out.println("E: " + E);
	// Läs in kanterna
	for (int i = 0; i < E; ++i) {
	    int a = io.getInt();
	    int b = io.getInt();
		edges.add(new Edge(a, b));
		System.out.println("kant: "+ "(" + a + ", "+ b + ")");
	}
    }
    
    
    void writeFlowGraph() {
	int s = 1, t = 2;
	// Skriv ut antal hörn och kanter samt källa och sänka
	io.println(V);
	io.println(s + " " + t);
	io.println(E);
	for (Edge edge : edges) {
		int c = 1;
		io.println(edge.from + " " + edge.to + " " + c);
	}
	// Var noggrann med att flusha utdata när flödesgrafen skrivits ut!
	io.flush();
	
	// Debugutskrift
	System.err.println("Skickade iväg flödesgrafen");
    }
    
    
    void readMaxFlowSolution() {
	// Läs in antal hörn, kanter, källa, sänka, och totalt flöde
	// (Antal hörn, källa och sänka borde vara samma som vi i grafen vi
	// skickade iväg)
	int v = io.getInt();
	int s = io.getInt();
	int t = io.getInt();
	int totflow = io.getInt();
	int e = io.getInt();
	
	for (int i = 0; i < e; ++i) {
	    // Flöde f från a till b
	    int a = io.getInt();
	    int b = io.getInt();
	    int f = io.getInt();
	}
    }
    
    
    void writeBipMatchSolution() {
	int x = 17, y = 4711, maxMatch = 0;
	
	// Skriv ut antal hörn och storleken på matchningen
	io.println(x + " " + y);
	io.println(maxMatch);
	
	for (int i = 0; i < maxMatch; ++i) {
	    int a = 5, b = 2323;
	    // Kant mellan a och b ingår i vår matchningslösning
	    io.println(a + " " + b);
	}
	
    }
    
    BipRed() {
	io = new Kattio(System.in, System.out);
	
	readBipartiteGraph();
	
	writeFlowGraph();
	
	//readMaxFlowSolution();
	
	//writeBipMatchSolution();

	// debugutskrift
	System.err.println("Bipred avslutar\n");

	// Kom ihåg att stänga ner Kattio-klassen
	io.close();
    }
    
    public static void main(String args[]) {
	new BipRed();
    }
}

