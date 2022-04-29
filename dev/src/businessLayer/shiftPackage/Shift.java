package businessLayer.shiftPackage;

import java.util.*;

public class Shift {
    private final int days = 6;
    private int day;
    private Date date;
    private String shift;   //morning or evening
    private Map<String, ArrayList<String>> workers;    //<jobTitle -> {workersId}

    public Shift(int day, Date date, String shift){
        this.day = day;
        this.checkDate(date);
        this.date = date;
        this.shift = shift;
        this.workers = new HashMap<String, ArrayList<String>>();
    }

    public String printShift(String day){
        String message = "";
        message = "Shift Details: \n" + "    Day: " + day + "\n" + "    Date: " + this.date + "\n" + "    Shift: " + this.shift + "\n" +
                "    workers: " + workers.toString();
        return message;
    }

    public String getShift(){
        return this.shift;
    }

    public void setWorker(String jobTitle, String id){
        this.workers.computeIfAbsent(jobTitle, k -> new ArrayList<String>());
        this.workers.get(jobTitle).add(id);
    }

    public int getDay(){
        return this.day;
    }

    public Date getDate(){
        return this.date;
    }

    public Map<String, ArrayList<String>> getWorkers(){
        return this.workers;
    }

    public void checkDate(Date date){
        Date today = Calendar.getInstance().getTime();
        if(date.before(today))
            throw new IllegalArgumentException("Date passed away");
    }
}
