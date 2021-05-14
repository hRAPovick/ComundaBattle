package com.hrapovick.demo.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Warrior implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String title;
    private Boolean isAlive;
    //@JsonAlias("random.number")
    private Integer hp;
}