package com.qn.controller;


import com.qn.model.DestServerConfig;
import com.qn.model.SrcServerConfig;
import com.qn.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class UserController {

    @Autowired
    ConfigService configService;

    @Value("${server.type}")
    private String serverType;

    @RequestMapping(value = "/")
    public Object index(Model model) {
        if (serverType.equals("desc")){
            //收流服务器
            DestServerConfig config = configService.getDestConfigInfo();
            model.addAttribute("srcipaddr", config.getSrcServerUrl());
            return "destindex";
        }
        //推流服务器
        SrcServerConfig config = configService.getConfigInfo();
        model.addAttribute("cameraurl", config.getRtspUrl());
        model.addAttribute("videosize", config.getSize());
        model.addAttribute("bitrate", config.getBitrate());
        return "index";
    }
}
