package com.dongdong.internship.Controller;

import com.dongdong.internship.bean.ResultInfo;
import com.dongdong.internship.bean.Student;
import com.dongdong.internship.mapper.StudentMapper;
import com.dongdong.internship.util.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/student")
@SessionAttributes(value= {"student","username"},types=
        {com.dongdong.internship.bean.Student.class,String.class})
public class StudentController {



    @RequestMapping("/index")
    public String  studentIndex(){

        return "student/studentIndex";
     }
    @RequestMapping("/registPage")
    public String  studentRegist(){

        return "student/studentRegist";
    }

    @RequestMapping("/loginPage")
    public String  studentLoginPage(){

        return "student/studentLogin";
    }




    @Autowired
    private StudentMapper studentMapper;


    @RequestMapping("/loginStudent")
    public void studentLogin(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sname = request.getParameter("sname");
        String password = request.getParameter("password");

       Student student = studentMapper.queryStudentByNamePassword(sname,password);

       if(student ==null){
           ResultUtil.feedBack(response,"Wrong username or password ",null,false);

       }
       else {
           System.out.println("登录成功");
           ResultUtil.feedBack(response,"find one ",student,true);
           model.addAttribute("student",student);
           model.addAttribute("username",student.getSname());

       }
    }


    @RequestMapping("/queryStudent")
    @ResponseBody
    public List<Student> queryStudent(){
        List<Student> users = studentMapper.queryStudentList();
        return users;
    }


    @RequestMapping("/registStudent")
    public void insert(HttpServletRequest request , HttpServletResponse response) throws IOException {
        Student confirmstudent = studentMapper.queryStudentByName(request.getParameter("sname"));
        if (confirmstudent != null) {
            ResultUtil.feedBack(response,"The user has existes \r\n Please try another username \r\n Or try to find your account by your email",null,false);
        }
        else {
            try {
                ResultInfo resultInfo = new ResultInfo();
                Student student = new Student();
                student.setSname(request.getParameter("sname"));
                student.setSupname(request.getParameter("supname"));
                student.setSmail(request.getParameter("supname"));
                student.setPassword(request.getParameter("password"));
                student.setSage(Integer.parseInt(request.getParameter("sage")));
                student.setSchool(request.getParameter("school"));
                studentMapper.registStudent(student);
                resultInfo.setFlag(true);
                resultInfo.setErrorMsg("注册成功");
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(resultInfo);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(json);
                System.out.println(studentMapper.queryStudentList());
            } catch (NumberFormatException ex) {
                System.out.println("here");
                String errorMsg = "";
                errorMsg += ex.getMessage() + "\r\n";
                StackTraceElement[] trace = ex.getStackTrace();
                for (StackTraceElement s : trace) {
                    errorMsg += "\tat " + s + "\r\n";
                }
                ResultUtil.feedBack(response, errorMsg, null, false);

            }
        }
    }

    @RequestMapping("/searchByName")
    @ResponseBody
    public String search(){
        Student student = new Student();

        String sname = "Abo";
        student = studentMapper.queryStudentByName(sname);

        return student.toString();
    }





/*
    ResultInfo resultInfo = new ResultInfo();
    String check = request.getParameter("check");
    HttpSession session = request.getSession();
    String checkFromserver = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");


        if (checkFromserver!=null&&check.equalsIgnoreCase(checkFromserver)){

        Map<String, String[]> users = request.getParameterMap();
        System.out.println("这里是registerservlet");
        User user = new User();


        try {
            BeanUtils.populate(user,users);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        UserService userService = new UserServiceImpl();
        boolean result = userService.regist(user);
        //System.out.println(result);


        if(result==true){
            System.out.println("注册成功");
            resultInfo.setFlag(true);
            resultInfo.setErrorMsg("注册成功");
        }
        else{
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败，用户名存在");
        }
        ObjectMapper mapper = new ObjectMapper();
        String  json = mapper.writeValueAsString(resultInfo);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

        else{

        resultInfo.setFlag(false);
        resultInfo.setErrorMsg("验证码错误！");
        ObjectMapper mapper = new ObjectMapper();
        String  json = mapper.writeValueAsString(resultInfo);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }*/




 public static Student makeStudnt(){
       Student student = new Student();
        student.setSage(26);
        student.setSname("ShengdongYan");
        student.setSchool("University of Birmingham");
        student.setPassword("Yanshengdong123");
        student.setSupname("Mohammed");
        student.setSmail("sxy777@student.bham.ac.uk");
        System.out.println("插入成功");
        return student;

 }
}
