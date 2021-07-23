package model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Manages a category of questions
 * @author Adam Wiener and Bruce Zeng
 *
 */
public class Category implements Comparable<Category> {
	private ArrayList<Question> _questions;
	private String _name;
	private int _numAnswered;
	
	/**
	 * 
	 * @param name The name of the category
	 */
	public Category(String name) {
		_name = name;
		_questions = new ArrayList<Question>();
	}
	
	/**
	 * 
	 * @param name The name of the category
	 * @param questions List of questions to add to the category
	 * @param numAnswered The number of questions that have been answered in the category.
	 */
	public Category (String name, ArrayList<Question> questions, int numAnswered) {
		_name = name;
		_questions = questions;
		_numAnswered = numAnswered;
	}
	
	/**
	 * 
	 * @return question A random question from the category
	 */
	public Question getRandomQuestion() {
		if (_questions.size()==0) {
			_questions.add(new Question("CATEGORY EMPTY|CATEGORY EMPTY|CATEGORY EMPTY"));
		}
		
		Random rand = new Random();
		int numQuestions = _questions.size();
		int index = rand.nextInt(numQuestions);
		return _questions.get(index);
	}
	
	/**
	 * Returns a list of 5 random questions from the category
	 * @return questions 5 random questions
	 */
	public ArrayList<Question> Get5Random() {
		ArrayList<Question> _5questions = new ArrayList<Question>();
		while (_5questions.size() < 5) {
			Question question = getRandomQuestion();
			if (!_5questions.contains(question)) {
				_5questions.add(question);
			}
		}
		return _5questions;
	}
	
	/**
	 * returns question at index
	 * @param index
	 * @return
	 */
	public Question getQuestion(int index) {
		return _questions.get(index);
	}
	
	/**
	 * Adds the question to the category
	 * @param question
	 */
	public void AddQuestion(Question question) {
		_questions.add(question);
	}
	
	public String toString() {
		String string = _name;
		return string;
	}
	
	/**
	 * 
	 * @return name The name of the category
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * 
	 * @return numAnswered the number of answered questions in the cateogry
	 */
	public int getNumAnswered() {
		return _numAnswered;
	}
	
	/**
	 * 
	 * @param numAnswered the number of answered questions in the categorys
	 */
	public void setNumAnswered(int numAnswered) {
		_numAnswered = numAnswered;
	}
	
	/**
	 * 
	 * @return numQuestions The number of questions in the category
	 */
	public int numQuestions() {
		return _questions.size();
	}

	/**
	 * returns a list of all questions in the category
	 * @return questions
	 */
	public ArrayList<Question> getAllQuestions(){
		return _questions;
	}
	
	@Override
	public int compareTo(Category arg0) {
		if (this._name.compareTo(arg0.getName()) < 0) {
			return -1;
		}
		return 1;
	}
}
