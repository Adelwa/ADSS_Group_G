package businessLayer.employeePackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RegularEmployee extends Employee {
    private List<Constraint> constraints;
    private int days = 6;
    private int shiftsNumber = 2;
    private int[][] shifts;
    private boolean isShiftManager;

    public RegularEmployee(String name, String id, int accountNumber, int department, String bankName, int salary, Date startDate,
                    String jobTitle, String jobType, boolean isShiftManager){
        super(name, id, accountNumber, department, bankName, salary, startDate, jobTitle, jobType, isShiftManager);
        this.isShiftManager = isShiftManager;
        this.shifts = new int[days][shiftsNumber];
        shifts[0][0] = -1;      //not valuable
        shifts[0][1] = -1;      ////not valuable
        /*
            1 - sunday  2 - monday  3 - tuesday  4 - wednesday  5 - thursday
            default status is worker can work in every day/shift
        */

        for(int i = 1; i < shifts.length; i++){
            for(int j = 0; j < shifts[i].length; j++)
                shifts[i][j] = 1;
        }
        this.constraints = new ArrayList<Constraint>();
    }

    public RegularEmployee(String name, String id, Bank account, Contract contract, boolean isShiftManager) {
        super(name, id, account, contract);
        this.shifts = new int[this.days][this.shiftsNumber];
        this.isShiftManager = isShiftManager;
        shifts[0][0] = -1;      //not valuable
        shifts[0][1] = -1;      ////not valuable
        /*
            1 - sunday  2 - monday  3 - tuesday  4 - wednesday  5 - thursday
            default status is worker can work in every day/shift
        */

        for(int i = 1; i < shifts.length; i++){
            for(int j = 0; j < shifts[i].length; j++)
                shifts[i][j] = 1;
        }
        this.constraints = new ArrayList<Constraint>();
    }

    public String printConstraints(){
        String message = "\n";
        if(constraints.size() == 0)
            return "\nThere Is No Constraints To Print\n";
        Iterator<Constraint> iterator = constraints.iterator();
        while(iterator.hasNext())
            message = message + iterator.next().print();
        return message;
    }

    public void addConstraint(int day, String shift, String reason){
        Constraint newConstrain = new Constraint(day, shift, reason);
        this.constraints.add(newConstrain);
        int d = newConstrain.getDay();
        int s = 1;         //evening
        String tmp = newConstrain.getShift();
        if(tmp.equals("morning"))
            s = 0;
        this.shifts[d][s] = 0;      //can't work
    }

    public boolean deleteConstraint(int day, String shift){
        boolean result = false;
        int index = findConstraint(day, shift);
        if(index != -1){
            this.constraints.remove(index);
            int tmp = 1;
            if(shift.equals("morning"))
                tmp = 0;
            this.shifts[day][tmp] = 1;
            result = true;
        }
        return result;
    }

    public boolean updateConstraintReason(int day, String shift, String newReason){
        boolean result = false;
        int index = findConstraint(day, shift);
        if(index != -1){
            this.constraints.get(index).setReason(newReason);
            result = true;
        }
        return result;
    }

    public boolean updateConstraintDay(int oldDay, String oldShift, int newDay, String newShift){
        boolean result = false;
        int index = findConstraint(oldDay, oldShift);
        if(index != -1){
            this.constraints.get(index).setDay(newDay);
            this.constraints.get(index).setShift(newShift);
            int tmp = 1;
            if(newShift.equals("morning"))
                tmp = 0;
            this.shifts[newDay][tmp] = 0;
            if(oldShift.equals("morning"))
                tmp = 0;
            else
                tmp = 1;
            this.shifts[oldDay][tmp] = 1;
            result = true;
        }
        return result;
    }

    private int findConstraint(int day, String shift){
        for(Constraint c : this.constraints)
            if(c.getDay() == day && c.getShift().equals(shift))
                return this.constraints.indexOf(c);
        return -1;
    }

    public void setShiftManager(){
        this.isShiftManager = true;
    }

    public boolean getIsShiftManager(){
        return this.isShiftManager;
    }

    public boolean isAvailable(int day, int shift){
        return shifts[day][shift] == 1;
    }

    public List<Constraint> getConstraints(){
        return this.constraints;
    }
}
