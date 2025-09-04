package in.co.rays.bean;

import java.sql.Timestamp;

/**
 * The {@code BaseBean} class is an abstract base class that provides common 
 * attributes and functionality for all beans in the application. 
 * It contains fields for auditing purposes, such as created/modified user 
 * and timestamps.
 * 
 * <p>All beans that represent database entities should extend this class 
 * to inherit these common fields.</p>
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
public abstract class BaseBean implements DropdownListBean {

    /** Unique identifier of the record (Primary Key). */
    protected long id;

    /** The user who created the record. */
    protected String createdBy;

    /** The user who last modified the record. */
    protected String modifiedBy;

    /** Timestamp when the record was created. */
    protected Timestamp createdDatetime;

    /** Timestamp when the record was last modified. */
    protected Timestamp modifiedDatetime;

    /**
     * Gets the ID of the record.
     * 
     * @return the unique identifier
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the record.
     * 
     * @param id the unique identifier to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the creator of the record.
     * 
     * @return the user who created the record
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the record.
     * 
     * @param createdBy the user who created the record
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the modifier of the record.
     * 
     * @return the user who last modified the record
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets the modifier of the record.
     * 
     * @param modifiedBy the user who last modified the record
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * Gets the creation timestamp of the record.
     * 
     * @return the timestamp when the record was created
     */
    public Timestamp getCreatedDatetime() {
        return createdDatetime;
    }

    /**
     * Sets the creation timestamp of the record.
     * 
     * @param createdDatetime the timestamp to set
     */
    public void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    /**
     * Gets the modification timestamp of the record.
     * 
     * @return the timestamp when the record was last modified
     */
    public Timestamp getModifiedDatetime() {
        return modifiedDatetime;
    }

    /**
     * Sets the modification timestamp of the record.
     * 
     * @param modifiedDatetime the timestamp to set
     */
    public void setModifiedDatetime(Timestamp modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

}
