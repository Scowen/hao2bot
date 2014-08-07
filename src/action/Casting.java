package action;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Spell;
import org.osbot.rs07.script.Script;

import main.Main;

public class Casting {
	
	private Script script;
	
	public Casting(Script script)
	{
		this.script = script;
	}

	// Rune IDs, probably will only use Body though.
	private final int[] runes = {
		555, // Water Rune.
		559, // Body Rune.
		557  // Earth Rune.
	};
	
	private Main main;

	// Main function to cast Curse on a target.
	public void cast(NPC target) throws InterruptedException
	{
		// Check that the user has runes to cast with.
		//checkRunes(script);
		
		// Check if the target exists, can be seen and is actually interactable.
		if(target != null){
			if(target.isVisible()){
				if(script.magic.castSpellOnEntity(Spell.CURSE, target)){
					script.sleep(script.random(41, 1388));
				} else {
					cast(target);
				}
			} else {
				script.camera.toEntity(target);
			}
		}
	}
	
	// Check if the user has the rune(s) required for the spell.
	public void checkRunes()
	{
		// Get the player inventory.
		Inventory inv = script.getInventory();
		
		// TODO: If a message appears:
		// You do not have enough XXXX Runes to cast this spell.
		// Then quit.
		
		// Check if it contains any body runes.
		if(!inv.contains(runes[1])){
			main.logout("No runes left for spell, logging out.");
		}
	}
	
}
/*
e static final Area SMITHING_AREA = new Area(3185,3420,3190,3427);
private enum State{
	SMITH, BANK, WALK_TO_SMITH, WALK_TO_BANK
};
private State getState(){
	if(inventory.contains("Iron bar") && SMITHING_AREA.contains(myPlayer().getPosition())){
		return State.SMITH;
	}
	else if(!inventory.contains("Iron bar") && !SMITHING_AREA.contains(myPlayer().getPosition())){
		return State.BANK;
	}
	else if(inventory.contains("Iron bar") && !SMITHING_AREA.contains(myPlayer().getPosition())){
		return State.WALK_TO_SMITH;
	}
	else{
		return State.WALK_TO_BANK;
	}
}

public int onLoop() throws InterruptedException{
	switch(getState()){
		case SMITH:
			outerIf:
			if(!myPlayer().isAnimating()){
				sleep(1000);
				if(!inventory.contains("Iron bar")){
					sleep(500);
					break outerIf;
				}
				int failSafe = 0;
				while(!myPlayer().isAnimating()){
					sleep(50);
					failSafe++;
					if(failSafe >= 10){
						inventory.interactWithNameThatContains("Use", "Iron bar");
						RS2Object anvil = objects.closest(2097);
						mouse.click(new EntityDestination(bot, anvil));
						if(interfaces.get(312) != null){
							
						}
						sleep(1500);
						mouse.move(370,180);
						mouse.click(true);
						sleep(250);
						mouse.move(365,250);
						mouse.click(false);
						sleep(1000);
						keyboard.typeString("27");
						sleep(500);
						break outerIf;
					}
				}
			}
			break;
		case BANK:
			RS2Object bankBooth = objects.closest("Bank booth");
		    if (bankBooth != null) {
		        if (bankBooth.interact("Bank")) {
		        	while(!bank.isOpen()){
		        		sleep(250);
		        	}
		        	bank.depositAll("Iron knife");
		        	if(bank.contains("Iron bar")){
		        		bank.withdraw("Iron bar", 27);
		        	}
		        	else{
		        		stop();
		        	}
		        	bank.close();
		        }
		    }
		    break;
		case WALK_TO_BANK:
			walkTile(new Position(3185,3436,0));
			break;
		case WALK_TO_SMITH:
			walkTile(new Position(3185,3426,0));
			break;
	}
	return random(200,300);
}
private boolean walkTile(Position p) throws InterruptedException {
	mouse.move(new MiniMapTileDestination(bot, p), false);
	sleep(random(150, 250));
	mouse.click(false);
	int failsafe = 0;
	while (failsafe < 10 && myPlayer().getPosition().distance(p) > 2) {
		sleep(200);
		failsafe++;
		if (myPlayer().isMoving())
			failsafe = 0;
	}
	if (failsafe == 10)
		return false;
	return true;
}
public void onPaint(Graphics2D g) {
	 
}

}
*/
