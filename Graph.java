/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.*;

// Used to signal violations of preconditions for
// various shortest path algorithms.
class GraphException extends RuntimeException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GraphException( String name )
    {
        super( name );
    }
}

// Represents an edge in the graph.
class Edge
{
    public Vertex     dest;   // Second vertex in Edge
    public double     cost;   // Edge cost
    
    public Edge( Vertex d, double c )
    {
        dest = d;
        cost = c;
    }
}

// Represents an entry in the priority queue for Dijkstra's algorithm.
class Path implements Comparable<Path>
{
    public Vertex     dest;   // w
    public double     cost;   // d(w)
    
    public Path( Vertex d, double c )
    {
        dest = d;
        cost = c;
    }
    
    public int compareTo( Path rhs )
    {
        double otherCost = rhs.cost;
        
        return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
    }
}

// Represents a vertex in the graph.
class Vertex
{
    public String     name;   // Vertex name
    public List<Edge> adj;    // Adjacent vertices
    public double     dist;   // Cost
    public Vertex     prev;   // Previous vertex on shortest path
    public int        scratch;// Extra variable used in algorithm

    public Vertex( String nm )
      { name = nm; adj = new LinkedList<Edge>( ); reset( ); }

    public void reset( )
    //  { dist = Graph.INFINITY; prev = null; pos = null; scratch = 0; }    
    { dist = Graph.INFINITY; prev = null; scratch = 0; }
      
   // public PairingHeap.Position<Path> pos;  // Used for dijkstra2 (Chapter 23)
}

// Graph class: evaluate shortest paths.
//
// CONSTRUCTION: with no parameters.
//
// ******************PUBLIC OPERATIONS**********************
// void addEdge( String v, String w, double cvw )
//                              --> Add additional edge
// void printPath( String w )   --> Print path after alg is run
// void dijkstra( String s )    --> Single-source weighted

// ******************ERRORS*********************************
// Some error checking is performed to make sure graph is ok,
// and to make sure graph satisfies properties needed by each
// algorithm.  Exceptions are thrown if errors are detected.

