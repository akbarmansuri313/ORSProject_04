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

/**
 * The SubjectTest class is a test harness to validate the operations 
 * (CRUD + search) provided by the SubjectModel class.
 * 
 * It tests methods like add, delete, update, findByName, findByPk, and search
 * using the SubjectBean.
 * 
 * @author YourName
 */
public class SubjectTest {

    /**
     * The main method is the entry point for testing different operations
     * of SubjectModel by calling individual test methods.
     * 
     * @param args command line arguments
     * @throws Exception if any error occurs during the method execution
     */
    public static void main(String[] args) throws Exception {
//      testAdd();
//      testDelete();
//      testFindByName();
        testUpdate();
//      testFindByPk();
//      testSearch();
    }

    /**
     * Tests the add operation of SubjectModel.
     * Adds a new SubjectBean to the database.
     * 
     * @throws DuplicateRecordException if a subject with the same name already exists
     * @throws ApplicationException if any database/application error occurs
     * @throws RecordNotFoundException if related record is not found
     */
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

    /**
     * Tests the delete operation of SubjectModel.
     * Deletes a SubjectBean from the database.
     * 
     * @throws ApplicationException if any database/application error occurs
     */
    public static void testDelete() throws ApplicationException {
        SubjectModel model = new SubjectModel();
        SubjectBean bean = new SubjectBean();

        model.delete(bean);
        bean.setId(2);
    }

    /**
     * Tests the findByName operation of SubjectModel.
     * Fetches a SubjectBean from the database using subject name.
     * 
     * @throws ApplicationException if any database/application error occurs
     * @throws RecordNotFoundException if no record is found with the given name
     */
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

    /**
     * Tests the update operation of SubjectModel.
     * Updates an existing SubjectBean in the database.
     * 
     * @throws ApplicationException if any database/application error occurs
     * @throws DuplicateRecordException if a subject with the same name already exists
     * @throws RecordNotFoundException if the record to update does not exist
     */
    public static void testUpdate() throws ApplicationException, DuplicateRecordException, RecordNotFoundException {
        SubjectBean bean = new SubjectBean();
        SubjectModel model = new SubjectModel();

        bean.setId(6);
        bean.setName("ASC");
        bean.setCourseId(8);
//      bean.setCourseName("123");
        bean.setDescription("programing");
        bean.setCreatedBy("admin");
        bean.setModifiedBy("admin");
        bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
        bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

        model.update(bean);
    }

    /**
     * Tests the findByPk operation of SubjectModel.
     * Fetches a SubjectBean using its primary key (ID).
     * 
     * @throws ApplicationException if any database/application error occurs
     */
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

    /**
     * Tests the search operation of SubjectModel.
     * Fetches a list of SubjectBeans based on search criteria.
     * 
     * @throws DatabaseException if a database error occurs
     * @throws ApplicationException if any application-level error occurs
     */
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
