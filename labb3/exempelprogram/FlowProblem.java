import java.util.*;

// representerar flödesproblemet. Mha edmonds agoritm få vi ut resultatet. 
public class FlowProblem {
    int nodes;

    // En matris för kapacitet och en för flöden
    int[][] capacities;
    int[][] flow;

    // En matris för positiva flöden som används för att skriva ut svaret
    int[][] flowEdges;

    // En grannlista för snabb BFS
    List<List<Integer>> flowList;

    public FlowProblem(int nodes) {
        this.nodes = nodes;
        this.flowEdges = new int[nodes][nodes];
        this.capacities = new int[nodes][nodes];
        this.flow = new int[nodes][nodes];

        // Initialisera flowlist med tomma listor
        this.flowList = new ArrayList<>(nodes);
        for (int i = 0; i < nodes; i++) {
            this.flowList.add(new ArrayList<>());
        }
    }

    public void addEdge(int start, int end, int capacity) {
        // Om kanten har samma start och slut hjälper det ej flödesproblemet
        if (start == end) {
            return;
        }
        
        // Kapaciteten uppdateras mellan start och slut
        capacities[start][end] += capacity;
        // Lägg även till edge till flowList om inte redan finns
        if (!flowList.get(start).contains(end)) {
            flowList.get(start).add(end);
        }
        // Lägg även till residual edge till flowList om inte redan finns
        if (!flowList.get(end).contains(start)) {
            flowList.get(end).add(start);
            // residual flödet är 0 till att börja med
            capacities[end][start] = 0;
        }
    }

    public int edmondKarp(int source, int sink) {
        int total = 0;
        while (true) {
            // Kolla på kortaste vägen från source till sink
            int[] prev = bfs(source, sink);
            if (prev[sink] == -1) {
                break;
            }

            int currentSink = sink;
            int thisFlow = Integer.MAX_VALUE;

            // Går från sink tillbaka till source och sätter flödet
            // till minsta residual kapaciteten.
            while (currentSink != source) {
                int newSink = prev[currentSink];
                thisFlow = Math.min(capacities[newSink][currentSink] - flow[newSink][currentSink], thisFlow);
                currentSink = newSink;
            }

            if (thisFlow == 0) {
                break;
            }
            currentSink = sink;

            while (currentSink != source) {
                int newSink = prev[currentSink];
                flow[currentSink][newSink] -= thisFlow;
                flow[newSink][currentSink] += thisFlow;
                currentSink = newSink;
            }

            total += thisFlow;
        }
        return total;

    }

    public int[] bfs(int source, int sink) {
        int[] prev = new int[nodes];
        Arrays.fill(prev, -1);
        prev[source] = -2;
        Queue<Integer> order = new LinkedList<>();
        order.add(source);
        while (!order.isEmpty()) {
            int i = order.poll();
            for (int j : flowList.get(i)) {
                if (capacities[i][j] - flow[i][j] > 0 && prev[j] == -1) {
                    order.add(j);
                    prev[j] = i;
                    if (j == sink) {
                        return prev;
                    }
                }
            }
        }

        return prev;
    }

    // Scanner io
    public void positiveFlows(Kattio io) {
        int positiveFlow = 0;
        // Kopierar positiva flödesvärden från “flow” matrisen till “flowedges” matrisen
        for (int u = 0; u < nodes; u++) {
            for (int v = 0; v < nodes; v++) {
                if (flow[u][v] > 0) {
                    positiveFlow++;
                    flowEdges[u][v] = flow[u][v];
                }
            }
        }
        io.println(positiveFlow);
    }

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        //Scanner io = new Scanner(System.in);
        int nodes = io.getInt();
        // int nodes = io.nextInt();
        int source = io.getInt();
        int sink = io.getInt();
        int edges = io.getInt();

        FlowProblem flowProblem = new FlowProblem(nodes);

        for (int i = 0; i < edges; i++) {
            int start = io.getInt();
            int end = io.getInt();
            int capacity = io.getInt();
            flowProblem.addEdge(start - 1, end - 1, capacity);
        }

        int total = flowProblem.edmondKarp(source - 1, sink - 1);

        // io.println
        // io.println
        io.println(nodes);
        io.println((source) + " " + (sink) + " " + total);
        // Print positive flow edges
        flowProblem.positiveFlows(io);

        for (int i = 0; i < flowProblem.flowEdges.length; i++) {
            for (int j = 0; j < flowProblem.flowEdges[i].length; j++) {
                if (flowProblem.flowEdges[i][j] > 0) {
                    io.println((i + 1) + " " + (j + 1) + " " + flowProblem.flowEdges[i][j]);
                }
            }
        }
        io.close();
    }
}