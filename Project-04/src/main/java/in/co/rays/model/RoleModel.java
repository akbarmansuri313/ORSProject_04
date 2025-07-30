package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.RoleBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

public class RoleModel {
	public Integer nextPK() throws ApplicationException {

		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("Select max(id) from st_role");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}

		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception in getting nextPk" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public long add(RoleBean bean) throws DuplicateRecordException, ApplicationException  {

		Connection conn = null;

		int pk = 0;

		RoleBean existBean = FindByName(bean.getName());

		if (existBean != null) {

			throw new DuplicateRecordException("Role Name is already exist");

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

			System.out.println("Data insert " + i);

		} catch (Exception e) {

			try {
				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Exception in Rollback " + e2.getMessage());

			}
			throw new ApplicationException("Exception : Exception in Add User Model" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;

	}

	public void delete(RoleBean bean) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_role where id = ?");

			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();

			conn.commit();

			System.out.println("data deleted=>" + i);

		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception in delete user" + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);

		}

	}

	public void update(RoleBean bean) throws ApplicationException, DuplicateRecordException  {

		Connection conn = null;

		RoleBean existBean = FindByName(bean.getName());

		if (existBean != null && bean.getId() != existBean.getId()) {

			throw new DuplicateRecordException("Role name Already exist");

		}

		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_role set name = ?,description = ?, created_by = ?, modified_by = ?,  created_datetime = ?, modified_datetime = ? where id = ?");

			pstmt.setString(1, bean.getName());

			pstmt.setString(2, bean.getDescription());

			pstmt.setString(3, bean.getCreatedBy());

			pstmt.setString(4, bean.getModifiedBy());

			pstmt.setTimestamp(5, bean.getCreatedDatetime());

			pstmt.setTimestamp(6, bean.getModifiedDatetime());

			pstmt.setLong(7, bean.getId());

			int i = pstmt.executeUpdate();

			conn.commit();

			System.out.println("data updated successfully=>" + i);

		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception in Update User " + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public List list() throws ApplicationException {
		return search(null, 0, 0);

	}

	public List search(RoleBean bean, int pageNo, int pageSize) throws ApplicationException {

		Connection conn = null;
		List list = new ArrayList();
		try {

			conn = JDBCDataSource.getConnection();

			StringBuffer sql = new StringBuffer("select * from st_role where 1 = 1");

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

			System.out.println("sql: " + sql.toString());

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

		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception in search Role model " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;

	}

	public RoleBean findByPK(long id) throws Exception {

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

			throw new ApplicationException("Exception : Exception in Role Id" + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	public RoleBean FindByName(String name) throws ApplicationException  {

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

			throw new ApplicationException("Exception : Exception in name " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

}
