/* * * * * * * * * * * * * * * * * * * * * * * *  
 * Name          : Michael Lieb
 * Date Started  : November 12, 2016
 * Date Finished : January 26, 2017
 * Description   : A binary classification machine learning algorithm known as a decision tree.
 */

package Main;

import java.io.File;

import DecisionTree.DecisionTree;

public class Main
{
	public static void main(String[]args)
	{
		//Create a new decision tree
		DecisionTree tree = new DecisionTree();
		//Train the tree with the training data
		tree.train(new File("src/train.psv"));
		
		tree.classify(new File("src/test.psv"));
		
		System.out.println("Done");
	}
}
