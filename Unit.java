// This code is not industry-strength! It is the result of a hack session.
package civ_techtree;

import java.util.ArrayList;

import sdljava.SDLException;
import sdljava.video.SDLRect;
import sdljava.video.SDLSurface;
import sdljavax.gfx.SDLGfx;

public class Unit
{
	public static SDLSurface unitSurface;
	public String uName;
	private SDLRect sr;
	public String move;
	public String obsolete = null;
	public String impr = null;
	public String cost;
	public String popcost;
	public String attack;
	public String defense;
	public String moverate;
	public String fire;
	public String hit;
	public String vision;
	public String transport;
	public String fuel;
	public String happy;
	public String shield;
	public String food;
	public String gold;
	public String flags;
	public String roles;
	public ArrayList<String> helpText;
	
	public Unit( String uName )
	{
		this.uName = uName;
		// find graphic
		sr = findGraphic(uName);
		helpText = new ArrayList<String>();
	}
	
	public void draw( int x, int y, SDLSurface dstSurface ) throws SDLException
	{
		SDLRect dr = new SDLRect( x, y, 30, 30 );
		unitSurface.blitSurface( sr, dstSurface, dr );
		dr = null;
	}
	
	// I don't feel like thinking, this is ugly but i don't care
	private static SDLRect findGraphic( String unitName )
	{
		if( unitName.equals("Armor") )							return new SDLRect( 0*30, 0*30, 30, 30 );
		if( unitName.equals("Howitzer") )						return new SDLRect( 1*30, 0*30, 30, 30 );
		if( unitName.equals("Battleship") )					return new SDLRect( 2*30, 0*30, 30, 30 );
		if( unitName.equals("Bomber") )							return new SDLRect( 3*30, 0*30, 30, 30 );
		if( unitName.equals("Cannon") )							return new SDLRect( 4*30, 0*30, 30, 30 );
		if( unitName.equals("Caravan") )						return new SDLRect( 5*30, 0*30, 30, 30 );
		if( unitName.equals("Carrier") )						return new SDLRect( 6*30, 0*30, 30, 30 );
		if( unitName.equals("Catapult") )						return new SDLRect( 7*30, 0*30, 30, 30 );
		if( unitName.equals("Horsemen") )						return new SDLRect( 8*30, 0*30, 30, 30 );
		if( unitName.equals("Chariot") )						return new SDLRect( 9*30, 0*30, 30, 30 );
		if( unitName.equals("Cruiser") )						return new SDLRect( 10*30, 0*30, 30, 30 );
		if( unitName.equals("Diplomat") )						return new SDLRect( 11*30, 0*30, 30, 30 );
		if( unitName.equals("Barbarian Leader") )		return new SDLRect( 11*30, 0*30, 30, 30 );
		if( unitName.equals("Fighter") )						return new SDLRect( 12*30, 0*30, 30, 30 );
		if( unitName.equals("Frigate") )						return new SDLRect( 13*30, 0*30, 30, 30 );
		if( unitName.equals("Ironclad") )						return new SDLRect( 14*30, 0*30, 30, 30 );
		if( unitName.equals("Knights") )						return new SDLRect( 15*30, 0*30, 30, 30 );
		if( unitName.equals("Legion") )							return new SDLRect( 16*30, 0*30, 30, 30 );
		if( unitName.equals("Mech. Inf.") )					return new SDLRect( 17*30, 0*30, 30, 30 );
		if( unitName.equals("Warriors") )						return new SDLRect( 18*30, 0*30, 30, 30 );
		if( unitName.equals("Musketeers") )					return new SDLRect( 19*30, 0*30, 30, 30 );
		
		if( unitName.equals("Nuclear") )						return new SDLRect( 0*30, 1*30, 30, 30 );
		if( unitName.equals("Phalanx") )						return new SDLRect( 1*30, 1*30, 30, 30 );
		if( unitName.equals("Riflemen") )						return new SDLRect( 2*30, 1*30, 30, 30 );
		if( unitName.equals("Caravel") )						return new SDLRect( 3*30, 1*30, 30, 30 );
		if( unitName.equals("Settlers") )						return new SDLRect( 4*30, 1*30, 30, 30 );
		if( unitName.equals("Submarine") )					return new SDLRect( 5*30, 1*30, 30, 30 );
		if( unitName.equals("Transport") )					return new SDLRect( 6*30, 1*30, 30, 30 );
		if( unitName.equals("Trireme") )						return new SDLRect( 7*30, 1*30, 30, 30 );
		if( unitName.equals("Archers") )						return new SDLRect( 8*30, 1*30, 30, 30 );
		if( unitName.equals("Cavalry") )						return new SDLRect( 9*30, 1*30, 30, 30 );
		if( unitName.equals("Cruise Missile") )			return new SDLRect( 10*30, 1*30, 30, 30 );
		if( unitName.equals("Destroyer") )					return new SDLRect( 11*30, 1*30, 30, 30 );
		if( unitName.equals("Dragoons") )						return new SDLRect( 12*30, 1*30, 30, 30 );
		if( unitName.equals("Explorer") )						return new SDLRect( 13*30, 1*30, 30, 30 );
		if( unitName.equals("Freight") )						return new SDLRect( 14*30, 1*30, 30, 30 );
		if( unitName.equals("Galleon") )						return new SDLRect( 15*30, 1*30, 30, 30 );
		if( unitName.equals("Partisan") )						return new SDLRect( 16*30, 1*30, 30, 30 );
		if( unitName.equals("Pikemen") )						return new SDLRect( 17*30, 1*30, 30, 30 );
		
		if( unitName.equals("Marines") )						return new SDLRect( 0*30, 2*30, 30, 30 );
		if( unitName.equals("Spy") )								return new SDLRect( 1*30, 2*30, 30, 30 );
		if( unitName.equals("Engineers") )					return new SDLRect( 2*30, 2*30, 30, 30 );
		if( unitName.equals("Artillery") )					return new SDLRect( 3*30, 2*30, 30, 30 );
		if( unitName.equals("Helicopter") )					return new SDLRect( 4*30, 2*30, 30, 30 );
		if( unitName.equals("Alpine Troops") )			return new SDLRect( 5*30, 2*30, 30, 30 );
		if( unitName.equals("Stealth Bomber") )			return new SDLRect( 6*30, 2*30, 30, 30 );
		if( unitName.equals("Stealth Fighter") )		return new SDLRect( 7*30, 2*30, 30, 30 );
		if( unitName.equals("AEGIS Cruiser") )			return new SDLRect( 8*30, 2*30, 30, 30 );
		if( unitName.equals("Paratroopers") )				return new SDLRect( 9*30, 2*30, 30, 30 );
		if( unitName.equals("Elephants") )					return new SDLRect( 10*30, 2*30, 30, 30 );
		if( unitName.equals("Crusaders") )					return new SDLRect( 11*30, 2*30, 30, 30 );
		if( unitName.equals("Fundamentalist") )			return new SDLRect( 12*30, 2*30, 30, 30 );
		if( unitName.equals("AWACS") )							return new SDLRect( 13*30, 2*30, 30, 30 );
		if( unitName.equals("Workers") )						return new SDLRect( 14*30, 2*30, 30, 30 );
		
		return new SDLRect( 19*30, 2*30, 30, 30 );
	}
	
