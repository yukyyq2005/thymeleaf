package com.qn.task;

import com.qn.model.DestServerConfig;
import com.qn.service.ConfigService;
import com.qn.service.PushManager;
import com.qn.util.MethodUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-11 11:04
 */
@Component//被容器扫描
@EnableScheduling //开启定时任务，自动扫描
public class DestServerTask {

    private int count;
    @Value("${server.type}")
    private String serverType;
    @Autowired
    PushManager pushManager;
    @Autowired
    ConfigService configService;
    RestTemplate restTemplate;
    /**
     * @Desc: 实时检测源服务器是否开启了服务
     * @param: []
     * @return: void
     * @date: 2019/3/11 上午11:07
     */
    @Scheduled(cron = "*/2 * * * * *")//每2秒执行一次
    private synchronized void monitorSrcServer() {
        if (!serverType.equals("desc")) {
            return;
        }
        List<String> list = MethodUtil.processList("ffmpeg");
        if (list.size() > 0) {
            count++;
            System.out.println("ffmpeg 已经开启 ！！！");
            if (count >= 30){
                count = 0;
                String ip = configService.getDestConfigInfo().getSrcServerUrl();
                boolean isPingSuc = MethodUtil.ping(ip);
                if (isPingSuc == true){
                    System.out.println("一分钟ping一次："+ip+"能通!");
                }else{
                    System.out.println("警告！！！"+ip+" 不通! 杀死ffmpeg");
                    pushManager.killProcess("ffmpeg");
                }
            }
            return;
        }
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        DestServerConfig config = configService.getDestConfigInfo();
        String idAddr = config.getSrcServerUrl();
        if (idAddr.length() > 0) {
            boolean isPingSuc = MethodUtil.ping(idAddr);
            //没有ping通，就返回
            if (!isPingSuc) {
                return;
            }
            //"http://localhost:8080/get_push_addr"
            String url = "http://" + idAddr + ":80/get_push_addr";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            String httpResult = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
            System.out.println("src url : " + httpResult);

            if (httpResult.length() > 1) {
                String ipAddr = MethodUtil.getLocalIpAddr();
                //ffmpeg  -i "rtmp://192.168.1.133:1935/live/push" -vcodec copy -acodec copy -f flv "rtmp://192.168.1.233:1935/live/push"
                String pushUrl = "ffmpeg -i " + httpResult + " -vcodec copy -acodec copy -f flv " + "\"rtmp://" + ipAddr + ":1935/live/push\"" + " 1>ffmpeg.log 2>&1 &";
                System.out.println("推流地址是：" + pushUrl);
                pushManager.startPush(pushUrl);
            } else {//如果服务器返回为0，则表示服务器没有开启服务
                pushManager.killProcess("ffmpeg");
                System.out.println("服务器无法连接! code：" + httpResult);
            }
        } else {
            System.out.println("配置文件为空!!!");
        }
    }
}
