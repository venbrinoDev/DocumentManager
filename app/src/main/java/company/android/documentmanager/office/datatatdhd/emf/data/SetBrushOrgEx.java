// Copyright 2002, FreeHEP.

package company.android.documentmanager.office.datatatdhd.emf.data;

import android.graphics.Point;
import java.io.IOException;

import company.android.documentmanager.office.datatatdhd.emf.EMFInputStream;
import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

/**
 * SetBrushOrgEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetBrushOrgEx.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetBrushOrgEx extends EMFTag
{

    private Point point;

    public SetBrushOrgEx()
    {
        super(13, 1);
    }

    public SetBrushOrgEx(Point point)
    {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetBrushOrgEx(emf.readPOINTL());
    }

    public String toString()
    {
        return super.toString() + "\n  point: " + point;
    }

    /**
     * displays the tag using the renderer
     *
     *  renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // The SetBrushOrgEx function sets the brush origin that GDI assigns to
        // the next (only to the next!) brush an application selects into the specified
        // device context.
        renderer.setBrushOrigin(point);
    }
}
