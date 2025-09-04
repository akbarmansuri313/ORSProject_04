package in.co.rays.bean;

import java.util.Date;

/**
 * The {@code TimetableBean} class represents the timetable entity in the system.
 * It extends {@link BaseBean} to inherit common properties such as
 * ID, createdBy, modifiedBy, and timestamps.
 *
 * <p>This bean contains details about exams, including semester,
 * description, exam date, exam time, course, and subject details.</p>
 *
 * <p>It implements methods from {@code DropdownListBean} to provide
 * key-value pairs for dropdown lists, where the key is the timetable ID
 * and the value is the subject name.</p>
 *
 * @author Akbar Mansuri
 * @version 1.0
 */
public class TimetableBean extends BaseBean {

    /** Semester of the timetable (e.g., "Spring 2025"). */
    private String semester;

    /** Description of the timetable or exam. */
    private String description;

    /** Date of the exam. */
    private Date examDate;

    /** Time of the exam. */
    private String examTime;

    /** ID of the associated course. */
    private long courseId;

    /** Name of the associated course. */
    private String courseName;

    /** ID of the associated subject. */
    private long subjectId;

    /** Name of the associated subject. */
    private String subjectName;

    /**
     * Gets the semester of the timetable.
     *
     * @return the semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Sets the semester of the timetable.
     *
     * @param semester the semester to set
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * Gets the description of the timetable or exam.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the timetable or exam.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the exam date.
     *
     * @return the exam date
     */
    public Date getExamDate() {
        return examDate;
    }

    /**
     * Sets the exam date.
     *
     * @param examDate the exam date to set
     */
    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    /**
     * Gets the exam time.
     *
     * @return the exam time
     */
    public String getExamTime() {
        return examTime;
    }

    /**
     * Sets the exam time.
     *
     * @param examTime the exam time to set
     */
    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    /**
     * Gets the ID of the associated course.
     *
     * @return the course ID
     */
    public long getCourseId() {
        return courseId;
    }

    /**
     * Sets the ID of the associated course.
     *
     * @param courseId the course ID to set
     */
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets the name of the associated course.
     *
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the name of the associated course.
     *
     * @param courseName the course name to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Gets the ID of the associated subject.
     *
     * @return the subject ID
     */
    public long getSubjectId() {
        return subjectId;
    }

    /**
     * Sets the ID of the associated subject.
     *
     * @param subjectId the subject ID to set
     */
    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * Gets the name of the associated subject.
     *
     * @return the subject name
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * Sets the name of the associated subject.
     *
     * @param subjectName the subject name to set
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * Gets the unique key for the timetable, which is the {@code id}.
     *
     * @return the ID as a string
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Gets the display value for the timetable, which is the subject name.
     *
     * @return the subject name
     */
    @Override
    public String getValue() {
        return semester;
    }
}
