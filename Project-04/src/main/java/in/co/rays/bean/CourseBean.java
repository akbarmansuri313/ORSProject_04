package in.co.rays.bean;

/**
 * The {@code CourseBean} class represents the data of a Course entity.
 * It extends {@link BaseBean} to inherit common fields such as 
 * ID, createdBy, modifiedBy, and timestamps.
 * 
 * <p>This bean is used to encapsulate and transfer course-related 
 * information such as name, duration, and description between 
 * different layers of the application.</p>
 * 
 * <p>It also implements methods from {@code DropdownListBean} 
 * to provide key-value pairs for dropdown lists in UI components.</p>
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
public class CourseBean extends BaseBean {

    /** Name of the course. */
    private String name;

    /** Duration of the course. */
    private String duration;

    /** Description of the course. */
    private String description;

    /**
     * Gets the name of the course.
     * 
     * @return the course name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the course.
     * 
     * @param name the course name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the duration of the course.
     * 
     * @return the course duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the course.
     * 
     * @param duration the course duration to set
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Gets the description of the course.
     * 
     * @return the course description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the course.
     * 
     * @param description the course description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the unique key for the course, which is the {@code id}.
     * 
     * @return the ID as a string
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Gets the display value for the course, which is the {@code name}.
     * 
     * @return the course name
     */
    @Override
    public String getValue() {
        return name;
    }

}
