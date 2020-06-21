

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
public class Test{ 
public static void main( String [ ] args )
    {
        
        try
        {   	
            //FileReader fin = new FileReader(args[0]);
        	FileReader fin = new FileReader("Graph2.txt");
            Scanner graphFile = new Scanner( fin );
	
	//Skip first line
	graphFile.nextLine();

	    // Counter used in filtering lines from input to seperate different lines based on what they represent
		int count = 0;
		int num = 0;
		int loopLength = 0;
		String temp = "";

            // Read the edges and insert
            String line;
            while( graphFile.hasNextLine( ) )
            {
                line = graphFile.nextLine( );
                StringTokenizer st = new StringTokenizer( line );
		num = st.countTokens( );
		loopLength = (num-1)/2;
                    if( num >= 3  )
                    {
			temp = st.nextToken( );
			for (int i = 0; i< loopLength ;i++){
				System.out.println(temp);
				System.out.println(st.nextToken( ));
				System.out.println(st.nextToken( ));
				System.out.println("");
				}
                        
                    }

		else{
			System.out.println( "Number of Hospital " + line );
			line = graphFile.nextLine( );
			System.out.println( "Hospitals " + line );
			line = graphFile.nextLine( );
			System.out.println( "Number of victims " + line );
			line = graphFile.nextLine( );
			System.out.println( "victims " + line );
			
			}

                    //String source  = st.nextToken( );
                    //String dest    = st.nextToken( );
                    //int    cost    = Integer.parseInt( st.nextToken( ) );
                    //g.addEdge( source, dest, cost );
                
             }
         }
         catch( IOException e )
           { System.err.println( e ); }

        
             
    }}
