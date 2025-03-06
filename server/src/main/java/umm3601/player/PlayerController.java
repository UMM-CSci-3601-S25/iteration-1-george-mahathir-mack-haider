package umm3601.player;

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

public class PlayerController implements Controller {
  private static final String API_PLAYERS = "/api/players";


  private final JacksonMongoCollection<Player> playerCollection;

  public PlayerController(MongoDatabase database) {
    playerCollection = JacksonMongoCollection.builder().build(
        database,
        "players",
        Player.class,
        UuidRepresentation.STANDARD);
  }

  public void getPlayers(Context ctx) {
    ArrayList<Player> players = playerCollection.find().into(new ArrayList<>());
    ctx.json(players);
  }



  public void addNewPlayer(Context ctx) {

    String body = ctx.body();
    Player newPlayer = ctx.bodyValidator(Player.class)
      .check(prm -> prm.name != null && prm.name.length() > 0,
        "players must have a non-empty value; body was " + body)
      .get();

      playerCollection.insertOne(newPlayer);

    ctx.json(Map.of("id", newPlayer._id));
    ctx.status(HttpStatus.CREATED);
  }

  @Override
  public void addRoutes(Javalin server) {

    // Get all players
    server.get(API_PLAYERS, this::getPlayers);

    // Add new players
    server.post(API_PLAYERS, this::addNewPlayer);
  }
}
