package counterpointOperations.facades;

import java.util.List;

import composition.Composition;



public interface CounterpointOperationsFacade {

	public Composition evaluateComposition(Composition composition);

	public List<Composition> generateFirstSpeciesSolutions(Composition composition);

}
