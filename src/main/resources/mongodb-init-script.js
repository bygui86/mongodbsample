 
// MongoDB script (JavaScript) to enable RabbitHole-MongoDB usage

// *** CONNECTION ***

// Connection to MongoDB (no need becuase already done by mongo shell) and create a database
// var db = connect('127.0.0.1:27017/rabbithole');

// *** COLLECTIONS ***

// Create collections (can be omitted directly adding a document)
// db.createCollection("user");
// db.createCollection("address");

// *** DATA ***

// Add documents to the collection User
db.user.insert({'name': 'Matteo', 'age': 31});
db.user.insert({'name': 'Paolo', 'age': 30});
db.user.insert({'name': 'John', 'age': 25});
// Add documents to the collection Address
db.address.insert({'city': 'Zurich', 'country': 'Switzerland'});
db.address.insert({'city': 'Cagliari', 'country': 'Italy'});
db.address.insert({'city': 'Milan', 'country': 'Italy'});

// *** OTHERS ***

// Show all documents in the collections Domains 
// var allDocs = db.user.find();
// while (allDocs.hasNext()) {
//	printjson(allDocs.next());
// }
