import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeCoordinator {
    static LocalDateTime dateTime;
    static LocalDateTime testTime;
    /**

     This method returns the DateTimeFormatter object for the "yyyy-MM-dd_HH:mm:ss" format.
     @return The DateTimeFormatter object for the "yyyy-MM-dd_HH:mm:ss" format.
     */
    public static DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
    }
    /**

     This method gets the current date and time as a LocalDateTime object.
     @return The current date and time as a LocalDateTime object.
     */
    public static LocalDateTime getDateTime() {
        return dateTime;
    }
    /**

     This method sets the current date and time to the specified LocalDateTime object.
     @param dateTime The LocalDateTime object to set as the current date and time.
     */
    public static void setDateTime(LocalDateTime dateTime) {
        TimeCoordinator.dateTime = dateTime;
    }
    /**

     This method sets the system time to the given date and time string in the format "yyyy-MM-dd_HH:mm:ss".
     @param givenDate The date and time string in the format "yyyy-MM-dd_HH:mm:ss" that will be used to set the system time.
     @param line The command line entered by the user.
     */
    public static void setTime(String givenDate, String line) {
        try {
            LocalDateTime.parse(givenDate, formatter());
        } catch (Exception e) {
            if (TimeCoordinator.dateTime == null&&line.contains("SetInitialTime")){
                OutputWriter.write("COMMAND: "+line+"\nERROR: Format of the initial date is wrong! Program is going to terminate!");
                OutputWriter.close();
                System.exit(0);
            }
            OutputWriter.write("ERROR: Time format is not correct!");
            return;
        }
        if (dateTime == null) {
            dateTime = LocalDateTime.parse(givenDate, formatter());
        }
        testTime = LocalDateTime.parse(givenDate, formatter());
        int result = testTime.compareTo(dateTime);
        if (result == 0) {
            if (!line.contains("SetInitial")) {
                OutputWriter.write("ERROR: There is nothing to change!");
            }
        }
        else if (result < 0){
            OutputWriter.write("ERROR: Time cannot be reversed!");
        }
        else {
            dateTime=testTime;
        }
        testTime=null;


    }

    /**

     This method skips the specified number of minutes to the current time.

     @param minutes the number of minutes to be skipped as a string

     @param line the command line received as a string
     */

    public static void skipMinutes( String minutes,String line) {
        int intMinutes;

        try {
            intMinutes = Integer.parseInt(minutes);
        } catch (Exception e) {
            OutputWriter.write("COMMAND: " + line);
            OutputWriter.write("ERROR: Erroneous command!");
            return;
        }
        if (intMinutes < 0) {
            OutputWriter.write("COMMAND: " + line);
            OutputWriter.write("ERROR: Time cannot be reversed!");
            return;
        }
        else if (intMinutes == 0) {
            OutputWriter.write("COMMAND: " + line);
            OutputWriter.write("ERROR: There is nothing to skip!");
            return;}


        OutputWriter.write("COMMAND: " + line);
        dateTime = dateTime.plusMinutes(intMinutes);

    }

    /**

     This method returns the current date and time as a string in the format "yyyy-MM-dd_HH:mm:ss".
     @return The current date and time as a string in the format "yyyy-MM-dd_HH:mm:ss".
     */
    public static String dateTimeDisplay() {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(formatter());
    }
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");



}