public class Graph
{
    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );

    /**
     * Add a new edge to the graph.
     */
    public void addEdge( String sourceName, String destName, double cost )
    {
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName );
        v.adj.add( new Edge( w, cost ) );
    }

    /**
     * Driver routine to handle unreachables and print total cost.
     * It calls recursive routine to print shortest path to
     * destNode after a shortest path algorithm has run.
     */
    public void printPath( String destName )
    {
        Vertex w = vertexMap.get( destName );
        if( w == null )
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            System.out.println( destName + " is unreachable" );
        else
        {
            System.out.print( "(Cost is: " + w.dist + ") " );
            printPath( w );
            System.out.println( );
        }
    }
	
	double outcost = 0.0 ;

	 public String printPath1( String destName )
    {
        Vertex w = vertexMap.get( destName );
        if( w == null )
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            return "Unreachable";
        else
        {
         outcost = outcost+ w.dist ;   
            return printPath1( w ) ;
           
        }
    }

		public double getCost(){
		return outcost;
		}

		public void clearCost(){
		outcost = 0.0;}

    /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
    private Vertex getVertex( String vertexName )
    {
        Vertex v = vertexMap.get( vertexName );
        if( v == null )
        {
            v = new Vertex( vertexName );
            vertexMap.put( vertexName, v );
        }
        return v;
    }

    /**
     * Recursive routine to print shortest path to dest
     * after running shortest path algorithm. The path
     * is known to exist.
     */
    private void printPath( Vertex dest )
    {
        if( dest.prev != null )
        {
            printPath( dest.prev );
            System.out.print( " to " );
        }
        System.out.print( dest.name );
    }
    	
	String Output =  "";
	private String printPath1( Vertex dest )
    {
        if( dest.prev != null )
        {
            printPath1( dest.prev );
            //System.out.print( " to " );
        }
        //System.out.print( dest.name );
	Output = Output + " " + dest.name;
	return Output;
    }

	// Function to reset output
	private void resetOut(){
		Output = "";}
    /**
     * Initializes the vertex output info prior to running
     * any shortest path algorithm.
     */
    private void clearAll( )
    {
        for( Vertex v : vertexMap.values( ) )
            v.reset( );
    }

    /**
     * Single-source unweighted shortest-path algorithm.
     */
    public void unweighted( String startName )
    {
        clearAll( ); 

        Vertex start = vertexMap.get( startName );
        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );

        Queue<Vertex> q = new LinkedList<Vertex>( );
        q.add( start ); start.dist = 0;

        while( !q.isEmpty( ) )
        {
            Vertex v = q.remove( );

            for( Edge e : v.adj )
            {
                Vertex w = e.dest;
                if( w.dist == INFINITY )
                {
                    w.dist = v.dist + 1;
                    w.prev = v;
                    q.add( w );
                }
            }
        }
    }

  /**
     * Single-source weighted shortest-path algorithm. (Dijkstra) 
     * using priority queues based on the binary heap
     */
    public void dijkstra( String startName )
    {
        PriorityQueue<Path> pq = new PriorityQueue<Path>( );

        Vertex start = vertexMap.get( startName );
        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );

        clearAll( );
        pq.add( new Path( start, 0 ) ); start.dist = 0;
        
        int nodesSeen = 0;
        while( !pq.isEmpty( ) && nodesSeen < vertexMap.size( ) )
        {
            Path vrec = pq.remove( );
            Vertex v = vrec.dest;
            if( v.scratch != 0 )  // already processed v
                continue;
                
            v.scratch = 1;
            nodesSeen++;

            for( Edge e : v.adj )
            {
                Vertex w = e.dest;
                double cvw = e.cost;
                
                if( cvw < 0 )
                    throw new GraphException( "Graph has negative edges" );
                    
                if( w.dist > v.dist + cvw )
                {
                    w.dist = v.dist +cvw;
                    w.prev = v;
                    pq.add( new Path( w, w.dist ) );
                }
            }
        }
    }
	
    

    /**
     * Process a request; return false if end of file.
     */
    public static boolean processRequest( Scanner in, Graph g )
    {
        try
        {
            System.out.print( "Enter start node:" );
            String startName = in.nextLine( );

            System.out.print( "Enter destination node:" );
            String destName = in.nextLine( );

            System.out.print( "Enter algorithm (u, d, n, a ): " );
            String alg = in.nextLine( );
            
            
            if( alg.equals( "d" ) )    
            {
                g.dijkstra( startName );
                g.printPath( destName );
            }
            
         
            g.printPath( destName );
        }
        catch( NoSuchElementException e )
          {  System.err.println( e ); }
        catch( GraphException e )
          { System.err.println( e ); }
        return true;
    }
