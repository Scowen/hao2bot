package antiban;

import java.util.Random;

import org.osbot.rs07.script.Script;

import antiban.Antiban;

public class Handler {

	private Script script;
	private long nextAntiban;
	
	private Antiban antiban;
	
	public Handler(Script script)
	{
		this.script = script;
		this.nextAntiban = getUnixTime() + randInt(30,120);
		
		antiban = new Antiban(script);
	}
	
	public void performAntiBan() throws InterruptedException
	{
		// Check if an anti ban is due.
		if(getUnixTime() >= nextAntiban){
			// Generate a random number, depending on the number of anti ban functions we have. Find its function.
			switch(randInt(1,6)){
				case 1:
					// Move the camera out of the screen and sleep.
					antiban.simulateAltTab();
				break;
				case 2:
					// Open friends tab and sleep.
					antiban.checkFriendsOnline();
				break;
				case 3:
					// Move camera pitch/yaw.
					antiban.moveCamera();
				break;
				case 4:
					// Open inventory and sleep.
					antiban.checkInventory();
				break;
				case 5:
					// Open skills tab and hover over magic.
					antiban.checkSkill();
				break;
				case 6:
					// Use pre-defined methods and move mouse randomly.
					antiban.randomMouseMovement();
				break;
				default:
					
				break;
			}
			// Add some time to the next antiban
			nextAntiban = getUnixTime() + randInt(30,90);
		}		
	}
	
	// Using unix time to determine when to next perform major and minor anti bans
	private long getUnixTime()
	{
		return System.currentTimeMillis() / 1000L;
	}
	
	// Generate a random in range integer
	public int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
}
