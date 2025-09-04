package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.MarksheetBean;
import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * Model class for handling Marksheet related operations such as add, update,
 * delete, search and fetch by primary key or roll number.
 * <p>
 * This class uses JDBC for database interactions and Log4j for logging.
 * All operations throw ApplicationException or DuplicateRecordException in case
 * of errors.
 * </p>
 */
public class MarksheetModel {

    /** Logger instance for MarksheetModel */
    private static Logger log = Logger.getLogger(MarksheetModel.class);

    /**
     * Returns the next primary key for the marksheet table.
     *
     * @return next primary key as Integer
     * @throws Exception if a database access error occurs
     */
    public Integer nextPk() throws Exception {
        log.debug("MarksheetModel nextPk start");
        int pk = 0;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_marksheet");
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

        log.debug("MarksheetModel nextPk end");
        return pk + 1;
    }

    /**
     * Adds a new marksheet record in the database.
     *
     * @param bean MarksheetBean containing marksheet details
     * @return primary key of the newly inserted record
     * @throws ApplicationException if a database access error occurs
     * @throws DuplicateRecordException if a marksheet with the same roll number exists
     */
    public long add(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("MarksheetModel add start");

        StudentModel stmodel = new StudentModel();
        StudentBean studentbean = stmodel.findByPk(bean.getStudentId());
        bean.setName(studentbean.getFirstName() + " " + studentbean.getLastName());

        MarksheetBean existBean = findByRollNo(bean.getRollNo());
        if (existBean != null) {
            log.error("Duplicate Roll No: " + bean.getRollNo());
            throw new DuplicateRecordException("Roll No already exists!");
        }

        int pk = 0;
        Connection conn = null;
        try {
            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into st_marksheet values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getRollNo());
            pstmt.setLong(3, bean.getStudentId());
            pstmt.setString(4, bean.getName());
            pstmt.setInt(5, bean.getPhysics());
            pstmt.setInt(6, bean.getChemistry());
            pstmt.setInt(7, bean.getMaths());
            pstmt.setString(8, bean.getCreatedBy());
            pstmt.setString(9, bean.getModifiedBy());
            pstmt.setTimestamp(10, bean.getCreatedDatetime());
            pstmt.setTimestamp(11, bean.getModifiedDatetime());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Data inserted: " + i);
        } catch (Exception e) {
            log.error("Exception in add", e);
            try {
                conn.rollback();
            } catch (Exception e2) {
                log.error("Rollback failed in add()", e2);
                throw new ApplicationException("Add rollback exception: " + e2.getMessage());
            }
            throw new ApplicationException("Add marksheet exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("MarksheetModel add end");
        return pk;
    }

    /**
     * Updates an existing marksheet record in the database.
     *
     * @param bean MarksheetBean containing updated marksheet details
     * @throws ApplicationException if a database access error occurs
     * @throws DuplicateRecordException if a marksheet with the same roll number exists
     */
    public void update(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("MarksheetModel update start");

        Connection conn = null;
        StudentModel studentModel = new StudentModel();
        StudentBean studentbean = studentModel.findByPk(bean.getStudentId());
        bean.setName(studentbean.getFirstName() + " " + studentbean.getLastName());

        MarksheetBean existBean = findByRollNo(bean.getRollNo());
        if (existBean != null && bean.getId() != existBean.getId()) {
            log.error("Duplicate Roll No found during update: " + bean.getRollNo());
            throw new DuplicateRecordException("Roll No already exists!");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_marksheet set roll_no = ?, student_id = ?, name = ?, physics = ?, chemistry = ?, "
                            + "maths = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
            pstmt.setString(1, bean.getRollNo());
            pstmt.setLong(2, bean.getStudentId());
            pstmt.setString(3, bean.getName());
            pstmt.setInt(4, bean.getPhysics());
            pstmt.setInt(5, bean.getChemistry());
            pstmt.setInt(6, bean.getMaths());
            pstmt.setString(7, bean.getCreatedBy());
            pstmt.setString(8, bean.getModifiedBy());
            pstmt.setTimestamp(9, bean.getCreatedDatetime());
            pstmt.setTimestamp(10, bean.getModifiedDatetime());
            pstmt.setLong(11, bean.getId());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Data updated: " + i);
        } catch (Exception e) {
            log.error("Exception in update", e);
            try {
                conn.rollback();
            } catch (Exception e2) {
                log.error("Rollback failed in update()", e2);
                throw new ApplicationException("Update rollback exception: " + e2.getMessage());
            }
            throw new ApplicationException("Update marksheet exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("MarksheetModel update end");
    }

    /**
     * Deletes a marksheet record from the database.
     *
     * @param bean MarksheetBean containing the ID to delete
     * @throws ApplicationException if a database access error occurs
     */
    public void delete(MarksheetBean bean) throws ApplicationException {
        log.debug("MarksheetModel delete start");

        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("delete from st_marksheet where id = ?");
            pstmt.setLong(1, bean.getId());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Data deleted: " + i);
        } catch (Exception e) {
            log.error("Exception in delete", e);
            try {
                conn.rollback();
            } catch (Exception e2) {
                log.error("Rollback failed in delete()", e2);
                throw new ApplicationException("Delete rollback exception: " + e2.getMessage());
            }
            throw new ApplicationException("Delete marksheet exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("MarksheetModel delete end");
    }

    /**
     * Finds a marksheet by primary key.
     *
     * @param id primary key of the marksheet
     * @return MarksheetBean if found, otherwise null
     * @throws ApplicationException if a database access error occurs
     */
    public MarksheetBean findByPk(long id) throws ApplicationException {
        log.debug("MarksheetModel findByPk start: " + id);
        Connection conn = null;
        MarksheetBean bean = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_marksheet where id = ?");
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new MarksheetBean();
                bean.setId(rs.getLong(1));
                bean.setRollNo(rs.getString(2));
                bean.setStudentId(rs.getLong(3));
                bean.setName(rs.getString(4));
                bean.setPhysics(rs.getInt(5));
                bean.setChemistry(rs.getInt(6));
                bean.setMaths(rs.getInt(7));
                bean.setCreatedBy(rs.getString(8));
                bean.setModifiedBy(rs.getString(9));
                bean.setCreatedDatetime(rs.getTimestamp(10));
                bean.setModifiedDatetime(rs.getTimestamp(11));
            }
        } catch (Exception e) {
            log.error("Exception in findByPk", e);
            throw new ApplicationException("Getting marksheet by pk exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("MarksheetModel findByPk end");
        return bean;
    }

    /**
     * Finds a marksheet by roll number.
     *
     * @param rollNo roll number of the student
     * @return MarksheetBean if found, otherwise null
     * @throws ApplicationException if a database access error occurs
     */
    public MarksheetBean findByRollNo(String rollNo) throws ApplicationException {
        log.debug("MarksheetModel findByRollNo start: " + rollNo);
        Connection conn = null;
        MarksheetBean bean = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_marksheet where roll_no = ?");
            pstmt.setString(1, rollNo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new MarksheetBean();
                bean.setId(rs.getLong(1));
                bean.setRollNo(rs.getString(2));
                bean.setStudentId(rs.getLong(3));
                bean.setName(rs.getString(4));
                bean.setPhysics(rs.getInt(5));
                bean.setChemistry(rs.getInt(6));
                bean.setMaths(rs.getInt(7));
                bean.setCreatedBy(rs.getString(8));
                bean.setModifiedBy(rs.getString(9));
                bean.setCreatedDatetime(rs.getTimestamp(10));
                bean.setModifiedDatetime(rs.getTimestamp(11));
            }
        } catch (Exception e) {
            log.error("Exception in findByRollNo", e);
            throw new ApplicationException("Getting marksheet by roll no exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("MarksheetModel findByRollNo end");
        return bean;
    }

    /**
     * Returns all marksheet records as a list.
     *
     * @return List of MarksheetBean objects
     * @throws ApplicationException if a database access error occurs
     */
    public List list() throws ApplicationException {
        log.debug("MarksheetModel list start");
        List list = search(null, 0, 0);
        log.debug("MarksheetModel list end");
        return list;
    }

    /**
     * Searches for marksheet records based on search criteria and pagination.
     *
     * @param bean MarksheetBean containing search criteria
     * @param pageNo page number for pagination
     * @param pageSize number of records per page
     * @return List of MarksheetBean objects matching criteria
     * @throws ApplicationException if a database access error occurs
     */
	public List search(MarksheetBean bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("MarksheetModel search start");
        Connection conn = null;
        List list = new ArrayList();

        try {
            conn = JDBCDataSource.getConnection();
            StringBuffer sql = new StringBuffer("select * from st_marksheet where 1=1");

            if (bean != null) {
                if (bean.getName() != null && bean.getName().length() > 0) {
                    sql.append(" and name like '" + bean.getName() + "%'");
                }
                if (bean.getRollNo() != null && bean.getRollNo().length() > 0) {
                    sql.append(" and roll_no like '" + bean.getRollNo() + "%'");
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
                bean = new MarksheetBean();
                bean.setId(rs.getLong(1));
                bean.setRollNo(rs.getString(2));
                bean.setStudentId(rs.getLong(3));
                bean.setName(rs.getString(4));
                bean.setPhysics(rs.getInt(5));
                bean.setChemistry(rs.getInt(6));
                bean.setMaths(rs.getInt(7));
                bean.setCreatedBy(rs.getString(8));
                bean.setModifiedBy(rs.getString(9));
                bean.setCreatedDatetime(rs.getTimestamp(10));
                bean.setModifiedDatetime(rs.getTimestamp(11));
                list.add(bean);
            }
            log.debug("Records found: " + list.size());
        } catch (Exception e) {
            log.error("Exception in search()", e);
            throw new ApplicationException("Search marksheet exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("MarksheetModel search end");
        return list;
    }

    /**
     * Returns a merit list of top scoring students based on total marks.
     *
     * @param pageNo page number for pagination
     * @param pageSize number of records per page
     * @return List of MarksheetBean objects representing merit list
     * @throws ApplicationException if a database access error occurs
     */
    public List<MarksheetBean> getMeritList(int pageNo, int pageSize) throws ApplicationException {
        log.debug("MarksheetModel getMeritList start");
        ArrayList<MarksheetBean> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer(
                "select id, roll_no, name, physics, chemistry, maths, (physics + chemistry + maths) as total "
                        + "from st_marksheet where physics > 33 and chemistry > 33 and maths > 33 order by total desc");

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }

        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                MarksheetBean bean = new MarksheetBean();
                bean.setId(rs.getLong(1));
                bean.setRollNo(rs.getString(2));
                bean.setName(rs.getString(3));
                bean.setPhysics(rs.getInt(4));
                bean.setChemistry(rs.getInt(5));
                bean.setMaths(rs.getInt(6));

                list.add(bean);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            log.error("Exception in getMeritList", e);
            throw new ApplicationException("Getting merit list exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("MarksheetModel getMeritList End ");
        return list;
    }
}
