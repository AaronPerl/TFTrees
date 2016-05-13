package perl.aaron.TruthTrees;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import perl.aaron.TruthTrees.graphics.TreePanel;
import perl.aaron.TruthTrees.logic.Statement;

public class FileManager {
	private static final String EXTENSION = "tft";
	public static TreePanel loadFile(TreePanel parent)
	{
		final JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			System.out.println(file.getName());
			return loadFromFile(file);
		}
		return null;
	}
	
	public static void saveFile(TreePanel parent)
	{
		final JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter tftFilter = new FileNameExtensionFilter(
		  EXTENSION + " files(*." + EXTENSION + ")",
		  EXTENSION);
		
		fileChooser.addChoosableFileFilter(tftFilter);
		fileChooser.setFileFilter(tftFilter);
		
		if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			if (fileChooser.getFileFilter() == tftFilter && !file.getName().endsWith("." + EXTENSION))
				file = new File(file.getAbsolutePath() + "." + EXTENSION);
			System.out.println(file.getName());
			saveToFile(parent.getRootBranch(), file, parent);
		}
	}
	
	private static void processBranchLine(Branch curBranch, Node node, TreePanel panel,
			ArrayList<Set<Integer>> lineDecompositions, ArrayList<Set<Integer>> branchDecompositions,
			ArrayList<BranchLine> lines, ArrayList<Branch> branches)
	{
		LinkedHashSet<Integer> curLineDecompositions = new LinkedHashSet<Integer>();
		LinkedHashSet<Integer> curBranchDecompositions = new LinkedHashSet<Integer>();
		Element curElement = (Element) node;
		BranchLine newLine;
		if (node.getNodeName().equals("Terminator"))
		{
			String isClose = curElement.getAttribute("close");
			if (isClose.equals("") || isClose.equals("true"))
			{
				newLine = panel.addTerminator(curBranch);
			}
			else
			{
				System.out.println("Open terminator");
				newLine = panel.addOpenTerminator(curBranch);
			}
		}
		else
		{
			String content = curElement.getAttribute("content");
			Statement newStatement = ExpressionParser.parseExpression(content);
			newLine = panel.addStatement(curBranch, newStatement);
		}
		lines.add(newLine);
		NodeList decompositions = node.getChildNodes();
		for (int j = 0; j < decompositions.getLength(); j++)
		{
			Node curDecomp = decompositions.item(j);
			if (curDecomp.getNodeName().equals("Decomposition"))
			{
				Element decompElement = (Element) curDecomp;
				String branchIndexString = decompElement.getAttribute("branchIndex");
				if (!branchIndexString.equals(""))
				{
					System.out.println("Branch index:" + branchIndexString);
					int branchIndex = Integer.parseInt(branchIndexString);
					curBranchDecompositions.add(branchIndex);
				}
				String lineIndexString = decompElement.getAttribute("lineIndex");
				if (!lineIndexString.equals(""))
				{
					System.out.println("Line index:" + lineIndexString);
					int lineIndex = Integer.parseInt(lineIndexString);
					curLineDecompositions.add(lineIndex);
				}
			}
		}
		lineDecompositions.add(curLineDecompositions);
		branchDecompositions.add(curBranchDecompositions);
	}
	
	private static void processNode(Branch curBranch, Node curNode, TreePanel panel,
			ArrayList<Set<Integer>> lineDecompositions, ArrayList<Set<Integer>> branchDecompositions,
			ArrayList<BranchLine> lines, ArrayList<Branch> branches)
	{
		NodeList children = curNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node curChild = children.item(i);
			if (curChild.getNodeName().equals("Branch"))
			{
				Branch newBranch = panel.addBranch(curBranch, false);
				branches.add(newBranch);
				processNode(newBranch, curChild, panel, lineDecompositions, branchDecompositions, lines, branches);
			}
			else if (curChild.getNodeName().equals("BranchLine") || curChild.getNodeName().equals("Terminator"))
			{
				processBranchLine(curBranch, curChild, panel, lineDecompositions, branchDecompositions, lines, branches);
			}
		}
	}
	
	private static TreePanel loadFromFile(File file)
	{
		try {
			TreePanel newPanel = new TreePanel(false);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			doc.getDocumentElement().normalize();
			Branch rootBranch = newPanel.getRootBranch();
			Branch premiseBranch = rootBranch.getRoot();
			Node rootElement = doc.getDocumentElement();
			NodeList rootList = rootElement.getChildNodes();
			ArrayList<Set<Integer>> lineDecompositions = new ArrayList<Set<Integer>>();
			ArrayList<Set<Integer>> branchDecompositions = new ArrayList<Set<Integer>>();
			ArrayList<BranchLine> lines = new ArrayList<BranchLine>();
			ArrayList<Branch> branches = new ArrayList<Branch>();
			branches.add(rootBranch);
			boolean foundRoot = false;
			for (int i = 0; i < rootList.getLength(); i++)
			{
				Node curNode = rootList.item(i);
				if (curNode.getNodeName().equals("BranchLine"))
				{
					processBranchLine(premiseBranch, curNode, newPanel, lineDecompositions, branchDecompositions, lines, branches);
				}
				else if(curNode.getNodeName().equals("Branch"))
				{
					if (foundRoot)
						return null; // two root nodes
					foundRoot = true;
					processNode(rootBranch,curNode,newPanel,
							lineDecompositions, branchDecompositions,
							lines, branches);
				}
			}
			for (int i = 0; i < lines.size(); i++)
			{
				Set<Integer> curLineIndices = lineDecompositions.get(i);
				Set<Integer> curBranchIndices = branchDecompositions.get(i);
				BranchLine curLine = lines.get(i);
				Set<BranchLine> selectedLines = curLine.getSelectedLines();
				Set<Branch> selectedBranches = curLine.getSelectedBranches();
				for (int lineIndex : curLineIndices)
				{
					BranchLine curDecomp = lines.get(lineIndex);
					selectedLines.add(curDecomp);
					if (!(curLine instanceof BranchTerminator))
						curDecomp.setDecomposedFrom(curLine);
				}
				for (int branchIndex : curBranchIndices)
				{
					Branch curDecomp = branches.get(branchIndex);
					selectedBranches.add(curDecomp);
				}
			}
			newPanel.deleteFirstPremise();
			newPanel.moveComponents();
			return newPanel;
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			return null; // error reading xml file
		}
	}
	
	private static void saveBranch(Branch curBranch, Document doc, Element parent,
			LinkedHashMap<Branch, Integer> branchIndexMap, LinkedHashMap<BranchLine, Integer> lineIndexMap)
	{
		for (int i = 0; i < curBranch.numLines(); i++)
		{
			BranchLine curLine = curBranch.getLine(i);
			Element curLineElement;
			if (curLine instanceof BranchTerminator)
			{
				curLineElement = doc.createElement("Terminator");
				curLineElement.setAttribute("close",
						Boolean.toString(((BranchTerminator)curLine).isClose()));
			}
			else
			{
				curLineElement = doc.createElement("BranchLine");
				curLineElement.setAttribute("content", curLine.toString());
			}
			curLineElement.setAttribute("index", Integer.toString(lineIndexMap.get(curLine)));
			for (Branch curDecomp : curLine.getSelectedBranches())
			{
				Element curDecompElement = doc.createElement("Decomposition");
				curDecompElement.setAttribute("branchIndex", Integer.toString(branchIndexMap.get(curDecomp) - 1)); // offset by 1 to skip premise branch
				curLineElement.appendChild(curDecompElement);
			}
			for (BranchLine curDecomp : curLine.getSelectedLines())
			{
				Element curDecompElement = doc.createElement("Decomposition");
				curDecompElement.setAttribute("lineIndex", Integer.toString(lineIndexMap.get(curDecomp)));
				curLineElement.appendChild(curDecompElement);
			}
			parent.appendChild(curLineElement);
		}
		for (Branch curChild : curBranch.getBranches())
		{
			Element curBranchElement = doc.createElement("Branch");
			curBranchElement.setAttribute("index", Integer.toString(branchIndexMap.get(curBranch)));
			parent.appendChild(curBranchElement);
			saveBranch(curChild, doc, curBranchElement, branchIndexMap, lineIndexMap);
		}
	}
	
	private static void createIndexMaps(Branch root, Map<Branch,Integer> branchIndexMap, Map<BranchLine,Integer> lineIndexMap)
	{
		branchIndexMap.put(root, branchIndexMap.size());
		for (int i = 0; i < root.numLines(); i++)
		{
			BranchLine curLine = root.getLine(i);
			lineIndexMap.put(curLine, lineIndexMap.size());
		}
		for (Branch curBranch : root.getBranches())
		{
			createIndexMaps(curBranch, branchIndexMap, lineIndexMap);
		}
	}
	
	private static void saveToFile(Branch root, File file, TreePanel parent)
	{
		try {
			LinkedHashMap<Branch, Integer> branchIndexMap = new LinkedHashMap<Branch, Integer>();
			LinkedHashMap<BranchLine, Integer> lineIndexMap = new LinkedHashMap<BranchLine, Integer>();
			createIndexMaps(root.getRoot(), branchIndexMap, lineIndexMap);
//			for (BranchLine b : lineIndexMap.keySet())
//			{
//				System.out.println(b.toString() + " - index " + lineIndexMap.get(b).toString());
//			}
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			Element rootElement = doc.createElement("Tree");
			doc.appendChild(rootElement);
//			for (BranchLine b : premises)
			saveBranch(root.getRoot(), doc, rootElement, branchIndexMap, lineIndexMap);
			
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(source, result);
			
			
		} catch (ParserConfigurationException | TransformerException e) {
			JOptionPane.showMessageDialog(parent, "Error: Could not save file!");
		}
		
	}

}
