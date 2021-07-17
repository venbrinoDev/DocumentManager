// Copyright 2002, FreeHEP.
package company.android.documentmanager.office.datatatdhd.emf.data;

import java.io.IOException;

import company.android.documentmanager.office.datatatdhd.emf.EMFInputStream;
import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

/**
 * EndPath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: EndPath.java 10367 2007-01-22 19:26:48Z duns $
 */
public class EndPath extends EMFTag {

    public EndPath() {
        super(60, 1);
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
        // TODO: fix EMFGraphics2D?
        // this happens only when EMF is created by EMFGraphics2D
        // there could be an open figure (created with LineTo, PolylineTo etc.)
        // that is not closed and therefore not written to the currentPath
        //renderer.closeFigure();
    	renderer.appendFigure();

        // The EndPath function closes a path bracket and selects the path
        // defined by the bracket into the specified device context.
        //renderer.closePath();
    }
}
