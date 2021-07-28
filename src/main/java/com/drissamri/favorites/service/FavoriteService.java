package com.drissamri.favorites.service;

import com.drissamri.favorites.model.dto.AddFavoriteRequest;
import com.drissamri.favorites.model.Favorite;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class FavoriteService {
    private DynamoDbAsyncTable<Favorite> favoritesTable;

    public FavoriteService(DynamoDbAsyncTable<Favorite> favoritesTable) {
        this.favoritesTable = favoritesTable;
    }

    public Favorite add(AddFavoriteRequest request) throws ExecutionException, InterruptedException {
        Favorite newFavorite = new Favorite();
        newFavorite.setId(UUID.randomUUID().toString());
        newFavorite.setName(request.getName());

        favoritesTable.putItem(newFavorite).get();
        return newFavorite;
    }
}
