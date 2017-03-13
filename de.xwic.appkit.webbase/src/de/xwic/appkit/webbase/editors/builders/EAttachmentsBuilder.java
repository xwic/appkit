/**
 * 
 */
package de.xwic.appkit.webbase.editors.builders;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.config.editor.EAttachments;
import de.xwic.appkit.core.config.editor.Style;
import de.xwic.appkit.webbase.editors.EditorContext;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.controls.AttachmentsControl;
import de.xwic.appkit.webbase.editors.events.EditorAdapter;
import de.xwic.appkit.webbase.editors.events.EditorEvent;
import de.xwic.appkit.webbase.editors.mappers.AttachmentsMapper;

/**
 * @author lippisch
 */
public class EAttachmentsBuilder extends Builder<EAttachments> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement, de.jwic.base.IControlContainer, de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	@Override
	public IControl buildComponents(EAttachments element, IControlContainer parent, IBuilderContext context) {

		AttachmentsControl attmControl = new AttachmentsControl(parent, null);
		Style style = element.getStyle();
		if (style.getStyleInt(Style.WIDTH_HINT) != 0) {
			attmControl.setWidth(style.getStyleInt(Style.WIDTH_HINT));
		}

		if (style.getStyleInt(Style.HEIGHT_HINT) != 0) {
			attmControl.setHeight(style.getStyleInt(Style.HEIGHT_HINT));
		}
		
		if (context instanceof EditorContext) {
			EditorContext ex = (EditorContext)context;
			ex.addEditorListener(new EditorAdapter() {
				@Override
				public void afterSave(EditorEvent event) {
					attmControl.saveAttachmentList(ex.getModel().getOriginalEntity());
				}
			});
		}
		
		context.registerField(null, attmControl, element, AttachmentsMapper.MAPPER_ID);
		
		return attmControl;
	}

}
