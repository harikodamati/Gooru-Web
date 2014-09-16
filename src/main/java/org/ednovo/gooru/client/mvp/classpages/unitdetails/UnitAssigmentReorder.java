package org.ednovo.gooru.client.mvp.classpages.unitdetails;

import java.util.ArrayList;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.settings.CustomAnimation;
import org.ednovo.gooru.client.uc.PlayerBundle;
import org.ednovo.gooru.shared.model.content.ClassDo;
import org.ednovo.gooru.shared.model.content.ClassUnitsListDo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class UnitAssigmentReorder extends PopupPanel{
private static UnitAssigmentReorderUiBinder uiBinder = GWT
			.create(UnitAssigmentReorderUiBinder.class);

	interface UnitAssigmentReorderUiBinder extends
			UiBinder<Widget, UnitAssigmentReorder> {
	}
	@UiField Image popupArrow;
	@UiField Button saveButton,CancelButton;
	@UiField Label titleLabel,descLabel,savingTextLabel;
	@UiField InlineLabel dropdownListPlaceHolder,dropdownListPlaceHolderAssignment;
	@UiField ScrollPanel dropdownListContainerScrollPanel,dropdownListContainerScrollPanelAssignment;
	@UiField HTMLPanel dropdownListContainer,dropdownListContainerAssignment,mainPanel;
	ClassDo classDo;
	int totalCount=0;
	int totalsize=0;
	String classpageId;
	private String selectedPathId;
	private HandlerRegistration onClickUnit;
	
	public UnitAssigmentReorder(ClassDo classDo,String classpageId) {
		setWidget(uiBinder.createAndBindUi(this));
		this.classDo = classDo;
		this.classpageId = classpageId;
		PlayerBundle.INSTANCE.getPlayerStyle().ensureInjected();
		setUnitAssignmentData(classDo);
				
	}
	public void setUnitAssignmentData(ClassDo classDo){
		popupArrow.setUrl("images/popArrow.png");
		saveButton.setText("Save");
		CancelButton.setText("Cancel");
		dropdownListContainerScrollPanel.getElement().getStyle().setDisplay(Display.NONE);
		dropdownListPlaceHolder.addClickHandler(new OnDropdownListPlaceHolderClick());
		saveButton.addClickHandler(new clickOnSave());
		dropdownListContainerScrollPanel.addScrollHandler(new ScrollDropdownListContainer());
		
		totalCount=classDo.getTotalHitCount()!=null?classDo.getTotalHitCount():0;
			if(classDo!=null&&classDo.getSearchResults()!=null&&classDo.getSearchResults().size()>0){
			ArrayList<ClassUnitsListDo> classListUnitsListDo =classDo.getSearchResults();
			titleLabel.setText(classListUnitsListDo.get(0).getResource().getTitle());
			totalsize =totalsize+classListUnitsListDo.size() ;
			descLabel.setText(classListUnitsListDo.get(0).getResource().getTitle());
			for(int i=0; i<classListUnitsListDo.size(); i++){
				int totalItemCount=classListUnitsListDo.get(0).getResource().getItemCount();
				displayAssignment(totalItemCount);
				int number=classListUnitsListDo.get(i).getItemSequence();
				dropdownListPlaceHolder.getElement().setInnerHTML(classListUnitsListDo.get(0).getItemSequence()+"");
				dropdownListPlaceHolder.getElement().setId(classListUnitsListDo.get(0).getCollectionItemId());
				String unitCollectionItemId=classListUnitsListDo.get(i).getCollectionItemId();
				Label dropDownListItem=new Label(number+"");
				dropDownListItem.getElement().setId(classListUnitsListDo.get(i).getResource().getItemCount()+"");
				
				dropDownListItem.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().dropdownListItemContainer());
				dropdownListContainer.add(dropDownListItem);
				dropDownListItem.addClickHandler(new OnDropdownItemClick(number+"",dropDownListItem.getElement().getId(),unitCollectionItemId));
				
			}
		}
		
				
	}
	public void displayAssignment(Integer totalItemCount)
	{
		dropdownListContainerAssignment.clear();
		dropdownListContainerScrollPanelAssignment.getElement().getStyle().setDisplay(Display.NONE);
		try{
			if(onClickUnit!=null) {
				onClickUnit.removeHandler();
			}
			
		}catch (AssertionError ae) { }
		onClickUnit = dropdownListPlaceHolderAssignment.addClickHandler(new OnDropdownListAssignmentPlaceHolderClick());
		
		if(totalItemCount!=null &&totalItemCount!=0){
			for(int i=1; i<=totalItemCount; i++){
				dropdownListPlaceHolderAssignment.getElement().setInnerHTML(1+"");
				int number=i;
				Label dropDownAssignmentListItem=new Label(number+"");
				dropDownAssignmentListItem.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().dropdownListItemContainer());
				dropdownListContainerAssignment.add(dropDownAssignmentListItem);
				dropDownAssignmentListItem.addClickHandler(new OnDropdownAssignmentItemClick(number+""));
			}
		}else
		{
			dropdownListPlaceHolderAssignment.getElement().setInnerHTML(1+"");
		}
	}
	private class ScrollDropdownListContainer implements ScrollHandler{
		@Override
		public void onScroll(ScrollEvent event) {
			if((dropdownListContainerScrollPanel.getVerticalScrollPosition() == dropdownListContainerScrollPanel.getMaximumVerticalScrollPosition())&&(totalCount>totalsize)){
				AppClientFactory.getInjector().getClasspageService().v2GetPathwaysOptimized(classpageId, Integer.toString(5),  Integer.toString(totalsize), new SimpleAsyncCallback<ClassDo>() {
					@Override
					public void onSuccess(ClassDo result) {
						totalsize = totalsize + result.getSearchResults().size();
						for(int i=0; i<result.getSearchResults().size(); i++){

							String CollectionItemId=result.getSearchResults().get(i).getCollectionItemId();
							int totalItemCount=result.getSearchResults().get(i).getResource().getItemCount();
							int number=result.getSearchResults().get(i).getItemSequence();
							Label dropDownListItem=new Label(number+"");
							dropDownListItem.getElement().setId(totalItemCount+"");
							dropDownListItem.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().dropdownListItemContainer());
							dropdownListContainer.add(dropDownListItem);
							dropDownListItem.addClickHandler(new OnDropdownItemClick(number+"",dropDownListItem.getElement().getId(),CollectionItemId));
						}
				}
				});
				}
			}
	}
	private class OnDropdownListPlaceHolderClick implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			new CustomAnimation(dropdownListContainerScrollPanel).run(300);
		}
	}
	private class OnDropdownListAssignmentPlaceHolderClick implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			new CustomAnimation(dropdownListContainerScrollPanelAssignment).run(300);
		}
	}
	
	private class OnDropdownItemClick implements ClickHandler{
		private String seq="";
		private String itemCount;
		private String UnitCollectionItemId;
	
		public OnDropdownItemClick(String seq,String itemCount,String UnitCollectionItemId){
			this.seq = seq;
			this.itemCount = itemCount;
			this.UnitCollectionItemId = UnitCollectionItemId;
		}
		@Override
		public void onClick(ClickEvent event) {
			dropdownListPlaceHolder.setText(seq);
			dropdownListPlaceHolder.getElement().setId(UnitCollectionItemId);
			dropdownListPlaceHolder.getElement().setAttribute("id", itemCount+"");
		
			selectedPathId = UnitCollectionItemId;
			displayAssignment(Integer.parseInt(itemCount));
			new CustomAnimation(dropdownListContainerScrollPanel).run(300);
			
			
		}
	}
	private class OnDropdownAssignmentItemClick implements ClickHandler{
		private String seq="";
			public OnDropdownAssignmentItemClick(String seq){
			this.seq=seq;
			
		}
		@Override
		public void onClick(ClickEvent event) {
			dropdownListPlaceHolderAssignment.setText(seq);
			new CustomAnimation(dropdownListContainerScrollPanelAssignment).run(300);
		}
	}
		
	
	@UiHandler("CancelButton")
	public void onclickCancelBtn(ClickEvent event){
		this.hide();
	}
	
	public class clickOnSave implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			CancelButton.setVisible(false);
			saveButton.setVisible(false);
			savingTextLabel.setText("Saving...");
			AppClientFactory.getInjector().getClasspageService().v2ReorderPathwaySequence(classpageId,selectedPathId,Integer.parseInt(dropdownListPlaceHolderAssignment.getText()),new SimpleAsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
					CancelButton.setVisible(true);
					saveButton.setVisible(true);
					hide();
					savingTextLabel.setText("");
					
				}
		});	
			
		}
	}
	
}