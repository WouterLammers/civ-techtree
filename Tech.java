package civ_techtree;

import java.util.ArrayList;

import sdljava.SDLException;
import sdljava.video.*;
import sdljavax.gfx.SDLGfx;

public class Tech
{
	public String name;
	public String req1Str;
	public String req2Str;
	public String flags;
	public Tech req1;
	public Tech req2;
	public ArrayList<Tech> children;
	public ArrayList<Building> buildings;
	public ArrayList<Unit> units;
	public boolean researched;
	public SDLRect position;
	private int searchId = 0;
	public SDLSurface techSurface;
	public SDLRect zoomedPosition;
	
	public static SDLSurface bg;
	public static SDLSurface bgDetailed;
	public static String techGoal = null;
	public static boolean showProgress = false;
	public static boolean goalOnly = false;
	public static Tech detailTech = null;
	public static int w = 0;
	public static int h = 0;
	private static int searchSeq = 0;
	public static boolean detailClicked = false;
	
	public Tech( String name, String req1Str, String req2Str, String flags )
	{
		this.name = name;
		this.req1Str = req1Str;
		this.req2Str = req2Str;
		this.flags = flags;
		this.req1 = null;
		this.req2 = null;
		children = new ArrayList<Tech>();
		buildings = new ArrayList<Building>();
		units = new ArrayList<Unit>();
		researched = false;
		position = new SDLRect( 0, 3000, Tech.w, Tech.h );
		zoomedPosition = new SDLRect( 0, 3000, Tech.w, Tech.h );
	
		techSurface = null;
	}
	
	public String printChildren( String indent )
	{
		String result = indent + name + "\n";
		for( Tech child : children )
		{
			result += indent + "  " + child.printChildren(indent+ "  ");
		}
		return result;
	}
	
	public void draw( SDLSurface screen )
	{
		try
		{
			if( goalOnly && (null!=techGoal) )
			{
				// dont draw if no on goal path
				if( !searchTech( techGoal ) )
					return;
			}

			// count how many left to do
			int nrNeeded = 0;
			for( Tech tt : Techtree.ta.techList  )
			{
				if( tt.searchTech(name) && !tt.researched && tt!=this)
					nrNeeded++;
			}

			if( null != techSurface ) techSurface.freeSurface();
			techSurface = bg.displayFormat();
			SDLGfx.rectangleRGBA( techSurface, 0, 0, techSurface.getWidth()-2, techSurface.getHeight()-2, 255, 255, 255, 255 );
			SDLGfx.rectangleRGBA( techSurface, 1, 1, techSurface.getWidth()-3, techSurface.getHeight()-3, 255, 255, 255, 255 );
			SDLGfx.stringRGBA( techSurface, 6, 6, name.toUpperCase() + "("+nrNeeded+")", 255, 255, 255, 255 );
			int y = 16;
			for( Building building : buildings )
			{
				building.draw( 6, y, techSurface );
				SDLGfx.stringRGBA( techSurface, 6+50, y+6, building.bName, 255, 255, 0, 255 );
				y += 21;
			}
			for( Unit unit : units )
			{
				unit.draw( 6, y, techSurface );
				SDLGfx.stringRGBA( techSurface, 6+50, y+11, unit.uName, 255, 0, 0, 255 );
				y += 31;
			}

			if( showProgress )
			{
				if( researched )
					colourMask( 0, 255, 0 );
				else
				{
					if( req1.researched && req2.researched )
						colourMask( 255, 255, 0 );
					// on path to goal
					if( null != techGoal)
					{
						if( searchTech(techGoal) )
						{
							if( req1.researched && req2.researched )
								colourMask( 255, 128, 255 );
							else
								colourMask( 255, 0, 0 );
						}
					}
				}
			}
			if( Techtree.zoomFactor != 1.0 )
			{
				SDLSurface zoomed = SDLGfx.zoomSurface( techSurface, Techtree.zoomFactor, Techtree.zoomFactor, true );
				techSurface.freeSurface();
				techSurface = zoomed;
				zoomed = null;
			}
			if( Techtree.zoomFactor < 0.7 )
			{
				techSurface.fillRect( new SDLRect(2,2,techSurface.getWidth()-4,10), techSurface.mapRGB(0,0,0) );
				SDLGfx.stringRGBA( techSurface, 3, 3, name.toUpperCase(), 255, 255, 255, 255 );
				SDLGfx.stringRGBA( techSurface, 12, 12, "("+nrNeeded+")", 255, 255, 255, 255 );
			}
			if( (zoomedPosition.x<0) || (zoomedPosition.y<0) )
			{
				// see if we need to clip
				if( ((zoomedPosition.x+zoomedPosition.width)>=0) ||
					((zoomedPosition.y+zoomedPosition.height)>=0) )
				{
					// need to clip, otherwise just don't draw
					SDLRect sr = new SDLRect();
					if( zoomedPosition.x<0 )
					{
						sr.x = -zoomedPosition.x;
						sr.width = zoomedPosition.width + zoomedPosition.x; 
					}
					else
					{
						sr.x = 0;
						sr.width = zoomedPosition.width; 
					}
					if( zoomedPosition.y<0 )
					{
						sr.y = -zoomedPosition.y;
						sr.height = zoomedPosition.height + zoomedPosition.y;
					}
					else
					{
						sr.y = 0;
						sr.height = zoomedPosition.height;
					}
					// try to use zoomedPosition as destRect anyway..
					techSurface.blitSurface( sr, screen, zoomedPosition );
					sr = null;
				}
			}
			else
				techSurface.blitSurface( screen, zoomedPosition );
		}
		catch( SDLException sdle )
		{
			System.err.println( "Couldn't update surface!\n" + sdle );
			System.exit(-1);
		}
	}

