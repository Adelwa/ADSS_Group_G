package businessLayer.shiftPackage;

import businessLayer.employeePackage.EmployeeController;

import java.util.*;

public class ShiftController {

    private enum Days{
        sunday,
        monday,
        tuesday,
        wednesday,
        thursday
    }
    private enum jobTitles{
        cashier,
        storeKeeper,
        shiftManager,
        general,
        driver,
        coronaInspector
    }

    private enum sTime{
        morning,
        evening
    }

    private Map<Date, ArrayList<Shift>> shifts;
    private Map<String, Integer>[][] shiftsStructure;
    /*
        0 - morning
        1 - evening
    */



    public ShiftController(){
        this.shifts = new HashMap<Date, ArrayList<Shift>>();
        this.shiftsStructure = new Map[6][2];
        for(int i = 0; i < this.shiftsStructure.length; i++)
            for(int j = 0; j < this.shiftsStructure[i].length; j++) {
                this.shiftsStructure[i][j] = new HashMap<String, Integer>();
                for(jobTitles job : jobTitles.values())
                    this.shiftsStructure[i][j].put(job.toString(), 1);
            }
    }

    public void createShift(int day, Date date, String shift){
        ArrayList<Shift> arr = this.shifts.get(date);
        if(arr != null){
            for(Shift s : arr)
                if(s.getShift().equals(shift))
                    throw new IllegalArgumentException("A shift in the same day and shift time is already created");
            this.shifts.get(date).add(new Shift(day, date, shift));
        }
        else{
            this.shifts.put(date, new ArrayList<Shift>());
            this.shifts.get(date).add(new Shift(day, date, shift));
        }
    }

    public void updateShift(Date shiftDate, String time, String jobTitle, String id){
        boolean found = false;
        ArrayList<Shift> arr = this.shifts.get(shiftDate);
        if(arr == null)
            throw new IllegalArgumentException("There are no shifts with this specific date");
        for(Shift s : arr)
            if(s.getShift().equals(time)) {
                for(String job : s.getWorkers().keySet())
                    if(s.getWorkers().get(job).contains(id))
                        throw new IllegalArgumentException("Employee Is Already Assigned To a Job In This Shift");
                assingEmployee(s, jobTitle, id);
                found = true;
            }
        if(!found)
            throw new IllegalArgumentException("There is no shift with this specific shift time");
    }

    private void assingEmployee(Shift s, String jobTitle, String id) {
        s.setWorker(jobTitle,id);
    }

    public void deleteShift(Date date, String shift){
        boolean found = false;
        ArrayList<Shift> arr = this.shifts.get(date);
        if(arr == null)
            throw new IllegalArgumentException("There are no shifts with this specific date");
        for(Shift s : arr)
            if(s.getShift().equals(shift)) {
                found = true;
                shifts.get(date).remove(s);
                break;
            }
        if(!found)
            throw new IllegalArgumentException("There is no shift with this specific shift time");
    }

    public String printShift(Date date, String shift){
        boolean found = false;
        String message = "";
        ArrayList<Shift> arr = this.shifts.get(date);
        if(arr == null) {
            message = "There is no shift with specified date: " + date;
            throw new IllegalArgumentException(message);
        }
        else{
            for(Shift s : arr)
                if(s.getShift().equals(shift)){
                    found = true;
                    message = s.printShift(Days.values()[s.getDay() - 1].toString());
                }
            if(!found) {
                message = "There is no shift with specified shift time: " + shift;
                throw new IllegalArgumentException(message);
            }
        }
        return message;
    }

    public void deleteEmployeeFromShift(String id, String jobTitle, Date date, String shiftTime){
        boolean foundShift = false;
        boolean foundWorker = false;
        String message = "";
        ArrayList<Shift> arr = this.shifts.get(date);
        if(arr == null)
            throw new IllegalArgumentException("There are no shifts with this specific date");
        for(Shift s : arr)
            if(s.getShift().equals(shiftTime)) {
                foundShift = true;
                if(s.getWorkers().get(jobTitle).contains(id)) {
                    s.getWorkers().get(jobTitle).remove(id);
                    foundWorker = true;
                }
            }
        if(!foundShift)
            throw new IllegalArgumentException("There is no shift with this specific shift time");
        if(!foundWorker)
            message = "Worker: " + id + " is not assigned to this shift time";
            throw new IllegalArgumentException(message);
    }

