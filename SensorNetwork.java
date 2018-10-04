import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SensorNetwork {

    private static long seed = 995;
    static Random rand = new Random(995);
    static Map<Integer, Axis> nodes = new LinkedHashMap<Integer, Axis>();
    static Map<Integer, Axis> nodes2 = new LinkedHashMap<Integer, Axis>();
	Map<Integer, Boolean> discovered = new HashMap<Integer, Boolean>();
	Map<Integer, Boolean> explored = new HashMap<Integer, Boolean>();
	Map<Integer, Integer> parent = new HashMap<Integer, Integer>();
	Map<Integer, Integer> connectedNodes = new HashMap<Integer, Integer>();
	Stack<Integer> s = new Stack<Integer>();
	static Map<String, Link> links = new HashMap<String, Link>();
	static ArrayList<Path> paths = new ArrayList<>();
    static int minCapacity;
    static int capacityRandomRange;
    static int[] dataGens;
    static int[] storageNodes;

	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the width: (15)");
//		double width = scan.nextDouble();
        double width = 15;

		System.out.println("Enter the height: (15)");
//		double height = scan.nextDouble();
        double height = 15;

		System.out.println("Enter the number of nodes: (6)");
//		int numberOfNodes = scan.nextInt();
        int numberOfNodes = 6;

		System.out.println("Enter the Transmission range in meters: (30)");
//		int transmissionRange = scan.nextInt();
        int transmissionRange = 15;

		System.out.println("How many DGs(Data Generators)? (2)");
//		int numberOfDG = scan.nextInt();
        int numberOfDG = 2;

		dataGens = new int[numberOfDG];
		System.out.println("Assuming the first " + numberOfDG + " nodes are DGs\n");
		for (int i=1; i<=dataGens.length; i++) {
            dataGens[i-1] = i;
        }

        storageNodes = new int[numberOfNodes-numberOfDG];
        for (int i=0; i<storageNodes.length; i++){
            storageNodes[i] = i + 1 + numberOfDG;
        }

		System.out.println("How many data items per DG? (30)");
//		int numberOfDataItemsPerDG = scan.nextInt();
        int numberOfDataItemsPerDG = 30;

        System.out.println("Capacity Random Range per node up from the min capacity:(5)");
//		capacityRandomRange= scan.nextInt();
        capacityRandomRange= 5;


		int numberOfSupDem = numberOfDataItemsPerDG * numberOfDG;
        System.out.println("The total number of data items in supply/demand: " + numberOfSupDem);
        scan.close();

		int numberOfStorageNodes = numberOfNodes - numberOfDG;
		int totalNumberOfData = numberOfDG * numberOfDataItemsPerDG;
		minCapacity = totalNumberOfData / numberOfStorageNodes;


		SensorNetwork sensor = new SensorNetwork();
		sensor.populateNodes(numberOfNodes, width, height);

		System.out.println("\nNode List:");
		for(int key :sensor.nodes.keySet()) {
			Axis ax = sensor.nodes.get(key);
			System.out.println("Node:" + key + ", xAxis:" + ax.getxAxis() + ", yAxis:" + ax.getyAxis());
		}

		Map<Integer, Set<Integer>> adjacencyList1 = new LinkedHashMap<Integer, Set<Integer>> ();

		sensor.populateAdjacencyList(numberOfNodes, transmissionRange, adjacencyList1);
		System.out.println("\nAdjacency List: ");

		for(int i: adjacencyList1.keySet()) {
			System.out.print(i);
			System.out.print(": {");
			int adjSize = adjacencyList1.get(i).size();

			if(!adjacencyList1.isEmpty()){
                int adjCount = 0;
				for(int j: adjacencyList1.get(i)) {
                    adjCount+=1;
				    if(adjCount==adjSize){
                        System.out.print(j);
                    } else {
                        System.out.print(j + ", ");
                    }
				}
			}
			System.out.println("}");
			}

		sensor.executeDepthFirstSearchAlg(width, height, adjacencyList1);
        System.out.println();

        System.out.println("Sensor Network Edges with Distance, Cost and Capacity");
		for (Link link : links.values()){
		    link.setCapacity(rand.nextInt(capacityRandomRange+1) + minCapacity);
		    System.out.println(link.toString());
        }
        System.out.println();

        StringBuilder dijkastra_input = new StringBuilder();
		for (Link link: links.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
        }


        String fileName = "dijkastra_input.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(dijkastra_input.toString());

        writer.close();

        // Calling Dijkastra Algorithm
        WeighedDigraph graph;
        graph = new WeighedDigraph(fileName);
        // Print graph
        // System.out.print("Representation of WeighedDigraph\n");
        //System.out.print(graph);
        // System.out.print("\n");

        DijkstraFind finder = new DijkstraFind(graph);

        System.out.print("Min Cost Flow Graph: Edge, Cost, Capacity\n");
        for(int dg: dataGens){
            for(int sn: storageNodes) {
                ArrayList<Integer> path = finder.shortestPath(dg, sn);
                double cost = 0;
                ArrayList<Double> capacities = new ArrayList<>();
                for (int i = 1; i <path.size(); i++) {
                    int tail, head;
                    tail = path.get(i-1);
                    head = path.get(i);
                    if (tail>head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                    Link link = links.get("(" + tail + ", " + head + ")");
                    cost += link.getCost();
                    capacities.add(link.getCapacity());
                }
                double capacity = Collections.min(capacities);
                Path newPath = new Path(path, cost, capacity);
                paths.add(newPath);
                System.out.println(newPath);
            }
        }

        for (int i = 0; i < dataGens.length; i++) {
            ArrayList<Integer> dummyArrList = new ArrayList<>();
            dummyArrList.add(0);
            dummyArrList.add(dataGens[i]);
            Path newPath = new Path(dummyArrList, 0, numberOfDataItemsPerDG);
            paths.add(newPath);
            System.out.println(newPath);
        }

        for (int i = 0; i < storageNodes.length; i++) {
            ArrayList<Integer> dummyArrList = new ArrayList<>();
            dummyArrList.add(storageNodes[i]);
            dummyArrList.add(numberOfNodes+1);
            Path newPath = new Path(dummyArrList, 0, minCapacity);
            paths.add(newPath);
            System.out.println(newPath);
        }

        System.out.println();

        /*StringBuilder output = new StringBuilder();

        output.append("p min ").append(numberOfNodes + 2).append(" ").append(paths.size()).append("\n");
        output.append("c min-cost flow problem with ").append(numberOfNodes+2).append(" nodes and ").
                append(paths.size()).append(" arcs\n");
        output.append("n 0 ").append(numberOfSupDem).append("\n");
        output.append("c supply of ").append(numberOfSupDem).append(" at node 0").append("\n");
        output.append("n ").append(numberOfNodes+1).append(" -").append(numberOfSupDem).append("\n");
        output.append("c demand of ").append(numberOfSupDem).append(" at node ").append(numberOfNodes+1).append("\n");
        output.append("c arc list follows\n");
        output.append("c arc has <tail> <head> <capacity l.b.> <capacity u.b> <cost>\n");

        for (Path path : paths){
            output.append("a ").append(path.getPath().get(0)).append(" ").
                    append(path.getPath().get(path.getPath().size()-1)).append(" ").append("0 ").
                    append((int) path.getCapacity()).append(" ").append(path.getCost()).append("\n");
        }
        System.out.println("Generated Input file for cs2-4.6 program:(pls refer to ourinput.inp in the folder");
        System.out.println(output);

        fileName = "outinput.inp";
        writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(output.toString());

        writer.close();*/
        
        //start testing if the net work is bi-conneceted
        System.out.println("to test the network is bi-connected, delete one of the nodes");
        
        //copy the nodes because directly removing the original nodes will influence the graphic
        for (Map.Entry<Integer, Axis> entry : nodes.entrySet()) {
            nodes2.put(entry.getKey(), entry.getValue());
        }
        //remove node 6
        nodes2.remove(6);
        
        //print the node list
		for(int key :sensor.nodes2.keySet()) {
			Axis ax = sensor.nodes2.get(key);
			System.out.println("Node:" + key + ", xAxis:" + ax.getxAxis() + ", yAxis:" + ax.getyAxis());
		}
		
		//create a new adjacent List base on the new nodes
		Map<Integer, Set<Integer>> adjacencyList2 = new LinkedHashMap<Integer, Set<Integer>> ();
		sensor.checkbiconnect(numberOfNodes-1, transmissionRange, adjacencyList2);

		//print out the new adjacent List
		System.out.println("\nAdjacency List: ");
		for(int i: adjacencyList2.keySet()) {
			System.out.print(i);
			System.out.print(": {");
			int adjSize = adjacencyList2.get(i).size();

			if(!adjacencyList2.isEmpty()){
                int adjCount = 0;
				for(int j: adjacencyList2.get(i)) {
                    adjCount+=1;
				    if(adjCount==adjSize){
                        System.out.print(j);
                    } else {
                        System.out.print(j + ", ");
                    }
				}
			}
			System.out.println("}");
			}
        //execute algorithm to draw the graphic
        sensor.executeDepthFirstSearchAlgbi(width, height, adjacencyList2);   
        
	}

    double getCost(double l){
        final int K = 512; // k = 512B (from paper0)
        final double E_elec = 100 * Math.pow(10,-9); // E_elec = 100nJ/bit (from paper1)
        final double Epsilon_amp = 100 * Math.pow(10,-12); // Epsilon_amp = 100 pJ/bit/squared(m) (from paper1)
        double Etx = E_elec * K + Epsilon_amp * K * l * l; // Sending energy consumption
        double Erx = E_elec * K; // Receiving energy consumption
        return Etx + Erx; // return the sum of sending and receiving energy
    }
    
    //for the original graphic
	void executeDepthFirstSearchAlg(double width, double height, Map<Integer, Set<Integer>> adjList) {
		System.out.println("\nExecuting DFS Algorithm");
		s.clear();
		explored.clear();
		discovered.clear();
		parent.clear();
		List<Set<Integer>> connectedNodes = new ArrayList<Set<Integer>>();
		for(int node: adjList.keySet()) {
			Set<Integer> connectedNode = new LinkedHashSet<Integer>();
			recursiveDFS(node, connectedNode, adjList);
			
			if(!connectedNode.isEmpty()) {
				connectedNodes.add(connectedNode);
			}
		}
		
		if(connectedNodes.size() == 1) {
			System.out.println("Graph is fully connected with one connected component.");
		} else {
			System.out.println("Graph is not fully connected");
		}
		
		System.out.println("There are " + connectedNodes.size() + " connected components");
		for(Set<Integer> list: connectedNodes) {
			System.out.println(list);
		}
		
		//Draw first sensor network graph
		SensorNetworkGraph graph = new SensorNetworkGraph(dataGens);
		graph.setGraphWidth(width);
		graph.setGraphHeight(height);
		graph.setNodes(nodes);
		graph.setAdjList(adjList);
		graph.setPreferredSize(new Dimension(960, 800));
		Thread graphThread = new Thread(graph);
		graphThread.start(); 
	}
	
	//for the new graphic (delete node 6)
	void executeDepthFirstSearchAlgbi(double width, double height, Map<Integer, Set<Integer>> adjList) {
		System.out.println("\nExecuting DFS Algorithm");
		//these have to be clear since they already have elements and values after running the algorithm
		s.clear();
		explored.clear();
		discovered.clear();
		parent.clear();
		//
		List<Set<Integer>> connectedNodes = new ArrayList<Set<Integer>>();
		for(int node: adjList.keySet()) {
			Set<Integer> connectedNode = new LinkedHashSet<Integer>();
			recursiveDFS(node, connectedNode, adjList);
			
			if(!connectedNode.isEmpty()) {
				connectedNodes.add(connectedNode);
			}
		}
		
		if(connectedNodes.size() == 1) {
			System.out.println("Graph is fully connected with one connected component.");
		} else {
			System.out.println("Graph is not fully connected");
		}
		
		System.out.println("There are " + connectedNodes.size() + " connected components");
		for(Set<Integer> list: connectedNodes) {
			System.out.println(list);
		}
		
		//Draw second sensor network graph
		SensorNetworkGraph graph = new SensorNetworkGraph(dataGens);
		graph.setGraphWidth(width);
		graph.setGraphHeight(height);
		//different graphic have to set different nodes
		graph.setNodes(nodes2);
		graph.setAdjList(adjList);
		graph.setPreferredSize(new Dimension(960, 800));
		Thread graphThread = new Thread(graph);
		graphThread.start(); 
	}

	void recursiveDFS(int u, Set<Integer> connectedNode, Map<Integer, Set<Integer>> adjList) {
		
		if(!s.contains(u) && !explored.containsKey(u)) {
			s.add(u);
			discovered.put(u, true);
		} 
		
			while(!s.isEmpty()) {
				if(!explored.containsKey(u)) {
					List<Integer> list = new ArrayList<Integer>(adjList.get(u));
					for(int v: list) {
						
						if(!discovered.containsKey(v)) {
							s.add(v);
							discovered.put(v, true);
							
							if(parent.get(v) == null) {
								parent.put(v, u);
							}
							recursiveDFS(v, connectedNode, adjList);
						} else if(list.get(list.size()-1) == v) {
							if( parent.containsKey(u)) {
								explored.put(u, true);
								s.removeElement(u);
								
								connectedNode.add(u);
								recursiveDFS(parent.get(u), connectedNode, adjList);
							}
						}
					}
				if(!explored.containsKey(u))
					explored.put(u, true);
					s.removeElement(u);
					connectedNode.add(u);
				}
			}
			
	}
	
	void populateNodes(int nodeCount, double width, double height) {
		Random random = new Random();
		
		for(int i = 1; i <= nodeCount; i++) {
			Axis axis = new Axis();
			int scale = (int) Math.pow(10, 1);
			double xAxis =(0 + random.nextDouble() * (width - 0));
			double yAxis = 0 + random.nextDouble() * (height - 0);
			
			xAxis = (double)Math.floor(xAxis * scale) / scale;
			yAxis = (double)Math.floor(yAxis * scale) / scale;
			
			axis.setxAxis(xAxis);
			axis.setyAxis(yAxis);
			
			nodes.put(i, axis);	
		}
	}
	
	void populateAdjacencyList(int nodeCount, int tr, Map<Integer, Set<Integer>> adjList) {
		for(int i=1; i<= nodeCount; i++) {
			adjList.put(i, new HashSet<Integer>());
		}
		
		for(int node1: nodes.keySet()) {
			Axis axis1 = nodes.get(node1);
			for(int node2: nodes.keySet()) {
				Axis axis2 = nodes.get(node2);
				
				if(node1 == node2) {
					continue;
				}
				double xAxis1 = axis1.getxAxis();
				double yAxis1 = axis1.getyAxis();
					
				double xAxis2 = axis2.getxAxis();
				double yAxis2 = axis2.getyAxis();
				
				double distance =  Math.sqrt(((xAxis1-xAxis2)*(xAxis1-xAxis2)) + ((yAxis1-yAxis2)*(yAxis1-yAxis2)));
				
				if(distance <= tr) {
					Set<Integer> tempList = adjList.get(node1);
					tempList.add(node2);
					adjList.put(node1, tempList);
						
					tempList = adjList.get(node2);
					tempList.add(node1);
					adjList.put(node2, tempList);
					if (node1>node2){
                        links.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1), distance, getCost(distance), 0));
                    } else {
                        links.put(new String("(" + node1 + ", " + node2 + ")"), new Link(new Edge(node1, node2), distance, getCost(distance), 0));
                    }
				}
			}
		}
	}
	
	//similar as populateAdjacencyList but the source nodes are different, only 5
	void checkbiconnect(int nodeCount, int tr, Map<Integer, Set<Integer>> adjList) {
		for(int i=1; i<= nodeCount; i++) {
			adjList.put(i, new HashSet<Integer>());
		}
		
		for(int node1: nodes2.keySet()) {
			Axis axis1 = nodes2.get(node1);
			for(int node2: nodes2.keySet()) {
				Axis axis2 = nodes2.get(node2);
				
				if(node1 == node2) {
					continue;
				}
				double xAxis1 = axis1.getxAxis();
				double yAxis1 = axis1.getyAxis();
					
				double xAxis2 = axis2.getxAxis();
				double yAxis2 = axis2.getyAxis();
				
				double distance =  Math.sqrt(((xAxis1-xAxis2)*(xAxis1-xAxis2)) + ((yAxis1-yAxis2)*(yAxis1-yAxis2)));
				
				if(distance <= tr) {
					Set<Integer> tempList = adjList.get(node1);
					tempList.add(node2);
					adjList.put(node1, tempList);
						
					tempList = adjList.get(node2);
					tempList.add(node1);
					adjList.put(node2, tempList);
					if (node1>node2){
                        links.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1), distance, getCost(distance), 0));
                    } else {
                        links.put(new String("(" + node1 + ", " + node2 + ")"), new Link(new Edge(node1, node2), distance, getCost(distance), 0));
                    }
				}
			}
		}
	}
}
