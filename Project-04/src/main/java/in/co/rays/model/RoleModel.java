package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.RoleBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * RoleModel is used to perform CRUD operations on Role entities.
 * It provides methods to add, update, delete, search, and fetch roles 
 * by primary key or name.
 * <p>
 * This model interacts with the "st_role" table in the database.
 * It also handles logging using Log4j and throws appropriate 
 * exceptions for database errors or duplicate records.
 * </p>
 */
public class RoleModel {

    /** Logger instance for RoleModel */
    private static Logger log = Logger.getLogger(RoleModel.class);

    /**
     * Returns the next primary key for the role table.
     *
     * @return next primary key as Integer
     * @throws ApplicationException if a database access error occurs
     */
    public Integer nextPK() throws ApplicationException {
        log.debug("RoleModel nextPK start");
        Connection conn = null;
        int pk = 0;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("Select max(id) from st_role");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pk = rs.getInt(1);
            }
            log.debug("Next PK fetched: " + pk);
        } catch (Exception e) {
            log.error("Exception in nextPK", e);
            throw new ApplicationException("Exception in getting nextPk: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("RoleModel nextPK end");
        return pk + 1;
    }

    /**
     * Adds a new role to the database.
     *
     * @param bean RoleBean containing role details
     * @return primary key of the newly inserted record
     * @throws DuplicateRecordException if a role with the same name exists
     * @throws ApplicationException if a database access error occurs
     */
    public long add(RoleBean bean) throws DuplicateRecordException, ApplicationException {
        log.debug("RoleModel add start: " + bean.getName());
        Connection conn = null;
        int pk = 0;

        RoleBean existBean = FindByName(bean.getName());
        if (existBean != null) {
            log.error("Duplicate role name: " + bean.getName());
            throw new DuplicateRecordException("Role Name already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPK();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("insert into st_role values(?,?,?,?,?,?,?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getName());
            pstmt.setString(3, bean.getDescription());
            pstmt.setString(4, bean.getCreatedBy());
            pstmt.setString(5, bean.getModifiedBy());
            pstmt.setTimestamp(6, bean.getCreatedDatetime());
            pstmt.setTimestamp(7, bean.getModifiedDatetime());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Role inserted successfully, rows affected: " + i);

        } catch (Exception e) {
            log.error("Exception in add", e);
            try {
                conn.rollback();
            } catch (Exception e2) {
                log.error("Rollback failed in add", e2);
                throw new ApplicationException("Rollback exception in add: " + e2.getMessage());
            }
            throw new ApplicationException("Add Role exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("RoleModel add end");
        return pk;
    }

    /**
     * Deletes a role from the database.
     *
     * @param bean RoleBean containing the ID of the role to delete
     * @throws ApplicationException if a database access error occurs
     */
    public void delete(RoleBean bean) throws ApplicationException {
        log.debug("RoleModel delete start");
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("delete from st_role where id = ?");
            pstmt.setLong(1, bean.getId());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Role deleted successfully, rows affected: " + i);

        } catch (Exception e) {
            log.error("Exception in delete", e);
            throw new ApplicationException("Delete Role exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("RoleMode delete end");
    }

    /**
     * Updates an existing role in the database.
     *
     * @param bean RoleBean containing updated role details
     * @throws ApplicationException if a database access error occurs
     * @throws DuplicateRecordException if a role with the same name exists
     */
    public void update(RoleBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("RoleModel.update start: ");
        Connection conn = null;

        RoleBean existBean = FindByName(bean.getName());
        if (existBean != null && bean.getId() != existBean.getId()) {
            log.error("Duplicate role name during update: " + bean.getName());
            throw new DuplicateRecordException("Role name already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                "update st_role set name = ?, description = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?"
            );
            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getDescription());
            pstmt.setString(3, bean.getCreatedBy());
            pstmt.setString(4, bean.getModifiedBy());
            pstmt.setTimestamp(5, bean.getCreatedDatetime());
            pstmt.setTimestamp(6, bean.getModifiedDatetime());
            pstmt.setLong(7, bean.getId());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("Role updated successfully, rows affected: " + i);

        } catch (Exception e) {
            log.error("Exception in update", e);
            throw new ApplicationException("Update Role exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("RoleModel update end");
    }

    /**
     * Returns all roles as a list.
     *
     * @return List of RoleBean objects
     * @throws ApplicationException if a database access error occurs
     */
    public List list() throws ApplicationException {
        log.debug("RoleModel list start");
        List list = search(null, 0, 0);
        log.debug("RoleModel list end ");
        return list;
    }

    /**
     * Searches for roles based on criteria and pagination.
     *
     * @param bean RoleBean containing search criteria
     * @param pageNo page number for pagination
     * @param pageSize number of records per page
     * @return List of RoleBean objects matching criteria
     * @throws ApplicationException if a database access error occurs
     */
    public List search(RoleBean bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("RoleModel search start");
        Connection conn = null;
        List list = new ArrayList();

        try {
            conn = JDBCDataSource.getConnection();
            StringBuffer sql = new StringBuffer("select * from st_role where 1=1");

            if (bean != null) {
                if (bean.getName() != null && bean.getName().length() > 0) {
                    sql.append(" and name like '" + bean.getName() + "%'");
                }
                if (bean.getId() > 0) {
                    sql.append(" and id=" + bean.getId());
                }
                if (bean.getDescription() != null && bean.getDescription().length() > 0) {
                    sql.append(" and description like '" + bean.getDescription() + "%'");
                }
                if (bean.getCreatedBy() != null && bean.getCreatedBy().length() > 0) {
                    sql.append(" and created_by like '" + bean.getCreatedBy() + "%'");
                }
                if (bean.getModifiedBy() != null && bean.getModifiedBy().length() > 0) {
                    sql.append(" and modified_by like '" + bean.getModifiedBy() + "%'");
                }
            }
            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                sql.append(" limit " + pageNo + "," + pageSize);
            }

            log.debug("Search SQL: " + sql.toString());
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new RoleBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setCreatedBy(rs.getString(4));
                bean.setModifiedBy(rs.getString(5));
                bean.setCreatedDatetime(rs.getTimestamp(6));
                bean.setModifiedDatetime(rs.getTimestamp(7));
                list.add(bean);
            }
            log.debug("Search completed, total records found: " + list.size());

        } catch (Exception e) {
            log.error("Exception in search", e);
            throw new ApplicationException("Search Role exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("RoleModel search end");
        return list;
    }

    /**
     * Finds a role by primary key.
     *
     * @param id primary key of the role
     * @return RoleBean if found, otherwise null
     * @throws ApplicationException if a database access error occurs
     */
    public RoleBean findByPK(long id) throws ApplicationException {
        log.debug("RoleModel findByPK start: ID=" + id);
        Connection conn = null;
        RoleBean bean = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_role where id = ?");
            pstmt.setLong(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new RoleBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setCreatedBy(rs.getString(4));
                bean.setModifiedBy(rs.getString(5));
                bean.setCreatedDatetime(rs.getTimestamp(6));
                bean.setModifiedDatetime(rs.getTimestamp(7));
            }
        } catch (Exception e) {
            log.error("Exception in findByPK", e);
            throw new ApplicationException("Find Role by PK exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("RoleModel findByPK end");
        return bean;
    }

    /**
     * Finds a role by its name.
     *
     * @param name name of the role
     * @return RoleBean if found, otherwise null
     * @throws ApplicationException if a database access error occurs
     */
    public RoleBean FindByName(String name) throws ApplicationException {
        log.debug("RoleModel.FindByName start");
        Connection conn = null;
        RoleBean bean = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_role where name = ?");
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new RoleBean();
                bean.setId(rs.getLong(1));
                bean.setName(rs.getString(2));
                bean.setDescription(rs.getString(3));
                bean.setCreatedBy(rs.getString(4));
                bean.setModifiedBy(rs.getString(5));
                bean.setCreatedDatetime(rs.getTimestamp(6));
                bean.setModifiedDatetime(rs.getTimestamp(7));
            }
        } catch (Exception e) {
            log.error("Exception in FindByName", e);
            throw new ApplicationException("Find Role by Name exception: " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("RoleModel FindByName end");
        return bean;
    }
}
