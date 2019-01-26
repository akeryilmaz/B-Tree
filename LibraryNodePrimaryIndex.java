import java.util.ArrayList;

public class LibraryNodePrimaryIndex extends LibraryNode
{
	private ArrayList<Integer> keys;
	private ArrayList<LibraryNode> children;	
	
	public LibraryNodePrimaryIndex(LibraryNode parent) 
	{
		super(parent);
		keys = new ArrayList<Integer>();
		children = new ArrayList<LibraryNode>();
		this.type = LibraryNodeType.Internal;
	}
	
	public LibraryNodePrimaryIndex(LibraryNode parent, ArrayList<Integer> keys, ArrayList<LibraryNode> children) 
	{
		super(parent);
		this.keys = keys;
		this.children = children;
		this.type = LibraryNodeType.Internal;
	}
	
	// GUI Methods - Do not modify
	public ArrayList<LibraryNode> getAllChildren()
	{
		return this.children;
	}
	
	public LibraryNode getChildrenAt(Integer index) {
		
		return this.children.get(index);
	}
	
	public Integer keyCount()
	{
		return this.keys.size();
	}
	public Integer keyAtIndex(Integer index)
	{
		if(index >= this.keyCount() || index < 0)
		{
			return -1;
		}
		else
		{
			return this.keys.get(index);			
		}
	}
	
	// Extra functions if needed
	
	public void addBook(CengBook book){
		// if its children are nodes
		if (this.children.get(0).type == LibraryNodeType.Internal){
			int length, i;
			length = this.keys.size();
			for(i=0;  i<length && this.keys.get(i)<book.key(); i++);
			((LibraryNodePrimaryIndex)this.children.get(i)).addBook(book);
		}
		// if its children are leaves
		else{
			ArrayList<CengBook> books;
			int length, i;
			length = this.keys.size();
			for(i=0;  i<length && this.keys.get(i)<book.key(); i++);
			// if it is not full
			if (((LibraryNodeLeaf)this.children.get(i)).bookCount() < 2*LibraryNode.order){
				int j;
				books = ((LibraryNodeLeaf)this.children.get(i)).getbooks();
				length = books.size();
				for(j=0; j<length && books.get(j).key()<book.key(); j++);
				((LibraryNodeLeaf)this.children.get(i)).addBook(j,book);
			}
			// if it is full
			else{
				LibraryNodeLeaf newChild1, newChild2, currentLeaf;
				int newNodeKey, j;
				currentLeaf = (LibraryNodeLeaf)this.children.get(i);
				books = currentLeaf.getbooks();
				length = books.size();
				for(j=0; j<length && books.get(j).key()<book.key(); j++);
				books.add(j,book);
				newChild1 = new LibraryNodeLeaf(null, (new ArrayList<CengBook> (books.subList(0,LibraryNode.order))));
				newChild2 = new LibraryNodeLeaf(null, (new ArrayList<CengBook> (books.subList(LibraryNode.order,2*LibraryNode.order+1))));
				newNodeKey = books.get(LibraryNode.order).key();
				this.addKeyAnd2Children(newNodeKey, newChild1, newChild2);
			}
		}
	}

	private void addKeyAnd2Children(int nodeNewKey, LibraryNode newChild1, LibraryNode newChild2){
		// if it is not full
		if(this.keyCount() < 2*LibraryNode.order){
			int length, i;
			ArrayList<LibraryNode> newChildren = this.children;
			length = this.keys.size();
			for(i=0;  i<length && this.keys.get(i)<nodeNewKey; i++);
			this.keys.add(i, nodeNewKey);
			newChild1.setParent(this);
			newChild2.setParent(this);
			newChildren.set(i, newChild1);
			newChildren.add(i+1, newChild2);
			this.children = newChildren;
		}
		// if it is full
		else{
			ArrayList<Integer> newKeys = this.keys;
			ArrayList<LibraryNode> newChildren = this.children;
			int length, i,parentNewKey;
			LibraryNodePrimaryIndex parentNewChild;
			length = keys.size();
			for(i=0; i<length && newKeys.get(i)<nodeNewKey; i++);
			newKeys.add(i,nodeNewKey);
			newChildren.set(i, newChild1);
			newChildren.add(i+1, newChild2);
			this.keys = new ArrayList<Integer> (newKeys.subList(0,LibraryNode.order));
			parentNewKey = newKeys.get(LibraryNode.order);
			this.children = new ArrayList<LibraryNode> (newChildren.subList(0,LibraryNode.order+1));
			parentNewChild = new LibraryNodePrimaryIndex(null, ( new ArrayList<Integer> (newKeys.subList(LibraryNode.order+1,2*LibraryNode.order+1))), (new ArrayList<LibraryNode> (newChildren.subList(LibraryNode.order+1,2*LibraryNode.order+2))));
			if (i+1 < LibraryNode.order){
				newChild1.setParent(this);
				newChild2.setParent(this);
			}
			else if (i < LibraryNode.order){
				newChild1.setParent(this);
				newChild2.setParent(parentNewChild);
			}
			else {
				newChild1.setParent(parentNewChild);
				newChild2.setParent(parentNewChild);
			}
			
			if(this.getParent() == null){
				ArrayList<LibraryNode> newParentChildren;
				ArrayList<Integer> newParentKeys;
				newParentChildren = new ArrayList<LibraryNode>();
				newParentChildren.add((LibraryNode) this);
				newParentChildren.add((LibraryNode) parentNewChild);
				newParentKeys = new ArrayList<Integer>();
				newParentKeys.add(parentNewKey);
				this.setParent((LibraryNode) new LibraryNodePrimaryIndex(null, newParentKeys , newParentChildren));
				parentNewChild.setParent(this.getParent());
			}
			else{
				((LibraryNodePrimaryIndex)this.getParent()).addKeyand1Child(parentNewKey, parentNewChild);
			}
		}
	}

