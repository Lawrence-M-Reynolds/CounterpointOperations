package counterpointOperations.evaluation.MScStrategy;

import java.util.List;

import counterpointOperations.counterpointComposition.CounterpointComposition;

public interface CounterpointCompositionEvaluator {

	public CounterpointComposition evaluateCounterpointComposition(final CounterpointComposition aCounterpointComposition);
	
	public List<CounterpointComposition> evaluateGeneratedCounterpointSolutions(
			List<CounterpointComposition> generatedCounterpointCompositions, boolean enableMaxNumberOfGeneratedSolutionsLimit);
	
	public CounterpointComposition evaluateGeneratedCounterpointSolution(
			CounterpointComposition generatedCounterpointComposition);
}
