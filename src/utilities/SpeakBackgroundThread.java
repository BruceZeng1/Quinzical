package utilities;

import java.text.Normalizer;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Thread to manage speaking in background to avoid freezing
 * @author Adam Wiener
 *
 */
public class SpeakBackgroundThread extends Thread{
	
	private BashCommand speak;
	private String _text;
	private double _speed;
	private Timer _timer;
	private TimerTask _task;
	
	public SpeakBackgroundThread(String text, double speed) {
		_text = Normalizer.normalize(text, Normalizer.Form.NFD);
		_text = _text.replaceAll("[^\\p{ASCII}]", "");
		_speed = 1/speed;
	}
	
	public SpeakBackgroundThread(String text, double speed, Timer timer, TimerTask task) {
		_text = Normalizer.normalize(text, Normalizer.Form.NFD);
		_text = _text.replaceAll("[^\\p{ASCII}]", "");
		_speed = 1/speed;
		_timer = timer;
		_task = task;
	}
	
	@Override
	public void run(){
		String sayCommand = "(SayText " + "\\\"" + _text + "\\\")";
		String settings = "(voice_akl_nz_jdt_diphone)\n(Parameter.set 'Duration_Stretch " + _speed + ")\n";
		BashCommand writeToTTS = new BashCommand("echo \""+settings+sayCommand+"\" > tmp/tts.scm");
		writeToTTS.endProcess();
		speak = new BashCommand("festival -b tmp/tts.scm");
		speak.endProcess();
		if (_timer != null) {
			try {
				_timer.scheduleAtFixedRate(_task, 1000, 1000);
			} catch (IllegalStateException e) {}
		}
	}
	
	/**
	 * Says the provided text with the given speed
	 * @param toSay string to be said	
	 * @param speed speed with which the say string
	 */
	public static SpeakBackgroundThread speak(String toSay, double speed){
		SpeakBackgroundThread thread = new SpeakBackgroundThread(toSay, speed);
		thread.start();
		return thread;
	}
	
	/**
	 * Says the provided text with the given speed and starts the given timer when done
	 * @param toSay string to be said	
	 * @param speed speed with which the say string
	 * @param timer the timer to start
	 * @param task the timer task to initiate
	 */
	public static SpeakBackgroundThread speakQuestion(String toSay, double speed, Timer timer, TimerTask task) {
		SpeakBackgroundThread thread = new SpeakBackgroundThread(toSay, speed, timer, task);
		thread.start();
		return thread;
	}
	
	public void stopSpeaking() {
		speak.killProcessAndChildren();
	}
	
	
}
