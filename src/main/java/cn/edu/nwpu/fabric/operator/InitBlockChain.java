package cn.edu.nwpu.fabric.operator;

import cn.edu.nwpu.fabric.Bean.AgriculturalChemicals;
import cn.edu.nwpu.fabric.Bean.UserContext;
import cn.edu.nwpu.fabric.Client.FabricCAClient;
import cn.edu.nwpu.fabric.Client.FabricClient;
import cn.edu.nwpu.fabric.config.FabricConfig;
import cn.edu.nwpu.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.*;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class InitBlockChain {

    @Autowired
    FabricConfig fabricConfig;

    public InitBlockChain(FabricConfig fabricConfig) {
        this.fabricConfig = fabricConfig;
    }

    public  void org1CreateChannel() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, org.bouncycastle.crypto.CryptoException, InvalidArgumentException, CryptoException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, TransactionException, ProposalException {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment =  UserUtils.getEnrollment(fabricConfig.getKeyFolderPath(),
                fabricConfig.getKeyFileName(),
                fabricConfig.getCertFoldePath(),
                fabricConfig.getCertFileName());
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
//        //47.108.136.201
        Orderer orderer = fabricClient.getOrderer("orderer.nwpu.com","grpcs://orderer.nwpu.com:7050",fabricConfig.getTlsOrderFilePath());
        Channel channel =  fabricClient.createChannel("agiot-channel",orderer,fabricConfig.getTxfilePath());

        //生成通道后,将利用组织1的管理员身份将该组织的所有peer节点加入通道
        channel.addOrderer(fabricClient.getOrderer("orderer.nwpu.com","grpcs://orderer.nwpu.com:7050",fabricConfig.getTlsOrderFilePath()));
        channel.joinPeer(fabricClient.getPeer("peer0.org1.nwpu.com","grpcs://peer0.org1.nwpu.com:7051",fabricConfig.getTlsPeerFilePath()));
        channel.joinPeer(fabricClient.getPeer("peer1.org1.nwpu.com","grpcs://peer1.org1.nwpu.com:8051",fabricConfig.getTlsPeerFilePath()));
    }

    public  void org2JoinChannel() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, org.bouncycastle.crypto.CryptoException, InvalidArgumentException, CryptoException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, TransactionException, ProposalException {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org2");
        userContext.setMspId("Org2MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment =  UserUtils.getEnrollment(fabricConfig.getKeyFolderPath2(),
                fabricConfig.getKeyFileName2(),
                fabricConfig.getCertFoldePath2(),
                fabricConfig.getCertFileName2());
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);

        //生成通道后,将利用组织2的管理员身份将该组织的所有peer节点加入通道
        Channel channel = fabricClient.getChannel("agiot-channel");
        channel.addOrderer(fabricClient.getOrderer("orderer.nwpu.com","grpcs://orderer.nwpu.com:7050", fabricConfig.getTlsOrderFilePath()));
        channel.joinPeer(fabricClient.getPeer("peer0.org2.nwpu.com","grpcs://peer0.org2.nwpu.com:9051",fabricConfig.getTlsPeerFilePath2()));
        channel.joinPeer(fabricClient.getPeer("peer1.org2.nwpu.com","grpcs://peer1.org2.nwpu.com:10051",fabricConfig.getTlsPeerFilePath2()));
    }

    public  void installChaincodeToOrg1() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, org.bouncycastle.crypto.CryptoException, InvalidArgumentException, CryptoException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, ProposalException {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment =  UserUtils.getEnrollment(fabricConfig.getKeyFolderPath(),
                fabricConfig.getKeyFileName(),
                fabricConfig.getCertFoldePath(),
                fabricConfig.getCertFileName());
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org1.nwpu.com","grpcs://peer0.org1.nwpu.com:7051",fabricConfig.getTlsPeerFilePath());
        Peer peer1 = fabricClient.getPeer("peer1.org1.nwpu.com","grpcs://peer1.org1.nwpu.com:8051",fabricConfig.getTlsPeerFilePath());
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);
        fabricClient.installChaincode(TransactionRequest.Type.JAVA,"AgriculturalChemicals","1.4","K:/IDEA/JavaChaincode/","",peers);

    }

    public void installChaincodeToOrg2() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, org.bouncycastle.crypto.CryptoException, InvalidArgumentException, CryptoException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, ProposalException {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org2");
        userContext.setMspId("Org2MSP");
        userContext.setAccount("赵而");
        userContext.setName("admin");
        Enrollment enrollment =  UserUtils.getEnrollment(fabricConfig.getKeyFolderPath2(),
                fabricConfig.getKeyFileName2(),
                fabricConfig.getCertFoldePath2(),
                fabricConfig.getCertFileName2());
        userContext.setEnrollment(enrollment);
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org2.nwpu.com","grpcs://peer0.org2.nwpu.com:9051",fabricConfig.getTlsPeerFilePath2());
        Peer peer1 = fabricClient.getPeer("peer1.org2.nwpu.com","grpcs://peer1.org2.nwpu.com:10051",fabricConfig.getTlsPeerFilePath2());
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);

        //链码的路径一定要用 / 的方式来写,不能使用 \\ ,否则在实例化链码的时候会 Failed to generate platform-specific docker build: Error returned from build: 1 "can't load package: package mainresourceschaincode: cannot find package "mainresourceschaincode" in any of: 	/opt/go/src/mainresourceschaincode
        //其次,如果部署的代码为JAVA代码,chaincodePath 为项目所在目录,java合约完整目录为 E:\Java\JavaChaincode\src\main\java\org\hyperledger\fabric\example\SimpleChaincode1.java 为完整目录

        fabricClient.installChaincode(TransactionRequest.Type.JAVA,"AgriculturalChemicals","1.4","K:/IDEA/JavaChaincode/","",peers);
    }

    public void chaincodeInstantiated() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, org.bouncycastle.crypto.CryptoException, InvalidArgumentException, CryptoException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, TransactionException, ProposalException {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment =  UserUtils.getEnrollment(fabricConfig.getKeyFolderPath(),
                fabricConfig.getKeyFileName(),
                fabricConfig.getCertFoldePath(),
                fabricConfig.getCertFileName());
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer = fabricClient.getPeer("peer0.org1.nwpu.com","grpcs://peer0.org1.nwpu.com:7051",fabricConfig.getTlsPeerFilePath());
        Orderer order = fabricClient.getOrderer("orderer.nwpu.com","grpcs://orderer.nwpu.com:7050",fabricConfig.getTlsOrderFilePath());

        String[] initArgs = {"a"};

        fabricClient.initChaincode("agiot-channel", TransactionRequest.Type.JAVA,"AgriculturalChemicals","1.4",order,peer,"init",initArgs);
    }

    public  void QueryChaincode() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, org.bouncycastle.crypto.CryptoException, InvalidArgumentException, CryptoException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, TransactionException, ProposalException {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment =  UserUtils.getEnrollment(fabricConfig.getKeyFolderPath(),
                fabricConfig.getKeyFileName(),
                fabricConfig.getCertFoldePath(),
                fabricConfig.getCertFileName());
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org1.nwpu.com","grpcs://peer0.org1.nwpu.com:7051",fabricConfig.getTlsPeerFilePath());
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        String[] initArgs = {"101-20220117020630"};
        Map map =  fabricClient.queryChaincode(peers,"agiot-channel", TransactionRequest.Type.JAVA,"AgriculturalChemicals","query",initArgs);
        System.out.println(map.get(200));
        JSONObject jo =JSONObject.fromObject(map.get(200)) ;
        AgriculturalChemicals ac = (AgriculturalChemicals) JSONObject.toBean(jo, AgriculturalChemicals.class);
        System.out.println("sc:"+ac);
    }

    public  void ChainCodeUpgradeTest() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, org.bouncycastle.crypto.CryptoException, InvalidArgumentException, CryptoException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, ChaincodeEndorsementPolicyParseException, TransactionException, ProposalException {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");
        Enrollment enrollment =  UserUtils.getEnrollment(fabricConfig.getKeyFolderPath2(),
                fabricConfig.getKeyFileName2(),
                fabricConfig.getCertFoldePath2(),
                fabricConfig.getCertFileName2());
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer = fabricClient.getPeer("peer0.org1.nwpu.com","grpcs://peer0.org1.nwpu.com:7051",fabricConfig.getTlsPeerFilePath());
        Orderer order = fabricClient.getOrderer("orderer.nwpu.com","grpcs://orderer.nwpu.com:7050",fabricConfig.getTlsOrderFilePath());
        String[] initArgs = {"b"};
        fabricClient.upgradeChaincode("agiot-channel", TransactionRequest.Type.JAVA,"AgriculturalChemicals","1.4",order,peer,"init",initArgs);
    }
    //注册用户 hyGQjGloTTXG
    @Test
    public void RegesterUserTest() throws Exception {

        FabricCAClient caClient = new FabricCAClient("http://192.168.109.145:7054",null);
        UserContext register = new UserContext();
        //设置注册的lihua属于哪个组织,在配置文件中有 org1 和 org2
        register.setName("lihua");
        register.setAffiliation("org1");
        Enrollment enrollment = caClient.enroll("admin","adminpw");
        UserContext registar = new UserContext();
        registar.setName("admin");
        registar.setAffiliation("org1");
        registar.setEnrollment(enrollment);
        String secret =  caClient.register(registar,register);
        log.info("secret is :" + secret);
    }
    public  void RegisterUserQuery() throws MalformedURLException, InvalidArgumentException, CryptoException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, EnrollmentException, TransactionException, ProposalException {
        FabricCAClient caClient = new FabricCAClient("http://192.168.109.145:7054",null);
        UserContext userContext = new UserContext();
        //连接区块链网络的组织 Org1
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");

        Enrollment enrollment = caClient.enroll("lihua","hyGQjGloTTXG");
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org1.nwpu.com","grpcs://peer0.org1.nwpu.com:7051",fabricConfig.getTlsPeerFilePath());
        //  Peer peer1 = fabricClient.getPeer("peer1.org1.nwpu.com","grpcs://peer1.org1.nwpu.com:8051",tlsPeerFilePath);
        Peer peer2 = fabricClient.getPeer("peer0.org2.nwpu.com","grpcs://peer0.org2.nwpu.com:9051",fabricConfig.getTlsPeerFilePath2());
        //  Peer peer3 = fabricClient.getPeer("peer1.org2.nwpu.com","grpcs://peer1.org2.nwpu.com:10051",tlsPeerFilePath2);
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        //  peers.add(peer1);
        peers.add(peer2);
        //  peers.add(peer3);
        String[] initArgs = {"123457"};
        Map map =  fabricClient.queryChaincode(peers,"agiot-channel", TransactionRequest.Type.JAVA,"AgriculturalChemicals","query",initArgs);
        System.out.println(map.get(200));
        JSONObject jo =JSONObject.fromObject(map.get(200)) ;
        AgriculturalChemicals ac = (AgriculturalChemicals) JSONObject.toBean(jo, AgriculturalChemicals.class);
        System.out.println(ac);
    }

    //注册用户invoke合约
    public  void newUserInvokeChaincode () throws IOException, InvalidArgumentException, CryptoException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, EnrollmentException, TransactionException, ProposalException, CryptoException, EnrollmentException {
        FabricCAClient caClient = new FabricCAClient("http://192.168.109.145:7054",null);
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("李伟");
        userContext.setName("admin");

        Enrollment enrollment = caClient.enroll("lihua","hyGQjGloTTXG ");
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org1.nwpu.com","grpcs://peer0.org1.nwpu.com:7051",fabricConfig.getTlsPeerFilePath());
        Peer peer1 = fabricClient.getPeer("peer1.org1.nwpu.com","grpcs://peer1.org1.nwpu.com:8051",fabricConfig.getTlsPeerFilePath());
        //  Peer peer2 = fabricClient.getPeer("peer0.org2.nwpu.com","grpcs://peer0.org2.nwpu.com:9051",tlsPeerFilePath2);
        //  Peer peer3 = fabricClient.getPeer("peer1.org2.nwpu.com","grpcs://peer1.org2.nwpu.com:10051",tlsPeerFilePath2);
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);
        Orderer order = fabricClient.getOrderer("orderer.nwpu.com","grpcs://orderer.nwpu.com:7050",fabricConfig.getTlsOrderFilePath());
        String initArgs[] = {"123457","{\"landNumber\":\"123457\",\"operator\":\"zhangsan\",\"mobile\":\"18910012222\",\"pesticideName\":\"草甘膦\",\"pesticideUsed\":\"5\",\"operateDate\":\"2021-12-23 14:49:30\"}"};
        fabricClient.invoke("agiot-channel", TransactionRequest.Type.JAVA,"AgriculturalChemicals",order,peers,"save",initArgs);

    }
    public void init() throws IOException, TransactionException, NoSuchAlgorithmException, InvocationTargetException, InstantiationException, org.bouncycastle.crypto.CryptoException, InvalidArgumentException, IllegalAccessException, NoSuchMethodException, CryptoException, InvalidKeySpecException, ClassNotFoundException, ProposalException {
        org1CreateChannel();
        org2JoinChannel();
        installChaincodeToOrg1();
        installChaincodeToOrg2();
    }

//    public static void main(String[] args) throws Exception {
//        FabricCAClient caClient = new FabricCAClient("http://192.168.109.145:7054",null);
//        UserContext register = new UserContext();
//        //设置注册的lihua属于哪个组织,在配置文件中有 org1 和 org2
//        register.setName("zhangsan");
//        register.setAffiliation("org1");
////        log.info("请输入Org1组织管理员账号:");
////        Scanner scan = new Scanner(System.in) ;
////        String name =  scan.next();
////        log.info("密码:");
////        String password =  scan.next();
////        //admin adminpw
//        Enrollment enrollment = caClient.enroll("admin","adminpw");
//        UserContext registar = new UserContext();
//        registar.setName("admin");
//        registar.setAffiliation("org1");
//        registar.setEnrollment(enrollment);
//        String secret =  caClient.register(registar,register);
//        log.info("secret is :" + secret);
//    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Spring.xml");
        FabricConfig fabricConfig = context.getBean("fabricConfig", FabricConfig.class);
        System.out.println(fabricConfig.toString());
        InitBlockChain initBlockChain = new InitBlockChain(fabricConfig);
        try{
            //initBlockChain.init();
           // initBlockChain.chaincodeInstantiated();
            initBlockChain.QueryChaincode();
        }catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
