package com.qn.impl;

import com.qn.model.SrcServerConfig;
import com.qn.service.ConfigService;
import com.qn.service.PushManager;
import com.qn.util.MethodUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
@Service
public class PushManagerImpl implements PushManager {

    @Autowired
    ConfigService configService;

    /**
     * @Desc: 开始运行shell脚本推流
     * @param: [cmd]
     * @return: void
     * @date: 2019/3/7 下午5:41
     */
    @Override
    public void startPush(String cmd) {
        //runCmd("/usr/local/youq/ffmpeg/start.sh");
        //MethodUtil.runCmd(cmd);
        try {
            MethodUtil.runCommand(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @Desc: 手动打开推流状态，只有打开了才能执行推流任务
     * @param: []
     * @return: void
     * @date: 2019/3/8 下午1:57
     */
    @Override
    public void openPush() {
        SrcServerConfig config = configService.getConfigInfo();
        if (config != null){
            config.setClosePush(false);
            configService.saveConfigInfo(config);
        }
    }
    /**
     * @Desc: 手动关闭推流状态
     * @param: []
     * @return: void
     * @date: 2019/3/8 下午1:49
     */
    @Override
    public void closePush() {
        SrcServerConfig config = configService.getConfigInfo();
        if (config != null){
            config.setClosePush(true);
            configService.saveConfigInfo(config);
        }
        this.killProcess("ffmpeg");
    }
    /**
     * @Desc: 杀死进程
     * @param: [command]
     * @return: java.lang.String
     * @date: 2019/3/8 下午1:54
     */
    @Override
    public void killProcess(String command) {
        try {
            List<String> list = MethodUtil.processList(command);
            for(String pid : list) {
                System.out.println("kill process pid： " + pid);
                MethodUtil.closeLinuxProcess(pid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






