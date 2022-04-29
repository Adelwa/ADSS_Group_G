package businessLayer.employeePackage;

import java.util.Date;

public abstract class Employee {
    private String name;
    private String id;
    private Bank account;
    private Contract contract;

    public Employee(String name, String id, Bank account, Contract contract){
        this.checkName(name);
        this.name = name;
        this.checkId(id);
        this.id = id;
        this.account = account;
        this.contract = contract;
    }

    public Employee(String name, String id, int accountNumber, int department, String bankName, int salary, Date startDate,
                                          String jobTitle, String jobType, boolean isShiftManager){
        this.name = name;
        this.id = id;
        this.account = new Bank(accountNumber, department, bankName);
        this.contract =  new Contract(salary, startDate, jobTitle, jobType);
    }

    public String printPersonalData(){
        String data = "";
        data = data + "--------------------Personal Information--------------------\n"+"Name: " + this.name + "\n" + "id: " + this.id +
                "\n" + "Bank Account: \n" + "    bank name: " + this.account.getBankName() + "\n" + "    account number: " + this.account.getAccountNumber() +
                "\n" + "    department: " + this.account.getDepartment() + "\n" + "Contract: \n" + "    Salary: " + this.contract.getSalary() + "\n" +
                "    start date: " + this.contract.getStartDate() + "\n" + "    job title: " + this.contract.getJobTitle() + "\n" + "    job type: " + this.contract.getJobType() +
                "\n";
        return data;
    }

    private void checkId(String id){
        if(id.length() == 9){
            for(int i = 0; i < id.length(); i++){
                if(id.charAt(i) < '0' || id.charAt(i) > '9')
                    throw new IllegalArgumentException("Illegal Id number");
            }
        }
        else{
            throw new IllegalArgumentException("Illegal Id number");
        }
    }

    private void checkName(String name){
        if(name.equals(""))
            throw new IllegalArgumentException("Illegal name");
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Bank getBank(){
        return this.account;
    }

    public Contract getContract(){
        return this.contract;
    }
}
