package counterpointOperations.rules.melodic.subRules;

import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.counterpointMusicRelatedEntities.interval.intervalRelationships.MelodicIntervalRelationship;


public abstract class MelodicSubRule {
	
	public abstract void evaluateInterval(MelodicIntervalRelationship melodicIntervalRelationship, 
			CounterpointComposition aCounterpointComposition, boolean annotateComposition);


}
