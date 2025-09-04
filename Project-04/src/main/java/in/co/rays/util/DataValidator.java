package in.co.rays.util;

import java.util.Calendar;
import java.util.Date;

/**
 * DataValidator class provides static utility methods to validate
 * various types of data such as strings, integers, long values,
 * emails, names, roll numbers, passwords, phone numbers, dates, 
 * and day of the week.
 * <p>
 * This class is typically used for input validation in forms
 * throughout the application.
 * </p>
 * 
 * @author Akbar
 * @version 1.0
 */
public class DataValidator {

    /**
     * Checks if a string is null or empty after trimming.
     * 
     * @param val the input string
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNull(String val) {
        if (val == null || val.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a string is not null and not empty.
     * 
     * @param val the input string
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotNull(String val) {
        return !isNull(val);
    }

    /**
     * Checks if a string represents a valid integer.
     * 
     * @param val the input string
     * @return true if the string is a valid integer, false otherwise
     */
    public static boolean isInteger(String val) {

        if (isNotNull(val)) {
            try {
                Integer.parseInt(val);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if a string represents a valid long value.
     * 
     * @param val the input string
     * @return true if the string is a valid long, false otherwise
     */
    public static boolean isLong(String val) {
        if (isNotNull(val)) {
            try {
                Long.parseLong(val);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if a string is a valid email address.
     * 
     * @param val the input string
     * @return true if the string is a valid email, false otherwise
     */
    public static boolean isEmail(String val) {

        String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (isNotNull(val)) {
            try {
                return val.matches(emailreg);
            } catch (NumberFormatException e) {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Checks if a string is a valid name.
     * 
     * @param val the input string
     * @return true if the string is a valid name, false otherwise
     */
    public static boolean isName(String val) {

        String namereg = "^[^-\\s][\\p{L} .'-]+$";

        if (isNotNull(val)) {
            try {
                return val.matches(namereg);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if a string is a valid roll number.
     * 
     * @param val the input string
     * @return true if the string is a valid roll number, false otherwise
     */
    public static boolean isRollNo(String val) {

        String rollreg = "[a-zA-Z]{2}[0-9]{3}";

        if (isNotNull(val)) {
            try {
                return val.matches(rollreg);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if a string is a valid password with required complexity.
     * 
     * @param val the input string
     * @return true if the string meets password complexity requirements, false otherwise
     */
    public static boolean isPassword(String val) {

        String passreg = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,12}";

        if (isNotNull(val)) {
            try {
                return val.matches(passreg);
            } catch (NumberFormatException e) {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Checks if a string's length is valid for a password (8-12 characters).
     * 
     * @param val the input string
     * @return true if length is between 8 and 12, false otherwise
     */
    public static boolean isPasswordLength(String val) {

        if (isNotNull(val) && val.length() >= 8 && val.length() <= 12) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a string is a valid phone number.
     * 
     * @param val the input string
     * @return true if the string is a valid phone number, false otherwise
     */
    public static boolean isPhoneNo(String val) {

        String phonereg = "^[6-9][0-9]{9}$";

        if (isNotNull(val)) {
            try {
                return val.matches(phonereg);
            } catch (NumberFormatException e) {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Checks if a string has a valid phone number length (10 digits).
     * 
     * @param val the input string
     * @return true if length is 10 digits, false otherwise
     */
    public static boolean isPhoneLength(String val) {

        if (isNotNull(val) && val.length() == 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a string can be parsed into a valid date.
     * 
     * @param val the input string
     * @return true if the string is a valid date, false otherwise
     */
    public static boolean isDate(String val) {

        Date d = null;
        if (isNotNull(val)) {
            d = DataUtility.getDate(val);
        }
        return d != null;
    }

    /**
     * Checks if a date string represents a Sunday.
     * 
     * @param val the input date string
     * @return true if the date is Sunday, false otherwise
     */
    public static boolean isSunday(String val) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(DataUtility.getDate(val));
        int i = cal.get(Calendar.DAY_OF_WEEK);

        if (i == Calendar.SUNDAY) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Main method for testing validation methods.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {

        System.out.println("Null String: " + isNull(null));

    

		// Test isNull and isNotNull
//		System.out.println("isNull Test:");
//		System.out.println("Empty String: " + isNull(""));

//		System.out.println("Non-null String: " + isNotNull("Hello"));
//
//		// Test isInteger
//		System.out.println("\nisInteger Test:");
//		System.out.println("Valid Integer String: '123' -> " + isInteger("123"));
//		System.out.println("Invalid Integer String: 'abc' -> " + isInteger("abc"));
//		System.out.println("Null String: -> " + isInteger(null));
//
//		// Test isLong
//		System.out.println("\nisLong Test:");
//		System.out.println("Valid Long String: '1234567890' -> " + isLong("1234567890"));
//		System.out.println("Invalid Long String: 'abc' -> " + isLong("abc"));
//
//		// Test isEmail
//		System.out.println("\nisEmail Test:");
//		System.out.println("Valid Email: 'test@example.com' -> " + isEmail("test@example.com"));
//		System.out.println("Invalid Email: 'test@.com' -> " + isEmail("test@.com"));
//
//		// Test isName
//		System.out.println("\nisName Test:");
//		System.out.println("Valid Name: 'John Doe' -> " + isName("John Doe"));
//		System.out.println("Invalid Name: '123John' -> " + isName("123John"));
//
//		// Test isRollNo
//		System.out.println("\nisRollNo Test:");
//		System.out.println("Valid RollNo: 'AB123' -> " + isRollNo("AB123"));
//		System.out.println("Invalid RollNo: 'A1234' -> " + isRollNo("A1234"));
//
//		// Test isPassword
//		System.out.println("\nisPassword Test:");
//		System.out.println("Valid Password: 'Passw0rd@123' -> " + isPassword("Passw0rd@123"));
//		System.out.println("Invalid Password: 'pass123' -> " + isPassword("pass123"));
//
//		// Test isPasswordLength
//		System.out.println("\nisPasswordLength Test:");
//		System.out.println("Valid Password Length: 'Passw0rd' -> " + isPasswordLength("Passw0rd"));
//		System.out.println("Invalid Password Length: 'pass' -> " + isPasswordLength("pass"));
//
//		// Test isPhoneNo
//		System.out.println("\nisPhoneNo Test:");
//		System.out.println("Valid PhoneNo: '9876543210' -> " + isPhoneNo("9876543210"));
//		System.out.println("Invalid PhoneNo: '1234567890' -> " + isPhoneNo("1234567890"));
//
//		// Test isPhoneLength
//		System.out.println("\nisPhoneLength Test:");
//		System.out.println("Valid Phone Length: '9876543210' -> " + isPhoneLength("9876543210"));
//		System.out.println("Invalid Phone Length: '98765' -> " + isPhoneLength("98765"));
//
//		// Test isDate
//		System.out.println("\nisDate Test:");
//		System.out.println("Valid Date: '10/15/2024' -> " + isDate("10/15/2024"));
//		System.out.println("Invalid Date: '2024-10-15' -> " + isDate("2024-10-15"));
//
//		// Test isSunday
//		System.out.println("\nisSunday Test:");
//		System.out.println("Date on Sunday: '10/13/2024' -> " + isSunday("10/13/2024"));
//		System.out.println("Date not on Sunday: '10/15/2024' -> " + isSunday("10/15/2024"));
//	}

    }
	}