    public String printEmployeeShiftsForWeek(String id){
        String message = "Shifts:\n";
        Date today = null;
        Calendar calendar;
        for(int i = 0; i < 7; i++){
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            message = message + printShiftForEmployee(today,id);
        }
        message = message + "\n-------------------------------------------------------------------------\n";
        return message;
    }
    public String printEmployeeShiftsForMonth(String id){
        String message = "Shifts:\n";
        Date today = null;
        Calendar calendar;
        for(int i = 0; i < 31; i++){
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            message = message + printShiftForEmployee(today,id);
        }
        message = message + "\n-------------------------------------------------------------------------\n";
        return message;
    }

    private String printShiftForEmployee(Date date, String id){ //+2 point ;)
        ArrayList<Shift> arr = this.shifts.get(date);
        String message = "" + date + "\n";
        int i = 1;
        boolean found = false;
        if(arr != null){
            for(Shift s : arr)
                for(String job : s.getWorkers().keySet())
                    if(s.getWorkers().get(job).contains(id)){
                        found = true;
                        message = message + i + ")\n" + s.printShift(Days.values()[s.getDay() - 1].toString()) + "\n";
                        message = message + "*****************************************************************\n";
                        i++;
                    }
            if(!found)
                message = message + "There is no shifts to show\n";
        }
        else
            message = message + "There is no shifts to show\n";
        return message;
    }

    public void updateShiftStructure(String jobTitle, int amount, int day, String shiftTime){ //shiftTime == 0? 'morning' : 'evening'
        int time = 0;
        for(sTime s : sTime.values())
            if(s.toString().equals(shiftTime))
                break;
            else
                time++;
        this.shiftsStructure[day][time].put(jobTitle, amount);
    }

    public String printShiftsStructure(){
        String message = "";
        for(int i = 1; i < this.shiftsStructure.length; i++)
            for(int j = 0; j < this.shiftsStructure[i].length; j++){
                message = message + "Day: " + Days.values()[i-1] + "\nShift: " + sTime.values()[j] + "\n";
                message = message + "   "+this.shiftsStructure[i][j].toString()+"\n" ;
                message = message + "-------------------------------------------------------------------------\n";
            }
        return message;
    }

