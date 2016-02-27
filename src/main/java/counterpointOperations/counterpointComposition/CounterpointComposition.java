package counterpointOperations.counterpointComposition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import composition.Composition;
import composition.IComposition;
import composition.components.InstrumentTrackDetails;
import composition.reporting.EvaluationScoringObject;
import composition.reporting.RuleReport;

import musicRelatedEntities.chord.Chord;
import musicRelatedEntities.key.Key;
import musicRelatedEntities.time.Tempo;
import musicRelatedEntities.time.TimeSignature;


import counterpointOperations.counterpointMusicRelatedEntities.interval.intervalRelationships.MelodicIntervalRelationship;
import counterpointOperations.exception.UnsupportedTimeSignatureException;
import counterpointOperations.permutation.BarNotePermutation;
import counterpointOperations.permutation.analysisObjects.harmonicAnalysis.VoiceAnalyser;


public class CounterpointComposition implements IComposition, Comparable<CounterpointComposition>{
	
	private List<BarNotePermutation> permutations;
	private Map<Integer, InstrumentTrackDetails> instrumentTrackDetailsMap;
	private Map<Integer, Collection<Chord>> harmonyLockdownMap;
	private Map<Integer, Set<MelodicIntervalRelationship>> trackNumberToMelodicIntervalRelationshipsMap;
	private EvaluationScoringObject evaluationScoringObject = new EvaluationScoringObject();;
	private List<RuleReport> ruleReports;
	
	//For CompositionDTO
	private String title;
	private Key key;
	private TimeSignature timeSignature;
	private Tempo tempo;
	
	/**
	This constructor is used when a user submits a solution of theirs to be evaluated. The
	Composition object must be converted into a list of BarNotePermutation objects and
	passed into this constructor. 
	 * @param aCompositionDTO 
	 * @throws UnsupportedTimeSignatureException 
	*/
	public CounterpointComposition(List<BarNotePermutation> aListOfPermutations, Composition aCompositionDTO,
			Map<Integer, InstrumentTrackDetails> aInstrumentTrackDetailsMap){
		permutations = aListOfPermutations;
		instrumentTrackDetailsMap = aInstrumentTrackDetailsMap;
		title = aCompositionDTO.getTitle();
		key = aCompositionDTO.getKey();
		timeSignature = aCompositionDTO.getTimeSignature();
		tempo = aCompositionDTO.getTempo();
		harmonyLockdownMap = aCompositionDTO.getHarmonyLockdownMap();
	}
	
	/**
	 * A copy constructor used in the solution generation algorithm. It copies everything
	 * from the orginal counterpoint composition except the barnote permutations
	 * which is recreated and the first permutation from those genated is added to it.
	 * This is passed in as a parameter.
	 * @param convertedCounterpointComposition
	 * @param barNotePermutation
	 */
	public CounterpointComposition(
			CounterpointComposition convertedCounterpointComposition,
			BarNotePermutation barNotePermutation) {
		super();
		this.permutations = new ArrayList<BarNotePermutation>();
		permutations.add(barNotePermutation);
		
		this.instrumentTrackDetailsMap = convertedCounterpointComposition.instrumentTrackDetailsMap;
		this.harmonyLockdownMap = convertedCounterpointComposition.harmonyLockdownMap;
		this.trackNumberToMelodicIntervalRelationshipsMap = 
				convertedCounterpointComposition.trackNumberToMelodicIntervalRelationshipsMap;
		this.title = convertedCounterpointComposition.title;
		this.key = convertedCounterpointComposition.key;
		this.timeSignature = convertedCounterpointComposition.timeSignature;
		this.tempo = convertedCounterpointComposition.tempo;
	}
	
