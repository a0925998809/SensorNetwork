import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.text.DecimalFormat;

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
	static Map<String, Link> linksamp1 = new HashMap<String, Link>();
	static Map<String, Link> linksamp10 = new HashMap<String, Link>();
	static Map<String, Link> linksamp1000 = new HashMap<String, Link>();
	static Map<String, Link> linksamp10000 = new HashMap<String, Link>();
	static Map<String, Link> linksamp1re = new HashMap<String, Link>();
	static Map<String, Link> linksamp10re = new HashMap<String, Link>();
	static Map<String, Link> linksamp1000re = new HashMap<String, Link>();
	static Map<String, Link> linksamp10000re = new HashMap<String, Link>();
	static ArrayList<Path> paths = new ArrayList<>();
	static ArrayList<Path> paths2 = new ArrayList<>();
	static ArrayList<Path> paths3 = new ArrayList<>();
	static ArrayList<Path> paths4 = new ArrayList<>();
	static ArrayList<Path> pathsex = new ArrayList<>();
	static ArrayList<Path> pathsamp1 = new ArrayList<>();
	static ArrayList<Path> pathsamp10 = new ArrayList<>();
	static ArrayList<Path> pathsamp1000 = new ArrayList<>();
	static ArrayList<Path> pathsamp10000 = new ArrayList<>();
	static ArrayList<Path> pathsamp1re = new ArrayList<>();
	static ArrayList<Path> pathsamp10re = new ArrayList<>();
	static ArrayList<Path> pathsamp1000re = new ArrayList<>();
	static ArrayList<Path> pathsamp10000re = new ArrayList<>();
    static int minCapacity;
    static int capacityRandomRange;
    static int biconnectcounter = 1;
    static int[] dataGens;
    static int[] storageNodes;
    static int[] dataGens2;
    static int[] storageNodes2;
    static int numberOfDG;
    static int numberOfDataItemsPerDG;
    static int numberOfNodes;
    static DecimalFormat fix = new DecimalFormat("##.########");
    
	public static void main(String[] args) throws IOException {

		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the width: (e.g.100)");
		//double width = scan.nextDouble();
        double width = 1000.0;

		System.out.println("Enter the height: (e.g.100)");
		//double height = scan.nextDouble();
        double height = 1000.0;

		System.out.println("Enter the number of nodes: (e.g.50)");
		//numberOfNodes = scan.nextInt();
        numberOfNodes = 50;

		System.out.println("Enter the Transmission range in meters: (e.g.30)");
		//int transmissionRange = scan.nextInt();
        int transmissionRange = 300;

		System.out.println("How many DGs(Data Generators)? (e.g.10)");
		//numberOfDG = scan.nextInt();
        numberOfDG = 10;

		dataGens = new int[numberOfDG];
		System.out.println("Assuming the first " + numberOfDG + " nodes are DGs\n");
		for (int i=1; i<=dataGens.length; i++) {
            dataGens[i-1] = i;
        }

        storageNodes = new int[numberOfNodes-numberOfDG];
        for (int i=0; i<storageNodes.length; i++){
            storageNodes[i] = i + 1 + numberOfDG;
        }


		System.out.println("How many data items per DG? (e.g.30)");
//		int numberOfDataItemsPerDG = scan.nextInt();
        numberOfDataItemsPerDG = 30;

        //System.out.println("Capacity Random Range per node up from the min capacity:(e.g.0)");
//		capacityRandomRange= scan.nextInt();
        capacityRandomRange= 0;

        int changenode = 0;
        
        //the head and tail of the target's path
        int dirhead = 0;
        int dirtail = 0;
        ArrayList<Integer> dirpath;
        
        
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
			System.out.println("Node:" + key + ", xAxis:" + ax.getxAxis() + ", yAxis:" + ax.getyAxis() + ", maxcapa:" + ax.getcapa());
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

		//sorting
		Map<String, Link> treeMap = new TreeMap<String, Link>(linkstest);
		
		//this link is to check duplicate nodes (only for printing information)
		//when using Dijkastra we need all link lists include duplicate links
		Map<String, Link> treeMap2 = new TreeMap<String, Link>(links);
		
		//create different link with different amp
		Map<String, Link> treeMapamp1capa = new TreeMap<String, Link>(linksamp1);
		Map<String, Link> treeMapamp10capa = new TreeMap<String, Link>(linksamp10);
		Map<String, Link> treeMapamp1000capa = new TreeMap<String, Link>(linksamp1000);
		Map<String, Link> treeMapamp10000capa = new TreeMap<String, Link>(linksamp10000);
		
		//setting capacity for the nodes (amp100)
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
		
		//setting capacity for the nodes (amp1)
		for (Link link : treeMapamp1capa.values()){
		    link.setCapacity(rand.nextInt(capacityRandomRange+1) + minCapacity);
		}
		//setting capacity for the nodes (amp10)
		for (Link link : treeMapamp10capa.values()){
		    link.setCapacity(rand.nextInt(capacityRandomRange+1) + minCapacity);
		}
		//setting capacity for the nodes (amp1000)
		for (Link link : treeMapamp1000capa.values()){
		    link.setCapacity(rand.nextInt(capacityRandomRange+1) + minCapacity);
		}
		//setting capacity for the nodes (amp10000)
		for (Link link : treeMapamp10000capa.values()){
		    link.setCapacity(rand.nextInt(capacityRandomRange+1) + minCapacity);
		}
		
        System.out.println();

        
       //building input files for dijkastra
        StringBuilder dijkastra_input = new StringBuilder();
		for (Link link: treeMap.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
		
		//building input files for dijkastra
        String fileName = "dijkastra_input.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(dijkastra_input.toString());
        writer.close();
        
        // Calling Dijkastra Algorithm
        WeighedDigraph graph;
        graph = new WeighedDigraph(fileName);

        DijkstraFind finder = new DijkstraFind(graph);
        
        
//first loop to generate the overall min cost flow
        
        System.out.print("Min Cost Flow Graph (original amp): Edge, Cost, Capacity\n"); //min capa 
        for(int dg: dataGens){
            for(int sn: storageNodes) {
                ArrayList<Integer> path = finder.shortestPath(dg, sn, numberOfDG);
                
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
                    
                    // can use treeMap (sorted)
                    Link link = linkstest.get("(" + tail + ", " + head + ")");

                    cost += link.getCost();
                    capacities.add(link.getCapacity());          
                }
            
                double capacity = 0;
                if (capacities != null) {
                	capacity = Collections.min(capacities);
                }
                
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
/*      
//test the min cost in different amp (use for testing)
        //example for amp10000
        //building input files for dijkastra
        StringBuilder dijkastra_input_ampex = new StringBuilder();
		for (Link link: treeMapamp1capa.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input_ampex.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
		
		//building input files for dijkastra
        String fileNameampex = "dijkastra_input_ampex.txt";
        BufferedWriter writerampex = new BufferedWriter(new FileWriter(fileNameampex));
        writerampex.write(dijkastra_input_ampex.toString());
        writerampex.close();
        
        // Calling Dijkastra Algorithm
        WeighedDigraph graphampex;
        graphampex = new WeighedDigraph(fileNameampex);

        DijkstraFind finderampex = new DijkstraFind(graphampex);
        
        System.out.print("Min Cost Flow Graph (amp10000): Edge, Cost, Capacity\n"); //min capa 
        for(int dg: dataGens){
            for(int sn: storageNodes) {
                ArrayList<Integer> path = finderampex.shortestPath(dg, sn, numberOfDG);
                
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
                    
                    Link link = linksamp10000.get("(" + tail + ", " + head + ")");

                    cost += link.getCost();
                    capacities.add(link.getCapacity());          
                }
            
                double capacity = 0;
                if (capacities != null) {
                	capacity = Collections.min(capacities);
                }
                
                Path newPath = new Path(path, cost, capacity);
                pathsex.add(newPath);
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
*/     

        System.out.println(); 
        System.out.println("Input the node which you want to change its cost: (must not be the data generaters (1 to 10 in this case))");
        changenode = scan.nextInt();
        //for(changenode = numberOfNodes-numberOfDG+1; changenode < numberOfNodes; changenode++) {
        	
        
//this loop is to generate the path information from the target node
        
        for(int dg: dataGens){
            for(int sn: storageNodes) {
                ArrayList<Integer> path = finder.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> path2 = null;
                double cost = 0;
                ArrayList<Double> capacities = new ArrayList<>();
                
                //get cost & capa
                for (int i = 1; i < path.size(); i++) {
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
                
                double capacity = 0;
                if (capacities != null) {
                	capacity = Collections.min(capacities);
                }
                
                for (int i = 1; i < path.size(); i++) {
                    if (path.get(i) == changenode){
                    	path2 = path;
                    	Path newPath2 = new Path(path2, cost, capacity);
                    	paths2.add(newPath2);
                    }
                 }                        
            }
        }
        
        int checkstorage = 0; //a token which check if the node is storage node (can't remove)
        ArrayList<Integer> checkpath = new ArrayList<>(); //the path which is not storage node will be in this list
        System.out.println("the path is:");
        //print selected path
        
        for (int i = 0; i <paths2.size(); i++) {
        	Path path = paths2.get(i);
        	int countpath = 0; //0 as the path is not storage node
        	
        	System.out.print(i + ". ");
        	System.out.println(path);
        	
        	for (int j = 1; j < path.getPath().size(); j++) {
        		if ((j+1 == path.getPath().size()) && (path.getPath().get(j) == changenode)) {
        			checkstorage ++;
        			countpath = 1;
        		}
        	}
        	
        	if (countpath == 0) {
        		checkpath.add(i);
        	}

        }
        
        //check if the node is storage node for all path
    	if(checkstorage == paths2.size()) {
    		System.out.println(changenode + " is storagenode!");
    		
    	}
    	
//if the node is not storage node use loop to run all usable path
    	
    	else {
    	
    	System.out.println();
    	System.out.println(checkpath + "can be used to calculate the payment");

        //run checkpath.size() to see all path
    	for(int a = 0; a < 1; a++ ) {
        
        int checkin = 0; //check if the path change after reporting fake cost 
        int checkinamp1 = 0;
        int checkinamp10 = 0;
        int checkinamp1000 = 0;
        int checkinamp10000 = 0;
        
        //initial the cost every time
        double Cvi = 0;
        double Cviamp1 = 0;
        double Cviamp10 = 0;
        double Cviamp1000 = 0;
        double Cviamp10000 = 0;
        double Cv = 0;
        double Cvamp1 = 0;
        double Cvamp10 = 0;
        double Cvamp1000 = 0;
        double Cvamp10000 = 0;
        double Ci = 0;
        double Ciamp1 = 0;
        double Ciamp10 = 0;
        double Ciamp1000 = 0;
        double Ciamp10000 = 0;
        double fakeCi = 0;
        double fakeCiamp1 = 0;
        double fakeCiamp10 = 0;
        double fakeCiamp1000 = 0;
        double fakeCiamp10000 = 0;
        double pay = 0;
        double payamp1 = 0;
        double payamp10 = 0;
        double payamp1000 = 0;
        double payamp10000 = 0;
        double ut =0;
        double utamp1 =0;
        double utamp10 =0;
        double utamp1000 =0;
        double utamp10000 =0;
        
         
    	System.out.println();
        System.out.println("calaulating path: " + checkpath.get(a));
        
        int change = checkpath.get(a);
        ArrayList<Integer> changepath = paths2.get(change).getPath();
        System.out.println(changepath);
        
        dirpath = changepath; //to record target path
        
        //use to calculate electricity cost
        double linkelec = 0;
        
        //need 5 since we need 5 link lists (amp 1, 10, 100, 1000, 10000)
        Map<String, Link> treeMap4 = new TreeMap<String, Link>(linkstest);
        Map<String, Link> treeMapamp1r = new TreeMap<String, Link>(linkstest);
        Map<String, Link> treeMapamp10r = new TreeMap<String, Link>(linkstest);
        Map<String, Link> treeMapamp1000r = new TreeMap<String, Link>(linkstest);
        Map<String, Link> treeMapamp10000r = new TreeMap<String, Link>(linkstest);
        
        //lists for the different amps (different fake cost)
        Map<String, Link> treeMapamp1 = new TreeMap<String, Link>(linksamp1);
        Map<String, Link> treeMapamp10 = new TreeMap<String, Link>(linksamp10);
        Map<String, Link> treeMapamp1000 = new TreeMap<String, Link>(linksamp1000);
        Map<String, Link> treeMapamp10000 = new TreeMap<String, Link>(linksamp10000);
        
        //coping the lists (don't want to change the original value)
        for (Map.Entry<String, Link> entry : treeMap.entrySet()) {
        	String k = entry.getKey();
        	Link v = entry.getValue();
        	treeMap4.put(k, v);
        	treeMapamp1r.put(k, v);
        	treeMapamp10r.put(k, v);
        	treeMapamp1000r.put(k, v);
        	treeMapamp10000r.put(k, v);
    	}
        for (Map.Entry<String, Link> entry : treeMapamp1capa.entrySet()) {
        	String k = entry.getKey();
        	Link v = entry.getValue();
        	treeMapamp1.put(k, v);
    	}
        for (Map.Entry<String, Link> entry : treeMapamp10capa.entrySet()) {
        	String k = entry.getKey();
        	Link v = entry.getValue();
        	treeMapamp10.put(k, v);
    	}
        for (Map.Entry<String, Link> entry : treeMapamp1000capa.entrySet()) {
        	String k = entry.getKey();
        	Link v = entry.getValue();
        	treeMapamp1000.put(k, v);
    	}
        for (Map.Entry<String, Link> entry : treeMapamp10000capa.entrySet()) {
        	String k = entry.getKey();
        	Link v = entry.getValue();
        	treeMapamp10000.put(k, v);
    	}
        
//this loop is to generate original total cost and adding the fake cost
        //find the paths which pass the target node
        ArrayList<Integer> findpath = paths2.get(change).getPath();
        for (int i=1;i < findpath.size();i++) {
        	int tail = 0, head = 0;
        	head = findpath.get(i);
        	tail = findpath.get(i-1);

        	if (tail == changenode) {
        		//when amp is 100 (original)
        		for (Link link : treeMap.values()){
        		    if (((link.getEdge().getHead() == head) && (link.getEdge().getTail() == tail))) {
        		    	System.out.println("Original amp(100)");
        		    	System.out.println("Original amp(100) edge:"+link.getEdge().toString());
        		    	System.out.println("Original cost (Ci) is: "+link.getCost());
        		    	Ci = link.getCost();
        		    	System.out.println("adding the cost (if doesn't change, the cost will remain same):");
        		    	//fakeCi = scan.nextDouble(); //user choose the fake cost
        		    	fakeCi = 0.0; //don't change (remain same cost)
        		   		
        		    	System.out.println("the cost is set to (fake cost Ci): "+ (link.getCost() + fakeCi));
        		   		treeMap4.put(new String("(" + tail + ", " + head + ")"), new Link(link.edge, link.distance, (link.cost + fakeCi), link.capacity));
        		   		treeMap4.put(new String("(" + head + ", " + tail + ")"), new Link(link.edge, link.distance, (link.cost + fakeCi), link.capacity));
        		   		
        		   		fakeCi = treeMap4.get("(" + tail + ", " + head +")").getCost();
        		    }
        		}
        		//when amp is 1
        		for (Link link : treeMap.values()){
    		    	if (((link.getEdge().getHead() == head) && (link.getEdge().getTail() == tail))) {
    		    		System.out.println("amp(1)");
    		    		System.out.println("Original cost (Ci amp1) is: " + link.getCost());
    		    		Ciamp1 = link.getCost();
    		    		fakeCiamp1 = treeMapamp1.get("(" + tail + ", " + head +")").getCost();
    		    		System.out.println("set the cost to: " + fakeCiamp1);
    		    		System.out.println("the cost is set to (fake cost Ci amp): "+ fakeCiamp1);
    		    		treeMapamp1r.put(new String("(" + tail + ", " + head + ")"), new Link(link.edge, link.distance, (fakeCiamp1), link.capacity));
    		    		treeMapamp1r.put(new String("(" + head + ", " + tail + ")"), new Link(link.edge, link.distance, (fakeCiamp1), link.capacity));
    		    	}
        		}
        		//when amp is 10
        		for (Link link : treeMap.values()){
    		    	if (((link.getEdge().getHead() == head) && (link.getEdge().getTail() == tail))) {
    		    		System.out.println("amp(10)");
    		    		System.out.println("Original cost (Ci amp10) is: "+link.getCost());
    		    		Ciamp10 = link.getCost();
    		    		fakeCiamp10 = treeMapamp10.get("(" + tail + ", " + head +")").getCost();
    		    		System.out.println("set the cost to: " + fakeCi);
    		    		System.out.println("the cost is set to (fake cost Ci amp10): "+ (fakeCiamp10));
    		    		treeMapamp10r.put(new String("(" + tail + ", " + head + ")"), new Link(link.edge, link.distance, (fakeCiamp10), link.capacity));
    		    		treeMapamp10r.put(new String("(" + head + ", " + tail + ")"), new Link(link.edge, link.distance, (fakeCiamp10), link.capacity));
    		    	}
        		}
        		//when amp is 1000
        		for (Link link : treeMap.values()){
    		    	if (((link.getEdge().getHead() == head) && (link.getEdge().getTail() == tail))) {
    		    		System.out.println("amp(1000)");
    		    		System.out.println("Original cost (Ci amp1000) is: "+link.getCost());
    		    		Ciamp1000 = link.getCost();
    		    		fakeCiamp1000 = treeMapamp1000.get("(" + tail + ", " + head +")").getCost();
    		    		System.out.println("set the cost to: " + fakeCiamp1000);
    		    		System.out.println("the cost is set to (fake cost Ci amp1000): "+ (fakeCiamp1000));
    		    		treeMapamp1000r.put(new String("(" + tail + ", " + head + ")"), new Link(link.edge, link.distance, (fakeCiamp1000), link.capacity));
    		    		treeMapamp1000r.put(new String("(" + head + ", " + tail + ")"), new Link(link.edge, link.distance, (fakeCiamp1000), link.capacity));
    		    	}
        		}
        		//when amp is 10000
        		for (Link link : treeMap.values()){
    		    	if (((link.getEdge().getHead() == head) && (link.getEdge().getTail() == tail))) {
    		    		System.out.println("amp(10000)");
    		    		System.out.println("Original cost (Ci amp10000) is: "+link.getCost());
    		    		Ciamp10000 = link.getCost();
    		    		fakeCiamp10000 = treeMapamp10000.get("(" + tail + ", " + head +")").getCost();
    		    		System.out.println("set the cost to: " + fakeCiamp10000);
    		    		System.out.println("the cost is set to (fake cost Ci amp10000): "+ (fakeCiamp10000));
    		    		treeMapamp10000r.put(new String("(" + tail + ", " + head + ")"), new Link(link.edge, link.distance, (fakeCiamp10000), link.capacity));
    		    		treeMapamp10000r.put(new String("(" + head + ", " + tail + ")"), new Link(link.edge, link.distance, (fakeCiamp10000), link.capacity));
    		    	}
        		}
        	}
        	//use to calculate energy cost for the node
        	if (tail == changenode) {
        		//when amp is 100 (original)
        		for (Link link : treeMap.values()) {
        		    if (((link.getEdge().getHead() == head) && (link.getEdge().getTail() == tail))) {
        		    	linkelec = link.getCost();
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
        
        //building new min cost path (after change the cost) for different amp needs different doc
        StringBuilder dijkastra_input3 = new StringBuilder();
		for (Link link: treeMap4.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input3.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
        StringBuilder dijkastra_input_amp1 = new StringBuilder();
		for (Link link: treeMapamp1r.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input_amp1.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
        StringBuilder dijkastra_input_amp10 = new StringBuilder();
		for (Link link: treeMapamp10r.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input_amp10.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
        StringBuilder dijkastra_input_amp1000 = new StringBuilder();
		for (Link link: treeMapamp1000r.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input_amp1000.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
        StringBuilder dijkastra_input_amp10000 = new StringBuilder();
		for (Link link: treeMapamp10000r.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input_amp10000.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
		
        String fileName3 = "dijkastra_input3.txt";
        BufferedWriter writer3 = new BufferedWriter(new FileWriter(fileName3));
        writer3.write(dijkastra_input3.toString());
        writer3.close();
        String fileNameamp1 = "dijkastra_input_amp1.txt";
        BufferedWriter writeramp1 = new BufferedWriter(new FileWriter(fileNameamp1));
        writeramp1.write(dijkastra_input_amp1.toString());
        writeramp1.close();
        String fileNameamp10 = "dijkastra_input_amp10.txt";
        BufferedWriter writeramp10 = new BufferedWriter(new FileWriter(fileNameamp10));
        writeramp10.write(dijkastra_input_amp10.toString());
        writeramp10.close();
        String fileNameamp1000 = "dijkastra_input_amp1000.txt";
        BufferedWriter writeramp1000 = new BufferedWriter(new FileWriter(fileNameamp1000));
        writeramp1000.write(dijkastra_input_amp1000.toString());
        writeramp1000.close();
        String fileNameamp10000 = "dijkastra_input_amp10000.txt";
        BufferedWriter writeramp10000 = new BufferedWriter(new FileWriter(fileNameamp10000));
        writeramp10000.write(dijkastra_input_amp10000.toString());
        writeramp10000.close();
        
        // Calling Dijkastra Algorithm
        WeighedDigraph graph3;
        graph3 = new WeighedDigraph(fileName3);
        WeighedDigraph graphamp1;
        graphamp1 = new WeighedDigraph(fileNameamp1);
        WeighedDigraph graphamp10;
        graphamp10 = new WeighedDigraph(fileNameamp10);
        WeighedDigraph graphamp1000;
        graphamp1000 = new WeighedDigraph(fileNameamp1000);
        WeighedDigraph graphamp10000;
        graphamp10000 = new WeighedDigraph(fileNameamp10000);

        DijkstraFind finder3 = new DijkstraFind(graph3);
        DijkstraFind finderamp1 = new DijkstraFind(graphamp1);
        DijkstraFind finderamp10 = new DijkstraFind(graphamp10);
        DijkstraFind finderamp1000 = new DijkstraFind(graphamp1000);
        DijkstraFind finderamp10000 = new DijkstraFind(graphamp10000);
     
//this loop is to calculate the new total cost (fake)
        
        for(int dg: dataGens){
            for(int sn: storageNodes) {
                ArrayList<Integer> path = finder3.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> pathamp1 = finderamp1.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> pathamp10 = finderamp10.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> pathamp1000 = finderamp1000.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> pathamp10000 = finderamp10000.shortestPath(dg, sn, numberOfDG);
                
                double cost = 0;
                double costamp1 = 0;
                double costamp10 = 0;
                double costamp1000 = 0;
                double costamp10000 = 0;
                
                ArrayList<Double> capacities = new ArrayList<>();
                ArrayList<Double> amp1capacities = new ArrayList<>();
                ArrayList<Double> amp10capacities = new ArrayList<>();
                ArrayList<Double> amp1000capacities = new ArrayList<>();
                ArrayList<Double> amp10000capacities = new ArrayList<>();
                
                //calculating to total cost and the capacity (amp100)
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
                //amp1
                for (int i = 1; i <pathamp1.size(); i++) {
                    int tail, head;
                    tail = pathamp1.get(i-1);
                    head = pathamp1.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = treeMapamp1r.get("(" + tail + ", " + head + ")");

                    costamp1 += link.getCost();
                    amp1capacities.add(link.getCapacity());          
                }
                //amp10
                for (int i = 1; i <pathamp10.size(); i++) {
                    int tail, head;
                    tail = pathamp10.get(i-1);
                    head = pathamp10.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = treeMapamp10r.get("(" + tail + ", " + head + ")");

                    costamp10 += link.getCost();
                    amp10capacities.add(link.getCapacity());          
                }
                //amp1000
                for (int i = 1; i <pathamp1000.size(); i++) {
                    int tail, head;
                    tail = pathamp1000.get(i-1);
                    head = pathamp1000.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = treeMapamp1000r.get("(" + tail + ", " + head + ")");

                    costamp1000 += link.getCost();
                    amp1000capacities.add(link.getCapacity());          
                }
                //amp10000
                for (int i = 1; i <pathamp10000.size(); i++) {
                    int tail, head;
                    tail = pathamp10000.get(i-1);
                    head = pathamp10000.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = treeMapamp10000r.get("(" + tail + ", " + head + ")");

                    costamp10000 += link.getCost();
                    amp10000capacities.add(link.getCapacity());          
                } 
            
                //calculate capa
                double capacity = 0;
                double amp1capacity = 0;
                double amp10capacity = 0;
                double amp1000capacity = 0;
                double amp10000capacity = 0;
                
                if (capacities != null) {
                	capacity = Collections.min(capacities);
                }
                if (amp1capacities != null) {
                	amp1capacity = Collections.min(amp1capacities);
                }
                if (amp10capacities != null) {
                	amp10capacity = Collections.min(amp10capacities);
                }
                if (amp1000capacities != null) {
                	amp1000capacity = Collections.min(amp1000capacities);
                }
                if (amp10000capacities != null) {
                	amp10000capacity = Collections.min(amp10000capacities);
                }
                
                Path newPath = new Path(path, cost, capacity);
                Path newPathamp1 = new Path(pathamp1, costamp1, amp1capacity);
                Path newPathamp10 = new Path(pathamp10, costamp10, amp10capacity);
                Path newPathamp1000 = new Path(pathamp1000, costamp1000, amp1000capacity);
                Path newPathamp10000 = new Path(pathamp10000, costamp10000, amp10000capacity);
                
                paths4.add(newPath);
                pathsamp1.add(newPathamp1);
                pathsamp10.add(newPathamp10);
                pathsamp1000.add(newPathamp1000);
                pathsamp10000.add(newPathamp10000);

                //adding new costs and paths, also check if the path has changed (the fake cost is too high, the path will change)
                for(int i=1; i < newPath.getPath().size(); i++) {
                	int tail = 0, head = 0;
                	head = newPath.getPath().get(i);
                	tail = newPath.getPath().get(i-1);
                	//checking if the path is the target path
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPath.getPath().size(); j++) {
                			head = 0;
                			head = newPath.getPath().get(j);
                			tail = newPath.getPath().get(j-1);

                			if (j == (newPath.getPath().size()-1) && (head == dirhead)) {
                				System.out.println();
                				System.out.println("the path with node " + changenode + " is:");
                				System.out.println(newPath);
                				//check weather the new path pass the target node or not
            					for (int k = 1; k < newPath.getPath().size() ; k++) {
            						if (newPath.getPath().get(k-1) == changenode) {
            							checkin = 1;
            						}         						
            					}
                				Cv = newPath.getCost();
                				System.out.println("its total cost (Cv) is: " + Cv);
                        	}
                		}
                	}
                }
                
                //amp1
                for(int i=1; i < newPathamp1.getPath().size(); i++) {
                	int tail = 0, head = 0, contsame = 1;
                	head = newPathamp1.getPath().get(i);
                	tail = newPathamp1.getPath().get(i-1);
                	//checking if the path is the target path
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPathamp1.getPath().size(); j++) {
                			head = 0;
                			head = newPathamp1.getPath().get(j);
                			tail = newPathamp1.getPath().get(j-1);

                			if (j == (newPathamp1.getPath().size()-1) && (head == dirhead)) {
 				
                				System.out.println("the path (amp1) with node " + changenode + " is:");
                				System.out.println(newPathamp1);
                				//check weather the new path pass the target node or not
                				for (int k = 1; k < newPathamp1.getPath().size() ; k++) {
                					if((newPathamp1.getPath().size() == newPath.getPath().size())) {
                						if ((newPathamp1.getPath().get(k-1) == newPath.getPath().get(k-1))) {
                							contsame = contsame+1;
                						} 
                						/*if ((newPathamp10000.getPath().get(k-1) == changenode)) {
                							checkinamp10000 = 1;
                						} */
                					}
                				}
                				if (contsame == newPathamp1.getPath().size()) {
                					checkinamp1 = 1;
                				}
                				Cvamp1 = newPathamp1.getCost();
                				System.out.println("its total cost (Cv amp1) is: " + Cvamp1);
                        	}
                		}
                	}
                }
                
                //amp10
                for(int i=1; i < newPathamp10.getPath().size(); i++) {
                	int tail = 0, head = 0, contsame = 1;
                	head = newPathamp10.getPath().get(i);
                	tail = newPathamp10.getPath().get(i-1);
                	//checking if the path is the target path
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPathamp10.getPath().size(); j++) {
                			head = 0;
                			head = newPathamp10.getPath().get(j);
                			tail = newPathamp10.getPath().get(j-1);

                			if (j == (newPathamp10.getPath().size()-1) && (head == dirhead)) {
 				
                				System.out.println("the path (amp10) with node " + changenode + " is:");
                				System.out.println(newPathamp10);
                				//check weather the new path pass the target node or not
                				for (int k = 1; k < newPathamp10.getPath().size() ; k++) {
                					if((newPathamp10.getPath().size() == newPath.getPath().size())) {
                						if ((newPathamp10.getPath().get(k-1) == newPath.getPath().get(k-1))) {
                							contsame = contsame+1;
                						} 
                						/*if ((newPathamp10000.getPath().get(k-1) == changenode)) {
                							checkinamp10000 = 1;
                						} */
                					}
                				}
                				if (contsame == newPathamp10.getPath().size()) {
                					checkinamp10 = 1;
                				}
                				Cvamp10 = newPathamp10.getCost();
                				System.out.println("its total cost (Cv amp10) is: " + Cvamp10);
                        	}
                		}
                	}
                }
                
                //amp1000
                for(int i=1; i < newPathamp1000.getPath().size(); i++) {
                	int tail = 0, head = 0, contsame = 1;
                	head = newPathamp1000.getPath().get(i);
                	tail = newPathamp1000.getPath().get(i-1);
                	//checking if the path is the target path
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPathamp1000.getPath().size(); j++) {
                			head = 0;
                			head = newPathamp1000.getPath().get(j);
                			tail = newPathamp1000.getPath().get(j-1);

                			if (j == (newPathamp1000.getPath().size()-1) && (head == dirhead)) {
 				
                				System.out.println("the path (amp1000) with node " + changenode + " is:");
                				System.out.println(newPathamp1000);
                				//check weather the new path pass the target node or not
                				for (int k = 1; k < newPathamp1000.getPath().size() ; k++) {
                					if((newPathamp1000.getPath().size() == newPath.getPath().size())) {
                						if ((newPathamp1000.getPath().get(k-1) == newPath.getPath().get(k-1))) {
                							contsame = contsame+1;
                						} 
                						/*if ((newPathamp10000.getPath().get(k-1) == changenode)) {
                							checkinamp10000 = 1;
                						} */
                					}
                				}
                				if (contsame == newPathamp1000.getPath().size()) {
                					checkinamp1000 = 1;
                				}
                				Cvamp1000 = newPathamp1000.getCost();
                				System.out.println("its total cost (Cv amp1000) is: " + Cvamp1000);
                        	}
                		}
                	}
                }
                
                //amp10000
                for(int i=1; i < newPathamp10000.getPath().size(); i++) {
                	int tail = 0, head = 0, contsame = 1;
                	head = newPathamp10000.getPath().get(i);
                	tail = newPathamp10000.getPath().get(i-1);
                	//checking if the path is the target path
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPathamp10000.getPath().size(); j++) {
                			head = 0;
                			head = newPathamp10000.getPath().get(j);
                			tail = newPathamp10000.getPath().get(j-1);

                			if (j == (newPathamp10000.getPath().size()-1) && (head == dirhead)) {
 				
                				System.out.println("the path (amp10000) with node " + changenode + " is:");
                				System.out.println(newPathamp10000);
                				//check weather the new path pass the target node or not
                				for (int k = 1; k < newPathamp10000.getPath().size() ; k++) {
                					if((newPathamp10000.getPath().size() == newPath.getPath().size())) {
                						if ((newPathamp10000.getPath().get(k-1) == newPath.getPath().get(k-1))) {
                							contsame = contsame+1;
                						} 
                						/*if ((newPathamp10000.getPath().get(k-1) == changenode)) {
                							checkinamp10000 = 1;
                						} */
                					}
                				}
                				if (contsame == newPathamp10000.getPath().size()) {
                					checkinamp10000 = 1;
                				}
                				Cvamp10000 = newPathamp10000.getCost();
                				System.out.println("its total cost (Cv amp10000) is: " + Cvamp10000);
                        	}
                		}
                	}
                }
            }
        }
        
//this loop is to remove nodes to calculate total cost without the target node
        
        //copy since don't want to remove to original nodes
        for (Map.Entry<Integer, Axis> entry : nodes.entrySet()) {
        	int k = entry.getKey();
        	Axis v = entry.getValue();
        	nodes2.put(k, v);
    	}
        
        //remove 
        nodes2.remove(changenode);
        Map<Integer, Set<Integer>> adjacencyList3 = new LinkedHashMap<Integer, Set<Integer>> (); 
        
        //populateAdjacencyList for removed nodes
        sensor.removenode(changenode, numberOfNodes, transmissionRange, adjacencyList3);
       
        //sort
        Map<String, Link> treeMap3 = new TreeMap<String, Link>(links3);
        Map<String, Link> treeMapamp1rr = new TreeMap<String, Link>(links3);
        Map<String, Link> treeMapamp10rr = new TreeMap<String, Link>(links3);
        Map<String, Link> treeMapamp1000rr = new TreeMap<String, Link>(links3);
        Map<String, Link> treeMapamp10000rr = new TreeMap<String, Link>(links3);
		
        //building new files for dijkastra input
        StringBuilder dijkastra_input2 = new StringBuilder();
		for (Link link: treeMap3.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input2.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
        StringBuilder dijkastra_input_reamp1 = new StringBuilder();
		for (Link link: treeMapamp1rr.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input_reamp1.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
        StringBuilder dijkastra_input_reamp10 = new StringBuilder();
		for (Link link: treeMapamp10rr.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input_reamp10.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
        StringBuilder dijkastra_input_reamp1000 = new StringBuilder();
		for (Link link: treeMapamp1000rr.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input_reamp1000.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
        StringBuilder dijkastra_input_reamp10000 = new StringBuilder();
		for (Link link: treeMapamp10000rr.values()){
		    Edge edge = link.getEdge();
		    dijkastra_input_reamp10000.append(edge.getTail()).append(' ').append(edge.getHead()).append(' ').append(link.getCost()).append("\n");
		}
		
        String fileName2 = "dijkastra_input2.txt";
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(fileName2));
        writer2.write(dijkastra_input2.toString());
        writer2.close();
        String fileNameamp1re = "dijkastra_input_reamp1.txt";
        BufferedWriter writeramp1re = new BufferedWriter(new FileWriter(fileNameamp1re));
        writeramp1re.write(dijkastra_input_reamp1.toString());
        writeramp1re.close();
        String fileNameamp10re = "dijkastra_input_reamp10.txt";
        BufferedWriter writeramp10re = new BufferedWriter(new FileWriter(fileNameamp10re));
        writeramp10re.write(dijkastra_input_reamp10.toString());
        writeramp10re.close();
        String fileNameamp1000re = "dijkastra_input_reamp1000.txt";
        BufferedWriter writeramp1000re = new BufferedWriter(new FileWriter(fileNameamp1000re));
        writeramp1000re.write(dijkastra_input_reamp1000.toString());
        writeramp1000re.close();
        String fileNameamp10000re = "dijkastra_input_reamp10000.txt";
        BufferedWriter writeramp10000re = new BufferedWriter(new FileWriter(fileNameamp10000re));
        writeramp10000re.write(dijkastra_input_reamp10000.toString());
        writeramp10000re.close();
        
        // Calling Dijkastra Algorithm
        WeighedDigraph graph2;
        graph2 = new WeighedDigraph(fileName2);
        WeighedDigraph graphamp1re;
        graphamp1re = new WeighedDigraph(fileNameamp1re);
        WeighedDigraph graphamp10re;
        graphamp10re = new WeighedDigraph(fileNameamp10re);
        WeighedDigraph graphamp1000re;
        graphamp1000re = new WeighedDigraph(fileNameamp1000re);
        WeighedDigraph graphamp10000re;
        graphamp10000re = new WeighedDigraph(fileNameamp10000re);

        DijkstraFind finder2 = new DijkstraFind(graph2);
        DijkstraFind finderamp1re = new DijkstraFind(graphamp1re);
        DijkstraFind finderamp10re = new DijkstraFind(graphamp10re);
        DijkstraFind finderamp1000re = new DijkstraFind(graphamp1000re);
        DijkstraFind finderamp10000re = new DijkstraFind(graphamp10000re);

        
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
        
        //start to calculate the cost without the target node
        for(int dg: dataGens){
            for(int sn: storageNodes2) {
                ArrayList<Integer> path = finder2.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> pathamp1 = finderamp1re.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> pathamp10 = finderamp10re.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> pathamp1000 = finderamp1000re.shortestPath(dg, sn, numberOfDG);
                ArrayList<Integer> pathamp10000 = finderamp10000re.shortestPath(dg, sn, numberOfDG);
                
                double cost = 0;
                double amp1cost = 0;
                double amp10cost = 0;
                double amp1000cost = 0;
                double amp10000cost = 0;
                double capacity = 0;
                double amp1capacity = 0;
                double amp10capacity = 0;
                double amp1000capacity = 0;
                double amp10000capacity = 0;
                
                ArrayList<Double> capacities = new ArrayList<>();
                ArrayList<Double> amp1capacities = new ArrayList<>();
                ArrayList<Double> amp10capacities = new ArrayList<>();
                ArrayList<Double> amp1000capacities = new ArrayList<>();
                ArrayList<Double> amp10000capacities = new ArrayList<>();
                
                //calculate cost and capa
                for (int i = 1; i <path.size(); i++) {
                    int tail, head;
                    tail = path.get(i-1);
                    head = path.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = treeMap3.get("(" + tail + ", " + head + ")");
                    cost += link.getCost();
                    capacities.add(link.getCapacity());          
                }
                //amp1
                for (int i = 1; i <pathamp1.size(); i++) {
                    int tail, head;
                    tail = pathamp1.get(i-1);
                    head = pathamp1.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = treeMapamp1rr.get("(" + tail + ", " + head + ")");
                    amp1cost += link.getCost();
                    amp1capacities.add(link.getCapacity());          
                }
                //amp10
                for (int i = 1; i <pathamp10.size(); i++) {
                    int tail, head;
                    tail = pathamp10.get(i-1);
                    head = pathamp10.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = treeMapamp10rr.get("(" + tail + ", " + head + ")");

                    amp10cost += link.getCost();
                    amp10capacities.add(link.getCapacity());          
                }
                //amp1000
                for (int i = 1; i <pathamp1000.size(); i++) {
                    int tail, head;
                    tail = pathamp1000.get(i-1);
                    head = pathamp1000.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = treeMapamp1000rr.get("(" + tail + ", " + head + ")");

                    amp1000cost += link.getCost();
                    amp1000capacities.add(link.getCapacity());          
                }
                //amp10000
                for (int i = 1; i <pathamp10000.size(); i++) {
                    int tail, head;
                    tail = pathamp10000.get(i-1);
                    head = pathamp10000.get(i);
                    if (tail > head){
                        int temp = tail;
                        tail = head;
                        head = temp;
                    }
                   
                    Link link = treeMapamp10000rr.get("(" + tail + ", " + head + ")");

                    amp10000cost += link.getCost();
                    amp10000capacities.add(link.getCapacity());          
                }
            
                
                if (capacities != null) {
                	capacity = Collections.min(capacities);
                }
                if (amp1capacities != null) {
                	amp1capacity = Collections.min(amp1capacities);
                }
                if (amp10capacities != null) {
                	amp10capacity = Collections.min(amp10capacities);
                }
                if (amp1000capacities != null) {
                	amp1000capacity = Collections.min(amp1000capacities);
                }
                if (amp10000capacities != null) {
                	amp10000capacity = Collections.min(amp10000capacities);
                }
                
                Path newPath = new Path(path, cost, capacity);
                Path newPathamp1 = new Path(pathamp1, amp1cost, amp1capacity);
                Path newPathamp10 = new Path(pathamp10, amp10cost, amp10capacity);
                Path newPathamp1000 = new Path(pathamp1000, amp1000cost, amp1000capacity);
                Path newPathamp10000 = new Path(pathamp10000, amp10000cost, amp10000capacity);
                
                paths3.add(newPath);
                pathsamp1re.add(newPathamp1);
                pathsamp10re.add(newPathamp10);
                pathsamp1000re.add(newPathamp1000);
                pathsamp10000re.add(newPathamp10000);
                
                //calculating total cost
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
                //amp1
                for(int i=1; i < newPathamp1.getPath().size(); i++) {
                	int tail = 0, head = 0;
                	head = newPathamp1.getPath().get(i);
                	tail = newPathamp1.getPath().get(i-1);
                	
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPathamp1.getPath().size(); j++) {
                			head = 0;
                			head = newPathamp1.getPath().get(j);
                			if (j == (newPathamp1.getPath().size()-1) && (head == dirhead)) {
                				System.out.println("the path (amp1) without node " + changenode + " is:");
                				System.out.println(newPathamp1);
                				Cviamp1 = newPathamp1.getCost();
                				System.out.println("its total cost (Cvi amp1) is: " + Cviamp1);
                        	}
                		}
                	}
                }
                //amp10
                for(int i=1; i < newPathamp10.getPath().size(); i++) {
                	int tail = 0, head = 0;
                	head = newPathamp10.getPath().get(i);
                	tail = newPathamp10.getPath().get(i-1);
                	
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPathamp10.getPath().size(); j++) {
                			head = 0;
                			head = newPathamp10.getPath().get(j);
                			if (j == (newPathamp10.getPath().size()-1) && (head == dirhead)) {
                				System.out.println("the path (amp10) without node " + changenode + " is:");
                				System.out.println(newPathamp10);
                				Cviamp10 = newPathamp10.getCost();
                				System.out.println("its total cost (Cvi amp10) is: " + Cviamp10);
                        	}
                		}
                	}
                }
                //amp1000
                for(int i=1; i < newPathamp1000.getPath().size(); i++) {
                	int tail = 0, head = 0;
                	head = newPathamp1000.getPath().get(i);
                	tail = newPathamp1000.getPath().get(i-1);
                	
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPathamp1000.getPath().size(); j++) {
                			head = 0;
                			head = newPathamp1000.getPath().get(j);
                			if (j == (newPathamp1000.getPath().size()-1) && (head == dirhead)) {
                				System.out.println("the path (amp1000) without node " + changenode + " is:");
                				System.out.println(newPathamp1000);
                				Cviamp1000 = newPathamp1000.getCost();
                				System.out.println("its total cost (Cvi amp1000) is: " + Cviamp1000);
                        	}
                		}
                	}
                }
                //amp10000
                for(int i=1; i < newPathamp10000.getPath().size(); i++) {
                	int tail = 0, head = 0;
                	head = newPathamp10000.getPath().get(i);
                	tail = newPathamp10000.getPath().get(i-1);
                	
                	if ((i == 1) && (tail == dirtail)) {
                		for(int j = 1; j < newPathamp10000.getPath().size(); j++) {
                			head = 0;
                			head = newPathamp10000.getPath().get(j);
                			if (j == (newPathamp10000.getPath().size()-1) && (head == dirhead)) {
                				System.out.println("the path (amp10000) without node " + changenode + " is:");
                				System.out.println(newPathamp10000);
                				Cviamp10000 = newPathamp10000.getCost();
                				System.out.println("its total cost (Cvi amp10000) is: " + Cviamp10000);
                        	}
                		}
                	}
                }
                
            }
        }

        pay = Cvi - (Cv - fakeCi);
        payamp1 = Cviamp1 - (Cvamp1 - fakeCiamp1);
        payamp10 = Cviamp10 - (Cvamp10 - fakeCiamp10);
        payamp1000 = Cviamp1000 - (Cvamp1000 - fakeCiamp1000);
        payamp10000 = Cviamp10000 - (Cvamp10000 - fakeCiamp10000);
        
        //payment will be 0 if the min cost flow choose different path
        if (checkin == 0) {
        	pay = 0.0;
        }
        if (checkinamp1 == 0) {
        	payamp1 = 0.0;
        }
        if (checkinamp10 == 0) {
        	payamp10 = 0.0;
        }
        if (checkinamp1000 == 0) {
        	payamp1000 = 0.0;
        }
        if (checkinamp10000 == 0) {
        	payamp10000 = 0.0;
        }
        
        ut = pay - Ci;
        utamp1 = payamp1- Ciamp1;
        utamp10 = payamp10 - Ciamp10;
        utamp1000 = payamp1000 - Ciamp1000;
        utamp10000 = payamp10000 - Ciamp10000;
        System.out.println();
        System.out.println("the following informaiton considers transfering one data item form the generater:");
        System.out.println("the payment for amp100 (Cvi-(Cv-fakeCi)) is:" + fix.format(pay));
        System.out.println("the payment for amp1 (Cvi-(Cv-fakeCi)) is:" + fix.format(payamp1));
        System.out.println("the payment for amp10 (Cvi-(Cv-fakeCi)) is:" + fix.format(payamp10));
        System.out.println("the payment for amp1000 (Cvi-(Cv-fakeCi)) is:" + fix.format(payamp1000));
        System.out.println("the payment for amp10000 (Cvi-(Cv-fakeCi)) is:" + fix.format(payamp10000));
        System.out.println();
        System.out.println("the utility amp100 (payment - its true cost) is:" + fix.format(ut));
        System.out.println("the utility amp1 (payment - its true cost) is:" + fix.format(utamp1));
        System.out.println("the utility amp10 (payment - its true cost) is:" + fix.format(utamp10));
        System.out.println("the utility amp1000 (payment - its true cost) is:" + fix.format(utamp1000));
        System.out.println("the utility amp10000 (payment - its true cost) is:" + fix.format(utamp10000));
        System.out.println();
        System.out.println("the electricity cost (receive + send) of this node is:" + fix.format(linkelec));
        System.out.println("the total energy cost of this path is:" + fix.format(Cv));
    	}//end for
    	}//end else
        

    	
