package com.distributed.db.slave.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    String id;
    String word;
    long epoch;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return epoch == word1.epoch && id.equals(word1.id) && word.equals(word1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, epoch);
    }
}
