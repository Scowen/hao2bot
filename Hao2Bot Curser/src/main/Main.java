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
import gui.GUI;

import org.osbot.rs07.api.Skills;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
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
	private long startScriptTime;
	private int startMagicExp;
	private int startMagicLevel;
	private String phMagicExp;
	
	public static String proposedTarget;
	private NPC target;
	private Position savedPosition;
	private String status;
	Image paint = getImage("http://www.ourgoose.com/paint.png");
	public static boolean guiActive;
	
	private Casting casting;
	private Move move;
	private Hunt hunt;
	private Handler antiBanHandler;
	private GUI gui;
	
	
	@Override
	public void onPaint(Graphics2D g)
	{	
		// Use the X and Y for paint relative items.
		int x = 251, y = 344;
		
		// Draw the paint image.
		g.drawImage(this.paint, x, y, null);
		
		// Draw the information.
		g.setColor(Color.WHITE);
		g.drawString("Status: " + status, x + 20, y + 50);
		g.drawString("Run Time: " + runTime(startScriptTime), x + 20, y + 70);
		//int gainedMagicExp = script.skills.getExperience(Skill.MAGIC) - startMagicExp;
		//g.drawString("Exp (p/h): " + gainedMagicExp + "(" + phMagicExp  + "k)", x + 10, y + 90);
		//int gainedMagicLevel = script.skills.getStatic(Skill.MAGIC) - startMagicLevel;
		//g.drawString("Level: " + script.skills.getStatic(Skill.MAGIC) + "(+" + gainedMagicLevel  + ")", x + 10, y + 100);
	}
	
	@Override
	public void onStart()
	{
		status = "Initializing Script";
		
		// Set GUI
		gui = new GUI();
		guiActive = true;
		gui.setEnabled(true);
		gui.setVisible(true);
			
		// Set starting script time.
		startScriptTime = System.currentTimeMillis();
		
		// Initialise classes.
		casting = new Casting(this);
		move = new Move(this);
		hunt = new Hunt(this);
		antiBanHandler = new Handler(this);
		
		// Set some nulls yo!
		savedPosition = null;
		target = null;
		proposedTarget = null;
		startMagicExp = 0;
		startMagicLevel = 0;
	}
	
	@Override
	public int onLoop() throws InterruptedException 
	{	
		if(proposedTarget != null)
			logger.info(proposedTarget);
		
		// Check if the GUI is still open
		if(checkGUIActive())
			return 100;
		
		// Update the paint stats with the users stats.
		updateStats();
		
		// Check the script has placed a saved position
		checkSavedPosition();
		
		// Get the state of the environment.
		State state = getState();
		
		// Determine what the script needs to do.
		switch (state)
		{
			case CAST:
				status = "Casting on Target";
				// This method will cast the spell on the target (providing there is one).
				casting.cast(target);
			break;
			case FIND_TARGET:
				status = "Searching for Target";
				// This method will find a target which is not in physical combat, close and suitable.
				target = hunt.findClosestSuitableTarget(proposedTarget);
			break;
			case MOVE:
				status = "Moving to Saved Position";
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
		
		return 60;
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
	
	public void updateStats()
	{
		// Set starting magic experience and level
		if(startMagicExp == 0)
			startMagicExp = script.skills.getExperience(Skill.MAGIC);
		if(startMagicExp == 0)
			startMagicExp = script.skills.getStatic(Skill.MAGIC);
		
		// Get the magic exp per hour
		getExpPerHour();
	}
	
	public void getExpPerHour()
	{
		phMagicExp = Integer.toString(Math.round((expPerHour(startScriptTime, startMagicExp, Skill.MAGIC) / 1000)), 1);
	}
	
	public boolean checkGUIActive()
	{
		// If the GUI is still open, wait for that.
		if(guiActive){
			// Check if the GUI has been closed.
			if(proposedTarget != null){
				// Set all properties false if one of the statements is true.
				guiActive = false;
				gui.setVisible(false);
				gui.setEnabled(false);
				return false;
			} else {
				status = "Waiting for GUI Input";
				return true;
			}
		} else {
			return false;
		}
	}
	
	public void checkSavedPosition()
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
	}
	
	public void logout(String message)
	{
		// Log out the user, display the message given in the console and stop the script.
		status = "Logging Out";
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
	
	public int expPerHour(double startTime, int startExp, Skill skill){
		return ((int)(3600000.0D / (System.currentTimeMillis() - startTime) * (script.skills.getExperience(skill) - startExp)));
	}

	private Image getImage(String url)
    {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {}
		return null;
    }
}
