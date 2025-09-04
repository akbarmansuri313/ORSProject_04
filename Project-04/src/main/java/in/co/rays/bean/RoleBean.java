package in.co.rays.bean;

/**
 * The {@code RoleBean} class represents different user roles in the system.
 * It extends {@link BaseBean} to inherit common properties such as 
 * ID, createdBy, modifiedBy, and timestamps.
 *
 * <p>This bean is used to store role details including the role name and 
 * description. It also defines constants for predefined roles such as 
 * ADMIN, STUDENT, FACULTY, and KIOSK.</p>
 *
 * <p>It implements methods from {@code DropdownListBean} to provide 
 * key-value pairs for dropdown lists, where the key is the role ID 
 * and the value is the role name.</p>
 *
 * @author Akbar Mansuri
 * @version 1.0
 */
public class RoleBean extends BaseBean {

    /** Constant representing the Admin role. */
    public static final int ADMIN = 1;

    /** Constant representing the Student role. */
    public static final int STUDENT = 2;

    /** Constant representing the Faculty role. */
    public static final int FACULTY = 3;

    /** Constant representing the Kiosk role. */
    public static final int KIOSK = 4;
    
    
    public static final int COLLEGE = 5;

    /** Name of the role (e.g., Admin, Student, Faculty, etc.). */
   
    private String name;

    /** Description of the role. */
    private String description;

    /**
     * Gets the name of the role.
     *
     * @return the role name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the role.
     *
     * @param name the role name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the role.
     *
     * @return the role description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the role.
     *
     * @param description the role description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the unique key for the role, which is the {@code id}.
     *
     * @return the ID as a string
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Gets the display value for the role, which is the {@code name}.
     *
     * @return the role name
     */
    @Override
    public String getValue() {
        return name;
    }
}
