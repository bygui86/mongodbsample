package com.rabbithole.mongodb.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.rabbithole.mongodb.daos.Address;
import com.rabbithole.mongodb.repos.AddressRepository;
import com.rabbithole.mongodb.services.AddressService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("addressService")
public class AddressServiceImpl implements AddressService {

	@Resource
	@Getter(value = AccessLevel.PROTECTED)
	@Setter
	private AddressRepository addressRepository;
	
	@Override
	public Address insert(final Address address) {

		log.debug("Insert new Address: " + address);
		
		return getAddressRepository().insert(address);
	}
	
	@Override
	public Address insertOrUpdate(final Address address) {
		
		log.debug("Insert or Update Address: " + address);

		return getAddressRepository().save(address);
	}
	
	@Override
	public boolean exists(final Address address) {
		
		log.debug("Exists Address: " + address);
		
		return getAddressRepository().exists(
				Example.of(address));
	}
	
	@Override
	public List<Address> findAll() {
		
		log.debug("Find all Addresses");

		return getAddressRepository().findAll();
	}
	
	@Override
	public Address findByExample(final Address address) {
		
		log.debug("Find Address by example: " + address);

		return getAddressRepository().findOne(
				Example.of(address));
	}
	
	@Override
	public void deleteAll() {
		
		log.debug("Delete all Addresses");

		getAddressRepository().deleteAll();
	}
	
}
