package DecisionTree;

import java.util.HashMap;

// Holds data for each attribute
public class AttributeInfo
{
	// Name of the attribute, received from the first line of the training file
	String name;
	// The name of the attribute and the info for that attribute
	HashMap<String, ValueInfo> values;
	// Number of rows without null values for this attribute
	int rowCount;
	// Index number for the attribute (where it is in the list)
	int index;
	// GETTERS AND SETTERS
	public HashMap<String, ValueInfo> getValues()
	{
		return values;
	}

	public void setValues(HashMap<String, ValueInfo> values)
	{
		this.values = values;
	}

	public Integer getRowCount()
	{
		return rowCount;
	}

	public void setRowCount(Integer rowCount)
	{
		this.rowCount = rowCount;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public int getIndex()
	{
		return index;
	}
}
