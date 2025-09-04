package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CourseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * The SubjectModel class provides methods to perform CRUD operations
 * on the "st_subject" database table.
 * <p>
 * It handles adding, updating, deleting, searching, and retrieving
 * subjects by primary key or name. It also handles logging and
 * database exceptions.
 * </p>
 */
public class SubjectModel {

    /** Logger instance for SubjectModel */
    private static Logger log = Logger.getLogger(SubjectModel.class);

    /**
     * Returns the next primary key for the subject table.
     *
     * @return next primary key as Integer
     * @throws DatabaseException if a database access error occurs
     */
    public Integer nextPk() throws DatabaseException {
        log.debug("SubjectModel nextPk start");
        Connection conn = null;
        int pk = 0;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_subject");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                pk = rs.getInt(1);
            }
            log.debug("Next PK: " + pk);
        } catch (Exception e) {
            log.error("Exception in nextPk", e);
            throw new DatabaseException("Exception in getting PK: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("SubjectModel nextPk end");
        return pk + 1;
    }

    /**
     * Adds a new subject to the database.
     *
     * @param bean SubjectBean containing subject details
     * @return primary key of the newly inserted record
     * @throws ApplicationException if a database access error occurs
     * @throws DuplicateRecordException if a subject with the same name exists
     */
    public long add(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("SubjectModel add start: ");
        Connection conn = null;
        int pk = 0;

        try {
            CourseModel courseModel = new CourseModel();
            CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
            bean.setCourseName(courseBean.getName());
            log.debug("Course name set: " + bean.getCourseName());

            SubjectBean existBean = findByName(bean.getName());
            if (existBean != null) {
                log.error("Duplicate subject name: " + bean.getName());
                throw new DuplicateRecordException("Subject name already exists");
            }

            conn = JDBCDataSource.getConnection();
            pk = nextPk();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("insert into st_subject values(?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getName());
            pstmt.setLong(3, bean.getCourseId());
            pstmt.setString(4, bean.getCourseName());
            pstmt.setString(5, bean.getDescription());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDatetime());
            pstmt.setTimestamp(9, bean.getModifiedDatetime());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Subject added successfully, rows affected: " + i);

        } catch (Exception e) {
            log.error("Exception in add", e);
            try {
                if (conn != null) conn.rollback();
            } catch (Exception e1) {
                log.error("Rollback failed in add", e1);
                throw new ApplicationException("Rollback exception in add: " + e1.getMessage());
            }
            throw new ApplicationException("Add Subject exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("SubjectModel add end");
        return pk;
    }

    /**
     * Deletes a subject from the database.
     *
     * @param bean SubjectBean containing the ID of the subject to delete
     * @throws ApplicationException if a database access error occurs
     */
    public void delete(SubjectBean bean) throws ApplicationException {
        log.debug("SubjectModel delete start: ID=" );
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_subject where id=?");
            pstmt.setLong(1, bean.getId());
            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Subject deleted successfully, rows affected: " + i);
        } catch (Exception e) {
            log.error("Exception in delete", e);
            throw new ApplicationException("Delete Subject exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("SubjectModel delete end");
    }

    /**
     * Updates an existing subject record in the database.
     *
     * @param bean SubjectBean containing updated subject details
     * @throws ApplicationException if a database access error occurs
     * @throws DuplicateRecordException if a subject with the same name exists
     */
    public void update(SubjectBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("SubjectModel update start");
        Connection conn = null;
        try {
            CourseModel courseModel = new CourseModel();
            CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
            bean.setCourseName(courseBean.getName());
            log.debug("Course name set: " + bean.getCourseName());

            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_subject set name=?, course_id=?, course_name=?, description=?, created_by=?, modified_by=?, created_datetime=?, modified_datetime=? where id=?");

            pstmt.setString(1, bean.getName());
            pstmt.setLong(2, bean.getCourseId());
            pstmt.setString(3, bean.getCourseName());
            pstmt.setString(4, bean.getDescription());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDatetime());
            pstmt.setTimestamp(8, bean.getModifiedDatetime());
            pstmt.setLong(9, bean.getId());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Subject updated successfully, rows affected: " + i);

        } catch (Exception e) {
            log.error("Exception in update", e);
            try {
                if (conn != null) conn.rollback();
            } catch (Exception e1) {
                log.error("Rollback failed in update()", e1);
                throw new ApplicationException("Rollback exception in update: " + e1.getMessage());
            }
            throw new ApplicationException("Update Subject exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("SubjectModel update end");
    }

    /**
     * Finds a subject by its name.
     *
     * @param name subject name
     * @return SubjectBean if found, otherwise null
     * @throws ApplicationException if a database access error occurs
     */
    public SubjectBean findByName(String name) throws ApplicationException {
        log.debug("SubjectModel.findByName() start: " + name);
        SubjectBean bean = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_subject where name=?");
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = new SubjectBean();
                bean.setId(rs.getInt(1));
                bean.setName(rs.getString(2));
                bean.setCourseId(rs.getLong(3));
                bean.setCourseName(rs.getString(4));
                bean.setDescription(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDatetime(rs.getTimestamp(8));
                bean.setModifiedDatetime(rs.getTimestamp(9));
            }
            log.debug("Subject fetched by name: " + (bean != null ? bean.getId() : "Not found"));
        } catch (Exception e) {
            log.error("Exception in findByName", e);
            throw new ApplicationException("Find by name exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("SubjectModel findByName end");
        return bean;
    }

    /**
     * Finds a subject by primary key.
     *
     * @param pk primary key of the subject
     * @return SubjectBean if found, otherwise null
     * @throws ApplicationException if a database access error occurs
     */
    public SubjectBean findByPk(long pk) throws ApplicationException {
        log.debug("SubjectModel findByPk start");
        SubjectBean bean = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_subject where id=?");
            pstmt.setLong(1, pk);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = new SubjectBean();
                bean.setId(rs.getInt(1));
                bean.setName(rs.getString(2));
                bean.setCourseId(rs.getLong(3));
                bean.setCourseName(rs.getString(4));
                bean.setDescription(rs.getString(5));
                bean.setCreatedBy(rs.getString(6));
                bean.setModifiedBy(rs.getString(7));
                bean.setCreatedDatetime(rs.getTimestamp(8));
                bean.setModifiedDatetime(rs.getTimestamp(9));
            }
            log.debug("Subject fetched by PK Not found");
        } catch (Exception e) {
            log.error("Exception in findByPk", e);
            throw new ApplicationException("Find by PK exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("SubjectModel findByPk end");
        return bean;
    }

    /**
     * Returns all subjects as a list.
     *
     * @return List of SubjectBean objects
     * @throws ApplicationException if a database access error occurs
     */
    public List list() throws ApplicationException {
        log.debug("SubjectModel list start");
        List list = search(null, 0, 0);
        log.debug("SubjectModel list end");
        return list;
    }

    /**
     * Searches for subjects based on criteria and pagination.
     *
     * @param bean SubjectBean containing search criteria
     * @param pageNo page number for pagination
     * @param pageSize number of records per page
     * @return List of SubjectBean objects matching criteria
     * @throws ApplicationException if a database access error occurs
     */
    public List search(SubjectBean bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("SubjectModel searc hstart");
        StringBuffer sql = new StringBuffer("Select * from st_subject where 1=1");

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" AND id= " + bean.getId());
            }
            if (bean.getName() != null && bean.getName().length() > 0) {
                sql.append(" AND name like '" + bean.getName() + "%'");
            }
            if (bean.getDescription() != null && bean.getDescription().length() > 0) {
                sql.append(" AND description like '" + bean.getDescription() + "%'");
            }
            if (bean.getCourseId() > 0) {
                sql.append(" AND course_id = " + bean.getCourseId());
            }
            if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
                sql.append(" AND course_name like '" + bean.getCourseName() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + "," + pageSize);
        }

        ArrayList list = new ArrayList();
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            log.debug("Search SQL: " + sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SubjectBean sBean = new SubjectBean();
                sBean.setId(rs.getInt(1));
                sBean.setName(rs.getString(2));
                sBean.setCourseId(rs.getLong(3));
                sBean.setCourseName(rs.getString(4));
                sBean.setDescription(rs.getString(5));
                sBean.setCreatedBy(rs.getString(6));
                sBean.setModifiedBy(rs.getString(7));
                sBean.setCreatedDatetime(rs.getTimestamp(8));
                sBean.setModifiedDatetime(rs.getTimestamp(9));
                list.add(sBean);
            }
            log.debug("Search completed, total records found: " + list.size());
        } catch (Exception e) {
            log.error("Exception in search", e);
            throw new ApplicationException("Search exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("SubjectModel search end");
        return list;
    }
}
