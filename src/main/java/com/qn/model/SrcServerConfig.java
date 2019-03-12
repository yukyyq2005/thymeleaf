package com.qn.model;

import java.io.Serializable;

/**
 * @description:推流服务器配置信息
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
public class SrcServerConfig implements Serializable{
    private static final long serialVersionUID = 1L;
    //rtsp流地址
    private String rtspUrl;

    @Override
    public String toString() {
        return "SrcServerConfig{" +
                "rtspUrl='" + rtspUrl + '\'' +
                ", codeFormat='" + codeFormat + '\'' +
                ", bitrate='" + bitrate + '\'' +
                ", size='" + size + '\'' +
                ", frameRate='" + frameRate + '\'' +
                ", isClosePush=" + isClosePush +
                '}';
    }

    public void setClosePush(boolean closePush) {
        isClosePush = closePush;
    }

    //编码格式 x264，x265
    private String codeFormat;



    public boolean isClosePush() {
        return isClosePush;
    }

    public SrcServerConfig() {
    }

    public void setRtspUrl(String rtspUrl) {
        this.rtspUrl = rtspUrl;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setFrameRate(String frameRate) {
        this.frameRate = frameRate;
    }

    public String getRtspUrl() {
        return rtspUrl;
    }

    public String getCodeFormat() {
        return codeFormat;
    }

    public String getBitrate() {
        return bitrate;
    }

    public String getSize() {
        return size;
    }

    public String getFrameRate() {
        return frameRate;
    }

    //码率
    private String bitrate;
    //视频画面的宽与高
    private String size;
    //帧率 默认为25
    private String frameRate;
    //是否关闭推流
    private boolean isClosePush;

}
