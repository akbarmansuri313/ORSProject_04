package in.co.rays.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.CourseBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.model.CourseModel;

/**
 * Test class for CourseModel.
 * <p>
 * This class contains different test methods to perform CRUD operations (Add,
 * Update, Delete, Find, Search) and utility operations (NextPk) on Course
 * entities using CourseModel and CourseBean.
 * </p>
 */
public class CourseTest {

	/**
	 * Main method to run specific test cases.
	 *
	 * @param args command-line arguments
	 * @throws Exception if any exception occurs
	 */
	public static void main(String[] args) throws Exception {
//      testNextPk();
//      testAdd();
//      testDelete();
//      testFindByName();
//      testFindByPk();
//      testUpdate();
		testSearch();
	}

	/**
	 * Tests retrieval of the next primary key from CourseModel.
	 *
	 * @throws ApplicationException if application-level error occurs
	 * @throws DatabaseException    if database error occurs
	 */
	private static void testNextPk() throws ApplicationException, DatabaseException {
		CourseModel model = new CourseModel();
		int i = model.nextPk();
		System.out.println("Next Pk " + i);
	}

	/**
	 * Tests adding a new Course record.
	 *
	 * @throws DuplicateRecordException if duplicate record is found
	 * @throws ApplicationException     if application-level error occurs
	 */
	private static void testAdd() throws DuplicateRecordException, ApplicationException {
		CourseBean bean = new CourseBean();
		CourseModel model = new CourseModel();

		// bean.setId(2);
		bean.setName("M.Ttech");
		bean.setDescription("Engineering");
		bean.setDuration("4 Year");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);
	}

	/**
	 * Tests deleting a Course record by ID.
	 *
	 * @throws ApplicationException if application-level error occurs
	 */
	private static void testDelete() throws ApplicationException {
		CourseBean bean = new CourseBean();
		CourseModel model = new CourseModel();

		model.delete(bean);

		bean.setId(2);
	}

	/**
	 * Tests finding a Course by its name.
	 *
	 * @throws Exception if any exception occurs
	 */
	private static void testFindByName() throws Exception {
		CourseModel model = new CourseModel();
		CourseBean bean = model.findByName("B.Tech");

		if (bean != null) {
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getDuration());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getCreatedDatetime());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getModifiedDatetime());
		}
	}

	/**
	 * Tests finding a Course by its primary key.
	 *
	 * @throws ApplicationException if application-level error occurs
	 */
	private static void testFindByPk() throws ApplicationException {
		CourseModel model = new CourseModel();
		CourseBean bean = model.findByPk(1);

		if (bean != null) {
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getDuration());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDatetime());
			System.out.println(bean.getModifiedDatetime());
		}
	}

	/**
	 * Tests updating an existing Course record.
	 *
	 * @throws ApplicationException     if application-level error occurs
	 * @throws DuplicateRecordException if duplicate record exists
	 */
	private static void testUpdate() throws ApplicationException, DuplicateRecordException {
		CourseBean bean = new CourseBean();
		CourseModel model = new CourseModel();

		bean.setId(1);
		bean.setName("B.com");
		bean.setDescription("commerce");
		bean.setDuration("3 Year");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.Update(bean);
	}

	/**
	 * Tests searching Courses based on given criteria with pagination.
	 *
	 * @throws DatabaseException    if database error occurs
	 * @throws ApplicationException if application-level error occurs
	 */
	private static void testSearch() throws DatabaseException, ApplicationException {
		CourseBean bean = new CourseBean();
		CourseModel model = new CourseModel();

		List list = model.search(bean, 1, 10);
		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (CourseBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t " + bean.getName());
			System.out.print("\t " + bean.getDescription());
			System.out.print("\t " + bean.getDuration());
			System.out.print("\t " + bean.getDescription());
			System.out.print("\t " + bean.getCreatedBy());
			System.out.print("\t " + bean.getModifiedBy());
			System.out.print("\t " + bean.getCreatedDatetime());
			System.out.println("\t " + bean.getModifiedDatetime());
		}
	}
}
