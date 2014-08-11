package action;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Spell;
import org.osbot.rs07.api.ui.Tab;
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
			if(script.magic.isSpellSelected()){
				script.magic.deselectSpell();
				script.tabs.open(Tab.INVENTORY);
			}
			
			script.magic.castSpellOnEntity(Spell.CURSE, target);
			script.sleep(script.random(41, 1388));
		}
	}
	
	// Check if the user has the rune(s) required for the spell.
	public void checkRunes()
	{
		// TODO: If a message appears:
		// You do not have enough XXXX Runes to cast this spell.
		// Then quit.
		
		// Check if it contains any body runes.
		if(!script.inventory.contains(runes[1])){
			main.logout("No runes left for spell, logging out.");
		}
	}
	
}