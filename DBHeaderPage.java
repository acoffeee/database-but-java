public class DBHeaderPage extends Node {
    /*
    # format type
16 | string describing format | "Snack Database"  (14bytes but its okay)
# header page
4 | page size
8 | free page list start
2 | amount of indexes
2 * indexCount | pointers to b tree configs
*/
    private char[] headerString;
    private int pageSize;
    private long freePageListStart;
    private short tableCount;
    private LinkedList<Short> tablePtrs;
    public DBHeaderPage() {
        super(TreeNodeTypes.METADATA, 0);
        this.headerString = {'S', 'n', 'a', 'c', 'k', ' ', 'D', 'a', 't', 'a', 'b', 'a', 's', 'e', 'e', 'e';
        this.pageSize = 
            
        }
    
}