package com.distributed.master.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LoadBalancingService {
    private int counter = 1;
    private final ReentrantLock lock;
    @Autowired
    SlaveManager slaveManager;

    public LoadBalancingService(List<String> list) {

        lock = new ReentrantLock();
    }


    public String getIp() {
        lock.lock();
        try {
            String ip = slaveManager.getUrl(counter);
            counter += 1;
            if (counter == slaveManager.getNoOfNodes()) {
                counter = 1;
            }
            return ip;
        } finally {
            lock.unlock();
        }
    }
}
