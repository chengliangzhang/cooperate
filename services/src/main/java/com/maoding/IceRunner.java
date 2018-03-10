package com.maoding;

import com.maoding.Base.IceConfig;
import com.maoding.Utils.FileUtils;
import com.maoding.Utils.StringUtils;
import com.maoding.Utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/12/13 12:06
 * 描    述 :
 */
@Component
public class IceRunner {
    /** 日志对象 */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IceConfig iceConfig;

    public void run(String[] args) {
        //执行启动命令行
        if (StringUtils.isNotEmpty(iceConfig.getStartCmd()) && StringUtils.isNotEmpty(iceConfig.getCmdStart())){
            log.info("命令行文件:" + iceConfig.getStartCmd());
            log.info("命令行配置文件:" + iceConfig.getCmdConfig());
            log.info("启动组件:" + iceConfig.getCmdStart());
            try {
                Properties properties = new Properties();
                InputStreamReader reader =new InputStreamReader(new FileInputStream(iceConfig.getCmdConfig()));
                properties.load(reader);
                for (Object key : properties.keySet()) {
                    String keyStr = key.toString();
                    if (keyStr.contains("LMDB.Path")){
                        String path = new String(properties.getProperty(keyStr).getBytes("ISO-8859-1"), "utf-8");
                        FileUtils.ensureDirExist(path);
                    } else if (StringUtils.isSame(keyStr,"Ice.LogFile")){
                        String path = StringUtils.getDirName(new String(properties.getProperty(keyStr).getBytes("ISO-8859-1"), "utf-8"));
                        FileUtils.ensureDirExist(path);
                    }
                }

                List<String> iceboxCmd = new ArrayList<>();
                iceboxCmd.add("cmd");
                iceboxCmd.add("/c");
                iceboxCmd.add("start");
                iceboxCmd.add("\"ice通用服务\"");
                iceboxCmd.add(iceConfig.getStartCmd());
                iceboxCmd.add(iceConfig.getCmdConfig());
                String[] cmdArray = iceConfig.getCmdStart().split(";");
                Collections.addAll(iceboxCmd, cmdArray);
                if ((args != null) && (args.length > 0)){
                    log.info("其他参数:" + Arrays.toString(args));
                    Collections.addAll(iceboxCmd, args);
                }
                String[] cmds = iceboxCmd.toArray(new String[iceboxCmd.size()]);
                Runtime.getRuntime().exec(cmds);
                ThreadUtils.sleep(8000);
                log.info("启动命令行文件执行完毕");
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(),e);
            } catch (IOException e) {
                log.error("执行启动命令行文件时有误");
            }
        }

        //启动服务
        String configFile = iceConfig.getConfigFileName();
        if (configFile != null) {
            List<String> params = new ArrayList<>();
            params.add("--Ice.Config=" + configFile);
            log.info("启动IceBox服务，配置文件：" + configFile);
            com.zeroc.IceBox.Server.main(params.toArray(new String[params.size()]));
        }
    }
}
