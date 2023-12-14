
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
    int biNodes;
	int biEdges;
	int X;
	int Y;
	List<Edge> edges = new ArrayList<>();
	ArrayList<Edge> flowEdges = new ArrayList<>();
	int totflow;

	LinkedList<Edge>[] adjacencyList;

	class Edge {
		int from, to, cap, flow;
	
		Edge(int from, int to, int cap, int flow) {
			this.from = from;
			this.to = to;
			this.cap = cap;
            this.flow = flow;
		}
	}
	
	class MatchingSolution {
		
		int totalFlow;
		List<Edge> matchingEdges;
	
		MatchingSolution(int totalFlow, List<Edge> matchingEdges) {
			this.totalFlow = totalFlow;
			this.matchingEdges = matchingEdges;
		}
	}

	class FlowSolution {
		int V;
		int s;
		int t;
		int maxFlow;
		int edgesWithPosFlow;
		LinkedList<Edge> flowEdges;
	
		FlowSolution(int V, int s, int t, int maxFlow, int edgesWithPosFlow, LinkedList<Edge> flowEdges) {
			this.V = V;
			this.s = s;
			this.t = t;
			this.maxFlow = maxFlow;
			this.edgesWithPosFlow = edgesWithPosFlow;
			this.flowEdges = flowEdges;
		}
	}
	class FlowGraph {
		int V; // Antal hörn
		int s; // Källa
		int t; // Sänka
		List<Edge> edges; // Kanter
	
		FlowGraph(int V, int s, int t, List<Edge> edges) {
			this.V = V;
			this.s = s;
			this.t = t;
			this.edges = edges;
		}
	}

    void readBipartiteGraph() {
	
		
		// Läs antal hörn och kanter
		X = io.getInt();
		Y = io.getInt();
		biNodes = X + Y;
		biEdges = io.getInt();

		//System.err.println("X: " + X);
		//System.err.println("Y: " + Y);
		//System.err.println("E: " + E);
		// Läs in kanterna
		for (int i = 0; i < biEdges; ++i) {
			int a = io.getInt();
			int b = io.getInt();
			edges.add(new Edge(a, b, 1, 0));
			//System.err.println("kant: "+ "(" + a + ", "+ b + ")");
		}

        
    }
    
    FlowGraph writeFlowGraph() {
		System.err.println("flödesgrafen");
		int flowNodes = biNodes + 2;
		int s = flowNodes-1, t = flowNodes;
		
		// Add new edges from source to each existing node in X
		for(int i = 1; i <= X; i++) {
			if(X == 0) {
				edges.add(new Edge(s, t, 1, 0));
			}
			edges.add(new Edge(s, i, 1, 0));
		}
	
		// Add new edges from Y to Sänka
		for(int i = X + 1; i <= X + Y; i++) {
			if(Y == 0) {
				edges.add(new Edge(t, s, 1, 0));
			}
			edges.add(new Edge(i, t, 1, 0));
		}
		
		return new FlowGraph(flowNodes, s, t, edges);
    }
    
	boolean containsEdge(LinkedList<Edge> list, Edge edgeToFind){
        for(Edge edge : list){
            if(edge.to == edgeToFind.to){
                return true;
            }
        }
        return false;
    }

	FlowSolution solveFlowGraph(FlowGraph flowGraph) {
		int v = flowGraph.V; // Antal hörn
        int s = flowGraph.s; // Källa
        int t = flowGraph.t; // Utlopp
        int e = edges.size(); // Antal kanter

		adjacencyList = new LinkedList[v+1];
		
		for(int i = 1; i < v+1; i++){
			adjacencyList[i] = new LinkedList<Edge>();
		}
		

		for(Edge edge : edges) {
			int a = edge.from;
			int b = edge.to;
			int c = edge.cap;
			Edge newEdge = new Edge(a, b, 0, 0);
            Edge inverseEdge = new Edge(b, a, 0, 0);

			if(!containsEdge(adjacencyList[a], newEdge)){
				adjacencyList[a].add(newEdge);
            }
			getEdgeFromAdjacencyList(a, b).cap += c;
			if(!containsEdge(adjacencyList[b], inverseEdge)){
				adjacencyList[b].add(inverseEdge);
				getEdgeFromAdjacencyList(b, a).cap = 0;
			}

		}
		
		return fordFulkerson(s, t, v);
	}


	FlowSolution printAdjacencyList(LinkedList<Edge>[] adjacencyList,int V, int s, int t, int max_flow){
		// Variabel för att räkna antalet kanter med flöde större än 0
		int edgeCount = 0;
		LinkedList<Edge> flowEdges = new LinkedList<>();
		// Gå igenom varje nod och dess adjacenslista för att hitta och räkna kanter med positivt flöde
		for (int a = 1; a < adjacencyList.length; a++) {
			for (Edge edge : adjacencyList[a]) {
				if (edge.flow > 0) {
					edgeCount++;
					flowEdges.add(edge);
				}
			}
		}
		FlowSolution flowSolution = new FlowSolution(V, s, t, max_flow,edgeCount, flowEdges);
		// Skriv ut resultat
		
        return flowSolution;
	}

	public int[] bfs(int V, int s, int t)
    {
        // Create a queue, enqueue source vertex and mark
        // source vertex as visited
		int parent[] = new int[V + 1];
		Arrays.fill(parent, -1);
        Queue<Integer> queue = new LinkedList<>();
		queue.add(s);
        parent[s] = -2;
        while (!queue.isEmpty()) {
            int a = queue.poll();
			for (Edge b : adjacencyList[a]) {
                if (b.cap - b.flow > 0 && parent[b.to] == -1 && b.to != -1) {
                    queue.add(b.to);
                    parent[b.to] = a;
					if(b.to == t) {
						return parent;
					}
                }
            }
        }
        return parent; 
    }

	FlowSolution fordFulkerson(int s, int t, int V)
    {
        int a, b; //from, to
        
        int max_flow = 0; // There is no flow initially
 
        // Augment the flow while there is path from source
        // to sink
        while (true) {
			int[] parent = bfs(V, s, t);
			if(parent[t] == -1) {
				break;
			}		
            // Find minimum residual capacity of the edges
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            int path_flow = Integer.MAX_VALUE;
			
            for (b = t; b != s; b = parent[b]) {
                a = parent[b];
				Edge edge = getEdgeFromAdjacencyList(a, b);
                path_flow = Math.min(path_flow, edge.cap - edge.flow);
            }

			if (path_flow == 0) {
                break;
            }

            // update residual capacities of the edges and
            // reverse edges along the path
            for (b = t; b != s; b = parent[b]) {
                a = parent[b];
				getEdgeFromAdjacencyList(b, a).flow -= path_flow;
                getEdgeFromAdjacencyList(a, b).flow += path_flow;
            }
 
            // Add path flow to overall flow
            max_flow += path_flow;
        }
		
		FlowSolution flowSolution = printAdjacencyList(adjacencyList, V, s, t, max_flow);
        // Return the overall flow

		
        return flowSolution;
    }

	Edge getEdgeFromAdjacencyList(int u, int v){
		//System.err.println("a: "+ u + " b: " + v);
		for(int i = 0; i < adjacencyList[u].size(); i++){
			if(adjacencyList[u].get(i).to == v){
				//Return correct edge if found
				return adjacencyList[u].get(i);
			}
		}
		
		//return 0-Edge if not found
		return new Edge(-1, -1, -1, -1);
	}


    
    MatchingSolution readMaxFlowSolution(FlowSolution flowSolution) {
		// Läs in antal hörn, kanter, källa, sänka, och totalt flöde
		// (Antal hörn, källa och sänka borde vara samma som vi i grafen vi
		// skickade iväg)
		int v = flowSolution.V;
		//System.err.println(v);
		int s = flowSolution.s;
		int t = flowSolution.t;
		int totflow = flowSolution.maxFlow;
		//System.err.println(s + "  " + t + "  " + totflow);
		int e = flowSolution.edgesWithPosFlow;
		//System.err.println(e);
		ArrayList<Edge> matchinEdges = new ArrayList<>();

		for(Edge edge : flowSolution.flowEdges) {
			int a = edge.from;
			int b = edge.to;
			int f = edge.flow;

			if (f > 0 && !(a == s || a == t) && !(b == s || b == t)) {
				// Vi lägger endast till kanter med flöde och som inte är direkt kopplade till källa eller sänka
				matchinEdges.add(new Edge(a, b, 0, 0));
				//System.err.println(a + " " + b + " " + f);
			}
		}

		MatchingSolution matchingSolution = new MatchingSolution(totflow, matchinEdges);

		return matchingSolution;
    }
    
    
    void writeBipMatchSolution(MatchingSolution matchingSolution) {
	
	// Skriv ut antal hörn och storleken på matchningen
	io.println(X + " " + Y);
	io.println(matchingSolution.totalFlow);
	
	for (Edge edge : matchingSolution.matchingEdges) {
        // Kant mellan a och b ingår i vår matchningslösning
        io.println(edge.from + " " + edge.to);
    }

	io.flush();
    }


    
    BipRed2() {
	io = new Kattio(System.in, System.out);
	
	readBipartiteGraph();
	
	FlowGraph flowGraph = writeFlowGraph();

	FlowSolution flowSolution = solveFlowGraph(flowGraph);
	
	MatchingSolution matchingSolution = readMaxFlowSolution(flowSolution);
	
	writeBipMatchSolution(matchingSolution);

	// debugutskrift
	System.err.println("BipRed2 avslutar\n");

	// Kom ihåg att stänga ner Kattio-klassen
	io.close();
    }
    
    public static void main(String args[]) {
	new BipRed2();
    }
}
