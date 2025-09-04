package in.co.rays.bean;

import java.util.Date;

/**
 * The {@code UserBean} class represents a user entity in the system.
 * It extends {@link BaseBean} to inherit common fields such as
 * ID, createdBy, modifiedBy, createdDatetime, and modifiedDatetime.
 *
 * <p>This bean stores personal and account-related details of a user,
 * including name, login credentials, date of birth, contact information,
 * role, and gender.</p>
 *
 * <p>It also provides implementations for {@code getKey()} and
 * {@code getValue()} methods to be used in dropdown lists or
 * selection menus, where the key is the user ID and the value is
 * the user's first name.</p>
 *
 * @author Akbar Mansuri
 * @version 1.0
 */
public class UserBean extends BaseBean {
    
    /** First name of the user. */
    private String firstName;

    /** Last name of the user. */
    private String lastName;

    /** Login ID of the user (used as username). */
    private String login;

    /** Password of the user account. */
    private String password;

    /** Confirm password field for validation. */
    private String confirmPassword;

    /** Date of birth of the user. */
    private Date dob;

    /** Mobile number of the user. */
    private String mobileNo;

    /** Role ID assigned to the user (e.g., Admin, Student, Faculty). */
    private long roleId;

    /** Gender of the user. */
    private String gender;

    /**
     * @return
     */
    public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
     * Gets the first name of the user.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the login ID of the user.
     *
     * @return the login ID
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the login ID of the user.
     *
     * @param login the login ID to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets the password of the user account.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user account.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the confirm password value.
     *
     * @return the confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the confirm password value.
     *
     * @param confirmPassword the confirm password to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Gets the date of birth of the user.
     *
     * @return the date of birth
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Sets the date of birth of the user.
     *
     * @param dob the date of birth to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Gets the mobile number of the user.
     *
     * @return the mobile number
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the mobile number of the user.
     *
     * @param mobileNo the mobile number to set
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
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
