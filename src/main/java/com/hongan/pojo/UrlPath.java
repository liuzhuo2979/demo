package com.hongan.pojo;

import org.springframework.stereotype.Component;

@Component
public class UrlPath {

    String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
