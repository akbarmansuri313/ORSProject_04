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

/**
 * The {@code StudentTest} class is a test harness to verify the CRUD operations
 * (Create, Read, Update, Delete) and utility methods provided by the
 * {@link StudentModel} and related model classes.
 * <p>
 * Each test method exercises a specific function of the {@code StudentModel},
 * such as adding a new student, updating details, deleting, searching, and
 * finding by primary key or email.
 * </p>
 *
 * @author Your Name
 * @version 1.0
 */
public class StudentTest {

    /**
     * The main method is the entry point for executing different test cases.
     * Uncomment the desired test method to run specific functionality.
     *
     * @param args command line arguments (not used)
     * @throws Exception if any operation fails
     */
    public static void main(String[] args) throws Exception {

//      testNextPK();
        testAdd();
//      testUpdate();
//      testDelete();
//      testFindByPK();
//      testFindByEmail();
//      testSearch();
    }

    /**
     * Tests the {@link StudentModel#search(StudentBean, int, int)} method by
     * fetching a list of students and printing their details.
     *
     * @throws Exception if search fails
     */
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

    /**
     * Tests the {@link StudentModel#findByEmail(String)} method by retrieving a
     * student using their email address.
     *
     * @throws Exception if retrieval fails
     */
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

    /**
     * Tests the {@link StudentModel#findByPk(long)} method by retrieving a
     * student using their primary key (ID).
     *
     * @throws Exception if retrieval fails
     */
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

    /**
     * Tests the {@link StudentModel#delete(StudentBean)} method by deleting a
     * student record based on their ID.
     *
     * @throws Exception if deletion fails
     */
    private static void testDelete() throws Exception {
        StudentBean bean = new StudentBean();
        StudentModel model = new StudentModel();

        bean.setId(2);
        model.delete(bean);
    }

    /**
     * Tests the {@link StudentModel#update(StudentBean)} method by updating an
     * existing student record with new details.
     *
     * @throws Exception if update fails
     */
    private static void testUpdate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        StudentBean bean = new StudentBean();
        StudentModel model = new StudentModel();

        bean.setId(13);
        bean.setFirstName("Raj");
        bean.setLastName("Malviya");
        bean.setDob(sdf.parse("2001-02-02"));
        bean.setGender("male");
        bean.setMobileNo("7648677688");
        bean.setEmail("Raji55@gmail.com");
        bean.setCollegeId(6);
//      bean.setCollegeName("MITS");
        bean.setCreatedBy("admin@gmail.com");
        bean.setModifiedBy("admin@gmail.com");
        bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
        bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

        model.update(bean);
    }

    /**
     * Tests the {@link StudentModel#add(StudentBean)} method by inserting a new
     * student record into the database.
     *
     * @throws Exception if insertion fails
     */
    private static void testAdd() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        StudentBean bean = new StudentBean();
        StudentModel model = new StudentModel();

        bean.setFirstName("chtean");
        bean.setLastName("Malviya");
        bean.setDob(sdf.parse("2002-02-02"));
        bean.setGender("male");
        bean.setMobileNo("0987654321");
        bean.setEmail("chet35@gmail.com");
        bean.setCollegeId(8);
        bean.setCreatedBy("admin@gmail.com");
        bean.setModifiedBy("admin@gmail.com");
        bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
        bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

        model.add(bean);
    }

    /**
     * Tests the {@link CollegeModel#nextPK()} method by retrieving the next
     * available primary key for a college record.
     *
     * @throws DatabaseException if primary key retrieval fails
     */
    private static void testNextPK() throws DatabaseException {
        CollegeModel model = new CollegeModel();
        int i = model.nextPK();
        System.out.println("Next pk " + i);
    }
}
