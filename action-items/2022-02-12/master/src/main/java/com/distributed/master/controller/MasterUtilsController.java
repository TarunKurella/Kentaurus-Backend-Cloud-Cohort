package com.distributed.master.controller;

import com.distributed.master.entity.InsertWordsRequest;
import com.distributed.master.entity.Slave;
import com.distributed.master.entity.Word;
import com.distributed.master.service.RestService;
import com.distributed.master.service.SlaveManager;
import com.distributed.master.service.SlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class MasterUtilsController {

    @Autowired
    SlaveManager slaveManager;
    @Autowired
    RestService restService;
    @Autowired
    SlaveService slaveService;

    @PatchMapping("/node/{nodeId}")
    ResponseEntity changeStatus(@PathVariable("nodeId") int nodeId, @RequestParam String action){
        if(nodeId>=slaveManager.getNoOfNodes()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if(!(action.equals("enable")||action.equals("disable")))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        restService.updateStatusforNodeId(nodeId,action);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/node/status")
    ResponseEntity<List<Integer>> getStatus(){
        List<Integer> nodes = slaveManager.getStatusofNodes();
        return new ResponseEntity(nodes,HttpStatus.OK);
    }

    @PostMapping("/node/add")
    ResponseEntity addNode(@RequestBody Slave slave){
        int id = slaveManager.addSlave(slave.getUrl());
        slave.setId(id);
        return new ResponseEntity(slave,HttpStatus.OK);
    }

    //post array of words
    @PostMapping("/words")
    ResponseEntity addWords(@RequestBody InsertWordsRequest insertWordsRequest){
       slaveService.insertWords(insertWordsRequest.getWords());
        return new ResponseEntity(HttpStatus.OK);
    }


    //get all words
    @GetMapping("/words")
    ResponseEntity<List<Word>> getAllWords(){
        return new ResponseEntity(slaveService.getAllWords(),HttpStatus.OK);
    }

    @DeleteMapping("/words")
    ResponseEntity deleteWords(){
        slaveService.deleteAllWords();
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/words/{word}")
    ResponseEntity deleteWord(@PathVariable("word") String word){
        boolean isExists =  slaveService.deleteWordFrom(word);
        return new ResponseEntity(isExists,HttpStatus.OK);
    }


}
