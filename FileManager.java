import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class FileManager {

	/**
	 * This method reads in the player data from text file containing the list of players
	 * @param names the file with the names of the players
	 * @return List of the players
	 * @throws FileNotFoundException if the file specified in the command line argument isn't found
	 */
	public static ArrayList<Player> getPlayerData(String names) throws FileNotFoundException
	{
		ArrayList<Player> players = new ArrayList<Player>();
		try
		{
			File playerInfo = new File(names);
			Scanner input = new Scanner(playerInfo);

			//This is the parsing. Once it reads in the first integer, it continues
			//to read in the name until it reaches another integer, then creates a
			//and adds it to an arraylist of player objects
			while(input.hasNext())
			{
				int playerNumber = input.nextInt();
				String playerName = input.next() + " ";
				
				while(!input.hasNextInt())
				{
					playerName += input.next() + " ";
					if(!input.hasNext())
					{
						break;
					}
				}
				
				players.add(new Player(playerNumber, playerName));
			}
		}

		catch(FileNotFoundException e)
		{
			System.err.println("File not found.");
			e.printStackTrace();
		}
		
		return players;
	}
	
	/**
	 * This method will get the results of the games and update the players' records and ranks
	 * @param games the file with the game results
	 * @param players the list of the players
	 * @throws FileNotFoundException if the file specified in the command line argument isn't found
	 */
	public static void getGameResults(String games, ArrayList<Player> players) throws FileNotFoundException
	{
		try 
		{
			File gameResults = new File(games);
			Scanner input = new Scanner(gameResults);
			
			//This while loop reads in the matches, increments the wins
			//and losses of each player, and then implements the ELO
			//algorithm and updates the rankings
			while(input.hasNextInt())
			{
				int winner = input.nextInt();
				int loser = input.nextInt();
					players.get(winner).addWin(players.get(loser));
					players.get(loser).addLoss(players.get(winner));
					Player.ELOAlgorithm(players.get(winner), players.get(loser));
			}
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File not found.");
			e.printStackTrace();
		}
	}
	
	/**
	 * This method creates an HTML file with the standings of the players
	 * @param players
	 * @throws IOException
	 */
	public static void outputStandingsHTML(ArrayList<Player> players) throws IOException
	{
		//Sort players by ranking
		Collections.sort(players);
		
		//Create formatted HTML string for players in table
		String tableContent = "";
				
		for(int i = 0; i<players.size(); i++)
		{
			tableContent += "<tr>\n";
			tableContent += "<td>" + Integer.toString(i + 1) + "</td>\n"; 
	        tableContent += "<td><a href = \"./Players/" + players.get(i).getName() + ".html\">" + players.get(i).getName() + "</a></td>\n";
			//tableContent += "<td>" + players.get(i).getName() + "</td>\n";
			tableContent += "<td>" + Integer.toString(players.get(i).getWins()) + "</td>\n";
			tableContent += "<td>" + Integer.toString(players.get(i).getLosses()) + "</td>\n";
			tableContent += "<td>" + Double.toString(players.get(i).getRanking()) + "</td>\n";
			tableContent += "</tr>\n";
		}
						
		try 
		{
			FileWriter writer = new FileWriter("Standings.html");
			BufferedWriter fileOut = new BufferedWriter(writer);

			//This line is formatted with the returns so it's easier to see what's being done
			fileOut.write("<head>\n" +
							"<h> " +
							"<font size = \"6\">Standings</font>" +
							"</h>\n" +
							"<body>\n" +
							"<table border=\"10\">\n" +
							"<tr>\n" +
							 "<th>Position</th>\n" +
							 "<th>Name</th>\n" +
							 "<th>Wins</th>\n" +							
							 "<th>Losses</th>\n" +
							 "<th>Ranking</th>\n" +
							"</tr>\n" + 
							tableContent +
							"\n</body>\n" +
							"</head>");
			fileOut.close();
		} 
		
		catch (IOException e) 
		{
			System.err.println("IOException found.");
			e.printStackTrace();
		}
	}
	
	/**
	 * This method creates the HTML file for each player and who they played
	 * @param players the list of the players in the tournament
	 */
	public static void outputPlayerHTML(ArrayList<Player> players)
	{
		//Creates a directory for all the individual player HTML files
		File playerFolder = new File("Players");
		playerFolder.mkdir();
		
		for(int i = 0; i<players.size();i++)
		{

			//These strings are the html for the wins and losses each player
			//achieved against their opponents
			String matchesWon = "", matchesLost = "";
			
			for(int j = 0; j<players.get(i).getMatchesWon().size(); j++)
			{
				matchesWon += "<tr>\n";
				matchesWon += "<td>" + players.get(i).getMatchesWon().get(j).getName() + "</td>\n";
				matchesWon += "<td>Win</td>\n";
				matchesWon += "<td>" + Double.toString(players.get(i).getMatchesWon().get(j).getRanking()) + "</td>\n";
				matchesWon += "</tr>\n";
			}
			
			for(int j = 0; j<players.get(i).getMatchesLost().size(); j++)
			{
				matchesLost += "<tr>\n";
				matchesLost += "<td>" + players.get(i).getMatchesLost().get(j).getName() + "</td>\n";
				matchesLost += "<td>Loss</td>\n";
				matchesLost += "<td>" + Double.toString(players.get(i).getMatchesLost().get(j).getRanking()) + "</td>\n";
				matchesLost += "</tr>\n";
			}
			
			try 
			{
				FileWriter writer = new FileWriter("./Players/" + players.get(i).getName() + ".html");
				BufferedWriter fileOut = new BufferedWriter(writer);
				//The HTML code for the individual player HTML files
				fileOut.write("<html>" +
						"<head>\n" +
								"<h>\n" +
								players.get(i).getName() + 
								"<p>Ranking:" +
								players.get(i).getRanking() + "</p>" +
								"\n<table border = \"10\">\n" +
								"<caption align = bottom>" +
								"<p>" + 
								"<a href = \"./../Standings.html\">Back to standings</a><BR>" +
								"*At end of tournament</p>" +
								"</caption>" +
									"<tr>\n" +
									"<th>Opponent</th>\n" +
									"<th>Win/Loss</th>" +
									"<th>Ranking*</th>" +
									"</tr>\n" +
									matchesWon +
									matchesLost +
								"\n</h>\n" +
								"</head>");
				fileOut.close();
			} 
			
			catch (IOException e) 
			{
				System.err.println("IOException found.");
				e.printStackTrace();
			}
		}
	}
}

