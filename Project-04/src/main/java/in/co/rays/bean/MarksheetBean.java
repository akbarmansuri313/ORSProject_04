package in.co.rays.bean;

/**
 * The {@code MarksheetBean} class represents the marksheet entity of a student.
 * It extends {@link BaseBean} to inherit common properties such as 
 * ID, createdBy, modifiedBy, and timestamps.
 *
 * <p>This bean is used to store and transfer marksheet details including
 * roll number, student ID, student name, and marks in Physics, Chemistry, and Maths.</p>
 *
 * <p>It also implements methods from {@code DropdownListBean} 
 * to provide key-value pairs for dropdown lists, where the 
 * key is the marksheet ID and the value is the roll number.</p>
 *
 * @author Akbar Mansuri
 * @version 1.0
 */
public class MarksheetBean extends BaseBean {

    /** Unique roll number of the student. */
    private String rollNo;

    /** Student ID associated with this marksheet. */
    private long studentId;

    /** Name of the student. */
    private String name;

    /** Marks obtained in Physics. */
    private Integer physics;

    /** Marks obtained in Chemistry. */
    private Integer chemistry;

    /** Marks obtained in Maths. */
    private Integer maths;

   
	/**
     * Gets the roll number of the student.
     *
     * @return the roll number
     */
    public String getRollNo() {
        return rollNo;
    }

    /**
     * Sets the roll number of the student.
     *
     * @param rollNo the roll number to set
     */
    /**
     * @param rollNo
     */
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
    
    /**
     * @return
     */
    public long getStudentId() {
		return studentId;
	}

	/**
	 * @param studentId
	 */
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public Integer getPhysics() {
		return physics;
	}

	/**
	 * @param physics
	 */
	public void setPhysics(Integer physics) {
		this.physics = physics;
	}

	/**
	 * @return
	 */
	public Integer getChemistry() {
		return chemistry;
	}

	/**
	 * @param chemistry
	 */
	public void setChemistry(Integer chemistry) {
		this.chemistry = chemistry;
	}

	/**
	 * @return
	 */
	public Integer getMaths() {
		return maths;
	}

	/**
	 * @param maths
	 */
	public void setMaths(Integer maths) {
		this.maths = maths;
	}


	@Override
	public String getKey() {
		
		return id + "";
	}

	@Override
	public String getValue() {
		
		return rollNo;
	}
	
	
}

  