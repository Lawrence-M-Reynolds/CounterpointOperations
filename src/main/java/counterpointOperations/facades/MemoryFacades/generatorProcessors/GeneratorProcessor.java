package counterpointOperations.facades.MemoryFacades.generatorProcessors;

import java.util.Collection;
import java.util.List;

import userInterface.counterpointUI.CounterpointOperationsUI;

import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.evaluation.MScStrategy.CounterpointCompositionEvaluator;
import counterpointOperations.permutation.BarNotePermutation;

public interface GeneratorProcessor {
	
	public void setCounterpointCompositionEvaluator(CounterpointCompositionEvaluator counterpointCompositionEvaluator);
	
	public List<CounterpointComposition> generateCounterpointCompositionSolutions(List<Collection<BarNotePermutation>> permutations,
			CounterpointComposition originalCounterpointComposition);

	public void setCounterpointOperationsUI(
			CounterpointOperationsUI counterpointOperationsTerminal);

}
