import java.util.ArrayList;
import java.lang.reflect.Type;
public class RowConfig {
    private ArrayList<Column> columns;
    public RowConfig(ArrayList<Column> columns) {
        this.columns = columns;
    }
    public ArrayList<Column> getColumns() { return columns; }
    public ArrayList<Type> getTypes() {
        ArrayList<Type> types = new ArrayList<>();
        columns.forEach(col -> types.add(col.getType()));
        return types;
    }
    public ArrayList<Integer> getSizes() {
        ArrayList<Integer> sizes = new ArrayList<>();
        columns.forEach(col -> sizes.add(col.getSize()));
        return sizes;
    }
    public int getValueSize() { 
        int size = 0;
        for(Column col : columns) size += col.getSize();
        return size;
    }
    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        columns.forEach(col -> names.add(col.getName()));
        return names;
    }
}