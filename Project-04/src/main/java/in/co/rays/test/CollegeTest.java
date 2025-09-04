package in.co.rays.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.model.CollegeModel;

/**
 * This class contains test methods for the CollegeModel CRUD operations such as
 * add, update, delete, search, and retrieval by primary key/name.
 * 
 * Each method demonstrates usage of the CollegeModel with CollegeBean.
 */
public class CollegeTest {

	public static void main(String[] args)
			throws DatabaseException, ApplicationException, RecordNotFoundException, DuplicateRecordException {
//		testNextPK();
//		testAdd();
//		testUpdate();
//		testDelete();
		testSearch();
//		testFindByPK();
//		tesFindByName();

	}

	/**
	 * Test method to find a College by its name.
	 *
	 * @throws ApplicationException if any application-level exception occurs
	 */
	private static void tesFindByName() throws ApplicationException {

		CollegeModel model = new CollegeModel();
		CollegeBean bean = model.findByName("CDGI");

		if (bean != null) {

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getAddress());

			System.out.print("\t " + bean.getState());
			System.out.print("\t " + bean.getCity());
			System.out.print("\t  " + bean.getPhoneNo());
			System.out.print("\t " + bean.getCreatedBy());
			System.out.print("\t " + bean.getModifiedBy());
			System.out.print("\t " + bean.getCreatedDatetime());
			System.out.println("\t " + bean.getModifiedDatetime());

		}

	}

	/**
	 * Test method to find a College by its primary key (ID).
	 *
	 * @throws ApplicationException if any application-level exception occurs
	 */
	private static void testFindByPK() throws ApplicationException {

		CollegeModel model = new CollegeModel();

		CollegeBean bean = model.findByPK(4);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getAddress());

			System.out.print("\t " + bean.getState());
			System.out.print("\t " + bean.getCity());
			System.out.print("\t  " + bean.getPhoneNo());
			System.out.print("\t " + bean.getCreatedBy());
			System.out.print("\t " + bean.getModifiedBy());
			System.out.print("\t " + bean.getCreatedDatetime());
			System.out.println("\t " + bean.getModifiedDatetime());

		}

	}

	/**
	 * Test method to search Colleges based on given criteria. Demonstrates
	 * pagination with (0, 10).
	 *
	 * @throws ApplicationException if any application-level exception occurs
	 */
	private static void testSearch() throws ApplicationException {

		CollegeBean bean = new CollegeBean();

		CollegeModel model = new CollegeModel();

		bean.setName("IIT Indore");

//		bean.setCity("Indore");

		List list = model.search(bean, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {

			bean = (CollegeBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getAddress());

			System.out.print("\t " + bean.getState());
			System.out.print("\t " + bean.getCity());
			System.out.print("\t  " + bean.getPhoneNo());
			System.out.print("\t " + bean.getCreatedBy());
			System.out.print("\t " + bean.getModifiedBy());
			System.out.print("\t " + bean.getCreatedDatetime());
			System.out.println("\t " + bean.getModifiedDatetime());
		}

	}

	/**
	 * Test method to delete a College record by ID.
	 *
	 * @throws ApplicationException if any application-level exception occurs
	 */

	private static void testDelete() throws ApplicationException {
		CollegeModel model = new CollegeModel();

		CollegeBean bean = new CollegeBean();

		bean.setId(2);

		model.delete(bean);

	}

	/**
	 * Test method to update an existing College record.
	 *
	 * @throws ApplicationException     if any application-level exception occurs
	 * @throws DuplicateRecordException if a duplicate college is found
	 */

	private static void testUpdate() throws ApplicationException, DuplicateRecordException {

		CollegeBean bean = new CollegeBean();

		bean.setId(0);
		bean.setName("CDGI");
		bean.setAddress("Lig");
		bean.setState("M.P");
		bean.setCity("Indore");
		bean.setPhoneNo("0987654321");
		bean.setCreatedBy("akbar@gmail.com");
		bean.setModifiedBy("akbar@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		CollegeModel model = new CollegeModel();

		model.update(bean);

	}

	/**
	 * Test method to add a new College record.
	 *
	 * @throws ApplicationException     if any application-level exception occurs
	 * @throws DuplicateRecordException if the college already exists
	 */

	private static void testAdd() throws ApplicationException, DuplicateRecordException {
		CollegeBean bean = new CollegeBean();

		bean.setName("Davv");
		bean.setAddress("Lig");
		bean.setState("M.P");
		bean.setCity("Indore");
		bean.setPhoneNo("0987654321");
		bean.setCreatedBy("akbar@gmail.com");
		bean.setModifiedBy("akbar@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		CollegeModel model = new CollegeModel();

		model.add(bean);

	}

	/**
	 * Test method to get the next primary key for College table.
	 *
	 * @throws DatabaseException if any database-level exception occurs
	 */

	private static void testNextPK() throws DatabaseException {

		CollegeModel model = new CollegeModel();

		int i = model.nextPK();

		System.out.println("Next Pk " + i);

	}

}
