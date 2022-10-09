package cn.edu.nwpu.fabric.Client;

import cn.edu.nwpu.fabric.Bean.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
public class FabricClient {


   private HFClient hfClient;

   public FabricClient(UserContext userContext) throws IllegalAccessException, InvocationTargetException, InvalidArgumentException, InstantiationException, NoSuchMethodException, CryptoException, ClassNotFoundException {
       hfClient = HFClient.createNewInstance();
       CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
       hfClient.setCryptoSuite(cryptoSuite);
       hfClient.setUserContext(userContext);
   }

    /**
     * @description 创建channel
     * @param channelName channel的名字
     * @param order order的信息
     * @param txPath 创建channel所需的tx文件
     * @return Channel
     * @throws IOException
     * @throws InvalidArgumentException
     * @throws TransactionException
     */
     public Channel createChannel(String channelName, Orderer order, String txPath) throws IOException, InvalidArgumentException, TransactionException {
       ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(txPath));
       return hfClient.newChannel(channelName,order,channelConfiguration,hfClient.getChannelConfigurationSignature(channelConfiguration,hfClient.getUserContext()));
    }

    /**
     * @description 安装合约
     * @param lang 合约开发语言
     * @param chaincodeName 合约名称
     * @param chaincodeVersion 合约版本
     * @param chaincodeLocation 合约的目录路径
     * @param chaincodePath 合约的文件夹
     * @param peers 安装的peers 节点
     * @throws InvalidArgumentException
     * @throws ProposalException
     */
    public void installChaincode(TransactionRequest.Type lang, String chaincodeName, String chaincodeVersion ,String chaincodeLocation,String chaincodePath, List<Peer> peers) throws InvalidArgumentException, ProposalException {

        InstallProposalRequest installProposalRequest = hfClient.newInstallProposalRequest();
       //用于唯一标识 合约
       ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion);
       //设置智能合约的语言
       installProposalRequest.setChaincodeLanguage(lang);
       //设置智能合约的id
       installProposalRequest.setChaincodeID(builder.build());
       //设置项目的路径
       installProposalRequest.setChaincodeSourceLocation(new File(chaincodeLocation));
       //设置项目的下的src下的智能合约路径
       installProposalRequest.setChaincodePath(chaincodePath);
       Collection<ProposalResponse> responses =  hfClient.sendInstallProposal(installProposalRequest,peers);
       for(ProposalResponse response:responses){
            if(response.getStatus().getStatus()==200){
                 log.info("{} installed sucess",response.getPeer().getName());
            }else{
                log.error("{} installed fail",response.getMessage());
            }
       }
    }

    /**
     * @description 合约的实例化 , 只有合约实例化 调用 Init 方法之后,才能对合约进行其它操作
     * @param channelName
     * @param lang
     * @param chaincodeName
     * @param chaincodeVersion
     * @param order
     * @param peer
     * @param funcName 合约实例化执行的函数
     * @param args  合约实例化执行的参数
     * @throws TransactionException
     * @throws ProposalException
     * @throws InvalidArgumentException
     */
    public void initChaincode(String channelName, TransactionRequest.Type lang, String chaincodeName, String chaincodeVersion, Orderer order, Peer peer, String funcName, String args[]) throws TransactionException, ProposalException, InvalidArgumentException {
       Channel channel = getChannel(channelName);
       channel.addPeer(peer);
       channel.addOrderer(order);
       channel.initialize();
       InstantiateProposalRequest instantiateProposalRequest = hfClient.newInstantiationProposalRequest();
       instantiateProposalRequest.setArgs(args);
       instantiateProposalRequest.setFcn(funcName);
       instantiateProposalRequest.setChaincodeLanguage(lang);
       ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion);
       instantiateProposalRequest.setChaincodeID(builder.build());
       Collection<ProposalResponse> responses =  channel.sendInstantiationProposal(instantiateProposalRequest);
        for(ProposalResponse response:responses){
            if(response.getStatus().getStatus()==200){
                log.info("{} init sucess",response.getPeer().getName());
            }else{
                log.error("{} init fail",response.getMessage());
            }
        }
       channel.sendTransaction(responses);
    }

    /**
     * @description 合约的升级
     * @param channelName
     * @param lang
     * @param chaincodeName
     * @param chaincodeVersion
     * @param order
     * @param peer
     * @param funcName
     * @param args
     * @throws TransactionException
     * @throws ProposalException
     * @throws InvalidArgumentException
     * @throws IOException
     * @throws ChaincodeEndorsementPolicyParseException
     */
    public void upgradeChaincode(String channelName, TransactionRequest.Type lang, String chaincodeName, String chaincodeVersion, Orderer order, Peer peer, String funcName, String args[]) throws TransactionException, ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException {
        Channel channel = getChannel(channelName);
        channel.addPeer(peer);
        channel.addOrderer(order);
        channel.initialize();
        UpgradeProposalRequest upgradeProposalRequest = hfClient.newUpgradeProposalRequest();
        upgradeProposalRequest.setArgs(args);
        upgradeProposalRequest.setFcn(funcName);
        upgradeProposalRequest.setChaincodeLanguage(lang);
        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(new File("src\\main\\resources\\chaincode\\chaincodeendorsementpolicy.yaml"));
        upgradeProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
        ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion);
        upgradeProposalRequest.setChaincodeID(builder.build());
        Collection<ProposalResponse> responses =  channel.sendUpgradeProposal(upgradeProposalRequest);
        for(ProposalResponse response:responses){
            if(response.getStatus().getStatus()==200){
                log.info("{} upgrade sucess",response.getPeer().getName());
            }else{
                log.error("{} upgrade fail",response.getMessage());
            }
        }
        channel.sendTransaction(responses);
    }

    /**
     * @description 合约的调用
     * @param channelName
     * @param lang
     * @param chaincodeName
     * @param order
     * @param peers
     * @param funcName 合约调用执行的函数名称
     * @param args 合约调用执行的参数
     * @throws TransactionException
     * @throws ProposalException
     * @throws InvalidArgumentException
     */
    public void invoke(String channelName, TransactionRequest.Type lang, String chaincodeName, Orderer order, List<Peer> peers, String funcName, String args[])  {
        try {

            Channel channel = getChannel(channelName);
            channel.addOrderer(order);
            for (Peer p : peers) {
                channel.addPeer(p);
            }
                channel.initialize();


            TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
            transactionProposalRequest.setChaincodeLanguage(lang);
            transactionProposalRequest.setArgs(args);
            transactionProposalRequest.setFcn(funcName);
            ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName);
            transactionProposalRequest.setChaincodeID(builder.build());
            Collection<ProposalResponse> responses = channel.sendTransactionProposal(transactionProposalRequest, peers);
            for (ProposalResponse response : responses) {
                if (response.getStatus().getStatus() == 200) {
                    log.info("blockchain node {} remote invoke return result , proposal {} success", response.getPeer().getName(), funcName);
                } else {
                    String logArgs[] = {response.getMessage(), funcName, response.getPeer().getName()};
                    log.error("blockchain node {} remote invoke return result , {} fail on {}", logArgs);
                }
            }
            channel.sendTransaction(responses);
         }catch (Exception e) {
             log.error("通道未打开，请检查区块链网络是否启动！");
         }

    }

    /**
     * @description 合约的调用
     * @param channelName
     * @param lang
     * @param chaincodeName
     * @param order
     * @param peers
     * @param funcName 合约调用执行的函数名称
     * @param args 合约调用执行的参数
     * @throws TransactionException
     * @throws ProposalException
     * @throws InvalidArgumentException
     */
    public void invokeCheckAuth(String channelName, TransactionRequest.Type lang, String chaincodeName, Orderer order, List<Peer> peers, String funcName, String args[]) throws TransactionException, ProposalException, InvalidArgumentException {
        Channel channel = getChannel(channelName);
        channel.addOrderer(order);
        for(Peer p : peers) {
            channel.addPeer(p);
        }
        channel.initialize();
        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeLanguage(lang);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setFcn(funcName);
        ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName);
        transactionProposalRequest.setChaincodeID(builder.build());
        Collection<ProposalResponse> responses = channel.sendTransactionProposal(transactionProposalRequest,peers);
        for(ProposalResponse response:responses){
            if(response.getStatus().getStatus()==200){
                String logArgs[] = {response.getPeer().getUrl(),response.getMessage()};
                log.info("{},{}",logArgs);
            }else{
                String logArgs[] = {response.getPeer().getUrl(),response.getMessage()};
                log.error("{},{}",logArgs);
            }
        }
        channel.sendTransaction(responses);
    }

    /**
     * @description 合约的查询
     * @param peers
     * @param channelName
     * @param lang
     * @param chaincodeName
     * @param funcName
     * @param args
     * @return
     * @throws TransactionException
     * @throws InvalidArgumentException
     * @throws ProposalException
     */
    public Map queryChaincode(List<Peer> peers, String channelName, TransactionRequest.Type lang, String chaincodeName,String funcName, String args[]) throws TransactionException, InvalidArgumentException, ProposalException {
       Channel channel = getChannel(channelName);
        for(Peer p : peers) {
            channel.addPeer(p);
        }
       channel.initialize();
        HashMap map = new HashMap();
       QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();
       ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName);
       queryByChaincodeRequest.setChaincodeID(builder.build());
       queryByChaincodeRequest.setArgs(args);
       queryByChaincodeRequest.setFcn(funcName);
       queryByChaincodeRequest.setChaincodeLanguage(lang);
       Collection<ProposalResponse> responses = channel.queryByChaincode(queryByChaincodeRequest);
       for (ProposalResponse response : responses) {
               if (response.getStatus().getStatus() == 200) {
                   log.info("data is {}", response.getProposalResponse().getResponse().getPayload());
                   map.put(response.getStatus().getStatus(),new String(response.getProposalResponse().getResponse().getPayload().toByteArray()));
                   return map;
               } else {
                   log.error("data get error {}", response.getMessage());
                   map.put(response.getStatus().getStatus(),response.getMessage());
                   return map;
               }
           }
       map.put("code","404");
       return map;
    }


    /**
     * @description 获取orderer节点
     * @param name
     * @param grpcUrl
     * @param tlsFilePath
     * @return
     * @throws InvalidArgumentException
     */
    public Orderer getOrderer(String name,String grpcUrl,String tlsFilePath) throws InvalidArgumentException {
        Properties properties = new Properties();
        properties.setProperty("pemFile",tlsFilePath);
        Orderer orderer = hfClient.newOrderer(name,grpcUrl,properties);
        return orderer;
   }

    /**
     * @description 获取peer节点
     * @param name
     * @param grpcUrl
     * @param tlsFilePath
     * @return
     * @throws InvalidArgumentException
     */
   public Peer getPeer(String name, String grpcUrl, String tlsFilePath) throws InvalidArgumentException {
       Properties properties = new Properties();
       properties.setProperty("pemFile",tlsFilePath);
       Peer peer = hfClient.newPeer(name,grpcUrl,properties);
       return peer;
   }

    /**
     * @description 获取已有的channel
     * @param channelName
     * @return
     * @throws InvalidArgumentException
     * @throws TransactionException
     * @throws ProposalException
     */
   public Channel getChannel(String channelName) throws InvalidArgumentException, TransactionException, ProposalException {
       Channel channel =  hfClient.newChannel(channelName);
       return channel;
   }
}
