import java.util.ArrayList;
import java.lang.reflect.Type;
public class BTreeConfig extends Configuration{
    private int valueSize; //row size i think but if index then just that ptr ig
    private RowConfig rowConfig;
    private int headerSize; // header size
    private int leafHeaderSize;           
    private int internalNodeHeaderSize;   
    private int freePoolNodeHeaderSize; 
    private int leafNodeDegree;
    private int treeDegree; // max amount of entrys a node can have (internal)
    public BTreeConfig(int pageSize,int keySize, ArrayList<Type> types, ArrayList<Integer> sizes, int valueSize, int headerSize, RowConfig rowConfig) {
        super(pageSize,keySize,types, sizes);
        this.rowConfig = rowConfig;
        this.valueSize = valueSize; //entry size in bytes
        this.headerSize = (Integer.SIZE * 3 + 4 * Long.SIZE) / 8 ;
        // Type.SIZE returns bits.
        //short is page type, long is a pointer to the prev and next node (linked list). integer is the current capacity
        leafHeaderSize = (Short.SIZE + 2 * Long.SIZE + Integer.SIZE) / 8; //22 bytes
        //short = page type, int = capacity
        internalNodeHeaderSize = (Short.SIZE + Integer.SIZE) / 8; // 6 bytes
        //short = pageType, long=nextFreepool page, int = capacity
        freePoolNodeHeaderSize = (Short.SIZE + Long.SIZE + Integer.SIZE) / 8;
        leafNodeDegree = calcDegree(valueSize + keySize, leafHeaderSize);
        //data: key + value but since u have more values than keys u cant keep exact amount of keys other wise i think there will not be enough space for the values
        treeDegree = (pageSize - internalNodeHeaderSize - Long.SIZE /8 ) / (keySize + Long.SIZE / 8);
    }
    /*degree is the amount of unit that can fit in the page
    for a leaf page, it calculates max amount of rows that can fit (needs the row pointer + the data)
    */
    private int calcDegree(int elementSize, int pageHeader) {
        return (pageSize - pageHeader) / elementSize;
    }
    public int getMaxInternalNodeCapacity() { return treeDegree; }
    // treedegree - 1 because that means it has the apropiate amount of keys which is more than that ion stuff lol
    public int getMinInternalNodeCapacity() { return (treeDegree-1) / 2; }
    public int getMaxLeafNodeCapacity() { return leafNodeDegree; }
    public int getMinLeafNodeCapacity() { return (leafNodeDegree-1) / 2; }
    public RowConfig getRowConfig() { return rowConfig; }
    public int getLeafHeaderSize() { return leafHeaderSize; }
    
    
}