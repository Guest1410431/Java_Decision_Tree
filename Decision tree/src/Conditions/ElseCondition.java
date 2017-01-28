package Conditions;

import DecisionTree.Node;

public class ElseCondition extends Condition
{
	public ElseCondition(Node nextNode)
	{
		super(nextNode);
	}	
	public boolean test(String value)
	{
		return true;
	}
}
