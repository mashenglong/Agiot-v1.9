package cn.edu.nwpu.utils;
import java.util.Properties;

/**
 * 配置文件工具类
 * 用于加载 resource/conf/ 目录下的 properties 的属性文件
 */

import java.io.InputStream;
import java.util.Iterator;

public class PropertiesUtil {

        private String propertiesName;
        public PropertiesUtil(String propertiesName){
                this.propertiesName = propertiesName;
        }

        public String getUrlValue(String urlName) {
                String url = null;
                Properties prop = new Properties();
                try {
                        ClassLoader classLoader = PropertiesUtil.class.getClassLoader();
                        InputStream in = classLoader.getResourceAsStream("conf/"+this.propertiesName+".properties");
                        prop.load(in);
                        Iterator<String> it = prop.stringPropertyNames().iterator();
                        while (it.hasNext()) {
                                if (it.next().equals(urlName)) {
                                        url = prop.getProperty(urlName);
                                }
                        }
                        in.close();
                } catch (Exception e) {

                }
                return url;
        }
        public static void main(String[] args) {
                System.out.println(new PropertiesUtil("mqtt").getUrlValue("com.mqtt.host"));
        }
}