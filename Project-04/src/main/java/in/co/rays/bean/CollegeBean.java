package in.co.rays.bean;

/**
 * The {@code CollegeBean} class represents the data of a College entity.
 * It extends {@link BaseBean} to inherit common fields such as 
 * ID, createdBy, modifiedBy, and timestamps.
 * 
 * <p>This bean is used to transfer college-related information 
 * such as name, address, state, city, and phone number between 
 * different layers of the application.</p>
 * 
 * <p>It also implements methods from {@code DropdownListBean} 
 * to provide key-value pairs for dropdown lists.</p>
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
public class CollegeBean extends BaseBean {

    /** Name of the college. */
    private String name;

    /** Address of the college. */
    private String address;

    /** State where the college is located. */
    private String state;

    /** City where the college is located. */
    private String city;

    /** Contact phone number of the college. */
    private String phoneNo;

    /**
     * Gets the name of the college.
     * 
     * @return the college name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the college.
     * 
     * @param name the college name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the address of the college.
     * 
     * @return the college address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the college.
     * 
     * @param address the college address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the state where the college is located.
     * 
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state where the college is located.
     * 
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets the city where the college is located.
     * 
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city where the college is located.
     * 
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the contact phone number of the college.
     * 
     * @return the phone number
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * Sets the contact phone number of the college.
     * 
     * @param phoneNo the phone number to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * Gets the unique key for the college, which is the {@code id}.
     * 
     * @return the ID as a string
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Gets the display value for the college, which is the {@code name}.
     * 
     * @return the college name
     */
    @Override
    public String getValue() {
        return name;
    }

}
