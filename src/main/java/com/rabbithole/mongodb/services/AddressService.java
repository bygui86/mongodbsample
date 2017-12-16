package com.rabbithole.mongodb.services;

import java.util.List;

import com.rabbithole.mongodb.daos.Address;

public interface AddressService {
	
	/**
	 * Insert a new Address
	 *
	 * @param address
	 *
	 * @return Inserted {@link com.rabbithole.mongodb.daos.Address}
	 */
	Address insert(final Address address);
	
	/**
	 * Insert a new Address or Update the existing one
	 *
	 * @param address
	 *
	 * @return Inserted or Updated {@link com.rabbithole.mongodb.daos.Address}
	 */
	Address insertOrUpdate(final Address address);
	
	/**
	 * Check if anAddress exists
	 *
	 * @param address
	 *
	 * @return TRUE if the Address exists, FALSE otherwise
	 */
	boolean exists(final Address address);

	/**
	 * Find all Addresses
	 *
	 * @return List of all {@link com.rabbithole.mongodb.daos.Address} found
	 */
	List<Address> findAll();

	/**
	 * Find Address by example
	 *
	 * @param address
	 *
	 * @return {@link com.rabbithole.mongodb.daos.Address}
	 */
	Address findByExample(final Address address);

	/**
	 * Delete all Addresses
	 */
	void deleteAll();
	
}
