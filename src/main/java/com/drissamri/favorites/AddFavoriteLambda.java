package com.drissamri.favorites;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.drissamri.favorites.config.AppConfig;
import com.drissamri.favorites.model.Favorite;
import com.drissamri.favorites.model.dto.AddFavoriteRequest;
import com.drissamri.favorites.service.FavoriteService;
import com.fasterxml.jackson.jr.ob.JSON;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.GlobalMeterProvider;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongUpDownCounter;
import io.opentelemetry.api.metrics.Meter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.http.HttpStatusCode;

import static io.opentelemetry.api.common.AttributeKey.*;

public class AddFavoriteLambda {
    private static final Logger LOG = LoggerFactory.getLogger(AddFavoriteLambda.class);
    private FavoriteService favoriteService;
    private JSON jsonMapper;

    // https://github.com/open-telemetry/opentelemetry-lambda/blob/main/java/sample-apps/aws-sdk/src/main/java/io/opentelemetry/lambda/sampleapps/awssdk/AwsSdkRequestHandler.java
    private static final AttributeKey<String> API_NAME = stringKey("apiName");
    private static final Meter sampleMeter = GlobalMeterProvider.get().get("aws-otel", "1.0", null);
    private static final LongCounter queueSizeCounter = sampleMeter
            .counterBuilder("queueSizeChange")
            .setDescription("Queue Size change")
            .setUnit("one")
            .build();
    private static final Attributes METRIC_ATTRIBUTES = Attributes.builder().put(API_NAME, "favorites-service").build();

    public AddFavoriteLambda() {
        this(AppConfig.favoriteService(), JSON.std);
    }

    public AddFavoriteLambda(FavoriteService favoriteService, JSON jsonMapper) {
        this.favoriteService = favoriteService;
        this.jsonMapper = jsonMapper;
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        APIGatewayProxyResponseEvent response;
        try {
            AddFavoriteRequest addFavoriteRequest = jsonMapper.beanFrom(AddFavoriteRequest.class, input.getBody());
            Favorite savedFavorite = favoriteService.add(addFavoriteRequest);
            LOG.info("Favorite created: {}", savedFavorite);
            LOG.info("Favorite created: {}", savedFavorite);

            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatusCode.OK)
                    .withBody(jsonMapper.asString(savedFavorite));

            queueSizeCounter.add(2, METRIC_ATTRIBUTES);


        } catch (Exception ex) {
            LOG.error("Exception: {}", ex.getMessage());
            response = createErrorResponse();
        }
        return response;
    }

    private APIGatewayProxyResponseEvent createErrorResponse() {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
}