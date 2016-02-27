package counterpointOperations.rules.melodic.subRules;

import java.awt.Graphics;
import java.awt.Point;

import composition.reporting.AnnotationObject;
import composition.reporting.EvaluationScoringObject;
import composition.reporting.RuleReport;


import musicRelatedEntities.key.Interval;

import counterpointOperations.counterpointComposition.CounterpointComposition;
import counterpointOperations.counterpointMusicRelatedEntities.interval.intervalRelationships.MelodicIntervalRelationship;


public class AllowedIntervalsMelodicSubRule extends MelodicSubRule{	
	private static int PENALTY_VALUE = 10000;
	@Override
	public void evaluateInterval(
			MelodicIntervalRelationship melodicIntervalRelationship, 
			CounterpointComposition aCounterpointComposition, boolean annotateComposition) {
		EvaluationScoringObject evaluationScoringObject = aCounterpointComposition.getEvaluationScoringObject();
		AllowedIntervalsMelodicSubRuleReport melodicRuleReport = null;

		//Check if it's more than an octave
		if(Math.abs(melodicIntervalRelationship.getNumberOfSemiTones()) > 12){
			if(annotateComposition){
				melodicRuleReport = new AllowedIntervalsMelodicSubRuleReport(
						"Melodic intervals of greater than an octave are not allowed");
				setAnnotationPoints(melodicRuleReport, melodicIntervalRelationship);
				aCounterpointComposition.addRuleReport(melodicRuleReport);
			}else{
				evaluationScoringObject.addToScore(PENALTY_VALUE);
			}
		}else if(Interval.MAJOR_SEVENTH.equals(melodicIntervalRelationship.getInterval())
				|| Interval.MINOR_SEVENTH.equals(melodicIntervalRelationship.getInterval())
				|| Interval.TRITONE.equals(melodicIntervalRelationship.getInterval())){
			if(annotateComposition){
				melodicRuleReport = new AllowedIntervalsMelodicSubRuleReport(
						"The melodic interval of a " + melodicIntervalRelationship.getInterval() 
						+ " is not allowed");		
				setAnnotationPoints(melodicRuleReport, melodicIntervalRelationship);
				aCounterpointComposition.addRuleReport(melodicRuleReport);
			}else{
				evaluationScoringObject.addToScore(PENALTY_VALUE);
			}
		}
	}
	
	private void setAnnotationPoints(AllowedIntervalsMelodicSubRuleReport melodicRuleReport, 
			MelodicIntervalRelationship melodicIntervalRelationship){
		AnnotationObject noteAnnotationObject = new AnnotationObject();
		melodicIntervalRelationship.getFirstSpeciesElement().addCounterpointAnnotationObject(noteAnnotationObject);
		melodicIntervalRelationship.getSecondSpeciesElement().addCounterpointAnnotationObject(noteAnnotationObject);
		melodicRuleReport.setNoteAnnotationObject(noteAnnotationObject);
	}

	private class AllowedIntervalsMelodicSubRuleReport extends RuleReport{
		public AllowedIntervalsMelodicSubRuleReport(String reportComments) {
			super(reportComments);
		}

		private AnnotationObject noteAnnotationObject;
		

		@Override
		public void drawResultMarkings(Graphics g) {
			Point[] points = noteAnnotationObject.getPointsArray();
			Point firstPoint = points[0];
			Point secondPoint = points[1];
			
			g.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
		}
		
		public void setNoteAnnotationObject(AnnotationObject noteAnnotationObject) {
			this.noteAnnotationObject = noteAnnotationObject;
		}
	}
}
