import java.io.File;

public class Test {
	public static void main(String[] args) {
		File file = new File("illiad.txt");
		HuffmanCoding huffmanCoding = new HuffmanCodingImpl();
		
		String frequency = huffmanCoding.getFrequencies(file);
		System.out.println("frequceny for illiad.txt: ");
		System.out.println(frequency);
		
		HuffTree tree = huffmanCoding.buildTree(file);
		String codes = huffmanCoding.traverseHuffmanTree(tree);
		System.out.println("codes for illiad.txt: ");
		System.out.println(codes);
		
		String decode = huffmanCoding.decodeFile("1101000111011010110100011101111101000111011011", tree);
		System.out.println("decode for 1101000111011010110100011101111101000111011011 is " + decode);
		
		String encoding = huffmanCoding.encodeFile(file, tree);
		System.out.println("encoding for illiad.txt (this method is so slow...): ");
		System.out.println(encoding);
	}
}
