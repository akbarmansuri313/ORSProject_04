package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.CollegeBean;

import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.util.JDBCDataSource;

public class CollegeModel {

	public Integer nextPK() throws DatabaseException {

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
			throw new DatabaseException("Exception : Exception Getting pk" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk + 1;
	}

	public long add(CollegeBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		int pk = 0;

		CollegeBean existBean = findByName(bean.getName());

		if (existBean != null) {

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
			System.out.println("Data insert " + i);
			conn.commit();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception :  Add RollBack Exception" + e1.getMessage());

			}

			throw new ApplicationException("Exception : Add College Exception" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;

	}

	public void update(CollegeBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		CollegeBean existBean = findByName(bean.getName());

		if (existBean != null && bean.getId() != existBean.getId()) {

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
			System.out.println("Data update " + i);
			conn.commit();
		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception e1) {
				throw new ApplicationException("Execption : Exception RollBack Data" + e1.getMessage());

			}
			throw new ApplicationException("Exception : Exception in update User" + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public void delete(long id) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_college where id  = ?");
			pstmt.setLong(1, id);

			int i = pstmt.executeUpdate();
			conn.commit();

			System.out.println("Data delete " + i);

		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in College Delete" + e.getMessage());

		} finally {

			JDBCDataSource.closeConnection(conn);
		}

	}

	public List list() throws RecordNotFoundException {
		return search(null, 0, 0);

	}

	public List search(CollegeBean bean, int pageSize, int pageNo) throws RecordNotFoundException {

		Connection conn = null;

		List list = new ArrayList();
		try {

			conn = JDBCDataSource.getConnection();
			StringBuffer sql = new StringBuffer("Select * from st_college where 1=1");

			if (bean != null) {
				if (bean.getId() > 0) {
					sql.append("And id be '" + bean.getId() + "%'");
				}
				if (bean.getName() != null && bean.getName().length() > 0) {

					sql.append(" And name like '" + bean.getName() + "%'");
				}
				if (bean.getPhoneNo() != null && bean.getPhoneNo().length() > 0) {
					sql.append(" And phone be like '" + bean.getPhoneNo() + "%'");
				}
				if (bean.getState() != null && bean.getState().length() > 0) {
					sql.append(" And state be like '" + bean.getState() + "%'");
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;

				sql.append(" limit " + pageNo + "," + pageSize);

			}
			System.out.println("sql " + sql);

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

			throw new RecordNotFoundException("Exception : Record Not Found" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

	public CollegeBean findByPK(long id) throws ApplicationException {

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

			throw new ApplicationException("Exception : Applocation Exception " + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public CollegeBean findByName(String name) throws ApplicationException {
		Connection conn = null;
		CollegeBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("Select * from st_college where name  = ?");

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

			throw new ApplicationException("Exception :  Exception in application " + e.getMessage());

		}
		return bean;
	}
}
