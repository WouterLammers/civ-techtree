package civ_techtree;

import sdljava.SDLException;
import sdljava.SDLMain;
import sdljava.event.SDLEvent;
import sdljava.event.SDLKey;
import sdljava.event.SDLKeyboardEvent;
import sdljava.event.SDLMouseButtonEvent;
import sdljava.event.SDLQuitEvent;
import sdljava.image.SDLImage;
import sdljava.video.SDLRect;
import sdljava.video.SDLSurface;
import sdljava.video.SDLVideo;
import sdljava.x.swig.SDLPressedState;
import sdljavax.gfx.SDLGfx;

/*
 * TODO
 */

public class Techtree
{
	public static TechArray ta;
	public static double zoomFactor = 1.0;
	public static SDLRect viewPort;
	public static String msg = null;
	private static double ZOOMSTEP = 0.1;
	private static int SCROLLSTEP = 10;
	
	public static final int RESOLUTION_X = 1000;
	public static final int RESOLUTION_Y = 720;
	private static final boolean FULLSCREEN = false;
	
	public SDLSurface screen;

	public Techtree()
	{
	}
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		Techtree tt = new Techtree();
		tt.start();
	}

	private int distance( int fromX, int fromY, int toX, int toY )
	{
		int a2 = (fromX-toX)*(fromX-toX);
		int b2 = (fromY-toY)*(fromY-toY);
		return (int)(Math.sqrt(a2+b2));
	}
	
	private void drawArrow( Tech fromTech, Tech toTech )
	{
		int r,g,b;
		if( (fromTech.position.y==3000) || (toTech.position.y==3000) )
			return;
		int fromX = fromTech.zoomedPosition.x + fromTech.zoomedPosition.width - 1;
		int fromY = fromTech.zoomedPosition.y + (fromTech.zoomedPosition.height / 2);
		int toX = toTech.zoomedPosition.x;
		int toY = toTech.zoomedPosition.y + (fromTech.zoomedPosition.height / 2);
		if( (distance(fromX,fromY,toX,toY) < 200) || (Tech.techGoal != null) )
		{
			r = g = b = 255;
		}
		else
		{
			b = g = 0; r = 160;
		}
		SDLGfx.aalineRGBA( screen, fromX, fromY, toX-8, toY, r, g, b, 255 );
		SDLGfx.filledCircleRGBA( screen, fromX, fromY, 3, r, g, b, 255 );
		SDLGfx.filledTrigonRGBA( screen, toX, toY, toX-8, toY-4, toX-8, toY+4, r, g, b, 255 );
	}
	
	private void drawDependencies( Tech fromTech )
	{
		for( Tech child : fromTech.children )
		{
			if( Tech.techGoal != null )
			{
				if( child.searchTech(Tech.techGoal))
					drawArrow( fromTech, child );
			}
			else
				drawArrow( fromTech, child );
		}
	}

	private void drawTech( Tech t )
	{
		t.zoom();
//		if( (t.position.x > -viewPort.x) && (t.position.x < -viewPort.x+viewPort.width) &&
//			(t.position.y > -viewPort.y) && (t.position.y < -viewPort.y+viewPort.height) )
			t.draw( screen );
	}
	
	private void draw() throws SDLException
	{
		screen.fillRect( screen.mapRGB(0,0,0) );

		if( null != Tech.detailTech )
		{
			Tech.detailTech.drawDetailed( screen );
			screen.flip();
			return;
		}
		
		/*Tech root = ta.techList.get(0);
		drawTech(root);
		for( Tech t : root.children )
		{
			for( Tech tc : t.children )
			{
				drawTech(tc);
			}
			drawTech(t);
		}
		
		drawDependencies(root);
		for( Tech t : root.children )
		{
			for( Tech tc : t.children )
			{
				drawDependencies(tc);
			}
			drawDependencies(t);
		}*/
		
		for( Tech t : ta.techList )
		{
			drawTech(t);
		}

		for( Tech t : ta.techList )
		{
			drawDependencies( t );
		}

		if( null != msg )
		{
			SDLGfx.stringRGBA( screen, 6, RESOLUTION_Y-16, msg, 255, 255, 255, 255 );
			msg = null;
		}
		
		SDLGfx.stringRGBA( screen, RESOLUTION_X-100, RESOLUTION_Y-16, (Runtime.getRuntime().freeMemory()/1024)+"/"+(Runtime.getRuntime().totalMemory()/1024), 255, 255, 255, 255 );
		if( null != Tech.techGoal )
			SDLGfx.stringRGBA( screen, RESOLUTION_X-160, RESOLUTION_Y-30, "Goal: "+Tech.techGoal, 255, 255, 255, 255 );
		
		screen.flip();
	}

	private void init() throws SDLException
	{
		SDLGfx.stringRGBA( screen, 10, 10, "Loading graphics", 0, 0, 255, 255 );
		screen.flip();
		Tech.bg = SDLImage.load( "civ_techtree/images/bg.png" );
		Tech.bgDetailed = SDLGfx.zoomSurface( Tech.bg, Techtree.RESOLUTION_X/Tech.bg.getWidth(), Techtree.RESOLUTION_Y/Tech.bg.getHeight(), true );
		Tech.w = Tech.bg.getWidth();
		Tech.h = Tech.bg.getHeight();
		Building.buildingSurface = SDLImage.load( "civ_techtree/images/buildings.png" );
		Unit.unitSurface = SDLImage.load( "civ_techtree/images/units.png" );

		SDLGfx.stringRGBA( screen, 10, 20, "Loading techs", 0, 0, 255, 255 );
		screen.flip();
		ta = new TechArray();
		LoadTechs.loadTechs(ta);
		
		SDLGfx.stringRGBA( screen, 10, 30, "Loading buildings", 0, 0, 255, 255 );
		screen.flip();
		LoadTechs.loadBuildings(ta);
		
		SDLGfx.stringRGBA( screen, 10, 40, "Loading units", 0, 0, 255, 255 );
		screen.flip();
		LoadTechs.loadUnits(ta);

		SDLGfx.stringRGBA( screen, 10, 50, "Loading master position", 0, 0, 255, 255 );
		screen.flip();
		screen.fillRect( screen.mapRGB(0,0,0) );
		viewPort = new SDLRect( 0, 0, RESOLUTION_X, RESOLUTION_Y );

		/*Tech root = ta.techList.get(0);
		root.position.x = root.position.y = 0;
		for( Tech t : root.children )
		{
			for( Tech tc : t.children )
			{
				tc.position.x = tc.position.y = 0;
			}
			t.position.x = t.position.y = 0;
		}*/

		ta.loadPositions("");
	}
	
	private void changeZoom( double changeZoom )
	{
		zoomFactor += changeZoom;
		viewPort.width = (int)(RESOLUTION_X*(1/zoomFactor));
		viewPort.height = (int)(RESOLUTION_Y*(1/zoomFactor));
		msg = "Zoom: " + zoomFactor;
	}
	
	private Tech findClickedTech( int mx, int my )
	{
		for( Tech t : ta.techList )
		{
			// hit test
			if( (mx > t.position.x) && (mx < t.position.x+t.position.width) &&
				  (my > t.position.y) && (my < t.position.y+t.position.height) )
			{
				return t;
			}
		}
		
		return null;
	}
	
	private void start()
	{
		int mouseDownX = 0;
		int mouseDownY = 0;
		Tech mouseDownT = null;
		try
		{
			// init SDL
			SDLMain.init( SDLMain.SDL_INIT_VIDEO );

			long flags = SDLVideo.SDL_HWSURFACE | SDLVideo.SDL_DOUBLEBUF;
			if( FULLSCREEN ) flags |= SDLVideo.SDL_FULLSCREEN;
			screen = SDLVideo.setVideoMode( RESOLUTION_X, RESOLUTION_Y, 32, flags );

			init();
			
			draw();
			screen.flip();

			boolean quit = false;
			while( !quit )
			{
				SDLEvent event = SDLEvent.waitEvent( true );

				if( event instanceof SDLQuitEvent )
				{
					quit = true;
					continue;
				}
				
				if( event instanceof SDLKeyboardEvent )
				{
					SDLKeyboardEvent kevent = (SDLKeyboardEvent) event;
					if( kevent.getState() == SDLPressedState.PRESSED )
					{
						// ugly !!!!!
						if( kevent.getSym() == SDLKey.SDLK_h )
						{
							screen.fillRect( screen.mapRGB(0,0,0) );
							int helpY = 100;
							SDLGfx.stringRGBA( screen, 100, helpY, "HELP - Shortcut Keys", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "1..5: save at position 1..5", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "6..0: load from position 1..5", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "s: save master position", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "l: load master position", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "p: toggle progress/info mode", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "g: toggle show goal only", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "c: set all techs to not-researched", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "f: set all techs on path to goal to researched", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "x: clear tech goal", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "arrow keys: scroll", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "+/- (keypad): zoom in/out", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "h: this help screen", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "ESC: exit program", 255, 255, 255, 255 );
							helpY += 40;
							SDLGfx.stringRGBA( screen, 100, helpY, "HELP - Mouse Buttons", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "Information mode:", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "  Left:  drag tech", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "  Right: see tech details", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "Progress mode:", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "  Left:  toggle tech researched status", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "  Right: set goal to tech (or remove goal)", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "Detail mode:", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "  Left:  show details about building or unit", 255, 255, 255, 255 );
							helpY += 20;
							SDLGfx.stringRGBA( screen, 100, helpY, "  Right: exit detail screen", 255, 255, 255, 255 );
							helpY += 50;
							SDLGfx.stringRGBA( screen, 100, helpY, "Press space to leave this help", 255, 255, 255, 255 );
							screen.flip();
							continue;
						}

						switch( kevent.getSym() )
						{
							case SDLKey.SDLK_ESCAPE:
								quit = true;
								break;
							case SDLKey.SDLK_KP_PLUS:
							case SDLKey.SDLK_EQUALS:
								changeZoom(ZOOMSTEP);
								break;
							case SDLKey.SDLK_KP_MINUS:
							case SDLKey.SDLK_MINUS:
								changeZoom(-ZOOMSTEP);
								break;
							case SDLKey.SDLK_DOWN:
							case SDLKey.SDLK_KP2:
								viewPort.y -= SCROLLSTEP;
								msg = "Viewport: ("+viewPort.x+","+viewPort.y+","+viewPort.width+","+viewPort.height+")";
								break;
							case SDLKey.SDLK_LEFT:
							case SDLKey.SDLK_KP4:
								viewPort.x += SCROLLSTEP;
								msg = "Viewport: ("+viewPort.x+","+viewPort.y+","+viewPort.width+","+viewPort.height+")";
								break;
							case SDLKey.SDLK_UP:
							case SDLKey.SDLK_KP8:
								viewPort.y += SCROLLSTEP;
								msg = "Viewport: ("+viewPort.x+","+viewPort.y+","+viewPort.width+","+viewPort.height+")";
								break;
							case SDLKey.SDLK_RIGHT:
							case SDLKey.SDLK_KP6:
								viewPort.x -= SCROLLSTEP;
								msg = "Viewport: ("+viewPort.x+","+viewPort.y+","+viewPort.width+","+viewPort.height+")";
								break;
							case SDLKey.SDLK_p:
								Tech.showProgress = !Tech.showProgress;
								if( Tech.showProgress ) 
									msg = "Progress mode";
								else
									msg = "Information mode";
								break;
							case SDLKey.SDLK_g:
								Tech.goalOnly = !Tech.goalOnly;
								break;
							case SDLKey.SDLK_s:
								ta.savePositions("");
								break;
							case SDLKey.SDLK_l:
								ta.loadPositions("");
								break;
							case SDLKey.SDLK_1:
								ta.savePositions("1");
								break;
							case SDLKey.SDLK_2:
								ta.savePositions("2");
								break;
							case SDLKey.SDLK_3:
								ta.savePositions("3");
								break;
							case SDLKey.SDLK_4:
								ta.savePositions("4");
								break;
							case SDLKey.SDLK_5:
								ta.savePositions("5");
								break;
							case SDLKey.SDLK_6:
								ta.loadPositions("1");
								break;
							case SDLKey.SDLK_7:
								ta.loadPositions("2");
								break;
							case SDLKey.SDLK_8:
								ta.loadPositions("3");
								break;
							case SDLKey.SDLK_9:
								ta.loadPositions("4");
								break;
							case SDLKey.SDLK_0:
								ta.loadPositions("5");
								break;
							case SDLKey.SDLK_c:
								for( Tech cl : ta.techList )
								{
									cl.researched = false;
									ta.techList.get(0).researched = true; // special case None
								}
								break;
							case SDLKey.SDLK_f:
								if( null != Tech.techGoal )
								{
									for( Tech fg : ta.techList )
									{
										if( fg.searchTech(Tech.techGoal) )
											fg.researched = true;
									}
								}
								break;
							case SDLKey.SDLK_x:
								Tech.techGoal = null;
								Tech.goalOnly = false;
								break;
							case SDLKey.SDLK_z:
								if( null != Tech.techGoal )
								{
									Tech tt = ta.findTech( Tech.techGoal );
									if( null != tt )
									{
										//-tt.position.x = 3000;
										tt.position.y = 3000;
									}
								}
								break;
							case SDLKey.SDLK_a:
								for( Tech tt : ta.techList )
								{
									if( tt.position.y == 3000 )
									{
										// count how many left to do
										int nrNeeded = 0;
										for( Tech ttt : ta.techList  )
										{
											if( ttt.searchTech(tt.name) && !ttt.researched && ttt!=tt)
												nrNeeded++;
										}
										tt.position.x = 100 + 400*(nrNeeded/10);
									}
								}
								break;
							case SDLKey.SDLK_F1:
								for( Tech tt : ta.techList )
								{
									if( tt.position.y == 2000 )
									{
										tt.position.y = 3000;
									}
								}
								break;
						}
						draw();
						
					}
				}

				if( event instanceof SDLMouseButtonEvent )
				{
					SDLMouseButtonEvent mevent = (SDLMouseButtonEvent) event;
					int mx = mevent.getX();
					int my = mevent.getY();
					//scroll
					mx -= viewPort.x;
					my -= viewPort.y;
					//scale
					mx = (int)(mx*(1/zoomFactor));
					my = (int)(my*(1/zoomFactor));

					if( ( mevent.getButton() == SDLMouseButtonEvent.SDL_BUTTON_LEFT ) &&
					    ( mevent.getState() == SDLPressedState.PRESSED ) )
					{
						if( null != Tech.detailTech )
						{
							if( !Tech.detailClicked )
								Tech.detailTech.detailClicked(mevent.getY(), screen);
							else
							{	
								Tech.detailClicked = false;
								draw();
							}
							continue;
						}
						Tech t = findClickedTech( mx, my );
						if( null != t )
						{
							if( Tech.showProgress )
							{
								if( null != t ) t.researched = !t.researched;
								// special case
								if( t.name.equals("None") ) t.researched = true;
								if( t.researched )
									msg = t.name + " set to RESEARCHED";
								else
									msg = t.name + " set to NOT-RESEARCHED";
							}
							else // drag mode
							{
								mouseDownX = mx;
								mouseDownY = my;
								mouseDownT = t;
								msg = t.name;
							}
							
							draw();
						}
					}

					if( ( mevent.getButton() == SDLMouseButtonEvent.SDL_BUTTON_LEFT ) &&
					    ( mevent.getState() == SDLPressedState.RELEASED ) )
					{
						if( null == Tech.detailTech)
						{
							if( !Tech.showProgress )
							{
								if( null != mouseDownT )
								{
									msg = mouseDownT.name;
									mouseDownT.position.x += mx - mouseDownX;
									mouseDownT.position.y += my - mouseDownY;
									mouseDownT = null;
									draw();
								}
							}
						}
					}
					
					if( ( mevent.getButton() == SDLMouseButtonEvent.SDL_BUTTON_RIGHT ) &&
				        ( mevent.getState() == SDLPressedState.PRESSED ) )
					{
						if( null != Tech.detailTech )
						{
							Tech.detailTech = null;
							Tech.detailClicked = false;
							draw();
							continue;
						}
						Tech.detailTech = null;
						if( Tech.showProgress )
						{
							Tech t = findClickedTech( mx, my );
							if( null != t )
							{
								if( t.name.equals(Tech.techGoal) )
									Tech.techGoal = null;
								else
								{
									Tech.techGoal = t.name;
									msg = "Goal : " + t.name;
								}
							}
							else
							{
								Tech.techGoal = null;
							}
							draw();
						}
						else	// not showprogress
						{
							Tech t = findClickedTech( mx, my );
							if( null != t )
								Tech.detailTech = t;
							else
								Tech.detailTech = null;
							draw();
						}
					}
				}
				
			}

			SDLMain.quit();
		}
		catch( SDLException sdle )
		{
			sdle.printStackTrace();
		}

	}
	
}
