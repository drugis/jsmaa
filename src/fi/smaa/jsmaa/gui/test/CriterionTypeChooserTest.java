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

package fi.smaa.jsmaa.gui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.CriterionTypeChooser;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.LogNormalMeasurement;
import fi.smaa.jsmaa.model.SMAAModel;

public class CriterionTypeChooserTest {

	CriterionTypeChooser chooser;
	private ScaleCriterion crit;
	private Alternative alt;
	private SMAAModel model;
	
	@Before
	public void setUp() throws Exception {
		setupModel();
		chooser = new CriterionTypeChooser(model, alt, crit);
	}
	
	@Test
	public void testConstructor() throws Exception {
		assertEquals(0, chooser.getSelectedIndex());
		model.setMeasurement(crit, alt, new GaussianMeasurement());
		chooser = new CriterionTypeChooser(model, alt, crit);
		assertEquals(1, chooser.getSelectedIndex());
	}
	
	@Test
	public void testChangesMeasurementInModel() throws Exception {
		chooser.setSelectedIndex(1);
		assertTrue(model.getMeasurement(crit, alt) instanceof GaussianMeasurement);		
		chooser.setSelectedIndex(2);
		assertTrue(model.getMeasurement(crit, alt) instanceof LogNormalMeasurement);	
	}
	
	private void setupModel() throws Exception {
		model = new SMAAModel("model");
		Alternative a1 = new Alternative("a1");
		Alternative a2 = new Alternative("a2");
		ScaleCriterion c1 = new ScaleCriterion("c1");
		ScaleCriterion c2 = new ScaleCriterion("c2");
		model.addAlternative(a1);
		model.addAlternative(a2);
		model.addCriterion(c1);
		model.addCriterion(c2);
		model.setMeasurement(c1, a1, new Interval(0.0, 6.0));
		alt = a1;
		crit = c1;
	}		
}