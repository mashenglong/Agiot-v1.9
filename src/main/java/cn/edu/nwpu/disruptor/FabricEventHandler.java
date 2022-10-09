package cn.edu.nwpu.disruptor;

import cn.edu.nwpu.fabric.dao.FabricBlockChainDao;
import cn.edu.nwpu.utils.DataUtil;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

@Slf4j
public class FabricEventHandler implements WorkHandler<QueueEvent>, EventHandler<QueueEvent> {
    public FabricEventHandler() {
    }
    @Override
    public void onEvent(QueueEvent queueEvent, long l, boolean b) throws Exception {
        this.onEvent(queueEvent);
    }
    @Override
    public void onEvent(QueueEvent queueEvent) {
        try {
            FabricBlockChainDao.insert(queueEvent.getSensorData());
            log.info("data number insert success:"+ DataUtil.tranDateForm2Number(queueEvent.getSensorData().getsDate()));
            log.info("upload data to fabric blockChain, data is " + queueEvent.getSensorData().toString());
        }catch (TransactionException e) {
            log.info("Failed to connect to the blockchain network, TransactionException failed!");
        }catch (ProposalException e){
            log.info("Failed to connect to the blockchain network, ProposalException failed!");
        }catch (InvalidArgumentException e){
            log.info("Failed to connect to the blockchain network, InvalidArgumentException failed!");
        }
        catch (Throwable e) {
            log.info("Failed to connect to the blockchain network, please check whether the blockchain network is up");
        }

    }
}