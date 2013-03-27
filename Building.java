package civ_techtree;

import java.util.ArrayList;

import sdljava.SDLException;
import sdljava.video.SDLRect;
import sdljava.video.SDLSurface;
import sdljavax.gfx.SDLGfx;

/*
[building_bank]
name    = _("Bank")
tech_req  = "Banking"
bldg_req  = "Marketplace"
;terr_gate  =
;spec_gate  =
equiv_range = "City"
;equiv_dupl =
;equiv_repl =
obsolete_by = "None"
is_wonder = 0
build_cost  = 80
upkeep    = 2
sabotage  = 100
effect    =
    { "name", "value", "req_type", "req"
      "Tax_Bonus", 50, "Building", "Marketplace"
      "Luxury_Bonus", 50, "Building", "Marketplace"
    }
helptext  = _("\
Together with the Marketplace improvement, a Bank increases the\
 luxury and tax production within a city by 100%.\
")
*/

public class Building
{
	public static SDLSurface buildingSurface;
	public String bName;
	public String bReq = null;
	public String tReq = null;
	public String sReq = null;
	public String eqRange = null;
	public String obsolete = null;
	public String isWonder = null;
	public String cost;
	public String upkeep;
	public String sabotage;
	public ArrayList<String> helpText;
	
	private SDLRect sr;
	
	public Building( String bName )
	{
		this.bName = bName;
		// find graphic
		sr = findGraphic(bName);
		helpText = new ArrayList<String>();
	}
	
	public void draw( int x, int y, SDLSurface dstSurface ) throws SDLException
	{
		SDLRect dr = new SDLRect( x, y, 36, 20 );
		buildingSurface.blitSurface( sr, dstSurface, dr );
		dr = null;
	}

	public void drawDetailed( SDLSurface dstSurface ) throws SDLException
	{
		Tech.bgDetailed.blitSurface( dstSurface );
		SDLRect dr = new SDLRect( 100, 100, 36, 20 );
		buildingSurface.blitSurface( sr, dstSurface, dr );
		dr = null;
		SDLGfx.stringRGBA( dstSurface, 100+50, 100+6, bName.toUpperCase(), 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+2*18, "Cost: " + cost, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+3*18, "Upkeep: " + upkeep, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+4*18, "Wonder: " + isWonder, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+5*18, "Obsoleted by: " + obsolete, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+6*18, "Requires: " + bReq + ", " + tReq + ", " + sReq, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+7*18, "Effects: " + eqRange, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+8*18, "Sabotage: " + sabotage + "%", 255, 255, 255, 255 );
		int tempY = 100+10*18;
		for( String helps : helpText )
		{
			SDLGfx.stringRGBA( dstSurface, 100, tempY, helps, 255, 255, 255, 255 );
			tempY += 18;
		}
		dstSurface.flip();
	}

