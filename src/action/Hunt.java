package action;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;

public class Hunt {

	private Script script;
	
	public Hunt(Script script)
	{
		this.script = script;
	}
	
	public NPC findClosestSuitableTarget(String target)
	{
		NPC prey = null;
		int distance = 0;
		
		// Get all NPCs, iterate, find the suitable target.
		for(NPC npc : script.npcs.getAll()){
			if(npc != null){ // If the NPC has data.
				if(npc.exists()){ // If the NPC "exists".
					if(npc.getName().equalsIgnoreCase(target)){ // If the NPC matches the descriptor.
						if(npc.isAttackable()){ // If the NPC is attackable.
							// Finally, if the prey has not been set, or the distance, assign the prey.
							// Or, if the prey has been set, but the NPC is closer that the previous, assign the prey.
							if( (prey == null || distance == 0) || (prey != null && distance > script.map.distance(npc)) ){
								distance = script.map.distance(npc);
								prey = npc;
							}
						}
					}
				}
			}
		}
		
		// If nothing matches these parameters then I need to re-work this.
				
		return prey;
	}
	
}
