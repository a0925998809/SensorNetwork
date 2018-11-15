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
    static Map<Integer, Axis> nodes5 = new LinkedHashMap<Integer, Axis>();
	Map<Integer, Boolean> discovered = new HashMap<Integer, Boolean>();
	Map<Integer, Boolean> explored = new HashMap<Integer, Boolean>();
	Map<Integer, Integer> parent = new HashMap<Integer, Integer>();
	Map<Integer, Integer> connectedNodes = new HashMap<Integer, Integer>();
	Stack<Integer> s = new Stack<Integer>();
	static Map<String, Link> links = new HashMap<String, Link>();
	static Map<String, Link> links2 = new HashMap<String, Link>();
	static Map<String, Link> links3 = new HashMap<String, Link>();
	static Map<String, Link> linkstest = new HashMap<String, Link>();
	static ArrayList<Path> paths = new ArrayList<>();
	static ArrayList<Path> paths2 = new ArrayList<>();
	static ArrayList<Path> paths3 = new ArrayList<>();
	static ArrayList<Path> paths4 = new ArrayList<>();
    static int minCapacity;
    static int capacityRandomRange;
    static int biconnectcounter = 1;
    static int[] dataGens;
    static int[] storageNodes;
    static int[] dataGens2;
    static int[] storageNodes2;

	public static void main(String[] args) throws IOException {

		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the width: (e.g.100)");
		double width = scan.nextDouble();
        //double width = 150;

		System.out.println("Enter the height: (e.g.100)");
		double height = scan.nextDouble();
        //double height = 150;

		System.out.println("Enter the number of nodes: (e.g.50)");
		int numberOfNodes = scan.nextInt();
        //int numberOfNodes = 50;

		System.out.println("Enter the Transmission range in meters: (e.g.30)");
		int transmissionRange = scan.nextInt();
        //int transmissionRange = 10;

		System.out.println("How many DGs(Data Generators)? (e.g.10)");
		int numberOfDG = scan.nextInt();
        //int numberOfDG = 10;

		dataGens = new int[numberOfDG];
		System.out.println("Assuming the first " + numberOfDG + " nodes are DGs\n");
		for (int i=1; i<=dataGens.length; i++) {
            dataGens[i-1] = i;
        }

        storageNodes = new int[numberOfNodes-numberOfDG];
        for (int i=0; i<storageNodes.length; i++){
            storageNodes[i] = i + 1 + numberOfDG;
        }
        /*
		dataGens2 = new int[numberOfDG];
		System.out.println("Assuming the first " + numberOfDG + " nodes are DGs\n");
			
		for (int i = 1; i<=dataGens2.length; i++) {
            	dataGens2[i-1] = i;
        }
        */
        
        /*
        storageNodes2 = new int[numberOfNodes-numberOfDG-1]; 
        //value q is the value match with the nodes2 (after delete node)
        int q = 0; 
        for (int i=0; i<storageNodes2.length; i++){
            if(q != 0) {
            	storageNodes2[i] = q + 1 + numberOfDG;
            	q++;
            } else {
            	q++;
            	i = i - 1;
            }
        }*/

		System.out.println("How many data items per DG? (e.g.30)");
//		int numberOfDataItemsPerDG = scan.nextInt();
        int numberOfDataItemsPerDG = 30;

        System.out.println("Capacity Random Range per node up from the min capacity:(e.g.0)");
//		capacityRandomRange= scan.nextInt();
        capacityRandomRange= 0;
        
        int changenode = 0;
        int dirhead = 0;
        int dirtail = 0;
        double Cvi = 0;
        double Cv = 0;
        double Ci = 0;
        double fakeCi = 0;
        double pay = 0;
        double ut =0;
        
        
		int numberOfSupDem = numberOfDataItemsPerDG * numberOfDG;
        System.out.println("The total number of data items in supply/demand: " + numberOfSupDem);
        

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
		System.out.println("\nOriginal Graph:");
		sensor.executeDepthFirstSearchAlg(width, height, adjacencyList1);
        System.out.println();
		
		//test if the graphic is bi-connect
		//System.out.println("\nExecuting DFS Algorithm");
		for (int i = 1; i <= numberOfNodes; i++) {
			for (Map.Entry<Integer, Axis> entry : nodes.entrySet()) {
            	int k = entry.getKey();
            	Axis v = entry.getValue();
            	nodes2.put(k, v);
        	}
			nodes2.remove(i);
			Map<Integer, Set<Integer>> adjacencyList2 = new LinkedHashMap<Integer, Set<Integer>> ();
			System.out.println("\nremoving node:"+ i);
			sensor.checkbiconnect(i ,numberOfNodes, transmissionRange, adjacencyList2);
			System.out.println("Executing DFS Algorithm");
			sensor.executeDepthFirstSearchAlgbi(width, height, adjacencyList2);
		}
		
		if(biconnectcounter == 1) {
			System.out.println("\nAll of the Graph is fully connected!");
		} else {
			System.out.println("\nSome Graph is not fully connected!!");
			return;
		}

		//sort
		Map<String, Link> treeMap = new TreeMap<String, Link>(linkstest);
		Map<String, Link> treeMap2 = new TreeMap<String, Link>(links);

        System.out.println("\nSensor Network Edges with Distance, Cost and Capacity");
		for (Link link : treeMap.values()){
		    link.setCapacity(rand.nextInt(capacityRandomRange+1) + minCapacity);
		    for (Link innerlink : treeMap2.values()) {
		    	if ((innerlink.getEdge().getHead() == link.getEdge().getHead()) &&(innerlink.getEdge().getTail() == link.getEdge().getTail())) {
		    		innerlink.setCapacity(link.getCapacity());
		    		System.out.println(innerlink.toString());
		    	}
		    }
		}
        System.out.println();

        
 
        StringBuilder dijkastra_input = new StringBuilder();
		for (Link link: treeMap.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
		
		//System.out.print(dijkastra_input);
        String fileName = "dijkastra_input.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(dijkastra_input.toString());
        writer.close();
        
        // Calling Dijkastra Algorithm
        WeighedDigraph graph;
        graph = new WeighedDigraph(fileName);
        //System.out.print(graph);

        DijkstraFind finder = new DijkstraFind(graph);
        //Dijkstras finder2 = new Dijkstras();
     
        System.out.print("Min Cost Flow Graph: Edge, Cost, Capacity\n"); //min capa 
        for(int dg: dataGens){
            for(int sn: storageNodes) {
                ArrayList<Integer> path = finder.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> path2 = null;
                double cost = 0;
                ArrayList<Double> capacities = new ArrayList<>();
                for (int i = 1; i <path.size(); i++) {
                    int tail, head;
                    tail = path.get(i-1);
                    head = path.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = linkstest.get("(" + tail + ", " + head + ")");

                    cost += link.getCost();
                    capacities.add(link.getCapacity());          
                }
                //System.out.println(path);
            
                double capacity = 0;
                if (capacities != null) {
                	capacity = Collections.min(capacities);
                }
                              
               /*
                if (path.size() == 3) {
                	withcost = cost;
                }*/    
                
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
        
        System.out.println("Input the node which you want to change its cost:");
        changenode = scan.nextInt();
        for(int dg: dataGens){
            for(int sn: storageNodes) {
                ArrayList<Integer> path = finder.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> path2 = null;
                double cost = 0;
                ArrayList<Double> capacities = new ArrayList<>();
                for (int i = 1; i <path.size(); i++) {
                    int tail, head;
                    tail = path.get(i-1);
                    head = path.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = linkstest.get("(" + tail + ", " + head + ")");

                    cost += link.getCost();
                    capacities.add(link.getCapacity());          
                }
                //System.out.println(path);
            
                double capacity = 0;
                if (capacities != null) {
                	capacity = Collections.min(capacities);
                }
                
                for (int i = 1; i <path.size(); i++) {
                    int ele;
                    ele = path.get(i);
                    if (ele == changenode){
                    	path2 = path;
                    	Path newPath2 = new Path(path2, cost, capacity);
                    	paths2.add(newPath2);
                    }
                 }              
               /*
                if (path.size() == 3) {
                	withcost = cost;
                }*/           
            }
        }
        
        System.out.println("the path is:");
        //print selected path
        for (int i = 1; i <paths2.size(); i++) {
        	Path path = paths2.get(i);

        	System.out.print(i + ". ");
        	System.out.println(path);
        }
        System.out.println("input the path which you want to change");
        
        int change = scan.nextInt();
        ArrayList<Integer> changepath = paths2.get(change).getPath();
        System.out.println(changepath);
        
        Map<String, Link> treeMap4 = new TreeMap<String, Link>(linkstest);;
        
        for (Map.Entry<String, Link> entry : treeMap.entrySet()) {
        	String k = entry.getKey();
        	Link v = entry.getValue();
        	treeMap4.put(k, v);
    	}
        //Cv = paths2.get(change).getCost();
        //System.out.println("the total cost pass node " + changenode + " (Cv) is: " + Cv);
        ArrayList<Integer> findpath = paths2.get(change).getPath();
        for (int i=1;i < findpath.size();i++) {
        	int tail = 0, head = 0;
        	head = findpath.get(i);
        	tail = findpath.get(i-1);
        	if (head == changenode) {
        		for (Link link : treeMap.values()){
        		    	if (((link.getEdge().getHead() == head) && (link.getEdge().getTail() == tail))) {
        		    		System.out.println("Original cost (Ci) is: "+link.getCost());
        		    		Ci = link.getCost();
        		    		System.out.println("adding the cost:");
        		    		fakeCi = scan.nextDouble();
        		    		System.out.println("the cost is set to (fake cost Ci): "+ (link.getCost() + fakeCi));
        		    		treeMap4.put(new String("(" + tail + ", " + head + ")"), new Link(link.edge, link.distance, (link.cost + fakeCi), link.capacity));
        		    		treeMap4.put(new String("(" + head + ", " + tail + ")"), new Link(link.edge, link.distance, (link.cost + fakeCi), link.capacity));
        		    		/*for (Link innerlink : treeMap4.values()){
        		    			int innertail = innerlink.getEdge().getTail();
        		    			int innerhead = innerlink.getEdge().getHead();
        		    			if ((innerlink.getEdge().getHead() == head)) {
        		    				treeMap4.put(new String("(" + innertail + ", " + innerhead + ")"), new Link(innerlink.edge, innerlink.distance, (innerlink.cost + fakeCi), innerlink.capacity));
        		    			}
        		    		}*/
        		    	}
        		}	
        	}
        	if (i == 1) {
        		dirtail = tail;
        	}
        	if (i == (findpath.size()-1)) {
        		dirhead = head;
        	}
        }
        
        //check cost change
       /* for (Link link : treeMap4.values()) {
        	System.out.println(link.toString());
		}*/
        //building new min cost path
        StringBuilder dijkastra_input3 = new StringBuilder();
		for (Link link: treeMap4.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input3.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
		
		//System.out.print(dijkastra_input);
        String fileName3 = "dijkastra_input3.txt";
        BufferedWriter writer3 = new BufferedWriter(new FileWriter(fileName3));
        writer3.write(dijkastra_input3.toString());
        writer3.close();
        
        // Calling Dijkastra Algorithm
        WeighedDigraph graph3;
        graph3 = new WeighedDigraph(fileName3);
        //System.out.print(graph);

        DijkstraFind finder3 = new DijkstraFind(graph3);
        //Dijkstras finder2 = new Dijkstras();
     

        for(int dg: dataGens){
            for(int sn: storageNodes) {
                ArrayList<Integer> path = finder3.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> path2 = null;
                double cost = 0;
                ArrayList<Double> capacities = new ArrayList<>();
                for (int i = 1; i <path.size(); i++) {
                    int tail, head;
                    tail = path.get(i-1);
                    head = path.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = treeMap4.get("(" + tail + ", " + head + ")");

                    cost += link.getCost();
                    capacities.add(link.getCapacity());          
                }
                //System.out.println(path);
            
                double capacity = 0;
                if (capacities != null) {
                	capacity = Collections.min(capacities);
                }  
                Path newPath = new Path(path, cost, capacity);
                paths4.add(newPath);
                //System.out.println(newPath);
                for(int i=1; i < newPath.getPath().size(); i++) {
                	int tail = 0, head = 0;
                	head = newPath.getPath().get(i);
                	tail = newPath.getPath().get(i-1);
                	
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPath.getPath().size(); j++) {
                			head = 0;
                			head = newPath.getPath().get(j);
                			if (j == (newPath.getPath().size()-1) && (head == dirhead)) {
                				System.out.println("the path with node " + changenode + " is:");
                				System.out.println(newPath);
                				Cv = newPath.getCost();
                				System.out.println("its total cost (Cv) is: " + Cv);
                        	}
                		}
                	}
                }
            }
        }
        
        for (Map.Entry<Integer, Axis> entry : nodes.entrySet()) {
        	int k = entry.getKey();
        	Axis v = entry.getValue();
        	nodes2.put(k, v);
    	}
        nodes2.remove(changenode);
        Map<Integer, Set<Integer>> adjacencyList3 = new LinkedHashMap<Integer, Set<Integer>> (); 
        sensor.removenode(changenode, numberOfNodes, transmissionRange, adjacencyList3);
       
        Map<String, Link> treeMap3 = new TreeMap<String, Link>(links3);
        //check path
		/*for (Link link : treeMap3.values()) {
			System.out.println(link.toString());
		}*/
		
        //checking new graph with new cost
        StringBuilder dijkastra_input2 = new StringBuilder();
		for (Link link: treeMap3.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input2.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
		
		//System.out.print(dijkastra_input);
        String fileName2 = "dijkastra_input2.txt";
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(fileName2));
        writer2.write(dijkastra_input2.toString());
        writer2.close();
        
        // Calling Dijkastra Algorithm
        WeighedDigraph graph2;
        graph2 = new WeighedDigraph(fileName2);
        //System.out.print(graph);

        DijkstraFind finder2 = new DijkstraFind(graph2);
        //Dijkstras finder2 = new Dijkstras();
        
        //since one storage node is removed, need to create new a storage node set
        storageNodes2 = new int[numberOfNodes-numberOfDG-1]; 
        //value q is the value match with the nodes2 (after delete node)
        int q = 0; 
        for (int i=0; i<storageNodes2.length; i++){
            if(q != (changenode-numberOfDG-1)) {
            	storageNodes2[i] = q + 1 + numberOfDG;
            	q++;
            } else {
            	q++;
            	i = i - 1;
            }
        }
        
        
        for(int dg: dataGens){
            for(int sn: storageNodes2) {
                ArrayList<Integer> path = finder2.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> path2 = null;
                double cost = 0;
                ArrayList<Double> capacities = new ArrayList<>();
                for (int i = 1; i <path.size(); i++) {
                    int tail, head;
                    tail = path.get(i-1);
                    head = path.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = linkstest.get("(" + tail + ", " + head + ")");

                    cost += link.getCost();
                    capacities.add(link.getCapacity());          
                }
                //System.out.println(path);
            
                double capacity = 0;
                if (capacities != null) {
                	capacity = Collections.min(capacities);
                }
                Path newPath = new Path(path, cost, capacity);
                paths3.add(newPath);
                for(int i=1; i < newPath.getPath().size(); i++) {
                	int tail = 0, head = 0;
                	head = newPath.getPath().get(i);
                	tail = newPath.getPath().get(i-1);
                	
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPath.getPath().size(); j++) {
                			head = 0;
                			head = newPath.getPath().get(j);
                			if (j == (newPath.getPath().size()-1) && (head == dirhead)) {
                				System.out.println("the path without node " + changenode + " is:");
                				System.out.println(newPath);
                				Cvi = newPath.getCost();
                				System.out.println("its total cost (Cvi) is: " + Cvi);
                        	}
                		}
                	}
                }
                //System.out.println(newPath);
                /*for (int i = 1; i <path.size(); i++) {
                    int ele;
                    ele = path.get(i);
                    if (ele == changenode){
                    	path2 = path;
                    	Path newPath2 = new Path(path2, cost, capacity);
                    	paths3.add(newPath2);
                    }
                 }  */              
            }
        }
        pay = Cvi - (Cv - fakeCi);
        ut = pay- Ci;
        System.out.println("the payment (Cvi-(Cv-fakeCi)) is:" + pay);
        System.out.println("the utility (payment - its true cost) is:" + ut);
        /*//print selected path
        for (int i = 1; i <paths3.size(); i++) {
        	Path path = paths3.get(i);

        	System.out.print(i + ". ");
        	System.out.println(path);
        }*/
        
        
 /*       
        System.out.print("total cost Cv: ");
        System.out.println(withcost);
        System.out.print("cost Ci: ");
        System.out.println(Ci);
        System.out.println();
*/
 /*       
        System.out.println("this time node 3 reports fake cost:");
        System.out.println("Sensor Network Edges with Distance, Cost and Capacity");
		for (Link link : links.values()){
		    link.setCapacity(rand.nextInt(capacityRandomRange+1) + minCapacity);
		    Edge edge = link.getEdge();
		    if (edge.getTail() == 3 && edge.getHead() == 6) {
		    	System.out.println("node 3 reports fake cost as 3");
		    	links.put(new String("(" + edge.getTail() + ", " + edge.getHead() + ")"), new Link(new Edge(edge.getTail(), edge.getHead()), link.getDistance() , link.getCost() + 1.09E-4, link.getCapacity()));
		    	//System.out.println(links);
		    	System.out.println(links.get("(3, 6)"));
		    }
		    System.out.println(link.toString());
		}
        System.out.println();
        
        
        StringBuilder dijkastra_input3 = new StringBuilder();
		for (Link link: links.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input3.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		    
		    if (edge.getTail() == 3 && edge.getHead() == 6) {
			 Ci = link.getCost();
		    }
		}
		
		
        String fileName3 = "dijkastra_input3.txt";
        BufferedWriter writer3 = new BufferedWriter(new FileWriter(fileName3));
        writer3.write(dijkastra_input3.toString());

        writer3.close();

        // Calling Dijkastra Algorithm
        WeighedDigraph graph3;
        graph3 = new WeighedDigraph(fileName3);


        DijkstraFind finder3 = new DijkstraFind(graph3);
        
        System.out.print("Min Cost Flow Graph: Edge, Cost, Capacity\n"); //min capa 
        for(int dg: dataGens){
            for(int sn: storageNodes) {
                ArrayList<Integer> path = finder3.shortestPath(dg, sn);
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
                
                
                double capacity = 0;
                if (capacities != null) {
                	capacity = Collections.min(capacities);
                } 
                
                if (path.size() == 3) {
                	withcost = cost;
                }
                
                Path newPath = new Path(path, cost, capacity);
                paths3.add(newPath);
                System.out.println(newPath);
            }
        }

        for (int i = 0; i < dataGens.length; i++) {
            ArrayList<Integer> dummyArrList = new ArrayList<>();
            dummyArrList.add(0);
            dummyArrList.add(dataGens[i]);
            Path newPath = new Path(dummyArrList, 0, numberOfDataItemsPerDG);
            paths3.add(newPath);
            System.out.println(newPath);
        }

        for (int i = 0; i < storageNodes.length; i++) {
            ArrayList<Integer> dummyArrList = new ArrayList<>();
            dummyArrList.add(storageNodes[i]);
            dummyArrList.add(numberOfNodes+1);
            Path newPath = new Path(dummyArrList, 0, minCapacity);
            paths3.add(newPath);
            System.out.println(newPath);
        }
        
        System.out.print("total cost Cv: ");
        System.out.println(withcost);
        System.out.print("cost Ci: ");
        System.out.println(Ci);
        System.out.println();

        StringBuilder output = new StringBuilder();

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
        System.out.println();
        System.out.println("Generated Input file for cs2-4.6 program:(pls refer to ourinput.inp in the folder");
        System.out.println(output);

        fileName = "outinput.inp";
        writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(output.toString());

        writer.close();
        
/*        
        
        //start testing if the net work is bi-conneceted
        System.out.println("to test the network is bi-connected, delete one of the nodes");
        
        //copy the nodes because directly removing the original nodes will influence the graphic
        for (Map.Entry<Integer, Axis> entry : nodes.entrySet()) {
            nodes2.put(entry.getKey(), entry.getValue());
        }
        //remove node 6
        nodes2.remove(3);
        
        //print the node list
		for(int key :sensor.nodes2.keySet()) {
			Axis ax = sensor.nodes2.get(key);
			System.out.println("Node:" + key + ", xAxis:" + ax.getxAxis() + ", yAxis:" + ax.getyAxis());
		}
		
		minCapacity = totalNumberOfData / numberOfStorageNodes;
		
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
        
        
        System.out.println("Sensor Network Edges with Distance, Cost and Capacity");
		for (Link link : links2.values()){
		    link.setCapacity(rand.nextInt(capacityRandomRange+1) + minCapacity);
		    System.out.println(link.toString());
        }
        System.out.println();
        
        StringBuilder dijkastra_input2 = new StringBuilder();
		for (Link link: links2.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input2.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
        }


        String fileName2 = "dijkastra_input2.txt";
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(fileName2));
        writer2.write(dijkastra_input2.toString());

        writer2.close();

        // Calling Dijkastra Algorithm
        WeighedDigraph graph2;
        graph2 = new WeighedDigraph(fileName2);


        DijkstraFind finder2 = new DijkstraFind(graph2);
        
        System.out.print("Min Cost Flow Graph: Edge, Cost, Capacity\n"); //min capa 
        for(int dg: dataGens2){
            for(int sn: storageNodes2) {
            	
            	ArrayList<Integer> path = finder2.shortestPath(dg, sn);
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
                    Link link = links2.get("(" + tail + ", " + head + ")");
                    cost += link.getCost();
                    capacities.add(link.getCapacity());
                }
                double capacity = Collections.min(capacities);
                
                if (path.size() == 3) {
                	totalcost = cost;
                }
                
                Path newPath = new Path(path, cost, capacity);
                paths2.add(newPath);
                System.out.println(newPath);
            }
        }
        System.out.print("total cost Cv-i:");
        System.out.println(totalcost);
        System.out.print("node 6 report its Ci as:");
        fuCi = 2;
        System.out.println(fuCi);
        System.out.print("pay:");
        System.out.println(totalcost - withcost + Ci - Ci);
 */   
        /*for (int i = 0; i < dataGens2.length; i++) {
            ArrayList<Integer> dummyArrList = new ArrayList<>();
            dummyArrList.add(0);
            dummyArrList.add(dataGens2[i]);
            Path newPath = new Path(dummyArrList, 0, numberOfDataItemsPerDG);
            paths2.add(newPath);
            System.out.println(newPath);
        }

        for (int i = 0; i < storageNodes2.length; i++) {
            ArrayList<Integer> dummyArrList = new ArrayList<>();
            dummyArrList.add(storageNodes2[i]);
            dummyArrList.add(numberOfNodes+1);
            Path newPath = new Path(dummyArrList, 0, minCapacity);
            paths2.add(newPath);
            System.out.println(newPath);
        }*/

       // System.out.println();

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
	
	//for the new graphic (delete nodes to test)
	void executeDepthFirstSearchAlgbi(double width, double height, Map<Integer, Set<Integer>> adjList) {
		//System.out.println("\nExecuting DFS Algorithm");
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
			biconnectcounter = biconnectcounter + 1;
			System.out.println("Graph is not fully connected");
		}
		
		System.out.println("There are " + connectedNodes.size() + " connected components");
		for(Set<Integer> list: connectedNodes) {
			System.out.println(list);
		}
		
		/*
		//Draw second sensor network graph
		SensorNetworkGraph2 graph = new SensorNetworkGraph2(dataGens2);
		graph.setGraphWidth(width);
		graph.setGraphHeight(height);
		//different graphic have to set different nodes
		graph.setNodes(nodes2);
		graph.setAdjList(adjList);
		graph.setPreferredSize(new Dimension(960, 800));
		Thread graphThread = new Thread(graph);
		graphThread.start(); 
		*/
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
		Random random = new Random(570);
		
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
					linkstest.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getCost(distance), 0));
					Set<Integer> tempList = adjList.get(node1);
					tempList.add(node2);
					adjList.put(node1, tempList);
						
					tempList = adjList.get(node2);
					tempList.add(node1);
					adjList.put(node2, tempList);
					if (node1 > node2){
                        links.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 1), distance, getCost(distance), 0));
					} else {
                    	links.put(new String("(" + node1 + ", " + node2 + ")"), new Link(new Edge(node1, node2, 1), distance, getCost(distance), 0));
					}
					
		
				}
			}
		}
	}
	
	//similar as populateAdjacencyList but the number of source nodes are different
	void checkbiconnect(int removeconter,int nodeCount, int tr, Map<Integer, Set<Integer>> adjList) {
		int j = 1;
		for(int i=1; i < nodeCount; i++) {
			if (j != removeconter) {
				adjList.put(j, new HashSet<Integer>());
				j++;
			} else {
				j++;
				i=i-1;
			}
		}
		//System.out.println(adjList.toString());
		
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
					if (node1 > node2){
						links2.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 1), distance, getCost(distance), 0));
                    } else {
                    	links2.put(new String("(" + node1 + ", " + node2 + ")"), new Link(new Edge(node1, node2, 1), distance, getCost(distance), 0));
                    }
				}
			}
		}
	}
	void removenode(int removeconter,int nodeCount, int tr, Map<Integer, Set<Integer>> adjList) {
		int j = 1;
		for(int i=1; i < nodeCount; i++) {
			if (j != removeconter) {
				adjList.put(j, new HashSet<Integer>());
				j++;
			} else {
				j++;
				i=i-1;
			}
		}
		//System.out.println(adjList.toString());
		
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
					links3.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getCost(distance), 0));
					Set<Integer> tempList = adjList.get(node1);
					tempList.add(node2);
					adjList.put(node1, tempList);
						
					tempList = adjList.get(node2);
					tempList.add(node1);
					adjList.put(node2, tempList);
					if (node1 > node2){
						links2.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 1), distance, getCost(distance), 0));
                    } else {
                    	links2.put(new String("(" + node1 + ", " + node2 + ")"), new Link(new Edge(node1, node2, 1), distance, getCost(distance), 0));
                    }
				}
			}
		}
	}
}
