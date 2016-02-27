package counterpointOperations.facades.DbFacade.DbAccessors;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.evaluation.DbStrategy.PermutationLink;
import counterpointOperations.evaluation.DbStrategy.Solution;
import counterpointOperations.permutation.BarNotePermutation;

public class InitialDbAccessor implements DbAccessor{

	public void persistCounterpointComposition(
			CounterpointComposition counterpointComposition) {
		// TODO Auto-generated method stub
		
	}

	public void generatePermutationLinkingTables() {
		// TODO Auto-generated method stub
		
	}

	public List<PermutationLink> getPermuationLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	public int removeWeakLinks() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void generatePermutationCombinations() {
		// TODO Auto-generated method stub
		
	}

	public List<Solution> retrievePermutationCombinations() {
		// TODO Auto-generated method stub
		return null;
	}

	public void persistSolutionResults(List<Solution> solutions) {
		// TODO Auto-generated method stub
		
	}

	public Map<Integer, Map<Integer, BarNotePermutation>> persistPermutations(
			List<Collection<BarNotePermutation>> permutations) {
		// TODO Auto-generated method stub
		return null;
	}

}
