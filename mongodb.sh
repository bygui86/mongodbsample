#!bin/sh

# Prerequisites:
#	. Java
#	. Gradle
#	. MongoDB
#		.. database "rabbithole" (see mongodb-script.js)
#		.. collection "domains" (see mongodb-script.js)

# Compile with Gradle
./gradlew build



# Start MongoDB, using alias for  
#	mongod 
#		--config "/Users/matteobaiguini/tools/mongodb-data/conf/mongod.conf" 
#		--dbpath "/Users/matteobaiguini/tools/mongodb-data/db" 
#		--logpath "/Users/matteobaiguini/tools/mongodb-data/logs/mongodb.log" 
#		--pidfilepath "/Users/matteobaiguini/tools/mongodb-data/conf/mongod-pid.txt")
mongod-start



# Test the application
java -jar build/libs/rh-mongodb-0.1.jar



# Stop MongoDB, using alias for  
#	mongo
#		127.0.0.1/admin 
#		--eval "db.shutdownServer()
mongod-stop
