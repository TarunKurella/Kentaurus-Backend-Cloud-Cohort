package com.distributed.master.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Slave {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String url;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    int id;
}
