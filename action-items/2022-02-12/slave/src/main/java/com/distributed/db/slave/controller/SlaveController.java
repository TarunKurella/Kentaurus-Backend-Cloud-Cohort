package com.distributed.db.slave.controller;

import com.distributed.db.slave.entity.Word;
import com.distributed.db.slave.service.StatusService;
import com.distributed.db.slave.service.StorageService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SlaveController {

    @Autowired
    StatusService statusService;
    @Autowired
    StorageService storageService;

    @PatchMapping("/changeStatus")
    ResponseEntity changeStatus(@RequestParam String action){
        if(!(action.equals("enable")||action.equals("disable")))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        statusService.changeStatus(action);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/addWord")
    ResponseEntity AddWord(@RequestBody Word word){
        System.out.println("request recieved for word"+word.getWord());
        storageService.add(word);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/getWords")
    List<Word> getWords(){
        return storageService.returnAllWords();
    }

    @DeleteMapping("/words")
    ResponseEntity deleteWords(){
        storageService.deleteAll();
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/words/{word}")
    ResponseEntity deleteWord(@PathVariable("word") String word){
       boolean isExists =  storageService.deleteWordswithValue(word);
        return new ResponseEntity(isExists,HttpStatus.OK);
    }




}
