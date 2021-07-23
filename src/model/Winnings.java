package model;

import java.io.File;
import java.io.IOException;

import utilities.BashCommand;

/**
 * The winnings class manages winning and its saving and loading
 * @author Adam Wiener and Bruce Zeng
 *
 */
public class Winnings {
	private int _winnings;
	
	/**
	 * 
	 * @param winnings string containing winnings value as number
	 */
	public Winnings(String winnings) {
		_winnings = Integer.parseInt(winnings);
	}
	
	/**
	 * 
	 * @return winnings total winnings
	 */
	public int getWinnings() {
		return _winnings;
	}
	
	/**
	 * 
	 * @param qValue amount to add to winnings
	 */
	public void addToWinnings(int qValue) {
		_winnings += qValue;
	}
	
	/**
	 * save winnings to file
	 */
	public void saveWinnings() {
		BashCommand writeToWinnings = new BashCommand("echo "+_winnings+" > tmp/winnings");
		writeToWinnings.endProcess();
	}
	
	public String toString() {
		return ""+_winnings;
	}
	
	/**
	 * clear winnings from file and set to 0
	 */
	public void clearWinnings() {
		BashCommand writeToWinnings = new BashCommand("echo '0' > tmp/winnings");
		writeToWinnings.endProcess();
		_winnings = 0;
	}
	
	/**
	 * load winnings from file and output as string.
	 * If the winnings file does not exist then make it.
	 * @return winnings 
	 */
	public static String loadWinnings() {
			String winnings = "0";
			File tmp = new File("tmp");
			if(!tmp.exists()) {
				tmp.mkdir();
			}
			File winningsFile = new File("tmp/winnings");
			if(!winningsFile.exists()) {
				try {
					winningsFile.createNewFile();
					BashCommand writeToWinnings = new BashCommand("echo '0' > tmp/winnings");
					writeToWinnings.endProcess();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				BashCommand getWinnings = new BashCommand("cat tmp/winnings");
				winnings = getWinnings.getStdOut();
				getWinnings.endProcess();
			}		
		return winnings;
	}
}
