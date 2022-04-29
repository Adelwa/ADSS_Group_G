package presentationLayer;

import businessLayer.Fcade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {

    public void run(Fcade fcade) throws ParseException {

                //initWorkers
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date date = format.parse("27/03/1998");
                fcade.createEmployee("Adel", "123456789", 443322, 666, "Leomi", 8500, date, "cashier", "full time", false);
                date = format.parse("11/11/2011");
                fcade.createEmployee("Mary", "987654321", 112233, 777, "Hapoalim", 15640, date, "manager", "full time", false);

                date=format.parse("21/1/2020");
                fcade.createEmployee("Nadia","206469017",111666,712,"Hapoalim",7000,date,"storeKeeper","full time",true);
                date=format.parse("10/1/2020");
                fcade.createEmployee("Robert","111111111",111111,712,"Hapoalim",7000,date,"driver","full time",true);
                date=format.parse("30/6/2021");
                fcade.createEmployee("John","222222222",222222,712,"Hapoalim",7000,date,"shiftManager","full time",true);
                date=format.parse("1/1/2018");
                fcade.createEmployee("James","333333333",222666,712,"Hapoalim",7500,date,"general","full time",false);
                date=format.parse("31/5/2020");
                fcade.createEmployee("Sandra","444444444",111666,712,"hapoalim",7000,date,"coronaInspector","full time",false);

                //addConstraints

                fcade.login("123456789");
                fcade.addConstraint(1,"morning","medicalIssue");
                fcade.addConstraint(4,"evening","university");
                fcade.logout();
                fcade.login("206469017");
                fcade.addConstraint(3,"morning","religion");
                fcade.logout();
                fcade.login("123456789");
                fcade.addConstraint(1,"morning","religion");
                fcade.logout();
                //initShifts
                fcade.login("987654321");
                date=format.parse("1/5/2022");
                fcade.createShift(1,date,"morning");
                fcade.createShift(1,date,"evening");


                //addWorkersToShifts

                fcade.updateShift(date,"morning","manager","987654321");
                fcade.updateShift(date,"morning","storeKeeper","206469017");
                fcade.updateShift(date,"morning","driver","111111111");
                fcade.updateShift(date,"morning","shiftManager","222222222");
                fcade.updateShift(date,"morning","general","333333333");
                fcade.updateShift(date,"morning","coronaInspector","444444444");

                fcade.updateShift(date,"evening","cashier","123456789");
                fcade.updateShift(date,"evening","manager","987654321");
                fcade.updateShift(date,"evening","storeKeeper","206469017");
                fcade.updateShift(date,"evening","driver","111111111");
                fcade.updateShift(date,"evening","shiftManager","222222222");
                fcade.updateShift(date,"evening","general","333333333");
                fcade.updateShift(date,"evening","coronaInspector","444444444");
            }}
