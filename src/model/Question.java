package model;

import java.text.Normalizer;


/**
 * Class representing a question
 * @author Adam Wiener and Bruce Zeng
 *
 */
public class Question {

	private String _questionString;
	private String _prefix;
	private String _question;
	private String _answer;
	
	/**
	 * @param qString string from category file
	 */
	public Question(String qString) {
		_questionString = qString;
	
		String[] arr = _questionString.split("\\|");
		try {
			_question = arr[0];
			_prefix = arr[1];
			_answer = arr[2];
		} catch ( Exception e){
			System.out.println("The Question "+_questionString+"is incorrecly formatted");
			System.out.println("Please change to QUESTION|ANSWER|PREFIX\ndelete tmp/ if you encounter errors");
			_question = "Incorrectly formatted";
			_prefix = "Incorrectly formatted";
			_answer = "Incorrectly formatted";
		}
	}
	
	/**
	 * 
	 * @param answer answer to question
	 * @return boolean true if answer is correct and false otherwise
	 */
	public boolean answerQuestion(String answer) {
		String formattedUserAnswer = (answer.replaceAll("\\s+", " ").trim().toLowerCase());
		String[] stringArray = _answer.split("/");
		for (String string: stringArray) {
			if (answer.equals(string.toLowerCase())) {
				return true;
			}
			string = Normalizer.normalize(string, Normalizer.Form.NFD);
			string = string.replaceAll("[^\\p{ASCII}]", "");
			if (formattedUserAnswer.equals(string.toLowerCase())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return value of question
	 */
	public String getPrefix() {
		return _prefix;
	}
	
	/**
	 * 
	 * @return question being asked
	 */
	public String getQuestion() {
		return _question;
	}
	
	/**
	 * 
	 * @return answer to question
	 */
	public String getAnswer() {
		return _answer;
	}
	
	public String toString() {
		return _questionString;
	}
}
