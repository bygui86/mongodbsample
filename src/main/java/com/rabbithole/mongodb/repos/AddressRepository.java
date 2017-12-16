package com.rabbithole.mongodb.repos;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.rabbithole.mongodb.daos.Address;

public interface AddressRepository extends MongoRepository<Address, ObjectId>, QueryDslPredicateExecutor<Address> {
	
	Address findById(final ObjectId id);
	
	Address findById(final Object objId);

	Address findById(final String hexStringId);
	
	List<Address> findByStreetName(final String streetName);

	List<Address> findByCity(final String city);

	List<Address> findByCountry(final String country);

}
