package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.CollegeBean;
import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.util.JDBCDataSource;

public class StudentModel {

	public Integer nextPk() throws DatabaseException {

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("Select max(Id) from st_student");

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}

		} catch (Exception e) {
			throw new DatabaseException("Exception : Exception is Getting " + e);

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public long add(StudentBean bean) throws ApplicationException, DuplicateRecordException {

		int pk = 0;

		Connection conn = null;

		CollegeModel clgModel = new CollegeModel();
		
		System.out.println("id: "+bean.getId());

		CollegeBean clgBean = clgModel.findByPK(bean.getCollegeId());
		
		System.out.println("name: "+clgBean.getName());

		bean.setCollegeName(clgBean.getName());

		StudentBean existBean = findByEmail(bean.getEmail());

		if (existBean != null) {

			throw new DuplicateRecordException("Email alredy Exist !!!...");

		}

		try {

			pk = nextPk();

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("insert into st_student values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

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
			System.out.println("data added successfully " + i);
			conn.commit();

		} catch (Exception e) {

			e.printStackTrace();

			try {
				conn.rollback();
			} catch (Exception e1) {

				throw new ApplicationException("Exception : Exception in TrnRollBack" + e1.getMessage());
			}
			throw new ApplicationException("Exception  : Application Exception" + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;

	}

	public void update(StudentBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		StudentBean existBean = findByEmail(bean.getEmail());

		if (existBean != null && existBean.getId() != bean.getId()) {
			throw new DuplicateRecordException("Email Id is already exist");
		}

		CollegeModel collegeModel = new CollegeModel();

		CollegeBean collegeBean = collegeModel.findByPK(bean.getCollegeId());

		bean.setCollegeName(collegeBean.getName());

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("update st_student set first_name = ?, last_name = ?, dob = ?,"
							+ "gender = ?, mobile_no = ?, email = ?, college_id = ?, college_name = ?, created_by = ?,"
							+ "modified_by = ?, created_datetime = ?, modified_datetime = ? where id  = ?");

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

			System.out.println();

			int i = pstmt.executeUpdate();
			conn.commit();

			System.out.println("Data Updated " + i);
		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception  : Application Exception" + e.getMessage());

			}
			throw new ApplicationException("Exception : Exception Student Exception" + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public void delete(StudentBean bean) throws Exception {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("delete from st_student where id = ?");

			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();

			JDBCDataSource.closeConnection(conn);

			System.out.println("data delete Successfully " + i);

		} catch (Exception e) {

			throw new ApplicationException("Exception : Application Exception " + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public StudentBean findByPk(long id) throws ApplicationException {

		Connection conn = null;
		StudentBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select * from st_student where id = ?");

			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
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

		} catch (Exception e) {

			throw new ApplicationException("Exception : Record not found Exception" + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public StudentBean findByEmail(String email) throws ApplicationException {

		Connection conn = null;
		StudentBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select * from st_student where email = ?");

			pstmt.setString(1, email);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
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
		} catch (Exception e) {
			throw new ApplicationException("Exception : Email not found");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;

	}

	public List list() throws Exception {
		return search(null, 0, 0);

	}

	public List search(StudentBean bean, int pageNo, int pageSize) throws Exception {

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

			System.out.println("sql ==>> " + sql.toString());

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

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

				list.add(bean);

			}

		} catch (Exception e) {

			throw new RecordNotFoundException("Exception : Record not found Exception" + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}
}