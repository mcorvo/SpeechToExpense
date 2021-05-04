package com.example.roomwordssample;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeechParser {
    private ArrayList<String> strList;
    private String action;
    //private Actions action;
    private Float amount;
    private String category;
    private Date date;
    private Integer interval = 1;
    private enum Actions { REGISTER, REPORT, CLOSE}

    public SpeechParser(ArrayList<String> strList) {
        this.strList = strList;
    }

    public String parse(ExpensesViewModel model) {
        String res = new String("");

        // Pattern to register a new expense: the model is "register AMOUNT of CATEGORY on DATE"
        //Pattern regPattern = Pattern.compile("(\\w+)\\s((\\d{1,3})(?:[.,]\\d{1,2})?|\\w+)\\s(?:for|of)?\\s(\\w+)\\s(?:on\\s)?(\\w+)");
        Pattern regPattern = Pattern.compile("(\\w+) ((\\d{1,3})(?:[.,]\\d{1,2})?|\\w+) (?:di)? (\\w+) (?:al\\s)?(\\w+)");

        Matcher m1 = regPattern.matcher(strList.get(0));

        // Pattern to sum up the expenses of a given category: the model is "sum CATEGORY by DATE"
        Pattern repPattern = Pattern.compile("(\\w+\\s?\\w+) (\\w+) (last month|last (\\d+|\\w+) months)");
        Matcher m2 = repPattern.matcher(strList.get(0));

        if (m1.matches()) {
            action = m1.group(1);
            //action = Actions.valueOf(act);
            // TODO: Fix the try...catch clause to avoid issues when inserting in Room (line 56)
            try {
                amount = Float.valueOf(m1.group(2));
            } catch (Exception e) {
                res = "Cannot parse amount " + m1.group(2);
            }
            category = m1.group(4);
            String d = m1.group(5);
            date = new Date();
            // TODO: Remove Calendar
            Calendar day = Calendar.getInstance();
            if (!d.equals("oggi")) {
                // TODO: Fix the date in case it's different from "today"
                day.set(day.get(Calendar.YEAR), Calendar.MONTH, Calendar.DATE);
                date = day.getTime();
            }
            //SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            //date = dateFormat.format(day.getTime());
            model.insert(new Expenses(amount, category, date));
            res = d;
        } else if (m2.matches()) {
            action = m2.group(1);
            category = m2.group(2);
            String temp = m2.group(3);
            if (!temp.equals("last month")) {
                interval = Integer.valueOf(m2.group(4));
            }
            //action = Actions.valueOf(act);
            model.getCategoryTotal(category);
        } else {
            res = "Cannot identify your request, please try again...";
        }
        return res;
    }
}
