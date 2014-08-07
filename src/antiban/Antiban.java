package antiban;

import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;

public class Antiban {
	
	private Script script;
	
	public Antiban(Script script)
	{
		this.script = script;
	}
	
	// Short 5 to 20 second Alt-tab, Simple.
	public void simulateAltTab() throws InterruptedException
	{
		// Move the mouse out of the screen.
		script.mouse.moveOutsideScreen();
		// Have a little kip!
		script.sleep(script.random(4000, 10000));
	}
	
	// Check friends tab to see who is online.
	public void checkFriendsOnline() throws InterruptedException
	{
		// Open up the friends tab.
		script.tabs.open(Tab.FRIENDS);
		// Pretend to have a little gander for a few seconds.
		script.sleep(script.random(1000, 4000));
	}
	
	// Move the camera to a random position.
	public void moveCamera()
	{
		// Randomize moving the camera up or down, or side to side
		if(script.random(1,2) == 1)
			script.camera.movePitch(script.random(1,359));
		else 
			script.camera.moveYaw(script.random(40,70));	
	}
	
	// Check runes left in inventory
	public void checkInventory() throws InterruptedException
	{
		// Open up the inventory tab.
		script.tabs.open(Tab.INVENTORY);
		// Pretend to have a little gander for a few seconds.
		script.sleep(script.random(1000, 4000));
	}
	
	// Check the progress on skill
	public void checkSkill() throws InterruptedException
	{
		// Open the skills tab
		script.tabs.open(Tab.SKILLS);
		// Move the mouse over the skill itself
		script.mouse.move(script.random(552, 605), script.random(363, 387));
		// Have a looksie for a few seconds.
		script.sleep(script.random(1200,4500));
	}
	
	// Just move the mouse a bit.
	public void randomMouseMovement() throws InterruptedException
	{
		script.mouse.moveRandomly(script.random(100,1000));
	}
}
