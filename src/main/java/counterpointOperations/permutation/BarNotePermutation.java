package counterpointOperations.permutation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import composition.components.trackComponents.Bar;

import musicRelatedEntities.chord.Chord;
import musicRelatedEntities.key.Key;
import musicRelatedEntities.key.KeyNote;
import musicRelatedEntities.time.Tempo;
import musicRelatedEntities.time.TimeSignature;

import counterpointOperations.counterpointMusicRelatedEntities.interval.intervalRelationships.HarmonicIntervalRelationship;
import counterpointOperations.permutation.analysisObjects.harmonicAnalysis.HarmonicAnalysisPairingKey;
import counterpointOperations.permutation.analysisObjects.harmonicAnalysis.PermutationAnalyser;
import counterpointOperations.permutation.speciesObjects.SpeciesElement;
import counterpointOperations.permutation.speciesObjects.SpeciesInformation;


public class BarNotePermutation implements Iterable<SpeciesElement>{
	private SpeciesInformation speciesInformation;
	private List<SpeciesElement> firstSpeciesElementsForEachVoice = new ArrayList<SpeciesElement>();
	//If this is null then it can be assumed that the bar didn't belong to a compound bar.
	private CompoundBarLinkInformation compoundBarLinkInformation;
	
	//Bar Related
	private TimeSignature timeSignature;
	private Key key;
	private Tempo tempo;
	private int barNumber;
	private Collection<Chord> allowedChords;
	private boolean analysisPerformed = false; 
	
	//Processor result maps.
	private Map<HarmonicAnalysisPairingKey, HarmonicIntervalRelationship> harmonicAnalysisResultsMap;
	
	public BarNotePermutation(SpeciesInformation aSpeciesInformation, CompoundBarLinkInformation aCompoundBarLinkInformation, 
			Collection<Chord> aColOfAllowedChords){
		speciesInformation = aSpeciesInformation;
		compoundBarLinkInformation = aCompoundBarLinkInformation;
		allowedChords = aColOfAllowedChords;
	}
	
	/**
	 * Copy constructor used in the generator which takes every property form the passed
	 * in permutation except the species element list. This is taken from the other
	 * paraemeter.
	 * @param barNotePermutation
	 * @param speciesElementList
	 */	
	public BarNotePermutation(
			BarNotePermutation barNotePermutation,
			List<SpeciesElement> firstSpeciesElementsForEachVoice) {
		super();
		this.speciesInformation = barNotePermutation.speciesInformation;
		this.firstSpeciesElementsForEachVoice = firstSpeciesElementsForEachVoice;
		this.compoundBarLinkInformation = barNotePermutation.compoundBarLinkInformation;
		this.timeSignature = barNotePermutation.timeSignature;
		this.key = barNotePermutation.key;
		this.tempo = barNotePermutation.tempo;
		this.barNumber = barNotePermutation.barNumber;
		this.allowedChords = barNotePermutation.allowedChords;
		this.analysisPerformed = barNotePermutation.analysisPerformed;
		this.harmonicAnalysisResultsMap = barNotePermutation.harmonicAnalysisResultsMap;
	}

	public void setBarValues(Bar bar){
		timeSignature = bar.getTimeSignature();
		key = bar.getKey();
		tempo = bar.getTempo();
		barNumber = bar.getBarNumber();
	}
	public SpeciesInformation getSpeciesInformation() {
		return speciesInformation;
	}
	
	/**
	 * This adds a first species element associated for one voice for this bar/step
	 * permutation object.
	 * The processors that perform analysis is done each time one is added.
	 * @param firstSpeciesElement
	 */
	public void addFirstSpeciesElement(SpeciesElement firstSpeciesElement) {
		firstSpeciesElementsForEachVoice.add(firstSpeciesElement);		
	}
	public Iterator<SpeciesElement> iterator() {
		return firstSpeciesElementsForEachVoice.iterator();
	}
	public List<SpeciesElement> getFirstSpeciesElementsForEachVoice() {
		return firstSpeciesElementsForEachVoice;
	}
	public CompoundBarLinkInformation getCompoundBarLinkInformation() {
		return compoundBarLinkInformation;
	}
	public TimeSignature getTimeSignature() {
		return timeSignature;
	}
	public Key getKey() {
		return key;
	}
	public Tempo getTempo() {
		return tempo;
	}
	public int getBarNumber() {
		return barNumber;
	}
	
	/**
	 * Returns a map of the harmonic relationships for every note between any of the two voice
	 * species elements.
	 * @return
	 */
	public Map<HarmonicAnalysisPairingKey, HarmonicIntervalRelationship> getHarmonicAnalysisResultsMap() {
		return harmonicAnalysisResultsMap;
	}
	
	/**
	 * This performs all of the analysis that can be done for once in a permutation so that it isn't
	 * repeated over and over again. This method is called by the counterpoint composition for
	 * all permutations before running the rules.
	 * LOW A command design pattern would be a much better way of doing this.
	 */
	public void performPermutationAnalysis(){
		List<SpeciesElement> firstSpeciesElementsForEachVoiceNoRests = new ArrayList<SpeciesElement>();
		ListIterator<SpeciesElement> listIter = firstSpeciesElementsForEachVoice.listIterator();
		while(listIter.hasNext()){
			SpeciesElement speciesElement = listIter.next();
			if(!speciesElement.isRest()){
				firstSpeciesElementsForEachVoiceNoRests.add(speciesElement);
			}
		}

		PermutationAnalyser.setFirstSpeciesVoicePitchOrdering(firstSpeciesElementsForEachVoiceNoRests);
		harmonicAnalysisResultsMap = 
				PermutationAnalyser.evaluateNoteHarmonyRelationships(firstSpeciesElementsForEachVoiceNoRests);


		analysisPerformed = true;

	}
	