	public void drawDetailed( SDLSurface dstSurface ) throws SDLException
	{
		Tech.bgDetailed.blitSurface( dstSurface );
		SDLRect dr = new SDLRect( 100, 100, 36, 20 );
		unitSurface.blitSurface( sr, dstSurface, dr );
		dr = null;
		SDLGfx.stringRGBA( dstSurface, 100+50, 100+11, uName.toUpperCase(), 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+2*18, "Cost: " + cost, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+3*18, "Attack: "+attack + " Defense: "+defense + " Moves: " + moverate, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+4*18, "Hitpoints: " + hit + " Firepower: " + fire, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+5*18, "Vision Range: " + vision, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+6*18, "Movetype: " + move, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+7*18, "Required Improvement: " + impr, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+8*18, "Obsoleted by: " + obsolete, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+9*18, "Fuel: " + fuel, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+10*18, "Population cost: " + popcost, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+11*18, "Transport capability: " + transport, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+12*18, "Upkeep - happy:"+happy + " shield:"+shield + " food:" + food + " gold:"+gold, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+13*18, "Flags: " + flags, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+14*18, "Roles: "+ roles, 255, 255, 255, 255 );
		SDLGfx.stringRGBA( dstSurface, 100, 100+15*18, "", 255, 255, 255, 255 );

		int tempY = 100+16*18;
		for( String helps : helpText )
		{
			SDLGfx.stringRGBA( dstSurface, 100, tempY, helps, 255, 255, 255, 255 );
			tempY += 18;
		}
		dstSurface.flip();
	}
	
}
