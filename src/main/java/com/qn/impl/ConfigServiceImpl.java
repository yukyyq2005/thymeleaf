package com.qn.impl;

import com.qn.model.DestServerConfig;
import com.qn.model.SrcServerConfig;
import com.qn.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
@Service
public class ConfigServiceImpl implements ConfigService{

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final static String SERI_SRC_FILE_NAME = "srcconfig.ser";
    private final static String SERI_DEST_FILE_NAME = "destconfig.ser";

    /**
     * @Desc: 获取源服务器配置信息
     * @param: []
     * @return: com.qn.model.SrcServerConfig
     * @date: 2019/3/11 上午11:27
     */
    @Override
    public SrcServerConfig getConfigInfo() {
        SrcServerConfig config = (SrcServerConfig)this.getInfo(SERI_SRC_FILE_NAME);
        if (config == null) {
            config = new SrcServerConfig();
        }
        return config;
    }
    /**
     * @Desc: 保存源服务器配置信息
     * @param: [config]
     * @return: boolean
     * @date: 2019/3/11 上午11:21
     */
    @Override
    public boolean saveConfigInfo(SrcServerConfig config) {
        return this.saveInfo(SERI_SRC_FILE_NAME,config);
    }
    /**
     * @Desc: 获取目标服务器配置信息
     * @param: []
     * @return: com.qn.model.DestServerConfig
     * @date: 2019/3/11 上午11:27
     */
    @Override
    public DestServerConfig getDestConfigInfo() {
        DestServerConfig config = (DestServerConfig)this.getInfo(SERI_DEST_FILE_NAME);
        if (config == null) {
            config = new DestServerConfig();
        }
        return config;
    }

    /**
     * @Desc: 保存目标服务器配置信息
     * @param: [config]
     * @return: boolean
     * @date: 2019/3/11 上午11:21
     */
    @Override
    public boolean saveDestConfigInfo(DestServerConfig config) {
        return this.saveInfo(SERI_DEST_FILE_NAME,config);
    }
    /**
     * @Desc: 保存配置信息
     * @param: [fileName, object]
     * @return: boolean
     * @date: 2019/3/11 上午11:22
     */
    private boolean saveInfo(String fileName, Object object){
        boolean result = true;
        //声明序列化后的名称, 文件保存在项目的根目录下（和src目录平级）
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        //开始写入
        try {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        System.out.println("file write finished");
        //关闭流
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private Object getInfo(String fileName){
        //声明序列化后的名称, 文件保存在项目的根目录下（和src目录平级）
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            logger.error("FileInputStream: ", e);
            System.out.println("配置文件打开失败");
            //e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            if (fis != null){
                ois = new ObjectInputStream(fis);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object config = null;
        //开始读取
        try {
            if (ois != null){
                config = ois.readObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return config;
    }
}
