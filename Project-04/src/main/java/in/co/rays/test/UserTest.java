package in.co.rays.test;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.UserModel;
/**
* @author 
*/
public class UserTest {

   /**
    * Main method to call different test methods of UserModel.
    * 
    * @param args command line arguments
    * @throws Exception if any error occurs during test execution
    */
   public static void main(String[] args) throws Exception {
//     testAuthenticate();
//     testSearch();
       testNextPK();
//     testUpdate();
//     testAdd();
//     testDelete();
//     testFindByPK();
//     testFindByLogin();
   }

   /**
    * Tests the authenticate method of UserModel.
    * Verifies user login credentials.
    * 
    * @throws ApplicationException if database or application error occurs
    */
   private static void testAuthenticate() throws ApplicationException {
       UserModel model = new UserModel();
       UserBean bean = model.authenticate("akbar@gmail.com", "1234");
       if (bean != null) {
           System.out.print(bean.getId());
           System.out.print(" \t  " + bean.getFirstName());
           System.out.print(" \t  " + bean.getLastName());
           System.out.print(" \t  " + bean.getLogin());
           System.out.print(" \t  " + bean.getPassword());
           System.out.print(" \t  " + bean.getDob());
           System.out.print(" \t  " + bean.getMobileNo());
           System.out.print(" \t   " + bean.getId());
           System.out.print(" \t  " + bean.getGender());
           System.out.print(" \t " + bean.getCreatedBy());
           System.out.print(" \t " + bean.getModifiedBy());
           System.out.print(" \t " + bean.getCreatedDatetime());
           System.out.print("\t  " + bean.getCreatedDatetime());
       }
   }

   /**
    * Tests the findByLogin method of UserModel.
    * Retrieves user details based on login id.
    * 
    * @throws ApplicationException if database or application error occurs
    */
   private static void testFindByLogin() throws ApplicationException {
       UserModel model = new UserModel();
       UserBean bean = model.findByLogin("akbar@gmail.com");

       if (bean != null) {
           System.out.print(bean.getId());
           System.out.print(" \t  " + bean.getFirstName());
           System.out.print(" \t  " + bean.getLastName());
           System.out.print(" \t  " + bean.getLogin());
           System.out.print(" \t  " + bean.getPassword());
           System.out.print(" \t  " + bean.getDob());
           System.out.print(" \t  " + bean.getMobileNo());
           System.out.print(" \t   " + bean.getId());
           System.out.print(" \t  " + bean.getGender());
           System.out.print(" \t " + bean.getCreatedBy());
           System.out.print(" \t " + bean.getModifiedBy());
           System.out.print(" \t " + bean.getCreatedDatetime());
           System.out.print("\t  " + bean.getCreatedDatetime());
       }
   }

   /**
    * Tests the findByPk method of UserModel.
    * Finds a user by primary key.
    * 
    * @throws ApplicationException if database or application error occurs
    */
   private static void testFindByPK() throws ApplicationException {
       UserBean bean = new UserBean();
       UserModel model = new UserModel();
       bean = model.findByPk(1);

       if (bean != null) {
           System.out.print(bean.getId());
           System.out.print(" \t  " + bean.getFirstName());
           System.out.print(" \t  " + bean.getLastName());
           System.out.print(" \t  " + bean.getLogin());
           System.out.print(" \t  " + bean.getPassword());
           System.out.print(" \t  " + bean.getDob());
           System.out.print(" \t  " + bean.getMobileNo());
           System.out.print(" \t   " + bean.getId());
           System.out.print(" \t  " + bean.getGender());
           System.out.print(" \t " + bean.getCreatedBy());
           System.out.print(" \t " + bean.getModifiedBy());
           System.out.print(" \t " + bean.getCreatedDatetime());
           System.out.print("\t  " + bean.getCreatedDatetime());
       }
   }

   /**
    * Tests the search method of UserModel.
    * Searches users based on given criteria.
    * 
    * @throws ApplicationException if database or application error occurs
    */
   private static void testSearch() throws ApplicationException {
       UserModel model = new UserModel();
       UserBean bean = new UserBean();

//     bean.setFirstName("Arjun");
       bean.setRoleId(1);
       bean.setLogin("arjun.patel@gmail.com");

       List list = model.search(bean, 1, 10);
       Iterator it = list.iterator();

       while (it.hasNext()) {
           bean = (UserBean) it.next();
           System.out.print(bean.getId());
           System.out.print("\t" + bean.getFirstName());
           System.out.print("\t" + bean.getLastName());
           System.out.print("\t" + bean.getLogin());
           System.out.print("\t" + bean.getPassword());
           System.out.print("\t" + bean.getDob());
           System.out.print("\t" + bean.getMobileNo());
           System.out.print("\t" + bean.getRoleId());
           System.out.print("\t" + bean.getGender());
           System.out.print("\t" + bean.getCreatedBy());
           System.out.print("\t" + bean.getModifiedBy());
           System.out.print("\t" + bean.getCreatedDatetime());
           System.out.println("\t " + bean.getCreatedDatetime());
       }
   }

   /**
    * Tests the delete method of UserModel.
    * Deletes a user based on user ID.
    * 
    * @throws ApplicationException if database or application error occurs
    */
   private static void testDelete() throws ApplicationException {
       UserModel model = new UserModel();
       UserBean bean = new UserBean();
       bean.setId(31);
       model.delete(bean);
   }

   /**
    * Tests the update method of UserModel.
    * Updates user details in the database.
    * 
    * @throws Exception if parsing or database error occurs
    */
   private static void testUpdate() throws Exception {
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       UserBean bean = new UserBean();
       UserModel model = new UserModel();

       bean.setId(2);
       bean.setFirstName("Shakir");
       bean.setLastName("Khan");
       bean.setDob(sdf.parse("2001-07-01"));
       bean.setLogin("Shakir@gmail.com");
       bean.setPassword("765");
       bean.setGender("Male");
       bean.setRoleId(1);
       bean.setMobileNo("987654321");
       bean.setCreatedBy("root");
       bean.setModifiedBy("root");
       bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
       bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

       model.update(bean);
   }

   /**
    * Tests the add method of UserModel.
    * Adds a new user record in the database.
    * 
    * @throws Exception if parsing or database error occurs
    */
   private static void testAdd() throws Exception {
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       UserBean bean = new UserBean();

       bean.setFirstName("saad");
       bean.setLastName("Mansuri");
       bean.setDob(sdf.parse("2025-07-01"));
       bean.setLogin("saad123@gmail.com");
       bean.setPassword("1234");
       bean.setGender("Male");
       bean.setMobileNo("9755530652");
       bean.setRoleId(2);
       bean.setCreatedBy("root");
       bean.setModifiedBy("root");
       bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
       bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

       UserModel model = new UserModel();
       model.add(bean);
   }

   /**
    * Tests the nextPK method of UserModel.
    * Retrieves the next primary key value.
    * 
    * @throws Exception if database error occurs
    */
   private static void testNextPK() throws Exception {
       UserModel model = new UserModel();
       int i = model.nextPK();
       System.out.println("Next PK " + i);
   }
}