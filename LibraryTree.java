import java.util.ArrayList;
public class LibraryTree {
	
	public LibraryNode primaryRoot;		//root of the primary B+ tree
	public LibraryNode secondaryRoot;	//root of the secondary B+ tree
	public LibraryTree(Integer order)
	{
		LibraryNode.order = order;
		primaryRoot = new LibraryNodeLeaf(null);
		primaryRoot.level = 0;
		secondaryRoot = new LibraryNodeLeaf(null);
		secondaryRoot.level = 0;
	}
	
	public void addBook(CengBook book) {

		// add to the primary tree
		// if primary root is leaf
		if (primaryRoot.getType() == LibraryNodeType.Leaf){
			ArrayList<CengBook> books;
			Integer length, i;
			// if it is not full
			if (((LibraryNodeLeaf)primaryRoot).bookCount() < 2*LibraryNode.order){
				books = ((LibraryNodeLeaf)primaryRoot).getbooks();
				length = books.size();
				for(i=0; i<length && books.get(i).key()<book.key(); i++);
				((LibraryNodeLeaf)primaryRoot).addBook(i,book);
			}
			// if it is full
			else{
				ArrayList<Integer> keys;
				ArrayList<LibraryNode> children;
				LibraryNodeLeaf newLeaf1, newLeaf2;
				Integer nodeKey;
				books = ((LibraryNodeLeaf)primaryRoot).getbooks();
				length = books.size();
				for(i=0; i<length && books.get(i).key()<book.key(); i++);
				books.add(i,book);
				nodeKey = books.get(LibraryNode.order).key();
				keys = new ArrayList<Integer>();
				keys.add(nodeKey);
				children = new ArrayList<LibraryNode>();
				newLeaf1 = new LibraryNodeLeaf(null, (new ArrayList<CengBook> (books.subList(0,LibraryNode.order))));
				newLeaf2 = new LibraryNodeLeaf(null, (new ArrayList<CengBook> (books.subList(LibraryNode.order,2*LibraryNode.order+1))));
				children.add(newLeaf1);
				children.add(newLeaf2);
				primaryRoot = new LibraryNodePrimaryIndex(null, keys, children);
				newLeaf1.setParent(primaryRoot);
				newLeaf2.setParent(primaryRoot);
			}
		}
		// if it is node
		else{
			((LibraryNodePrimaryIndex)primaryRoot).addBook(book);
			if(primaryRoot.getParent()!=null){
				primaryRoot = primaryRoot.getParent();
			}
		}
		// add to the secondary tree
		// if secondary root is leaf
		if (secondaryRoot.getType() == LibraryNodeType.Leaf){
			ArrayList<CengBook> books;
			Integer length, i;
			// if it is not full
			if (((LibraryNodeLeaf)secondaryRoot).bookCount() < 2*LibraryNode.order){
				books = ((LibraryNodeLeaf)secondaryRoot).getbooks();
				length = books.size();
				for(i=0; i<length && books.get(i).year().intValue()<book.year().intValue(); i++);
				for(;i<length && books.get(i).year().intValue()==book.year().intValue() && books.get(i).key().intValue()<book.key().intValue();i++);
				((LibraryNodeLeaf)secondaryRoot).addBook(i,book);
			}
			// if it is full
			else{
				ArrayList<Integer> keys, years;
				ArrayList<LibraryNode> children;
				LibraryNodeLeaf newLeaf1, newLeaf2;
				Integer nodeKey, nodeYear;
				books = ((LibraryNodeLeaf)secondaryRoot).getbooks();
				length = books.size();
				for(i=0; i<length && books.get(i).year().intValue()<book.year().intValue(); i++);
				for(;i<length && books.get(i).year().intValue()==book.year().intValue() && books.get(i).key().intValue()<book.key().intValue();i++);
				books.add(i,book);
				nodeYear = books.get(LibraryNode.order).year();
				nodeKey = books.get(LibraryNode.order).key();
				keys = new ArrayList<Integer>();
				years = new ArrayList<Integer>();
				keys.add(nodeKey);
				years.add(nodeYear);
				children = new ArrayList<LibraryNode>();
				newLeaf1 = new LibraryNodeLeaf(null, (new ArrayList<CengBook> (books.subList(0,LibraryNode.order))));
				newLeaf2 = new LibraryNodeLeaf(null, (new ArrayList<CengBook> (books.subList(LibraryNode.order,2*LibraryNode.order+1))));
				children.add(newLeaf1);
				children.add(newLeaf2);
				secondaryRoot = new LibraryNodeSecondaryIndex(null, years, keys, children);
				newLeaf1.setParent(secondaryRoot);
				newLeaf2.setParent(secondaryRoot);
			}
		}
		// if it is node
		else{
			((LibraryNodeSecondaryIndex)secondaryRoot).addBook(book);
			if(secondaryRoot.getParent()!=null){
				secondaryRoot = secondaryRoot.getParent();
			}
		}
	}
	
	
	
	public CengBook searchBook(Integer key) {

		// if it is leaf
		if (primaryRoot.getType() == LibraryNodeType.Leaf){
			ArrayList<CengBook> books;
			Integer length, i;
			books = ((LibraryNodeLeaf)primaryRoot).getbooks();
			length = books.size();
			for(i=0; i<length && books.get(i).key()<key; i++);
			if(books.get(i).key()==key){
				System.out.println("<data>");
				System.out.print("<record>");
				System.out.print(books.get(i).key());
				System.out.print("|");
				System.out.print(books.get(i).year());
				System.out.print("|");
				System.out.print(books.get(i).name());
				System.out.print("|");
				System.out.print(books.get(i).author());
				System.out.println("</record>");
				System.out.println("</data>");
				return books.get(i);
			}
			else{
				System.out.print("No match for ");
				System.out.println(key);
				return null;
			}
		}
		// if it is node
		else{
			return ((LibraryNodePrimaryIndex)primaryRoot).searchBook(key);
		}
	}
	
	
	public void printPrimaryLibrary() {
		
		// if it is leaf
		if (primaryRoot.getType() == LibraryNodeType.Leaf){
			ArrayList<CengBook> books;
			Integer length, i;
			books = ((LibraryNodeLeaf)primaryRoot).getbooks();
			length = books.size();
			System.out.println("<data>");
			for(i=0; i<length; i++){
				System.out.print("<record>");
				System.out.print(books.get(i).key());
				System.out.print("|");
				System.out.print(books.get(i).year());
				System.out.print("|");
				System.out.print(books.get(i).name());
				System.out.print("|");
				System.out.print(books.get(i).author());
				System.out.println("</record>");
			}
			System.out.println("</data>");
		}
		// if it is node
		else{
			((LibraryNodePrimaryIndex)primaryRoot).print();
		}
	}
	
	public void printSecondaryLibrary() {

		// if it is leaf
		if (secondaryRoot.getType() == LibraryNodeType.Leaf){
			ArrayList<CengBook> books;
			Integer length, i;
			books = ((LibraryNodeLeaf)secondaryRoot).getbooks();
			length = books.size();
			System.out.println("<data>");
			for(i=0; i<length; i++){
				System.out.print("<record>");
				System.out.print(books.get(i).key());
				System.out.print("|");
				System.out.print(books.get(i).year());
				System.out.print("|");
				System.out.print(books.get(i).name());
				System.out.print("|");
				System.out.print(books.get(i).author());
				System.out.println("</record>");
			}
			System.out.println("</data>");
		}
		// if it is node
		else{
			((LibraryNodeSecondaryIndex)secondaryRoot).print();
		}
		
	}
	
	// Extra functions if needed
}
