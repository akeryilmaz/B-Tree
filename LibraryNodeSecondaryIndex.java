import java.util.ArrayList;

public class LibraryNodeSecondaryIndex extends LibraryNode
{
	private ArrayList<Integer> keys;
	private ArrayList<Integer> years;
	private ArrayList<LibraryNode> children;	
	
	public LibraryNodeSecondaryIndex(LibraryNode parent) 
	{
		super(parent);
		keys = new ArrayList<Integer>();
		years = new ArrayList<Integer>();
		children = new ArrayList<LibraryNode>();
		this.type = LibraryNodeType.Internal;
	}
	
	public LibraryNodeSecondaryIndex(LibraryNode parent, ArrayList<Integer> years, ArrayList<Integer> keys, ArrayList<LibraryNode> children) 
	{
		super(parent);
		this.years = years;
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
	
	public Integer yearAtIndex(Integer index)
	{
		if(index >= this.keyCount() || index < 0)
		{
			return -1;
		}
		else
		{
			return this.years.get(index);			
		}
	}
	
	
	// Extra functions if needed

	public void addBook(CengBook book){
		// if its children are nodes
		if (this.children.get(0).type == LibraryNodeType.Internal){
			Integer length, i=0;
			length = this.keys.size();
			for(i=0;  i<length && this.years.get(i)<book.year(); i++);
			for(; i<length && this.years.get(i).intValue()==book.year().intValue() && this.keys.get(i)<book.key(); i++);
			((LibraryNodeSecondaryIndex)this.children.get(i)).addBook(book);
		}
		// if its children are leaves
		else{
			ArrayList<CengBook> books;
			Integer length, i;
			length = this.keys.size();
			for(i=0;  i<length && this.years.get(i)<book.year(); i++);
			for(; i<length && this.years.get(i).intValue()==book.year().intValue() && this.keys.get(i)<book.key(); i++);
			// if it is not full
			if (((LibraryNodeLeaf)this.children.get(i)).bookCount() < 2*LibraryNode.order){
				Integer j;
				books = ((LibraryNodeLeaf)this.children.get(i)).getbooks();
				length = books.size();
				for(j=0; j<length && books.get(j).year()<book.year(); j++);
				for(;j<length && books.get(j).year().intValue()==book.year().intValue() && books.get(j).key()<book.key(); j++);
				((LibraryNodeLeaf)this.children.get(i)).addBook(j,book);
			}
			// if it is full
			else{
				LibraryNodeLeaf newChild1, newChild2, currentLeaf;
				Integer newNodeKey, newNodeYear, j;
				currentLeaf = (LibraryNodeLeaf)this.children.get(i);
				books = currentLeaf.getbooks();
				length = books.size();
				for(j=0; j<length && books.get(j).year()<book.year(); j++);
				for(;j<length && books.get(j).year().intValue()==book.year().intValue() && books.get(j).key()<book.key(); j++);
				books.add(j,book);
				newChild1 = new LibraryNodeLeaf(null, (new ArrayList<CengBook> (books.subList(0,LibraryNode.order))));
				newChild2 = new LibraryNodeLeaf(null, (new ArrayList<CengBook> (books.subList(LibraryNode.order,2*LibraryNode.order+1))));
				newNodeKey = books.get(LibraryNode.order).key();
				newNodeYear = books.get(LibraryNode.order).year();
				this.addKeyAnd2Children(newNodeKey, newNodeYear, newChild1, newChild2);
			}
		}
	}

	private void addKeyAnd2Children(Integer nodeNewKey, Integer nodeNewYear,  LibraryNode newChild1, LibraryNode newChild2){
		// if it is not full
		if(this.keyCount() < 2*LibraryNode.order){
			Integer length, i;
			ArrayList<LibraryNode> newChildren = this.children;
			length = this.keys.size();
			for(i=0;  i<length && this.years.get(i)<nodeNewYear; i++);
			for(;  i<length && this.years.get(i).intValue()==nodeNewYear.intValue() && this.keys.get(i)<nodeNewKey; i++);
			this.keys.add(i, nodeNewKey);
			this.years.add(i, nodeNewYear);
			newChild1.setParent(this);
			newChild2.setParent(this);
			newChildren.set(i, newChild1);
			newChildren.add(i+1, newChild2);
			this.children = newChildren;
		}
		// if it is full
		else{
			ArrayList<Integer> newKeys = this.keys, newYears = this.years;
			ArrayList<LibraryNode> newChildren = this.children;
			Integer length, i,parentNewKey, parentNewYear;
			LibraryNodeSecondaryIndex parentNewChild;
			length = keys.size();
			for(i=0;  i<length && this.years.get(i)<nodeNewYear; i++);
			for(;  i<length && this.years.get(i).intValue()==nodeNewYear.intValue() && this.keys.get(i)<nodeNewKey; i++);
			newKeys.add(i,nodeNewKey);
			newYears.add(i,nodeNewYear);
			newChildren.set(i, newChild1);
			newChildren.add(i+1, newChild2);
			this.keys = new ArrayList<Integer> (newKeys.subList(0,LibraryNode.order));
			this.years = new ArrayList<Integer> (newYears.subList(0,LibraryNode.order));
			parentNewKey = newKeys.get(LibraryNode.order);
			parentNewYear = newYears.get(LibraryNode.order);
			this.children = new ArrayList<LibraryNode> (newChildren.subList(0,LibraryNode.order+1));
			parentNewChild = new LibraryNodeSecondaryIndex(null, (new ArrayList<Integer> (newYears.subList(LibraryNode.order+1,2*LibraryNode.order+1))) ,( new ArrayList<Integer> (newKeys.subList(LibraryNode.order+1,2*LibraryNode.order+1))), (new ArrayList<LibraryNode> (newChildren.subList(LibraryNode.order+1,2*LibraryNode.order+2))));
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
				ArrayList<Integer> newParentKeys, newParentYears;
				newParentChildren = new ArrayList<LibraryNode>();
				newParentChildren.add((LibraryNode) this);
				newParentChildren.add((LibraryNode) parentNewChild);
				newParentKeys = new ArrayList<Integer>();
				newParentYears = new ArrayList<Integer>();
				newParentKeys.add(parentNewKey);
				newParentYears.add(parentNewYear);
				this.setParent((LibraryNode) new LibraryNodeSecondaryIndex(null, newParentYears, newParentKeys , newParentChildren));
				parentNewChild.setParent(this.getParent());
			}
			else{
				((LibraryNodeSecondaryIndex)this.getParent()).addKeyand1Child(parentNewKey, parentNewYear, parentNewChild);
			}
		}
	}

