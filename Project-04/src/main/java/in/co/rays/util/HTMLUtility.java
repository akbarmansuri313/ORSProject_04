package in.co.rays.util;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.DropdownListBean;
import in.co.rays.model.RoleModel;
import in.co.rays.model.UserModel;

/**
 * HTMLUtility is a helper class for generating HTML form elements dynamically.
 * <p>
 * It provides methods to create HTML select (dropdown) elements using either a HashMap or a List of 
 * {@link DropdownListBean} objects. It also includes test methods to demonstrate the HTML generation.
 * </p>
 * 
 * @author Akbar
 * @version 1.0
 */
public class HTMLUtility {

    /**
     * Generates an HTML select element from a HashMap.
     * 
     * @param name        the name attribute of the select element
     * @param selectedVal the value to be pre-selected in the dropdown
     * @param map         a HashMap containing key-value pairs for option values and display text
     * @return HTML string of the select element
     */
    public static String getList(String name, String selectedVal, HashMap<String, String> map) {

        StringBuffer sb = new StringBuffer(
                "<select style=\"width: 170px;text-align-last: center;\"; class='form-control' name='" + name + "'>");

        sb.append("\n<option selected value=''>-------------Select-------------</option>");

        Set<String> keys = map.keySet();
        String val = null;

        for (String key : keys) {
            val = map.get(key);
            if (key.trim().equals(selectedVal)) {
                sb.append("\n<option selected value='" + key + "'>" + val + "</option>");
            } else {
                sb.append("\n<option value='" + key + "'>" + val + "</option>");
            }
        }
        sb.append("\n</select>");
        return sb.toString();
    }

    /**
     * Generates an HTML select element from a List of {@link DropdownListBean}.
     * 
     * @param name        the name attribute of the select element
     * @param selectedVal the value to be pre-selected in the dropdown
     * @param list        a List of {@link DropdownListBean} objects containing key-value pairs
     * @return HTML string of the select element
     */
    public static String getList(String name, String selectedVal, List list) {

        List<DropdownListBean> dd = (List<DropdownListBean>) list;

        StringBuffer sb = new StringBuffer("<select style=\"width: 170px;text-align-last: center;\"; "
                + "class='form-control' name='" + name + "'>");

        sb.append("\n<option selected value=''>-------------Select-------------</option>");

        String key = null;
        String val = null;

        for (DropdownListBean obj : dd) {
            key = obj.getKey();
            val = obj.getValue();

            if (key.trim().equals(selectedVal)) {
                sb.append("\n<option selected value='" + key + "'>" + val + "</option>");
            } else {
                sb.append("\n<option value='" + key + "'>" + val + "</option>");
            }
        }
        sb.append("\n</select>");
        return sb.toString();
    }

    /**
     * Test method to demonstrate generating HTML select element from a HashMap.
     */
    public static void testGetListByMap() {

        HashMap<String, String> map = new HashMap<>();
        map.put("male", "male");
        map.put("female", "female");

        String selectedValue = "null";
        String htmlSelectFromMap = HTMLUtility.getList("gender", selectedValue, map);

        System.out.println(htmlSelectFromMap);
    }

    /**
     * Test method to demonstrate generating HTML select element from a List of {@link DropdownListBean}.
     * 
     * @throws Exception if any error occurs while retrieving the list from the model
     */
    public static void testGetListByList() throws Exception {

        RoleModel model = new RoleModel();

        List<DropdownListBean> list = model.list();

        String selectedValue = null;

        String htmlSelectFromList = HTMLUtility.getList("roleId", selectedValue, list);

        System.out.println(htmlSelectFromList);
    }

    /**
     * Main method to test the HTML generation methods.
     * 
     * @param args command-line arguments
     * @throws Exception if any error occurs during testing
     */
    public static void main(String[] args) throws Exception {

//        testGetListByMap();

        testGetListByList();

    }

}
