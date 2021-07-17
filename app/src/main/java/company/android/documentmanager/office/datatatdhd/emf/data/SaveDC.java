// Copyright 2002, FreeHEP.
package company.android.documentmanager.office.datatatdhd.emf.data;

import java.io.IOException;

import company.android.documentmanager.office.datatatdhd.emf.EMFInputStream;
import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

/**
 * SaveDC TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: SaveDC.java 10367 2007-01-22 19:26:48Z duns $
 */
public class SaveDC extends EMFTag {

    public SaveDC() {
        super(33, 1);
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
        renderer.saveDC();
    }
}
