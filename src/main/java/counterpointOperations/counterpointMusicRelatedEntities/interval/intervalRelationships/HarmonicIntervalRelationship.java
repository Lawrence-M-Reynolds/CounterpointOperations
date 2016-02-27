package counterpointOperations.counterpointMusicRelatedEntities.interval.intervalRelationships;

import counterpointOperations.counterpointMusicRelatedEntities.interval.IntervalEvaluator;
import counterpointOperations.permutation.speciesObjects.SpeciesElement;


public class HarmonicIntervalRelationship extends IntervalRelationship{
	
	private final SpeciesElement higherSpeciesElement; 
	private final SpeciesElement lowerSpeciesElement;
	
	public HarmonicIntervalRelationship(SpeciesElement firstSpeciesElement, 
			SpeciesElement secondSpeciesElement){
		super(firstSpeciesElement, secondSpeciesElement);

		
		if(numberOfSemiTones < 0){
			higherSpeciesElement = secondSpeciesElement;
			lowerSpeciesElement = firstSpeciesElement;
			numberOfSemiTones = -(numberOfSemiTones);
		}else{
			higherSpeciesElement = firstSpeciesElement;
			lowerSpeciesElement = secondSpeciesElement;
		}
		
		intervalType = IntervalEvaluator.determineHarmonicIntervalType(this);
	}

	public SpeciesElement getHigherSpeciesElement() {
		return higherSpeciesElement;
	}

	public SpeciesElement getLowerSpeciesElement() {
		return lowerSpeciesElement;
	}

}
