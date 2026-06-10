import java.lang.reflect.Type;
public class Column {
    private String name;
    private Type type;
    private int size; // in bytes, and max string size ill support is 255
    private int id;
    private boolean optional; // if optional
    public Column(String name, Type type, int size, int id, boolean optional) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.optional = optional;
    }
    public String getName() { return name; }
    public Type getType() { return type; }
    public int getSize() { return size; }
    public int getId() { return id; }
    public boolean isOptional() {return optional;} // if optional
    
}