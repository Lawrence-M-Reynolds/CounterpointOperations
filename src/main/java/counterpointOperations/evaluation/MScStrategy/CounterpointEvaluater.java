package counterpointOperations.evaluation.MScStrategy;

import java.util.List;
import java.util.ListIterator;

import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.evaluation.MScStrategy.components.RuleRunner;


public class CounterpointEvaluater implements CounterpointCompositionEvaluator{

	private static final List<CounterpointComposition> generatedCounterpointCompositions = null;

	public CounterpointComposition evaluateCounterpointComposition(final CounterpointComposition aCounterpointComposition) {
				
		RuleRunner.runRulesOnCounterpointComposition(aCounterpointComposition, true);
		return aCounterpointComposition;
	}

	public List<CounterpointComposition> evaluateGeneratedCounterpointSolutions(
			List<CounterpointComposition> generatedCounterpointCompositions, boolean enableMaxNumberOfGeneratedSolutionsLimit){
		ListIterator<CounterpointComposition> listIter = generatedCounterpointCompositions.listIterator();
		while(listIter.hasNext()){
			CounterpointComposition aCounterpointComposition = listIter.next();
			aCounterpointComposition.resetEvaluationScoringObject();
			RuleRunner.runRulesOnCounterpointComposition(aCounterpointComposition, false);
			if(aCounterpointComposition.getEvaluationScoringObject().getOverallScore() > 10000
					&& !enableMaxNumberOfGeneratedSolutionsLimit){
				listIter.remove();
			}
		}
		return generatedCounterpointCompositions;
	}
	
	public CounterpointComposition evaluateGeneratedCounterpointSolution(
		CounterpointComposition generatedCounterpointComposition){
		generatedCounterpointComposition.resetEvaluationScoringObject();
		
		RuleRunner.runRulesOnCounterpointComposition(generatedCounterpointComposition, false);

		return generatedCounterpointComposition;
	}

}
