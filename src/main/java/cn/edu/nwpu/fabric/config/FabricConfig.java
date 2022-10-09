package cn.edu.nwpu.fabric.config;

import cn.edu.nwpu.fabric.Bean.UserContext;
import lombok.Data;
import lombok.ToString;
import org.hyperledger.fabric.sdk.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Data
@ToString
@Component
@PropertySource(value = "classpath:FabricConfig.properties",ignoreResourceNotFound = true,encoding = "UTF-8")
public class FabricConfig {
    
    @Autowired
    private UserContext userContext;

    private Enrollment enrollment;

    //组织1的管理员私钥路径与密钥文件
    @Value("${fabricConfig.keyFolderPath}")
    private  String keyFolderPath;

    @Value("${fabricConfig.keyFileName}")
    private String keyFileName;

    //组织1的管理员证书路径与密钥文件
    @Value("${fabricConfig.certFoldePath}")
    private String certFoldePath;
    @Value("${fabricConfig.certFileName}")
    private String certFileName;

    //组织2的管理员私钥路径与密钥文件
    @Value("${fabricConfig.keyFolderPath2}")
    private String keyFolderPath2;
    @Value("${fabricConfig.keyFileName2}")
    private String keyFileName2;

    //组织2的管理员证书路径与密钥文件

    @Value("${fabricConfig.certFoldePath2}")
    private String certFoldePath2;
    @Value("${fabricConfig.certFileName2}")
    private String certFileName2;

    //order catls 的组织证书 ,作用?
    @Value("${fabricConfig.tlsOrderFilePath}")
    private String tlsOrderFilePath;

    //通道文件
    @Value("${fabricConfig.txfilePath}")
    private String txfilePath;

    //组织1  tls证书文件
    @Value("${fabricConfig.tlsPeerFilePath}")
    private String tlsPeerFilePath ;

    @Value("${fabricConfig.tlsPeerFilePath2}")
    private String tlsPeerFilePath2;


    public FabricConfig(){

    }

    @PostConstruct
    public void init() {
    }

}
