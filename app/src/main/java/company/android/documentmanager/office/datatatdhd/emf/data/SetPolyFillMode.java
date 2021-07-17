// Copyright 2002, FreeHEP.

package company.android.documentmanager.office.datatatdhd.emf.data;

import java.io.IOException;

import company.android.documentmanager.office.java.awt.geom.GeneralPath;
import company.android.documentmanager.office.datatatdhd.emf.EMFConstants;
import company.android.documentmanager.office.datatatdhd.emf.EMFInputStream;
import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

/**
 * SetPolyFillMode TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetPolyFillMode.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetPolyFillMode extends EMFTag implements EMFConstants
{

    private int mode;

    public SetPolyFillMode()
    {
        super(19, 1);
    }

    public SetPolyFillMode(int mode)
    {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        return new SetPolyFillMode(emf.readDWORD());
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
        renderer.setWindingRule(getWindingRule(mode));
    }

    /**
     * gets a winding rule for GeneralPath creation based on
     * EMF SetPolyFillMode.
     *
     *  polyFillMode PolyFillMode to convert
     * @return winding rule
     */
    private int getWindingRule(int polyFillMode)
    {
        if (polyFillMode == EMFConstants.WINDING)
        {
            return GeneralPath.WIND_EVEN_ODD;
        }
        else if (polyFillMode == EMFConstants.ALTERNATE)
        {
            return GeneralPath.WIND_NON_ZERO;
        }
        else
        {
            return GeneralPath.WIND_EVEN_ODD;
        }
    }

}
