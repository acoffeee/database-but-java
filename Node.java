import java.util.LinkedList;
import java.util.ArrayList;
import java.lang.reflect.Type;
abstract class Node {
    
    protected final LinkedList<ArrayList<Object>> keys; //key array
    private TreeNodeTypes type; // node type
    private long pageIndex;
    private int currentCapacity;
    
    public void write(this) {
        Pager.writePage(this);
    }
    public long getPageIndex() 
        { return pageIndex; }
        
    long getCurrentCapacity() 
        { return currentCapacity; }
        
    public void setCurrentCapacity(int newCapacity) 
        { currentCapacity = newCapacity; }
        
    public void incrementCurrentCapacity()
        { currentCapacity++; }
        
    public void decrementCurrentCapacity()
        { currentCapacity--; }
    public Node(TreeNodeTypes type,long pageIndex ) {
        this.type = type;
        this.pageIndex = pageIndex;
        keys = new LinkedList<>();
    }
    TreeNodeTypes getType() { return type; }
    boolean isFull(BTreeConfig c) {
        return switch(type) {
            case TreeNodeTypes.LEAF -> c.getMaxLeafNodeCapacity() == currentCapacity;
            case TreeNodeTypes.INTERNAL -> c.getMaxInternalNodeCapacity() == currentCapacity;
            default -> false;
        };
    }
    private void validateCurrentCapacity(BTreeConfig c) throws DBException {
        if(isLeaf()) {
            if (getCurrentCapacity() > c.getMaxLeafNodeCapacity() ||
                getCurrentCapacity() < c.getMinLeafNodeCapacity())
                throw new DBException(DBException.InvalidNodeState);
        }
    }
    private boolean isRoot() { return type == TreeNodeTypes.ROOT; }
    private boolean isLeaf() { return type == TreeNodeTypes.LEAF; }
    private boolean isInternal() { return type == TreeNodeTypes.INTERNAL; }
    private ArrayList<Object> getKeyAt(int keyPos) {
        return keys.get(keyPos);
    }
    public void addKey(ArrayList<Object> key) {
        keys.add(key);
    }
    public void addKeys(LinkedList<ArrayList<Object>> newKeys) {
        for(ArrayList key : newKeys) {
            keys.add(key);
        }
    }
    public void updateCapacity() {
        currentCapacity = keys.length();
    }
    public ArrayList<Object> removeKeyAt(int index) {
        return keys.remove(index);
    }
    public ArrayList<Object> popKey() {
        return keys.pop();
    }
    public ArrayList<Object> removeFirstKey() {
        return keys.removeFirst();
    }
    
   
}