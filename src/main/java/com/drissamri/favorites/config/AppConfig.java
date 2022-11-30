package com.drissamri.favorites.config;

import com.drissamri.favorites.model.Favorite;
import com.drissamri.favorites.service.FavoriteService;
import software.amazon.awssdk.auth.credentials.ContainerCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.http.crt.AwsCrtAsyncHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

public class AppConfig {
    public static FavoriteService favoriteService() {
        DynamoDbAsyncClient dynamoDbClient = DynamoDbAsyncClient.builder()
                .httpClient(AwsCrtAsyncHttpClient.builder().build())
                .build();

        DynamoDbEnhancedAsyncClient client = DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        DynamoDbAsyncTable<Favorite> dynamoDbTable = client.table(
                System.getenv("FAVORITE_TABLE"),
                TableSchema.fromBean(Favorite.class));
        try {

            return new FavoriteService(dynamoDbTable);
        } catch (Exception e) {
            throw new RuntimeException("Init dynamodb failed");
        }
    }
}
