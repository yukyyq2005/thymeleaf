ffmpeg  -rtsp_transport tcp -i rtsp://admin:qn1234567890@192.168.1.64:554/h264/ch1/main/av_stream -s:v 1920*1080 -bufsize:v 1000k -b:v 2000k -bt 2000k -maxrate:v 1950k -minrate:v 1900k -g 30 -codec:v libx264 -keyint_min 2 -nal-hrd cbr  -pass 1 -passlogfile ffmpeg2pass -sc_threshold 0 -bf 0 -b_strategy 0 -r 20 -profile:v high -preset:v fast -tune:v zerolatency -f flv "rtmp://192.168.1.133:1935/live/push" 1>1.txt 2>&1 &