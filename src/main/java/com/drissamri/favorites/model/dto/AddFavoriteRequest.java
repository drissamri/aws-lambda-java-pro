package com.drissamri.favorites.model.dto;

public class AddFavoriteRequest {
    public AddFavoriteRequest() {
    }

    public AddFavoriteRequest(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
