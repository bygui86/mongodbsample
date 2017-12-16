 
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
db.user.insert(
		{
			'name': 'Jane', 
			'age': 32,
			'address': {
				'_id': ObjectId(),
				'city': 'testCity2',
				'country': 'testCountry2'
			}
		}
	);

// Add documents to the collection Address

// *** OTHERS ***
