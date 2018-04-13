package tail9;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @org.junit.jupiter.api.Test
    void main() {
        Main.main(new String[] {"-n", "2", "-o", "output.txt", "file1.txt"});
        // допишу тест очень скоро. до этого долго разбирался.
    }

}