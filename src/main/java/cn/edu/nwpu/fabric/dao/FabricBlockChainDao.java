package cn.edu.nwpu.fabric.dao;

import cn.edu.nwpu.fabric.Bean.UserContext;
import cn.edu.nwpu.fabric.Client.FabricClient;
import cn.edu.nwpu.fabric.config.FabricConfig;
import cn.edu.nwpu.mysql.pojo.Sensor;
import cn.edu.nwpu.utils.DataUtil;
import cn.edu.nwpu.utils.UserUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.bouncycastle.crypto.CryptoException;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public  class FabricBlockChainDao{

    public static void insert(Sensor sensor) throws InvalidKeySpecException, NoSuchAlgorithmException, CryptoException, IOException, IllegalAccessException, InvalidArgumentException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, org.hyperledger.fabric.sdk.exception.CryptoException, ProposalException, TransactionException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Spring.xml");
        UserContext userContext = context.getBean("userContext", UserContext.class);
        FabricConfig fabricConfig = context.getBean("fabricConfig",FabricConfig.class);
        Enrollment enrollment =  UserUtils.getEnrollment(fabricConfig.getKeyFolderPath2(),
                fabricConfig.getKeyFileName2(),
                fabricConfig.getCertFoldePath2(),
                fabricConfig.getCertFileName2());
        userContext.setEnrollment(enrollment);
        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer("peer0.org1.nwpu.com","grpcs://peer0.org1.nwpu.com:7051",fabricConfig.getTlsPeerFilePath());
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        Orderer order = fabricClient.getOrderer("orderer.nwpu.com","grpcs://orderer.nwpu.com:7050",fabricConfig.getTlsOrderFilePath());
        String jsonObject = JSONObject.toJSONString(sensor);
        String[] initArgs = {sensor.getsId()+"-"+DataUtil.tranDateForm2Number(sensor.getsDate()),jsonObject};


        try {
            fabricClient.invoke("agiot-channel", TransactionRequest.Type.JAVA,"AgriculturalChemicals",order,peers,"saveSensor",initArgs);
        } catch (Exception e) {
            throw  new IllegalAccessException("请检查区块链是否已经打开！");
          //  log.info(e.getMessage());
        }
    }
    public static void main(String[] args) throws IOException, TransactionException, NoSuchAlgorithmException, InstantiationException, NoSuchMethodException, CryptoException, InvalidArgumentException, InvocationTargetException, IllegalAccessException, org.hyperledger.fabric.sdk.exception.CryptoException, InvalidKeySpecException, ClassNotFoundException, ProposalException {
        Sensor s =   new Sensor();
        FabricBlockChainDao.insert(s);
    }
}
