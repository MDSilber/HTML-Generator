import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

	/**
	 * Main method
	 * @param args command line arguments. The first should be names.txt, the second games.txt
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{		
		try 
		{
			ArrayList<Player> players = FileManager.getPlayerData(args[0]);
			FileManager.getGameResults(args[1],players);
			FileManager.outputPlayerHTML(players);
			FileManager.outputStandingsHTML(players);
		} 
		
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

}

