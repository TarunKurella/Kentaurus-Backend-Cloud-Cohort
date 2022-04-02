package com.distributed.master.service;

import com.distributed.master.entity.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SlaveService {
    @Autowired
    LoadBalancingService loadBalancingService;
    @Autowired
    SlaveManager slaveManager;
    @Autowired
    RestService restService;
   // Snowflake s = new Snowflake(slaveManager.getNoOfNodes());



    public void insertWords(String[] words) {
        for (String word : words) {
            String ip = loadBalancingService.getIp();
            Word wordd = new Word();
            wordd.setWord(word);
            wordd.setId(Long.toString(new java.sql.Timestamp(System.currentTimeMillis()).getTime()));
            restService.addWordTo(wordd,ip);
        }
    }

    public List<Word> getAllWords() {
        List<Word> combinedOutput = new ArrayList<>();
        int noOFslaves = slaveManager.getNoOfNodes()-1;
        //can make below line run parallel?
        for (int i = 0; i < noOFslaves; i++) {
            combinedOutput.addAll(restService.getWordsFrom(loadBalancingService.getIp()));
        }
        return combinedOutput;
    }

    public boolean deleteWordFrom(String word) {

        int noOFslaves = slaveManager.getNoOfNodes()-1;
        boolean isWordExists=false;
        //can make below line run parallel?
        for (int i = 0; i < noOFslaves; i++) {
            isWordExists|=restService.deleteWordFrom(word,loadBalancingService.getIp());
        }
        return isWordExists;
    }

    public void deleteAllWords() {

        int noOFslaves = slaveManager.getNoOfNodes()-1;
        //can make below line run parallel?
        for (int i = 0; i < noOFslaves; i++) {
            restService.deleteAllWordsFrom(loadBalancingService.getIp());
        }
    }
}
