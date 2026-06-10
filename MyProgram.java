import java.nio.MappedByteBuffer;
import java.io.IOException;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
public class MyProgram
{
    public static void main(String[] args)
    {
        try {
            Pager pager = new Pager("data.bin");
            String[] names = {"bob", "carry", "melissia", "etc"};
            int i = 1;
            for(String string : names) {
                pager.writePage(i, string.getBytes(StandardCharsets.UTF_8));
                i++;
            }
            for(int j = 1; j <i; j++) {
                System.out.println("new person __________");
                System.out.println(new String(pager.getPage(j), StandardCharsets.UTF_8));
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}