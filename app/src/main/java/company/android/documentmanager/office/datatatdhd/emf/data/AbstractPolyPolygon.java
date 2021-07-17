// Copyright 2007, FreeHEP.
package company.android.documentmanager.office.datatatdhd.emf.data;

import company.android.documentmanager.office.java.awt.Rectangle;
import company.android.documentmanager.office.java.awt.geom.GeneralPath;
import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

import android.graphics.Point;

/**
 * abstract parent for PolyPolygon drawing
 *
 * @author Steffen Greiffenberg
 * @version $Id$
 */
public abstract class AbstractPolyPolygon extends EMFTag {

    private Rectangle bounds;

    private int[] numberOfPoints;

    private Point[][] points;

    /**
     * Constructs a EMFTag.
     *
     *  id      id of the element
     *  version emf version in which this element was first supported
     *  bounds bounds of figure
     *  numberOfPoints number of points
     *  points points
     */
    protected AbstractPolyPolygon(
        int id, int version,
        Rectangle bounds,
        int[] numberOfPoints,
        Point[][] points) {

        super(id, version);
        this.bounds = bounds;
        this.numberOfPoints = numberOfPoints;
        this.points = points;
    }

    public String toString() {
        return super.toString() +
            "\n  bounds: " + bounds +
            "\n  #polys: " + numberOfPoints.length;
    }

    protected Rectangle getBounds() {
        return bounds;
    }

    protected int[] getNumberOfPoints() {
        return numberOfPoints;
    }

    protected Point[][] getPoints() {
        return points;
    }

    /**
     * displays the tag using the renderer. The default behavior
     * is to close and fill the polgygons for rendering.
     *
     *  renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        render(renderer,  true);
    }

    /**
     * displays the tag using the renderer
     *
     *  renderer EMFRenderer storing the drawing session data
     *  closePath if true the path is closed and filled
     */
    protected void render(EMFRenderer renderer, boolean closePath) {
        // create a GeneralPath containing GeneralPathes
        GeneralPath path = new GeneralPath(
            renderer.getWindingRule());

        // iterate the polgons
        Point p;
        for (int polygon = 0; polygon < numberOfPoints.length; polygon++) {

            // create a new member of path
            GeneralPath gp = new GeneralPath(
                renderer.getWindingRule());
            for (int point = 0; point < numberOfPoints[polygon]; point ++) {
                // add a point to gp
                p = points[polygon][point];
                if (point > 0) {
                    gp.lineTo((float) p.x,  (float)p.y);
                } else {
                    gp.moveTo((float) p.x,  (float)p.y);
                }
            }

            // close the member, add it to path
            if (closePath) {
                gp.closePath();
            }

            path.append(gp, false);
        }

        // draw the complete path
        if (closePath) {
            renderer.fillAndDrawOrAppend(path);
        } else {
            renderer.drawOrAppend(path);
        }
    }
}
