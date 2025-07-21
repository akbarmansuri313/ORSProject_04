package in.co.rays.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.model.CollegeModel;
import in.co.rays.model.StudentModel;

public class StudentTest {

	public static void main(String[] args) throws Exception {

//		testNextPK();
//		testAdd();
//		testUpdate();
//		testDelete();
//		testFindByPK();
//		testFindByEmail();
		testSearch();
	}

	private static void testSearch() throws Exception {

		StudentModel model = new StudentModel();

		StudentBean bean = new StudentBean();

		List list = model.search(bean, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {

			bean = (StudentBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getFirstName());
			System.out.print("\t" + bean.getLastName());
			System.out.print("\t" + bean.getDob());
			System.out.print("\t" + bean.getGender());
			System.out.print("\t" + bean.getMobileNo());
			System.out.print("\t" + bean.getEmail());
			System.out.print("\t" + bean.getCollegeId());
			System.out.print("\t" + bean.getCollegeName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());

		}

	}

	private static void testFindByEmail() throws Exception {

		StudentModel model = new StudentModel();

		StudentBean bean = model.findByEmail("Raj@gmail.com");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t " + bean.getFirstName());
			System.out.print("\t " + bean.getLastName());
			System.out.print("\t " + bean.getDob());
			System.out.print("\t " + bean.getGender());
			System.out.print("\t " + bean.getMobileNo());
			System.out.print("\t " + bean.getEmail());
			System.out.print("\t " + bean.getCollegeId());
			System.out.print("\t " + bean.getCollegeName());
			System.out.print("\t " + bean.getCreatedBy());
			System.out.print("\t " + bean.getModifiedBy());
			System.out.print("\t " + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}

	private static void testFindByPK() throws Exception {
		StudentModel model = new StudentModel();

		StudentBean bean = model.findByPk(1);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t " + bean.getFirstName());
			System.out.print("\t " + bean.getLastName());
			System.out.print("\t " + bean.getDob());
			System.out.print("\t " + bean.getGender());
			System.out.print("\t " + bean.getMobileNo());
			System.out.print("\t " + bean.getEmail());
			System.out.print("\t " + bean.getCollegeId());
			System.out.print("\t " + bean.getCollegeName());
			System.out.print("\t " + bean.getCreatedBy());
			System.out.print("\t " + bean.getModifiedBy());
			System.out.print("\t " + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}

	private static void testDelete() throws Exception {

		StudentBean bean = new StudentBean();
		StudentModel model = new StudentModel();
		model.delete(2);

	}

	private static void testUpdate() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StudentBean bean = new StudentBean();
		StudentModel model = new StudentModel();
		bean.setId(1);
		bean.setFirstName("Raj");
		bean.setLastName("Malviya");
		bean.setDob(sdf.parse("2001-02-02"));
		bean.setGender("male");
		bean.setMobileNo("7648677688");
		bean.setEmail("Raj@gmail.com");
		bean.setCollegeId(3);

		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.update(bean);
	}

	private static void testAdd() throws ParseException, ApplicationException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StudentBean bean = new StudentBean();
		StudentModel model = new StudentModel();

		bean.setFirstName("Rahul");
		bean.setLastName("Malviya");
		bean.setDob(sdf.parse("2002-02-02"));
		bean.setGender("male");
		bean.setMobileNo("0987654321");
		bean.setEmail("rahul@gmail.com");
		bean.setCollegeId(3);
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.add(bean);

	}

	private static void testNextPK() throws DatabaseException {
		CollegeModel model = new CollegeModel();
		int i = model.nextPK();

		System.out.println("Next pk " + i);

	}
}
