package com.rabbithole.mongodb.aggregators.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.rabbithole.mongodb.aggregators.ZipCodeAggregator;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.daos.aggregated.StatePopulation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Aggregation in MongoDB was built to process data and return computed results. Data is processed in stages and the output of one stage is provided as input to the next stage.
 * This ability to apply transformations and do computations on data in stages makes aggregation a very powerful tool for analytics.
 *
 * Spring Data MongoDB provides an abstraction for native aggregation queries using the three classes
 * 		. {@link org.springframework.data.mongodb.core.aggregation.Aggregation} which wraps an aggregation query
 * 		. {@link org.springframework.data.mongodb.core.aggregation.AggregationOperation} which wraps individual pipeline stages
 * 		. {@link org.springframework.data.mongodb.core.aggregation.AggregationResults<T>} which is the container of the result produced by aggregation
 *
 * To perform and aggregation, first, create aggregation pipelines using the static builder methods on Aggregation class, then create an instance of Aggregation using the
 * {@link org.springframework.data.mongodb.core.aggregation.Aggregation#newAggregation(org.springframework.data.mongodb.core.aggregation.AggregationOperation...)} method
 * on the Aggregation class and finally run the aggregation using {@link org.springframework.data.mongodb.core.MongoOperations}.
 *
 * Please note that both {@link org.springframework.data.mongodb.core.aggregation.MatchOperation} and {@link org.springframework.data.mongodb.core.aggregation.ProjectionOperation}
 * implement AggregationOperation. There are similar implementations for other aggregation pipelines.
 * Generic type given to AggregationResults is the data model for expected output.
 */
@Slf4j
@Service("zipCodeAggregator")
public class ZipCodeAggregatorImpl implements ZipCodeAggregator {

	@Resource(name = "mongoTemplate")
	@Getter(value = AccessLevel.PROTECTED)
	@Setter
	private MongoOperations mongoOperations;
	
	/**
	 * Here we will have three stages:
	 * 		. $group stage summing up the population of all zip codes
	 * 		. $match stage to filter out states with population over 10 million
	 * 		. $sort stage to sort all the documents in descending order of population
	 * The expected output will have a field _id as state and a field population with the total state population ({@link com.rabbithole.mongodb.daos.aggregated.StatePopulation}).
	 * PLEASE NOTE: If the output data model is not known, the standard MongoDB classes Document or DBObject can be used.
	 *
	 * The AggregationResults class implements Iterable and hence we can iterate over it and print the results.
	 */
	@Override
	public List<StatePopulation> getStatesWithPopulationGreaterThan(final long populationThreshold, final Direction sortingDirection) {

		log.debug("Get all the states with a total population greater than " + populationThreshold + " million order by population " + sortingDirection);
		
		// Here it's a good idea to use a Queue, because operations must be executed in the same order we defined them.
		final List<AggregationOperation> operations = new LinkedList<>();

		// GroupOperation > group by state and sum population
		operations.add(
				Aggregation.group(ProjectConstants.ZIPCODE_FIELDS_STATE)
						.sum(ProjectConstants.ZIPCODE_FIELDS_POPULATION)
						.as(ProjectConstants.STATEPOPULATION_FIELDS_TOTALPOPULATION));

		// MatchOperation > filter by states with population greater than 'populationThreshold'
		operations.add(
				Aggregation.match(
						new Criteria(ProjectConstants.STATEPOPULATION_FIELDS_TOTALPOPULATION).gt(populationThreshold)));

		// SortOperation > sort by total population
		if (sortingDirection != null) {
			operations.add(
					Aggregation.sort(
							new Sort(Direction.DESC, ProjectConstants.STATEPOPULATION_FIELDS_TOTALPOPULATION)));
		}
		
		return getMongoOperations()
				.aggregate(Aggregation.newAggregation(operations), "zipCode", StatePopulation.class)
				.getMappedResults();
	}

	/**
	 * Here we use four stages:
	 * 		1) $group to sum the total population of each city
	 * 		2) $group to calculate average population of each state
	 * 		3) $sort stage to order states by their average city population in ascending order
	 * 		4) $limit to get the first state with lowest average city population
	 * Although it’s not necessarily required, we will use an additional $project stage to reformat the document as per out StatePopulation data model.
	 *
	 * In this case, we already know that there will be ONLY ONE DOCUMENT in the result since we limit the number of output documents to 1 in the LimitOperation stage.
	 * As such, we can invoke getUniqueMappedResult() (instead of getMappedResults()) to get the required StatePopulation instance.
	 *
	 * Another thing to notice is that, instead of relying on the @Id annotation to map _id to state, we have explicitly done it in projection stage.
	 */
	@Override
	public StatePopulation getSmallestStateByAvgCityPopulation() {

		// Here it's a good idea to use a Queue, because operations must be executed in the same order we defined them.
		final List<AggregationOperation> operations = new LinkedList<>();

		// GroupOperation > sum the total city population
		operations.add(
				Aggregation.group(ProjectConstants.STATEPOPULATION_FIELDS_STATE, ProjectConstants.ZIPCODE_FIELDS_CITY)
						.sum(ProjectConstants.ZIPCODE_FIELDS_POPULATION).as(ProjectConstants.STATEPOPULATION_TEMP_FIELDS_CITYPOPULATION));

		// GroupOperation > average the population of each state
		operations.add(
				Aggregation.group(ProjectConstants.STATEPOPULATION_FIELDS_ID + "." + ProjectConstants.STATEPOPULATION_FIELDS_STATE)
						.avg(ProjectConstants.STATEPOPULATION_TEMP_FIELDS_CITYPOPULATION).as(ProjectConstants.STATEPOPULATION_TEMP_FIELDS_AVGCITYPOPULATION));

		// SortOperation > sort ascending by average population
		operations.add(
				Aggregation.sort(
						new Sort(Direction.ASC, ProjectConstants.STATEPOPULATION_TEMP_FIELDS_AVGCITYPOPULATION)));

		// LimitOperation > limit to only first document found
		operations.add(
				Aggregation.limit(1));

		// ProjectionOperation > project to match StatePopulation data model
		operations.add(
				Aggregation.project()
						.andExpression(ProjectConstants.STATEPOPULATION_FIELDS_ID).as(ProjectConstants.STATEPOPULATION_FIELDS_STATE)
						.andExpression(ProjectConstants.STATEPOPULATION_TEMP_FIELDS_AVGCITYPOPULATION).as(ProjectConstants.STATEPOPULATION_FIELDS_TOTALPOPULATION));

		return getMongoOperations()
				.aggregate(Aggregation.newAggregation(operations), "zipCode", StatePopulation.class)
				.getUniqueMappedResult();
	}
	
}
