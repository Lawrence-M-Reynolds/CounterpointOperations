package counterpointOperations.facades.DbFacade;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import composition.Composition;

import counterpointOperations.compositionTranslation.CompositionToCounterpointCompositionTranslator;
import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.evaluation.DbStrategy.DbRuleRunner;
import counterpointOperations.evaluation.DbStrategy.PermutationLink;
import counterpointOperations.evaluation.DbStrategy.Solution;
import counterpointOperations.facades.CounterpointOperationsFacade;
import counterpointOperations.facades.DbFacade.DbAccessors.DbAccessor;
import counterpointOperations.permutation.BarNotePermutation;
import counterpointOperations.solutionGenerator.SolutionGenerator;


public class DbCounterpointOperationsFacade implements CounterpointOperationsFacade{

	private static Logger logger = Logger.getLogger(DbCounterpointOperationsFacade.class);
	
	private static DbAccessor dbAccessor;
	
	public DbCounterpointOperationsFacade(DbAccessor dbAccessor){
		this.dbAccessor = dbAccessor;
	}

	public List<Composition> generateFirstSpeciesSolutions(
			Composition composition) {
		
		//Generate and persist the counterpoint composition
		logger.info("Generating and persisting the counterpoint composition.");
		CounterpointComposition counterpointComposition = 
				CompositionToCounterpointCompositionTranslator.
		generateCounterpointCompositionFromComposition(composition);
		
		dbAccessor.persistCounterpointComposition(counterpointComposition);
		
		logger.info("Generating permutaitons");
		/*
		 * This list contains has a value for each step in the composition. These values are a collection
		 * of all the possible permutations of where the first species notes could go. 
		 */
		List<Collection<BarNotePermutation>> permutations;
		try {
			permutations = SolutionGenerator.generatePossiblePermutations(counterpointComposition);
		} catch (Exception e) {
			logger.error("Every step must have at least one bar with a written" +
					" note in it.");
			return null;
		}

		logger.info("Persisting permutations to DB.");
		/*
		 * The database won't actually store the permutations but id's that are mapped in the returned map
		 * from this method.
		 */
		Map<Integer, Map<Integer, BarNotePermutation>> generatedPermutationsMap = 
				dbAccessor.persistPermutations(permutations);
		
		logger.info("Generating the permutation linking tables.");
		dbAccessor.generatePermutationLinkingTables();
		
		logger.info("Analysing links.");
		List<PermutationLink> permutationLinks = dbAccessor.getPermuationLinks();
		DbRuleRunner.runLinkEvaluationRules(permutationLinks, generatedPermutationsMap);		
		
		logger.info("Eliminating weak links.");
		int numLinksRemoved = dbAccessor.removeWeakLinks();
		logger.info(numLinksRemoved + " deleted...");
		
		logger.info("Generating permutation combinations and persisting.");
		dbAccessor.generatePermutationCombinations();
		
		logger.info("Evaluating combinations.");
		List<Solution> solutions = dbAccessor.retrievePermutationCombinations();
		solutions = DbRuleRunner.evaluateSolutions(solutions, counterpointComposition);
		dbAccessor.persistSolutionResults(solutions);
		
		logger.info("Retrieving strongest solutions.");
		
		return null;
	}

	public Composition evaluateComposition(Composition composition) {
		// TODO Auto-generated method stub
		return null;
	}
}
