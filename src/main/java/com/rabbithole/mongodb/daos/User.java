package com.rabbithole.mongodb.daos;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.querydsl.core.annotations.QueryEntity;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.utils.JsonUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Data - MongoDB DAO class
 *
 * The field level @Id annotation can decorate any type, including long and string.
 * If the value of the @Id field is not null, it’s stored in the database as-is; otherwise, the converter will assume you want to store
 * an ObjectId in the database (either ObjectId, String or BigInteger work).
 *
 * @Document annotation simply marks a class as being a domain object that needs to be persisted to the database, along with allowing us
 * to choose the name of the collection to be used.
 */
// Lombok
// @NoArgsConstructor(onConstructor = @__({@PersistenceConstructor}))}
// @NoArgsConstructor
// @RequiredArgsConstructor(onConstructor = @__({@PersistenceConstructor}))}
@RequiredArgsConstructor
// @AllArgsConstructor(onConstructor = @__({@PersistenceConstructor}))}
@AllArgsConstructor
@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, @Setter on all non-final fields, and @RequiredArgsConstructor
@Builder
// Logback
@Slf4j
// Jackson
// @JsonIgnoreProperties(value = { "id" })
@JsonIgnoreProperties(ignoreUnknown = true)
// QueryDSL
@QueryEntity
// Spring Data - MongoDB
@Document
public class User {

	// Spring Data - MongoDB
	@Id
	// Jackson
	// @JsonIgnore
	private ObjectId id;
	
	private String name;
	
	private String surname;
	
	/**
	 * Spring Data - MongoDB
	 *
	 * @Indexed annotation marks the field as indexed in MongoDB (and eventually also unique).
	 */
	@Indexed(unique = true)
	// Lombok
	@NonNull
	private String email;
	
	/**
	 * Spring Data - MongoDB
	 *
	 * @Field indicates the key to be used for the field in the JSON document.
	 * Now "bDay" will be saved in the database using the key "birthday".
	 */
	@Field("birthday")
	private DateTime bDay;
	
	/**
	 * Spring Data - MongoDB
	 *
	 * {@link org.springframework.data.annotation.Transient} annotation excludes the field from being persisted.
	 * Not working with {@link javax.persistence.Transient} or {@link java.beans.Transient}
	 *
	 * This is a dynamic field
	 * @Transient + custom Converter to calculate it during persistence logic (store and query).
	 */
	@Transient
	// @JsonIgnore
	private Integer age;
	
	/**
	 * Spring Data - MongoDB
	 *
	 * The mapping framework doesn’t support storing parent-child relations and embedded documents within other documents.
	 * What we can do though is – we can store them separately and use a DBRef to refer to the documents.
	 * When the object is loaded from MongoDB, those references will be eagerly resolved, and we’ll get back a mapped
	 * object that looks the same as if it had been stored embedded within our master document.
	 *
	 * Without a proper EventListener to save the referenced-object (in this case the Address) BEFORE converting/saving the referent-object
	 * (in this case the User), the referent-object save will fail because MongoDB is not able to establish the reference.
	 * If no EventListener is defined, the referenced-object (in this case the Address) must be saved BEFORE saving the
	 * referent-object (in this case the User).
	 *
	 * If two documents are referencing each other, one of the two must define the @DBRef as lazy (@DBRef(lazy = true)), otherwise
	 * this will result in a cycling reference.
	 */
	@DBRef
	// @JsonIgnore
	// @CascadeSave
	private Address address;

	/**
	 * TODO revise after mongodb auditing implementation is ready
	 *
	 * Your code is working as expected. After you've implemented Persistable you can see that @CreatedDate annotation is working.
	 *
	 * Sure that createdDate become null on second time because object already exist in the database and you update it with createdDate = null.
	 * As you can see from documentation for @CreatedDate:
	 * 		@CreatedDate annotation. This identifies the field whose value is set when the entity is persisted to the database for the first time.
	 *
	 * So to not overwrite your createdDate with null on second call you should retrieve your customer from database with c = repository.findOne("test_id"); and then update it.
	 */
	@CreatedBy
	// @JsonIgnore
	private String createdBy;

	@LastModifiedBy
	// @JsonIgnore
	private String lastModifiedBy;

	@CreatedDate
	// @JsonIgnore
	private DateTime creationDate;

	@LastModifiedDate
	// @JsonIgnore
	private DateTime lastModifiedDate;

	/**
	 * Spring Data - MongoDB
	 *
	 * {@link org.springframework.data.annotation.PersistenceConstructor} marks a constructor, even one that’s package protected,
	 * to be the primary constructor used by the persistence logic.
	 * The constructor arguments are mapped by name to the key values in the retrieved DBObject.
	 *
	 * Here we can use the standard Spring {@link org.springframework.beans.factory.annotation.Value} annotation. It’s with the help
	 * of this annotation that we can use the Spring Expressions to transform a key’s value retrieved from the database before it is
	 * used to construct a domain object.
	 * That is a very powerful and highly useful feature here.
	 *
	 * In this example if birthday is not set, than it will be set to NULL by default, also after queries:
	 * 		mongoTemplate.findOne(Query.query(Criteria.where("name").is("Alex")), User.class).getBDay();
	 * The result will be NULL.
	 */
	@PersistenceConstructor
	public User(final ObjectId id, final String name, final String surname, final String email, @Value("#root.birthday ?: null") final DateTime bDay, final Address address) {

		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.bDay = bDay;
		this.address = address;
	}
	
	@Override
	public String toString() {
		
		return parseToJson(false);
	}
	
	public String toStringPrettyPrint() {
		
		return parseToJson(true);
	}
	
	protected String parseToJson(final boolean prettyPrint) {
		
		try {
			return JsonUtils.serializeObjectToJsonString(this, prettyPrint);
			
		} catch (final JsonProcessingException e) {
			log.error("Error converting User[id:" + getId() + ", email:" + getEmail() + "] :"
					+ e.getMessage() + ProjectConstants.LOG_NEWLINE
					+ "Returning empty String to avoid possible NullPointerException");
		}
		return ProjectConstants.EMPTY_STRING;
	}
	
}
