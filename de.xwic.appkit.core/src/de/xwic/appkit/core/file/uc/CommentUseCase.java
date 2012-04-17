/**
 * 
 */
package de.xwic.appkit.core.file.uc;

import java.util.Iterator;
import java.util.List;

import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.model.entities.IEntityComment;

/**
 * Saves the given comments.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class CommentUseCase implements DAOCallback {

	private List<IEntityComment> comments;
	
	/**
	 * 
	 * @param commentList
	 * @param commentDao
	 */
	public CommentUseCase(List<IEntityComment> commentList) {
		this.comments = commentList;
	}
	
	
	/* (non-Javadoc)
	 * @see de.jwic.entitytools.base.DAOCallback#run(de.jwic.entitytools.base.DAOProviderAPI)
	 */
	public Object run(DAOProviderAPI api) {
		for (Iterator<IEntityComment> iterator = comments.iterator(); iterator.hasNext();) {
			IEntityComment c = iterator.next();
			api.update(c);
		}
        return null;
	}
}
