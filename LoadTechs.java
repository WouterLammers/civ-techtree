// This code is not industry-strength! It is the result of a hack session.
package civ_techtree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoadTechs 
{

	public static void loadTechs( TechArray ta )
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("civ_techtree/data/techs.ruleset"));

			String line;
			while( null != (line=in.readLine()) )
			{
				if( line.startsWith("[advance_") )
				{
					Tech newTech = readTech(in);
					if( null != newTech )
						ta.insertTech( newTech );
				}
			}
		}
		catch( FileNotFoundException fnfe )
		{
			System.err.println( "File not found: data/techs.ruleset" );
			fnfe.printStackTrace();
			System.exit(-1);
		}
		catch( IOException ioe )
		{
			ioe.printStackTrace();
			System.exit(-1);
		}
	}

	private static Tech readTech( BufferedReader in ) throws IOException
	{
		String name  = getValue(in.readLine());
		String req1  = getValue(in.readLine());
		String req2  = getValue(in.readLine());
		String flags = getValueNumber(in.readLine());
		
		if( req1.equalsIgnoreCase("Never") || req2.equalsIgnoreCase("Never") )
			return null;
		
		return new Tech( name, req1, req2, flags );
	}

	// returns the part of the string between "'s
	private static String getValue( String s )
	{
		return s.substring( s.indexOf('"')+1, s.indexOf('"', s.indexOf('"')+1) );
	}

	// returns the part of the string after = in strings without "
	private static String getValueNumber( String s )
	{
		return s.substring( s.indexOf('=')+2 );
	}

	
	public static void loadBuildings( TechArray ta )
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("civ_techtree/data/buildings.ruleset"));

			String line;
			while( null != (line=in.readLine()) )
			{
				if( line.startsWith("[building_") )
				{
					String buildingName = getValue(in.readLine());
					String reqTechName = getValue(in.readLine());
					Tech reqTech = ta.findTech( reqTechName );
					if( null != reqTech )
					{
						Building newB = new Building( buildingName );
						readBuilding( newB, in );
						reqTech.buildings.add( newB );
					}
				}
			}
		}
		catch( FileNotFoundException fnfe )
		{
			System.err.println( "File not found: data/buildings.ruleset" );
			fnfe.printStackTrace();
			System.exit(-1);
		}
		catch( IOException ioe )
		{
			ioe.printStackTrace();
			System.exit(-1);
		}
	}

	private static void readBuilding( Building newB, BufferedReader in ) throws IOException
	{
		String line;
		while( null != (line=in.readLine()) )
		{
			if( line.trim().equals("") ) return;
			
			if( line.startsWith("bldg_req") ) newB.bReq = getValue(line);
			if( line.startsWith("terr_gate") ) newB.tReq = getValue(line);
			if( line.startsWith("spec_gate") ) newB.sReq = getValue(line);
			if( line.startsWith("equiv_range") ) newB.eqRange = getValue(line);
			if( line.startsWith("obsolete_by") ) newB.obsolete = getValue(line);
			if( line.startsWith("is_wonder") ) newB.isWonder = getValueNumber(line);
			if( line.startsWith("build_cost") ) newB.cost = getValueNumber(line);
			if( line.startsWith("upkeep") ) newB.upkeep = getValueNumber(line);
			if( line.startsWith("sabotage") ) newB.sabotage = getValueNumber(line);
			if( line.startsWith("helptext") )
			{
				while( null != (line=in.readLine()) )
				{
					if( line.trim().equals("\")") )
						break;
					else
					{
						String newLine = line.trim().substring(0,line.trim().length()-1);
						if( newLine.startsWith("\\n") ) newLine = "";
						newB.helpText.add( newLine );
					}
				}
			}
		}
	}

	public static void loadUnits( TechArray ta )
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("civ_techtree/data/units.ruleset"));

			String line;
			while( null != (line=in.readLine()) )
			{
				if( line.startsWith("[unit_") )
				{
					String unitName = getValue(in.readLine());
					String moveType = in.readLine();
					String reqTechName = getValue(in.readLine());
					Tech reqTech = ta.findTech( reqTechName );
					if( null != reqTech )
					{
						Unit newU = new Unit( unitName );
						newU.move = getValue(moveType);
						readUnit( newU, in );
						reqTech.units.add( newU );
					}
				}
			}
		}
		catch( FileNotFoundException fnfe )
		{
			System.err.println( "File not found: data/buildings.ruleset" );
			fnfe.printStackTrace();
			System.exit(-1);
		}
		catch( IOException ioe )
		{
			ioe.printStackTrace();
			System.exit(-1);
		}
	}

	private static void readUnit( Unit newU, BufferedReader in ) throws IOException
	{
		String line;
		while( null != (line=in.readLine()) )
		{
			if( line.trim().equals("") ) return;
			if( line.startsWith("build_cost") ) newU.cost = getValueNumber(line);
			if( line.startsWith("pop_cost") ) newU.popcost = getValueNumber(line);
			if( line.startsWith("attack") ) newU.attack = getValueNumber(line);
			if( line.startsWith("defense") ) newU.defense = getValueNumber(line);
			if( line.startsWith("hitpoints") ) newU.hit = getValueNumber(line);
			if( line.startsWith("firepower") ) newU.fire = getValueNumber(line);
			if( line.startsWith("move_rate") ) newU.moverate = getValueNumber(line);
			if( line.startsWith("vision_range") ) newU.vision = getValueNumber(line);
			if( line.startsWith("transport_cap") ) newU.transport = getValueNumber(line);
			if( line.startsWith("fuel") ) newU.fuel = getValueNumber(line);
			if( line.startsWith("uk_happy") ) newU.happy = getValueNumber(line);
			if( line.startsWith("uk_shield") ) newU.shield = getValueNumber(line);
			if( line.startsWith("uk_food") ) newU.food = getValueNumber(line);
			if( line.startsWith("uk_gold") ) newU.gold = getValueNumber(line);
			if( line.startsWith("flags") ) newU.flags = getValueNumber(line);
			if( line.startsWith("roles") ) newU.roles = getValueNumber(line);
			if( line.startsWith("obsolete_by") ) newU.obsolete = getValue(line);
			if( line.startsWith("helptext") )
			{
				while( null != (line=in.readLine()) )
				{
					if( line.trim().equals("\")") )
						break;
					else
					{
						String newLine = line.trim().substring(0,line.trim().length()-1);
						if( newLine.startsWith("\\n") ) newLine = "";
						newU.helpText.add( newLine );
					}
				}
			}
		}
	}

}
