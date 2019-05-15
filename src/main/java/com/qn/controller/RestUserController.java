package com.qn.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.qn.model.DestServerConfig;
import com.qn.model.SrcServerConfig;
import com.qn.service.ConfigService;
import com.qn.service.PushManager;
import com.qn.task.SrcServerTask;
import com.qn.util.MethodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qn.model.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
@RestController
public class RestUserController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	PushManager pushManager;
	@Autowired
	ConfigService configService;
	@Autowired
    SrcServerTask taskService;

	@RequestMapping(value = "submit.do", method = RequestMethod.POST)
	public Object submitPushUrl(String ipcameraUrl, String videoSize, String bitrate, HttpServletRequest request) throws Exception {
	    String host = MethodUtil.getIPAddr(ipcameraUrl);
		if (host == null){
			return "摄像头推流地址的ip地址错误!";
		}
		System.out.println(ipcameraUrl);
        SrcServerConfig config = configService.getConfigInfo();
		config.setRtspUrl(ipcameraUrl);
		config.setBitrate(bitrate);
		config.setSize(videoSize);
		configService.saveConfigInfo(config);
		taskService.reStart();
		return "设置成功！";
	}
	@RequestMapping(value = "dest_submit.do", method = RequestMethod.POST)
	public Object submitPushUrl(String srcIpAddr,HttpServletRequest request) throws Exception {
		String host = MethodUtil.getIPAddr(srcIpAddr);
		if (host == null){
			return "ip地址格式错误";
		}
		System.out.println("id地址为:"+srcIpAddr);
		DestServerConfig config = configService.getDestConfigInfo();
		config.setSrcServerUrl(srcIpAddr);
		boolean result = configService.saveDestConfigInfo(config);
		if (result){
            return "设置成功！";
        }else{
            return "设置失败！";
        }
	}
	/**
	 * @Desc: 是否开启了服务，如果开启了就返回推流地址，否则返回空字符串
	 * @param: []
	 * @return: java.lang.String
	 * @date: 2019/3/11 上午11:08
	 */
	@RequestMapping("/get_push_addr")
	public String getPushAddr() {
        String ipAddr = MethodUtil.getLocalIpAddr();
        List<String> list = MethodUtil.processList("ffmpeg");
        if (list.size() > 0){
            //System.out.println("ffmpeg 已经开启 ！！！");
            String result = "rtmp://"+ipAddr+":1935/live/push";
            return result;
        }
        return "0";
	}


    @RequestMapping("/test")
    public String test() {
        String ipAddr = MethodUtil.getLocalIpAddr();
        return ipAddr;
    }




	@RequestMapping("/param/{uid}")
	public User getUserInfo(@PathVariable Integer uid) {
		logger.info("info nihao");

		return new User(uid,"张三",20,"中国广州");
	}
	@RequestMapping("/system")
	public List<User> system() {
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i <1; i++) {
			userList.add(new User(i,"张三"+i,20+i,"中国广州"));
		}
		System.out.println("ffmpeg 已经开启 ！！！");
		return userList;
	}
	@RequestMapping("/userlist")
	public List<User> listUserxx() {
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i <10; i++) {
			userList.add(new User(i,"张三"+i,20+i,"中国广州"));
		}
		logger.error("error nihao");
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
