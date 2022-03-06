package com.distributed.db.slave.controller;

import com.distributed.db.slave.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlaveController {

    @Autowired
    StatusService statusService;

    @PatchMapping("/changeStatus")
    ResponseEntity changeStatus(@RequestParam String action){
        if(!(action.equals("enable")||action.equals("disable")))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        statusService.changeStatus(action);
        return new ResponseEntity(HttpStatus.OK);
    }


}
