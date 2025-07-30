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

public class CourseTest {

	public static void main(String[] args) throws Exception {
//		testNextPk();
//		testAdd();
//		 testDelete();
//		testFindByName();
//		 testFindByPk();
//		 testUpdate();
		testSearch();

	}

	private static void testNextPk() throws ApplicationException, DatabaseException {

		CourseModel model = new CourseModel();

		int i = model.nextPk();

		System.out.println("Next Pk " + i);

	}

	private static void testAdd() throws DuplicateRecordException, ApplicationException {

		CourseBean bean = new CourseBean();
		CourseModel model = new CourseModel();

//		bean.setId(2);
		bean.setName("M.Ttech");
		bean.setDescription("Engineering");
		bean.setDuration("4 Year");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);

	}

	private static void testDelete() throws ApplicationException {

		CourseBean bean = new CourseBean();
		CourseModel model = new CourseModel();

		model.delete(bean);
		
		bean.setId(2);
	}

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
