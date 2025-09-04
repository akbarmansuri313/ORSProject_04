package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CollegeBean;
import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * StudentModel handles all database operations related to Student entity.
 * It provides methods to add, update, delete, search, and fetch student records.
 * <p>
 * This model interacts with the "st_student" table in the database.
 * It also handles logging using Log4j and throws appropriate exceptions
 * for database errors or duplicate records.
 * </p>
 */
public class StudentModel {

    /** Logger instance for StudentModel */
    private static Logger log = Logger.getLogger(StudentModel.class);

    /**
     * Returns the next primary key for the student table.
     *
     * @return next primary key as Integer
     * @throws DatabaseException if a database access error occurs
     */
    public Integer nextPk() throws DatabaseException {
        log.debug("StudentModel nextPk start");
        Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("Select max(Id) from st_student");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                pk = rs.getInt(1);
            }
            log.debug("Next PK fetched: " + pk);

        } catch (Exception e) {
            log.error("Exception in nextPk", e);
            throw new DatabaseException("Exception in getting next PK: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("StudentModel nextPk end");
        return pk + 1;
    }

    /**
     * Adds a new student to the database.
     *
     * @param bean StudentBean containing student details
     * @return primary key of the newly inserted record
     * @throws ApplicationException if a database access error occurs
     * @throws DuplicateRecordException if a student with the same email exists
     */
    public long add(StudentBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("StudentModel add start: ");
        int pk = 0;
        Connection conn = null;

        try {
            CollegeModel clgModel = new CollegeModel();
            CollegeBean clgBean = clgModel.findByPK(bean.getCollegeId());
            bean.setCollegeName(clgBean.getName());
            log.debug("College Name set: " + bean.getCollegeName());

            StudentBean existBean = findByEmail(bean.getEmail());
            if (existBean != null) {
                log.error("Duplicate email: " + bean.getEmail());
                throw new DuplicateRecordException("Email already exists");
            }

            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into st_student values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getFirstName());
            pstmt.setString(3, bean.getLastName());
            pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(5, bean.getGender());
            pstmt.setString(6, bean.getMobileNo());
            pstmt.setString(7, bean.getEmail());
            pstmt.setLong(8, bean.getCollegeId());
            pstmt.setString(9, bean.getCollegeName());
            pstmt.setString(10, bean.getCreatedBy());
            pstmt.setString(11, bean.getModifiedBy());
            pstmt.setTimestamp(12, bean.getCreatedDatetime());
            pstmt.setTimestamp(13, bean.getModifiedDatetime());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Student added successfully, rows affected: " + i);

        } catch (Exception e) {
            log.error("Exception in add", e);
            try {
                if (conn != null) conn.rollback();
            } catch (Exception e1) {
                log.error("Rollback failed in add", e1);
                throw new ApplicationException("Rollback exception in add(): " + e1.getMessage());
            }
            throw new ApplicationException("Add Student exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("StudentModel add end");
        return pk;
    }

    /**
     * Updates an existing student record in the database.
     *
     * @param bean StudentBean containing updated student details
     * @throws ApplicationException if a database access error occurs
     * @throws DuplicateRecordException if a student with the same email exists
     */
    public void update(StudentBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("StudentModel update start:" );
        Connection conn = null;

        try {
            StudentBean existBean = findByEmail(bean.getEmail());
            if (existBean != null && existBean.getId() != bean.getId()) {
                log.error("Duplicate email during update: " + bean.getEmail());
                throw new DuplicateRecordException("Email already exists");
            }

            CollegeModel collegeModel = new CollegeModel();
            CollegeBean collegeBean = collegeModel.findByPK(bean.getCollegeId());
            bean.setCollegeName(collegeBean.getName());

            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_student set first_name=?, last_name=?, dob=?, gender=?, mobile_no=?, email=?, college_id=?, college_name=?, created_by=?, modified_by=?, created_datetime=?, modified_datetime=? where id=?");

            pstmt.setString(1, bean.getFirstName());
            pstmt.setString(2, bean.getLastName());
            pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(4, bean.getGender());
            pstmt.setString(5, bean.getMobileNo());
            pstmt.setString(6, bean.getEmail());
            pstmt.setLong(7, bean.getCollegeId());
            pstmt.setString(8, bean.getCollegeName());
            pstmt.setString(9, bean.getCreatedBy());
            pstmt.setString(10, bean.getModifiedBy());
            pstmt.setTimestamp(11, bean.getCreatedDatetime());
            pstmt.setTimestamp(12, bean.getModifiedDatetime());
            pstmt.setLong(13, bean.getId());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Student updated successfully, rows affected: " + i);

        } catch (Exception e) {
            log.error("Exception in update", e);
            try {
                if (conn != null) conn.rollback();
            } catch (Exception e2) {
                log.error("Rollback failed in update", e2);
                throw new ApplicationException("Rollback exception in update " + e2.getMessage());
            }
            throw new ApplicationException("Update Student exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("StudentModel update end");
    }

    /**
     * Deletes a student record from the database.
     *
     * @param bean StudentBean containing the ID of the student to delete
     * @throws ApplicationException if a database access error occurs
     */
    public void delete(StudentBean bean) throws ApplicationException {
        log.debug("StudentModel delete start: ID=" );
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from st_student where id = ?");
            pstmt.setLong(1, bean.getId());

            int i = pstmt.executeUpdate();
            log.debug("Student deleted successfully, rows affected: " + i);

        } catch (Exception e) {
            log.error("Exception in delete", e);
            throw new ApplicationException("Delete Student exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("StudentModel delete end");
    }

    /**
     * Finds a student by primary key.
     *
     * @param id primary key of the student
     * @return StudentBean if found, otherwise null
     * @throws ApplicationException if a database access error occurs
     */
    public StudentBean findByPk(long id) throws ApplicationException {
        log.debug("StudentModel findByPk start");
        Connection conn = null;
        StudentBean bean = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_student where id = ?");
            pstmt.setLong(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = new StudentBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setDob(rs.getDate(4));
                bean.setGender(rs.getString(5));
                bean.setMobileNo(rs.getString(6));
                bean.setEmail(rs.getString(7));
                bean.setCollegeId(rs.getLong(8));
                bean.setCollegeName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
            log.debug("Student fetched: " + (bean != null ? bean.getEmail() : "Not found"));

        } catch (Exception e) {
            log.error("Exception in findByPk", e);
            throw new ApplicationException("Find Student by PK exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("StudentModel findByPk end");
        return bean;
    }

    /**
     * Finds a student by email.
     *
     * @param email email of the student
     * @return StudentBean if found, otherwise null
     * @throws ApplicationException if a database access error occurs
     */
    public StudentBean findByEmail(String email) throws ApplicationException {
        log.debug("StudentModel findByEmail start: ");
        Connection conn = null;
        StudentBean bean = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_student where email = ?");
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = new StudentBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setDob(rs.getDate(4));
                bean.setGender(rs.getString(5));
                bean.setMobileNo(rs.getString(6));
                bean.setEmail(rs.getString(7));
                bean.setCollegeId(rs.getLong(8));
                bean.setCollegeName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
            log.debug("Student fetched by email: " + (bean != null ? bean.getId() : "Not found"));

        } catch (Exception e) {
            log.error("Exception in findByEmail", e);
            throw new ApplicationException("Find Student by email exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("StudentModel findByEmail end");
        return bean;
    }

    /**
     * Returns all students as a list.
     *
     * @return List of StudentBean objects
     * @throws ApplicationException if a database access error occurs
     */
    public List list() throws ApplicationException {
        log.debug("StudentModel list start");
        List list = search(null, 0, 0);
        log.debug("StudentModel slist end");
        return list;
    }

    /**
     * Searches for students based on criteria and pagination.
     *
     * @param bean StudentBean containing search criteria
     * @param pageNo page number for pagination
     * @param pageSize number of records per page
     * @return List of StudentBean objects matching criteria
     * @throws ApplicationException if a database access error occurs
     */
    public List search(StudentBean bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("StudentModel search start");
        Connection conn = null;
        List list = new ArrayList();

        try {
            conn = JDBCDataSource.getConnection();
            StringBuffer sql = new StringBuffer("select * from st_student where 1=1");

            if (bean != null) {
                if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
                    sql.append(" and first_name like '" + bean.getFirstName() + "%'");
                }
                if (bean.getLastName() != null && bean.getLastName().length() > 0) {
                    sql.append(" and last_name like '" + bean.getLastName() + "%'");
                }
                if (bean.getDob() != null && bean.getDob().getTime() > 0) {
                    sql.append(" and dob like '" + new java.sql.Date(bean.getDob().getTime()) + "%'");
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" limit " + pageNo + " , " + pageSize);
            }

            log.debug("Search SQL: " + sql.toString());

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                StudentBean sBean = new StudentBean();
                sBean.setId(rs.getLong(1));
                sBean.setFirstName(rs.getString(2));
                sBean.setLastName(rs.getString(3));
                sBean.setDob(rs.getDate(4));
                sBean.setGender(rs.getString(5));
                sBean.setMobileNo(rs.getString(6));
                sBean.setEmail(rs.getString(7));
                sBean.setCollegeId(rs.getLong(8));
                sBean.setCollegeName(rs.getString(9));
                sBean.setCreatedBy(rs.getString(10));
                sBean.setModifiedBy(rs.getString(11));
                sBean.setCreatedDatetime(rs.getTimestamp(12));
                sBean.setModifiedDatetime(rs.getTimestamp(13));

                list.add(sBean);
            }

            log.debug("Search completed, total records found: " + list.size());

        } catch (Exception e) {
            log.error("Exception in search", e);
            throw new ApplicationException("Record not found Exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("StudentModel search end");
        return list;
    }
}
