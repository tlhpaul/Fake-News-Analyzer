import java.io.FileNotFoundException;
import java.util.*;


/**
 * The user interface for asking questions
 *
 */
public class UserInteraction {
	private Scanner scan = new Scanner(System.in);
	private TwitterReader twitterReader;

	/**
	 * Asks users to select a question and a sub-group to analyse if relevant
	 *
	 */
	public void askUser() {
		while (true) {
			try {
				twitterReader = new TwitterReader();
				LinkedHashMap<String, ArrayList<String[]>> tweetMap = twitterReader.parse();

				String question = askQuestion();
				String grouping;
				List<String> unGrouped = Arrays.asList("6", "7", "8", "10");
				if (!unGrouped.contains(question)) {
					grouping = findGrouping();
				}else{
					grouping = "all";
				}
				answerQuestion(tweetMap, question, grouping);
				break;
			} catch (FileNotFoundException e) {
				System.out.println("The file doesn't exist.");
			} catch (NumberFormatException e) {
				System.out.println("The input is invalid, please try again.");
			}
		}
	}

	/**
	 * Finds the grouping to analyse based on the user's input
	 *
	 * @return the group to analyse or 'all' if the question does not allow for sub-groups
	 */
	private String findGrouping() {
		System.out.println("Great, now please choose a target group:\nNews: left, center, " +
				"right or all (all-wings)");

		String grouping = scan.nextLine().toLowerCase();
		String[] options = new String[]{"left", "right", "center", "all"};
		if (! Arrays.asList(options).contains(grouping)) {
			throw new NumberFormatException();
		} 
		
		if(grouping.contains("left")) {
			grouping = "left";
		}
		if(grouping.contains("center")) {
			grouping = "center";
		}
		if(grouping.contains("right")) {
			grouping = "right";
		}

		return grouping;
	}

	/**
	 * Asks user to select a question and returns the question number string
	 *
	 * @return the string representing the question number being asked
	 */
	private String askQuestion(){
		System.out.println("Welcome to the HsuWeinberg analysis. We want to help you decide what news is fake and" +
				" what is real with a number of analyses. Our analyses take data from 11 news media twitter " +
				"accounts that range from the far left to the far right. To make things more interesting, we added"  +
				" Trump's twitter. Let's find out where the biases really stand!\n\nNow type the number corresponding" +
				" to the question you want answered:\n1) How kind has news been to Trump?\n2) How frequently does a " +
				"specified media relative to the right mention Trump? \n3) What is the decomposition of news" +
				" mentioning the Russia Scandal?\n4) What was the rate that news talked about wiretapping?\n"+
				"5) What is the moving average for media discussion of taxes? \n6) How much has each media source " +
				"talked about Democrats over time? \n7) How much are all news organizations discussing North Korea?" +
				" \n8) What is the effect of media bias and date on frequency of news mentioning Healthcare? \n9) " +
				"How often have different news media discussed ISIS?\n10) What is the ratio of all media discussing " +
				"Russia vs. Wiretapping?");
		String[] options = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		String question = scan.nextLine();
		if (! Arrays.asList(options).contains(question)) {
			throw new NumberFormatException();
		} 
		return question;
	}

	/**
	 * Answers the user's question based on the question being asked and the group if it is relevant
	 *
	 * @param tweetMap being assessed for an answer
	 * @param question being asked by the user
	 * @param grouping the grouping or 'all' that specifies the question
	 */
	private void answerQuestion(LinkedHashMap<String, ArrayList<String[]>> tweetMap,
								String question, String grouping){

		QuestionFormatter questionFormatter = new QuestionFormatter(tweetMap);
		QuestionAsker questionAsker = new QuestionAsker(questionFormatter, grouping);

		switch(question){
			case "1":
				questionAsker.questionOne();
				break;
			case "2":
				questionAsker.questionTwo();
				break;
			case "3":
				questionAsker.questionThree();
				break;
			case "4":
				questionAsker.questionFour();
				break;
			case "5":
				questionAsker.questionFive();
				break;
			case "6":
				questionAsker.questionSix();
				break;
			case "7":
				questionAsker.questionSeven();
				break;
			case "8":
				questionAsker.questionEight();
				break;
			case "9":
				questionAsker.questionNine();
				break;
			case "10":
				questionAsker.questionTen();
				break;
		}
	}
}
