import com.mongodb.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.net.UnknownHostException;

public class HelloJob implements Job
{
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        System.out.println("Hello Quartz master!");

        //connect to the mongoDB server
        MongoClient mongo = null;
        try{
            mongo = new MongoClient("localhost" , 27017);
        }catch(UnknownHostException exception){
            exception.printStackTrace();
        }
        DB db = mongo.getDB("quartzDemo");
        DBCollection person = db.getCollection("person");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("name","Krishna");

        DBCursor cursor = person.find(searchQuery);
        while(cursor.hasNext()){
            System.out.println(cursor.next());
            DBObject personDoc = cursor.next();
            String recipientEmail = (String)personDoc.get("email");
        }
    }

}