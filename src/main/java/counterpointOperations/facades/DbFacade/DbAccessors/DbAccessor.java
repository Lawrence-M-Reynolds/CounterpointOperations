package counterpointOperations.facades.DbFacade.DbAccessors;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.evaluation.DbStrategy.PermutationLink;
import counterpointOperations.evaluation.DbStrategy.Solution;
import counterpointOperations.permutation.BarNotePermutation;

public interface DbAccessor {

	public Map<Integer, Map<Integer, BarNotePermutation>> persistPermutations(List<Collection<BarNotePermutation>> permutations);

	public void persistCounterpointComposition(CounterpointComposition counterpointComposition);

	public void generatePermutationLinkingTables();

	public List<PermutationLink> getPermuationLinks();

	public int removeWeakLinks();

	public void generatePermutationCombinations();

	public List<Solution> retrievePermutationCombinations();

	public void persistSolutionResults(List<Solution> solutions);

	
}
