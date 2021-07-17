package company.android.documentmanager.office.datatatdhd.emf.data;

import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;

/**
 * A GDIObject uses a {@link EMFRenderer}
 * to render itself to a Graphics2D object.
 *
 * @author Steffen Greiffenberg
 * @version $Id$
 */
public interface GDIObject {

    /**
     * displays the tag using the renderer
     *
     *  renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer);
}
