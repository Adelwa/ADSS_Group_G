package businessLayer.employeePackage;

import businessLayer.shiftPackage.ShiftController;

import java.util.*;

public class EmployeeController {
    private Map<String, Employee> employees;
    private String loggedIn;           //Id

    private enum constraints {
        medicalIssue,
        religion,
        university,
        familyEvent
    }

    private enum jobTitles {
        cashier,
        storeKeeper,
        shiftManager,
        general,
        driver,
        coronaInspector,
        manager
    }

    public EmployeeController() {
        this.employees = new HashMap<String, Employee>();
        this.loggedIn = "";
    }

    public boolean login(String id) {
        if (employees.get(id) != null) {
            loggedIn = id;
            if (employees.get(id) instanceof Manager){
                //isManager= ((RegularEmployee) employees.get(id)).getisShiftManager() ;
                return true;
            }
        } else
            throw new IllegalArgumentException("Id is not found");
        return false;
    }

    public void logout() {
        loggedIn = "";
    }

    public String printPersonalData() {
        String message =  employees.get(loggedIn).printPersonalData();
        if(employees.get(loggedIn) instanceof RegularEmployee)
            message = message + "Shift manager: " + ((RegularEmployee) employees.get(loggedIn)).getIsShiftManager() + "\n";
        message = message + "---------------------------------------------------------------\n";
        return message;
    }

    public String printConstraints() {
        if(employees.get(loggedIn) instanceof RegularEmployee)
            return ((RegularEmployee) employees.get(loggedIn)).printConstraints();
        return "";
    }

    public void addConstraint(int day, String shift, String reason) {
        Date today = Calendar.getInstance().getTime();
        if (today.getDay() > 4)  //(0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday)
            throw new IllegalArgumentException("You don't have permission to add constraints in weekends");
        ((RegularEmployee) employees.get(loggedIn)).addConstraint(day, shift, reason);
    }

    public void deleteConstraint(int day, String shift) {
        if (!((RegularEmployee) employees.get(loggedIn)).deleteConstraint(day, shift))
            throw new IllegalArgumentException("You don't have a constraint with this specific day and shift time");
    }

    public void updateConstraintReason(int day, String shift, String newReason) {
        if (!((RegularEmployee) employees.get(loggedIn)).updateConstraintReason(day, shift, newReason))
            throw new IllegalArgumentException("You don't have a constraint with this specific day and shift time");
    }

    public void updateConstraintDay(int oldDay, String oldShift, int newDay, String newShift) {
        boolean result = ((RegularEmployee) employees.get(loggedIn)).updateConstraintDay(oldDay, oldShift, newDay, newShift) ;
        if(!result)
            throw new IllegalArgumentException("You don't have a constraint with this specific day and shift time");
    }

    public void createEmployee(String name, String id, int accountNumber, int department, String bankName, int salary, Date startDate,
                               String jobTitle, String jobType, boolean isShiftManager) {
        //Employee newEmployee = ((Manager) employees.get(loggedIn)).createEmployee(name, id, accountNumber, department, bankName, salary, startDate, jobTitle, jobType, isShiftManager);
        Employee newEmployee;
        if(jobTitle.equals(jobTitles.manager.toString()))
            newEmployee = new Manager(name, id, accountNumber, department, bankName, salary, startDate, jobTitle, jobType, isShiftManager);
        else
            newEmployee = new RegularEmployee(name, id, accountNumber, department, bankName, salary, startDate, jobTitle, jobType, isShiftManager);
        employees.put(id, newEmployee);
    }

    public void upgradeToShiftManager(String id){
        if(employees.get(id) instanceof RegularEmployee)
            ((RegularEmployee) employees.get(id)).setShiftManager();
    }

    public ArrayList<String> suggestEmployees(String jobTitle, int day, int shiftTime) {
        ArrayList<String> workers = new ArrayList<String>();
        for(String key : employees.keySet())
            if(employees.get(key).getContract().getJobTitle().equals(jobTitle))
                if(employees.get(key) instanceof RegularEmployee)
                    if(((RegularEmployee)employees.get(key)).isAvailable(day, shiftTime))
                        workers.add(employees.get(key).getId());
        return workers;
    }

    public String getEmployeesData(ArrayList<String> workers) {
        String message = "";
        int index = 1;
        for(String id : this.employees.keySet())
            if(workers.contains(id)){
                message = message + index + ")\n";
                message = message + "Name: " + employees.get(id).getName() + "\n";
                message = message + "Id: " + employees.get(id).getId() + "\n";
                message = message + "Job title: " + employees.get(id).getContract().getJobTitle() + "\n";
                message = message + "Job type: " + employees.get(id).getContract().getJobType();
                message = message + "\n";
            }
        message = message + "-------------------------------------------------------------------------\n";
        return message;
    }

