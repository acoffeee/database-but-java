import java.util.LinkedList;
import java.util.ArrayList;
public class LeafNode extends Node {
    /* header
    offset byte_num disc
    0 2 page type
    2 10 prev page ptr
    10 20 next page ptr
    data start here
    data is formated like this
    key row data
    key row data

*/
    private long NEXT_PAGE, PREV_PAGE;
    private LinkedList<ArrayList<Object>> valueList;
    public LeafNode(long prevP, long nextP,TreeNodeTypes type, long index ) {
        super(type, index);
        PREV_PAGE = prevP;
        NEXT_PAGE = nextP;
        LinkedList<ArrayList<Object>> valueList = new LinkedList<>();
    }
    public void addValue(ArrayList<Object> value) {
        valueList.add(value);
    }
    public void addValues(LinkedList<ArrayList<Object>> values) {
        for(ArrayList value : values) {
            valueList.add(value);
        }
    }
    public long getPrevPageNum() {
        return PREV_PAGE;
    }
    public long getNextPageNum() {
        return NEXT_PAGE;
    }
    public ArrayList<Object> getRow(int rowID) {
        return valueList.get(rowID);
    }
    public ArrayList<Object> removeFirstRow() {
        return valueList.removeFirst();
    }
    publi ArrayList<Object
    }
}