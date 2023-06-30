//package oop.course.tools;
//
//import oop.course.entity.*;
//import oop.course.tools.implementations.*;
//import oop.course.tools.interfaces.*;
//
//public class TestTool {
//    public static void main(String[] args) {
//
//        String string = "{    \n\n\"name\" :    \"John\"," +
//                "\"surname\" :    \"Week\",\"password\" :    \"asfasfas\"    , \"email\":\"a@b.c\"," +
//                " \n \"name2\"  :  \"!John2\" , \"id\":\"123\"}";
//
//        Form form = new JsonForm(string);
//        String value = form.extractString("email");
//        System.out.println(String.format("[%s]", value));
//
//        System.out.println(new Customer(
//                new JsonForm(string)
//        ));
//    }
//}
