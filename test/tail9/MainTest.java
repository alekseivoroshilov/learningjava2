package tail9;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @org.junit.jupiter.api.Test
    void main() {
        Main.main(new String[] {"-n", "2", "-o", "output.txt", "file1.txt", "file2.txt"});
        boolean equal = true;
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader
                    (new FileInputStream("output.txt"), "UTF-8"));
            BufferedReader test = new BufferedReader(new InputStreamReader
                    (new FileInputStream("outputtest.txt"), "UTF-8"));
            String a;
            String b;
            while ((a = in.readLine()) != null && (b = test.readLine()) != null) {
                System.out.println(a);
                System.out.println(b);
                if(!a.equals(b)) {equal = false; break;}
            }
            in.close();
            test.close();
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        assertTrue(equal);
    }
}