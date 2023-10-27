
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
	int X;
	int Y;
	List<Edge> edges = new ArrayList<>();

	class Edge {
		int from, to;
	
		Edge(int from, int to) {
			this.from = from;
			this.to = to;
		}


		public int getFrom() {
        	return from;
    	}

		public int getTo() {
        	return to;
    	}

    	public void setFrom(int from) {
        	this.from = from;
    	}
	}



    void readBipartiteGraph() {
	
		System.err.println("Bipartit graf:");
		// Läs antal hörn och kanter
		X = io.getInt();
		Y = io.getInt();
		V = X + Y;
		E = io.getInt();

		System.err.println("X: " + X);
		System.err.println("Y: " + Y);
		System.err.println("E: " + E);
		// Läs in kanterna
		for (int i = 0; i < E; ++i) {
			int a = io.getInt();
			int b = io.getInt();
			edges.add(new Edge(a, b));
			System.err.println("kant: "+ "(" + a + ", "+ b + ")");
		}
    }
    
/*
	bipgen 2 2 2
	X = 2, Y = 2, E = 2
	(2,3)
	(1,4)
	
		2--3
	s--		--t		
		1--4
	V = 6

 */


    
    void writeFlowGraph() {
		V = V + 2;
		int s = V-1, t = V;
		// Create a new list for additional edges
		List<Edge> additionalEdges = new ArrayList<>();
		
		// Add new edges from source to each existing node in X
		for(int i = 1; i <= X; i++) {
			additionalEdges.add(new Edge(s, i));
		}
	
		// Add new edges from Y to Sänka
		for(int i = X + 1; i <= X + Y; i++) {
			additionalEdges.add(new Edge(i, t));
		}
		
		edges.addAll(additionalEdges);
		// Skriv ut antal hörn och kanter samt källa och sänka
		//io.println();
		io.println(V);
		io.println(s + " " + t);
		io.println(edges.size());
		
		
		for (Edge edge : edges) {
			int c = 1;
			io.println(edge.from + " " + edge.to + " " + c);
		}
		// Var noggrann med att flusha utdata när flödesgrafen skrivits ut!
		io.flush();
		
		// Debugutskrift
		//System.err.println("Skickade iväg flödesgrafen");
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
	//System.err.println("Bipred avslutar\n");

	// Kom ihåg att stänga ner Kattio-klassen
	io.close();
    }
    
    public static void main(String args[]) {
	new BipRed();
    }
}

