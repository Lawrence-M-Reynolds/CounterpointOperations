package counterpointOperations.permutation.analysisObjects.harmonicAnalysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import counterpointOperations.counterpointMusicRelatedEntities.interval.intervalRelationships.MelodicIntervalRelationship;
import counterpointOperations.permutation.BarNotePermutation;
import counterpointOperations.permutation.speciesObjects.SpeciesElement;


public class VoiceAnalyser {
	public static Map<Integer, Set<MelodicIntervalRelationship>> analyseVoices(BarNotePermutation currentPermutation, 
			BarNotePermutation nextPermutation,	Map<Integer, Set<MelodicIntervalRelationship>> trackNumberToMelodicIntervalRelationshipsMap){
		
		if(trackNumberToMelodicIntervalRelationshipsMap == null){
			trackNumberToMelodicIntervalRelationshipsMap = new HashMap<Integer, Set<MelodicIntervalRelationship>>();
		}
		
		for(SpeciesElement thisSpeciesElement : currentPermutation){
			int speciesElementNumber = currentPermutation.getVoiceNumberOfSpeciesElementInPermutation(thisSpeciesElement);
			SpeciesElement nextSpeciesElement = nextPermutation.getVoiceSpeciesElement(speciesElementNumber);
			
			if(!thisSpeciesElement.isRest() && !nextSpeciesElement.isRest()){
				Set<MelodicIntervalRelationship> voiceMelodicRelationships;

				int trackNumber = thisSpeciesElement.getTrackNumber();
				if(trackNumberToMelodicIntervalRelationshipsMap.get(trackNumber) == null){
					/*
					 * Using a set so that the same intervals aren't added when using the generator and
					 * the same permutations are iterated over.
					 */
					voiceMelodicRelationships = new HashSet<MelodicIntervalRelationship>();
					trackNumberToMelodicIntervalRelationshipsMap.put(trackNumber, voiceMelodicRelationships);
				}else{
					voiceMelodicRelationships = trackNumberToMelodicIntervalRelationshipsMap.get(trackNumber);
				}

				MelodicIntervalRelationship melodicIntervalRelationship = new MelodicIntervalRelationship(
						thisSpeciesElement, nextSpeciesElement);

				voiceMelodicRelationships.add(melodicIntervalRelationship);
			}
		}
		return trackNumberToMelodicIntervalRelationshipsMap;
	}

}
