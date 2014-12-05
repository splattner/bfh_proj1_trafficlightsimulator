/*
 * Copyright 2014
 * Sebastian Plattner, Donatello Gallucci
 * Bern University of applied Science
 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.bfh.proj1.trafficlightsimulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public interface DrawableObject {
	
	/**
	 * Paint the object
	 * @param g
	 */
	public void paintObject(Graphics g);
	
	/**
	 * Return the origin of this object
	 * @return Point
	 */
	public Point getOrigin();
	
	/**
	 * Set Origin of this object
	 * @param origin
	 */
	public void setOrigin(Point origin);
	
	/**
	 * Return dimension of this object
	 * @return
	 */
	public Dimension getDimension();
	
	/**
	 * Set dimension of this object
	 * @param dimension
	 */
	public void setDimension(Dimension dimension);
	

}
