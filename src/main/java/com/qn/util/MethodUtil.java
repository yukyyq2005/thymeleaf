package com.qn.util;

import org.springframework.util.StringUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-08 10:51
 */
public class MethodUtil {


    /**
     * @Desc: ping
     * @param: [ipAddress]
     * @return: boolean true-能ping通，false-不能ping通
     * @date: 2019/3/12 上午11:13
     */
    public static boolean ping(String ipAddress){
        int timeOut = 3000;  //超时应该在3钞以上
        boolean status = false;     // 当返回值是true时，说明host是可用的，false则不可。
        try {
            status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("地址：" + ipAddress + " 不可达 ");
        }
//        System.out.println("exit VolumeManage.execPingCommand(String deviceIp)"
//                + "[networkUseable] =" + status);
        return status;
    }
    public static String getIPAddr(String ipcUrl) {
        //        String host1 = "rtsp://admin:qn1234567890@192.168.1.64:554/h264/ch1/main/av_stream";
        //正则匹配找出url里面的ip地址
        String host = null;
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(ipcUrl);
        if (matcher.find()) {
            host = matcher.group();
        }
        if (!MethodUtil.isIpLegal(host)){
            return null;
        }
        return host;
    }
    public static boolean isIpLegal(String ipStr) {
        if (StringUtils.isEmpty(ipStr)) {
            return false;
        }
        //ip地址范围：(1~255).(0~255).(0~255).(0~255)
        String ipRegEx = "^([1-9]|([1-9][0-9])|(1[0-9][0-9])|(2[0-4][0-9])|(25[0-5]))(\\.([0-9]|([1-9][0-9])|(1[0-9][0-9])|(2[0-4][0-9])|(25[0-5]))){3}$";
        //String ipRegEx = "^([1-9]|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))(\\.(\\d|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))){3}$";
        //String ipRegEx = "^(([1-9]\\d?)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))(\\.(0|([1-9]\\d?)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))){3}$";
        Pattern pattern = Pattern.compile(ipRegEx);
        Matcher matcher = pattern.matcher(ipStr);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static List<String> processList(String command) {
        List<String> list = new ArrayList();
        BufferedReader reader = null;
        try {
            //显示所有进程
            Process process = Runtime.getRuntime().exec("ps -ef");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(command)) {
                    //System.out.println("相关信息 -----> " + command);
                    String[] strs = line.split("\\s+");
                    //System.out.println("kill process pid： " + strs[1]);
                    list.add(strs[1]);
                    //return strs[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
        return list;
    }
    public static String getLocalIpAddr() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("获取ip地址异常");
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        System.out.println("本机 IP: " + ip);
        return ip;
    }
    public static String runCommand(String cmd) throws Exception {
        if (StringUtils.isEmpty(cmd)) {
            return null;
        }
        //使用"sh -c 命令字符串"的方式解决管道和重定向的问题
        List<String> cmds = new LinkedList<String>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add(cmd);
        ProcessBuilder pb = new ProcessBuilder(cmds);
        //重定向到标准输出
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor(3, TimeUnit.SECONDS);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        String result = sb.toString();
        return result;
    }

    /**
     * @Desc: 运行shell命令
     * @param: [command]
     * @return: void
     * @date: 2019/3/7 下午5:22
     */
    public static void runCmd(String command) {
        //String cmd = "/home/ty/t.sh";//这里必须要给文件赋权限 chmod u+x fileName;
        try {
            // 使用Runtime来执行command，生成Process对象
            Runtime runtime = Runtime.getRuntime();
            String[] sh = new String[]{"/bin/sh", "-c", command};
            Process process = runtime.exec(sh);
            // 取得命令结果的输出流
            InputStream is = process.getInputStream();
            // 用一个读输出流类去读
            InputStreamReader isr = new InputStreamReader(is);
            // 用缓冲器读行
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println("进程ID：" + line);
            }
            //执行关闭操作
            is.close();
            isr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        System.out.println("start push command");
//        try {
//            Runtime rt = Runtime.getRuntime();
//            Process proc = rt.exec(command);
//            InputStream stderr = proc.getErrorStream();
//            InputStreamReader isr = new InputStreamReader(stderr);
//            BufferedReader br = new BufferedReader(isr);
//            String line = null;
//            System.out.println("<ERROR>");
//            while ((line = br.readLine()) != null){
//                System.out.println(line);
//            }
//            System.out.println("</ERROR>");
//            int exitVal = proc.waitFor();
//            System.out.println("Process exitValue: " + exitVal);
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//        System.out.println("end push command");
        //System.out.println("ffmpeg进程ID：" + this.getPID("ffmpeg"));
    }

    /**
     * 关闭Linux进程
     *
     * @param Pid 进程的PID
     */
    public static void closeLinuxProcess(String Pid) {
        Process process = null;
        BufferedReader reader = null;
        try {
            //杀掉进程
            process = Runtime.getRuntime().exec("kill -9 " + Pid);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println("kill PID return info -----> " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param @param deviceIp
     * @return boolean true-能ping通，false-不能ping通
     * @throws
     * @Title: execPingCommand
     * @Description: 执行ping命令，查看设备是否可用
     */

//    public boolean execPingCommand(String deviceIp) {
//        logger.debug("enter VolumeManage.execPingCommand(String deviceIp)");
//        boolean networkUseable = false;
//// String address="www.javawind.net";
//        Process process = null;
//        try {
//            process = Runtime.getRuntime().exec("ping -c 3" + deviceIp);
//        } catch (IOException e1) {
//            logger.error("System error:", e1);
//        }
//        InputStreamReader r = new InputStreamReader(process.getInputStream());
//        LineNumberReader returnData = new LineNumberReader(r);
//        String returnMsg = "";
//        String line = "";
//        try {
//            while ((line = returnData.readLine()) != null) {
//                System.out.println(line);
//                returnMsg += line;
//            }
//            if (returnMsg.indexOf("Unreachable") != -1
//                    || returnMsg.indexOf("100% packet loss") != -1) {
//                networkUseable = false;
//            } else {
//                networkUseable = true;
//            }
//        } catch (IOException e) {
//            logger.error("System error:", e);
//        } finally {
//            if (returnData != null) {
//                try {
//                    returnData.close();
//                } catch (IOException e) {
//// TODO Auto-generated catch block
//                    logger.error("System error:", e);
//                }
//            }
//            if (r != null) {
//                try {
//                    r.close();
//                } catch (IOException e) {
//                    logger.error("System error:", e);
//                }
//            }
//        }
//        logger.debug("exit VolumeManage.execPingCommand(String deviceIp)"
//                + "[networkUseable] =" + networkUseable);
//        return networkUseable;
//    }
}
