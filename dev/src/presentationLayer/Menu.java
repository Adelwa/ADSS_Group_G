package presentationLayer;
import businessLayer.Fcade;
import businessLayer.Response;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private static Menu instance = null;
    private Fcade fcade;
    private boolean loggedIn;
    private String id;
    private boolean isManager;
    private Handler handler;
    private Data init;

    private Menu() throws ParseException {
        scanner = new Scanner(System.in);
        fcade = new Fcade();
        loggedIn = false;
        id = "";
        isManager = false;
        handler = new Handler(scanner);
        init = new Data();
        init.run(fcade);
    }

    public static Menu getInstance() throws ParseException {
        if(instance == null)
            instance = new Menu();
        return instance;
    }


    //-------------------------------------------ENUM GETTERS----------------------------------------------------
    private String getReason() {
        Response<String> response;
        int choiceNumber = 0;
        String choice = "error";
        while(choice.equals("error")){
            try{
                response = fcade.getConstraints();
                System.out.print(response.getValue());
                System.out.print("Enter Your Choice Number: ");
                choiceNumber = scanner.nextInt();
                response = fcade.getUserConstrainOption(choiceNumber - 1);
                choice = response.getValue();
            }catch (Exception e){
                System.out.println("Please Enter a legit Number Only\n");
                scanner.nextLine();
            }
        }
        return choice;
    }

    private String getShiftTime() {
        Response<String> response;
        int choiceNumber = 0;
        String choice = "error";
        while(choice.equals("error")){
            try{
                response = fcade.getShiftTime();
                System.out.print(response.getValue());
                System.out.print("Enter Your Choice Number: ");
                choiceNumber = scanner.nextInt();
                response = fcade.getUserShiftTimeOption(choiceNumber - 1);
                choice = response.getValue();
            }catch (Exception e){
                System.out.println("Please Enter a legit Number Only\n");
                scanner.nextLine();
            }
        }
        return choice;
    }

    private int getShiftTime2() {
        Response<String> response = fcade.getShiftTime();
        System.out.println(response.getValue());
        System.out.println("Enter Your Choice Number: ");
        return scanner.nextInt();
    }

    private int getDay() {
        Response<String> response1;
        Response<Integer> response2;
        int choiceNumber = 0;
        int choice = -1;
        while(choice == -1){
            try{
                response1 = fcade.getWorkingDays();
                System.out.print(response1.getValue());
                System.out.print("Enter Your Choice Number: ");
                choiceNumber = scanner.nextInt();
                response2 = fcade.getUserDayOption(choiceNumber);
                choice = response2.getValue();
            }catch (Exception e){
                System.out.println("Please Enter a legit Number Only\n");
                scanner.nextLine();
            }
        }
        return choice;
    }

    private String getJobTitle() {
        Response<String> response;
        int choiceNumber = 0;
        String choice = "error";
        while(choice.equals("error")){
            response = fcade.getJobTitles();
            System.out.print(response.getValue());
            System.out.print("Enter Your Choice Number: ");
            choiceNumber = scanner.nextInt();
            response = fcade.getUserJobTitleOption(choiceNumber - 1);
            choice = response.getValue();
        }
        return choice;
    }
    //-------------------------------------------END OF ENUM GETTERS---------------------------------------------


    //-------------------------------------------ID & Number MANIPULATION-------------------------------------------------
    private String getUserId() {
        System.out.print("Please enter your id or -1 to exit\nID: ");
        int inputValue = scanner.nextInt();
        String input = String.valueOf(inputValue);
        while(!checkId(input)){
            System.out.print("Please enter a valid id or -1 to exit\nID: ");
            inputValue = scanner.nextInt();
            input = String.valueOf(inputValue);
        }
        return input;
    }

    private boolean checkId(String input) {
        boolean result = true;
        if(input.length() == 2 && input.equals("-1"))
            return result;
        if(input.length() == 9)
            for(int i = 0; i < input.length(); i++){
                if(input.charAt(i) < '0' || input.charAt(i) > '9') {
                    result = false;
                    break;
                }
            }
        else
            result = false;
        return result;
    }
    //-------------------------------------------END OF ID MANIPULATION------------------------------------------

    //-------------------------------------------MENUS-----------------------------------------------------------
    private int welcomeMenu() {
        System.out.println("****************************************");
        System.out.println("WELCOME      TO         SUPER        LEE");
        System.out.println("****************************************");
        System.out.println("1) Login");
        System.out.println("2) Exit");
        System.out.print("Please choose an option: ");
        return scanner.nextInt();
    }

    private void printManagerMenu() {
        System.out.println("\n*****************");
        System.out.println("MANAGER      MENU");
        System.out.println("*****************");
        System.out.println("1) Show My Personal Information");
        System.out.println("2) Create Employee");
        System.out.println("3) Upgrade Employee To Shift Manager");
        System.out.println("4) Create New Shift");
        System.out.println("5) Update Shift");
        System.out.println("6) Delete Shift");
        System.out.println("7) Print Shift");
        System.out.println("8) Cancel Shift Appointment For Employee");
        System.out.println("9) Print Shifts Structure");
        System.out.println("10) Update Shifts Structure");
        System.out.println("11) Update Employee Bank Data");
        System.out.println("12) Update Employee Contract Data");
        System.out.println("13) Delete Employee");
        System.out.println("14) Logout");
        System.out.print("Please Choose an Option: ");
    }

    private void printRegularMenu() {
        System.out.println("**********************************");
        System.out.println("REGULAR      EMPLOYEE         MENU");
        System.out.println("**********************************");
        System.out.println("1) Show My Personal Information");
        System.out.println("2) Print Constraints");
        System.out.println("3) Add Constraint");
        System.out.println("4) Delete Constraint");
        System.out.println("5) Update Constraint's Reason");
        System.out.println("6) Update Constraint's Day");
        System.out.println("7) Show Weekly Shifts");
        System.out.println("8) Show Monthly Shifts");
        System.out.println("9) Logout");
        System.out.print("Please Choose an Option: ");
    }

    private void regularEmployeeMenu() {
        int option;
        boolean exit = false;
        Response<?> response;
        int day = 0;
        String shiftTime = "";
        String reason = "";
        while(!exit){
            printRegularMenu();
            option = scanner.nextInt();
            switch (option){
                case 1:
                    response = fcade.printPersonalData();
                    System.out.println(response.getValue());
                    break;
                case 2:
                    response = fcade.printConstraints();
                    System.out.println(response.getValue());
                    break;
                case 3:
                    System.out.println("\nPlease Choose a Working Day:");
                    day = getDay();
                    System.out.println("Please Choose a Shift Time:");
                    shiftTime = getShiftTime();
                    System.out.println("\nPlease Choose a Constraint:");
                    reason = getReason();
                    response = fcade.addConstraint(day, shiftTime, reason);
                    System.out.println(response.getMessage());
                    break;
                case 4:
                    System.out.println("Please Choose a Working Day:");
                    day = getDay();
                    System.out.println("Please Choose a Shift Time:");
                    shiftTime = getShiftTime();
                    response = fcade.deleteConstraint(day, shiftTime);
                    System.out.println(response.getMessage());
                    break;
                case 5:
                    System.out.println("Please Choose a Working Day:");
                    day = getDay();
                    System.out.println("Please Choose a Shift Time:");
                    shiftTime = getShiftTime();
                    System.out.println("Please Choose a New Reason:");
                    String newReason = getReason();
                    response = fcade.updateConstraintReason(day, shiftTime, newReason);
                    System.out.println(response.getMessage());
                    break;
                case 6:
                    System.out.println("Please Choose a Working Day:");
                    day = getDay();
                    System.out.println("Please Choose a Shift Time:");
                    shiftTime = getShiftTime();
                    System.out.println("Please Choose a New Day:");
                    int newDay = getDay();
                    System.out.println("Please Choose a New Shift Time:");
                    String newShiftTime = getShiftTime();
                    response = fcade.updateConstraintDay(day, shiftTime, newDay, newShiftTime);
                    System.out.println(response.getMessage());
                    break;
                case 7:
                    response = fcade.printEmployeeShiftsForWeek(id);
                    if(response.isError())
                        System.out.println(response.getMessage());
                    else
                        System.out.println(response.getValue());
                    break;
                case 8:
                    response = fcade.printEmployeeShiftsForMonth(id);
                    if(response.isError())
                        System.out.println(response.getMessage());
                    else
                        System.out.println(response.getValue());
                    break;
                case 9:
                    loggedIn = false;
                    id = "";
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid Argument, Try Again");
            }
            System.out.print("\n");
        }
    }

    private void managerMenu() throws ParseException {
        int option;
        boolean exit = false;
        Response<?> response;
        int day = 0;
        String shiftTime = "";
        String reason = "";
        Date shiftDate = null;
        String name = "";
        String empId = "";
        String bankName = "";
        String jobTitle = "";
        String jobType = "";
        int salary = 0;
        while(!exit){
            printManagerMenu();
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option){
                case 1:
                    response = fcade.printPersonalData();
                    System.out.println(response.getValue());
                    break;
                case 2:
                    System.out.println("\n********PERSONAL INFORMATION********");
                    name = handler.inputHandler1("name");
                    empId = handler.inputHandler1("id");
                    while(!checkId(empId)){
                        empId = handler.inputHandler1("id");
                    }
                    jobTitle = getJobTitle();
                    scanner.nextLine();
                    jobType = handler.inputHandler1("jobType");
                    System.out.println("\n********BANK INFORMATION********");
                    bankName = handler.inputHandler1("bankName");
                    int accountNumber = handler.inputHandler2("accountNumber");
                    int department = handler.inputHandler2("department");
                    salary = handler.inputHandler2("salary");
                    Date today = Calendar.getInstance().getTime();
                    boolean isShiftManager =  false;
                    if(jobTitle.equals("shiftManager"))
                        isShiftManager = true;
                    else
                        isShiftManager = handler.inputHandler3("isShiftManager");
                    response = fcade.createEmployee(name, empId, accountNumber, department, bankName, salary, today, jobTitle, jobType, isShiftManager);
                    System.out.println(response.getMessage());
                    break;
                case 3:
                    empId = handler.inputHandler1("id");
                    while(!checkId(empId)){
                        empId = handler.inputHandler1("id");
                    }
                    response = fcade.upgradeToShiftManager(empId);
                    System.out.println(response.getMessage());
                    break;
                case 4:
                    Date date = handler.dateHandler(true);
                    day = date.getDay();
                    day++;
                    //day = getDay();
                    shiftTime = getShiftTime();
                    scanner.nextLine();
                    response = fcade.createShift(day, date, shiftTime);
                    System.out.println(response.getMessage());
                    break;
                case 5:
                    shiftDate = handler.dateHandler(true);
                    shiftTime = getShiftTime();
                    jobTitle = getJobTitle();
                    //POSSIBLE WORKERS:
                    response = fcade.getPossibleEmployees(shiftDate, shiftTime, jobTitle);
                    System.out.print(response.getValue());
                    scanner.nextLine();
                    empId = handler.inputHandler1("id");
                    while(!checkId(empId)){
                        empId = handler.inputHandler1("id");
                    }
                    response = fcade.checkIfEmployeeHasConstraint(shiftDate, shiftTime, empId); // true = available
                    if(!((Boolean)response.getValue())){
                        System.out.println("Employee Has a Constraint In The Same Day, Do You Want TO Proceed? Yes/No");
                        String answer = scanner.nextLine();
                        if(answer.toLowerCase(Locale.ROOT).equals("no"))
                            break;
                    }
                    response = fcade.updateShift(shiftDate, shiftTime, jobTitle, empId);
                    System.out.println(response.getMessage());
                    break;
                case 6:
                    shiftDate = handler.dateHandler(false);
                    shiftTime = getShiftTime();
                    response = fcade.deleteShift(shiftDate, shiftTime);
                    System.out.println(response.getMessage());
                    break;
                case 7:
                    shiftDate = handler.dateHandler(false);
                    shiftTime = getShiftTime();
                    response = fcade.printShift(shiftDate, shiftTime);
                    if(response.isError())
                        System.out.println(response.getMessage());
                    else
                        System.out.println(response.getValue());
                    break;
                case 8:
                    empId = handler.inputHandler1("id");
                    while(!checkId(empId)){
                        empId = handler.inputHandler1("id");
                    }
                    jobTitle = getJobTitle();
                    scanner.nextLine();
                    shiftDate = handler.dateHandler(true);
                    shiftTime =getShiftTime();
                    response = fcade.deleteEmployeeFromShift(empId, jobTitle, shiftDate, shiftTime);
                    System.out.println(response.getMessage());
                    break;
                case 9:
                    response = fcade.printShiftsStructure();
                    System.out.println(response.getValue());
                    break;
                case 10:
                    jobTitle = getJobTitle();
                    scanner.nextLine();
                    int amonut = handler.inputHandler2("amount");
                    day = getDay();
                    shiftTime = getShiftTime();
                    response = fcade.updateShiftStructure(jobTitle, amonut, day, shiftTime);
                    System.out.println(response.getMessage());
                    break;
                case 11:
                    empId = handler.inputHandler1("id");
                    while(!checkId(empId)){
                        empId = handler.inputHandler1("id");
                    }
                    int newAccountNumber = handler.inputHandler2("accountNumber");
                    int newDepartment = handler.inputHandler2("department");
                    bankName = handler.inputHandler1("bankName");
                    response = fcade.updateEmployeeBankData(empId, newAccountNumber, newDepartment, bankName);
                    System.out.println(response.getMessage());
                    break;
                case 12:
                    empId = handler.inputHandler1("id");
                    while(!checkId(empId)){
                        empId = handler.inputHandler1("id");
                    }
                    salary = handler.inputHandler2("salary");
                    jobType = handler.inputHandler1("jobType");
                    response = fcade.updateEmployeeContractData(empId, salary, jobType);
                    System.out.println(response.getMessage());
                    break;
                case 13:
                    empId = handler.inputHandler1("id");
                    while(!checkId(empId)){
                        empId = handler.inputHandler1("id");
                    }
                    response = fcade.deleteEmployee(empId);
                    System.out.println(response.getMessage());
                    break;
                case 14:
                    loggedIn = false;
                    isManager = false;
                    id = "";
                    exit = true;
                    break;
            }
            System.out.print("\n");
        }
    }
    //-------------------------------------------END OF MENUS-----------------------------------------------------------


    public void run() throws ParseException {
        String inputId = "";
        String input = "";
        boolean done = false;
        Response<String> stringResponse;
        Response<Boolean> booleanResponse;
        while(true){
            if(!loggedIn) {
                int option = welcomeMenu();
                if (option == 1) {
                    inputId = getUserId();
                    if(inputId.equals("-1"))
                        option = 2;
                    else {
                        stringResponse =  fcade.login(inputId);
                        if(stringResponse.isError()) {
                            System.out.println(stringResponse.getMessage());
                        }
                        else {
                            if (stringResponse.getValue().equals("manager")) {
                                isManager = true;
                                booleanResponse = fcade.checkIncompleteShifts();
                                if(booleanResponse.getValue()){
                                    stringResponse = fcade.printIncompleteShifts();
                                    System.out.println(stringResponse.getValue());
                                    System.out.println("Do You Need a Suggestion For an Employee? yes/no");
                                    scanner.nextLine();
                                    while(!done){
                                        input = scanner.nextLine();
                                        if(input.toLowerCase(Locale.ROOT).equals("yes")) {
                                            done = true;
                                            String jobTitle = getJobTitle();
                                            int day = getDay();
                                            int shiftTime = getShiftTime2();
                                            stringResponse = fcade.suggestEmployees(jobTitle, day, shiftTime);
                                            System.out.println(stringResponse.getValue());
                                       }
                                        else if(input.toLowerCase(Locale.ROOT).equals("no"))
                                            done =  true;
                                        else
                                            System.out.println("Please Enter Yes or No\n Your Choice: ");
                                    }
                                }
                            }
                            loggedIn = true;
                            id = inputId;
                        }
                    }
                }
                if(option == 2)
                    break;
            }
            if(loggedIn && isManager){
                managerMenu();
            }
            if(loggedIn && !isManager){
                regularEmployeeMenu();
            }
            System.out.print("\n");
        }
        System.out.println("**** See You Later :) ****");
    }
}
