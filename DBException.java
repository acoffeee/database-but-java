public class DBException extends RuntimeException {
    public DBException(String message) {
        super(message);
    }
    public static String BadNodeType = "Internal error. Wrong node type.";
    public static String InvalidNodeState = "State not Valid";
    public static String InvalidType = "Sorry, type is not valid";
    public static String NotImplemented = "Sorry, Thats not implemented yet";
}