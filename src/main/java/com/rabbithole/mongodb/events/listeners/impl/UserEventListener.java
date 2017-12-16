package com.rabbithole.mongodb.events.listeners.impl;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.rabbithole.mongodb.daos.Address;
import com.rabbithole.mongodb.daos.User;
import com.rabbithole.mongodb.services.AddressService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("userEventListener")
public class UserEventListener extends AbstractMongoEventListener<User> {
	
	@Resource
	@Getter
	@Setter
	private AddressService addressService;
	
	/*
	 * Direction [Java >>> MongoDB]
	 */
	@Override
	public void onBeforeConvert(final BeforeConvertEvent<User> event) {
		
		log.debug("EventListener BeforeConvert [USER]: " + event.getSource());
		
		final User user = event.getSource();
		if (user != null
				&& user.getAddress() != null) {
			
			Address address = user.getAddress();
			if (address.getId() == null) {
				
				if (getAddressService().exists(address)) {
					address = getAddressService().findByExample(address);
				} else {
					address = getAddressService().insert(address);
				}
				user.setAddress(address);
			} else {
				getAddressService().insertOrUpdate(address);
			}
		}
	}
	
}
