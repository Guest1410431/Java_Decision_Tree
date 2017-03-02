package Conditions;

import DecisionTree.Node;

public class EqualCondition extends Condition
{
	String conditionValue;

	public EqualCondition(Node nextNode, String conditionValue)
	{
		super(nextNode);
		this.conditionValue = conditionValue;
	}

	public boolean test(String value)
	{
		return conditionValue.equals(value);
	}
}
