package in.co.rays.bean;

import java.util.Date;

import java.util.Date;

/**
 * The {@code StudentBean} class represents a student entity in the system.
 * It extends {@link BaseBean} to inherit common audit fields such as
 * ID, createdBy, modifiedBy, and timestamps.
 *
 * <p>This bean contains personal and academic details of a student,
 * such as first name, last name, date of birth, gender, contact details,
 * and associated college information.</p>
 *
 * <p>It implements methods from {@code DropdownListBean} to provide
 * key-value pairs for dropdown lists, where the key is the student ID
 * and the value is the full student name.</p>
 *
 * @author Akbar Mansuri
 * @version 1.0
 */
public class StudentBean extends BaseBean {

    /** First name of the student. */
    private String firstName;

    /** Last name of the student. */
    private String lastName;

    /** Date of birth of the student. */
    private Date dob;

    /** Gender of the student (e.g., Male, Female, Other). */
    private String gender;

    /** Mobile number of the student. */
    private String mobileNo;

    /** Email address of the student. */
    private String email;

    /** ID of the college to which the student belongs. */
    private long collegeId;

    /** Name of the college to which the student belongs. */
    private String collegeName;

    /**
     * Gets the first name of the student.
     *
     * @return the student's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the student.
     *
     * @param firstName the student's first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the student.
     *
     * @return the student's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the student.
     *
     * @param lastName the student's last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the date of birth of the student.
     *
     * @return the student's date of birth
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Sets the date of birth of the student.
     *
     * @param dob the student's date of birth to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Gets the gender of the student.
     *
     * @return the student's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the student.
     *
     * @param gender the student's gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the mobile number of the student.
     *
     * @return the student's mobile number
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the mobile number of the student.
     *
     * @param mobileNo the student's mobile number to set
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * Gets the email address of the student.
     *
     * @return the student's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the student.
     *
     * @param email the student's email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the ID of the college associated with the student.
     *
     * @return the college ID
     */
    public long getCollegeId() {
        return collegeId;
    }

    /**
     * Sets the ID of the college associated with the student.
     *
     * @param collegeId the college ID to set
     */
    public void setCollegeId(long collegeId) {
        this.collegeId = collegeId;
    }

    /**
     * Gets the name of the college associated with the student.
     *
     * @return the college name
     */
    public String getCollegeName() {
        return collegeName;
    }

    /**
     * Sets the name of the college associated with the student.
     *
     * @param collegeName the college name to set
     */
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    /**
     * Gets the unique key for the student, which is the {@code id}.
     *
     * @return the ID as a string
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Gets the display value for the student, which is the full name
     * (first name + last name).
     *
     * @return the student's full name
     */
    @Override
    public String getValue() {
        return firstName + " " + lastName;    
    }
}
