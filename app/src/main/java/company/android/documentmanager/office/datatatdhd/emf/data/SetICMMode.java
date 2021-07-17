// Copyright 2002, FreeHEP.

package company.android.documentmanager.office.datatatdhd.emf.data;

import java.io.IOException;

import company.android.documentmanager.office.datatatdhd.emf.EMFConstants;
import company.android.documentmanager.office.datatatdhd.emf.EMFInputStream;
import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

/**
 * SetICMMode TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetICMMode.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetICMMode extends EMFTag implements EMFConstants
{

    private int mode;

    public SetICMMode()
    {
        super(98, 1);
    }

    public SetICMMode(int mode)
    {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetICMMode(emf.readDWORD());
    }

    public String toString()
    {
        return super.toString() + "\n  mode: " + mode;
    }

    /**
     * displays the tag using the renderer
     *
     *  renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        // do nothing
    }
}
