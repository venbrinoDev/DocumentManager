// Copyright 2002, FreeHEP.

package company.android.documentmanager.office.datatatdhd.emf.data;

import android.graphics.Point;

import java.io.IOException;

import company.android.documentmanager.office.java.awt.Rectangle;
import company.android.documentmanager.office.datatatdhd.emf.EMFInputStream;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

/**
 * PolylineTo16 TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: PolylineTo16.java 10367 2007-01-22 19:26:48Z duns $
 */
public class PolylineTo16 extends PolylineTo
{

    public PolylineTo16()
    {
        super(89, 1, null, 0, null);
    }

    public PolylineTo16(Rectangle bounds, int numberOfPoints, Point[] points)
    {
        super(89, 1, bounds, numberOfPoints, points);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        Rectangle r = emf.readRECTL();
        int n = emf.readDWORD();
        return new PolylineTo16(r, n, emf.readPOINTS(n));
    }

}
