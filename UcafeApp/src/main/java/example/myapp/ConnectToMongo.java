package example.myapp;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Updates.set;

public class ConnectToMongo {

    private static final String MONGO_URI = "mongodb+srv://yakut:kosmosnasH@myfirstcluster.nmy5s.mongodb.net/count_db?retryWrites=true&w=majority&maxIdleTimeMS=0&socketTimeoutMS=3600000&serverSelectionTimeoutMS=36000000";

    ConnectionString uri = new ConnectionString(MONGO_URI);
    MongoClient mongoClient = MongoClients.create(uri);
    MongoDatabase database = mongoClient.getDatabase("count_db");
    MongoCollection<Document> collection = database.getCollection("bank");
    Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
    {
        mongoLogger.setLevel(Level.WARNING);
    }

    public void setNumber(int number){
        List<Document> values = collection.find().into(new ArrayList<>());

        values.forEach(c -> {
            Object id = c.get("_id");
            Document filter = new Document("_id", id);
            Bson update = set("day", "" + number);
            FindOneAndUpdateOptions findOptions = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
            collection.findOneAndUpdate(filter, update, findOptions);
        });
    }

    public int getNumber() {
        final int[] result = new int[1];
        List<Document> values = collection.find().into(new ArrayList<>());
        values.forEach(d -> {
            Object number = d.get("day");
            result[0] = Integer.parseInt((String) number);
        });
        return result[0];
    }

}
