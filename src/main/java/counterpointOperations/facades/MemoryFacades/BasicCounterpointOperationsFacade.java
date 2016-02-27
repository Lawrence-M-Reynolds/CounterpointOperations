package counterpointOperations.facades.MemoryFacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import composition.Composition;

import userInterface.counterpointUI.CounterpointOperationsUI;
import counterpointOperations.compositionTranslation.Translator;
import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.evaluation.MScStrategy.CounterpointCompositionEvaluator;
import counterpointOperations.evaluation.MScStrategy.CounterpointEvaluater;
import counterpointOperations.facades.CounterpointOperationsFacade;
import counterpointOperations.facades.MemoryFacades.generatorProcessors.GeneratorProcessor;
import counterpointOperations.facades.MemoryFacades.generatorProcessors.basicProcessor.BasicProcessor;
import counterpointOperations.permutation.BarNotePermutation;
import counterpointOperations.solutionGenerator.SolutionGenerator;



public class BasicCounterpointOperationsFacade implements CounterpointOperationsFacade{
	public static int MAX_NUMBER_OF_GENERATED_SOLUTIONS = 10000;
	
	private CounterpointOperationsUI counterpointOperationsTerminal;
	private GeneratorProcessor generatorProcessor;
	private CounterpointCompositionEvaluator counterpointCompositionEvaluator;
	
	public BasicCounterpointOperationsFacade(
			CounterpointOperationsUI aCounterpointOperationsTerminal) {
		counterpointOperationsTerminal = aCounterpointOperationsTerminal;
		counterpointCompositionEvaluator = new CounterpointEvaluater();
		
		generatorProcessor = new BasicProcessor();
//		generatorProcessor = new GeneticProcessor();
		generatorProcessor.setCounterpointCompositionEvaluator(counterpointCompositionEvaluator);
		generatorProcessor.setCounterpointOperationsUI(counterpointOperationsTerminal);
	}

	public List<Composition> generateFirstSpeciesSolutions(final Composition aCompositionDTO){
		counterpointOperationsTerminal.setMessage("Converting composition");
		/*
		 * First the permutations which will only be partially filled are generated from the composition. 
		 */
		CounterpointComposition convertedCounterpointComposition = 
				Translator.generateCounterpointCompositionFromComposition(aCompositionDTO);	

		counterpointOperationsTerminal.setMessage("Generating permutaitons");
		/*
		 * This list contains has a value for each step in the composition. These values are a collection
		 * of all the possible permutations of where the first species notes could go. 
		 */
		List<Collection<BarNotePermutation>> permutations;
		try {
			permutations = SolutionGenerator.generatePossiblePermutations(convertedCounterpointComposition);
		} catch (Exception e) {
			counterpointOperationsTerminal.setMessage("Every step must have at least one bar with a written" +
					" note in it.");
			return null;
		}

		List<CounterpointComposition> counterpointCompositions = generatorProcessor.generateCounterpointCompositionSolutions(
				permutations, convertedCounterpointComposition);

		/*
		 * At this point the solutions are fully built and they just need to be converted
		 * into CompositionDTOs that the front end can use. The CounterpointComposition
		 * implments comparable so that they are ordered according to how high their
		 * score is. This ensures that the returned solutions are in the right order
		 * and the best can be selected from the top.
		 */
		List<Composition> solutions = new ArrayList<Composition>();
		for(CounterpointComposition  counterpointComposition : counterpointCompositions){
			solutions.add(Translator.generateCompositionDTOFromCounterpointComposition(counterpointComposition));
		}
		counterpointCompositions = null;
		counterpointOperationsTerminal.setMessage("Complete");
		return solutions;		
	}

	public Composition evaluateComposition(Composition composition) {
		CounterpointComposition convertedCounterpointComposition = 
				Translator.generateCounterpointCompositionFromComposition(composition);
		/*
		 * Analyse convertedBarNotePermutations, convert back to CO and return it.
		 */
		counterpointCompositionEvaluator.evaluateCounterpointComposition(convertedCounterpointComposition);
		
		return Translator.generateCompositionDTOFromCounterpointComposition(convertedCounterpointComposition);
	}
}
