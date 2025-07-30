package in.co.rays.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.MarksheetBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.MarksheetModel;

public class MarkSheetTest {

	public static void main(String[] args) throws Exception {

//		testNextPK();
//		testAdd();
//		testUpdate();
//		testDelete();
//		findByPk();
		testSearch();
//		testRollNo();
	}

	private static void testRollNo() throws ApplicationException {

		MarksheetModel model = new MarksheetModel();

		MarksheetBean bean = model.findByRollNo("321");

		if (bean != null) {

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getRollNo());
			System.out.print("\t" + bean.getStudentId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getPhysics());
			System.out.print("\t" + bean.getChemistry());
			System.out.print("\t" + bean.getMaths());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}

	private static void testSearch() throws ApplicationException {

		MarksheetBean bean = new MarksheetBean();

		MarksheetModel model = new MarksheetModel();
		
//		bean.setName("Arjun");
		
		bean.setRollNo("BE204");

		List list = model.search(bean, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {

			bean = (MarksheetBean) it.next();
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getRollNo());
			System.out.print("\t" + bean.getStudentId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getPhysics());
			System.out.print("\t" + bean.getChemistry());
			System.out.print("\t" + bean.getMaths());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}

	private static void findByPk() throws ApplicationException {

		MarksheetModel model = new MarksheetModel();

		MarksheetBean bean = model.findByPk(1);

		if (bean != null) {

			System.out.print(bean.getId());

			System.out.print("\t" + bean.getRollNo());
			System.out.print("\t" + bean.getStudentId());
			System.out.print("\t" + bean.getName());
			System.out.print("\t" + bean.getPhysics());
			System.out.print("\t" + bean.getChemistry());
			System.out.print("\t" + bean.getMaths());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}

	private static void testDelete() throws ApplicationException {

		MarksheetModel model = new MarksheetModel();
		
		MarksheetBean bean = new MarksheetBean();
		
		bean.setId(2);
		
		model.delete(bean);

	}

	private static void testUpdate() throws Exception {
		MarksheetBean bean = new MarksheetBean();

		MarksheetModel model = new MarksheetModel();

		bean.setId(1);
		bean.setRollNo("BE321");
		bean.setStudentId(1);
		bean.setPhysics(67);
		bean.setChemistry(99);
		bean.setMaths(77);
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.update(bean);

	}

	private static void testAdd() throws Exception {
		MarksheetBean bean = new MarksheetBean();
		MarksheetModel model = new MarksheetModel();

		bean.setRollNo("211");
		bean.setStudentId(5);
//		bean.setName("CS");
		bean.setPhysics(67);
		bean.setChemistry(78);
		bean.setMaths(45);
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);

	}

	private static void testNextPK() throws Exception {

		MarksheetBean bean = new MarksheetBean();
		MarksheetModel model = new MarksheetModel();

		int i = model.nextPk();

		System.out.println("Next pk is " + i);

	}

}
