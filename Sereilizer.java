import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
public class Sereilizer {
    private BTreeConfig config;
    public Sereilizer(BTreeConfig c) { config = c; }
    
    public Node serializePage(byte[] page, long pageNum) {
        ByteBuffer bbuffer = ByteBuffer.wrap(page);
        return switch(bbuffer.getShort()) {
            case 0: {
                throw new DBException(DBException.NotImplemented); // leaf root
            }
            case 1: {
                throw new DBException(DBException.NotImplemented); // internal root
            }
            case 2: { //leaf
                Long prevPagePtr = bbuffer.getLong();
                Long nextPagePtr = bbuffer.getLong();
                int curCapacity = bbuffer.getInt();
                LeafNode lNode = new LeafNode(prevPagePtr, nextPagePtr,TreeNodeTypes.LEAF, pageNum);
                lNode.setCurrentCapacity(bbuffer.getInt());
                LinkedList<ArrayList<Object>> keys = readKeys(bbuffer, lNode, config.getLeafHeaderSize());
                LinkedList<ArrayList<Object>> values = readValues(bbuffer, lNode, config.getLeafHeaderSize());
                lNode.addKeys(keys);
                lNode.addValues(values);
                yield lNode;
            }
            case 3: {
                throw new DBException(DBException.NotImplemented); //internal
            }
            case 4: {
                throw new DBException(DBException.NotImplemented); //free pool
            }
            case 5: { //meta data
                throw new DBException(DBException.NotImlemented);
            }
            default: throw new DBException(DBException.NotImplemented);
        };
    }
    public 
    //arraylist cause multi column index
    private ArrayList<Object> readKey(ByteBuffer BBuffer) {
        ArrayList<Object> key = new ArrayList<>();
        ArrayList<Type> types = config.getKeyTypes();

        for(int i = 0; i < config.getKeyTypes().size(); i++) {
            
            if(types.get(i) == Integer.TYPE) key.add(BBuffer.getInt());
            else if(types.get(i) == Float.TYPE) key.add(BBuffer.getFloat());
            else if(types.get(i) == Double.TYPE) key.add(BBuffer.getDouble());
            else if(types.get(i) == Long.TYPE) key.add(BBuffer.getLong());
            else if(types.get(i) == String.class) {
                byte[] stringBuffer = new byte[config.getKeySizes().get(i)];
                BBuffer.get(stringBuffer, 0, stringBuffer.length);
                key.add(new String(stringBuffer, StandardCharsets.UTF_8));
            }
            else throw new DBException(DBException.InvalidType);
        }
        return key;
    }
    //assumes ByteBuffer.position() = row value byte start
    private ArrayList<Object> readRowValue(ByteBuffer BBuffer) {
        RowConfig rConfig = config.getRowConfig();
        ArrayList<Column> columns = rConfig.getColumns();
        ArrayList<Object> rowItems = new ArrayList<>();
        for(Column col : columns) {
            if(col.getType() == Integer.class) rowItems.add(BBuffer.getInt());
            else if(col.getType() == Short.class ) rowItems.add(BBuffer.getShort());
            else if(col.getType() == Float.class ) rowItems.add(BBuffer.getFloat());
            else if(col.getType() == Double.class ) rowItems.add(BBuffer.getDouble());
            else if(col.getType() == String.class ) {
                byte[] stringBuffer = new byte[col.getSize()];
                BBuffer.get(stringBuffer, 0, stringBuffer.length);
                rowItems.add(new String(stringBuffer, StandardCharsets.UTF_8));
            }
        }
        return rowItems;
    }
    private <T extends Node> LinkedList<ArrayList<Object>> readKeys(ByteBuffer BBuffer, T node, int headerOffset) {
        BBuffer.position(headerOffset);
        LinkedList<ArrayList<Object>> keys = new LinkedList<>();
        for(int i = 0; i < node.getCurrentCapacity(); i++ ) {
            keys.add(readKey(BBuffer));
            BBuffer.position(BBuffer.position() + config.getRowConfig().getValueSize());
        }
        return keys;
    }
    private <T extends Node> LinkedList<ArrayList<Object>> readValues(ByteBuffer BBuffer, T node, int headerOffset ) {
        // lowk should prolly make it so that the values are a linked list within the node so that like i can calculate the next position better cause rn if there not sequential it dies but also that is circumnavigated rn by like any update rewrites to storages and then re sets it, which in turn adds them sequntially in memory.
        BBuffer.position(headerOffset);
        LinkedList<ArrayList<Object>>  rows = new LinkedList<>();
        RowConfig rConfig = config.getRowConfig();
        ArrayList<Column> colTypes = rConfig.getColumns();
        for(int i = 0; i < node.getCurrentCapacity(); i++) {
            ArrayList<Object> row = new ArrayList<>();
            row.add(readRowValue(BBuffer));
            rows.add(row);
            BBuffer.position(BBuffer.position() + config.getKeySize());
        }
        return rows;
    }
    private ByteBuffer deserializeRowOrKey(ArrayList<Object> rowOrKey, int size) {
        ByteBuffer rawKeyOrRow = ByteBuffer.allocate(size);
        for(Object colOrKeyPart : rowOrKey) {
            switch(colOrKeyPart) {
                case Integer castedColOrKeyPart -> rawKeyOrRow.putInt(castedColOrKeyPart);
                case Float castedColOrKeyPart -> rawKeyOrRow.putFloat(castedColOrKeyPart);
                case Double castedColOrKeyPart ->rawKeyOrRow.putDouble(castedColOrKeyPart);
                case Short castedColOrKeyPart -> rawKeyOrRow.putShort(castedColOrKeyPart);
                case String castedColOrKeyPart ->rawKeyOrRow.put(castedColOrKeyPart);
                case null -> throw new DBException(DBException.NotImplemented); //potentiall support empty columns but then like i gotta implement error handling and checking if its allowed to be empty and also i gotta account for that when serilizing it and it sounds like a massive pain tbh so i just wont XD.
                default -> throw new DBException(DBException.NotImplemented); // what other types are there wtf
            }
        }
        rawKeyOrRow.flip(); // does a backflip
        return rawKeyOrRow;
    }
    private ByteBuffer deserializeKey(ArrayList<Object> key) {
        return deserializeRowOrKey(key, config.getKeySize());
    }
    private ByteBuffer deserializeRow(ArrayList<Object> row) {
        return deserializeRowOrKey(row, config.getValueSize());
    }
    public <T extends Node> ByteBuffer deserializeNode(T node) {
        ByteBuffer rawNodePage = ByteBuffer.allocate(config.getPageSize());
        if(node instanceof LeafNode) {
            lNode = (LeafNode)node;
            rawNodePage.putShort(2); //leaf type
            rawNodePage.putLong(lNode.getPrevPageNum()); 
            rawNodePage.putLong(lNode.getNextPageNum()); 
            rawNodePage.putInt(lNode.getCurrentCapacity());
            for(int i = 0; i< lNode.getCurrentCapacity()) {
                //pops head, deserilize it, then writes it
                rawNodePage.put(desereilizeKey(node.removeFirstKey()));
                rawNodePage.put(desereilizeRow(node.removeFirstRow()));
            }
            return rawNodePage;
            
        }
        if(node instanceof InternalNode) {
            iNode = (InternalNode)node;
            rawNodePage.putShort(3); //type
            rawNodePage.putInt(iNode.getCurrentCapacity());
            for(int i = 0; i< iNode.getCurrentCapacity()) {
                rawNodePage.put(desereilizeKey(node.removeFirstKey()));
                rawNodePage.putLong(node.removeFirstPtr());
            }
            return rawNodePage;
        }
    }
}