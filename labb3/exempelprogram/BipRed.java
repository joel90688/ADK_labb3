
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
	ArrayList<Edge> flowEdges = new ArrayList<>();
	
	int totflow;

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

		//System.err.println("X: " + X);
		//System.err.println("Y: " + Y);
		//System.err.println("E: " + E);
		// Läs in kanterna
		for (int i = 0; i < E; ++i) {
			int a = io.getInt();
			int b = io.getInt();
			edges.add(new Edge(a, b));
			// System.err.println("kant: "+ "(" + a + ", "+ b + ")");
		}
    }

    
    void writeFlowGraph() {
		System.err.println("flödesgrafen");
		V = V + 2;
		int s = V-1, t = V;
		int c = 1;
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
		System.err.println(V);
		io.println(s + " " + t);
		System.err.println(s + " " + t);
		io.println(edges.size());
		System.err.println(edges.size());
		
		for (Edge edge : edges) {
			io.println(edge.from + " " + edge.to + " " + c);
			System.err.println(edge.from + " " + edge.to + " " + c);
		}
		// Var noggrann med att flusha utdata när flödesgrafen skrivits ut!
		io.flush();
		
		// Debugutskrift
		System.err.println("Skickade iväg flödesgrafen");
    }
    
	
	void solveFlowGraph() {
        int v = io.getInt(); // Antal hörn
        int s = io.getInt(); // Källa
        int t = io.getInt(); // Utlopp
        int e = io.getInt(); // Antal kanter

        int[][] capacity = new int[v + 1][v + 1];
        int[][] flow = new int[v + 1][v + 1];

        // Läs in kanterna och uppdatera kapaciteten
        for (int i = 0; i < e; i++) {
            int a = io.getInt();
            int b = io.getInt();
            int c = io.getInt();
            capacity[a][b] = c;
        }

        int maxFlow = 0;

        // Ford-Fulkerson's algoritm
        while (true) {
            int[] parent = new int[v + 1]; // Används för att spara vägen
            Arrays.fill(parent, -1);
            Queue<Integer> queue = new LinkedList<>();
            queue.add(s);
            parent[s] = s;

            // Standard BFS för att hitta en förbättrande stig
            while (!queue.isEmpty()) {
                int a = queue.poll();
                for (int b = 1; b <= v; b++) {
                    if (parent[b] == -1 && capacity[a][b] - flow[a][b] > 0) {
                        queue.add(b);
                        parent[b] = a;
						
                    }
                }
            }

            // Om det inte finns någon stig från s till t
            if (parent[t] == -1) {
                break;
            }

            // Hitta den minsta restkapaciteten längs stigen
            int pathFlow = Integer.MAX_VALUE;
            for (int b = t; b != s; b = parent[b]) {
                int a = parent[b];
                pathFlow = Math.min(pathFlow, capacity[a][b] - flow[a][b]);
            }

            // Uppdatera flödena längs stigen
            for (int b = t; b != s; b = parent[b]) {
                int a = parent[b];
                flow[a][b] += pathFlow;
                flow[b][a] -= pathFlow;
            }

            maxFlow += pathFlow;
        }

		int edgeCount = 0;

    		for (int a = 1; a <= v; a++) {
        		for (int b = 1; b <= v; b++) {
            		if (flow[a][b] > 0) {
                		edgeCount++;
            		}
        		}
    		}
        // Skriv ut resultat
        io.println(v);
        io.println(s + " " + t + " " + maxFlow);
		io.println(edgeCount);
        for (int a = 1; a <= v; a++) {
            for (int b = 1; b <= v; b++) {
                if (flow[a][b] > 0) {
                    io.println(a + " " + b + " " + flow[a][b]);
                }
            }
        }

        io.flush();
    }

    
    void readMaxFlowSolution() {
		// Läs in antal hörn, kanter, källa, sänka, och totalt flöde
		// (Antal hörn, källa och sänka borde vara samma som vi i grafen vi
		// skickade iväg)
		int v = io.getInt();
		int s = io.getInt();
		int t = io.getInt();
		totflow = io.getInt();
		int e = io.getInt();
		System.err.println(v);
		System.err.println(s + " " + t + " " + totflow);
		System.err.println(e);
		

		for (int i = 0; i < e; ++i) {
			
			// Flöde f från a till b
			int a = io.getInt();
			int b = io.getInt();
			int f = io.getInt();
			if (f > 0 && a != s && b != t) {
				// Vi lägger endast till kanter med flöde och som inte är direkt kopplade till källa eller sänka
				flowEdges.add(new Edge(a, b));
				System.err.println(a + " " + b + " " + f);
			}
	
		}

		System.err.println("Läst flödeslösningen");
    }
    
    
    void writeBipMatchSolution() {
	
	// Skriv ut antal hörn och storleken på matchningen
	io.println(X + " " + Y);
	io.println(totflow);
	
	for (Edge edge : flowEdges) {
        // Kant mellan a och b ingår i vår matchningslösning
        io.println(edge.from + " " + edge.to);
    }

	io.flush();
    }
    
    BipRed() {
	io = new Kattio(System.in, System.out);
	
	//readBipartiteGraph();
	
	//writeFlowGraph();

	solveFlowGraph();
	
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

