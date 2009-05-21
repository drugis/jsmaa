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

package fi.smaa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.Alternative;
import fi.smaa.Criterion;
import fi.smaa.Measurement;

public class CriterionTest {
	
	@SuppressWarnings("unchecked")
	private Criterion criterion;
	
	private List<Alternative> alts;
	
	@SuppressWarnings("unchecked")
	private Criterion createInstance() {
		return new Criterion("name") {
			public String getTypeLabel() {
				return null;
			}
			protected Measurement createMeasurement() {
				return null;
			}
			protected void fireMeasurementChange() {
			}
			public Map getMeasurements() {
				return new HashMap();
			}
			@Override
			public Criterion deepCopy() {
				return null;
			}			
		};
		
	}
	
	@Before
	public void setUp() {
		criterion = createInstance();
		alts = new ArrayList<Alternative>();
		alts.add(new Alternative("alt"));			
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testConstructor() {
		Criterion c = createInstance();
		c.setName("c");
		assertEquals("c", c.getName());
	}
	
	@Test
	public void testSetName() {
		JUnitUtil.testSetter(criterion, Criterion.PROPERTY_NAME, "name", "nameCrit");
	}

	
	@Test
	public void testToString() {
		criterion.setName("crit");
		assertEquals("crit", criterion.toString());
	}
	
	@Test
	public void testSetAlternatives() {
		List<Alternative> list = new ArrayList<Alternative>();
		list.add(new Alternative("alt"));
		JUnitUtil.testSetter(criterion, Criterion.PROPERTY_ALTERNATIVES, new ArrayList<Alternative>(), list);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testequals() {
		Criterion c2 = createInstance();
		assertTrue(criterion.equals(c2));
		c2.setName("newname");
		assertFalse(criterion.equals(c2));
	}
	
}
