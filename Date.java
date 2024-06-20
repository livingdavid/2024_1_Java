import java.util.Scanner;

public class Date {
    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    private static boolean leapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static int daysDate(Date date) {
        int days = 0;
        int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        for (int i = 0; i < date.year; i++) {
            days += leapYear(i) ? 366 : 365;
        }

        for (int i = 1; i < date.month; i++) {
            days += daysInMonth[i];
            if (i == 2 && leapYear(date.year)) {
                days += 1;
            }
        }

        days += date.day;
        return days;
    }

    public static int daysDiff(Date startDate, Date endDate) {
        return daysDate(endDate) - daysDate(startDate);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a start date (yyyyMMdd): ");
        String startDateInput = scanner.nextLine();
        Date startDate = new Date(
                Integer.parseInt(startDateInput.substring(6, 8)),
                Integer.parseInt(startDateInput.substring(4, 6)),
                Integer.parseInt(startDateInput.substring(0, 4))
        );

        System.out.println("Enter an end date (yyyyMMdd): ");
        String endDateInput = scanner.nextLine();
        Date endDate = new Date(
                Integer.parseInt(endDateInput.substring(6, 8)),
                Integer.parseInt(endDateInput.substring(4, 6)),
                Integer.parseInt(endDateInput.substring(0, 4))
        );
        int diff = daysDiff(startDate, endDate);
        System.out.println("D-day: " + diff + " days.");
    }
}
