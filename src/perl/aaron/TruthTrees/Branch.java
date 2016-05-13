package perl.aaron.TruthTrees;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import perl.aaron.TruthTrees.logic.Statement;

/**
 * A class that represents a Branch in a Truth Tree, containing a list of Decompositions
 * and a set of Branches stemming from this Branch
 * @author Aaron Perl
 *
 */
public class Branch {
	public static final int BRANCH_SEPARATION = 10;
	public static final int VERTICAL_GAP = 20;
	public static final int LABEL_BORDER = 5;
	public static final int MIN_WIDTH = 80;
	private List<BranchLine> lines;
	private Set<Branch> branches;
	private Branch root;
	
	private FontMetrics fm;	
	private BranchLine widestLine;
	private int width;
	private BranchLine decomposedFrom;
	
	/**
	 * Constructs a branch stemming from the given root Branch
	 * @param root The root Branch to stem from (may be null)
	 */
	public Branch(Branch root)
	{
		lines = new ArrayList<BranchLine>();
		branches = new LinkedHashSet<Branch>();
		this.root = root;
		fm = null;
		widestLine = null;
		if (root != null)
		{
			root.addBranch(this);
		}
	}
	
	/**
	 * Returns the BranchLine at the given index
	 * @param index The index of the line to get
	 * @return The BranchLine at the given index
	 */
	public BranchLine getLine(int index)
	{
		return lines.get(index);
	}
	
	/**
	 * Returns the Statement at the given index
	 * @param index The index of the statement to get
	 * @return The Statement at the given index
	 */
	public Statement getStatement(int index)
	{
		return lines.get(index).getStatement();
	}
	
	/**
	 * Gets the number of lines in this branch (not including it's childrens' lines)
	 * @return The number of lines in this branch
	 */
	public int numLines()
	{
		return lines.size();
	}
	
	/**
	 * Gets the Set Branch stemming from this Branch (unmodifiable)
	 * @return The Set of Branches stemming from this Branch
	 */
	public Set<Branch> getBranches()
	{
		return java.util.Collections.unmodifiableSet(branches);
	}
	
	/**
	 * Removes a Branch by index
	 * @param index The index of the Branch to remove
	 */
	public void removeBranch(int index)
	{
		branches.remove(index);
	}
	
