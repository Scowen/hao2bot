package main;

import java.awt.Graphics;

import main.State;
import action.Casting;
import action.Move;
import action.Hunt;

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
	
	private Casting casting;
	private Move move;
	private Hunt hunt;
	
	public void onStart()
	{
		// Set starting script time.
		scriptStartTime = System.currentTimeMillis();
		
		// Initialise classes.
		casting = new Casting(this);
		move = new Move(this);
		hunt = new Hunt(this);
		
		// Set some nulls yo!
		savedPosition = null;
		target = null;
		// TODO: TEMPORARY
		proposedTarget = "Monk of Zamorak";
		
		status = "Initializing";
		
		script.mouse.setSpeed(1);
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
		
		logger.info(state);
		
		// Determine what the script needs to do.
		switch (state)
		{
			case CAST:
				// This method will cast the spell on the target (providing there is one).
				casting.cast(target);
				status = "Casting";
			break;
			case FIND_TARGET:
				// This method will find a target which is not in physical combat, close and suitable.
				target = hunt.findClosestSuitableTarget(proposedTarget);
				status = "Finding Target";
			break;
			case MOVE:
				// If the user deviates from the starting location, use methods to return.
				move.moveToSavedPosition(savedPosition);
				status = "Moving";
			case ANTIBAN:
				// Every X amount of time, an AntiBan procedure will be carried out.
				// antiBan();
			break;
			default:
				logout("Could not determine a state, please screenshot and report steps to forum thread.");
			break;
		}
		
		return 50;
	}
	
	public void onPaint(Graphics g)
	{
//		g.drawString("State: " + getState(), 400, 10);
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

}
