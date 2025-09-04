package in.co.rays.bean;

/**
 * The {@code SubjectBean} class represents a subject entity in the system.
 * It extends {@link BaseBean} to inherit common audit fields such as
 * ID, createdBy, modifiedBy, and timestamps.
 *
 * <p>This bean contains information about a subject, including
 * its name, associated course details, and description.</p>
 *
 * <p>It implements methods from {@code DropdownListBean} to provide
 * key-value pairs for dropdown lists, where the key is the subject ID
 * and the value is the subject name.</p>
 *
 * @author Akbar Mansuri
 * @version 1.0
 */
public class SubjectBean extends BaseBean {

    /** Name of the subject. */
    private String name;

    /** ID of the course associated with this subject. */
    private long courseId;

    /** Name of the course associated with this subject. */
    private String courseName;

    /** Description of the subject. */
    private String description;

    /**
     * Gets the name of the subject.
     *
     * @return the subject name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the subject.
     *
     * @param name the subject name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the ID of the course associated with the subject.
     *
     * @return the course ID
     */
    public long getCourseId() {
        return courseId;
    }

    /**
     * Sets the ID of the course associated with the subject.
     *
     * @param courseId the course ID to set
     */
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets the name of the course associated with the subject.
     *
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the name of the course associated with the subject.
     *
     * @param courseName the course name to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Gets the description of the subject.
     *
     * @return the subject description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the subject.
     *
     * @param description the subject description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the unique key for the subject, which is the {@code id}.
     *
     * @return the ID as a string
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Gets the display value for the subject, which is the subject name.
     *
     * @return the subject name
     */
    @Override
    public String getValue() {
        return name;
    }
}
