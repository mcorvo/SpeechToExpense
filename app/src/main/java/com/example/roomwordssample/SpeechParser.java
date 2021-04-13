package com.example.roomwordssample;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeechParser {
    private ArrayList<String> strList;

    public SpeechParser(ArrayList<String> strList) {
        this.strList = strList;
    }

    public String parse(ExpensesViewModel model) {
        String res = new String("");
        Pattern pattern = Pattern.compile("(\\w+)\\s(\\d{1,3})(?:[.,]\\d{1,2})?\\s(for|of)\\s(\\w+)\\s(?:on\\s)?(\\w+)");
        Matcher m1 = pattern.matcher(strList.get(0));

        if (m1.matches()) {
            String action = m1.group(1);
            Float amount = Float.valueOf(m1.group(2));
            String category = m1.group(4);
            String date = m1.group(5);
            Date d = new Date();
            // TODO: Remove Calendar
            Calendar day = Calendar.getInstance();
            if (!date.equals("today")) {
                // TODO: Fix the date in case it's different from "today"
                day.set(day.get(Calendar.YEAR), Calendar.MONTH, Calendar.DATE);
                d = day.getTime();
            }
            //SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            //date = dateFormat.format(day.getTime());
            model.insert(new Expenses(amount, category, d));
            res = date;
        } else {
            res = "Cannot identify your request, please try again...";
        }
        return res;
    }
}
