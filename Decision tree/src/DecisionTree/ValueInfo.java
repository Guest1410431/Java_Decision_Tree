package DecisionTree;

import java.util.HashMap;
import java.util.BitSet;
import java.util.Set;

//Hold the information for each possible value an attribute has
public class ValueInfo
{
	// Name of the value
	String name;
	// Class name as key, number of rows this value is repeated
	HashMap<String, Integer> attributes;
	// Rows associated with current value
	BitSet rows;
	// total number of rows associated with this value
	int rowsCount;
	// Entropy of splitting on this trait
	double entropy;

	public ValueInfo(Set<String> classes, BitSet rows)
	{
		this.attributes = new HashMap<String, Integer>();

		for (String currentAttrib : classes)
		{
			attributes.put(currentAttrib, 0);
		}
		this.rows = rows;
		this.rowsCount = 0;
		this.entropy = 0;
	}

	public ValueInfo(HashMap<String, Integer> attributes, BitSet rows, int rowsCount)
	{
		super();

		this.attributes = attributes;
		this.rows = rows;
		this.rowsCount = rowsCount;
	}

	// Adds an attribute to the list
	public void addAttrib(String attribName)
	{
		attributes.put(attribName, 0);
	}

	// Increases the number of occurences of the attribute by one
	public void incrementAttrib(String className)
	{
		attributes.put(className, attributes.get(className) + 1);
	}

	// Increases the amount of rows by one
	public void increaseRowCount()
	{
		rowsCount++;
	}
	//GETTERS AND SETTERS
	public void setRowAt(int index)
	{
		rows.set(index);
	}

	public HashMap<String, Integer> getAttributeClasses()
	{
		return attributes;
	}
	
	public void setAttributeClasses(HashMap<String, Integer> attributes)
	{
		this.attributes = attributes;
	}

	public BitSet getRows()
	{
		return rows;
	}

	public void setRows(BitSet rows)
	{
		this.rows = rows;
	}

	public int getRowsCount()
	{
		return rowsCount;
	}

	public void setRowsCount(int rowsCount)
	{
		this.rowsCount = rowsCount;
	}

	public void setEntropy(double entropy)
	{
		this.entropy = entropy;
	}

	public double getEntropy()
	{
		return entropy;
	}

	// Returns a textual representation of the values
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("\n--- Classes ---");
		sb.append("\n");
		attributes.forEach((k, v) -> sb.append(k + " (count) = " + v + " |"));
		sb.append("\n");
		sb.append(" --- Rows ---");
		sb.append(rows);
		sb.append("\n");
		sb.append("--- Count ----");
		sb.append(rowsCount);
		return sb.toString();
	}
}
