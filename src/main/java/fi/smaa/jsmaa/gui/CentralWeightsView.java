/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/

package fi.smaa.jsmaa.gui;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.LayoutUtil;
import fi.smaa.jsmaa.gui.jfreechart.CentralWeightsDataset;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.simulator.SMAA2Results;

public class CentralWeightsView extends SMAA2ResultsView {
	
	private JLabel[][] CWCells;
	private JLabel[] CFCells;

	public CentralWeightsView(SMAA2Results results) {
		super(results);
	}
	
	synchronized public JComponent buildPanel() {
	
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p, 3dlu, p, 3dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Central weight vectors and confidence factors", cc.xy(1, 1));
		
		builder.add(buildCWPart(), cc.xy(1, 3));
	
		builder.addSeparator("", cc.xy(1, 5));
		builder.add(buildFigurePart(), cc.xy(1, 7));
		
		fireResultsChanged();		
		return builder.getPanel();
	}

	private JComponent buildCWPart() {
		int numAlts = getNumAlternatives();
		int numCrit = getCriteria().size();
		
		FormLayout layout = new FormLayout(
				"pref, 3dlu, center:pref",
				"p");
		
		int[] groupCol = new int[numCrit];
		
		for (int i=0;i<numAlts;i++) {
			LayoutUtil.addRow(layout);
		}
		for (int i=0;i<numCrit;i++) {
			layout.appendColumn(ColumnSpec.decode("5dlu"));
			layout.appendColumn(ColumnSpec.decode("center:pref"));
			groupCol[i] = 5 + 2*i;			
		}
		
		layout.setColumnGroups(new int[][]{groupCol});

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		JLabel cfLabel = new JLabel("CF");
		cfLabel.setToolTipText("Confidence factor");
		builder.add(cfLabel, cc.xy(3, 1));
		
		CellConstraints cc1 = new CellConstraints();
		int col = 5;
		int row1 = 1;
		for (Criterion c : getCriteria()) {
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Criterion>(c).getModel(Criterion.PROPERTY_NAME)),
					cc1.xy(col, row1));
			col += 2;
		}
		int startRow = 3;
		int startCol = 1;
		CellConstraints cc2 = new CellConstraints();
		for (Alternative alt : results.getAlternatives()) {
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(alt).getModel(Alternative.PROPERTY_NAME)),
					cc2.xy(startCol, startRow));
			if (true) {
				startRow += 2;
			} else {
				startCol += 2;
			}
		}
		CellConstraints cc3 = new CellConstraints();
		int numAlternatives = getNumAlternatives();
		int numCriteria = getCriteria().size();
		CWCells = new JLabel[numAlternatives][numCriteria];
		CFCells = new JLabel[numAlternatives];
		int startRow1 = 3;
		int startCol1 = 5;
		int CFcol = 3;
		for (int altIndex=0;altIndex<numAlternatives;altIndex++) {
			JLabel CFlabel = new JLabel("NA");
			CFCells[altIndex] = CFlabel;
			builder.add(CFlabel, cc3.xy(CFcol, startRow1 + altIndex * 2));
			for (int critIndex=0;critIndex<numCriteria;critIndex++) {
				JLabel label = new JLabel("NA");
				CWCells[altIndex][critIndex] = label;
				builder.add(label, cc3.xy(startCol1 + critIndex * 2, startRow1 + altIndex * 2));
			}
		}
		return builder.getPanel();
	}
	
	private JComponent buildFigurePart() {
		CategoryDataset dataset = new CentralWeightsDataset((SMAA2Results) results);
		final JFreeChart chart = ChartFactory.createLineChart(
                "", "Criterion", "Central Weight",
                dataset, PlotOrientation.VERTICAL, true, true, false);
		LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
		chart.getCategoryPlot().setRenderer(renderer);
		ChartPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
	}	
	
	synchronized public void fireResultsChanged() {
		//change confidence factors
		Map<Alternative, Double> cfs = ((SMAA2Results) results).getConfidenceFactors();
		for (int altIndex = 0;altIndex<results.getAlternatives().size();altIndex++) {
			CFCells[altIndex].setText(formatDouble(cfs.get(results.getAlternatives().get(altIndex))));
		}
		
		//change central weights
		Map<Alternative, Map<Criterion, Double>> cws = ((SMAA2Results) results).getCentralWeightVectors();
		
		for (int altIndex=0;altIndex<results.getAlternatives().size();altIndex++) {
			Map<Criterion, Double> cw = cws.get(results.getAlternatives().get(altIndex));
			for (int critIndex=0;critIndex<getCriteria().size();critIndex++) {
				CWCells[altIndex][critIndex].setText(formatDouble(cw.get(getCriteria().get(critIndex))));
			}
		}
	}

}
