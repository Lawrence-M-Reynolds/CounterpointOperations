package counterpointOperations.compositionTranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import composition.Composition;
import composition.CompositionManager;
import composition.components.InstrumentTrackDetails;
import composition.components.trackComponents.Bar;
import composition.components.trackComponents.BarManager;
import composition.components.trackComponents.barComponents.BarComponentManager;
import composition.components.tracks.InstrumentTrack;
import composition.components.tracks.InstrumentTrackManager;

import musicRelatedEntities.note.noteAttributes.lengthValues.LengthValue;
import musicRelatedEntities.note.noteAttributes.lengthValues.NoteLengthValue;
import musicRelatedEntities.note.noteAttributes.lengthValues.RestLengthValue;
import musicRelatedEntities.note.writtenNotes.CompositeWrittenNote;


import counterpointOperations.counterpointComposition.CounterpointBarEvent;
import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.permutation.BarNotePermutation;
import counterpointOperations.permutation.speciesObjects.SpeciesElement;


public class CounterpointCompositionToCompositionTranslator {
	public static Composition generateCompositionDTOFromCounterpointComposition(
			CounterpointComposition counterpointComposition){
		
		/*FIXME
		 * Quick hack to get this manager in here. Should be brought in by
		 * injection. Also refactor whole process so that it's more
		 * layered and organised.
		 */
		CompositionManager compositionManager = 
				CompositionManager.getInstance();
		InstrumentTrackManager instrumentTrackManager = 
				compositionManager.getInstrumentTrackManager();
		BarManager barManager = 
				instrumentTrackManager.getBarManager();
		BarComponentManager barComponentManager = 
				BarComponentManager.getInstance();
		
		Composition compositionDTO = new Composition(counterpointComposition);
		
		List<InstrumentTrack> instrumentTracks = new ArrayList<InstrumentTrack>();
		Map<Integer, InstrumentTrackDetails> instrumentTrackDetailsMap = 
				counterpointComposition.getInstrumentTrackDetailsMap();
		//LOW This doesn't work for converting back compound time
		List<BarNotePermutation> permutations = counterpointComposition.getPermutations();
		
		for(BarNotePermutation barNotePermutation : permutations){
			int voiceNumber = 0;
			for(SpeciesElement firstSpeciesElement : barNotePermutation){
				
				InstrumentTrack instrumentTrack;
				if(permutations.indexOf(barNotePermutation) == 0){
					instrumentTrack = 
							instrumentTrackManager.createInstrumentTrackFromInstrumentTrackDetails(
									instrumentTrackDetailsMap.get(voiceNumber));					
					instrumentTracks.add(instrumentTrack);
				}else{
					instrumentTrack = instrumentTracks.get(voiceNumber);
				}

				//TODO passing null for the VoicePitchRangeLimitObject which is probably wrong.
				Bar bar = barManager.createBar(
						firstSpeciesElement.getClef(), barNotePermutation.getTimeSignature(), 
						barNotePermutation.getKey(), barNotePermutation.getTempo(), 
						barNotePermutation.getBarNumber(), firstSpeciesElement.getTrackNumber(), null);
				
				List<SpeciesElement> innerElementsList = firstSpeciesElement.getBaseInnerElementsList();
				for(SpeciesElement speciesElement : innerElementsList){
					CounterpointBarEvent counterpointBarEvent = speciesElement.getCounterpointBarEvent();
					
					LengthValue[] lengthValues = counterpointBarEvent.isRest() ? 
							RestLengthValue.values() : NoteLengthValue.values();
					//LOW This needs cleaning up
					CompositeWrittenNote aCompositeNoteValue = 
							barComponentManager.getCompositeWrittenNoteWithNumberOf32ndNotes(
									new CompositeWrittenNote(counterpointBarEvent.getWrittenNote()), 
										counterpointBarEvent.getEventLength(), lengthValues);
					
					/*
					 * Takes the accidental value from counterpointBarEvent.getNoteValue() 
					 * (the WrittenNote object) and uses the line pitch value to create the 
					 * composition bar event. writeCompositeNoteValue then obtains the stave line note
					 * from the bar's midi mapper.
					 */
					barManager.writeCompositeNoteValueToBar(bar, counterpointBarEvent.getTimeLocation(), 
							aCompositeNoteValue, counterpointBarEvent.getLinePitchValue(), 
							counterpointBarEvent.isTied(), speciesElement.getCounterpointAnnotationObjects());
				}
				instrumentTrack.addBar(bar);
				voiceNumber++;
			}
		}
		compositionDTO.setInstrumentTracks(instrumentTracks);
		
		return compositionDTO;
	}
}