	/**
	 * Removes a Branch passed by reference
	 * @param b The Branch to remove
	 * @return True if b was contained (and removed), false otherwise
	 */
	public boolean removeBranch(Branch b)
	{
		if (branches.remove(b))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a BranchLine by index
	 * @param index The index of the BranchLine to remove
	 */
	public void removeLine(int index)
	{
		boolean widest = false;
		if (lines.get(index) == widestLine)
			widest = true;
		lines.remove(index);
		if (widest)
		{
			calculateWidestLine();
		}
	}
	
	/**
	 * Removes a Statement passed by reference
	 * @param s The Statement to remove
	 * @return True if s was contained (and removed), false otherwise
	 */
	public boolean removeStatement(Statement s)
	{
		for (int i = 0; i < lines.size(); i++)
		{
			if (lines.get(i).getStatement() == s)
			{
				removeLine(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds a Statement to the list of statements at the given index
	 * @param s The Statement to add
	 * @param index The index to add the Statement at
	 */
	public BranchLine addStatement(Statement s, int index)
	{
		BranchLine newLine = new BranchLine(this);
		newLine.setStatement(s);
		lines.add(index, newLine);
		if (newLine.getWidth(fm) > widestLine.getWidth(fm))
		{
			widestLine = newLine;
		}
		return newLine;
	}
	
	/**
	 * Adds a Statement to the end of the list of statements
	 * @param s The Statement to add
	 */
	public BranchLine addStatement(Statement s)
	{
		BranchLine newLine = new BranchLine(this);
		newLine.setStatement(s);
		lines.add(newLine);
		if (fm != null && (widestLine == null || newLine.getWidth(fm) > widestLine.getWidth(fm)))
		{
			widestLine = newLine;
		}
		return newLine;
	}
	
	/**
	 * Adds a BranchTerminator to this branch if it does not already have one
	 * @param terminator The BranchTerminator to add
	 */
	public void addTerminator(BranchTerminator terminator)
	{
		if (!isClosed())
			lines.add(terminator);
	}
	
	/**
	 * Adds a Branch to the set of Branches stemming from this Branch
	 * (Automatically called by constructor)
	 * @param b
	 */
	public void addBranch(Branch b)
	{
		branches.add(b);
	}
	
	/**
	 * Returns the root Branch that this Branch stems from
	 * @return The root Branch that this Branch stems from
	 */
	public Branch getRoot()
	{
		return root;
	}
	
	/**
	 * Gets the width of the widest child
	 * @return The width of the widest child
	 */
	public int getWidestChild()
	{
		int maxWidth = 0;
		for (Branch curBranch : branches)
		{
			int curWidth = curBranch.getWidth();
			if (curWidth > maxWidth)
				maxWidth = curWidth;
		}
		return maxWidth;
	}

	/**
	 * Returns the previously calculated width of this branch
	 * @return The previously calculated width of this branch
	 */
	public int getWidth()
	{
		if (widestLine == null)
		{
			calculateWidestLine();
		}
		if (branches.size() == 0)
		{
			if (widestLine == null)
				calculateWidestLine();
			if (widestLine != null)
				return Math.max(widestLine.getWidth(fm) + 2 * Branch.LABEL_BORDER, MIN_WIDTH);
			return MIN_WIDTH;
		}
		else
		{
			int maxWidth = getWidestChild();
			return (maxWidth + Branch.BRANCH_SEPARATION) * branches.size() - Branch.BRANCH_SEPARATION;
		}
	}
	
	/**
	 * Returns the height of this branch, not including descendants
	 * @return The height of this branch, not including descendants
	 */
	public int getLocalHeight()
	{
		return fm.getHeight() * lines.size();
	}
	
	/**
	 * Returns the height of a line of text with this Branch's FontMetric
	 * @return The height of a line of text with this Branch's FontMetric
	 */
	public int getLineHeight()
	{
		return fm.getHeight() + Branch.LABEL_BORDER * 2;
	}
	
	/**
	 * Returns the calculated width of the given line
	 * @param index The index of the line to calculate the width of
	 * @return The calculated width
	 */
	public int getLineWidth(int index)
	{
		return fm.stringWidth(lines.get(index).toString()) + 2 * Branch.LABEL_BORDER;
	}
	
	/**
	 * Recursively sets the FontMetrics for this branch and all its descendents
	 * @param fm The FontMetrics to use
	 */
	public void setFontMetrics(FontMetrics fm)
	{
		this.fm = fm;
		for (Branch curBranch : branches)
		{
			curBranch.setFontMetrics(fm);
		}
	}
	
	/**
	 * Returns the width of the widest line in this branch
	 * @return The width of the widest line in this branch
	 */
	public int getWidestLine()
	{
		if (widestLine == null)
			if (lines.size() > 0)
				calculateWidestLine();
			else
				return MIN_WIDTH;
		if (widestLine != null)
			return Math.max(widestLine.getWidth(fm) + 2 * Branch.LABEL_BORDER, MIN_WIDTH);
		else
			return 0;
	}
	
	/**
	 * Calculates and sets the widest line in this branch
	 */
	public void calculateWidestLine()
	{
		if (fm == null)
			throw new IllegalStateException("resetWidestLine() called without a FontMetrics");
		int maxWidth = 0;
		int maxIndex = -1;
		for (int i = 0; i < lines.size(); i++)
		{
			int curWidth = lines.get(i).getWidth(fm);
			if (curWidth > maxWidth)
			{
				maxWidth = curWidth;
				maxIndex = i;
			}
		}
		if (maxIndex != -1)
			widestLine = lines.get(maxIndex);
		else
			widestLine = null;
	}
	
	/**
	 * Returns whether or not this branch has been closed
	 * @return True if the branch has a BranchTerminator set to close
	 */
	public boolean isClosed()
	{
		for (int i = lines.size() - 1; i >= 0; i--) // start from the end, since it should be the last line
		{
			if (lines.get(i) instanceof BranchTerminator &&
					((BranchTerminator)lines.get(i)).isClose())
				return true;
		}
		return false;
	}
	
	/**
	 * Returns whether or not this branch is open
	 * @return True if the branch has a BranchTerminator set to open
	 */
	public boolean isOpen()
	{
		for (int i = lines.size() - 1; i >= 0; i--) // start from the end, since it should be the last line
		{
			if (lines.get(i) instanceof BranchTerminator &&
					!((BranchTerminator)lines.get(i)).isClose())
				return true;
		}
		return false;
	}
	
	/**
	 * Returns a recursive deep copy of this branch
	 * @return The copy of this branch
	 */
	public Branch deepCopy()
	{
		return deepCopy(null);
	}
	
	private Branch deepCopy(Branch parent)
	{
		Branch copy = new Branch(parent);
		copy.setFontMetrics(fm);
		for (BranchLine l : lines)
		{
			copy.addStatement(l.getStatement());
		}
		for (Branch b : branches)
		{
			copy.addBranch(b.deepCopy(copy));
		}
		return copy;
	}
	
	/**
	 * @param parent The parent to check if this branch is a descendant of
	 * @return True iff this branch is a descendant of the given branch
	 */
	public boolean isChildOf(Branch parent)
	{
		Branch curAncestor = this;
		while (curAncestor != null)
		{
			if (parent == curAncestor) return true;
			curAncestor = curAncestor.getRoot();
		}
		return false;
	}

	/**
	 * Returns the set of all constants in this branch, ancestor branches and descendent branches
	 * @return The set of all constants in this branch, ancestor branches and descendent branches
	 */
	public Set<String> getConstants() {
		Set<String> constants = new LinkedHashSet<String>();
		constants.addAll(getConstantsThis());
		constants.addAll(getConstantsChildren());
		constants.addAll(getConstantsBefore(null));
		return constants;
	}
	
	/** 
	 * Returns the set of all constants in this branch, not including parents or children
	 * @return The set of all constants in this branch
	 */
	private Set<String> getConstantsThis() {
		Set<String> constants = new LinkedHashSet<String>();
		for (BranchLine line : lines)
		{
			if (line.getStatement() != null)
				constants.addAll(line.getStatement().getConstants());
		}
		return constants;
	}
	
	/**
	 * Returns the set of all constants in children branches of this branch (including all descendents)
	 * @return The set of all constants in children branches of this branch
	 */
	private Set<String> getConstantsChildren() {
		Set<String> constants = new LinkedHashSet<String>();
		for (Branch b : branches)
		{
			constants.addAll(b.getConstantsThis());
			constants.addAll(b.getConstantsChildren());
		}
		return constants;
	}

	/**
	 * Returns the set of all constants in this branch (and parent branches) before the given line
	 * @param last The line to stop collecting constants at
	 * @return The set of all constants in this branch (and parent branches) before the given line
	 */
	public Set<String> getConstantsBefore(BranchLine last) {
		Set<String> constants = new LinkedHashSet<String>();
		for (BranchLine line : lines)
		{
			if (line == last) break;
			if (line.getStatement() != null)
				constants.addAll(line.getStatement().getConstants());
		}
		if (root != null)
		{
			constants.addAll(root.getConstantsBefore(null));
		}
		return constants;
	}
	
	public void setDecomposedFrom(BranchLine line)
	{
		decomposedFrom = line;
	}
	
	public BranchLine getDecomposedFrom()
	{
		return decomposedFrom;
	}
}
