package counterpointOperations.solutionGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import musicRelatedEntities.chord.Chord;
import musicRelatedEntities.key.KeyNote;
import musicRelatedEntities.note.StaveLineNote;
import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.permutation.BarNotePermutation;
import counterpointOperations.permutation.speciesObjects.SpeciesElement;


/**
 * This class encapsulates the algorithms used for generating counterpoint solutions.
 * @author BAZ
 *
 */
public class SolutionGenerator {
	
	/**
	 * This method is used by the first species generator to analyse the partially filled
	 * permutation objects converted from the composition. It returns all of the possible
	 * first species possibilities for the voices that don't have notes in them 
	 * @param bars
	 * @return
	 * @throws Exception 
	 */
	public static List<Collection<BarNotePermutation>> generatePossiblePermutations(
			CounterpointComposition convertedCounterpointComposition) throws Exception{
		
		List<Collection<BarNotePermutation>> permutationsForEachStep = new ArrayList<Collection<BarNotePermutation>>();
		
		ListIterator<BarNotePermutation> permIter = convertedCounterpointComposition.getPermutationListIterator(0);
		while(permIter.hasNext()){
			BarNotePermutation barNotePermutation = permIter.next();
			
			Collection<Chord> chordsToBuildPermutations;
			if(barNotePermutation.getAllowedChords() != null){
				chordsToBuildPermutations = barNotePermutation.getAllowedChords();
			}else{
				// generate the triad chords that contain the existing notes and then process with them.
				Collection<KeyNote> keyNotesInPermutation = barNotePermutation.getAllFirstSpeciesKeyNotes();
				chordsToBuildPermutations = SolutionGeneratorTaskMethods.generateTriadChordsContainingKeyNotes(
						keyNotesInPermutation);
				if(keyNotesInPermutation.isEmpty()){
					//There's too many possibilities so just throw an exceptions
					throw new Exception("Too many possibilities exception"){};
				}
			}
			
			//Create a list of the species elements to create the new permutations with.
			ArrayList<List<SpeciesElement>> permutationLists = new ArrayList<List<SpeciesElement>>();
			for(Chord chord : chordsToBuildPermutations){
				//For each of the rests get all of the possible notes it could have.
				Collection<SpeciesElement> restsInPermutation = barNotePermutation.getAllRestFirstSpeciesElements();
				Map<Integer, Collection<StaveLineNote>> trackNumToPossibleStavelineNoteMap = 
						SolutionGeneratorTaskMethods.getStaveLineNotesForEachSpeciesElementMap(chord, restsInPermutation);
				

				//Build all possible permutations
				//Create a map of the species elements that are in the original permutation	
				Map<Integer, SpeciesElement> trackNumToSpeciesElementMap = barNotePermutation.getTrackNumToSpeciesElementMap();
				
				//Create the list of species elements to pass into the permutations for this chord and then add it to 
				//the outer collection
				ArrayList<List<SpeciesElement>> permutationListsForChord = 
						SolutionGeneratorTaskMethods.generatePermutationSpeciesElementLists(trackNumToSpeciesElementMap, 
								trackNumToPossibleStavelineNoteMap);
				
				permutationLists.addAll(permutationListsForChord);
			}
			Collection<BarNotePermutation> generatedPermutationsForStep = new HashSet<BarNotePermutation>();
			//Create the permutations from the species elment lists.
			for(List<SpeciesElement> speciesElementList : permutationLists){
				generatedPermutationsForStep.add(new BarNotePermutation(barNotePermutation, speciesElementList));
			}
			permutationsForEachStep.add(generatedPermutationsForStep);
		}
		return permutationsForEachStep;
	}

}
