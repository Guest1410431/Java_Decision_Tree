# Java_Decision_Tree
A machine learning algorithm defined as a Decision Tree based in Java


I started this a few months ago and just finished the final methods.

Main.java 
* This is where the program starts.
* Creates a new Decision Tree and trains it with the file "Train.psv".
* Tests it by classifying with the testing data set "Test.psv".

DecisionTree.java
* Starts off with a base rootNode.
* Looks at the attributes given to train by, and develops into a tree based on the best attribute to split on.
* Best attribute is decided on by the Entropy Gained if the tree were to split on the attribute.
* I learned the entropy algorithm from https://www.youtube.com/watch?v=pLzE2Oh9QDI.

Node.java
* Holds the information of the attribute that the previous split had.
* The list forks hold the list of splits below the given node, easier to follow for testing.
* Leaf nodes are found at the bottom of the tree and are the classifiers.
* Nodes have no concept of confidence, but I plan on implementing that if I use this for a random forest.
* rootNode holds no information other than the forks below it.

AttributeInfo.java
* Holds the information of each attribute

ValueInfo.java
* Holds all of the values given in the training set that each attribute can have.

Condition.java
* Abstract class for EqualCondition.java and ElseCondition.java
* Handles the state of the attribute that the tree is developed with.

*EqualCondition.java*
* Tests equality between two given attribute states

*ElseCondition.java*
* If the conditions are not equal, the attribute state of the one being tested goes through this class
