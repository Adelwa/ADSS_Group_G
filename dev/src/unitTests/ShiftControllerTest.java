package unitTests;
import businessLayer.shiftPackage.Shift;
import businessLayer.shiftPackage.ShiftController;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class ShiftControllerTest {
    private Shift shift;
    ShiftController shifts;

    @BeforeEach
    void setUp() throws ParseException {

        shifts=new ShiftController();

    }

    @Test
    void createShift() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse("04/05/2022");
        shifts.createShift(4,date,"evening");
        Assert.assertEquals(1,shifts.getShifts().size());
    }

    @Test
    void updateShift1() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse("02/05/2022");
        shift=new Shift(2,date,"morning");
        ArrayList<Shift> shiftArrayList=new ArrayList<>();
        shift.setWorker("driver","123456789");
        shiftArrayList.add(shift);
        shifts.getShifts().put(date,shiftArrayList);
        try {
            shifts.updateShift(date,"morning","cashier","123456789");
        }catch (Exception e){
            Assert.assertEquals("Employee Is Already Assigned To a Job In This Shift",e.getMessage());
        }

    }

    @Test
    void updateShift2() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse("03/05/2022");
        try{
            shifts.updateShift(date,"morning","cashier","123456789");
        }catch (Exception e){
            Assert.assertEquals("There are no shifts with this specific date",e.getMessage());
        }
    }


    @Test
    void deleteShift1() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse("08/05/2022");

        try {
            shifts.deleteShift(date,"evening");
        }catch (Exception e){
            Assert.assertEquals("There are no shifts with this specific date",e.getMessage());
        }

    }
    @Test
    void deleteShift2() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse("12/05/2022");
        shift=new Shift(5,date,"morning");
        ArrayList<Shift> shiftArrayList=new ArrayList<>();
        shiftArrayList.add(shift);
        shifts.getShifts().put(date,shiftArrayList);
        try {
            shifts.deleteShift(date,"evening");
        }catch (Exception e){
            Assert.assertEquals("There is no shift with this specific shift time",e.getMessage());
        }

    }

}