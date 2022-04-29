package unitTests;
import static org.junit.jupiter.api.Assertions.*;

import businessLayer.employeePackage.Constraint;
import businessLayer.employeePackage.RegularEmployee;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class RegularEmployeeTest {


    private RegularEmployee emp;
    @BeforeEach
    void setUp() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse("27/03/1998");
        emp = new RegularEmployee("Adel", "123456789", 443322, 666, "Leomi", 8500, date, "cashier", "full time", false);
    }

    @org.junit.jupiter.api.Test
    void addConstraint(){
        emp.addConstraint(2,"evening","familyEvent");
        Assert.assertEquals(1,emp.getConstraints().size());

    }



    @org.junit.jupiter.api.Test
    void deleteConstraintTest1() {
        Assert.assertFalse(emp.deleteConstraint(4, "morning"));
    }

    @org.junit.jupiter.api.Test
    void deleteConstraintTest2() {
        Constraint constraint = new Constraint(1, "morning", "medicalIssue");
        emp.getConstraints().add(constraint);
        Assert.assertTrue(emp.deleteConstraint(1, "morning"));
    }



    @org.junit.jupiter.api.Test
    void updateConstraintReason() {
        Constraint constraint = new Constraint(5, "morning", "medicalIssue");
        emp.getConstraints().add(constraint);
        emp.updateConstraintReason(5,"morning","familyEvent");
        Assert.assertEquals("familyEvent", emp.getConstraints().get(0).getReason());
    }

    @org.junit.jupiter.api.Test
    void updateConstraintDay() {
        Constraint constraint = new Constraint(3, "morning", "familyEvent");
        emp.getConstraints().add(constraint);
        emp.updateConstraintDay(3,"morning",2,"evening");
        Assert.assertEquals(2, emp.getConstraints().get(0).getDay());
    }
}