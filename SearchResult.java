//helper class for btree
public class SearchResult {
    public LeafNode node; // node
    public final int index; // index where key is found
    public final boolean found;
    public SearchResult(LeafNode n, int index, boolean found) {
        node = n;
        this.index = index;
        this.found = found;
    }
}