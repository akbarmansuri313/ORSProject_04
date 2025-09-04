package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CollegeBean;
import in.co.rays.bean.CourseBean;
import in.co.rays.bean.FacultyBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * FacultyModel class provides CRUD operations and search functionality for Faculty entity.
 * It uses JDBC for database interactions and Apache Log4j for logging.
 * 
 * @author
 */
public class FacultyModel {

    /** Logger for FacultyModel */
    private static Logger log = Logger.getLogger(FacultyModel.class);

    /**
     * Returns the next primary key for the faculty table.
     * 
     * @return next primary key as Integer
     * @throws Exception if database operation fails
     */
    public Integer nextPk() throws Exception {
        log.debug("FacultyModel nextPk start");
        int pk = 0;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_faculty");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pk = rs.getInt(1);
            }
            log.debug("Next PK fetched: " + pk);
        } catch (Exception e) {
            log.error("Database Exception in nextPk", e);
            throw new DatabaseException("Exception in getting pk: " + e);
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("FacultyModel nextPk end");
        return pk + 1;
    }

    /**
     * Adds a new faculty record to the database.
     * 
     * @param bean FacultyBean containing faculty details
     * @return primary key of the newly inserted record
     * @throws DuplicateRecordException if email already exists
     * @throws ApplicationException if database operation fails
     */
    public long add(FacultyBean bean) throws DuplicateRecordException, ApplicationException {
        log.debug("FacultyModel add start");
        CollegeModel collegeModel = new CollegeModel();
        CollegeBean collegeBean = collegeModel.findByPK(bean.getCollegeId());
        bean.setCollegeName(collegeBean.getName());

        CourseModel courseModel = new CourseModel();
        CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
        bean.setCourseName(courseBean.getName());

        SubjectModel subjectModel = new SubjectModel();
        SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
        bean.setSubjectName(subjectBean.getName());

        FacultyBean existBean = findByEmail(bean.getEmail());
        if (existBean != null) {
            log.error("Duplicate email: " + bean.getEmail());
            throw new DuplicateRecordException("Email already exists!");
        }

        int pk = 0;
        Connection conn = null;

        try {
            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into st_faculty values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getFirstName());
            pstmt.setString(3, bean.getLastName());
            pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(5, bean.getGender());
            pstmt.setString(6, bean.getMobileNo());
            pstmt.setString(7, bean.getEmail());
            pstmt.setLong(8, bean.getCollegeId());
            pstmt.setString(9, bean.getCollegeName());
            pstmt.setLong(10, bean.getCourseId());
            pstmt.setString(11, bean.getCourseName());
            pstmt.setLong(12, bean.getSubjectId());
            pstmt.setString(13, bean.getSubjectName());
            pstmt.setString(14, bean.getCreatedBy());
            pstmt.setString(15, bean.getModifiedBy());
            pstmt.setTimestamp(16, bean.getCreatedDatetime());
            pstmt.setTimestamp(17, bean.getModifiedDatetime());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Data inserted: " + i);
        } catch (Exception e) {
            log.error("Exception in add()", e);
            try {
                conn.rollback();
            } catch (Exception e2) {
                log.error("Rollback failed in add()", e2);
                throw new ApplicationException("Add rollback exception: " + e2.getMessage());
            }
            throw new ApplicationException("Add faculty exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("FacultyModel add end");
        return pk;
    }

    /**
     * Updates an existing faculty record in the database.
     * 
     * @param bean FacultyBean containing updated faculty details
     * @throws DuplicateRecordException if email already exists for another record
     * @throws ApplicationException if database operation fails
     */
    public void update(FacultyBean bean) throws DuplicateRecordException, ApplicationException {
        log.debug("FacultyModel update start");

        CollegeModel collegeModel = new CollegeModel();
        CollegeBean collegeBean = collegeModel.findByPK(bean.getCollegeId());
        bean.setCollegeName(collegeBean.getName());

        CourseModel courseModel = new CourseModel();
        CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
        bean.setCourseName(courseBean.getName());

        SubjectModel subjectModel = new SubjectModel();
        SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
        bean.setSubjectName(subjectBean.getName());

        FacultyBean existBean = findByEmail(bean.getEmail());
        if (existBean != null && bean.getId() != existBean.getId()) {
            log.error("Duplicate email found during update: " + bean.getEmail());
            throw new DuplicateRecordException("Email already exists!");
        }

        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_faculty set first_name = ?, last_name = ?, dob = ?, gender = ?, mobile_no = ?, email = ?, college_id = ?,"
                            + " college_name = ?, course_id = ?, course_name = ?, subject_id = ?, subject_name = ?, created_by = ?,"
                            + " modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");

            pstmt.setString(1, bean.getFirstName());
            pstmt.setString(2, bean.getLastName());
            pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(4, bean.getGender());
            pstmt.setString(5, bean.getMobileNo());
            pstmt.setString(6, bean.getEmail());
            pstmt.setLong(7, bean.getCollegeId());
            pstmt.setString(8, bean.getCollegeName());
            pstmt.setLong(9, bean.getCourseId());
            pstmt.setString(10, bean.getCourseName());
            pstmt.setLong(11, bean.getSubjectId());
            pstmt.setString(12, bean.getSubjectName());
            pstmt.setString(13, bean.getCreatedBy());
            pstmt.setString(14, bean.getModifiedBy());
            pstmt.setTimestamp(15, bean.getCreatedDatetime());
            pstmt.setTimestamp(16, bean.getModifiedDatetime());
            pstmt.setLong(17, bean.getId());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Data updated: " + i);
        } catch (Exception e) {
            log.error("Exception in update()", e);
            try {
                conn.rollback();
            } catch (Exception e2) {
                log.error("Rollback failed in update()", e2);
                throw new ApplicationException("Update rollback exception: " + e2.getMessage());
            }
            throw new ApplicationException("Update faculty exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("FacultyModel update end");
    }

    /**
     * Deletes a faculty record from the database.
     * 
     * @param bean FacultyBean containing faculty ID to delete
     * @throws ApplicationException if database operation fails
     */
    public void delete(FacultyBean bean) throws ApplicationException {
        log.debug("FacultyModel delete start");

        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("delete from st_faculty where id = ?");
            pstmt.setLong(1, bean.getId());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Data deleted: " + i);
        } catch (Exception e) {
            log.error("Exception in delete()", e);
            try {
                conn.rollback();
            } catch (Exception e2) {
                log.error("Rollback failed in delete()", e2);
                throw new ApplicationException("Delete rollback exception: " + e2.getMessage());
            }
            throw new ApplicationException("Delete faculty exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("FacultyModel delete end");
    }

    /**
     * Finds a faculty record by primary key.
     * 
     * @param id primary key of the faculty
     * @return FacultyBean object if found, otherwise null
     * @throws ApplicationException if database operation fails
     */
    public FacultyBean findByPk(long id) throws ApplicationException {
        log.debug("FacultyModel findByPk start: " + id);
        Connection conn = null;
        FacultyBean bean = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_faculty where id = ?");
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new FacultyBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setDob(rs.getDate(4));
                bean.setGender(rs.getString(5));
                bean.setMobileNo(rs.getString(6));
                bean.setEmail(rs.getString(7));
                bean.setCollegeId(rs.getLong(8));
                bean.setCollegeName(rs.getString(9));
                bean.setCourseId(rs.getLong(10));
                bean.setCourseName(rs.getString(11));
                bean.setSubjectId(rs.getLong(12));
                bean.setSubjectName(rs.getString(13));
                bean.setCreatedBy(rs.getString(14));
                bean.setModifiedBy(rs.getString(15));
                bean.setCreatedDatetime(rs.getTimestamp(16));
                bean.setModifiedDatetime(rs.getTimestamp(17));
            }
            log.debug("Faculty found: " + bean);
        } catch (Exception e) {
            log.error("Exception in findByPk", e);
            throw new ApplicationException("Getting faculty by pk exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("FacultyModel findByPk end");
        return bean;
    }

    /**
     * Finds a faculty record by email.
     * 
     * @param email email of the faculty
     * @return FacultyBean object if found, otherwise null
     * @throws ApplicationException if database operation fails
     */
    public FacultyBean findByEmail(String email) throws ApplicationException {
        log.debug("FacultyModel findByEmail start: " + email);
        Connection conn = null;
        FacultyBean bean = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_faculty where email = ?");
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new FacultyBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setDob(rs.getDate(4));
                bean.setGender(rs.getString(5));
                bean.setMobileNo(rs.getString(6));
                bean.setEmail(rs.getString(7));
                bean.setCollegeId(rs.getLong(8));
                bean.setCollegeName(rs.getString(9));
                bean.setCourseId(rs.getLong(10));
                bean.setCourseName(rs.getString(11));
                bean.setSubjectId(rs.getLong(12));
                bean.setSubjectName(rs.getString(13));
                bean.setCreatedBy(rs.getString(14));
                bean.setModifiedBy(rs.getString(15));
                bean.setCreatedDatetime(rs.getTimestamp(16));
                bean.setModifiedDatetime(rs.getTimestamp(17));
            }
            log.debug("Faculty found by email: " + bean);
        } catch (Exception e) {
            log.error("Exception in findByEmail", e);
            throw new ApplicationException("Getting faculty by email exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("FacultyModel findByEmail end");
        return bean;
    }

    /**
     * Returns a list of all faculty records.
     * 
     * @return List of FacultyBean objects
     * @throws ApplicationException if database operation fails
     */
    public List list() throws ApplicationException {
        log.debug("FacultyModel list start");
        List list = search(null, 0, 0);
        log.debug("FacultyModel list end");
        return list;
    }

    /**
     * Searches for faculty records based on search criteria and pagination.
     * 
     * @param bean FacultyBean containing search criteria
     * @param pageNo page number for pagination
     * @param pageSize number of records per page
     * @return List of FacultyBean objects matching the criteria
     * @throws ApplicationException if database operation fails
     */
    public List search(FacultyBean bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("FacultyModel search start");
        Connection conn = null;
        List list = new ArrayList();

        try {
            conn = JDBCDataSource.getConnection();
            StringBuffer sql = new StringBuffer("select * from st_faculty where 1=1");

            if (bean != null) {
                if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
                    sql.append(" and first_name like '" + bean.getFirstName() + "%'");
                }
                if (bean.getLastName() != null && bean.getLastName().length() > 0) {
                    sql.append(" and last_name like '" + bean.getLastName() + "%'");
                }
                if (bean.getEmail() != null && bean.getEmail().length() > 0) {
                    sql.append(" and email like '" + bean.getEmail() + "%'");
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" limit " + pageNo + ", " + pageSize);
            }

            log.debug("Search SQL: " + sql.toString());
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new FacultyBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setDob(rs.getDate(4));
                bean.setGender(rs.getString(5));
                bean.setMobileNo(rs.getString(6));
                bean.setEmail(rs.getString(7));
                bean.setCollegeId(rs.getLong(8));
                bean.setCollegeName(rs.getString(9));
                bean.setCourseId(rs.getLong(10));
                bean.setCourseName(rs.getString(11));
                bean.setSubjectId(rs.getLong(12));
                bean.setSubjectName(rs.getString(13));
                bean.setCreatedBy(rs.getString(14));
                bean.setModifiedBy(rs.getString(15));
                bean.setCreatedDatetime(rs.getTimestamp(16));
                bean.setModifiedDatetime(rs.getTimestamp(17));

                list.add(bean);
            }
            log.debug("FacultyModel search found " + list.size() + " records");
        } catch (Exception e) {
            log.error("Exception in search ", e);
            throw new ApplicationException("Search faculty exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("FacultyModel search end");
        return list;
    }

}
