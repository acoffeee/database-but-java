import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.io.IOException;
public class Pager {
    private final int PAGE_SIZE = 4096;
    private final RandomAccessFile RAF;
    public Pager(String fileName) throws IOException
    {
        RAF = new RandomAccessFile(fileName, "rw");
    }
    public byte[] getPage(int pageNum) throws IOException {
        //if page is not dirty, dirty=changed, so if one thread has the object but another thread changed it it would flip the object as dirty.
        //if(useCache && !(cache.getPage(pageNum).isDirty())) return cache.getPageAsByteArr(pageNum); 
        byte[] buffer = new byte[PAGE_SIZE];
        RAF.seek(PAGE_SIZE * pageNum);
        RAF.read(buffer);
        return buffer;
    }
    public boolean writePage(int pageIndex, Node node) throws IOException {
        byte[]data;
        if(node.isLeafNode()) {
            data = desereilize(node);
        }
        //if(useCache) cache.add(pageIndex, node);
        RAF.seek(pageNum * PAGE_SIZE);
        RAF.write(data);
        return true;
    }
    
}