	// I don't feel like thinking, this is ugly but i don't care
	private static SDLRect findGraphic( String buildingName )
	{
		if( buildingName.equals("Palace") )			return new SDLRect( 0*36, 0*20, 36, 20 );
		if( buildingName.equals("Barracks") )			return new SDLRect( 1*36, 0*20, 36, 20 );
		if( buildingName.equals("Barracks II") )			return new SDLRect( 1*36, 0*20, 36, 20 );
		if( buildingName.equals("Barracks III") )			return new SDLRect( 1*36, 0*20, 36, 20 );
		if( buildingName.equals("Granary") )			return new SDLRect( 2*36, 0*20, 36, 20 );
		if( buildingName.equals("Temple") )			return new SDLRect( 3*36, 0*20, 36, 20 );
		if( buildingName.equals("Marketplace") )			return new SDLRect( 4*36, 0*20, 36, 20 );
		if( buildingName.equals("Library") )			return new SDLRect( 5*36, 0*20, 36, 20 );
		if( buildingName.equals("Courthouse") )			return new SDLRect( 6*36, 0*20, 36, 20 );
		if( buildingName.equals("City Walls") )			return new SDLRect( 7*36, 0*20, 36, 20 );

		if( buildingName.equals("Aqueduct") )			return new SDLRect( 0*36, 1*20, 36, 20 );
		if( buildingName.equals("Bank") )			return new SDLRect( 1*36, 1*20, 36, 20 );
		if( buildingName.equals("Cathedral") )			return new SDLRect( 2*36, 1*20, 36, 20 );
		if( buildingName.equals("University") )			return new SDLRect( 3*36, 1*20, 36, 20 );
		if( buildingName.equals("Mass Transit") )			return new SDLRect( 4*36, 1*20, 36, 20 );
		if( buildingName.equals("Colosseum") )			return new SDLRect( 5*36, 1*20, 36, 20 );
		if( buildingName.equals("Factory") )			return new SDLRect( 6*36, 1*20, 36, 20 );
		if( buildingName.equals("Mfg. Plant") )			return new SDLRect( 7*36, 1*20, 36, 20 );

		if( buildingName.equals("SDI Defense") )			return new SDLRect( 0*36, 2*20, 36, 20 );
		if( buildingName.equals("Recycling Center") )			return new SDLRect( 1*36, 2*20, 36, 20 );
		if( buildingName.equals("Power Plant") )			return new SDLRect( 2*36, 2*20, 36, 20 );
		if( buildingName.equals("Hydro Plant") )			return new SDLRect( 3*36, 2*20, 36, 20 );
		if( buildingName.equals("Nuclear Plant") )			return new SDLRect( 4*36, 2*20, 36, 20 );
		if( buildingName.equals("Stock Exchange") )			return new SDLRect( 5*36, 2*20, 36, 20 );
		if( buildingName.equals("Sewer System") )			return new SDLRect( 6*36, 2*20, 36, 20 );
		if( buildingName.equals("Supermarket") )			return new SDLRect( 7*36, 2*20, 36, 20 );

		if( buildingName.equals("Super Highways") )			return new SDLRect( 0*36, 3*20, 36, 20 );
		if( buildingName.equals("Research Lab") )			return new SDLRect( 1*36, 3*20, 36, 20 );
		if( buildingName.equals("SAM Battery") )			return new SDLRect( 2*36, 3*20, 36, 20 );
		if( buildingName.equals("Coastal Defense") )			return new SDLRect( 3*36, 3*20, 36, 20 );
		if( buildingName.equals("Solar Plant") )			return new SDLRect( 4*36, 3*20, 36, 20 );
		if( buildingName.equals("Harbour") )			return new SDLRect( 5*36, 3*20, 36, 20 );
		if( buildingName.equals("Offshore Platform") )			return new SDLRect( 6*36, 3*20, 36, 20 );
		if( buildingName.equals("Airport") )			return new SDLRect( 7*36, 3*20, 36, 20 );

		if( buildingName.equals("Police Station") )			return new SDLRect( 0*36, 4*20, 36, 20 );
		if( buildingName.equals("Port Facility") )			return new SDLRect( 1*36, 4*20, 36, 20 );
		//if( buildingName.equals("") )			return new SDLRect( 5*36, 4*20, 36, 20 );

		if( buildingName.equals("Apollo Program") )			return new SDLRect( 0*36, 5*20, 36, 20 );
		if( buildingName.equals("A.Smith's Trading Co.") )			return new SDLRect( 1*36, 5*20, 36, 20 );
		if( buildingName.equals("Coinage") )			return new SDLRect( 2*36, 5*20, 36, 20 );
		if( buildingName.equals("Colossus") )			return new SDLRect( 3*36, 5*20, 36, 20 );
		if( buildingName.equals("Copernicus' Observatory") )			return new SDLRect( 4*36, 5*20, 36, 20 );
		if( buildingName.equals("Cure For Cancer") )			return new SDLRect( 5*36, 5*20, 36, 20 );
		if( buildingName.equals("Darwin's Voyage") )			return new SDLRect( 6*36, 5*20, 36, 20 );
		if( buildingName.equals("Eiffel Tower") )			return new SDLRect( 7*36, 5*20, 36, 20 );

		if( buildingName.equals("Great Library") )			return new SDLRect( 0*36, 6*20, 36, 20 );
		if( buildingName.equals("Great Wall") )			return new SDLRect( 1*36, 6*20, 36, 20 );
		if( buildingName.equals("Hanging Gardens") )			return new SDLRect( 2*36, 6*20, 36, 20 );
		if( buildingName.equals("Hoover Dam") )			return new SDLRect( 3*36, 6*20, 36, 20 );
		if( buildingName.equals("Isaac Newton's College") )			return new SDLRect( 4*36, 6*20, 36, 20 );
		if( buildingName.equals("J.S. Bach's Cathedral") )			return new SDLRect( 5*36, 6*20, 36, 20 );
		if( buildingName.equals("King Richard's Crusade") )			return new SDLRect( 6*36, 6*20, 36, 20 );
		if( buildingName.equals("Leonardo's Workshop") )			return new SDLRect( 7*36, 6*20, 36, 20 );

		if( buildingName.equals("Lighthouse") )			return new SDLRect( 0*36, 7*20, 36, 20 );
		if( buildingName.equals("Magellan's Expedition") )			return new SDLRect( 1*36, 7*20, 36, 20 );
		if( buildingName.equals("Manhattan Project") )			return new SDLRect( 2*36, 7*20, 36, 20 );
		if( buildingName.equals("Marco Polo's Embassy") )			return new SDLRect( 3*36, 7*20, 36, 20 );
		if( buildingName.equals("Michelangelo's Chapel") )			return new SDLRect( 4*36, 7*20, 36, 20 );
		if( buildingName.equals("Oracle") )			return new SDLRect( 5*36, 7*20, 36, 20 );
		if( buildingName.equals("Pyramids") )			return new SDLRect( 6*36, 7*20, 36, 20 );
		if( buildingName.equals("SETI Program") )			return new SDLRect( 7*36, 7*20, 36, 20 );

		if( buildingName.equals("Shakespeare's Theatre") )			return new SDLRect( 0*36, 8*20, 36, 20 );
		if( buildingName.equals("Space Component") )			return new SDLRect( 1*36, 8*20, 36, 20 );
		if( buildingName.equals("Space Module") )			return new SDLRect( 2*36, 8*20, 36, 20 );
		if( buildingName.equals("Space Structural") )			return new SDLRect( 3*36, 8*20, 36, 20 );
		if( buildingName.equals("Statue of Liberty") )			return new SDLRect( 4*36, 8*20, 36, 20 );
		if( buildingName.equals("Sun Tzu's War Academy") )			return new SDLRect( 5*36, 8*20, 36, 20 );
		if( buildingName.equals("United Nations") )			return new SDLRect( 6*36, 8*20, 36, 20 );
		if( buildingName.equals("Women's Suffrage") )			return new SDLRect( 7*36, 8*20, 36, 20 );

		
		return new SDLRect( 2*36, 4*20, 36, 20 );
	}

}
