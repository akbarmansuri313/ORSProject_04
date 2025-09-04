package in.co.rays.bean;

import java.util.Date;

/**
 * The {@code FacultyBean} class represents the data of a Faculty entity. It
 * extends {@link BaseBean} to inherit common fields such as ID, createdBy,
 * modifiedBy, and timestamps.
 * 
 * <p>
 * This bean is used to transfer faculty-related information such as personal
 * details, contact information, and associations with college, course, and
 * subject between different layers of the application.
 * </p>
 * 
 * <p>
 * It also implements methods from {@code DropdownListBean} to provide key-value
 * pairs for dropdown lists, where the key is the faculty ID and the value is
 * the faculty's first name.
 * </p>
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
public class FacultyBean extends BaseBean {

	/** First name of the faculty. */
	private String firstName;

	/** Last name of the faculty. */
	private String lastName;

	/** Date of birth of the faculty. */
	private Date dob;

	/** Gender of the faculty (e.g., Male/Female/Other). */
	private String gender;

	/** Mobile number of the faculty. */
	private String mobileNo;

	/** Email address of the faculty. */
	private String email;

	/** Associated college ID. */
	private long collegeId;

	/** Associated college name. */
	private String collegeName;

	/** Associated course ID. */
	private long courseId;

	/** Associated course name. */
	private String courseName;

	/** Associated subject ID. */
	private long subjectId;

	/** Associated subject name. */
	private String subjectName;

	/**
	 * Gets the first name of the faculty.
	 * 
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the faculty.
	 * 
	 * @param firstName the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name of the faculty.
	 * 
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the faculty.
	 * 
	 * @param lastName the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the date of birth of the faculty.
	 * 
	 * @return the date of birth
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * Sets the date of birth of the faculty.
	 * 
	 * @param dob the date of birth to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * Gets the gender of the faculty.
	 * 
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the gender of the faculty.
	 * 
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Gets the mobile number of the faculty.
	 * 
	 * @return the mobile number
	 */
	public String getMobileNo() {
		return mobileNo;
	}

	/**
	 * Sets the mobile number of the faculty.
	 * 
	 * @param mobileNo the mobile number to set
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	/**
	 * Gets the email address of the faculty.
	 * 
	 * @return the email address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address of the faculty.
	 * 
	 * @param email the email address to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the associated college ID.
	 * 
	 * @return the college ID
	 */
	public long getCollegeId() {
		return collegeId;
	}

	/**
	 * Sets the associated college ID.
	 * 
	 * @param collegeId the college ID to set
	 */
	public void setCollegeId(long collegeId) {
		this.collegeId = collegeId;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	@Override
	public String getKey() {

		return id + "";
	}

	@Override
	public String getValue() {

		return firstName;
	}
}
/**
 * Gets the associated college name.
 * 
 * @return the college name
 */
