package com.rabbithole.mongodb.aggregators;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.rabbithole.mongodb.daos.aggregated.StatePopulation;

public interface ZipCodeAggregator {
	
	/**
	 * Get all the states with a total population greater than 'populationThreshold' million order by population 'sortingDirection'
	 *
	 * @param populationThreshold
	 * @param sortingDirection
	 *
	 * @return {@link java.util.List} of {@link com.rabbithole.mongodb.daos.aggregated.StatePopulation}
	 */
	List<StatePopulation> getStatesWithPopulationGreaterThan(final long populationThreshold, final Direction sortingDirection);
	
	/**
	 * Get the smallest state by average city population
	 *
	 * @return {@link com.rabbithole.mongodb.daos.aggregated.StatePopulation}
	 */
	StatePopulation getSmallestStateByAvgCityPopulation();
	
}
