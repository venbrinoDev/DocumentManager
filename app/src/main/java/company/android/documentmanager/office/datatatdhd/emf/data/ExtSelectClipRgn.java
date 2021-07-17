// Copyright 2002, FreeHEP.

package company.android.documentmanager.office.datatatdhd.emf.data;

import java.io.IOException;

import company.android.documentmanager.office.datatatdhd.emf.EMFConstants;
import company.android.documentmanager.office.datatatdhd.emf.EMFInputStream;
import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

/**
 * ExtSelectClipRgn TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: ExtSelectClipRgn.java 10515 2007-02-06 18:42:34Z duns $
 */
public class ExtSelectClipRgn extends AbstractClipPath
{

    private Region rgn;

    public ExtSelectClipRgn()
    {
        super(75, 1, EMFConstants.RGN_COPY);
    }

    public ExtSelectClipRgn(int mode, Region rgn)
    {
        super(75, 1, mode);
        this.rgn = rgn;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) throws IOException
    {

        int length = emf.readDWORD();
        int mode = emf.readDWORD();
        return new ExtSelectClipRgn(mode, length > 8 ? new Region(emf) : null);
    }

    /**
     * displays the tag using the renderer
     *
     *  renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        if (rgn == null || rgn.getBounds() == null)
        {
            return;
        }

        render(renderer, rgn.getBounds());
    }
}
