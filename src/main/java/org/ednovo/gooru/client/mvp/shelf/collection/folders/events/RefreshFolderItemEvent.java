/**
 * 
 */
package org.ednovo.gooru.client.mvp.shelf.collection.folders.events;

import java.util.HashMap;

import org.ednovo.gooru.client.mvp.folders.event.RefreshFolderType;
import org.ednovo.gooru.shared.model.folder.FolderDo;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Search Team
 * 
 */
public class RefreshFolderItemEvent extends GwtEvent<RefreshFolderItemHandler> {

	public static final Type<RefreshFolderItemHandler> TYPE = new Type<RefreshFolderItemHandler>();

	private FolderDo folderDo;
	
	private RefreshFolderType refreshFolderType;
	
	private HashMap<String,String> params;
	
	/**
	 * Class constructor , assign  collection object and refresh type
	 */
	public RefreshFolderItemEvent(FolderDo folderDo, RefreshFolderType refreshFolderType, HashMap<String,String> params) {
		this.folderDo = folderDo;
		this.refreshFolderType = refreshFolderType;
		this.params = params;
	}

	@Override
	public Type<RefreshFolderItemHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RefreshFolderItemHandler handler) {
		handler.refreshFolderItem(folderDo, refreshFolderType, params);
	}
}