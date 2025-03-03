package umm3601.host;

// import java.lang.reflect.Array;
import java.util.ArrayList;

// import static com.mongodb.client.model.Filters.and;
// import static com.mongodb.client.model.Filters.eq;
// import static com.mongodb.client.model.Filters.regex;

// import java.nio.charset.StandardCharsets;
// import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;
// import java.util.ArrayList;
// import java.util.List;
import java.util.Map;
// import java.util.Objects;
// import java.util.regex.Pattern;

// import org.bson.Document;
import org.bson.UuidRepresentation;
// import org.bson.conversions.Bson;
// import org.bson.types.ObjectId;
import org.mongojack.JacksonMongoCollection;

import com.mongodb.client.MongoDatabase;
// import com.mongodb.client.model.Sorts;
// import com.mongodb.client.result.DeleteResult;

import io.javalin.Javalin;
// import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
// import io.javalin.http.NotFoundResponse;
import umm3601.Controller;

public class HostController implements Controller {
  private static final String API_PROMPTS = "/api/prompts";


  private final JacksonMongoCollection<Host> hostCollection;

  public HostController(MongoDatabase database) {
    hostCollection = JacksonMongoCollection.builder().build(
        database,
        "prompts",
        Host.class,
        UuidRepresentation.STANDARD);
  }

  public void getPrompts(Context ctx) {
    ArrayList<Host> prompts = hostCollection.find().into(new ArrayList<>());
    ctx.json(prompts);
  }





  public void addNewPrompt(Context ctx) {

    String body = ctx.body();
    Host newPrompt = ctx.bodyValidator(Host.class)
      .check(prm -> prm.text != null && prm.text.length() > 0,
        "Prompt must have a non-empty value; body was " + body)
      .get();

    hostCollection.insertOne(newPrompt);

    ctx.status(HttpStatus.CREATED);
    ctx.json(Map.of("id", newPrompt._id));
  }

  @Override
  public void addRoutes(Javalin server) {

    // Get all prompts
    server.get(API_PROMPTS, this::getPrompts);

    // Add new prompt
    server.post(API_PROMPTS, this::addNewPrompt);
  }
}
