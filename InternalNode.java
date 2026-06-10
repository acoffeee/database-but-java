import java.util.LinkedList;
public class InternalNode extends Node{
    /* headers
    offset length disc
    0 2 page type
    2 6 capacity
    */
    LinkedList<Long> pointers;
    public InterenalNode(TreeNodeTypes type, long index) {
        super(type, index);
        pointers = new LinkedList<>();
    }
    public void addPointers(LinkedList<Long> newPointers) {
        for(Long ptr : newPointers) pointers.add(ptr);
    }
    // where ptr1,key1,ptr2,key2,ptr2 etc, returns the left ptr
    public Long getLeftPtr(int keyPos) {
        return pointers.get(keyPos);
    }
    public Long getRightPtr(int keyPos) {
        return pointers.get(keyPos + 1);
    }
    public long getLastPtr() {
        return pointers.getLast();
    }
    public long getPtrAt(int index) {
        return pointers.get(index);
    }
    public void addPtrAt(int index, long ptr) {
        pointers.add(index, ptr);
    }
    public void addLastPtr(Long ptrValue) {
        pointers.addLast(ptrValue);
    }

    public long popPtr() {
        return pointers.pop();
    }
    public long removeFirstPtr() {
        return pointers.removeFirst();
    }
    /*
    |key|key|key|key|key|key|
    |ptr|ptr|ptr|ptr|ptr|ptr|ptr|
    */
    public void insertPair(int index, ArrayList<Object> key, long pointer) {
        keys.add(index, key);
        pointers.add(index, pointer);
    }
}