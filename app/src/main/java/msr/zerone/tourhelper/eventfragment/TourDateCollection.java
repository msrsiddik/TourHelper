package msr.zerone.tourhelper.eventfragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

public class TourDateCollection {
    public static List<String> dateList = new ArrayList<>();

    public TourDateCollection() {
    }

    public static void dateRange(String departureDate, String returnDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        Date depDt = format.parse(departureDate);
        Date retDt = format.parse(returnDate);

        long depLg = depDt.getTime();
        long retLg = retDt.getTime();
        dateList.clear();
        for (long i = depLg; i <= retLg; i+=100000000) {
            dateList.add(format.format(new Date(i)));
        }
    }
}
