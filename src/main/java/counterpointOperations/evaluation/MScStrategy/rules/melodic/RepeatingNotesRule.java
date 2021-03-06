package counterpointOperations.evaluation.MScStrategy.rules.melodic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import composition.reporting.EvaluationScoringObject;

import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.counterpointMusicRelatedEntities.interval.intervalRelationships.MelodicIntervalRelationship;
import counterpointOperations.evaluation.MScStrategy.rules.Rule;
import counterpointOperations.permutation.speciesObjects.SpeciesElement;


public class RepeatingNotesRule extends Rule{
	private static String RULE_MESSAGE = "There shouldn't be too many repeating notes in a melody.";
	private static int INTERVAL_DISTANCE_OF_REPEAT_NOTES = 0;
	private static int PENALTY_VALUE = 200/(INTERVAL_DISTANCE_OF_REPEAT_NOTES + 1);
	
	@Override
	public void evaluateCounterpointComposition(
			CounterpointComposition aCounterpointComposition,
			boolean annotateComposition) {
		
		EvaluationScoringObject evaluationScoringObject = aCounterpointComposition.getEvaluationScoringObject();
		Map<Integer, Set<MelodicIntervalRelationship>> melodicIntervalsMap = 
				aCounterpointComposition.getTrackNumberToMelodicIntervalRelationshipsMap();
		
		Set<Integer> voiceNumbers = melodicIntervalsMap.keySet();
		for(int voiceNumber : voiceNumbers){
			Set<MelodicIntervalRelationship> voiceMelodicRelationships =
					melodicIntervalsMap.get(voiceNumber);
			
			
			List<MelodicIntervalRelationship> melodicIntervalRelationships = new ArrayList<MelodicIntervalRelationship>();
			Iterator<MelodicIntervalRelationship> iter = voiceMelodicRelationships.iterator();
			while(iter.hasNext()){
				MelodicIntervalRelationship melodicIntervalRelationship = iter.next();
				SpeciesElement thisSpeciesElement = melodicIntervalRelationship.getFirstSpeciesElement();
				melodicIntervalRelationships.add(0, melodicIntervalRelationship);
				//Want the evaluation to be a function of how close to together the repeat notes are.
				INTERVAL_DISTANCE_OF_REPEAT_NOTES = 0;
				for(MelodicIntervalRelationship listedMelodicIntervalRelationship: melodicIntervalRelationships){
					SpeciesElement otherSpeciesElement = listedMelodicIntervalRelationship.getSecondSpeciesElement();
					//Don't want to use equals for this as I don't want the inner species elments to be important.
					if(thisSpeciesElement.isSameMidiValue(otherSpeciesElement)){
						//There's a repeating note.
						if(annotateComposition){
							//FIXME implement rule report
						}else{
							//The score is a function of the distance value.
							evaluationScoringObject.addToScore(PENALTY_VALUE);
						}
					}
					INTERVAL_DISTANCE_OF_REPEAT_NOTES++;
				}
			}
		}
	}
}
