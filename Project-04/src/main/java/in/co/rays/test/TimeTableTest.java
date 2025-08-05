package in.co.rays.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.TimetableBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.TimetableModel;

public class TimeTableTest {

	public static void main(String[] args) throws Exception {

//		testNextPk();
//		testAdd();
//		testUpdate();
//		testDelete();
//		testFindByPk();
		testSearch();
//		checkByCouresName();
	}

	private static void checkByCouresName() throws ApplicationException, ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		TimetableBean bean = new TimetableBean();

		TimetableModel model = new TimetableModel();

		bean = model.checkByCourseName(1L, sdf.parse("2026-05-01 00:00:00"));

		if (bean != null) {

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());

		}

	}

	private static void testSearch() throws ApplicationException {

		TimetableBean bean = new TimetableBean();

		TimetableModel model = new TimetableModel();

		List list = model.search(bean, 1, 10);

		Iterator it = list.iterator();

		while (it.hasNext()) {

			bean = (TimetableBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());

		}

	}

	private static void testFindByPk() throws ApplicationException {

		TimetableModel model = new TimetableModel();

		TimetableBean bean = model.findByPk(1);

		if (bean != null) {

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getSemester());
			System.out.print("\t" + bean.getDescription());
			System.out.print("\t" + bean.getExamDate());
			System.out.print("\t" + bean.getExamTime());
			System.out.print("\t" + bean.getCourseId());
			System.out.print("\t" + bean.getCourseName());
			System.out.print("\t" + bean.getSubjectId());
			System.out.print("\t" + bean.getSubjectName());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}

	private static void testDelete() throws ApplicationException {
		TimetableModel model = new TimetableModel();

		TimetableBean bean = new TimetableBean();

		model.delete(bean);

		bean.setId(1);

	}

	private static void testUpdate() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		TimetableModel model = new TimetableModel();
		TimetableBean bean = new TimetableBean();

		bean.setSemester("4nd");
		bean.setDescription("B.Coms Student");
		bean.setExamDate(sdf.parse("2025-08-09"));
		bean.setExamTime("01:00");
		bean.setCourseId(1);
		bean.setSubjectId(1);
		bean.setCreatedBy("root");
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		bean.setId(1);

		model.update(bean);

	}

	private static void testAdd() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		TimetableBean bean = new TimetableBean();
		TimetableModel model = new TimetableModel();

		bean.setSemester("3rd");
		bean.setDescription("M.Tech Student");
		bean.setExamDate(sdf.parse("2025-09-09"));
		bean.setExamTime("01:00");
		bean.setCourseId(4);
		bean.setSubjectId(8);
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);

	}

	private static void testNextPk() throws Exception {

		TimetableModel model = new TimetableModel();

		int i = model.nextPk();

		System.out.println("Next pk " + i);

	}

}
