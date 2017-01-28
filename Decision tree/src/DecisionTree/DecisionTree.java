package DecisionTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import Conditions.Condition;
import Conditions.EqualCondition;

public class DecisionTree
{
	private int rowsCount;
	private int columnsCount;
	//
	private BitSet columns;
	// List of all the classifications
	private HashSet<String> classes_train;
	// Root node to hold it all together
	private Node rootNode;
	private File trainFile;

	public DecisionTree()
	{
		this.columns = new BitSet(columnsCount);
		classes_train = new HashSet<String>();
		rootNode = new Node();
	}
	// Classification through the file
	public String classify(File file)
	{	// Simply so the first line (topics) aren't read into the program
		int counter = 0;
		
		String line = "";
		String ret = "";
		
		try
		{
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			
			while((line = br.readLine()) != null)
			{	//If it isn't the first line of the file
				if(counter != 0)
				{
					ret = classify(line) + "\n";
				}
				counter++;
			}
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	// Classification through the console
	public String classify(String row)
	{
		int numRight = 0;
		int numWrong = 0;
		
		String[] attrs = row.split("\\|");
		
		String actual = attrs[attrs.length-1];
		attrs[attrs.length-1] = null;
		
		String classification = classify(attrs, rootNode);
		System.out.println(attrs[0] + " | (" + actual + ") - (" + classification + ")");
		
		if(classification.equals(actual))
		{			
			numRight++;
		}
		else 
		{
			numWrong++;
		}
		return "Classification with " + (numRight / (numRight+numWrong))*100 + "% correct classifications";
	}

	// Gets the testing data, deals with it
	private String classify(String[] attrs, Node node)
	{
		// If the test is classified to a leaf, meaning the end of the tree
		// has been reached, return the leaf node's label (t/f)
		if (node.isLeaf())
		{
			return node.getLabel();
		}
		String currentValue = attrs[node.getIndex()];
		// If no leaf node has been met, then recursively search the tree
		for (Condition condition : node.getForks())
		{
			if (condition.test(currentValue))
			{
				return classify(attrs, condition.getNextNode());
			}
		}
		// If no leaf node is found with any attributes similar to the test,
		// then there are not enough values in the tree, the test fails
		return "Unable to classify: " + attrs[0] + ". Need more samples";
	}

	public void train(File trainFile)
	{
		try
		{
			this.trainFile = trainFile;
			findAttribs(trainFile);

			BitSet rows = new BitSet(rowsCount);

			for (int i = 0; i < rowsCount; i++)
			{
				rows.set(i);
			}
			buildTree(rootNode, rows);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void findAttribs(File trainFile) throws IOException
	{
		int counter = 0;

		String line;

		FileReader reader = null;
		try
		{
			reader = new FileReader(trainFile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(reader);
		// While the end of the file hasn't been reached
		while ((line = br.readLine()) != null)
		{ // The reason this is checked before the counter is incremented
			// is because of the file's first line containing labels
			if (counter != 0)
			{ // Once it gets to the data, split it by each "|"
				String[] cols = line.split("\\|");
				columnsCount = cols.length;
				// If there are more than two attributes to split on, go through the rest
				// add them to the list if the attribute isn't already in it
				if (cols.length > 2)
				{
					String targetValue = cols[cols.length - 1];
					// Check if the attribute is in the list
					if (!classes_train.contains(targetValue))
					{ // If not, add it into the list
						classes_train.add(cols[cols.length - 1]);
					}
				}
			}
			counter++;
		}
		// Number of samples in the train set
		rowsCount = counter;
		// Close the files
		br.close();
		reader.close();
	}

	public void buildTree(Node currentNode, BitSet rows)
	{
		AttributeInfo bestAttrib = findBestSplit(trainFile, rows);

		if (bestAttrib == null)
		{
			currentNode.setLeaf(true);
		}
		currentNode.setLabel(bestAttrib.getName());
		currentNode.setIndex(bestAttrib.getIndex());
		// Set the attribute as "Processed" to avoid using it again
		columns.set(bestAttrib.getIndex());

		HashMap<String, ValueInfo> infoValues = bestAttrib.getValues();
		// Easiest way to iterate through a hashmap
		Iterator<Entry<String, ValueInfo>> iterator = infoValues.entrySet().iterator();
		// Iterate through the map
		while (iterator.hasNext())
		{ // entry becomes the current node
			Entry<String, ValueInfo> entry = (Entry<String, ValueInfo>) iterator.next();
			// Grab info from the node
			ValueInfo currentInfo = entry.getValue();
			String valueName = entry.getKey();
			HashMap<String, Integer> classes = currentInfo.getAttributeClasses();
			// If it if perfectly pure (100/0 or 0/100 split)
			if (currentInfo.getEntropy() == 0.0)
			{
				String classLabel = findClassLabel(classes);

				Node leafNode = new Node();
				leafNode.setLabel(classLabel);
				leafNode.setLeaf(true);

				EqualCondition equal = new EqualCondition(leafNode, valueName);
				currentNode.addCondition(equal);
			} // Otherwise
			else
			{
				Node newNode = new Node();
				EqualCondition equal = new EqualCondition(newNode, valueName);
				currentNode.addCondition(equal);

				buildTree(newNode, currentInfo.getRows());
			}
		}
	}
	//Returns the classification label of a leaf node, (true/false), (yes/no), (gif/jif), etc.
	private String findClassLabel(HashMap<String, Integer> classes)
	{	//Impossible value for max, purely for comparison
		int max = -1;
		String classLabel = "";
		Iterator<Entry<String, Integer>> classIterator = classes.entrySet().iterator();
		//Iterate through the list
		while (classIterator.hasNext())
		{
			Entry<String, Integer> classEntry = (Entry<String, Integer>) classIterator.next();
			//If a better value for the set is found, replace the old
			if (classEntry.getValue() > max)
			{
				max = classEntry.getValue();
				classLabel = classEntry.getKey();
			}
		}
		return classLabel;
	}

	private AttributeInfo findBestSplit(File trainFile, BitSet rows)
	{
		AttributeInfo bestAttrib = null;
		// Set the baseline entropy to be the max number (better entropy = lower number)
		double bestEntropy = Double.MAX_VALUE;
		// Go through all the columns of data
		for (int i = 1; i < columnsCount - 1; i++)
		{
			// Bounds handling
			if (columns.get(i))
			{
				continue;
			}
			AttributeInfo data = singleAttirbuteInfo(trainFile, i, rows);
			HashMap<String, ValueInfo> attributes = data.getValues();
			// Calculate entropy as if a split was made on the given attribute
			double entropy = calculateSubTreeEntropy(attributes, data.getRowCount());
			// If the split is better than the previous best, update the info accordingly
			if (entropy < bestEntropy)
			{
				bestEntropy = entropy;
				bestAttrib = data;
				bestAttrib.setIndex(i);
			}
		}
		columns.set(bestAttrib.getIndex());
		return bestAttrib;
	}

	// Calculates the entropy of the split on the given attribute
	// ENTROPY = H(s) = -p(+)log(base 2)(p(+)) - p(-)log(base 2)(p(-))
	// Where s is the subset of training examples
	private double calculateSubTreeEntropy(HashMap<String, ValueInfo> attributes, Integer rowCount)
	{
		double totalEntropy = 0;
		// Iterate through the list of given attributes (easiest way I could think of)
		Iterator<Entry<String, ValueInfo>> iterator = attributes.entrySet().iterator();
		// Until the list is gone through
		while (iterator.hasNext())
		{
			// Set entry to the next element in the list
			Entry<String, ValueInfo> entry = (Entry<String, ValueInfo>) iterator.next();
			// Grab the value of the entry
			ValueInfo info = entry.getValue();
			// Calculate the entropy of the given value
			double entropy = calculateEntropy(new ArrayList<Integer>(info.getAttributeClasses().values()), info.getRowsCount());

			info.setEntropy(entropy);
			// Add the total entropy that one set of +/- attributes had to the entropic sum
			totalEntropy += (info.getRowsCount() / rowCount) * entropy;
		}
		return totalEntropy;
	}

	// Calculates entropy for one set of attributes
	private double calculateEntropy(ArrayList<Integer> list, int rowsCount)
	{
		double entropy = 0;

		for (int i = 0; i < list.size(); i++)
		{ // Chance that the element at i is + or -
			double probability = list.get(i) / rowsCount;

			entropy -= probability * logb(probability, 2);
		}
		return entropy;
	}

	// Gets the attribute info for a single attribute.
	private AttributeInfo singleAttirbuteInfo(File trainFile, int index, BitSet rows)
	{
		// Keeps track of the cycle the reader is on
		// Cycle 1 gets the attribute name, cycle 2 gets the info
		int counter = 0;
		// Holds the line of text that the file reader is on
		String line;
		//
		AttributeInfo attributeInfo = new AttributeInfo();
		attributeInfo.setIndex(index);
		// Keeps track of the attribute data
		HashMap<String, ValueInfo> attributes = new HashMap<String, ValueInfo>();

		try
		{
			FileReader reader = new FileReader(trainFile);
			BufferedReader br = new BufferedReader(reader);

			while ((line = br.readLine()) != null)
			{ // If it is the first line, read in the attribute as a title
				if (counter == 0)
				{ // Splits the line into an array
					String[] cols = line.split("\\|");
					// looks for the index requested
					for (int i = 0; i < cols.length; i++)
					{
						if (i == index)
						{ // Set it as the attribute name
							attributeInfo.setName(cols[i]);
						}
					}
				} // Any other cycle, do this
				else
				{ // If rows doesn't contain the counter number, then it
					// isn't in the set, so everything past this is skipped
					if (!rows.get(counter))
					{
						continue;
					}
					// Split the line into array
					String[] cols = line.split("\\|");
					// Get the classification, last column in the array
					String classify = cols[cols.length - 1];
					// Get the value of the indexed element
					String value = cols[index];
					// If the attribute list does not have the given attribute in it, add it into the list
					if (!attributes.containsKey(value))
					{
						attributes.put(value, new ValueInfo(classes_train, new BitSet(rowsCount)));
					}
					ValueInfo info = attributes.get(value);
					info.incrementAttrib(classify);
					info.setRowAt(counter);
					info.increaseRowCount();
				}
				counter++;
			}
			// Close the files
			br.close();
			reader.close();
			// Update all that work to the to-be returned attributeInfo object
			attributeInfo.setRowCount(counter);
			attributeInfo.setValues(attributes);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return attributeInfo;
	}
	// Returns the logarithm of a, with base b
	public static double logb(double a, double b)
	{
		if (a == 0)
			return 0;
		return Math.log(a) / Math.log(b);
	}

	// Returns the root node
	public Node getRootNode()
	{
		return rootNode;
	}
}
