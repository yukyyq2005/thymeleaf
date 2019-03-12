package com.qn.model;

import java.io.Serializable;

/**
 * @description:收流服务器配置信息，此服务器只做rtmp的中转，目的是为了存储rtmp流
 * @author: Youq
 * @create: 2019-03-11 10:43
 */
public class DestServerConfig implements Serializable {
    private String srcServerUrl;
    private String destServerUrl;

    public String getSrcServerUrl() {
        return srcServerUrl;
    }

    public void setSrcServerUrl(String srcServerUrl) {
        this.srcServerUrl = srcServerUrl;
    }

//    public String getDestServerUrl() {
//        return destServerUrl;
//    }

    public void setDestServerUrl(String destServerUrl) {
        this.destServerUrl = destServerUrl;
    }
}
