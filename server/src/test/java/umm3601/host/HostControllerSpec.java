package umm3601.host;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;
// import io.javalin.validation.ValidationException;
import io.javalin.json.JavalinJackson;

class HostControllerSpec {

  private HostController hostController;

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

    MongoCollection<Document> promptDocuments = db.getCollection("prompts");
    promptDocuments.drop();

    hostController = new HostController(db);
  }

  // @Test
  // void addsRoutes() {
  //   Javalin mockServer = mock(Javalin.class);
  //   hostController.addRoutes(mockServer);
  //   verify(mockServer).post("/api/prompts", hostController::addNewPrompt);
  // }

  @Test
  void addNewPrompt() throws IOException {
    Host newPrompt = new Host();
    newPrompt.text = "Test Prompt";

    String newPromptJson = javalinJackson.toJsonString(newPrompt, Host.class);

    when(ctx.bodyValidator(Host.class))
        .thenReturn(new BodyValidator<>(newPromptJson, Host.class,
            () -> javalinJackson.fromJsonString(newPromptJson, Host.class)));

    hostController.addNewPrompt(ctx);
    verify(ctx).json(mapCaptor.capture());

    verify(ctx).status(HttpStatus.CREATED);

    Document addedPrompt = db.getCollection("prompts")
        .find(new Document("_id", new ObjectId(mapCaptor.getValue().get("id")))).first();

    assertNotNull(addedPrompt);
    assertEquals(newPrompt.text, addedPrompt.get("text"));
  }

//   @Test
//   void addInvalidPrompt() throws IOException {
//     String invalidPromptJson = "{}";

//     when(ctx.body()).thenReturn(invalidPromptJson);
//     when(ctx.bodyValidator(Host.class))
//         .thenReturn(new BodyValidator<>(invalidPromptJson, Host.class,
//             () -> javalinJackson.fromJsonString(invalidPromptJson, Host.class)));

//     ValidationException exception = assertThrows(ValidationException.class, () -> {
//       hostController.addNewPrompt(ctx);
//     });

//     String exceptionMessage = exception.getErrors().get("REQUEST_BODY").get(0).toString();
//     assertTrue(exceptionMessage.contains("Prompt must have a non-empty value"));
//   }
 }