	private void addKeyand1Child(int nodeNewKey, LibraryNodePrimaryIndex newChild){
		// if it is not full
		if(this.keyCount() < 2*LibraryNode.order){
			int length, i;
			ArrayList<LibraryNode> newChildren = this.children;
			length = this.keys.size();
			for(i=0;  i<length && this.keys.get(i)<nodeNewKey; i++);
			this.keys.add(i, nodeNewKey);
			newChild.setParent(this);
			newChildren.add(i+1, newChild);
			this.children = newChildren;
		}
		// if it is full
		else{
			int length, i, parentNewKey;
			ArrayList<LibraryNode> newChildren = this.children;
			ArrayList<Integer> newKeys = this.keys;
			LibraryNodePrimaryIndex parentNewChild;
			length = this.keys.size();
			for(i=0;  i<length && this.keys.get(i)<nodeNewKey; i++);
			newKeys.add(i, nodeNewKey);
			newChildren.add(i+1, newChild);
			this.keys = new ArrayList<Integer> (newKeys.subList(0,LibraryNode.order));
			parentNewKey = newKeys.get(LibraryNode.order);
			this.children = new ArrayList<LibraryNode> (newChildren.subList(0,LibraryNode.order+1));
			parentNewChild = new LibraryNodePrimaryIndex(null, (new ArrayList<Integer> (newKeys.subList(LibraryNode.order+1,2*LibraryNode.order+1))), (new ArrayList<LibraryNode> (newChildren.subList(LibraryNode.order+1,2*LibraryNode.order+2))));
			if (i+1 < LibraryNode.order){
				newChild.setParent(this);
			}
			else {
				newChild.setParent(parentNewChild);
			}
			if(this.getParent() == null){
				ArrayList<LibraryNode> newParentChildren;
				ArrayList<Integer> newParentKeys;
				newParentChildren = new ArrayList<LibraryNode>();
				newParentChildren.add(this);
				newParentChildren.add(parentNewChild);
				newParentKeys = new ArrayList<Integer>();
				newParentKeys.add(parentNewKey);
				this.setParent(new LibraryNodePrimaryIndex(null, newParentKeys , newParentChildren));
				parentNewChild.setParent(this.getParent());
			}
			else{
				((LibraryNodePrimaryIndex)this.getParent()).addKeyand1Child(parentNewKey, parentNewChild);
			}
		}
	}

	public CengBook searchBook(int key){
		LibraryNode result = null;
		int length = this.keys.size(), i;
		System.out.println("<index>");
		for(i=0;  i<length; i++){
			System.out.println(this.keys.get(i));
			if (this.keys.get(i)<=key){
				result = children.get(i+1);
			}
		}
		System.out.println("</index>");
		// if its children are nodes
		if (this.children.get(0).type == LibraryNodeType.Internal){
			LibraryNodePrimaryIndex currentNode;
			currentNode = (LibraryNodePrimaryIndex) result;
			if (result!=null){
				return currentNode.searchBook(key);
			}
			else{
				return ((LibraryNodePrimaryIndex)this.children.get(0)).searchBook(key);
			}
		}
		// if its children are leaves
		else{
			ArrayList<CengBook> books;
			LibraryNodeLeaf currentLeaf;
			if (result==null){
				result = children.get(0);
			}
			currentLeaf = (LibraryNodeLeaf) result;
			books = currentLeaf.getbooks();
			length = books.size();
			for(i=0; i<length; i++){
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
			}
			System.out.print("No match for ");
			System.out.println(key);
			return null;
		}
	}

	public void print(){
		int length = this.keys.size(), i;
		System.out.println("<index>");
		for(i=0;  i<length; i++){
			System.out.println(this.keys.get(i));
		}
		System.out.println("</index>");
		// if its children are nodes
		if (this.children.get(0).getType() == LibraryNodeType.Internal){
			for(LibraryNode child:this.children){
				LibraryNodePrimaryIndex currentNode;
				currentNode = (LibraryNodePrimaryIndex) child;
				currentNode.print();
			}
		}
		// if its children are leaves
		else{
			for(LibraryNode child:this.children){
				ArrayList<CengBook> books;
				LibraryNodeLeaf currentLeaf;
				currentLeaf = (LibraryNodeLeaf) child;
				books = currentLeaf.getbooks();
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
		}
	}

}
