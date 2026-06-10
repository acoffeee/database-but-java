import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;
public class BTree {
    private Node Root;
    private BTreeConfig config;
    private ArrayList<Long> freePages;
    private Long usedPages;
    private Long totalPages;
    private Long firstFreePoolPointer;
    private Pager pager;
    private Serieilizer seriel;
    private Node aChild;
    /*
    This is how an internal node split
    min(2) max 4
    @param node is the parent node
    @param index is the index in the parent node we will add the median for.
    Say we're in the insert functio    
        [0,1,3,4] and what if we add 2?
        [0,1,2,3,4] so we need to split it.
        this function will walk you through it.
    */
    public void splitNode(Node node, int index) {
        //aChild is the current node. the reason we use aChild instead of current node is to demonstrate that @param node is the parent and aChild is a child of that node.
        if(aChild.isInternalNode()) {
            /*right now its
                [5]
                /   \
    [0,1,2,3,4]   [...] (sometimes you can redistribute but thats later)
            */
            //your making two nodes, [] [0,1,2,3,4]
            InternalNode rightNode = (InternalNode)aChild;
            InternalNode leftNode = new InternalNode(TreeNodeTypes.Internal, generateFreePageIndex());
            //if(canRedistribute) redistribute();
            //TODO: deal with non unnique keys
            //grab the current amount of elements. and also the median index.
            int oldCapacity = rightNode.getCurrentCapacity();
            int nodeSplitNumber = oldCapacity / 2;
            int i;
            //Internalnodes store keys and pointers as linked lists
            /*
            5/2 = 2 so 2 iterations
            [] [0,1,2,3,4]
            iter 1: [0] [1,2,3,4]
            iter 2: [0,1] [2,3,4]
            */
            for(i = 0; i < nodeSplitNumber; i++) {
                leftNode.addPair(i, rightNode.popKey(), rightNode.popValue());
            }
            //leftnode: keys: [*,0,*,1]
            //rightnode: keys [*,2,*,3,*,4,*]
            //but we are promoting 2, so
            //          /[2]\
            //  [*,0,*,1]   [*,*,3,*,4,*]
            //so we can move the first pointer to the left node
            leftNode.addPtrAt(i, rightNode.popPtr());
            //so you now have [*,0,*,1,*] [*,*,4,*]
            //but 2 is not a actual standalone key so
            // if we had [*0,*,5,*]
            //          /  |      \_____________\
            //      [...][the now split child]  [other edge]
            //we insert the key, 2 at 1 so [0,2,5]
            node.addPair(index, leftNode.getPageIndex(), rightNode.popPtr());
            rightNode.setCurrentCapacity(oldCapacity - nodeSplitNumber - 1);
            leftNode.setCurrentCapacity(nodeSplitNumber);
            if(node.isRoot()) node.setNodeType(TreeNodeTypes.INTERNAL);
            node.incrementCapacity();
            node.write();
            leftNode.write();
            rightNode.write();
            
        }
    }
    public SearchResult search(Node current, ArrayList<Object> key) {
        int keyLocation = binarySearch(current, key, 0, current.getCurrentCapacity());
        if(current.isInternalNode()) {
            InternalNode iNode = (InternalNode)current;
            int ptrLocation = keyLocation;
            //if at end, get keylocation + 1 cause like the value ptrs will always be key +1
            if(config.ge(key, current.getKeyAt(search))) ptrLocation++;
            Node node = readNode(ptrLocation);
            //recursively descend through tho tree
            search(node, key);
        }
        else if(current.isLeafNode()) {
            LeafNode lNode = (LeafNode)current;
            if(keyLocation == lNode.getCurrentCapacity() || config.neq(key, lNode.getKeyAt(keyLocation))) return new SearchResult(lNode, keyLocation, false)
            else return new SearchResult(lNode, keyLocation, true)
            else throw new DBException(DBException.BadNodeType);
        }
    }
    //search is [leftBound, rightBound)
    //finds index of key
    // if key is smaller than the smallest key in the node, lowerBound - 1 is returned
    // if key is larger than the lergest key in the node, n -1 is returned.
    // returns closest match, left justified
    private int binarySearch(InternalNode node, ArrayList<Object> key, int leftBound, int rightBound) {
        while(leftBound < rightBound) {
            int middle = (leftBound + rightBound) / 2;
            if(config.lt(key, node.getKeyAt(middle))) {
                rightBound = middle;
            } else {
                leftBound = middle + 1;
            }
        }
        return leftBound - 1;
    }
    
    public static void delete() 
    {
        
    }
    public static void update(int key, Value value)
    public Node readNode(int page) {
        return sereil.serializePage(pager.getPage(page), page);
    }
    private long generateFreePageIndex() { 
        usedPages++;
        //total pages is 1 indexed, while pages is an array kinda thats 0 indexed. 
        return freePages.size() == 0 ? totalPages++ : freePages.pop();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}