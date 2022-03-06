package com.distributed.master.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SlaveManager {


    //node. 0 is master. So default no of nodes always 1
    private AtomicInteger noOfNodes = new AtomicInteger(1);
    ConcurrentHashMap<Integer,Boolean> slaveStatusCache = new ConcurrentHashMap();
    ConcurrentHashMap<Integer,String> idToUrl= new ConcurrentHashMap();

    public Integer addSlave(String url){
       int id = incrementNoOfNodes()-1;
        idToUrl.put(id,url);
        slaveStatusCache.put(id,true);

        //returns id
        return id;
    }

    public Boolean removeSlave(int id){
        return true;
    }

    public Boolean changeSlaveStatus(int id, boolean status){
        slaveStatusCache.put(id,status);
        return true;
    }

    public int getNoOfNodes(){
        return noOfNodes.get();
    }

    public String getUrl(int id){
        return idToUrl.get(id);
    }

    private int incrementNoOfNodes() {
        while (true) {
            int existingValue = noOfNodes.get();
            int newValue = existingValue + 1;
            if (noOfNodes.compareAndSet(existingValue, newValue)) {
                return newValue;
            }
        }
    }
}
