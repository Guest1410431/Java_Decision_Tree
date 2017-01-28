package DecisionTree;

import java.util.LinkedList;

import Conditions.Condition;

//Maps every step in the decision tree
public class Node
{
	// List of all nodes below the fork until the leaf node is reached
	private LinkedList<Condition> forks;
	// Name of the node. Leaf - class value, not Leaf - Attribute's name
	private String label;
	// Attribute index of the node
	private int index;
	// True if leaf node, else false
	private boolean leaf;

	public Node()
	{ 
		forks = new LinkedList<Condition>();
		leaf = false;
	}
	public void addCondition(Condition condition)
	{
		forks.add(condition);
	}
	public void setLabel(String label)
	{
		this.label = label;
	}
	public String getLabel()
	{
		return label;
	}
	public void setLeaf(boolean leaf)
	{
		this.leaf = leaf;
	}
	public LinkedList<Condition> getForks()
	{
		return forks;
	}
	public void setForks(LinkedList<Condition> forks)
	{
		this.forks = forks;
	}
	public int getIndex()
	{
		return index;
	}
	public void setIndex(int index)
	{
		this.index = index;
	}
	public boolean isLeaf()
	{
		return leaf;
	}
	public String toString()
	{
		String out = "\n <Node Label = '" + label + "'  isLeaf = '" + isLeaf() + "' index=" + index + " >";
		out += "\n <Conditions>";

		for (Condition condition : forks)
		{
			out += "\n " + condition.toString();
		}
		out += "\n </Conditions>";
		out += "\n </Node> ";
		return out;
	}
}
