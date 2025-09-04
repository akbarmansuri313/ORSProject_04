package in.co.rays.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.FacultyBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.FacultyModel;

/**
 * Test class for FacultyModel.
 * <p>
 * This class contains test methods to perform CRUD operations 
 * (Add, Update, Delete, Find, Search) and utility methods (NextPk, FindByEmail) 
 * on Faculty entities using FacultyModel and FacultyBean.
 * </p>
 */
public class FacultyTest {

    /**
     * Main method to run specific test cases.
     *
     * @param args command-line arguments
     * @throws Exception if any exception occurs
     */
    public static void main(String[] args) throws Exception {
//      testnextPk();
//      testAdd();
//      testUpdate();
//      testFindByPk();
//      testFindByEmail();
        testSearch();
//      testDelete();
    }

    /**
     * Tests deleting a Faculty record by ID.
     *
     * @throws ApplicationException if application-level error occurs
     */
    private static void testDelete() throws ApplicationException {
        FacultyModel model = new FacultyModel();
        FacultyBean bean  = new FacultyBean();

        model.delete(bean);
        bean.setId(2);
    }

    /**
     * Tests updating an existing Faculty record.
     *
     * @throws Exception if any parsing or application-level exception occurs
     */
    private static void testUpdate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        FacultyBean bean = new FacultyBean();
        FacultyModel model = new FacultyModel();

        bean.setId(10);
        bean.setFirstName("Charan");
        bean.setLastName("Choudhary");
        bean.setDob(sdf.parse("1999-09-08"));
        bean.setGender("Male");
        bean.setMobileNo("9876676878");
        bean.setEmail("charan@gmail.com");
        bean.setCollegeId(8);
        bean.setCourseId(8);
        bean.setSubjectId(8);
        bean.setCreatedBy("root");
        bean.setModifiedBy("root");
        bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
        bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

        model.update(bean);
    }

    /**
     * Tests adding a new Faculty record.
     *
     * @throws Exception if any parsing or application-level exception occurs
     */
    private static void testAdd() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        FacultyBean bean = new FacultyBean();
        FacultyModel model = new FacultyModel();

        bean.setFirstName("Shaad");
        bean.setLastName("Khan");
        bean.setDob(sdf.parse("1985-09-09"));
        bean.setGender("Male");
        bean.setMobileNo("9875676226");
        bean.setEmail("shaad@gmail.com");
        bean.setCollegeId(3);
        bean.setCourseId(9);
        bean.setSubjectId(8);
        bean.setCreatedBy("admin");
        bean.setModifiedBy("admin");
        bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
        bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

        model.add(bean);
    }

    /**
     * Tests retrieval of the next primary key from FacultyModel.
     *
     * @throws Exception if any application-level error occurs
     */
    private static void testnextPk() throws Exception {
        FacultyModel model = new FacultyModel();
        int i = model.nextPk();
        System.out.println("Next Pk " + i);
    }

    /**
     * Tests finding a Faculty record by its primary key.
     *
     * @throws ApplicationException if application-level error occurs
     */
    private static void testFindByPk() throws ApplicationException {
        FacultyModel model = new FacultyModel();
        FacultyBean bean = model.findByPk(10);

        if (bean != null) {
            System.out.print(bean.getId());
            System.out.print("\t" + bean.getFirstName());
            System.out.print("\t" + bean.getLastName());
            System.out.print("\t" + bean.getDob());
            System.out.print("\t" + bean.getGender());
            System.out.print("\t" + bean.getMobileNo());
            System.out.print("\t" + bean.getEmail());
            System.out.print("\t" + bean.getCollegeId());
            System.out.print("\t" + bean.getCollegeName());
            System.out.print("\t" + bean.getCourseId());
            System.out.print("\t" + bean.getCourseName());
            System.out.print("\t" + bean.getSubjectId());
            System.out.print("\t" + bean.getSubjectName());
            System.out.print("\t" + bean.getCreatedBy());
            System.out.print("\t" + bean.getModifiedBy());
            System.out.print("\t" + bean.getCreatedDatetime());
            System.out.println("\t" + bean.getModifiedDatetime());
        } else {
            System.out.println("id not found");
        }
    }

    /**
     * Tests finding a Faculty record by email.
     *
     * @throws ApplicationException if application-level error occurs
     */
    private static void testFindByEmail() throws ApplicationException {
        FacultyModel model = new FacultyModel();
        FacultyBean bean = model.findByEmail("charan@gmail.com");

        if (bean != null) {
            System.out.print(bean.getId());
            System.out.print("\t" + bean.getFirstName());
            System.out.print("\t" + bean.getLastName());
            System.out.print("\t" + bean.getDob());
            System.out.print("\t" + bean.getGender());
            System.out.print("\t" + bean.getMobileNo());
            System.out.print("\t" + bean.getEmail());
            System.out.print("\t" + bean.getCollegeId());
            System.out.print("\t" + bean.getCollegeName());
            System.out.print("\t" + bean.getCourseId());
            System.out.print("\t" + bean.getCourseName());
            System.out.print("\t" + bean.getSubjectId());
            System.out.print("\t" + bean.getSubjectName());
            System.out.print("\t" + bean.getCreatedBy());
            System.out.print("\t" + bean.getModifiedBy());
            System.out.print("\t" + bean.getCreatedDatetime());
            System.out.println("\t" + bean.getModifiedDatetime());
        } else {
            System.out.println("id not found");
        }
    }

    /**
     * Tests searching Faculty records with pagination.
     *
     * @throws ApplicationException if application-level error occurs
     */
    private static void testSearch() throws ApplicationException {
        FacultyModel model = new FacultyModel();
        FacultyBean bean = new FacultyBean();

        List list = model.search(bean, 1, 10);
        Iterator it = list.iterator();

        while (it.hasNext()) {
            bean = (FacultyBean) it.next();

            System.out.print(bean.getId());
            System.out.print("\t" + bean.getFirstName());
            System.out.print("\t" + bean.getLastName());
            System.out.print("\t" + bean.getDob());
            System.out.print("\t" + bean.getGender());
            System.out.print("\t" + bean.getMobileNo());
            System.out.print("\t" + bean.getEmail());
            System.out.print("\t" + bean.getCollegeId());
            System.out.print("\t" + bean.getCollegeName());
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
}
