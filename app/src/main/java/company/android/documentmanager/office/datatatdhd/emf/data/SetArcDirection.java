// Copyright 2002, FreeHEP.

package company.android.documentmanager.office.datatatdhd.emf.data;

import java.io.IOException;

import company.android.documentmanager.office.datatatdhd.emf.EMFConstants;
import company.android.documentmanager.office.datatatdhd.emf.EMFInputStream;
import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

/**
 * SetArcDirection TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: SetArcDirection.java 10377 2007-01-23 15:44:34Z duns $
 */
public class SetArcDirection extends EMFTag implements EMFConstants
{

    private int direction;

    public SetArcDirection()
    {
        super(57, 1);
    }

    public SetArcDirection(int direction)
    {
        this();
        this.direction = direction;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetArcDirection(emf.readDWORD());
    }

    public String toString()
    {
        return super.toString() + "\n  direction: " + direction;
    }

    /**
     * displays the tag using the renderer
     *
     *  renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // The SetArcDirection sets the drawing direction to
        // be used for arc and rectangle functions.
        renderer.setArcDirection(direction);
    }
}
