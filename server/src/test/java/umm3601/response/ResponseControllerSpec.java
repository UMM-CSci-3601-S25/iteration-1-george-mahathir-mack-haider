package umm3601.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

// import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;

// import io.javalin.validation.ValidationException;
import io.javalin.json.JavalinJackson;

class ResponseControllerSpec {

  private ResponseController responseController;

  private static MongoClient mongoClient;
  private static MongoDatabase db;

  private static JavalinJackson javalinJackson = new JavalinJackson();

  @Mock
  private Context ctx;

  @Captor
  private ArgumentCaptor<Map<String, String>> mapCaptor;

  @BeforeAll
  static void setupAll() {
    String mongoAddr = System.getenv().getOrDefault("MONGO_ADDR", "localhost");

    mongoClient = MongoClients.create(
        MongoClientSettings.builder()
            .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(new ServerAddress(mongoAddr))))
            .build());
    db = mongoClient.getDatabase("test");
  }

  @AfterAll
  static void teardown() {
    db.drop();
    mongoClient.close();
  }

  @BeforeEach
  void setupEach() throws IOException {
    MockitoAnnotations.openMocks(this);

    MongoCollection<Document> responseDocuments = db.getCollection("Responses");
    responseDocuments.drop();

    responseController = new ResponseController(db);
  }


  @Test
  void addNewResponse() throws IOException {
    Response newResponse = new Response();
    newResponse.text = "Test Response";

    String newResponseJson = javalinJackson.toJsonString(newResponse, Response.class);

    when(ctx.bodyValidator(Response.class))
        .thenReturn(new BodyValidator<>(newResponseJson, Response.class,
            () -> javalinJackson.fromJsonString(newResponseJson, Response.class)));

    responseController.addNewResponse(ctx);
    verify(ctx).json(mapCaptor.capture());

    verify(ctx).status(HttpStatus.CREATED);

    Document addedResponse = db.getCollection("Responses")
        .find(new Document("_id", new ObjectId(mapCaptor.getValue().get("id")))).first();

    assertNotNull(addedResponse);
    assertEquals(newResponse.text, addedResponse.get("text"));
  }

  @Test
  void deleteResponse() throws IOException {
    Response newResponse = new Response();
    newResponse.text = "Test Response";

    String newResponseJson = javalinJackson.toJsonString(newResponse, Response.class);

    when(ctx.bodyValidator(Response.class))
        .thenReturn(new BodyValidator<>(newResponseJson, Response.class,
            () -> javalinJackson.fromJsonString(newResponseJson, Response.class)));

    responseController.addNewResponse(ctx);
    verify(ctx).json(mapCaptor.capture());

    verify(ctx).status(HttpStatus.CREATED);

    Document addedResponse = db.getCollection("Responses")
        .find(new Document("_id", new ObjectId(mapCaptor.getValue().get("id")))).first();

    assertNotNull(addedResponse);
    assertEquals(newResponse.text, addedResponse.get("text"));

    when(ctx.pathParam("id")).thenReturn(mapCaptor.getValue().get("id"));

    responseController.deleteResponse(ctx);
    verify(ctx).status(HttpStatus.OK);

    Document deletedResponse = db.getCollection("Responses")
        .find(new Document("_id", new ObjectId(mapCaptor.getValue().get("id")))).first();

    assertEquals(null, deletedResponse);
  }

  @Test
  void getResponse() throws IOException {
    Response newResponse = new Response();
    newResponse.text = "Test Response";

    String newResponseJson = javalinJackson.toJsonString(newResponse, Response.class);

    when(ctx.bodyValidator(Response.class))
        .thenReturn(new BodyValidator<>(newResponseJson, Response.class,
            () -> javalinJackson.fromJsonString(newResponseJson, Response.class)));

    responseController.addNewResponse(ctx);
    verify(ctx).json(mapCaptor.capture());

    verify(ctx).status(HttpStatus.CREATED);

    Document addedResponse = db.getCollection("Responses")
        .find(new Document("_id", new ObjectId(mapCaptor.getValue().get("id")))).first();

    assertNotNull(addedResponse);
    assertEquals(newResponse.text, addedResponse.get("text"));

    responseController.getResponse(ctx);
    verify(ctx).json(mapCaptor.capture());

    verify(ctx).status(HttpStatus.OK);
  }

  @Test
  void getResponseById() throws IOException {
    Response newResponse = new Response();
    newResponse.text = "Test Response";

    String newResponseJson = javalinJackson.toJsonString(newResponse, Response.class);

    when(ctx.bodyValidator(Response.class))
        .thenReturn(new BodyValidator<>(newResponseJson, Response.class,
            () -> javalinJackson.fromJsonString(newResponseJson, Response.class)));

    responseController.addNewResponse(ctx);
    verify(ctx).json(mapCaptor.capture());

    verify(ctx).status(HttpStatus.CREATED);

    Document addedResponse = db.getCollection("Responses")
        .find(new Document("_id", new ObjectId(mapCaptor.getValue().get("id")))).first();

    assertNotNull(addedResponse);
    assertEquals(newResponse.text, addedResponse.get("text"));

    when(ctx.pathParam("id")).thenReturn(mapCaptor.getValue().get("id"));

    responseController.getResponseById(ctx);
    verify(ctx).json(mapCaptor.capture());

    verify(ctx).status(HttpStatus.OK);
  }
}
