import java.lang.reflect.Type;
import java.util.ArrayList;
import java.
public class Configuration {
    //protected lets us keep private but also do the inheritence thing i guess
    protected final int pageSize, keySize;
    protected final ArrayList<Type> keyTypes; // key types
    protected final ArrayList<Integer> keySizes; // size of keys bytes
    public Configuration(int pageSize,int keySize, ArrayList<Type> types, ArrayList<Integer> sizes) {
        this.pageSize = pageSize;
        this.keySize = keySize;
        this.keyTypes = types;
        this.keySizes = sizes;
    }
    public ArrayList<Type> getKeyTypes() { return keyTypes; }
    public ArrayList<Integer> getKeySizes() { return keySizes; }
    public int getKeySize() { return keySize; }
    public int getPageSize() { return pageSize; }
    public boolean compare(ArrayList<Object> keyOne, ArrayList<Object> keyTwo, int truthyValue, boolean valueIfEqual) {
        for(int i = 0; i < keyTypes.length(); i++) {
            //finds type of current column in the key, then casts it.
            Type type = keyTypes.get(i);
            Object keyOne = keyOne.get(i);
            Object keyTwo = keyTwo.get(i);
            int result;
            //compares. returns 1 if first key is larger, -1 if second, 0 if equal
            if(type == Integer.class) result = Integer.compare((Integer)keyOne, (Integer)keyTwo);
            else if(type == Float.class)result =  Float.compare((Float)keyOne, (Float)keyTwo);
            else if(type == Double.class)result = Double.compare((Double)keyOne, (Double)KeyTwo);
            else if(type == Long.class)result = Long.compare((Long)keyOne, (Long)KeyTwo);
            else if(type == String.class)result = ((String)keyOne).compareTo((String)keyTwo);
            //if equal, then we can check other columns in the key
            if(result == 0) continue;
            //return result according to what is true 
            return truthyValue == result;
        }
        //if all parts in the key are equal, what is the result?
        return valueIfEqual;
        
    }
    //greater than >
    public boolean gt(ArrayList<Object> keyOne, ArrayList<Object> keyTwo) {
        return compare(keyOne, keyTwo, 1, false);
    }
    //less than <
    public boolean lt(ArrayList<Object> keyOne, ArrayList<Object> keyTwo) {
        return compare(keyOne, keyTwo, -1, false);
    }
    //greater or equal >=
    public boolean ge(ArrayList<Object> keyOne, ArrayList<Object> keyTwo) {
        return compare(keyOne, keyTwo, 1, true);
    }
    //less or equal <=
    public boolean lt(ArrayList<Object> keyOne, ArrayList<Object> keyTwo) {
        return compare(keyOne, keyTwo, -1, true);
    }
    //equal ==
    public boolean eq(ArrayList<Object> keyOne, ArrayList<Object> keyTwo) {
        return compare(keyOne, keyTwo, 0, true);
    }
    //not equal
    public boolean neq(ArrayList<Object> keyOne, ArrayList<Object> keyTwo) {
        return !eq(keyOne, keyTwo);
    }
}