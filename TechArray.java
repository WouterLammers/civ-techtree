package civ_techtree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TechArray
{
	public ArrayList<Tech> techList;
	private ArrayList<Tech> notPlacedList;
	
	public TechArray()
	{
		techList = new ArrayList<Tech>();
		notPlacedList = new ArrayList<Tech>();
		Tech rootTech = new Tech( "None", null, null, "" );
		rootTech.researched = true;
		techList.add( rootTech );
	}
	
	public void insertTech( Tech t )
	{
		techList.add( t );
		if( !placeTech(t) )
		{
			notPlacedList.add(t);
		}
		processNotDoneList();
	}
	
	private boolean placeTech( Tech t )
	{
		for( Tech tree : techList )
		{
			boolean addKid = false;
			if( (null==t.req1) && (tree.name.equals(t.req1Str)) )
			{
				t.req1 = tree;
				addKid = true;
			}
			if( (null==t.req2) && (tree.name.equals(t.req2Str)) )
			{
				t.req2 = tree;
				addKid = true;
			}
			if( addKid ) tree.children.add( t );
		}
		return (t.req1!=null) && (t.req2!=null);
	}
	
	private void processNotDoneList()
	{
		boolean placedOne = false;
		ArrayList<Tech> newlyPlaced = new ArrayList<Tech>();

		do
		{
			placedOne = false;
			for( Tech t : notPlacedList )
			{
				if( placeTech(t) )
				{
					newlyPlaced.add( t );
					placedOne = true;
				}
			}
			for( Tech placed : newlyPlaced )
			{
				notPlacedList.remove( placed );
			}
		}
		while( placedOne );
	}
	
	public Tech getRootNode()
	{
		return techList.get(0);
	}
	
	public Tech findTech( String searchTech )
	{
		for( Tech tech : techList )
		{
			if( tech.name.equals(searchTech) )
				return tech;
		}
		return null;
	}
	
	public void savePositions( String p )
	{
		try
		{
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("civ_techtree/save/positions"+p)));
			out.write( Techtree.viewPort.x +":"+ Techtree.viewPort.y +":"+ Techtree.viewPort.width +":"+ Techtree.viewPort.height + "\n" );
			out.write( "" + Techtree.zoomFactor + "\n" );
			if( null != Tech.techGoal )
				out.write( Tech.techGoal +":"+ Tech.goalOnly +":"+ Tech.showProgress + "\n" );
			else
				out.write( "None" +":"+ Tech.goalOnly +":"+ Tech.showProgress + "\n" );
			
			for( Tech t : techList )
			{
				out.write( t.name + ":" + t.position.x + ":" + t.position.y + ":" + t.researched + "\n" );
			}
			out.close();
			Techtree.msg = "Positions ("+p+") saved";
		}
		catch( IOException ioe )
		{
			ioe.printStackTrace();
			Techtree.msg = "Error saving positions"+p;
		}
	}

	public void loadPositions( String p )
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("civ_techtree/save/positions"+p));

			String line;
			String[] parts;
			
			line=in.readLine();
			parts = line.trim().split(":");
			Techtree.viewPort.x = Integer.parseInt(parts[0]);
			Techtree.viewPort.y = Integer.parseInt(parts[1]);
			Techtree.viewPort.width = Integer.parseInt(parts[2]);
			Techtree.viewPort.height = Integer.parseInt(parts[3]);
			
			line=in.readLine();
			Techtree.zoomFactor = Double.parseDouble( line.trim() );
			
			line=in.readLine();
			parts = line.trim().split(":");
			Tech.techGoal = parts[0];
			if( Tech.techGoal.equals("None") ) Tech.techGoal = null;
			Tech.goalOnly = false;
			if( parts[1].equals("true") ) 
				Tech.goalOnly = true;
			Tech.showProgress = false;
			if( parts[2].equals("true") )
				Tech.showProgress = true;
			
			while( null != (line=in.readLine()) )
			{
				parts = line.split(":");
				Tech t = findTech( parts[0] );
				if( null != t )
				{
					t.position.x = Integer.parseInt( parts[1] );
					t.position.y = Integer.parseInt( parts[2] );
					t.researched = false;
					if( parts[3].equals("true") )
						t.researched = true;
					if( t.name.equals("None") ) t.researched = true; // special case None
				}
			}
			Techtree.msg = "Positions ("+p+") loaded";
		}
		catch( FileNotFoundException fnfe )
		{
			System.err.println( "File not found: positions"+p );
			fnfe.printStackTrace();
			Techtree.msg = "File not found: positions"+p;
		}
		catch( IOException ioe )
		{
			ioe.printStackTrace();
			Techtree.msg = "Error loading positions"+p; 
		}
	}
	
}
