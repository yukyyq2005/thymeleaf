package com.qn.controller;

import java.util.ArrayList;
import java.util.List;

import com.qn.model.Config;
import com.qn.service.ConfigService;
import com.qn.service.ConfigService;
import com.qn.service.PushManager;
import com.qn.task.TaskService;
import com.qn.util.MethodUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.qn.model.User;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
@Controller
//@RequestMapping("/user3")
public class UserController {

	@Autowired
	ConfigService configService;
    @Autowired
    TaskService taskService;

    @RequestMapping(value = "/")
    public Object index() {
        return "index";
    }

    @RequestMapping(value = "submit.do", method = RequestMethod.POST)
    @ResponseBody
    public Object submitPushUrl(String ipcameraUrl, String videoSize, String bitrate, HttpServletRequest request) throws Exception {
        String host = MethodUtil.getIPAddr(ipcameraUrl);
        if (host == null){
            return "摄像头推流地址的ip地址错误";
        }
        System.out.println(ipcameraUrl);
        Config config = configService.getConfigInfo();
        config.setRtspUrl(ipcameraUrl);
        config.setBitrate(bitrate);
        config.setSize(videoSize);
        configService.saveConfigInfo(config);
        taskService.reStart();
        return "设置成功！";
    }

//	@RequestMapping("/{id}")
//	public String  getUser(@PathVariable Integer id,Model model) {
//
//		model.addAttribute("user",new User(id,"张三",20,"中国广州"));
//		return "detail";
//	}

//	@RequestMapping("/list")
//	public Object  listUser(Model model) {
//		List<User> userList = new ArrayList<User>();
//		for (int i = 0; i <10; i++) {
//			userList.add(new User(i,"张三"+i,20+i,"中国广州"));
//		}
//
//		model.addAttribute("users", userList);
//		return "list";
//	}
}
