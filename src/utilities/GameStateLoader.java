package utilities;

import java.io.File;
import java.util.ArrayList;

import model.Category;
import model.Question;

public class GameStateLoader {
	ArrayList<Category> _catList;
	
	public GameStateLoader(ArrayList<Category> catList) {
		_catList = catList;
	}
	
	/**
	 * saves the game state to the tmp folder
	 * @param catList categories to save
	 */
	public static void saveGameState(ArrayList<Category> catList) {
		if (!catList.isEmpty()) {
			File file = new File("tmp/categories/");
			file.mkdirs();
			for (Category cat: catList) {
				BashCommand createCat = new BashCommand("echo \""+cat.getName()+"\n"+cat.getNumAnswered()+"\" > \"tmp/categories/"+cat.getName()+"\"");
				createCat.endProcess();
				for (Question question: cat.getAllQuestions()) {
					BashCommand saveQuestion = new BashCommand("echo \""+question+"\" >> \"tmp/categories/"+cat.getName()+"\"");
					saveQuestion.endProcess();
				}
			}
		}
	}
	
	/**
	 * Load questions for game module from save state
	 * @param catList list to load questions into
	 * @return boolean indicating whether load was successful
	 */
	public static boolean LoadGameState(ArrayList<Category> catList) {
		File catFile = new File("tmp/categories/");
		if (catFile.exists()) {
			String questionString;
			BashCommand lsCommand = new BashCommand("ls tmp/categories/");
			String catOnFile = lsCommand.getStdOut();
			//create all category objects and populate with questions
			while (catOnFile != null) {;
			BashCommand catCategories = new BashCommand("cat \"tmp/categories/"+catOnFile+"\"");
			Category category = new Category(catOnFile);
			catList.add(category);
			catCategories.getStdOut();
			category.setNumAnswered(Integer.parseInt(catCategories.getStdOut()));
			questionString = catCategories.getStdOut();
			//load in all questions.
			while (questionString != null) {
				category.AddQuestion(new Question(questionString));
				questionString = catCategories.getStdOut();
			}
			catCategories.endProcess();
			catOnFile = lsCommand.getStdOut();
			}
			lsCommand.endProcess();
			return true;
		} 
		return false;
	}
	
	/**
	 * load the question bank from the questions folder
	 */
	public static ArrayList<Category> loadQuestionBank() {
		ArrayList<Category> questionBank = new ArrayList<Category>();
		String questionString;
		BashCommand lsCommand = new BashCommand("ls Questions/");
		String catFile = lsCommand.getStdOut();
		//create all category objects and populate with questions
		while (catFile != null) {;
		BashCommand catCategories = new BashCommand("cat \"Questions/"+catFile+"\"");
		Category category = new Category(catFile);
		catFile = lsCommand.getStdOut();
		catCategories.getStdOut();
		questionBank.add(category);
		questionString = catCategories.getStdOut();

		while (questionString != null) {
			if (!questionString.equals("")) {
				category.AddQuestion(new Question(questionString));
			}
			questionString = catCategories.getStdOut();
		}
		catCategories.endProcess();
		}
		lsCommand.endProcess();
		return questionBank;
	}

	/**
	 * Loads the question bank for International section
	 */
	public static Category loadInternationalQuestions() {
		BashCommand command = new BashCommand("cat InternationalQuestions/International");
		Category internationalQuestions = new Category("International");
		command.getStdOut();
		String question = command.getStdOut();
		while(question != null && !question.equals("")) {
			internationalQuestions.AddQuestion(new Question(question));	
			question = command.getStdOut();
		}
		command.endProcess();
		return internationalQuestions;
	}
	
}
