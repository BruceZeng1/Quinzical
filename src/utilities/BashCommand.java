package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

/**
 * The BashCommand class handles the running of bash commands and allows reading of output
 * @author Adam Wiener
 *
 */
public class BashCommand {

	private BufferedReader _stdOut;
	private BufferedReader stderr;
	private String _stdErr;
	private int _exitStatus;
	private Process process;
	
	/**
	 * 
	 * @param cmd command to be run
	 */
	public BashCommand(String cmd){
		try {
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			process = builder.start();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			_stdOut = stdout;
		} catch (IOException e) {e.printStackTrace();
		} finally {}
		
	}
	
	public int getExitStatus() {
		try {
			process.waitFor();
			_exitStatus = process.exitValue();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return _exitStatus;
	}
	
	/**
	 * returns stdOut for command,
	 * @return stdOut, will return null if no more lines are produced
	 */
	public String getStdOut() {
		String toReturn = null;
		try {
			process.waitFor();
			toReturn = _stdOut.readLine();
		} catch (IOException | InterruptedException e) {
			System.out.println("failed to read command");
		}
		return toReturn;
	}
	
	/**
	 * waits for the call to finish before killing the process
	 */
	public void endProcess() {
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		process.destroy();
	}
	
	/**
	 * Kills the process regardless of if it has finished
	 */
	public void killProcessAndChildren() {
		//taken from Shrey Tailor's response to question @174 on piazza
		Stream<ProcessHandle> descendents = process.descendants();
		descendents.filter(ProcessHandle::isAlive).forEach(ph -> {
		    ph.destroy();
		});
		process.destroy();
	}
	
	public String getStdErr() {
		try {
			process.waitFor();
			stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			_stdErr = stderr.readLine();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return _stdErr;
	}
}
