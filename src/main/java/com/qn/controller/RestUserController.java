package com.qn.controller;

import java.util.ArrayList;
import java.util.List;

import com.qn.service.ConfigService;
import com.qn.service.PushManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qn.model.User;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
@RestController
public class RestUserController {

	@Autowired
	ConfigService configService;
	@Autowired
	PushManager pushManager;

	@RequestMapping("/{info}")
	public User getUserInfo(@PathVariable Integer id) {
		return new User(id,"张三",20,"中国广州");
	}
	
	@RequestMapping("/userlist")
	public List<User> listUserxx() {
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i <10; i++) {
			userList.add(new User(i,"张三"+i,20+i,"中国广州"));
		}
		return userList;
	}
	@RequestMapping("/kill")
	public Object killProcess(){
		pushManager.killProcess("ffmpeg");
		return "OK";
	}
	@RequestMapping("/close")
	public Object closePush(){
		pushManager.closePush();
		return "OK";
	}
	@RequestMapping("/open")
	public Object openPush(){
		pushManager.openPush();
		return "OK";
	}
}
