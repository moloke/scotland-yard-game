# scotland-yard-game
Java implementation of the hide-seek board game Scotland Yard

CONTENTS OF THIS FILE
---------------------

 * Introduction
 * Requirements
 * Loading up Scotland Yard
 * Gameplay
 * FYIs

---------------------
INTRODUCTION
------------

The purpose of this project is to explore the possible ways to create an AI opponent capable of playing the board game Scotland Yard. The project discusses the ways in which existing algorithms for game playing, namely Minimax and Monte Carlo Tree Searching, can be adapted to determine moves for a Scotland Yard AI to play. This report also covers the process of creating a suitable digital Scotland Yard game representation for an end user, and a GUI to view the progress of the game. In addition to this, the project also investigates the use of Alpha-Beta pruning to speed up the computation of the AI agent determining the current game state, and ultimately deciding the most optimal move from there. This range of possibilities to design the AI are then put into practice, with the appropriate methods implemented, resulting in an accurate recreation of the original Scotland Yard board game, with an opponent that uses AI methods to make decisions during gameplay as an opponent.


	* For a full description of the project please see: 
	FYP-ScotlandYard.pdf

------------

REQUIREMENTS
------------

This is a java application, and thus certain requirements must be
met in order to run it.

 * Java JDK (https://www.oracle.com/uk/java/technologies/javase/javase-jdk8-downloads.html)
 * Java JRE (https://www.oracle.com/uk/java/technologies/javase-jre8-downloads.html)
 * Java Jar launcher (http://www.java2s.com/Code/Jar/l/Downloadlauncherjar.htm)

------------

LOADING UP SCOTLAND YARD
------------------------

To load up the application (method 1):

1) Move the ScotlandYardJarProject to your name in the Users folder

2) Navigate to the .jar file i.e:

ScotlandYardJarProject-> out -> artifacts -> ScotlandYardJarProject_jar -> ScotlandYardJarProject.jar

3) Double-clicking this file should automatically launch the application


To load up the application (method 2):

1) Open up a Java IDE (Netbeans, Eclipse, Intellij)

2) Open the project folder within the IDE

3) Navigate to “main.java”:

ScotlandYardJarProject-> src -> sy -> fxgui -> main.java

4) Double click to open this up in the editor

5) Once fully loaded, hit run to run the application

------------------------

GAMEPLAY
--------

Once the application has been successfully launched, you will be presented with a dialog box prompting you to choose who you want to play as. Select your player.

If playing as Mr. X, you will be making the first move. Your mission is to traverse the map of London, by clicking on the cell indicators on the map, in such a way that you avoid getting captured by any one of the detectives. Do this for all 24 rounds, and you win!
You have unlimited transport tickets at your disposal, with 2 double-moves (allowing you to effectively skip the detectives’ turn to move) and 5 blackfares (allowing you to hide which mode of transport you have used, and also travel by boat). You are “captured” if any one of the detectives is at the same node on the map as you at any one time.

If playing as the detectives, you will be making your moves after Mr. X. Your mission is to control all five detectives to traverse the map of London, by clicking on the cell indicators on the map, in such a way as to close in on, and eventually capture, Mr. X. His whereabouts is only revealed in rounds 3, 8, 13, 18 and 24 of the game. 
You have 10 bus tickets, 8 taxis and 4 underground tickets at your disposal. These are not replenished throughout the game so use them wisely! The game ends either if you capture Mr. X by landing on the same node as him at any one time on the map, or if Mr. X goes 24 rounds without getting captured. 

The full list of game rules can be found at this link:
https://desktopgames.com.ua/games/199/scotlandyard_rules_en.pdf

--------

FYIs
----

The current player’s turn is highlighted in the player pane to the right.

The spinning loading sign indicates the AI is determining its next move, please wait for this to finish.

Because of assets used in the application, these cannot be resized but the window can be. For optimal playing conditions we recommend a screen of at least 1300x750px.





