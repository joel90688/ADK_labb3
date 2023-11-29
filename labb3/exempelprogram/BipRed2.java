
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

public class BipRed2 {
    Kattio io;
    int V;
	int E;
	int X;
	int Y;
	List<Edge> edges = new ArrayList<>();
	ArrayList<Edge> flowEdges = new ArrayList<>();
	int totflow;

	class Edge {
		int from, to, value, flow;
	
		Edge(int from, int to, int value, int flow) {
			this.from = from;
			this.to = to;
			this.value = value;
            this.flow = flow;
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

		public void setValue(int value) {
        	this.value = value;
    	}

	}



    void readBipartiteGraph() {
	
		System.err.println("Bipartit graf");
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
			edges.add(new Edge(a, b, 0, 0));
			// System.err.println("kant: "+ "(" + a + ", "+ b + ")");
		}

        System.err.println("läst bipartit graf");
    }
    
    void writeFlowGraph() {
		System.err.println("flödesgrafen");
		V = V + 2;
		int s = V-1, t = V;
		// Create a new list for additional edges
		List<Edge> additionalEdges = new ArrayList<>();
		
		// Add new edges from source to each existing node in X
		for(int i = 1; i <= X; i++) {
			if(X == 0) {
				additionalEdges.add(new Edge(s, t, 0, 0));
			}
			additionalEdges.add(new Edge(s, i, 0, 0));
		}
	
		// Add new edges from Y to Sänka
		for(int i = X + 1; i <= X + Y; i++) {
			if(Y == 0) {
				additionalEdges.add(new Edge(t, s, 0, 0));
			}
			additionalEdges.add(new Edge(i, t, 0, 0));
		}
		
		edges.addAll(additionalEdges);
		// Skriv ut antal hörn och kanter samt källa och sänka
		//io.println();
		io.println(V);
		//System.err.println(V);
		io.println(s + " " + t);
		//System.err.println(s + " " + t);
		io.println(edges.size());
		//System.err.println(edges.size());
		
		
		for (Edge edge : edges) {
			int c = 1;
			io.println(edge.from + " " + edge.to + " " + c);
			//System.err.println(edge.from + " " + edge.to + " " + c);
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
        @SuppressWarnings("unchecked")
		LinkedList<Edge>[] adjacencyList = new LinkedList[v+1];
		
		
		for(int i = 1; i < v+1; i++){
			adjacencyList[i] = new LinkedList<Edge>();
		}
		for (int i = 0; i < e; i++) {
            int a = io.getInt();
            int b = io.getInt();
            int c = io.getInt();
			Edge newEdge = new Edge(a, b, c, 0);
			Edge inverseEdge = new Edge(b, a, 0, 0);
			if(containsEdge(adjacencyList[a], newEdge)){
				getEdgeFromAdjacencyList(adjacencyList, a, b).value = c;
			}else{
				adjacencyList[b].add(new Edge(b, a, 0, 0));
				adjacencyList[a].add(new Edge(a, b, c, 0));
			}
        }

		fordFulkerson(adjacencyList, s, t, v);
	}

	boolean containsEdge(LinkedList<Edge> list, Edge edgeToFind){
		for(Edge edge : list){
			if(edge.to == edgeToFind.to){
				return true;
			}
		}
		return false;
	}

	void printAdjacencyList(LinkedList<Edge>[] adjacencyList,int V, int s, int t, int max_flow){
		// Variabel för att räkna antalet kanter med flöde större än 0
		int edgeCount = 0;

		// Gå igenom varje nod och dess adjacenslista för att hitta och räkna kanter med positivt flöde
		for (int a = 1; a < adjacencyList.length; a++) {
			for (Edge edge : adjacencyList[a]) {
				if (edge.flow > 0) {
					edgeCount++;
				}
			}
		}

		// Skriv ut resultat
		io.println(V);
		io.println(s + " " + t + " " + max_flow);
		io.println(edgeCount);

		// Gå igenom varje nod och dess adjacenslista igen för att skriva ut kanter med positivt flöde
		for (int a = 1; a < adjacencyList.length; a++) {
			for (Edge edge : adjacencyList[a]) {
				if (edge.flow > 0) {
					io.println(edge.from + " " + edge.to + " " + edge.value);
				}
			}
		}

        io.flush();
        System.err.println("Skickade iväg flödeslösningen");
	}

	boolean bfs(LinkedList<Edge>[] adjacencyList, int V, int s, int t, int parent[])
    {
        // Create a queue, enqueue source vertex and mark
        // source vertex as visited
        //LinkedList<Integer> queue = new LinkedList<Integer>();
		
        Queue<Integer> queue = new LinkedList<>();
        Arrays.fill(parent, -1);
		queue.add(s);
        parent[s] = s;
        while (!queue.isEmpty()) {
            int a = queue.poll();
            for (int b = 1; b <= V; b++) {
				
                if (parent[b] == -1 && getEdgeFromAdjacencyList(adjacencyList, a, b).from != s && getEdgeFromAdjacencyList(adjacencyList, a, b).value -getEdgeFromAdjacencyList(adjacencyList, a, b).flow  > 0) {
					queue.add(b);
                    parent[b] = a;  			
                }
            }
        }
       
        return parent[t] != -1; 
    }

	Edge getEdgeFromAdjacencyList(LinkedList<Edge>[] list, int u, int v){
		//System.err.println("a: "+ u + " b: " + v);
		for(int i = 0; i < list[u].size(); i++){
			
			if(list[u].get(i).to == v){
				//Return correct edge if found
				return list[u].get(i);
			}
		}
		
		//return 0-Edge if not found
		return new Edge(0, 0, 0, 0);
	}

	int fordFulkerson(LinkedList<Edge> adjacencyList[], int s, int t, int V)
    {
        int a; //from
		int b; //to

		// Kanske inte behövs
		LinkedList<Edge>[] resList = adjacencyList.clone();
        
        // This array is filled by BFS and to store path
        int parent[] = new int[V + 1];
 
        int max_flow = 0; // There is no flow initially
 
        // Augment the flow while there is path from source
        // to sink
        while (bfs(resList, V, s, t, parent)) {
			
            // Find minimum residual capacity of the edges
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            int path_flow = Integer.MAX_VALUE;
            for (b = t; b != s; b = parent[b]) {
                a = parent[b];
                path_flow = Math.min(path_flow, getEdgeFromAdjacencyList(resList, a, b).value - getEdgeFromAdjacencyList(resList, a, b).flow);
            }
 
            // update residual capacities of the edges and
            // reverse edges along the path
            for (b = t; b != s; b = parent[b]) {
                a = parent[b];
                getEdgeFromAdjacencyList(resList, a, b).flow += path_flow;
                getEdgeFromAdjacencyList(resList, b, a).flow -= path_flow;
            }
 
            // Add path flow to overall flow
            max_flow += path_flow;
        }
		printAdjacencyList(adjacencyList, V, s, t, max_flow);
        // Return the overall flow
        return max_flow;
    }


    
    void readMaxFlowSolution() {
		// Läs in antal hörn, kanter, källa, sänka, och totalt flöde
		// (Antal hörn, källa och sänka borde vara samma som vi i grafen vi
		// skickade iväg)
		int v = io.getInt();
		//System.err.println(v);
		int s = io.getInt();
		int t = io.getInt();
		totflow = io.getInt();
		//System.err.println(s + "  " + t + "  " + totflow);
		int e = io.getInt();
		//System.err.println(e);
		

		for (int i = 0; i < e; ++i) {
			// Flöde f från a till b
			int a = io.getInt();
			int b = io.getInt();
			int f = io.getInt();
			if (f > 0 && a != s && b != t) {
				// Vi lägger endast till kanter med flöde och som inte är direkt kopplade till källa eller sänka
				flowEdges.add(new Edge(a, b, 0, 0));
				//System.err.println(a + " " + b + " " + f);
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
    
    BipRed2() {
	io = new Kattio(System.in, System.out);
	
	//readBipartiteGraph();
	
	//writeFlowGraph();

	solveFlowGraph();
	
	//readMaxFlowSolution();
	
	//writeBipMatchSolution();

	// debugutskrift
	System.err.println("BipRed2 avslutar\n");

	// Kom ihåg att stänga ner Kattio-klassen
	io.close();
    }
    
    public static void main(String args[]) {
	new BipRed2();
    }
}