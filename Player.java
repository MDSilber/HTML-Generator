

import java.math.BigDecimal;
import java.util.ArrayList;

public class Player implements Comparable<Player> 
{

	private String name;
	private int number, wins = 0, losses = 0; 
	double ranking = 1500;
	private ArrayList<Player> matchesWon, matchesLost;
	
	/**
	 * This is the constructor for the Player Class
	 * @param playerName the name of the player
	 * @param playerNumber the number of the player (indexed 0-39)
	 */
	public Player(int playerNumber, String playerName)
	{
		name = playerName;
		number = playerNumber;
		matchesWon = new ArrayList<Player>();
		matchesLost = new ArrayList<Player>();
	}
	
	/**
	 * Accessor method for ranking
	 * @return player's ranking
	 */
	public double getRanking()
	{
		BigDecimal twoDigitRounding = new BigDecimal(ranking).setScale(2,BigDecimal.ROUND_HALF_UP);
		return twoDigitRounding.doubleValue();
	}
	
	/**
	 * Accessor method for name
	 * @return player's name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Accessor method for player number
	 * @return player's number
	 */
	public int getNumber()
	{
		return number;
	}
	
	/**
	 * Increment wins by 1
	 */
	public void addWin(Player opponent)
	{
		matchesWon.add(opponent);
		wins++;
	}
	
	/**
	 * Increment losses by 1
	 */
	public void addLoss(Player opponent)
	{
		matchesLost.add(opponent);
		losses++;
	}
	
	/**
	 * Accessor method for player's wins
	 * @return Player's wins
	 */
	public int getWins()
	{
		return wins;
	}
	
	/**
	 * Accessor method for player's losses
	 * @return Player's losses
	 */
	public int getLosses()
	{
		return losses;
	}
	
	/**
	 * Accessor method for the opponents this player defeated
	 * @return list of players this player defeated
	 */
	public ArrayList<Player> getMatchesWon()
	{
		return matchesWon;
	}
	
	/**
	 * Accessor method for the opponents to whom this player lost
	 * @return list of players to whom this player lost
	 */
	public ArrayList<Player> getMatchesLost()
	{
		return matchesLost;
	}
	
	public void updateRanking(double alteration)
	{
		ranking += alteration;
	}

	/**
	 * This method allows the use of the Collections API to sort the list of players
	 */
	@Override
	//Sort starting with highest
	public int compareTo(Player other) 
	{
		if(ranking>other.getRanking())
		{
			return -1;
		}
		else if(ranking<other.getRanking())
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * This method updates the rankings between two players after a match
	 * @param a The first player
	 * @param b The second player
	 */
	public static void ELOAlgorithm(Player a, Player b)
	{
		final int k = 25;
		double expectedScoreA = Math.pow(10,a.getRanking()/400)/(Math.pow(10,a.getRanking()/400) + Math.pow(10,b.getRanking()/400));
		double expectedScoreB = Math.pow(10,b.getRanking()/400)/(Math.pow(10,a.getRanking()/400) + Math.pow(10,b.getRanking()/400));
		double scoreA = a.getWins();
		double scoreB = b.getWins();
		
		a.updateRanking(k*(scoreA-expectedScoreA));
		b.updateRanking(k*(scoreB-expectedScoreB));
	}
}

