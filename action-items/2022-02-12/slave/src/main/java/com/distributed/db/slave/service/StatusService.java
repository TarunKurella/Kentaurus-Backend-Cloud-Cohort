package com.distributed.db.slave.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class StatusService {
    AtomicBoolean status = new AtomicBoolean(true);


    public void changeStatus(String action) {
        status.set(action.equals("enable")?true:false);
        log.info("status set to {}",action);
    }
}
