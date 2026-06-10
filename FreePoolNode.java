public class FreePoolNode {
    /* header
    offset length disc
    0 2 page type 
    2 10 nextPointer
    */
    public FreePoolNode(long pageIndex, long nextPointer) {
        super(TreeNodeTypes.FREE_POOL, pageIndex);
    }
}