package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.util.EmailBuilder;
import in.co.rays.util.EmailMessage;
import in.co.rays.util.EmailUtility;
import in.co.rays.util.JDBCDataSource;

/**
 * UserModel class provides methods to perform CRUD operations on UserBean objects.
 * It includes user authentication, password management, and registration functionalities.
 * This class uses JDBC for database interaction and Log4j for logging.
 */
public class UserModel {

    Logger log = Logger.getLogger(UserModel.class);

    /**
     * Returns the next primary key for User table.
     *
     * @return next primary key as Integer
     * @throws DatabaseException if database error occurs
     */
    public Integer nextPK() throws DatabaseException {
        log.debug("UserModel nextPK started");
        Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("Select max(id) from st_user");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pk = rs.getInt(1);
            }
        } catch (Exception e) {
            log.error("Exception in nextPK", e);
            throw new DatabaseException("Exception : Exception in getting next Pk " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("UserModel nextPK ended");
        return pk + 1;
    }

    /**
     * Adds a new user to the database.
     *
     * @param bean UserBean object to be added
     * @return primary key of the newly added user
     * @throws ApplicationException if an application error occurs
     * @throws DuplicateRecordException if the login already exists
     */
    public long add(UserBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("UserModel add started");
        Connection conn = null;
        UserBean existBean = findByLogin(bean.getLogin());

        if (existBean != null) {
            log.error("Duplicate login found: " + bean.getLogin());
            throw new DuplicateRecordException("Exception : Duplicate Record Exception");
        }

        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPK();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("insert into st_user values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getFirstName());
            pstmt.setString(3, bean.getLastName());
            pstmt.setString(4, bean.getLogin());
            pstmt.setString(5, bean.getPassword());
            pstmt.setDate(6, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(7, bean.getMobileNo());
            pstmt.setLong(8, bean.getRoleId());
            pstmt.setString(9, bean.getGender());
            pstmt.setString(10, bean.getCreatedBy());
            pstmt.setString(11, bean.getModifiedBy());
            pstmt.setTimestamp(12, bean.getCreatedDatetime());
            pstmt.setTimestamp(13, bean.getModifiedDatetime());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("User added successfully, rows inserted: " + i);
        } catch (Exception e) {
            log.error("Exception in add()", e);
            try {
                conn.rollback();
            } catch (Exception e2) {
                log.error("Rollback failed in add()", e2);
                throw new ApplicationException("Exception : AddRollback in application" + e2.getMessage());
            }
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("UserModel add ended ");
        return pk;
    }

    /**
     * Updates an existing user in the database.
     *
     * @param bean UserBean object to be updated
     * @throws ApplicationException if an application error occurs
     * @throws DuplicateRecordException if the login already exists for another user
     */
    public void update(UserBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("UserModel update started ");
        Connection conn = null;

        UserBean beanExist = findByLogin(bean.getLogin());

        if (beanExist != null && !(beanExist.getId() == bean.getId())) {
            log.error("Duplicate login found during update: " + bean.getLogin());
            throw new DuplicateRecordException("Login Id is already exist");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "update st_user set first_name = ?, last_name = ?, login = ?, "
                            + "password = ?, dob = ?, mobile_no = ?, role_id = ?, gender = ?, created_by = ?, modified_by = ?, "
                            + "created_datetime = ?, modified_datetime = ? where id = ? ");

            pstmt.setString(1, bean.getFirstName());
            pstmt.setString(2, bean.getLastName());
            pstmt.setString(3, bean.getLogin());
            pstmt.setString(4, bean.getPassword());
            pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(6, bean.getMobileNo());
            pstmt.setLong(7, bean.getRoleId());
            pstmt.setString(8, bean.getGender());
            pstmt.setString(9, bean.getCreatedBy());
            pstmt.setString(10, bean.getModifiedBy());
            pstmt.setTimestamp(11, bean.getCreatedDatetime());
            pstmt.setTimestamp(12, bean.getModifiedDatetime());
            pstmt.setLong(13, bean.getId());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("User updated successfully, rows affected: " + i);
        } catch (Exception e) {
            log.error("Exception in update", e);
            try {
                conn.rollback();
            } catch (Exception e2) {
                log.error("Rollback failed in update", e2);
                throw new ApplicationException("Exception : Exception Update User" + e2.getMessage());
            }
            throw new DuplicateRecordException("Exception : Exception in user" + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("UserModel update ended ");
    }

    /**
     * Deletes a user from the database.
     *
     * @param bean UserBean object to be deleted
     * @throws ApplicationException if an application error occurs
     */
    public void delete(UserBean bean) throws ApplicationException {
        log.debug("UserModel delete started ");
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("delete from st_user where id = ?");
            pstmt.setLong(1, bean.getId());

            int i = pstmt.executeUpdate();
            conn.commit();
            log.debug("User deleted successfully, rows affected: " + i);

        } catch (Exception e) {
            log.error("Exception in delete()", e);
            throw new ApplicationException("Exception : Exception in delete data" + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("UserModel delete ended");
    }

    /**
     * Returns a list of all users.
     *
     * @return List of UserBean objects
     * @throws ApplicationException if an application error occurs
     */
    public List list() throws ApplicationException {
        log.debug("UserModel list called");
        return search(null, 0, 0);
    }

    /**
     * Searches for users based on criteria in the UserBean object.
     *
     * @param bean UserBean containing search criteria
     * @param pageNo page number for pagination
     * @param pageSize number of records per page
     * @return List of UserBean objects matching the criteria
     * @throws ApplicationException if an application error occurs
     */
    public List search(UserBean bean, int pageNo, int pageSize) throws ApplicationException {
        log.debug("UserModel search started");
        Connection conn = null;
        List list = new ArrayList();
        try {
            conn = JDBCDataSource.getConnection();

            StringBuffer sql = new StringBuffer("select * from st_user where 1=1");

            if (bean != null) {
                if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
                    sql.append(" and first_name like '" + bean.getFirstName() + "%'");
                }
                if (bean.getId() > 0) {
                    sql.append(" and Id like'" + bean.getId() + "%'");
                }
                if (bean.getLogin() != null && bean.getLogin().length() > 0) {
                    sql.append(" and login like '" + bean.getLogin() + "%'");
                }
                if (bean.getRoleId() > 0) {
                    sql.append(" and Role_id = '" + bean.getRoleId() + "%'");
                }
                if (bean.getMobileNo() != null && bean.getMobileNo().length() > 0) {
                    sql.append(" And mobile_no '" + bean.getMobileNo() + "%'");
                }

                if (pageSize > 0) {
                    pageNo = (pageNo - 1) * (pageSize);
                    sql.append(" Limit " + pageNo + "," + pageSize);
                }
            }

            log.debug("Search SQL: " + sql.toString());

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bean = new UserBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setLogin(rs.getString(4));
                bean.setPassword(rs.getString(5));
                bean.setDob(rs.getDate(6));
                bean.setMobileNo(rs.getString(7));
                bean.setRoleId(rs.getLong(8));
                bean.setGender(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
                list.add(bean);
            }

        } catch (Exception e) {
            log.error("Exception in search", e);
            throw new ApplicationException("Exception : Exception In Search " + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("UserModel search ended");
        return list;
    }

    /**
     * Finds a user by primary key.
     *
     * @param id primary key of the user
     * @return UserBean object
     * @throws ApplicationException if an application error occurs
     */
    public UserBean findByPk(long id) throws ApplicationException {
        log.debug("UserModel findByPk started ");
        Connection conn = null;
        UserBean bean = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_user where id  = ?");
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new UserBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setLogin(rs.getString(4));
                bean.setPassword(rs.getString(5));
                bean.setDob(rs.getDate(6));
                bean.setMobileNo(rs.getString(7));
                bean.setRoleId(rs.getLong(8));
                bean.setGender(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
        } catch (Exception e) {
            log.error("Exception in findByPk", e);
            throw new ApplicationException("Exception :Exception in find By Pk" + e.getMessage());
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("UserModel findByPk ended");
        return bean;
    }

    /**
     * Finds a user by login ID.
     *
     * @param login login ID of the user
     * @return UserBean object
     * @throws ApplicationException if an application error occurs
     */
    public UserBean findByLogin(String login) throws ApplicationException {
        log.debug("UserModel findByLogin started ");
        Connection conn = null;
        UserBean bean = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("Select * from st_user where login = ?");
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new UserBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setLogin(rs.getString(4));
                bean.setPassword(rs.getString(5));
                bean.setDob(rs.getDate(6));
                bean.setMobileNo(rs.getString(7));
                bean.setRoleId(rs.getLong(8));
                bean.setGender(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
        } catch (Exception e) {
            log.error("Exception in findByLogin", e);
            throw new ApplicationException("Exception : Application Exception is getting By User" + e);
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("UserModel findByLogin ended ");
        return bean;
    }

    /**
     * Authenticates a user with login and password.
     *
     * @param login login ID
     * @param password password
     * @return UserBean object if authentication succeeds, null otherwise
     * @throws ApplicationException if login fails
     */
    public UserBean authenticate(String login, String password) throws ApplicationException {
        log.debug("UserModel authenticate start");
        Connection conn = null;
        UserBean bean = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from st_user where login = ? and password = ?");
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new UserBean();
                bean.setId(rs.getLong(1));
                bean.setFirstName(rs.getString(2));
                bean.setLastName(rs.getString(3));
                bean.setLogin(rs.getString(4));
                bean.setPassword(rs.getString(5));
                bean.setDob(rs.getDate(6));
                bean.setMobileNo(rs.getString(7));
                bean.setRoleId(rs.getLong(8));
                bean.setGender(rs.getString(9));
                bean.setCreatedBy(rs.getString(10));
                bean.setModifiedBy(rs.getString(11));
                bean.setCreatedDatetime(rs.getTimestamp(12));
                bean.setModifiedDatetime(rs.getTimestamp(13));
            }
        } catch (Exception e) {
            log.error("Authentication failed for login: " + login, e);
            throw new ApplicationException("Exception : User login and password does not match");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("UserModel authenticate ended");
        return bean;
    }

    /**
     * Changes the password for a user.
     *
     * @param id user ID
     * @param oldPassword old password
     * @param newPassword new password
     * @return true if password changed successfully
     * @throws RecordNotFoundException if user not found
     * @throws ApplicationException if an application error occurs
     */
    public boolean changePassword(Long id, String oldPassword, String newPassword)
            throws RecordNotFoundException, ApplicationException {
        log.debug("UserModel changePassword start ");
        boolean flag = false;

        UserBean beanExist = findByPk(id);

        if (beanExist != null && beanExist.getPassword().equals(oldPassword)) {
            beanExist.setPassword(newPassword);
            try {
                update(beanExist);
                log.debug("Password updated in DB for user id: " + id);
            } catch (DuplicateRecordException e) {
                log.error("Duplicate record exception in changePassword", e);
                throw new ApplicationException("LoginId is already exist");
            }
            flag = true;
        } else {
            log.error("Old password invalid for user id: " + id);
            throw new RecordNotFoundException("Login not exist");
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("login", beanExist.getLogin());
        map.put("password", beanExist.getPassword());
        map.put("firstName", beanExist.getFirstName());
        map.put("lastName", beanExist.getLastName());

        String message = EmailBuilder.getChangePasswordMessage(map);

        EmailMessage msg = new EmailMessage();
        msg.setTo(beanExist.getLogin());
        msg.setSubject("Rays ORS Password has been changed Successfully.");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);

        EmailUtility.sendMail(msg);
        log.debug("Change password email sent for user id: " + id);

        return flag;
    }

    /**
     * Sends the forgotten password to user's email.
     *
     * @param login login ID of the user
     * @return true if email sent successfully
     * @throws ApplicationException if an application error occurs
     * @throws RecordNotFoundException if user not found
     */
    public boolean forgetPassword(String login) throws ApplicationException, RecordNotFoundException {
        log.debug("UserModel forgetPassword() called for login: " + login);
        UserBean userData = findByLogin(login);
        boolean flag = false;

        if (userData == null) {
            log.error("No user found with login: " + login);
            throw new RecordNotFoundException("Email ID does not exists !");
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("login", userData.getLogin());
        map.put("password", userData.getPassword());
        map.put("firstName", userData.getFirstName());
        map.put("lastName", userData.getLastName());

        String message = EmailBuilder.getForgetPasswordMessage(map);

        EmailMessage msg = new EmailMessage();
        msg.setTo(login);
        msg.setSubject("Rays ORS Password Reset");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);

        EmailUtility.sendMail(msg);
        log.debug("Forget password email sent for login: " + login);
        flag = true;

        return flag;
    }

    /**
     * Registers a new user and sends a registration email.
     *
     * @param bean UserBean object to be registered
     * @return primary key of newly registered user
     * @throws ApplicationException if an application error occurs
     * @throws DuplicateRecordException if login already exists
     */
    public long registerUser(UserBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("UserModel registerUser() started for login: " + bean.getLogin());
        long pk = add(bean);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("login", bean.getLogin());
        map.put("password", bean.getPassword());

        String message = EmailBuilder.getUserRegistrationMessage(map);

        EmailMessage msg = new EmailMessage();
        msg.setTo(bean.getLogin());
        msg.setSubject("Registration is successful for ORS Project");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);

        EmailUtility.sendMail(msg);
        log.debug("Registration email sent for login: " + bean.getLogin());
        return pk;
    }
}