	public void drawDetailed( SDLSurface screen )
	{
		try
		{
			bgDetailed.blitSurface( screen );
			SDLGfx.stringRGBA( screen, 100, 100, name.toUpperCase(), 255, 255, 255, 255 );
			int y = 116;
			for( Building building : buildings )
			{
				building.draw( 100, y, screen );
				SDLGfx.stringRGBA( screen, 100+50, y+6, building.bName, 255, 255, 0, 255 );
				y += 21;
			}
			for( Unit unit : units )
			{
				unit.draw( 100, y, screen );
				SDLGfx.stringRGBA( screen, 100+50, y+11, unit.uName, 255, 0, 0, 255 );
				y += 31;
			}

			if( researched )
			{
				SDLGfx.stringRGBA( screen, 100, y+11, "RESEARCHED", 255, 255, 255, 255 );
			}
			else
			{
				int nrNeeded = 0;
				for( Tech tt : Techtree.ta.techList  )
				{
					if( tt.searchTech(name) && !tt.researched && tt!=this)
						nrNeeded++;
				}
				if( nrNeeded == 0 )
					SDLGfx.stringRGBA( screen, 100, y+11, "You can research this tech!", 255, 255, 255, 255 );
				if( nrNeeded == 1 )
					SDLGfx.stringRGBA( screen, 100, y+11, "Still need one tech!", 255, 255, 255, 255 );
				if( nrNeeded > 1 )
					SDLGfx.stringRGBA( screen, 100, y+11, "Still need " + nrNeeded + " techs!", 255, 255, 255, 255 );
			}
			
			y += 20;
			if( !flags.equals("\"\"") )
				SDLGfx.stringRGBA( screen, 100, y+11, "Extra flags: " + flags, 255, 255, 255, 255 );
		}
		catch( SDLException sdle )
		{
			System.err.println( "Couldn't update surface!\n" + sdle );
			System.exit(-1);
		}
	}

	private void colourMask( int r, int g, int b ) throws SDLException
	{
		// copy techSurface
		SDLSurface temp = SDLVideo.createRGBSurface( SDLVideo.SDL_SRCCOLORKEY|SDLVideo.SDL_SRCALPHA, Tech.w, Tech.h, 32, r, g, b, 0 );
		techSurface.blitSurface( temp );
		techSurface.freeSurface();
		techSurface = temp;
	}

	public boolean searchTech( String sTech )
	{
		searchSeq++;
		return searchTechRec( sTech );
	}
	
	public boolean searchTechRec( String sTech )
	{
		if( searchId == searchSeq )
			return false;
		searchId = searchSeq;
		
		if( sTech.equals(name) )
			return true;
		
		for( Tech child : children )
		{
			if( child.searchTechRec( sTech ) )
				return true;
		}
		return false;
	}

	public void zoom()
	{
		zoomedPosition.x = (int)(position.x*Techtree.zoomFactor);
		zoomedPosition.y = (int)(position.y*Techtree.zoomFactor);
		zoomedPosition.x += Techtree.viewPort.x;
		zoomedPosition.y += Techtree.viewPort.y;
		zoomedPosition.width = (int)(position.width*Techtree.zoomFactor);
		zoomedPosition.height = (int)(position.height*Techtree.zoomFactor);
	}
	
	public void detailClicked( int my, SDLSurface screen ) throws SDLException
	{
		detailClicked = true;
		int Y = 116;
		for( Building building : buildings )
		{
			if( (my>Y) && (my<(Y+20)) )
			{
				building.drawDetailed( screen );
				return;
			}
			Y += 21;
		}
		for( Unit unit : units )
		{
			if( (my>Y) && (my<(Y+30)) )
			{
				unit.drawDetailed( screen );
				return;
			}
			Y += 31;
		}

	}
}
