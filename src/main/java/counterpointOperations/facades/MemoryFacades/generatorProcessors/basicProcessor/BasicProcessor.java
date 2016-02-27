package counterpointOperations.facades.MemoryFacades.generatorProcessors.basicProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import userInterface.counterpointUI.CounterpointOperationsUI;

import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.evaluation.MScStrategy.CounterpointCompositionEvaluator;
import counterpointOperations.facades.MemoryFacades.generatorProcessors.GeneratorProcessor;
import counterpointOperations.permutation.BarNotePermutation;

public class BasicProcessor implements GeneratorProcessor{
	private CounterpointCompositionEvaluator counterpointCompositionEvaluator;
	private CounterpointOperationsUI counterpointOperationsTerminal;

	public void setCounterpointCompositionEvaluator(
			CounterpointCompositionEvaluator counterpointCompositionEvaluator) {
		this.counterpointCompositionEvaluator = counterpointCompositionEvaluator;
	}


	public void setCounterpointOperationsUI(
			CounterpointOperationsUI aCounterpointOperationsTerminal) {
		counterpointOperationsTerminal = aCounterpointOperationsTerminal;		
	}

	
	private List<CounterpointComposition> buildFirstSpeciesCounterpointSolutionsForNextStep(
			Collection<BarNotePermutation> barNotePermutations, CounterpointComposition counterpointComposition){
		
		ArrayList<CounterpointComposition> counterpointCompositions = new ArrayList<CounterpointComposition>();
		for(BarNotePermutation permutation : barNotePermutations){
			//Make a copy of the original and add the permutation 
			CounterpointComposition counterpointCompositionClone = new CounterpointComposition(counterpointComposition);
			counterpointCompositionClone.add(permutation);
			counterpointCompositions.add(counterpointCompositionClone);
		}
		
		return counterpointCompositions;
	}


	public List<CounterpointComposition> generateCounterpointCompositionSolutions(
			List<Collection<BarNotePermutation>> permutations,
			CounterpointComposition originalCounterpointComposition) {

		counterpointOperationsTerminal.setMessage("Seeding Solutions");
		//create the CCO's with the first step permtuaitons here
		List<CounterpointComposition> counterpointCompositions = new ArrayList<CounterpointComposition>();
		Collection<BarNotePermutation> firstStepPermutations = permutations.remove(0);
		for(BarNotePermutation barNotePermutation : firstStepPermutations){
			CounterpointComposition generatedCounterpointComposition = 
					new CounterpointComposition(originalCounterpointComposition, barNotePermutation);
			counterpointCompositions.add(generatedCounterpointComposition);
		}
		firstStepPermutations = null;
		counterpointOperationsTerminal.setMessage("Processing solutions");
		/*
		 * Have a for loop here which adds the next steps to each of those by going through the algorthm.
		 * The object responsible for doing that will then return the partially created CCO's and they
		 * will be sent off for evaluation.
		 */
		ListIterator<Collection<BarNotePermutation>> permutationListIter = permutations.listIterator();
		
		boolean enableMaxNumberOfGeneratedSolutionsLimit = 
				counterpointOperationsTerminal.isMaxCompositionLimitEnabled();
		int maxNumberOfGeneratedSolutions = counterpointOperationsTerminal.getMaxNumberOfGeneratedSolutions();
		while(permutationListIter.hasNext()){
			Collection<BarNotePermutation> barNotePermutations = permutationListIter.next();
			//May help with heap space issues to remove the permutations
			permutationListIter.remove();
			List<CounterpointComposition> evaluatedCounterpointCompositions = new ArrayList<CounterpointComposition>();
			//Iterate through one counterpoint composition at a time rather than building all of the permutations for
			//all of them at once. This requires less memory.
			int compositionCount = 0;
			try{
				for(CounterpointComposition counterpointComposition : counterpointCompositions){
					List<CounterpointComposition> constructedCounterpointCompositions = null;
					//Take the counterpoint composition and create news ones from it adding each permutation
					//to it.
					constructedCounterpointCompositions = 
							buildFirstSpeciesCounterpointSolutionsForNextStep(
									barNotePermutations, counterpointComposition);


					String message = "Permutations left = " + permutations.size() + " --> Composition " + ++compositionCount + 
							" out of " + counterpointCompositions.size();

					counterpointOperationsTerminal.setMessage(message);
					//Evaluate each of the new counterpoint compositions
					constructedCounterpointCompositions = counterpointCompositionEvaluator.evaluateGeneratedCounterpointSolutions(
							constructedCounterpointCompositions, enableMaxNumberOfGeneratedSolutionsLimit);

					//Add the evaluated solutions to the outer List where they are sorted by their 
					//success scoring. A set number of the best is then taken through with the others
					//being discarded.
					evaluatedCounterpointCompositions.addAll(constructedCounterpointCompositions);
					if(evaluatedCounterpointCompositions.size() > maxNumberOfGeneratedSolutions
							&& enableMaxNumberOfGeneratedSolutionsLimit){
						Collections.sort(evaluatedCounterpointCompositions);
						evaluatedCounterpointCompositions = new ArrayList<CounterpointComposition>(
								evaluatedCounterpointCompositions.subList(0, maxNumberOfGeneratedSolutions));
					}
				}
			}catch(OutOfMemoryError outOfMemoryError){
				//Too many possibilites so report back to the user.
				counterpointOperationsTerminal.setMessage("System ran out of memory. Please lower the search space by" +
						" either lowering the number of voices, placing more notes, declaring\n more harmonies or decreasing" +
						" the maximum number of generated solutions size.");
				return null;
			}
			//The most successful will either pass onto the next iteration to build more
			//solutions or be passed back out for translation.
			counterpointCompositions = evaluatedCounterpointCompositions;
		}
		permutations = null;
		permutationListIter = null;
		//Don't want to translate more than 100 back as that's more than will be looked at
		counterpointCompositions = new ArrayList<CounterpointComposition>(
				counterpointCompositions.subList(0, 100));
		return counterpointCompositions;
	}
}
