package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import main.State;
import action.Casting;
import action.Move;
import action.Hunt;
import antiban.Handler;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(
	author = "Hao2Bot", 
	info = "Curse any target, anywhere...", 
	logo = "", 
	name = "Hao2Curser", 
	version = 0.1
)
public class Main extends Script 
{
	
	private Script script;
	private long scriptStartTime;
	private String proposedTarget;
	private NPC target;
	private Position savedPosition;
	private String status;
	Image paint = getImage("http://i60.tinypic.com/2a50hg2.png");
	
	private Casting casting;
	private Move move;
	private Hunt hunt;
	private Handler antiBanHandler;
	
	@Override
	public void onPaint(Graphics2D g)
	{		
		// g.setColor(Color.BLACK);
		g.drawImage(this.paint, 251, 344, null);
	}
	
	@Override
	public void onStart()
	{
		status = "Initializing";
			
		// Set starting script time.
		scriptStartTime = System.currentTimeMillis();
		
		// Initialise classes.
		casting = new Casting(this);
		move = new Move(this);
		hunt = new Hunt(this);
		antiBanHandler = new Handler(this);
		
		// Set some nulls yo!
		savedPosition = null;
		target = null;
		// TODO: TEMPORARY
		proposedTarget = "Monk of Zamorak";
	}
	
	@Override
	public int onLoop() throws InterruptedException 
	{
		// If there isn't a saved position, set it.
		if(savedPosition == null){
			// Set the saved position to where the user is currently located.
			savedPosition = new Position(
				myPosition().getX(),
				myPosition().getY(),
				myPosition().getZ()
			);
		}
		
		// Get the state of the environment.
		State state = getState();
		
		// Determine what the script needs to do.
		switch (state)
		{
			case CAST:
				status = "Casting";
				// This method will cast the spell on the target (providing there is one).
				casting.cast(target);
			break;
			case FIND_TARGET:
				status = "Get Target";
				// This method will find a target which is not in physical combat, close and suitable.
				target = hunt.findClosestSuitableTarget(proposedTarget);
			break;
			case MOVE:
				status = "Moving";
				// If the user deviates from the starting location, use methods to return.
				move.moveToSavedPosition(savedPosition);
			break;
			default:
				logout("Could not determine a state, please screenshot and report steps to forum thread.");
			break;
		}
		
		// Always check for anti bans.
		if(antiBanHandler.nextAntiBan()){
			status = "AntiBan";
			antiBanHandler.performAntiBan();
		}
		
		return 50;
	}
	
	private State getState()
	{		
		// Priority 1: Make sure the user is in the designated spot.
		// Get current player position
		Position currentPosition = new Position(
			myPosition().getX(),
			myPosition().getY(),
			myPosition().getZ()
		);
		if(savedPosition.distance(currentPosition) >= 3){
			return State.MOVE;
		}
		
		// Priority 2: Always need a target in order to cast.
		if(target == null || (target != null && !target.isAttackable())){
			return State.FIND_TARGET;
		}
		
		// If the function makes it here, we are good to go!
		return State.CAST;
	}
	
	public void logout(String message)
	{
		// Log out the user, display the message given in the console and stop the script.
		logger.info(message);
		script.stop();
	}
	
	private String runTime(long i)
	{
		DecimalFormat nf = new DecimalFormat("00");
		long millis = System.currentTimeMillis() - i;
		long hours = millis / 3600000L;
		millis -= hours * 3600000L;
		long minutes = millis / 60000L;
		millis -= minutes * 60000L;
		long seconds = millis / 1000L;
		return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
	}

	private Image getImage(String url)
    {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {}
		return null;
    }
}
