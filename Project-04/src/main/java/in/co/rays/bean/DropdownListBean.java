package in.co.rays.bean;

/**
 * The {@code DropdownListBean} interface provides a contract 
 * for beans that can supply key-value pairs. 
 * 
 * <p>It is typically implemented by entity beans such as 
 * {@code CollegeBean}, {@code CourseBean}, etc., to provide 
 * data for UI dropdown lists where a unique key and a display 
 * value are required.</p>
 * 
 * <p>For example, in a dropdown of colleges, the key might be 
 * the college ID and the value might be the college name.</p>
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
public interface DropdownListBean {

    /**
     * Returns the unique key for the dropdown item.
     * 
     * @return the key as a {@code String}
     */
    public String getKey();

    /**
     * Returns the display value for the dropdown item.
     * 
     * @return the value as a {@code String}
     */
    public String getValue();

}
