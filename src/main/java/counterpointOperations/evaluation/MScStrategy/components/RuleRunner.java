package counterpointOperations.evaluation.MScStrategy.components;

import java.util.Collection;
import java.util.HashSet;

import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.evaluation.MScStrategy.rules.Rule;
import counterpointOperations.evaluation.MScStrategy.rules.counterpoint.HarmonicRule;
import counterpointOperations.evaluation.MScStrategy.rules.counterpoint.MotionRule;
import counterpointOperations.evaluation.MScStrategy.rules.melodic.LeapStepRule;
import counterpointOperations.evaluation.MScStrategy.rules.melodic.MelodicRules;
import counterpointOperations.evaluation.MScStrategy.rules.melodic.RepeatingNotesRule;

/**
 * Uses the command design pattern to run all of the rules against a 
 * composition.
 * @author BAZ
 *
 */
public class RuleRunner {
	private static Collection<Rule> RULES = new HashSet<Rule>();
	
	//LOW - use spring to inject these
	// Static constructor
	static {
		RULES.add(new MotionRule());
		RULES.add(new HarmonicRule());
		RULES.add(new MelodicRules());
		RULES.add(new LeapStepRule());
		RULES.add(new RepeatingNotesRule());
	}

	public static CounterpointComposition runRulesOnCounterpointComposition(
			CounterpointComposition aCounterpointComposition, boolean annotateComposition){
		
		aCounterpointComposition.performPreRuleEvaluationAnalysis();
		
		for(Rule rule : RULES){
			rule.evaluateCounterpointComposition(aCounterpointComposition, annotateComposition);
		}
		return aCounterpointComposition;
	}
}
