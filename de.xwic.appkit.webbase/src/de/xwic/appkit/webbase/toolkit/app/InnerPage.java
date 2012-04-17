/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.Dimension;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;
import de.jwic.base.Page;
import de.jwic.events.IPageListener;
import de.jwic.events.PageEvent;

/**
 * Template for an innner page view. Normally used
 * as "entry" template of a submodule.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class InnerPage extends ControlContainer implements IPageControl,
		IOuterLayout {

    private String title = "";
    private String subtitle = "";

    private int scrollTop = 0;
    private int scrollLeft = 0;
    
    private int maxWidth = 0; // no max width
    private int minWidth = 0; // no min width
    
	/**
	 * Creates the template.
	 * 
	 * @param container
	 * @param name
	 */
	public InnerPage(IControlContainer container, String name) {
		super(container, name);
        setRendererId(DEFAULT_OUTER_RENDERER);

        // add a listener to refresh after the page has been resized.
        Page myPage = Page.findPage(this);
        myPage.addPageListener(new IPageListener() {
        	@Override
        	public void pageSizeChanged(PageEvent event) {
        		if (maxWidth > 0 || minWidth > 0) {
        			InnerPage.this.requireRedraw();
        		}
        	}
        });
        
	}

	
	
	/* (non-Javadoc)
	 * @see de.pol.ui.toolkit.app.IPageControl#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see de.pol.ui.toolkit.app.IPageControl#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;

	}

	@Override
	public String getTemplateName() {
        if (super.getTemplateName().equals(getOuterTemplateName())) {
            return null;
        }
        return super.getTemplateName();
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.base.IOuterLayout#getOuterTemplateName()
	 */
	public String getOuterTemplateName() {
		return InnerPage.class.getName();
	}



	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}



	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}



	/**
	 * @return the scrollTop
	 */
	public int getScrollTop() {
		return scrollTop;
	}



	/**
	 * @param scrollTop the scrollTop to set
	 */
	public void setScrollTop(int scrollTop) {
		this.scrollTop = scrollTop;
	}



	/**
	 * @return the scrollLeft
	 */
	public int getScrollLeft() {
		return scrollLeft;
	}



	/**
	 * @param scrollLeft the scrollLeft to set
	 */
	public void setScrollLeft(int scrollLeft) {
		this.scrollLeft = scrollLeft;
	}



	/**
	 * @return the maxWidth
	 */
	public int getMaxWidth() {
		return maxWidth;
	}



	/**
	 * @param maxWidth the maxWidth to set
	 */
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}



	/**
	 * @return the minWidht
	 */
	public int getMinWidth() {
		return minWidth;
	}



	/**
	 * @param minWidht the minWidht to set
	 */
	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}

	/**
	 * Calculate the fixed width (if to be set) based on the current resolution.
	 * @return
	 */
	public int getFixedWidth() {
		
		if (maxWidth != 0 || minWidth != 0) {
			Page myPage = Page.findPage(this);
			if (myPage != null) {
				Dimension size = myPage.getPageSize();
				if (size.width > 0) {	// width is known.
					int width = size.width - 10; // reduce by the default margin
					if (maxWidth > 0 && width > maxWidth) {
						return maxWidth;
					} else if (minWidth > 0 && width < minWidth) {
						return minWidth;
					}
				}
			}
		}
		return 0; // dont set
	}
	
}