    public String getConstraints() {
        int index = 1;
        String message = "";
        for(constraints constraint : constraints.values()){
            message = message + index + ") " + constraint.toString() + "\n";
            index++;
        }
        message = message + "\n";
        return message;
    }

    public void checkShiftAppointment(String jobTitle, String id, Date date, String shiftTime) {
        if(employees.get(id) == null)
            throw new IllegalArgumentException("Employee Does Not Exist");
        if(employees.get(id) instanceof RegularEmployee) {
            if (jobTitle.equals(jobTitles.shiftManager.toString())){
                if (!((RegularEmployee) employees.get(id)).getIsShiftManager()){
                    throw new IllegalArgumentException("Employee Is Not Competent For This Job Title");
                }
//                else if (!(jobTitle.equals(employees.get(id).getContract().getJobTitle()))) {
//                    throw new IllegalArgumentException("Employee Is Not Competent For This Job Title");
//                }
            }
            else{
                if(!((RegularEmployee) employees.get(id)).getContract().getJobTitle().equals(jobTitle))
                    throw new IllegalArgumentException("Employee Is Not Competent For This Job Title");
            }
        }else
            if(!(jobTitle.equals(jobTitles.manager.toString()))) {
                throw new IllegalArgumentException("Employee Is Not Competent For This Job Title");
            }
    }

    public String getUserConstrainOption(int choice) {
        if(constraints.values().length > choice)
            return constraints.values()[choice].toString();
        return "error";
    }

    public String getJobTitles() {
        int index = 1;
        String message = "Job Titles:\n";
        for(jobTitles jobTitle : jobTitles.values()){
            message = message + index + ") " + jobTitle.toString() + "\n";
            index++;
        }
        message = message + "\n";
        return message;
    }

    public String getUserJobTitleOption(int choice) {
        if(jobTitles.values().length > choice)
            return jobTitles.values()[choice].toString();
        return "error";
    }

    public String getPossibleEmployees(Date shiftDate, String shiftTime, String jobTitle) {
        String message = "";
        int day = shiftDate.getDay();
        day++;
        int time = 0;
        if(shiftTime.equals("evening"))
            time = 1;
        message = message + "Possible Workers:\n";
        for(String id : employees.keySet())
            if(employees.get(id).getContract().getJobTitle().equals(jobTitle)) {
                message = message + "Name: " + employees.get(id).getName() + "   " + "Id: " + id;
                if(employees.get(id) instanceof  RegularEmployee){
                    if(!((RegularEmployee) employees.get(id)).isAvailable(day, time))
                        message = message + "  *Has a Constraint*";
                }
                message = message + "\n";
            }
        if(jobTitle.equals("shiftManager"))
            for(String id : employees.keySet())
                if(employees.get(id) instanceof RegularEmployee)
                    if(((RegularEmployee) employees.get(id)).getIsShiftManager())
                        if(!employees.get(id).getContract().getJobTitle().equals(jobTitle)) {
                            message = message + "Name: " + employees.get(id).getName() + "   " + "Id: " + id;
                            if(!((RegularEmployee) employees.get(id)).isAvailable(day, time))
                                message = message + "  *Has a Constraint*";
                            message = message + "\n";
                        }
        return message;
    }

    public void updateEmployeeBankData(String empId, int newAccountNumber, int newDepartment, String newBankName) {
        Employee emp = employees.get(empId);
        if(emp != null){
            emp.getBank().setBankName(newBankName);
            emp.getBank().setAccountNumber(newAccountNumber);
            emp.getBank().setDepartment(newDepartment);
        }
        else
            throw new IllegalArgumentException("Employee Does Not Exists");
    }

    public void updateEmployeeContractData(String empId, int salary, String jobType) {
        Employee emp = employees.get(empId);
        if(emp != null){
            emp.getContract().setSalary(salary);
            emp.getContract().setJobType(jobType);
        }
        else
            throw new IllegalArgumentException("Employee Does Not Exists");
    }

    public String deleteEmployee(String id) {
        String jobTitle = "";
        Employee emp = employees.get(id);
        if(emp != null) {
            jobTitle = employees.get(id).getContract().getJobTitle();
            employees.remove(id);
        }
        else
            throw new IllegalArgumentException("Employee Does Not Exists");
        return jobTitle;
    }

    public boolean checkIfEmployeeHasConstraint(Date shiftDate, String shiftTime, String empId) {
        int day = shiftDate.getDay();
        day++;
        int time = 0;
        if(shiftTime.equals("evening"))
            time = 1;
        if(employees.get(empId) instanceof  RegularEmployee){
            return ((RegularEmployee) employees.get(empId)).isAvailable(day, time);
        }
        return false;
    }
}
