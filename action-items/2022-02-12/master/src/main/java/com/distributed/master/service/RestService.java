package com.distributed.master.service;

import com.distributed.master.entity.Slave;
import com.distributed.master.entity.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
public class RestService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    SlaveManager slaveManager;

     public void updateStatusforNodeId(int id,String action){


        HttpHeaders headers = new HttpHeaders();
       // headers.set("Accept", "application/json");

        HttpEntity<String> request = new HttpEntity<>(null, headers);

        String url = slaveManager.getUrl(id)+"/changeStatus";
        UriComponents builder= UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("action",action).build();

       log.info("url for patching is "+builder.toUriString());

           try{
               HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, request , String.class);
               slaveManager.changeSlaveStatus(id,action);
           }
           catch(HttpStatusCodeException e){
               log.info("oops");
               throw e;
           }
     }

     public List<Word> getWordsFrom(String url){
         List outputList;
         HttpHeaders headers = new HttpHeaders();
         HttpEntity<String> request = new HttpEntity<>(null, headers);
         UriComponents builder= UriComponentsBuilder.fromHttpUrl(url+"/getWords").build();
         try{
             HttpEntity<Word[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request , Word[].class);
             outputList = Arrays.asList(response.getBody());
         }
         catch(HttpStatusCodeException e){
             log.info("oops");
             throw e;
         }
      return outputList;
     }

     public void addWordTo(Word word,String url){
         HttpHeaders headers = new HttpHeaders();
         HttpEntity<String> request = new HttpEntity<>(null, headers);
         UriComponents builder= UriComponentsBuilder.fromHttpUrl(url+"/addWord").build();
         try{
             HttpEntity<String> response = restTemplate.postForEntity(builder.toUriString(),word,String.class);
         }
         catch(HttpStatusCodeException e){
             log.info("oops");
             throw e;
         }
     }
}
