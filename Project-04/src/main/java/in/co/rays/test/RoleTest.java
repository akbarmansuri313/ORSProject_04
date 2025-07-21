package in.co.rays.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.RoleBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.RoleModel;

public class RoleTest {

	public static void main(String[] args) throws Exception {

//		testNextPK();
		testAdd();
//		testUpdate();
//		testSearch();
//		testFindByName();
//		testFindByPK();
//		testDelete();
	}

	private static void testNextPK() throws ApplicationException {

		RoleModel model = new RoleModel();

		int i = model.nextPK();
		System.out.println("Next Pk " + i);

	}

	private static void testAdd() throws Exception {

		RoleModel model = new RoleModel();

		RoleBean bean = new RoleBean();
		bean.setName("Kiosk");
		bean.setDescription("Kiosk access application");
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);

	}

	public static void testDelete() throws Exception {
		
		RoleModel model = new RoleModel();

		model.delete(1);

	}

	public static void testUpdate() throws Exception {
		
		RoleBean bean = new RoleBean();
		RoleModel model = new RoleModel();

		bean.setId(1);
		bean.setName("Admin");
		bean.setDescription("Admin has access appilcation");
		bean.setCreatedBy("Admin@gmail.com");
		bean.setModifiedBy("Admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.update(bean);

	}

	private static void testSearch() throws ApplicationException {

		RoleModel model = new RoleModel();

		RoleBean bean = new RoleBean();

		List list = model.search(bean, 1, 4);

		Iterator it = list.iterator();

		while (it.hasNext()) {

			bean = (RoleBean) it.next();

			System.out.print(bean.getId());

			System.out.print("\t" + bean.getName());

			System.out.print("\t" + bean.getDescription());

			System.out.print("\t" + bean.getCreatedBy());

			System.out.print("\t" + bean.getModifiedBy());

			System.out.print("\t" + bean.getCreatedDatetime());

			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}

	public static void testFindByPK() throws Exception {
		RoleBean bean = new RoleBean();
		RoleModel model = new RoleModel();
		bean = model.findByPK(2);

		if (bean != null) {
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDatetime());
			System.out.println(bean.getModifiedDatetime());
		}
	}

	private static void testFindByName() throws Exception {

		RoleModel model = new RoleModel();
		RoleBean bean = model.FindByName("Kiosk");

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}
	}

}
