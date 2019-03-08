package com.qn.util;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
}
