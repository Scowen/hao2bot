Hao2Bot Curser
============

Cursing script for www.osbot.org


#About This Project
This project is written in Java, using the API from www.osbot.org to utilize pre-defined functions and algorithms. 
The project itself is a "script", it runs on the OSbot client. The scripts features can be found below.

#Features
Well, here they are:
 - Cursing (obviously).
 - Position Memory.
  - The script will remember where the user is cursing from, should the user deviate from that position, the script will attempt to return to that position before continuing.
 - AntiBan
  - The script includes several custom AntiBan features, these include:
   - Small human-like movements.
   - Tab switching (ie: checking magic, friends online, a quest, etc.).
   - Short AFK periods (as if the user was alt-tabbing).
   - Middle mouse button camera movement.
   - Random intervals between clicks and wait periods.
 - Rune Checking
  - The script will keep small track of the runes the user has, making sure to log out when finished.
 - Target finding
  - The user can specify any target around them, the script will look for the most suitable target matching the description and use that as the primary target.

#Script Requirements
In order to run this script, you will need to meet the following requirements.
 - Level 19 Magic
 - 3 Earth, 2 Water, 1 Body rune per spell (Use staffs)
 - A target (preferably one you can safe-spot)
 - A magic attack bonus of -65 or below (so you splash every hit)

#Fork Requirements
In order to fork this script, to modify. You will need to download the latest version of OSbot and use as an external library.
