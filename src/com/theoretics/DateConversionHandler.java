/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.theoretics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class DateConversionHandler {
    
    private static Logger log = LogManager.getLogger(DateConversionHandler.class.getName());

    public static void main(String[] av) {
        DateConversionHandler dch = new DateConversionHandler();
        //System.out.println(dch.convertDate2DBbase("07/19/17 00:00:0.00"));
        System.out.println(dch.convertDate2DBbase("07/19/17"));
        
        System.out.println("NOW:" + dch.convertNow2Sec());
        //System.out.println("NOW in Milli:" + dch.convertNow2MilliSec());
        dch.convertDateLong2Sec( String.valueOf(dch.convertNow2Sec()));
        long secs = 0;
        try {
            System.out.println("2017-05-27 23:37:50.0:" + dch.convertDate2Sec("2017-05-27 23:37:50.0"));
            //secs = dch.convertDate2UnixTime("2017-06-23 23:37:50.0:") - dch.convertNow2Sec();
            secs = dch.convertJavaDate2UnixTime(new Date());
            Date d0 = dch.convertJavaUnixTime2Date(secs);
            System.out.println("D0 converted unixDate:" + secs);
            
            Date d1 = dch.convertJavaUnixTime2Date(1500007020);
            
            System.out.println("D1 converted unixDate:" + d1.toString());
            Date d2 = dch.convertJavaUnixTime2Date(1500018415); 
            
            System.out.println("D2 converted unixDate:" + d2.toString());
            
            Date d3 = dch.convertJavaUnixTime2Date(1500040800);
            
            System.out.println("D3 converted unixDate:" + d3.toString());

            
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        System.out.println(dch.getHoursfromseconds(3600));
        System.out.println(dch.getRemainingMinutesfromseconds(60));
    }
    
    public Date getElapsedTimeFromUnixTime(String timestampStr) {
        Date retDate = new Date();
        Long nowTS = convertJavaDate2UnixTime(new Date());
        Long inTS = Long.parseLong(timestampStr);
        
        Long elapsed = nowTS - inTS;
        Date d2 = convertJavaUnixTime2Date(elapsed);
        System.out.println("getElapsedTimeFromUnixTime:" + d2.toString());
        return retDate;
    }
    
    public long convertArduinoDate2UnixTime(Date date) {
        long unixTime = 0;
        // Minus 8 from Arduino
            date.setHours(date.getHours() + 8);
            date.setMinutes(date.getMinutes());
            unixTime = (long) date.getTime()/1000;
            System.out.println("Unix Time:" + unixTime);//<- prints 1352504418   
        
        return unixTime;
    }
    
    public long convertJavaDate2UnixTime(Date date) {
        long unixTime = 0;
        // Minus 8 from Arduino
//            date.setHours(date.getHours() + 4);
            date.setHours(date.getHours());
            date.setMinutes(date.getMinutes());
            unixTime = (long) date.getTime()/1000;
            //System.out.println("Unix Time:" + unixTime);//<- prints 1352504418   
        
        return unixTime;
    }
    
    public long convertJavaDate2UnixTimeDB(Date date) {
        long unixTime = 0;
        // Minus 8 from Arduino
            date.setHours(date.getHours() - 8);
            date.setMinutes(date.getMinutes());
            unixTime = (long) date.getTime()/1000;
            System.out.println("Unix Time:" + unixTime);//<- prints 1352504418   
        
        return unixTime;
    }
    
    public long convertJavaDate2UnixTime4Card(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss.S");
        Date date = sdf.parse(dateStr);
        long unixTime = 0;
        // Minus 8 from Arduino
            date.setHours(date.getHours() + 8);
            date.setMinutes(date.getMinutes());
            unixTime = (long) date.getTime()/1000;
            System.out.println("Unix Time:" + unixTime);//<- prints 1352504418   
        
        return unixTime;
    }
    
    public long convertJavaDate2UnixTime4Card(Date date) {
        long unixTime = 0;
        // Minus 8 from Arduino
            date.setHours(date.getHours() + 8);
            date.setMinutes(date.getMinutes());
            unixTime = (long) date.getTime()/1000;
            System.out.println("Unix Time:" + unixTime);//<- prints 1352504418   
        
        return unixTime;
    }
    
    public Date convertArduinoUnixTime2Date(String unixTimeStr) {
        // Add 8 from Arduino
        long unixTime = Long.parseLong(unixTimeStr);
            Date d = new Date(unixTime * 1000);
            d.setHours(d.getHours() - 8);
            d.setMinutes(d.getMinutes());
            System.out.println("unixDate:" + d.toString());
        
        return d;
    }
    
    public Date convertJavaUnixTime2Date(String unixTimeStr) {
        // Add 8 from Arduino
        long unixTime = Long.parseLong(unixTimeStr);
            Date d = new Date(unixTime * 1000);
            d.setHours(d.getHours() - 8);
            d.setMinutes(d.getMinutes());
            //System.out.println("unixDate:" + d.toString());
        
        return d;
    }
    
    public Date convertArduinoUnixTime2Date(long unixTime) {
        // Minus 8 from Arduino
            Date d = new Date(unixTime * 1000);
            d.setHours(d.getHours() - 8);
            d.setMinutes(d.getMinutes());
            System.out.println("unixDate:" + d.toString());
        
        return d;
    }
    
    public Date convertJavaUnixTime2Date(long unixTime) {
        // Minus 8 from Arduino
            Date d = new Date(unixTime * 1000);
            d.setHours(d.getHours() - 8);
            d.setMinutes(d.getMinutes());
            System.out.println("unixDate:" + d.toString());
        
        return d;
    }
    
    public Date convertJavaUnixTime2Date4DB(long unixTime) {
        // Minus 8 from Arduino
            Date d = new Date(unixTime * 1000);
            d.setHours(d.getHours());
            d.setMinutes(d.getMinutes());
            System.out.println("unixDate:" + d.toString());
        
        return d;
    }
    
    public long convertDate2Sec(String entdateStr) throws ParseException {// the datetext is already in date.long format - - -
        long noOFsecs = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss.S");
        Date entdate = sdf.parse(entdateStr);
        noOFsecs = this.GetYearinSecfrom2000(entdate.getYear());  //this gets total seconds from this year back to jan 1 2000
        noOFsecs = noOFsecs + this.convertmonths2secs(entdate.getMonth(), entdate.getYear()); //this adds the total seconds of the past month of this year
        noOFsecs = noOFsecs + this.convertdays2secs(entdate.getDate());
        noOFsecs = noOFsecs + this.converthours2secs(entdate.getHours());
        noOFsecs = noOFsecs + this.convertmins2secs(entdate.getMinutes());
        noOFsecs = noOFsecs + entdate.getSeconds();
        return noOFsecs;
    }

    public long convertNow2Sec() {// the datetext is already in date.long format - - -
        long noOFsecs = 0;
        Date NowStamp = new Date();
        noOFsecs = this.GetYearinSecfrom2000(NowStamp.getYear());  //this gets total seconds from this year back to jan 1 2000
        noOFsecs = noOFsecs + this.convertmonths2secs(NowStamp.getMonth(), NowStamp.getYear()); //this adds the total seconds of the past month of this year
        noOFsecs = noOFsecs + this.convertdays2secs(NowStamp.getDate());
        noOFsecs = noOFsecs + this.converthours2secs(NowStamp.getHours());
        noOFsecs = noOFsecs + this.convertmins2secs(NowStamp.getMinutes());
        noOFsecs = noOFsecs + NowStamp.getSeconds();
        return noOFsecs;
    }
    
    public long convertNow2MilliSec() {// the datetext is already in date.long format - - -
        long noOFsecs = 0;
        Date NowStamp = new Date();
        noOFsecs = this.GetYearinSecfrom2000(NowStamp.getYear());  //this gets total seconds from this year back to jan 1 2000
        noOFsecs = noOFsecs + this.convertmonths2secs(NowStamp.getMonth(), NowStamp.getYear()); //this adds the total seconds of the past month of this year
        noOFsecs = noOFsecs + this.convertdays2secs(NowStamp.getDate());
        noOFsecs = noOFsecs + this.converthours2secs(NowStamp.getHours());
        noOFsecs = noOFsecs + this.convertmins2secs(NowStamp.getMinutes());
        noOFsecs = noOFsecs + NowStamp.getSeconds();
        return noOFsecs * 1000;
    }

    public long convertDateLong2Sec(String datetext) {// the datetext is already in date.long format - - -
        Long convlong = Long.valueOf(datetext);
        convlong = convlong - (3600 * 32);  //3600 = 1 hour
        Date ParkerStamp = new Date(convlong * 1000);
        ParkerStamp.setYear(ParkerStamp.getYear() + 30);
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        System.out.println("--ParkerStamp--");
        System.out.println(df.format(ParkerStamp));
        System.out.println(tf.format(ParkerStamp));
        long noOFsecs = 0;

        noOFsecs = this.GetYearinSecfrom2000(ParkerStamp.getYear());  //this gets total seconds from this year back to jan 1 2000
        noOFsecs = noOFsecs + this.convertmonths2secs(ParkerStamp.getMonth(), ParkerStamp.getYear()); //this adds the total seconds of the past month of this year
        noOFsecs = noOFsecs + this.convertdays2secs(ParkerStamp.getDate());
        noOFsecs = noOFsecs + this.converthours2secs(ParkerStamp.getHours());
        noOFsecs = noOFsecs + this.convertmins2secs(ParkerStamp.getMinutes());
        noOFsecs = noOFsecs + ParkerStamp.getSeconds();

        return noOFsecs;
    }

    public long getHoursfromseconds(long seconds) {
        long Hours;
        Hours = seconds / 3600;
        return Hours;
    }

    public int getRemainingMinutesfromseconds(long seconds) {
        double Minutes;
        Minutes = seconds % 3600;
        Minutes = Minutes / 3600;
        Minutes = Minutes * 60;
        int x = (int) Minutes;
        return x;
    }

    public long GetYearinSecfrom2000(int year) {
        long cumulativeyears = 0;
        int loopbackto2000 = year - 100;
        while (loopbackto2000 != 0) {
            cumulativeyears = cumulativeyears + this.convertyears2secs(loopbackto2000);//31622280;
            loopbackto2000--;
        }
        return cumulativeyears;
    }

    public long convertyears2secs(int year) {
        int febleap = 0;
        if (year % 4 == 0) {
            febleap = 1;
        } else {
            febleap = 0;
        }
        return (365 + febleap) * 86400;
    }

    public long convertmonths2secs(int month, int year) {
        month = month + 1;
        year = year - 100;
        int febleap = 0;
        if (year % 4 == 0) {
            febleap = 29;
        } else {
            febleap = 28;
        }
        switch (month) {
            case 1:
                return 0;
            case 2:
                return 31 * 86400;
            case 3:
                return (31 + febleap) * 86400;
            case 4:
                return (31 + febleap + 31) * 86400;
            case 5:
                return (31 + febleap + 31 + 30) * 86400;
            case 6:
                return (31 + febleap + 31 + 30 + 31) * 86400;
            case 7:
                return (31 + febleap + 31 + 30 + 31 + 30) * 86400;
            case 8:
                return (31 + febleap + 31 + 30 + 31 + 30 + 31) * 86400;
            case 9:
                return (31 + febleap + 31 + 30 + 31 + 30 + 31 + 31) * 86400;
            case 10:
                return (31 + febleap + 31 + 30 + 31 + 30 + 31 + 31 + 30) * 86400;
            case 11:
                return (31 + febleap + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31) * 86400;
            case 12:
                return (31 + febleap + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30) * 86400;
        }
        return 0;
    }

    public long convertdays2secs(int days) {
        if (days == 0) //since there are no jan 0 , 2008
        {
            return 0;
        }
        long Elapseddays;
        Elapseddays = days * 86400;
        return Elapseddays;
    }

    public long converthours2secs(int hoursafter12) {
        if (hoursafter12 == 0) //since there are less than 3600 secs in 0:00 am - 0:59 am
        {
            return 0;
        }
        long Elapseddays;
        Elapseddays = hoursafter12 * 3600;
        return Elapseddays;
    }

    public long convertmins2secs(int minutesafter0) {
        if (minutesafter0 == 0) //since there are less than 60 secs in 0:00:00 am - 0:00:59 am
        {
            return 0;
        }
        long Elapseddays;
        Elapseddays = minutesafter0 * 60;
        return Elapseddays;
    }

    public String convertInt2base(int time) {
        String base = "";
        String Time = String.valueOf(time);
        if (Time.toString().length() == 1) {
            base = "0" + Time;
        } else {
            base = Time;
        }
        return base;
    }
    
    public String convertTime2base(String Time) {
        String base = "";
        if (Time.substring(1, 2).compareToIgnoreCase(":") == 0) {//ex. 1:45(4.length)  --- 1:9(3.length)   --- 2:4 PM(6.length) --- 2:18 PM(7.length)
            String newhour = "";
            switch (Time.length()) {
                case 3:
                    base = "0" + Time.substring(0, 1) + "0" + Time.substring(2, 3);
                    break;
                case 4:
                    base = "0" + Time.substring(0, 1) + Time.substring(2, 4);
                    break;
                case 6:
                    if (Time.substring(4, 5).compareToIgnoreCase("A") == 0)//AM
                    {
                        base = "0" + Time.substring(0, 1) + "0" + Time.substring(2, 3);
                    } else {
                        int newhr = Integer.parseInt(Time.substring(0, 1)) + 12;
                        newhour = String.valueOf(newhr);
                        base = newhour + "0" + Time.substring(2, 3);
                    }
                    break;
                case 7:
                    if (Time.substring(5, 6).compareToIgnoreCase("A") == 0) {
                        base = "0" + Time.substring(0, 1) + Time.substring(2, 4);
                    } else {
                        int newhr = Integer.parseInt(Time.substring(0, 1)) + 12;
                        if (newhr == 24) {
                            newhour = "00";
                            break;
                        } else if (newhr > 24) {
                            newhr = newhr - 12;
                        }
                        newhour = String.valueOf(newhr);
                        base = newhour + Time.substring(2, 4);
                    }
                    break;
            }

        } else if (Time.substring(2, 3).compareToIgnoreCase(":") == 0) {//ex. 11:32(5.length)  --- 10:5(4.length) --- 12:5 PM(7.length) --- 10:28 PM(8.length)
            String newhour = "";
            switch (Time.length()) {
                case 4:
                    base = Time.substring(0, 2) + "0" + Time.substring(3, 4);
                    break;
                case 5:
                    base = Time.substring(0, 2) + Time.substring(3, 5);
                    break;
                case 7:
                    if (Time.substring(5, 6).compareToIgnoreCase("A") == 0) {
                        base = Time.substring(0, 2) + "0" + Time.substring(3, 4);
                    } else {
                        int newhr = Integer.parseInt(Time.substring(0, 2)) + 12;
                        if (newhr == 24) {
                            newhour = "00";
                            break;
                        } else if (newhr > 24) {
                            newhr = newhr - 12;
                        }
                        newhour = String.valueOf(newhr);
                        base = newhour + "0" + Time.substring(3, 4);
                    }

                    break;
                case 8:
                    base = Time.substring(0, 2) + Time.substring(3, 5);
                    if (Time.substring(6, 7).compareToIgnoreCase("A") == 0)//AM
                    {
                        if (base.startsWith("12") == true) {
                            base = "00" + base.substring(2, base.length());
                        }
                    } else {
                        int newhr = Integer.parseInt(Time.substring(0, 2)) + 12;
                        if (newhr == 24) {
                            newhour = "00";

                        } else if (newhr > 24) {
                            newhr = newhr - 12;
                        }
                        newhour = String.valueOf(newhr);
                        base = newhour + "0" + Time.substring(3, 4);
                    }
                    break;
            }

        }
        return base;
    }

    public String convertDate2base(String date) {
        String base = "";
        if (date.substring(1, 2).compareToIgnoreCase("/") == 0) {//ex. 1/25/08(7.length)  --- 1/9/08(6.length)
            if (date.length() == 7) {
                base = "0" + date.substring(0, 1) + date.substring(2, 4) + "20" + date.substring(5, 7);
            } else {
                base = "0" + date.substring(0, 1) + "0" + date.substring(2, 3) + "20" + date.substring(4, 6);
            }
        } else if (date.substring(2, 3).compareToIgnoreCase("/") == 0) {//ex. 11/15/08(8.length)  --- 10/2/08(7.length)
            if (date.length() == 8) {
                base = date.substring(0, 2) + date.substring(3, 5) + "20" + date.substring(6, 8);
            } else {
                base = date.substring(0, 2) + "0" + date.substring(3, 4) + "20" + date.substring(5, 7);
            }
        }
        return base;
    }
    
    public String convertDate2BaseWidDash(String date) {
        String base = "";
        if (date.substring(1, 2).compareToIgnoreCase("/") == 0) {//ex. 1/25/08(7.length)  --- 1/9/08(6.length)
            if (date.length() == 7) {
                base = "0" + date.substring(0, 1) + "/" + date.substring(2, 4) + "/20" + date.substring(5, 7);
            } else {
                base = "0" + date.substring(0, 1) + "/0" + date.substring(2, 3) + "/20" + date.substring(4, 6);
            }
        } else if (date.substring(2, 3).compareToIgnoreCase("/") == 0) {//ex. 11/15/08(8.length)  --- 10/2/08(7.length)
            if (date.length() == 8) {
                base = date.substring(0, 2) + "/" + date.substring(3, 5) + "/20" + date.substring(6, 8);
            } else {
                base = date.substring(0, 2) + "/0" + date.substring(3, 4) + "/20" + date.substring(5, 7);
            }
        }
        return base;
    }

    public String convertDate2DBbase(String dateStr) {
        String base = "";
        try {
            dateStr = this.convertDate2BaseWidDash(dateStr) + " 00:00:0.00";
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy hh:mm:ss.S");
            
            Date date = sdf.parse(dateStr);
            
            SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-d");
            
            base = sdfOut.format(date);
            
        } catch (ParseException ex) {
             ex.printStackTrace();
        }
        return base;
    }

    public String formatTimeStamp(int MonthStamp) {
        String temp = "";
        MonthStamp = MonthStamp + 1;
        if (MonthStamp <= 9) {
            temp = "0" + String.valueOf(MonthStamp);
        } else if (MonthStamp > 9) {
            temp = String.valueOf(MonthStamp);
        }
        return temp;
    }

    public Date convertLong2Date(Long convlong) {
        
        convlong = convlong - (3600 * 32);  //3600 = 1 hour
        Date ParkerStamp = new Date(convlong * 1000);
        ParkerStamp.setYear(ParkerStamp.getYear() + 30);
        
        return ParkerStamp;
    }
    
    public Date convertStr2Date(String dateStr, String timeStr) {
        Date returnDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mma", Locale.ENGLISH);
            if (timeStr.substring(0, 2).compareToIgnoreCase("12") == 0) {
                if (timeStr.substring(timeStr.length() - 2, timeStr.length()).compareToIgnoreCase("AM") == 0) {
                    timeStr = timeStr.replaceFirst("12", "00");
                }
            }
            //timeStr = "0:00AM";
            returnDate = sdf.parse(dateStr + " " + timeStr);
            return returnDate;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return returnDate;
    }
}
