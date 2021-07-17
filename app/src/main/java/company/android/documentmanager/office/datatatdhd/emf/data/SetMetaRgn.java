// Copyright 2002, FreeHEP.
package company.android.documentmanager.office.datatatdhd.emf.data;

import java.io.IOException;

import company.android.documentmanager.office.datatatdhd.emf.EMFInputStream;
import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

/**
 * SetMetaRgn TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SetMetaRgn.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SetMetaRgn extends EMFTag {

    public SetMetaRgn() {
        super(28, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return this;
    }

    /**
     * displays the tag using the renderer
     *
     *  renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The SetMetaRgn function intersects the current clipping region
        // for the specified device context with the current metaregion and
        // saves the combined region as the new metaregion for the specified
        // device context. The clipping region is reset to a null region.

        // TODO: what ist the current metaregion?
    }
}
