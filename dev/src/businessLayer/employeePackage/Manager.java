package businessLayer.employeePackage;

import java.util.Date;

public class Manager extends Employee {

    public Manager(String name, String id, Bank account, Contract contract) {
        super(name, id, account, contract);
    }

    public Manager(String name, String id, int accountNumber, int department, String bankName, int salary, Date startDate,
                    String jobTitle, String jobType, boolean isShiftManager){
        super(name, id, accountNumber, department, bankName, salary, startDate, jobTitle, jobType, isShiftManager);
    }

    public RegularEmployee createEmployee(String name, String id, int accountNumber, int department, String bankName, int salary, Date startDate,
                               String jobTitle, String jobType, boolean isShiftManager){
        Bank bank = new Bank(accountNumber, department, bankName);
        Contract contract = new Contract(salary, startDate, jobTitle, jobType);
        RegularEmployee newEmployee = new RegularEmployee(name, id, bank, contract, isShiftManager);
        return newEmployee;
    }

}
