package de.xwic.appkit.webbase.entityselection;

/**
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public interface IPageListener {

    public enum PageEvent {
        OPENED, CLOSED
    }

    /**
     * @param event
     */
    public void pageStateChanged(PageEvent event);


}