	private void addKeyand1Child(Integer nodeNewKey, Integer nodeNewYear,  LibraryNodeSecondaryIndex newChild){
		// if it is not full
		if(this.keyCount() < 2*LibraryNode.order){
			Integer length, i;
			ArrayList<LibraryNode> newChildren = this.children;
			length = this.keys.size();
			for(i=0;  i<length && this.years.get(i)<nodeNewYear; i++);
			for(;  i<length && this.years.get(i).intValue()==nodeNewYear.intValue() && this.keys.get(i)<nodeNewKey; i++);
			this.keys.add(i, nodeNewKey);
			this.years.add(i, nodeNewYear);
			newChild.setParent(this);
			newChildren.add(i+1, newChild);
			this.children = newChildren;
		}
		// if it is full
		else{
			Integer length, i, parentNewKey, parentNewYear;
			ArrayList<LibraryNode> newChildren = this.children;
			ArrayList<Integer> newKeys = this.keys, newYears = this.years;
			LibraryNodeSecondaryIndex parentNewChild;
			length = this.keys.size();
			for(i=0;  i<length && this.years.get(i)<nodeNewYear; i++);
			for(;  i<length && this.years.get(i).intValue()==nodeNewYear.intValue() && this.keys.get(i)<nodeNewKey; i++);
			newKeys.add(i, nodeNewKey);
			newYears.add(i, nodeNewYear);
			newChildren.add(i+1, newChild);
			this.keys = new ArrayList<Integer> (newKeys.subList(0,LibraryNode.order));
			this.years = new ArrayList<Integer> (newYears.subList(0,LibraryNode.order));
			parentNewKey = newKeys.get(LibraryNode.order);
			parentNewYear = newYears.get(LibraryNode.order);
			this.children = new ArrayList<LibraryNode> (newChildren.subList(0,LibraryNode.order+1));
			parentNewChild = new LibraryNodeSecondaryIndex(null, (new ArrayList<Integer> (newYears.subList(LibraryNode.order+1,2*LibraryNode.order+1))), (new ArrayList<Integer> (newKeys.subList(LibraryNode.order+1,2*LibraryNode.order+1))), (new ArrayList<LibraryNode> (newChildren.subList(LibraryNode.order+1,2*LibraryNode.order+2))));
			if (i+1 < LibraryNode.order){
				newChild.setParent(this);
			}
			else {
				newChild.setParent(parentNewChild);
			}
			if(this.getParent() == null){
				ArrayList<LibraryNode> newParentChildren;
				ArrayList<Integer> newParentKeys, newParentYears;
				newParentChildren = new ArrayList<LibraryNode>();
				newParentChildren.add(this);
				newParentChildren.add(parentNewChild);
				newParentKeys = new ArrayList<Integer>();
				newParentYears = new ArrayList<Integer>();
				newParentKeys.add(parentNewKey);
				newParentYears.add(parentNewYear);
				this.setParent(new LibraryNodeSecondaryIndex(null, newParentYears, newParentKeys , newParentChildren));
				parentNewChild.setParent(this.getParent());
			}
			else{
				((LibraryNodeSecondaryIndex)this.getParent()).addKeyand1Child(parentNewKey, parentNewYear, parentNewChild);
			}
		}
	}

	public void print(){
		Integer length = this.keys.size(), i;
		System.out.println("<index>");
		for(i=0;  i<length; i++){
			System.out.println( this.years.get(i) + "|" + this.keys.get(i));
		}
		System.out.println("</index>");
		// if its children are nodes
		if (this.children.get(0).getType() == LibraryNodeType.Internal){
			for(LibraryNode child:this.children){
				LibraryNodeSecondaryIndex currentNode;
				currentNode = (LibraryNodeSecondaryIndex) child;
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