    public String printIncompleteShifts() {
        String message = "Incomplete Shifts:\n";
        Date today = null;
        Calendar calendar;
        for(int i = 0; i < 7; i++) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            message = message + today.toString() + ":\n";
            message = message + checkIncompleteShift(today);
            message = message + "-------------------------------------------------------------------------\n";
        }
        return message;
    }

    private String checkIncompleteShift(Date date){
        String message = "";
        int entered = 0;
        ArrayList<Shift> shifts = this.shifts.get(date);
        int time = 0;
        if(shifts != null){
            if(shifts.size() == 0){
                message = "  There is no shifts\n";
            }
            else{
                for(Shift s : shifts) {
                    entered = 0;
                    message = message + "   Shift Time: " + s.getShift() + "\n";
                    if (s.getShift().equals("evening")) time = 1;
                    for (String jobTitle : shiftsStructure[s.getDay()][time].keySet()) {
                        if(s.getWorkers().get(jobTitle) != null) {
                            if (s.getWorkers().get(jobTitle).size() <  shiftsStructure[s.getDay()][time].get(jobTitle)){
                                entered++;
                                int lack = shiftsStructure[s.getDay()][time].get(jobTitle) - s.getWorkers().get(jobTitle).size();
                                message = message + "      job title: " + jobTitle + "\n";
                                message = message + "      Lack: " + lack + " workers\n";
                                message = message + "\n";
                            }
                        }
                        else{
                            entered++;
                            message = message + "      job title: " + jobTitle + "\n";
                            message = message + "      Lack: " + shiftsStructure[s.getDay()][time].get(jobTitle) + " workers\n";
                            message = message + "\n";
                        }
                    }
                    if(entered == 0)
                        message = message + "      There Is No Lack Of Employees\n";
                }
            }
        }
        else
            message = "  There is no shifts\n";
        return message;
    }


    public Boolean checkIncompleteShifts() {
        boolean isIncomplete = false;
        Date today = null;
        Calendar calendar;
        for(int i = 0; i < 7; i++) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            today = calendar.getTime();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, i);
            today = calendar.getTime();
            isIncomplete = checkIncomplete(today);
            if(isIncomplete)
                break;
        }
        return isIncomplete;
    }

    private boolean checkIncomplete(Date date) {
        boolean isIncomplete = false;
        ArrayList<Shift> shifts = this.shifts.get(date);
        int time = 0;
        if(shifts != null){
            for(Shift s : shifts) {
                if (s.getShift().equals("evening")) time = 1;
                for (String jobTitle : shiftsStructure[s.getDay()][time].keySet()) {
                    if(s.getWorkers().get(jobTitle) != null){
                        if (s.getWorkers().get(jobTitle).size() <  shiftsStructure[s.getDay()][time].get(jobTitle)){
                            isIncomplete = true;
                            break;
                        }
                    }
                    else {
                        isIncomplete = true;
                        break;
                    }
                }
            }
        }
        return isIncomplete;
    }

    public ArrayList<String> filterEmployees(ArrayList<String> employees, int day, int shiftTime, String jobTitle) {
        Date today = Calendar.getInstance().getTime();
        Calendar calendar;
        int currentDay = today.getDay();
        currentDay++;
        int toAdd = day - currentDay;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, toAdd);
        today = calendar.getTime();
        calendar.setTime(today);
        today = calendar.getTime();
        ArrayList<Shift> shifts = this.shifts.get(today);
        if(shifts != null){
            Shift shift = shifts.get(shiftTime);
            if (shift != null){
                if(shift.getWorkers() != null){
                    if(shift.getWorkers().containsKey(jobTitle)){
                        for(String id : shift.getWorkers().get(jobTitle)){
                            employees.remove(id);
                        }
                    }
                }

            }
        }
        return employees;
    }

    public String getWorkingDays() {
        int index = 1;
        String message = "";
        for(Days day : Days.values()){
            message = message + index + ") " + day.toString() + "\n";
            index++;
        }
        message = message + "\n";
        return message;
    }

    public String getShifTime() {
        int index = 1;
        String message = "";
        for(sTime time : sTime.values()){
            message = message + index + ") " + time.toString() + "\n";
            index++;
        }
        message = message + "\n";
        return message;
    }

    public String getUserShiftTimeOption(int choice) {
        if(sTime.values().length > choice)
            return sTime.values()[choice].toString();
        return "error";
    }

    public int getUserDayOption(int choice) {
        int result = 0;
        switch (choice){
            case 1:
                result = 1;
                break;
            case 2:
                result = 2;
                break;
            case 3:
                result = 3;
                break;
            case 4:
                result = 4;
                break;
            case 5:
                result = 5;
                break;
            default:
                result = -1;
                break;
        }
        return result;
    }

    public void checkWeekend(int day) {
        if(day > Days.values().length)
            throw new IllegalArgumentException("You Can't Create Shifts on Weekends");
    }

    public void deleteEmployeeFromShifts(String id, String jobTitle) {
        Date today = null;
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        today = calendar.getTime();
        for(Date date : shifts.keySet()){
            if(date.after(today)){
                if(shifts.get(date).size() > 0){
                    for(Shift s : shifts.get(date)){
                       if( s.getWorkers().get(jobTitle) != null)
                           s.getWorkers().get(jobTitle).remove(id);
                    }
                }
            }
        }
    }

    public Map<Date, ArrayList<Shift>> getShifts(){
        return this.shifts;
    }
}

