public class Database {
    private BTreeConfig metadata;
    private Pager pager;
    public Database() {}
    public void createDatabaseFile(int pageSize,int keySize, ArrayList<Type> types, ArrayList<Integer> sizes, int valueSize, int headerSize, RowConfig rowConfig) {
        metadata = new BTreeConfig(pageSize)
}