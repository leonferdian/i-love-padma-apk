package com.project45.ilovepadma.data;

import java.io.Serializable;

public class Data_jenis_complain implements Serializable {

    private String name, id;

    public Data_jenis_complain() {

    }

    public Data_jenis_complain(String id, String name){
        this.setId(id);
        this.setName(name);

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
