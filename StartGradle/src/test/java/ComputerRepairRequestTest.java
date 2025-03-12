import model.ComputerRepairRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ComputerRepairRequestTest {
    @Test
    @DisplayName("First test")
    public void testExample(){
        ComputerRepairRequest crr = new ComputerRepairRequest();
        assertEquals("", crr.getOwnerName());
        assertEquals("", crr.getOwnerAddress());

        ComputerRepairRequest crr2 = new ComputerRepairRequest();
        crr2.setOwnerName("John Doe");
        crr2.setOwnerAddress("Bucharest");
        assertEquals("John Doe", crr2.getOwnerName());
        assertEquals("Bucharest", crr2.getOwnerAddress());
    }

    @Test
    @DisplayName("Test Exemplu")
    public void testExample2(){
        assertEquals(2, 2, "Numerele ar trebui sa fie egale!");
    }
}