	public void setSpeciesInformation(SpeciesInformation speciesInformation) {
		this.speciesInformation = speciesInformation;
	}

	public void setCompoundBarLinkInformation(
			CompoundBarLinkInformation compoundBarLinkInformation) {
		this.compoundBarLinkInformation = compoundBarLinkInformation;
	}
	public void setTimeSignature(TimeSignature timeSignature) {
		this.timeSignature = timeSignature;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public void setTempo(Tempo tempo) {
		this.tempo = tempo;
	}
	public void setBarNumber(int barNumber) {
		this.barNumber = barNumber;
	}

	public boolean isAnalysisPerformed() {
		return analysisPerformed;
	}
	
	public SpeciesElement getVoiceSpeciesElement(int voiceNumber){
		return firstSpeciesElementsForEachVoice.get(voiceNumber);
	}
	//FIXME this should use the track number
	public int getVoiceNumberOfSpeciesElementInPermutation(SpeciesElement speciesElement){
		return firstSpeciesElementsForEachVoice.indexOf(speciesElement);
	}
	
	public int getNumberOfFirstSpeciesElements(){
		return firstSpeciesElementsForEachVoice.size();
	}

	public Collection<Chord> getAllowedChords() {
		return allowedChords;
	}

	public Collection<KeyNote> getAllFirstSpeciesKeyNotes() {
		Set<KeyNote> firstSpeciesKeyNotes = new HashSet<KeyNote>();
		for(SpeciesElement firstSpeciesElement : firstSpeciesElementsForEachVoice){
			if(!firstSpeciesElement.isRest()){
				//TODO provide more getters so that it isn't done through all the objects like this
				KeyNote keyNote = firstSpeciesElement.getCounterpointBarEvent().getStaveLineNote().getKeyNote();
				firstSpeciesKeyNotes.add(keyNote);
			}
		}
		return firstSpeciesKeyNotes;
	}

	public Collection<SpeciesElement> getAllRestFirstSpeciesElements() {
		Set<SpeciesElement> firstSpeciesRests = new HashSet<SpeciesElement>();
		for(SpeciesElement firstSpeciesElement : firstSpeciesElementsForEachVoice){
			if(firstSpeciesElement.isRest()){
				firstSpeciesRests.add(firstSpeciesElement);
			}
		}
		return firstSpeciesRests;
	}

	public Map<Integer, SpeciesElement> getTrackNumToSpeciesElementMap() {
		Map<Integer, SpeciesElement> trackNumToSpeciesElementMap = new HashMap<Integer, SpeciesElement>();
		for(SpeciesElement speciesElement : firstSpeciesElementsForEachVoice){
			trackNumToSpeciesElementMap.put(speciesElement.getTrackNumber(), speciesElement);
		}
		return trackNumToSpeciesElementMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((allowedChords == null) ? 0 : allowedChords.hashCode());
		result = prime * result + (analysisPerformed ? 1231 : 1237);
		result = prime * result + barNumber;
		result = prime
				* result
				+ ((compoundBarLinkInformation == null) ? 0
						: compoundBarLinkInformation.hashCode());
		result = prime
				* result
				+ ((firstSpeciesElementsForEachVoice == null) ? 0
						: firstSpeciesElementsForEachVoice.hashCode());
		result = prime
				* result
				+ ((harmonicAnalysisResultsMap == null) ? 0
						: harmonicAnalysisResultsMap.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime
				* result
				+ ((speciesInformation == null) ? 0 : speciesInformation
						.hashCode());
		result = prime * result + ((tempo == null) ? 0 : tempo.hashCode());
		result = prime * result
				+ ((timeSignature == null) ? 0 : timeSignature.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BarNotePermutation other = (BarNotePermutation) obj;
		if (allowedChords == null) {
			if (other.allowedChords != null)
				return false;
		} else if (!allowedChords.equals(other.allowedChords))
			return false;
		if (analysisPerformed != other.analysisPerformed)
			return false;
		if (barNumber != other.barNumber)
			return false;
		if (compoundBarLinkInformation == null) {
			if (other.compoundBarLinkInformation != null)
				return false;
		} else if (!compoundBarLinkInformation
				.equals(other.compoundBarLinkInformation))
			return false;
		if (firstSpeciesElementsForEachVoice == null) {
			if (other.firstSpeciesElementsForEachVoice != null)
				return false;
		} else if (!firstSpeciesElementsForEachVoice
				.equals(other.firstSpeciesElementsForEachVoice))
			return false;
		if (harmonicAnalysisResultsMap == null) {
			if (other.harmonicAnalysisResultsMap != null)
				return false;
		} else if (!harmonicAnalysisResultsMap
				.equals(other.harmonicAnalysisResultsMap))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (speciesInformation == null) {
			if (other.speciesInformation != null)
				return false;
		} else if (!speciesInformation.equals(other.speciesInformation))
			return false;
		if (tempo == null) {
			if (other.tempo != null)
				return false;
		} else if (!tempo.equals(other.tempo))
			return false;
		if (timeSignature == null) {
			if (other.timeSignature != null)
				return false;
		} else if (!timeSignature.equals(other.timeSignature))
			return false;
		return true;
	}
}
