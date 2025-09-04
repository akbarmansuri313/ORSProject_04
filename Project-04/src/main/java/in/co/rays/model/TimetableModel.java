package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CourseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.bean.TimetableBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * TimetableModel class handles CRUD operations and business logic for
 * the Timetable entity in the database.
 * 
 * It provides methods to add, update, delete, search, and check timetable
 * entries for a particular course, subject, semester, exam date, and time.
 * 
 * This class interacts with the "st_timetable" database table and also
 * integrates with CourseModel and SubjectModel for course and subject
 * validation.
 */
public class TimetableModel {

	private static Logger log = Logger.getLogger(TimetableModel.class);
	
    /**
     * Returns the next primary key value for the timetable table.
     * 
     * @return Integer representing the next primary key
     * @throws Exception if any database error occurs
     */
    public Integer nextPk() throws Exception {
    	log.debug("TimetableModel nextPk started");
        int pk = 0;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_timetable");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pk = rs.getInt(1);
            }
            log.debug("Next primary key generated: " + pk);
        } catch (Exception e) {
        	 log.error("Database error in nextPk", e);
            throw new DatabaseException("Exception: Exception in getting pk:" + e);
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Next pk end");
        return pk + 1;
        
    }

    /**
     * Adds a new timetable record to the database.
     * 
     * @param bean TimetableBean object containing timetable data
     * @return long primary key of newly inserted record
     * @throws ApplicationException if application-level error occurs
     * @throws DuplicateRecordException if a duplicate record already exists
     */
    public long add(TimetableBean bean) throws ApplicationException, DuplicateRecordException {
        int pk = 0;
        CourseModel courseModel = new CourseModel();
        CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
        bean.setCourseName(courseBean.getName());
        SubjectModel subjectModel = new SubjectModel();
        SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
        bean.setSubjectName(subjectBean.getName());
        Connection conn = null;
        try {
            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn
                    .prepareStatement("insert into st_timetable values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getSemester());
            pstmt.setString(3, bean.getDescription());
            pstmt.setDate(4, new java.sql.Date(bean.getExamDate().getTime()));
            pstmt.setString(5, bean.getExamTime());
            pstmt.setLong(6, bean.getCourseId());
            pstmt.setString(7, bean.getCourseName());
            pstmt.setLong(8, bean.getSubjectId());
            pstmt.setString(9, bean.getSubjectName());
            pstmt.setString(10, bean.getCreatedBy());
            pstmt.setString(11, bean.getModifiedBy());
            pstmt.setTimestamp(12, bean.getCreatedDatetime());
            pstmt.setTimestamp(13, bean.getModifiedDatetime());
            int i = pstmt.executeUpdate();
            System.out.println("data inserted => " + i);
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e2) {
                throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
            }
            throw new ApplicationException("Exception : Add Timetable Exception" + e);
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return pk;
    }

    /**
     * Updates an existing timetable record in the database.
     * 
     * @param bean TimetableBean object containing updated timetable data
     * @throws ApplicationException if application-level error occurs
     * @throws DuplicateRecordException if a duplicate record exists
     */
    public void update(TimetableBean bean) throws ApplicationException, DuplicateRecordException {
        CourseModel courseModel = new CourseModel();
        CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
        bean.setCourseName(courseBean.getName());
        SubjectModel subjectModel = new SubjectModel();
        SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
        bean.setSubjectName(subjectBean.getName());
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_timetable set semester = ?, description = ?, exam_date = ?, exam_time = ?,"
                            + " course_id = ?, course_name = ?, subject_id = ?, subject_name = ?, created_by = ?,"
                            + " modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getSemester());
            pstmt.setString(2, bean.getDescription());
            pstmt.setDate(3, new java.sql.Date(bean.getExamDate().getTime()));
            pstmt.setString(4, bean.getExamTime());
            pstmt.setLong(5, bean.getCourseId());
            pstmt.setString(6, bean.getCourseName());
            pstmt.setLong(7, bean.getSubjectId());
            pstmt.setString(8, bean.getSubjectName());
            pstmt.setString(9, bean.getCreatedBy());
            pstmt.setString(10, bean.getModifiedBy());
            pstmt.setTimestamp(11, bean.getCreatedDatetime());
            pstmt.setTimestamp(12, bean.getModifiedDatetime());
            pstmt.setLong(13, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("data updated => " + i);
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e2) {
                throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
            }
            throw new ApplicationException("Exception : Add timetable Exception" + e);
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Deletes a timetable record from the database.
     * 
     * @param bean TimetableBean object containing the ID of the record to be deleted
     * @throws ApplicationException if application-level error occurs
     */
    public void delete(TimetableBean bean) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("delete from st_timetable where id = ?");
            pstmt.setLong(1, bean.getId());
            int i = pstmt.executeUpdate();
            System.out.println("Data Deleted = " + i);
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e2) {
                throw new ApplicationException("Exception : Add RollBack Exception" + e2.getMessage());
            }
            throw new ApplicationException("Exception : Delete Timetable Exception" + e);
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Finds a timetable record by primary key.
     * 
     * @param id timetable ID
     * @return TimetableBean if found, otherwise null
     * @throws ApplicationException if application-level error occurs
     */
    public TimetableBean findByPk(long id) throws ApplicationException {
        Connection conn = null;
        TimetableBean bean = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_timetable where id = ?");
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new TimetableBean();
                bean.setId(rs.getLong(1));
                bean.setSemester(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setExamDate(rs.getDate(4));
                bean.setExamTime(rs.getString(5));
                bean.setCourseId(rs.getLong(6));
                bean.setCourseName(rs.getString(7));
                bean.setSubjectId(rs.getLong(8));
                bean.setSubjectName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception getting timetable by pk");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Returns all timetable records.
     * 
     * @return List of TimetableBean
     * @throws ApplicationException if application-level error occurs
     */
    public List list() throws ApplicationException {
        return search(null, 0, 0);
    }

    /**
     * Searches timetable records based on given parameters with pagination.
     * 
     * @param bean TimetableBean containing filter criteria
     * @param pageNo page number for pagination
     * @param pageSize number of records per page
     * @return List of TimetableBean
     * @throws ApplicationException if application-level error occurs
     */
    public List search(TimetableBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        List list = new ArrayList();
        try {
            conn = JDBCDataSource.getConnection();
            StringBuffer sql = new StringBuffer("select * from st_timetable where 1=1");
            if (bean != null) {
                if (bean.getId() > 0) {
                    sql.append(" and id = " + bean.getId());
                }
                if (bean.getCourseId() > 0) {
                    sql.append(" and course_id = " + bean.getCourseId());
                }
                if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
                    sql.append(" and course_name like '" + bean.getCourseName() + "%'");
                }
                if (bean.getSubjectId() > 0) {
                    sql.append(" and subject_id = " + bean.getSubjectId());
                }
                if (bean.getSubjectName() != null && bean.getSubjectName().length() > 0) {
                    sql.append(" and subject_name like '" + bean.getSubjectName() + "%'");
                }
                if (bean.getSemester() != null && bean.getSemester().length() > 0) {
                    sql.append(" and semester like '" + bean.getSemester() + "%'");
                }
                if (bean.getDescription() != null && bean.getDescription().length() > 0) {
                    sql.append(" and description like '" + bean.getDescription() + "%'");
                }
                if (bean.getExamDate() != null && bean.getExamDate().getDate() > 0) {
                    sql.append(" and exam_date like '" + new java.sql.Date(bean.getExamDate().getTime()) + "%'");
                }
                if (bean.getExamTime() != null && bean.getExamTime().length() > 0) {
                    sql.append(" and exam_time like '" + bean.getExamTime() + "%'");
                }
            }
            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" limit " + pageNo + ", " + pageSize);
            }
            System.out.println("sql ==>> " + sql.toString());
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new TimetableBean();
                bean.setId(rs.getLong(1));
                bean.setSemester(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setExamDate(rs.getDate(4));
                bean.setExamTime(rs.getString(5));
                bean.setCourseId(rs.getLong(6));
                bean.setCourseName(rs.getString(7));
                bean.setSubjectId(rs.getLong(8));
                bean.setSubjectName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
                list.add(bean);
            }
        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception in search timetable " + e);
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return list;
    }

    /**
     * Checks timetable by course ID and exam date.
     * 
     * @param courseId course ID
     * @param examDate exam date
     * @return TimetableBean if found, otherwise null
     * @throws ApplicationException if application-level error occurs
     */
    public TimetableBean checkByCourseName(Long courseId, Date examDate) throws ApplicationException {
        StringBuffer sql = new StringBuffer("select * from st_timetable where course_id = ? and exam_date = ?");
        TimetableBean bean = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, courseId);
            pstmt.setDate(2, new java.sql.Date(examDate.getTime()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new TimetableBean();
                bean.setId(rs.getLong(1));
                bean.setSemester(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setExamDate(rs.getDate(4));
                bean.setExamTime(rs.getString(5));
                bean.setCourseId(rs.getLong(6));
                bean.setCourseName(rs.getString(7));
                bean.setSubjectId(rs.getLong(8));
                bean.setSubjectName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in get Timetable" + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Checks timetable by course, subject, and exam date.
     * 
     * @param courseId course ID
     * @param subjectId subject ID
     * @param examDate exam date
     * @return TimetableBean if found, otherwise null
     * @throws ApplicationException if application-level error occurs
     */
    public TimetableBean checkBySubjectName(Long courseId, Long subjectId, Date examDate)
            throws ApplicationException {
        StringBuffer sql = new StringBuffer(
                "select * from st_timetable where course_id = ? and subject_id = ? and exam_date = ?");
        TimetableBean bean = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, courseId);
            pstmt.setLong(2, subjectId);
            pstmt.setDate(3, new java.sql.Date(examDate.getTime()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new TimetableBean();
                bean.setId(rs.getLong(1));
                bean.setSemester(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setExamDate(rs.getDate(4));
                bean.setExamTime(rs.getString(5));
                bean.setCourseId(rs.getLong(6));
                bean.setCourseName(rs.getString(7));
                bean.setSubjectId(rs.getLong(8));
                bean.setSubjectName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in get Timetable");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Checks timetable by semester for a specific course and subject on an exam date.
     * 
     * @param courseId course ID
     * @param subjectId subject ID
     * @param semester semester name
     * @param examDate exam date
     * @return TimetableBean if found, otherwise null
     * @throws ApplicationException if application-level error occurs
     */
    public TimetableBean checkBySemester(Long courseId, Long subjectId, String semester, Date examDate)
            throws ApplicationException {
        StringBuffer sql = new StringBuffer(
                "select * from st_timetable where course_id = ? and subject_id = ? and semester = ? and exam_date = ?");
        TimetableBean bean = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, courseId);
            pstmt.setLong(2, subjectId);
            pstmt.setString(3, semester);
            pstmt.setDate(4, new java.sql.Date(examDate.getTime()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new TimetableBean();
                bean.setId(rs.getLong(1));
                bean.setSemester(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setExamDate(rs.getDate(4));
                bean.setExamTime(rs.getString(5));
                bean.setCourseId(rs.getLong(6));
                bean.setCourseName(rs.getString(7));
                bean.setSubjectId(rs.getLong(8));
                bean.setSubjectName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception in get Timetable");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Checks timetable by exam time and description for a specific course, subject, semester, and date.
     * 
     * @param courseId course ID
     * @param subjectId subject ID
     * @param semester semester name
     * @param examDate exam date
     * @param examTime exam time
     * @param description exam description
     * @return TimetableBean if found, otherwise null
     * @throws ApplicationException if application-level error occurs
     */
    public TimetableBean checkByExamTime(Long courseId, Long subjectId, String semester, Date examDate,
            String examTime, String description) throws ApplicationException {
        StringBuffer sql = new StringBuffer(
                "select * from st_timetable where course_id = ? and subject_id = ? and semester = ? and exam_date = ? and exam_time = ? and description = ?");
        TimetableBean bean = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, courseId);
            pstmt.setLong(2, subjectId);
            pstmt.setString(3, semester);
            pstmt.setDate(4, new java.sql.Date(examDate.getTime()));
            pstmt.setString(5, examTime);
            pstmt.setString(6, description);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new TimetableBean();
                bean.setId(rs.getLong(1));
                bean.setSemester(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setExamDate(rs.getDate(4));
                bean.setExamTime(rs.getString(5));
                bean.setCourseId(rs.getLong(6));
                bean.setCourseName(rs.getString(7));
                bean.setSubjectId(rs.getLong(8));
                bean.setSubjectName(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception : Exception in get Timetable");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }
}
