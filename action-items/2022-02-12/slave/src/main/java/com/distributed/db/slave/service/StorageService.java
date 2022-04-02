package com.distributed.db.slave.service;

import com.distributed.db.slave.entity.Word;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class StorageService {
    Map<String, Word> m = new HashMap<>();
    int size = 25;
    Map<String, Word> idToWord = Collections.synchronizedMap(m);
    Deque<String> deque
            = new LinkedList<String>();

    Lock lock = new ReentrantLock();

    public void add(Word word) {

        try {
            lock.lock();
            if (deque.size() >= size) {
                String id = deque.pollLast();
                idToWord.remove(id);
            }
            deque.offerFirst(word.getId());
            idToWord.put(word.getId(), word);
        } finally {
            lock.unlock();
        }

    }

    public boolean deleteWordswithValue(String word) {


        AtomicBoolean isWordExists = new AtomicBoolean(false);
        try {
            lock.lock();
            Iterator<Map.Entry<String, Word>> iter = idToWord.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Word> entry = iter.next();
                Word v = entry.getValue();
                if (v.getWord().equals(word)) {
                    removeForId(v.getId());
                    isWordExists.set(true);
                }
            }
//            idToWord.forEach((k, v) -> {
//                if (v.getWord().equals(word)) {
//                    removeForId(v.getId());
//                    isWordExists.set(true);
//                }
//            });


        } finally {
            lock.unlock();
        }
        return isWordExists.get();
    }

    public void deleteAll() {
          try{
              lock.lock();
              idToWord.clear();
              deque.clear();
          }finally {
              lock.unlock();
          }
    }

    public void rollbackForEpoch(long epoch) {
        try {
            lock.lock();
            idToWord.forEach((k, v) -> {
                if (v.getEpoch()==epoch) {
                    removeForId(v.getId());
                }
            });


        } finally {
            lock.unlock();
        }
    }


    void removeForId(String id) {
        idToWord.remove(id);
        deque.remove(id);
    }


    public List<Word> returnAllWords() {
        List<Word> outputList = new ArrayList<>();
        outputList.addAll(idToWord.values());
        return outputList;
    }
}
