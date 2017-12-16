package com.rabbithole.mongodb.examples.programs;

import java.net.UnknownHostException;
import java.util.Date;

import org.bson.BSON;
import org.joda.time.DateTime;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * from
 * https://gist.github.com/jyemin/84ccac245782f1ebdc98
 */
@SuppressWarnings("all")
public class MongoDBWithJodaExample {
	
	public static void main(final String[] args) throws UnknownHostException {
		
		// Add hook to encode Joda DateTime as a java.util.Date
		BSON.addEncodingHook(DateTime.class, o -> new Date(((DateTime) o).getMillis()));
		
		// Add hook to decode java.util.Date as a Joda DateTime
		BSON.addDecodingHook(Date.class, o -> new DateTime(((Date) o).getTime()));
		
		final MongoClient mongoClient = new MongoClient();
		final DBCollection c = mongoClient.getDB("test").getCollection("joda");
		c.drop();
		
		// insert a document containing a Joda DateTime
		c.insert(new BasicDBObject("createdAt", new DateTime()));
		
		final DBObject doc = c.findOne();
		
		// Note that when querying for this document, it will come back as a Joda DateTime as well.
		System.out.println(doc.get("createdAt").getClass());

		mongoClient.close();
	}
	
}
