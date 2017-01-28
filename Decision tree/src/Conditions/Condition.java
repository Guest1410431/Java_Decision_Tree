package Conditions;

import DecisionTree.Node;

public abstract class Condition
{
	Node node;
	
	public Condition(Node nextNode)
	{
		this.node = nextNode;
	}
	public Node getNextNode()
	{
		return node;
	}
	public void setNext(Node node)
	{
		this.node = node;
	}
	public abstract boolean test(String value);
}
