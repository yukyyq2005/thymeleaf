package com.ts.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ：youq
 * @date ：Created in 2019/3/7 20:24
 * @modified By：
 */
@Controller
public class FileController {
//    @RequestMapping(value = "/api/v")
//    public Object index(){
//        return "index";
//    }
    @RequestMapping(value = "/")
    public Object index() {
        return "index";
    }
}