	/**
	 * Another copy constructor used by the generation algorithm. However, this one is
	 * real copy constructor and copies everything from the passed in counterpoint 
	 * composition.
	 * @param convertedCounterpointComposition
	 */
	public CounterpointComposition(
			CounterpointComposition convertedCounterpointComposition) {
		super();
		//Create a new permutation list with the contents of the old one.
		this.permutations = new ArrayList<BarNotePermutation>(convertedCounterpointComposition.getPermutations());	
		this.instrumentTrackDetailsMap = convertedCounterpointComposition.instrumentTrackDetailsMap;
		this.harmonyLockdownMap = convertedCounterpointComposition.harmonyLockdownMap;
		this.trackNumberToMelodicIntervalRelationshipsMap = null;
		this.title = convertedCounterpointComposition.title;
		this.key = convertedCounterpointComposition.key;
		this.timeSignature = convertedCounterpointComposition.timeSignature;
		this.tempo = convertedCounterpointComposition.tempo;
	}

	public int getNumberOfVoicesInComposition(){
		return permutations.get(0).getNumberOfFirstSpeciesElements();
	}
	
	public void addBarNotePermutation(BarNotePermutation barNotePermutation){
		permutations.add(barNotePermutation);
	}
	
	/**
	 * Run before evaluating with the rules, this performs some of the analysis
	 * that will be shared between different rules.
	 */
	public void performPreRuleEvaluationAnalysis(){
		ListIterator<BarNotePermutation> iter = permutations.listIterator();
		while(iter.hasNext()){
			BarNotePermutation permutation = iter.next();
			if(!permutation.isAnalysisPerformed()){		
				//permutation related analysis
				permutation.performPermutationAnalysis();
			}
			if(iter.hasNext()){
				//Composition related analysis
				trackNumberToMelodicIntervalRelationshipsMap = VoiceAnalyser.analyseVoices(
						permutation, iter.next(), trackNumberToMelodicIntervalRelationshipsMap);
				//Because the melodic analyser used the next iteration as well.
				iter.previous();
			}
		}
	}
	
	public ListIterator<BarNotePermutation> getPermutationListIterator(int indexStartValue) {
		return permutations.listIterator();
	}


	public void add(BarNotePermutation permutation) {
		permutations.add(permutation);		
	}

	public void addRuleReport(RuleReport ruleReport){
		if(ruleReports == null){
			ruleReports = new ArrayList<RuleReport>();
		}
		ruleReports.add(ruleReport);
	}


	public List<BarNotePermutation> getPermutations() {
		return permutations;
	}


	public Map<Integer, InstrumentTrackDetails> getInstrumentTrackDetailsMap() {
		return instrumentTrackDetailsMap;
	}


	public EvaluationScoringObject getEvaluationScoringObject() {
		return evaluationScoringObject;
	}


	public String getTitle() {
		return title;
	}


	public Key getKey() {
		return key;
	}


	public TimeSignature getTimeSignature() {
		return timeSignature;
	}


	public Tempo getTempo() {
		return tempo;
	}



	public Map<Integer, Collection<Chord>> getHarmonyLockdownMap() {
		return harmonyLockdownMap;
	}


	public Map<Integer, Set<MelodicIntervalRelationship>> getTrackNumberToMelodicIntervalRelationshipsMap() {
		return trackNumberToMelodicIntervalRelationshipsMap;
	}

	/**
	 * Sorts the counterpoint compositions by the overall score of their scoring
	 * object. They lower the better so the lowest ones are first.
	 */
	public int compareTo(CounterpointComposition otherCounterpointComposition) {
		return -(otherCounterpointComposition.evaluationScoringObject.getOverallScore() - 
				this.evaluationScoringObject.getOverallScore());
	}

	public List<RuleReport> getRuleReports() {
		return ruleReports;
	}

	public void resetEvaluationScoringObject() {
		evaluationScoringObject.reset();
		
	}
	
	public int getLength(){
		return permutations.size();
	}

	public void setPermutations(List<BarNotePermutation> permutations) {
		trackNumberToMelodicIntervalRelationshipsMap = null;
		this.permutations = permutations;
	}
}
