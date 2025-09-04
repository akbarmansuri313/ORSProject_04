package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

public class CollegeModel {

	private static Logger log = Logger.getLogger(CollegeModel.class);

	/**
	 * Returns the next primary key for the college table.
	 * 
	 * @return next primary key as Integer
	 * @throws DatabaseException if there is a database error
	 */
	public Integer nextPK() throws DatabaseException {
		log.debug("Start nextPK()");
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("Select max(ID) from st_college");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
		} catch (Exception e) {
			log.error("Error in nextPK(): " + e.getMessage(), e);
			throw new DatabaseException("Exception Getting pk: " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("End nextPK");
		return pk + 1;
	}

	/**
	 * Adds a new college record in the database.
	 * 
	 * @param bean CollegeBean containing college details
	 * @return primary key of the newly inserted college
	 * @throws ApplicationException     if an application-level error occurs
	 * @throws DuplicateRecordException if college name already exists
	 */
	public long add(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Start add() - College: " + bean.getName());
		Connection conn = null;
		int pk = 0;

		CollegeBean existBean = findByName(bean.getName());
		if (existBean != null) {
			log.error("Duplicate College name: " + bean.getName());
			throw new DuplicateRecordException("College name Already Exist");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			pk = nextPK();
			PreparedStatement pstmt = conn
					.prepareStatement("insert into st_college values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getAddress());
			pstmt.setString(4, bean.getState());
			pstmt.setString(5, bean.getCity());
			pstmt.setString(6, bean.getPhoneNo());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			log.debug("Data inserted: " + i);
			conn.commit();
		} catch (Exception e) {
			log.error("Error in add(): " + e.getMessage(), e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error("Rollback error in add(): " + e1.getMessage(), e1);
				throw new ApplicationException("Add Rollback Exception: " + e1.getMessage());
			}
			throw new ApplicationException("Add College Exception: " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("End add College");
		return pk;
	}

	/**
	 * Updates an existing college record in the database.
	 * 
	 * @param bean CollegeBean containing updated college details
	 * @throws ApplicationException     if an application-level error occurs
	 * @throws DuplicateRecordException if college name already exists
	 */
	public void update(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Start update");
		Connection conn = null;

		CollegeBean existBean = findByName(bean.getName());
		if (existBean != null && bean.getId() != existBean.getId()) {
			log.error("Duplicate College name: " + bean.getName());
			throw new DuplicateRecordException("College name Already exist");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("update st_college set name = ?, address = ?, state = ?, city = ?,"
							+ "phone_no = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id  = ?");

			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getAddress());
			pstmt.setString(3, bean.getState());
			pstmt.setString(4, bean.getCity());
			pstmt.setString(5, bean.getPhoneNo());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.setLong(10, bean.getId());

			int i = pstmt.executeUpdate();
			log.debug("Data updated: " + i);
			conn.commit();
		} catch (Exception e) {
			log.error("Error in update(): " + e.getMessage(), e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				log.error("Rollback error in update(): " + e1.getMessage(), e1);
				throw new ApplicationException("Rollback Exception: " + e1.getMessage());
			}
			throw new ApplicationException("Update College Exception: " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("End update College ");
	}

	/**
	 * Deletes a college record from the database.
	 * 
	 * @param bean CollegeBean containing the ID to delete
	 * @throws ApplicationException if an application-level error occurs
	 */
	public void delete(CollegeBean bean) throws ApplicationException {
		log.debug("Start delete() - College ID: " + bean.getId());
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_college where id  = ?");
			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			log.debug("Data deleted: " + i);
			conn.commit();
		} catch (Exception e) {
			log.error("Error in delete(): " + e.getMessage(), e);
			throw new ApplicationException("Delete College Exception: " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("End delete() - College deleted successfully, ID: " + bean.getId());
	}

	/**
	 * Returns all college records.
	 * 
	 * @return List of CollegeBean
	 * @throws ApplicationException if an application-level error occurs
	 */
	public List list() throws ApplicationException {
		log.debug("Start list()");
		List list = search(null, 0, 0);
		log.debug("End list Records");
		return list;
	}

	/**
	 * Searches for college records based on criteria.
	 * 
	 * @param bean     CollegeBean containing search criteria
	 * @param pageNo   current page number
	 * @param pageSize number of records per page
	 * @return List of CollegeBean matching criteria
	 * @throws ApplicationException if an application-level error occurs
	 */
	public List search(CollegeBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Start search()");
		Connection conn = null;
		List list = new ArrayList();

		try {
			conn = JDBCDataSource.getConnection();
			StringBuffer sql = new StringBuffer("Select * from st_college where 1=1");

			if (bean != null) {

				if (bean.getId() > 0) {
					sql.append(" and id = " + bean.getId());
				}

				if (bean.getName() != null && bean.getName().length() > 0) {
					sql.append(" and name like '" + bean.getName() + "%'");
				}
				if (bean.getCity() != null && bean.getCity().length() > 0) {
					sql.append(" and city like '" + bean.getCity() + "%'");
				}

				if (bean.getPhoneNo() != null && bean.getPhoneNo().length() > 0) {
					sql.append(" and phone_no'" + bean.getPhoneNo() + "%'");
				}

				if (bean.getState() != null && bean.getState().length() > 0) {
					sql.append(" and state like '" + bean.getState() + "%'");
				}

			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				sql.append(" limit " + pageNo + "," + pageSize);
			}

			log.debug("SQL: " + sql);
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in search(): " + e.getMessage());
			throw new ApplicationException("Record Not Found: " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("End search Records ");
		return list;
	}

	/**
	 * Finds a college record by primary key.
	 * 
	 * @param id college ID
	 * @return CollegeBean if found, else null
	 * @throws ApplicationException if an application-level error occurs
	 */
	public CollegeBean findByPK(long id) throws ApplicationException {
		log.debug("Start findByPK");
		Connection conn = null;
		CollegeBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_college where id = ?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
			}
		} catch (Exception e) {
			log.error("Error in findByPK(): " + e.getMessage(), e);
			throw new ApplicationException("Application Exception: " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("End findByPK College");
		return bean;
	}

	/**
	 * Finds a college record by name.
	 * 
	 * @param name college name
	 * @return CollegeBean if found, else null
	 * @throws ApplicationException if an application-level error occurs
	 */
	public CollegeBean findByName(String name) throws ApplicationException {
		log.debug("Start findByName");
		Connection conn = null;
		CollegeBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_college where name  = ?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
			}
		} catch (Exception e) {
			log.error("Error in findByName(): " + e.getMessage(), e);
			throw new ApplicationException("Exception in application: " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("End findByName");
		return bean;
	}
}