/**
     * Process a the distance from victim to hospital; returns nothing.
     */

	public static void processRequest1( String start, String dest, Graph g )
    {
        try
        {
            //String startName = in.nextLine( );
            //String destName = in.nextLine( );
            //String alg = in.nextLine( );
            
            g.dijkstra( start );
            //g.printPath( dest );
            //g.printPath( destName );
        }
        catch( NoSuchElementException e )
          { System.err.println( e ); }
        catch( GraphException e )
          { System.err.println( e ); }
    }

	public static void pathN(String start, String dest, Graph g){
			String out;
			String out1;
			String total;

			g.dijkstra(dest );
			//g.printPath( start );
            		out1 = g.printPath1(start);

			g.dijkstra( start );
            		//g.printPath( dest );
			out = g.printPath1(dest);
			
			total = out1+out;
			int length = total.length()/2;
			
			
			System.out.println(total.substring(1,length));
			g.resetOut();
			g.clearCost();
		}
	 
	public static double calculateCost(String start, String dest, Graph g){
			
			// Variables used in calculating cost
			double cost1;
			double cost2;
			double totalCost;
			
			g.dijkstra(dest );
			String o = g.printPath1(start);
			cost2 = g.getCost();
			g.clearCost();

			g.dijkstra( start );
			String u = g.printPath1(dest);
			cost1 = g.getCost();
			g.clearCost();

			g.resetOut();
			
			return cost1+cost2;
		} 
    /**
     * A main routine that:
     * 1. Reads a file containing edges (supplied as a command-line parameter);
     * 2. Forms the graph;
     * 3. Repeatedly prompts for two vertices and
     *    runs the shortest path algorithm.
     * The data file is a sequence of lines of the format
     *    source destination cost
     */
    public static void main( String [ ] args )
    {
        Graph g = new Graph( );
        try
        {   	
	
	    PrintWriter writer = new PrintWriter("meme.txt", "UTF-8");
          	Scanner in = new Scanner( System.in );

		String input = in.nextLine();
		int looplength =  Integer.parseInt(input );
		for(int n = 0;n<looplength;n++){
			input = in.nextLine();
			writer.println(input);
							}

	
		Scanner in1 = new Scanner( System.in );					
		for(int n = 0;n<4;n++){
			input = in1.nextLine();
			writer.println(input);
			
				}
		

		writer.close();
            //FileReader fin = new FileReader(args[0]);
        	FileReader fin = new FileReader("meme.txt");
            Scanner graphFile = new Scanner( fin );

            // Read the edges and insert
		String line;

		//Declaring variables that will be used in the loop
		int num;
		int nodes;
		int loopLength;
		int count = 0;
		String temp;
		int victimsNo;
		int hospitalsNo;
		double checkCost = 0.0;
		double currentCost = 0.0;
		int counter = 0;

		String[] Hospitals;
		String[] Victims;
		ArrayList<Integer> arl = new ArrayList<Integer>(); 

		// Skipping the first line which represents the number of nodes
		//graphFile.nextLine();	

            while( graphFile.hasNextLine( ) )
            {
                line = graphFile.nextLine( );
		line = line.replace("\n","").replace("\n","");
                StringTokenizer st = new StringTokenizer( line );

		num = st.countTokens();
		loopLength = (num-1)/2;

               
		// Adding nodes to graph
                    if( num >= 3 && count == 0 )
                    {	temp = st.nextToken( );
			for (int i = 0; i< loopLength ;i++){
				String source = temp;
				String dest = st.nextToken( );
				int    cost    = Integer.parseInt( st.nextToken( ) );
				g.addEdge( source, dest, cost );
				
				}
                       
                    }
             
			else{
			count = 1;
			StringTokenizer st1 = new StringTokenizer( line );
			victimsNo = Integer.parseInt( st1.nextToken( ) );

			line = graphFile.nextLine(); 
			Hospitals = line.split(" ");
			
			line = graphFile.nextLine();			
			StringTokenizer st2 = new StringTokenizer( line );
			hospitalsNo = Integer.parseInt( st2.nextToken( ) );
			
			line = graphFile.nextLine();
			Victims = line.split(" ");

			
			//For looping for displaying output
			for (int j = 0; j<victimsNo;j++){
				checkCost = calculateCost(Victims[j],Hospitals[0],g);
				currentCost = calculateCost(Victims[j],Hospitals[0],g);
				
				for(int k = 0; k<hospitalsNo;k++){
					currentCost = calculateCost(Victims[j],Hospitals[k],g);	
					if(currentCost<=checkCost){
						if(checkCost == currentCost){
						arl.add(k);
						checkCost = calculateCost(Victims[j],Hospitals[k],g);
						}
						else{
						arl.clear();
						arl.add(k);
						checkCost = calculateCost(Victims[j],Hospitals[k],g);
							}
						}
						
					
					}
				System.out.println("victim "+Victims[j]);
				for(int m = 0; m<arl.size();m++){
					System.out.println("Hospital "+Hospitals[arl.get(m)]);
					pathN(Victims[j],Hospitals[arl.get(m)],g);
					
					}

				
				//
			}

			}
                
                
             }

	// Make while loop to calculate distance from victim to hopsital
	
         }
         catch( IOException e )
           { System.err.println( e ); }

        // System.out.println( "File read..." );
         //System.out.println( g.vertexMap.size( ) + " vertices" );

	
	
         //Scanner in = new Scanner( System.in );
         //while( processRequest( in, g ) )
          //   ;
    }
}
