package in.co.rays.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.model.SubjectModel;

public class SubjectTest {

	public static void main(String[] args) throws Exception {
//		testAdd();
//		 testDelete();
//		testFindByName();
		testUpdate();
//		testFindByPk();
//		testSearch();

	}

	public static void testAdd() throws DuplicateRecordException, ApplicationException, RecordNotFoundException {

		SubjectBean bean = new SubjectBean();
		SubjectModel model = new SubjectModel();

		bean.setName("Pathan");

		bean.setCourseId(5);
		

		bean.setDescription("Data Structure");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);

	}

	public static void testDelete() throws ApplicationException {

		SubjectModel model = new SubjectModel();

		SubjectBean bean = new SubjectBean();

		model.delete(bean);

		bean.setId(2);

	}

	public static void testFindByName() throws ApplicationException, RecordNotFoundException {

		SubjectModel model = new SubjectModel();
		SubjectBean bean = model.findByName("BCA");

		System.out.print(bean.getId());
		System.out.print("\t " + bean.getName());
		System.out.print("\t " + bean.getDescription());
		System.out.print("\t " + bean.getCourseId());
		System.out.print("\t " + bean.getCourseName());
		System.out.print("\t " + bean.getCreatedBy());

		System.out.print("\t " + bean.getModifiedBy());
		System.out.print("\t " + bean.getCreatedDatetime());
		System.out.println("\t " + bean.getModifiedDatetime());

	}

	public static void testUpdate() throws ApplicationException, DuplicateRecordException, RecordNotFoundException {

		SubjectBean bean = new SubjectBean();
		SubjectModel model = new SubjectModel();

		bean.setId(6);
		bean.setName("ASC");
		bean.setCourseId(8);
//		bean.setCourseName("123");
		bean.setDescription("programing");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.update(bean);

	}

	public static void testFindByPk() throws ApplicationException {

		SubjectModel model = new SubjectModel();
		SubjectBean bean = model.findByPk(1);

		if (bean != null) {

			System.out.print(bean.getId());
			System.out.print("\t " + bean.getName());
			System.out.print("\t " + bean.getDescription());
			System.out.print("\t " + bean.getCourseId());
			System.out.print("\t " + bean.getCourseName());
			System.out.print("\t " + bean.getCreatedBy());
			System.out.print("\t " + bean.getCreatedDatetime());
			System.out.print("\t " + bean.getModifiedBy());
			System.out.println("\t " + bean.getModifiedDatetime());

		}
	}

	public static void testSearch() throws DatabaseException, ApplicationException {

		SubjectBean bean = new SubjectBean();

		SubjectModel model = new SubjectModel();

		List list = new ArrayList();

		list = model.search(null, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {

			bean = (SubjectBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t " + bean.getName());
			System.out.print("\t " + bean.getDescription());
			System.out.print("\t " + bean.getCourseId());
			System.out.print("\t " + bean.getCourseName());
			System.out.print("\t " + bean.getCreatedBy());
			System.out.print("\t " + bean.getModifiedBy());
			System.out.print("\t " + bean.getCreatedDatetime());
			System.out.println("\t " + bean.getModifiedDatetime());

		}

	}

}
