package action;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.Script;

import main.Main;

public class Move {

	private Script script;
	private Main main;
	
	public Move(Script script)
	{
		this.script = script;
	}
	
	public void moveToSavedPosition(Position position)
	{	
		// The user has wandered, return them to the saved spot.
		
		// See if the user can reach the position again.
		if(!script.map.canReach(position))
			main.logout("Could not reach the starting location.");
		
		// Attempt to walk to the tile specified, else, try again damnit.
		if(!script.map.walk(position))
			moveToSavedPosition(position);

	}
	
}