/*      //for generating input file for min cost program

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
*/

      

	}

    double getCost(double l){
        final int K = 512; // k = 512B (from paper0)
        final double E_elec = 100 * Math.pow(10,-9); // E_elec = 100nJ/bit (from paper1)
        final double Epsilon_amp = 100 * Math.pow(10,-12); // Epsilon_amp = 100 pJ/bit/squared(m) (from paper1)
        double Etx = E_elec * K + Epsilon_amp * K * l * l; // Sending energy consumption
        double Erx = E_elec * K; // Receiving energy consumption
        return Math.round((Etx + Erx)*1000000)/100.0; // return the sum of sending and receiving energy
    }
    double getCost1(double l){
        final int K = 512; // k = 512B (from paper0)
        final double E_elec = 100 * Math.pow(10,-9); // E_elec = 100nJ/bit (from paper1)
        final double Epsilon_amp = 1 * Math.pow(10,-12); // Epsilon_amp = 1 pJ/bit/squared(m) (from paper1)
        double Etx = E_elec * K + Epsilon_amp * K * l * l; // Sending energy consumption
        double Erx = E_elec * K; // Receiving energy consumption
        return Math.round((Etx + Erx)*1000000)/100.0; // return the sum of sending and receiving energy
    }
    double getCost10(double l){
        final int K = 512; // k = 512B (from paper0)
        final double E_elec = 100 * Math.pow(10,-9); // E_elec = 100nJ/bit (from paper1)
        final double Epsilon_amp = 10 * Math.pow(10,-12); // Epsilon_amp = 10 pJ/bit/squared(m) (from paper1)
        double Etx = E_elec * K + Epsilon_amp * K * l * l; // Sending energy consumption
        double Erx = E_elec * K; // Receiving energy consumption
        return Math.round((Etx + Erx)*1000000)/100.0; // return the sum of sending and receiving energy
    }
    double getCost1000(double l){
        final int K = 512; // k = 512B (from paper0)
        final double E_elec = 100 * Math.pow(10,-9); // E_elec = 100nJ/bit (from paper1)
        final double Epsilon_amp = 1000 * Math.pow(10,-12); // Epsilon_amp = 1000 pJ/bit/squared(m) (from paper1)
        double Etx = E_elec * K + Epsilon_amp * K * l * l; // Sending energy consumption
        double Erx = E_elec * K; // Receiving energy consumption
        return Math.round((Etx + Erx)*1000000)/100.0; // return the sum of sending and receiving energy
    }
    double getCost10000(double l){
        final int K = 512; // k = 512B (from paper0)
        final double E_elec = 100 * Math.pow(10,-9); // E_elec = 100nJ/bit (from paper1)
        final double Epsilon_amp = 10000 * Math.pow(10,-12); // Epsilon_amp = 10000 pJ/bit/squared(m) (from paper1)
        double Etx = E_elec * K + Epsilon_amp * K * l * l; // Sending energy consumption
        double Erx = E_elec * K; // Receiving energy consumption
        return Math.round((Etx + Erx)*1000000)/100.0; // return the sum of sending and receiving energy
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
		
		/*
		//Draw first sensor network graph
		SensorNetworkGraph graph = new SensorNetworkGraph(dataGens);
		graph.setGraphWidth(width);
		graph.setGraphHeight(height);
		graph.setNodes(nodes);
		graph.setAdjList(adjList);
		graph.setPreferredSize(new Dimension(960, 800));
		Thread graphThread = new Thread(graph);
		graphThread.start();
		*/ 
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
		Random random = new Random(555);
		
		for(int i = 1; i <= nodeCount; i++) {
			Axis axis = new Axis();
			int scale = (int) Math.pow(10, 1);
			double xAxis =(0 + random.nextDouble() * (width - 0));
			double yAxis = 0 + random.nextDouble() * (height - 0);
			int capa = ((numberOfDG * numberOfDataItemsPerDG) / (numberOfNodes - numberOfDG)) + 10;
			
			xAxis = (double)Math.floor(xAxis * scale) / scale;
			yAxis = (double)Math.floor(yAxis * scale) / scale;
			
			
			axis.setxAxis(xAxis);
			axis.setyAxis(yAxis);
			axis.setcapa(capa);
			
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
					linksamp1.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getCost1(distance), 0));
					linksamp10.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getCost10(distance), 0));
					linksamp1000.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getCost1000(distance), 0));
					linksamp10000.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getCost10000(distance), 0));
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
		
		for(int node1: nodes2.keySet()) {
			Axis axis1 = nodes2.get(node1);
			for(int node2: nodes2.keySet()) {
				
				Axis axis2 = nodes2.get(node2);
				
				if((node1 == node2) || ((node1 <= numberOfDG) && (node2 <= numberOfDG))) {
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
	
	//use for removing nodes
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
					linksamp1re.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getCost1(distance), 0));
					linksamp10re.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getCost10(distance), 0));
					linksamp1000re.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getCost1000(distance), 0));
					linksamp10000re.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getCost10000(distance), 0));
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
