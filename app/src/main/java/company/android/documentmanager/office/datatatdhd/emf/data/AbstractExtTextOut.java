// Copyright 2007, FreeHEP

package company.android.documentmanager.office.datatatdhd.emf.data;

import company.android.documentmanager.office.java.awt.Rectangle;
import company.android.documentmanager.office.datatatdhd.emf.EMFConstants;
import company.android.documentmanager.office.datatatdhd.emf.EMFRenderer;
import company.android.documentmanager.office.datatatdhd.emf.EMFTag;

/**
 * Abstraction of commonality between the {@link ExtTextOutA} and {@link ExtTextOutW} tags.
 *
 * @author Daniel Noll (daniel@nuix.com)
 * @version $Id: ExtTextOutW.java 10140 2006-12-07 07:50:41Z duns $
 */
public abstract class AbstractExtTextOut extends EMFTag implements EMFConstants
{

    private Rectangle bounds;

    private int mode;

    private float xScale, yScale;

    /**
     * Constructs the tag.
     *
     *  id id of the element
     *  version emf version in which this element was first supported
     *  bounds text boundary
     *  mode text mode
     *  xScale horizontal scale factor
     *  yScale vertical scale factor
     */
    protected AbstractExtTextOut(int id, int version, Rectangle bounds, int mode, float xScale,
        float yScale)
    {

        super(id, version);
        this.bounds = bounds;
        this.mode = mode;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public abstract Text getText();

    public String toString()
    {
        return super.toString() + "\n  bounds: " + bounds + "\n  mode: " + mode + "\n  xScale: "
            + xScale + "\n  yScale: " + yScale + "\n" + getText().toString();
    }

    /**
     * displays the tag using the renderer
     *
     *  renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer)
    {
        Text text = getText();
        renderer.drawOrAppendText(text.getString(), text.getPos().x, text.getPos().y);
    }
}
