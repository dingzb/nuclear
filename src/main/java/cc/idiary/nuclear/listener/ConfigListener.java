package cc.idiary.nuclear.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

public class ConfigListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        // 设置cxf日志使用slf4j
        System.setProperty("org.apache.cxf.Logger", "org.apache.cxf.common.logging.Slf4jLogger");
        // 获取项目WEB-INF路径
        String webInfPath = sc.getRealPath("/WEB-INF/");
        System.setProperty("webInfPath", webInfPath);
        // native lib
        System.setProperty("java.library.path", webInfPath + "/lib");

        // Supreme user
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getResourceAsStream("/properties/baseinfo.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        BaseInfo.USER_SUPREME.setUsername(prop.getProperty("baseInfo.supremeUsername") == null ? StringTools.randomUUID() : prop.getProperty("baseInfo.supremeUsername"));
//        BaseInfo.USER_SUPREME.setPassword(StringTools.Md5(prop.getProperty("baseInfo.supremePassword") == null ? StringTools.randomUUID() : prop.getProperty("baseInfo.supremePassword")));

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
