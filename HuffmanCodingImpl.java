import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.HashMap;

public class HuffmanCodingImpl implements HuffmanCoding {

	private HashMap<Character, Integer> getFrequencyTable(File inputFile) {
		BufferedReader bufferedReader = null;
		HashMap<Character, Integer> counter = new HashMap<>();
		try {
			bufferedReader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return counter;
		}
		String str = null;
		try {
			while((str = bufferedReader.readLine()) != null) {
				for (int i = 0;i < str.length(); ++i) {
					char ch = str.charAt(i);
					if (!counter.containsKey(ch)) {
						counter.put(ch, 1);
					} else {
						counter.put(ch, 1 + counter.get(ch));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return counter; 
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return counter;
	}

	@Override
	public String getFrequencies(File inputFile) {
		HashMap<Character, Integer> frequencyTable = getFrequencyTable(inputFile);
		String out = "";
		for (Character ch: frequencyTable.keySet()) {
			out += ch + " " + frequencyTable.get(ch) + "\n";
		}
		return out;
	}

	@Override
	public HuffTree buildTree(File inputFile) {
		HashMap<Character, Integer> frequencyTable = getFrequencyTable(inputFile);
		PriorityQueue<HuffTree> Hheap = new PriorityQueue<>();
		for (Character ch: frequencyTable.keySet()) {
			HuffTree node = new HuffTree(ch, frequencyTable.get(ch));
			Hheap.add(node);
		}
		HuffTree tmp1, tmp2, tmp3 = null;
		while (Hheap.size() > 1) { // While two items left
			tmp1 = Hheap.remove();
			tmp2 = Hheap.remove();
			tmp3 = new HuffTree(tmp1.root(), tmp2.root(),
					tmp1.weight() + tmp2.weight());
			Hheap.add(tmp3);   // Return new tree to heap
		}
		return tmp3;            // Return the tree
	}

	@Override
	public String encodeFile(File inputFile, HuffTree huffTree) {
		HashMap<Character, String> encodes = new HashMap<>();
		collectEncode(encodes, huffTree.root(), "");
		String encodeResult = "";
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return encodeResult;
		}
		String str = null;
		try {
			while((str = bufferedReader.readLine()) != null) {
				for (int i = 0;i < str.length(); ++i) {
					char ch = str.charAt(i);
					encodeResult += encodes.getOrDefault(ch, "");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return encodeResult;
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return encodeResult;
	}

	@Override
	public String decodeFile(String code, HuffTree huffTree) {
		String decodeResult = "";
		HuffBaseNode cur = huffTree.root();
		for (int i = 0;i < code.length(); ++i) {
			HuffInternalNode node = (HuffInternalNode)cur;
			if (code.charAt(i) == '0') {
				cur = node.left();
			} else {
				cur = node.right();
			}
			if (cur.isLeaf()) {
				HuffLeafNode leaf = (HuffLeafNode)cur;
				decodeResult += leaf.value();
				cur = huffTree.root();
			}
		}
		return decodeResult;
	}

	@Override
	public String traverseHuffmanTree(HuffTree huffTree) {
		return traverseHuffmanTreeRecursive(huffTree.root(), "");
	}

	private String traverseHuffmanTreeRecursive(HuffBaseNode root, String innerPath) {
		if (root == null) {
			return "";
		}
		if (root.isLeaf()) {
			HuffLeafNode node = (HuffLeafNode)root;
			return "" + node.value() + " " + innerPath + "\n";
		}
		HuffInternalNode node = (HuffInternalNode)root;
		String out = traverseHuffmanTreeRecursive(node.left(), innerPath + "0");
		out += traverseHuffmanTreeRecursive(node.right(), innerPath + "1");
		return out;
	}

	private void collectEncode(HashMap<Character, String> encodes, HuffBaseNode root, String innerPath) {
		if (root != null) {
			if (root.isLeaf()) {
				HuffLeafNode node = (HuffLeafNode)root;
				encodes.put(node.value(), innerPath);
			} else {
				HuffInternalNode node = (HuffInternalNode)root;
				collectEncode(encodes, node.left(), innerPath + "0");
				collectEncode(encodes, node.right(), innerPath + "1");
			}
		}
	}
}
