package counterpointOperations.counterpointMusicRelatedEntities.interval.intervalRelationships;

import musicRelatedEntities.key.Interval;
import counterpointOperations.counterpointMusicRelatedEntities.interval.IntervalEvaluator.IntervalType;
import counterpointOperations.permutation.speciesObjects.SpeciesElement;

public abstract class IntervalRelationship {
	protected final Interval interval;
	protected int numberOfOctaves;
	protected int numberOfSemiTones;
	protected IntervalType intervalType;
	
	protected IntervalRelationship(SpeciesElement firstSpeciesElement, 
			SpeciesElement secondSpeciesElement){
		numberOfSemiTones = firstSpeciesElement.getMidiValue() - 
				secondSpeciesElement.getMidiValue();
		
		//In case it's negative.
		int varNumberOfSemiTones = Math.abs(numberOfSemiTones);
		numberOfOctaves = varNumberOfSemiTones / 12;
		varNumberOfSemiTones = varNumberOfSemiTones % 12;
		interval = Interval.values()[varNumberOfSemiTones];
	}

	public Interval getInterval() {
		return interval;
	}

	public int getNumberOfOctaves() {
		return numberOfOctaves;
	}

	/**
	 * Gets the number of semitones for the interval between the two notes.
	 * This may be negative, indicating whether the firstSpeciesElement that
	 * was passed into the constructor is above (-ve) or below (+ve) the 
	 * secondSpeciesElement.
	 * @return
	 */
	public int getNumberOfSemiTones() {
		return numberOfSemiTones;
	}

	public IntervalType getIntervalType() {
		return intervalType;
	}
}
