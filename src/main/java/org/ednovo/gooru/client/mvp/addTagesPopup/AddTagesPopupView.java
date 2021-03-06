package org.ednovo.gooru.client.mvp.addTagesPopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.effects.FadeInAndOut;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionCBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.assign.CollectionAssignCBundle;
import org.ednovo.gooru.client.uc.AppMultiWordSuggestOracle;
import org.ednovo.gooru.client.uc.AppSuggestBox;
import org.ednovo.gooru.client.uc.CloseLabel;
import org.ednovo.gooru.client.uc.DownToolTipWidgetUc;
import org.ednovo.gooru.client.uc.StandardsPreferenceOrganizeToolTip;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.content.ResourceTagsDo;
import org.ednovo.gooru.shared.model.search.SearchDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

public abstract class AddTagesPopupView extends PopupPanel implements SelectionHandler<SuggestOracle.Suggestion>{

	public PopupPanel appPopUp;
	
	private static AddTagesPopupViewUiBinder uiBinder = GWT
			.create(AddTagesPopupViewUiBinder.class);

	interface AddTagesPopupViewUiBinder extends
			UiBinder<Widget, AddTagesPopupView> {
	}
	public MessageProperties i18n = GWT.create(MessageProperties.class);

	@UiField(provided = true)
	AddTagesCBundle res;
	
	@UiField Label lexileHeader,AdsHeader, kindergarden, level1, level2, level3, level4, level5, level6, level7, level8, level9, level10, level11, level12;
	
	@UiField Label headerEducationalUse, handout, homework, game, presentation, refMaterial, quiz, currPlan, lessonPlan, unitPlan, projectPlan, reading, textbook, article, book, activity;
	
	@UiField Button cancelBtn,addTagsBtn,mobileYes,mobileNo;
	
	@UiField HTMLPanel htmlMediaFeatureListContainer;
	
	@UiField ScrollPanel spanelMediaFeaturePanel;
	
	@UiField Label mediaLabel,lblMediaPlaceHolder,lblMediaFeatureArrow;
	
	@UiField Label noAds,modAds,aggreAds,standardMaxMsg,standardsDefaultText;
	
	@UiField Label accessHazard,flashingHazard,motionSimulationHazard,soundHazard;
	
	@UiField InlineLabel addTagesTitle,popupContentText,moblieFriendly;
	
	List<String> tagListGlobal = new ArrayList<String>();
	
	@UiField(provided = true)
	AppSuggestBox standardSgstBox;
	
	@UiField FlowPanel standardContainer,standardsPanel;
	private AppMultiWordSuggestOracle standardSuggestOracle;
	private SearchDo<CodeDo> standardSearchDo = new SearchDo<CodeDo>();
	private static final String FLT_CODE_ID = "id";
	List<String> standardPreflist;
	private Map<String, String> standardCodesMap = new HashMap<String, String>();
	String courseCode="";
	boolean isEditResource=true;
	ArrayList<String> standardsDo=new ArrayList<String>();
	Set<CodeDo> deletedStandardsDo=new HashSet<CodeDo>();
	final StandardsPreferenceOrganizeToolTip standardsPreferenceOrganizeToolTip=new StandardsPreferenceOrganizeToolTip();
	private static final String USER_META_ACTIVE_FLAG = "0";

	boolean isCancelclicked=false;
	private boolean isClickedOnDropDwn=false;
	String mediaFeatureStr = i18n.GL1767();
	String resourceId=null;


	public AddTagesPopupView(String resourceId) {
		super(false);

		initializeAutoSuggestedBox();
		this.res = AddTagesCBundle.INSTANCE;
		res.css().ensureInjected();
		add(uiBinder.createAndBindUi(this));
		this.resourceId=resourceId;
		this.setGlassEnabled(true);
		if(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().contains("resource-search")||AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().contains("collection-search")||AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().contains("mycollections")){
				this.getGlassElement().addClassName(AddTagesCBundle.INSTANCE.css().tagsStyleSearch());
			}else{
			
				this.removeStyleName(AddTagesCBundle.INSTANCE.css().tagsStyleSearch());
				this.getGlassElement().setAttribute("style", "z-index:99999; position:absolute; left:0px; top:0px;");
				this.getElement().setAttribute("style", "z-index:999999;");
		}
		this.setWidth("596px");
		this.setHeight("586px");
		//AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99999, false));
		this.center();
		
		
		standardsPanel.getElement().setId("pnlStandards");
		standardsPanel.getElement().setAttribute("alt","");
		standardsPanel.getElement().setAttribute("title","");
		
		standardMaxMsg.getElement().setId("lblStandardMaxMsg");
		standardMaxMsg.getElement().setAttribute("alt","");
		standardMaxMsg.getElement().setAttribute("title","");
		
		addTagesTitle.setText(i18n.GL1795());
		addTagesTitle.getElement().setId("spnAddTagesTitle");
		addTagesTitle.getElement().setAttribute("alt",i18n.GL1795());
		addTagesTitle.getElement().setAttribute("title",i18n.GL1795());
		
		popupContentText.setText(i18n.GL1812());
		popupContentText.getElement().setId("spnpopupContentText");
		popupContentText.getElement().setAttribute("alt",i18n.GL1812());
		popupContentText.getElement().setAttribute("title",i18n.GL1812());
		
		
		headerEducationalUse.setText(i18n.GL1664());
		headerEducationalUse.getElement().setId("lblHeaderEducationalUse");
		headerEducationalUse.getElement().setAttribute("alt",i18n.GL1664());
		headerEducationalUse.getElement().setAttribute("title",i18n.GL1664());
		
	
		activity.setText(i18n.GL1665());
		activity.getElement().setId("lblActivity");
		activity.getElement().setAttribute("alt",i18n.GL1665());
		activity.getElement().setAttribute("title",i18n.GL1665());
		
		handout.setText(i18n.GL0907());
		handout.getElement().setId("lblHandout");
		handout.getElement().setAttribute("alt",i18n.GL0907());
		handout.getElement().setAttribute("title",i18n.GL0907());
		
		homework.setText(i18n.GL1666());
		homework.getElement().setId("lblHomework");
		homework.getElement().setAttribute("alt",i18n.GL1666());
		homework.getElement().setAttribute("title",i18n.GL1666());
		
		game.setText(i18n.GL1667());
		game.getElement().setId("lblGame");
		game.getElement().setAttribute("alt",i18n.GL1667());
		game.getElement().setAttribute("title",i18n.GL1667());
		
		presentation.setText(i18n.GL1668());
		presentation.getElement().setId("lblPresentation");
		presentation.getElement().setAttribute("alt",i18n.GL1668());
		presentation.getElement().setAttribute("title",i18n.GL1668());
		
		refMaterial.setText(i18n.GL1669());
		refMaterial.getElement().setId("lblRefMaterial");
		refMaterial.getElement().setAttribute("alt",i18n.GL1669());
		refMaterial.getElement().setAttribute("title",i18n.GL1669());
		
		quiz.setText(i18n.GL1670());
		quiz.getElement().setId("lblQuiz");
		quiz.getElement().setAttribute("alt",i18n.GL1670());
		quiz.getElement().setAttribute("title",i18n.GL1670());
		
		currPlan.setText(i18n.GL1671());
		currPlan.getElement().setId("lblCurrPlan");
		currPlan.getElement().setAttribute("alt",i18n.GL1671());
		currPlan.getElement().setAttribute("title",i18n.GL1671());
		
		lessonPlan.setText(i18n.GL1672());
		lessonPlan.getElement().setId("lblLessonPlan");
		lessonPlan.getElement().setAttribute("alt",i18n.GL1672());
		lessonPlan.getElement().setAttribute("title",i18n.GL1672());
		
		unitPlan.setText(i18n.GL1673());
		unitPlan.getElement().setId("lblUnitPlan");
		unitPlan.getElement().setAttribute("alt",i18n.GL1673());
		unitPlan.getElement().setAttribute("title",i18n.GL1673());
		
		projectPlan.setText(i18n.GL1674());
		projectPlan.getElement().setId("lblProjectPlan");
		projectPlan.getElement().setAttribute("alt",i18n.GL1674());
		projectPlan.getElement().setAttribute("title",i18n.GL1674());
		
		reading.setText(i18n.GL1675());
		reading.getElement().setId("lblReading");
		reading.getElement().setAttribute("alt",i18n.GL1675());
		reading.getElement().setAttribute("title",i18n.GL1675());
		
		textbook.setText(i18n.GL0909());
		textbook.getElement().setId("lblTextbook");
		textbook.getElement().setAttribute("alt",i18n.GL0909());
		textbook.getElement().setAttribute("title",i18n.GL0909());
		
		article.setText(i18n.GL1676());
		article.getElement().setId("lblArticle");
		article.getElement().setAttribute("alt",i18n.GL1676());
		article.getElement().setAttribute("title",i18n.GL1676());
		
		book.setText(i18n.GL1677());
		book.getElement().setId("lblBook");
		book.getElement().setAttribute("alt",i18n.GL1677());
		book.getElement().setAttribute("title",i18n.GL1677());
		
		lexileHeader.setText(i18n.GL1798());
		lexileHeader.getElement().setId("lblReadingLevel");
		lexileHeader.getElement().setAttribute("alt",i18n.GL1798());
		lexileHeader.getElement().setAttribute("title",i18n.GL1798());
		
		kindergarden.setText(i18n.GL1799());
		kindergarden.getElement().setId("lblKindergarten");
		kindergarden.getElement().setAttribute("alt",i18n.GL1799());
		kindergarden.getElement().setAttribute("title",i18n.GL1799());
		
		moblieFriendly.setText(i18n.GL1811());
		moblieFriendly.getElement().setId("spnMobileFriendly");
		moblieFriendly.getElement().setAttribute("alt",i18n.GL1811());
		moblieFriendly.getElement().setAttribute("title",i18n.GL1811());
		
		level1.setText(i18n.GL_GRR_NUMERIC_ONE());
		level1.getElement().setId("lblOne");
		level1.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_ONE());
		level1.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_ONE());
		
		level2.setText(i18n.GL_GRR_NUMERIC_TWO());
		level2.getElement().setId("lblTwo");
		level2.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_TWO());
		level2.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_TWO());
		
		level3.setText(i18n.GL_GRR_NUMERIC_THREE());
		level3.getElement().setId("lblThree");
		level3.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_THREE());
		level3.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_THREE());
		
		level4.setText(i18n.GL_GRR_NUMERIC_FOUR());
		level4.getElement().setId("lblFour");
		level4.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_FOUR());
		level4.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_FOUR());
		
		level5.setText(i18n.GL_GRR_NUMERIC_FIVE());
		level5.getElement().setId("lblFive");
		level5.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_FIVE());
		level5.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_FIVE());
		
		level6.setText(i18n.GL_GRR_NUMERIC_SIX());
		level6.getElement().setId("lblSix");
		level6.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_SIX());
		level6.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_SIX());
		
		level7.setText(i18n.GL_GRR_NUMERIC_SEVEN());
		level7.getElement().setId("lblSeven");
		level7.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_SEVEN());
		level7.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_SEVEN());
		
		level8.setText(i18n.GL_GRR_NUMERIC_EIGHT());
		level8.getElement().setId("lblEight");
		level8.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_EIGHT());
		level8.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_EIGHT());
		
		level9.setText(i18n.GL_GRR_NUMERIC_NINE());
		level9.getElement().setId("lblNine");
		level9.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_NINE());
		level9.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_NINE());
		
		level10.setText(i18n.GL_GRR_NUMERIC_TEN());
		level10.getElement().setId("lblNine");
		level10.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_TEN());
		level10.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_TEN());
		
		level11.setText(i18n.GL_GRR_NUMERIC_ELEVEN());
		level11.getElement().setId("lblEleven");
		level11.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_ELEVEN());
		level11.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_ELEVEN());
		
		level12.setText(i18n.GL_GRR_NUMERIC_TWELVE());
		level12.getElement().setId("lblTwelve");
		level12.getElement().setAttribute("alt",i18n.GL_GRR_NUMERIC_TWELVE());
		level12.getElement().setAttribute("title",i18n.GL_GRR_NUMERIC_TWELVE());
		
		AdsHeader.setText(i18n.GL1800());
		AdsHeader.getElement().setId("lblAds");
		AdsHeader.getElement().setAttribute("alt",i18n.GL1800());
		AdsHeader.getElement().setAttribute("title",i18n.GL1800());
		
		noAds.setText(i18n.GL1801());
		noAds.getElement().setId("lblNoAdvertisement");
		noAds.getElement().setAttribute("alt",i18n.GL1801());
		noAds.getElement().setAttribute("title",i18n.GL1801());
		
		modAds.setText(i18n.GL1802());
		modAds.getElement().setId("lblModerateAdvertisements");
		modAds.getElement().setAttribute("alt",i18n.GL1802());
		modAds.getElement().setAttribute("title",i18n.GL1802());
		
		aggreAds.setText(i18n.GL1803());
		aggreAds.getElement().setId("lblAggressiveAdvertisements");
		aggreAds.getElement().setAttribute("alt",i18n.GL1803());
		aggreAds.getElement().setAttribute("title",i18n.GL1803());
		
		accessHazard.setText(i18n.GL1804());
		accessHazard.getElement().setId("lblAccessHazard");
		accessHazard.getElement().setAttribute("alt",i18n.GL1804());
		accessHazard.getElement().setAttribute("title",i18n.GL1804());
		
		flashingHazard.setText(i18n.GL1806());
		flashingHazard.getElement().setId("lblFlashingHazard");
		flashingHazard.getElement().setAttribute("alt",i18n.GL1806());
		flashingHazard.getElement().setAttribute("title",i18n.GL1806());
		
		motionSimulationHazard.setText(i18n.GL1808());
		motionSimulationHazard.getElement().setId("lblMotionSimulationHazard");
		motionSimulationHazard.getElement().setAttribute("alt",i18n.GL1808());
		motionSimulationHazard.getElement().setAttribute("title",i18n.GL1808());
		
		soundHazard.setText(i18n.GL1810());
		soundHazard.getElement().setId("lblSoundHazard");
		soundHazard.getElement().setAttribute("alt",i18n.GL1810());
		soundHazard.getElement().setAttribute("title",i18n.GL1810());
		
		cancelBtn.setText(i18n.GL0142());
		cancelBtn.getElement().setId("btnCancel");
		cancelBtn.getElement().setAttribute("alt",i18n.GL0142());
		cancelBtn.getElement().setAttribute("title",i18n.GL0142());
		
		addTagsBtn.setText(i18n.GL1795());
		addTagsBtn.getElement().setId("btnAddTags");
		addTagsBtn.getElement().setAttribute("alt",i18n.GL1795());
		addTagsBtn.getElement().setAttribute("title",i18n.GL1795());
		
		mobileYes.setText(i18n.GL_GRR_YES());
		mobileYes.getElement().setId("btnYes");
		mobileYes.getElement().setAttribute("alt",i18n.GL_GRR_YES());
		mobileYes.getElement().setAttribute("title",i18n.GL_GRR_YES());
		
		mobileNo.setText(i18n.GL1735());
		mobileNo.getElement().setId("btnNo");
		mobileNo.getElement().setAttribute("alt",i18n.GL1735());
		mobileNo.getElement().setAttribute("title",i18n.GL1735());
		
		
		standardsDefaultText.setText(i18n.GL1682());
		standardsDefaultText.getElement().setId("lblStandards");
		standardsDefaultText.getElement().setAttribute("alt",i18n.GL1682());
		standardsDefaultText.getElement().setAttribute("title",i18n.GL1682());
		
		CollectionAssignCBundle.INSTANCE.css().ensureInjected();
		spanelMediaFeaturePanel.setVisible(false);
		
		mediaLabel.setText("Media Feature");
		mediaLabel.getElement().setId("lblMediaFeature");
		mediaLabel.getElement().setAttribute("alt","Media Feature");
		mediaLabel.getElement().setAttribute("title","Media Feature");
		
		lblMediaPlaceHolder.setText("Choose a Media Feature Option:");
		lblMediaPlaceHolder.getElement().setId("phMediaFeature");
		lblMediaPlaceHolder.getElement().setAttribute("alt","Choose a Media Feature Option:");
		lblMediaPlaceHolder.getElement().setAttribute("title","Choose a Media Feature Option:");
		
		lblMediaFeatureArrow.getElement().setId("lblMediaFeatureArrow");
		lblMediaFeatureArrow.getElement().setAttribute("alt","");
		lblMediaFeatureArrow.getElement().setAttribute("title","");
		
		spanelMediaFeaturePanel.getElement().setId("sbMediaFeaturesPanel");
		spanelMediaFeaturePanel.getElement().setAttribute("alt","");
		spanelMediaFeaturePanel.getElement().setAttribute("title","");
		
		htmlMediaFeatureListContainer.getElement().setId("pnlMediaFeaturesList");
		htmlMediaFeatureListContainer.getElement().setAttribute("alt","");
		htmlMediaFeatureListContainer.getElement().setAttribute("title","");
		
		lblMediaFeatureArrow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				OpenMediaFeatureDropdown();
			}
		});
		lblMediaPlaceHolder.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				OpenMediaFeatureDropdown();
			}
		});
		getTagsServiceRequest(resourceId);

		
		List<String> mediaFeatureList = Arrays.asList(mediaFeatureStr.split(","));
		for(int n=0; n<mediaFeatureList.size(); n++)
		{
				String mediaTitleVal = mediaFeatureList.get(n);
				
				final Label titleLabel = new Label(mediaTitleVal);
				titleLabel.setStyleName(CollectionAssignCBundle.INSTANCE.css().classpageTitleText());
				titleLabel.getElement().setAttribute("id", mediaTitleVal);
				//Set Click event for title
				titleLabel.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {		
						String optionSelected = titleLabel.getElement().getId();
						lblMediaPlaceHolder.setText(optionSelected);
						spanelMediaFeaturePanel.setVisible(false);
						lblMediaPlaceHolder.getElement().setId(titleLabel.getElement().getId());
						lblMediaPlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().selectedClasspageText());
						lblMediaPlaceHolder.setText(optionSelected);
					}
				});
				htmlMediaFeatureListContainer.add(titleLabel);
		}
		AppClientFactory.getInjector().getUserService().getUserProfileV2Details(AppClientFactory.getGooruUid(),USER_META_ACTIVE_FLAG,new SimpleAsyncCallback<ProfileDo>() {
			@Override
			public void onSuccess(ProfileDo profileObj) {
			if(profileObj.getUser().getMeta().getTaxonomyPreference().getCodeId()!=null){
					if(profileObj.getUser().getMeta().getTaxonomyPreference().getCodeId().size()==0){
						standardContainer.setVisible(false);
					}else
					{
						standardContainer.setVisible(true);
						standardPreflist=new ArrayList<String>();
						for (String code : profileObj.getUser().getMeta().getTaxonomyPreference().getCode()) {
							standardPreflist.add(code);
							standardPreflist.add(code.substring(0, 2));
						 }
					}
				}else{
					standardContainer.setVisible(false);
				}
			}
		});
		
		ClickHandler tagHandler= new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!isClickedOnDropDwn){
					spanelMediaFeaturePanel.setVisible(false);
				}else{
					isClickedOnDropDwn=false;
				}
				
			}
		};
		RootPanel.get().addDomHandler(tagHandler, ClickEvent.getType());
	}
	public void initializeAutoSuggestedBox(){
		standardSuggestOracle = new AppMultiWordSuggestOracle(true);
		standardSearchDo.setPageSize(10);
		standardSgstBox = new AppSuggestBox(standardSuggestOracle) {
			@Override
			public void keyAction(String text) {
				text=text.toUpperCase();
				standardsPreferenceOrganizeToolTip.hide();
				standardSearchDo.setSearchResults(null);
				boolean standardsPrefDisplayPopup = false;
				standardSgstBox.hideSuggestionList();
				if(!courseCode.isEmpty()) {
					Map<String,String> filters = new HashMap<String, String>();
					filters.put(FLT_CODE_ID,courseCode);
					standardSearchDo.setFilters(filters);
				}
				standardSearchDo.setQuery(text);
				if (text != null && text.trim().length() > 0) {
					standardsPreferenceOrganizeToolTip.hide();
					if(standardPreflist!=null){
						for(int count=0; count<standardPreflist.size();count++) {
							if(text.contains(standardPreflist.get(count))) {
								standardsPrefDisplayPopup = true;
								break;
							} else {
								standardsPrefDisplayPopup = false;
							}
						}						
					}
					if(standardsPrefDisplayPopup){
						standardsPreferenceOrganizeToolTip.hide();
						AppClientFactory.getInjector().getSearchService().getSuggestStandardByFilterCourseId(standardSearchDo, new SimpleAsyncCallback<SearchDo<CodeDo>>() {
							@Override
							public void onSuccess(SearchDo<CodeDo> result) {
								setStandardSuggestions(result);
							}
						});
						
						standardSgstBox.showSuggestionList();
						}
					else{
						standardSgstBox.hideSuggestionList();
						standardSuggestOracle.clear();
						standardsPreferenceOrganizeToolTip.show();
						standardsPreferenceOrganizeToolTip.setPopupPosition(standardSgstBox.getAbsoluteLeft()+3, standardSgstBox.getAbsoluteTop()+33);
						standardsPreferenceOrganizeToolTip.getElement().getStyle().setZIndex(1111);
						//standardSuggestOracle.add(i18n.GL1613);
					}
					}
			}
			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return null;
			}
		};
		standardSgstBox.getElement().setId("tbautoStandardSgstBox");
		standardSgstBox.getElement().setAttribute("alt","");
		standardSgstBox.getElement().setAttribute("title","");
		
		standardSgstBox.addSelectionHandler(this);
		BlurHandler blurHandler=new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(standardsPreferenceOrganizeToolTip.isShowing()){
				standardsPreferenceOrganizeToolTip.hide();
				}
			}
		};
		standardSgstBox.addDomHandler(blurHandler, BlurEvent.getType());
	}
	public void setStandardSuggestions(SearchDo<CodeDo> standardSearchDo) {
		standardSuggestOracle.clear();
		this.standardSearchDo = standardSearchDo;
		if (this.standardSearchDo.getSearchResults() != null) {
			List<String> sources = getAddedStandards(standardsPanel);
			for (CodeDo code : standardSearchDo.getSearchResults()) {
				if (!sources.contains(code.getCode())) {
					standardSuggestOracle.add(code.getCode());
				}
				standardCodesMap.put(code.getCodeId() + "", code.getLabel());
			}
		}
		standardSgstBox.showSuggestionList();
	}
	/**
	 * get the standards are added for collection
	 * 
	 * @param flowPanel
	 *            having all added standards label
	 * @return standards text in list which are added for the collection
	 */
	private List<String> getAddedStandards(FlowPanel flowPanel) {
		List<String> suggestions = new ArrayList<String>();
		for (Widget widget : flowPanel) {
			if (widget instanceof DownToolTipWidgetUc) {
				suggestions.add(((CloseLabel) ((DownToolTipWidgetUc) widget).getWidget()).getSourceText());
			}
		}
		return suggestions;
	}
	@Override
	public void onSelection(SelectionEvent<Suggestion> event) {
		addStandard(standardSgstBox.getValue(), getCodeIdByCode(standardSgstBox.getValue(), standardSearchDo.getSearchResults()));
		standardSgstBox.setText("");
		standardSuggestOracle.clear();
	}
	/**
	 * Adding new standard for the collection , will check it has more than
	 * fifteen standards
	 * 
	 * @param standard
	 *            which to be added for the collection
	 */
	public void addStandard(String standard, String id) {
		if (standardsPanel.getWidgetCount() <5) {
			if (standard != null && !standard.isEmpty()) {
				standardsDo.add(standard);
				standardsPanel.add(createStandardLabel(standard, id, standardCodesMap.get(id)));
			}
		} else {
			standardMaxShow();
			standardSgstBox.setText("");
		}
	}
	public void standardMaxShow() {
		standardSgstBox.addStyleName(CollectionCBundle.INSTANCE.css().standardTxtBox());
		standardMaxMsg.setStyleName(CollectionCBundle.INSTANCE.css().standardMax());
		standardsPanel.addStyleName(CollectionCBundle.INSTANCE.css().floatLeftNeeded());
		new FadeInAndOut(standardMaxMsg.getElement(), 5000, 5000);
	}
	/**
	 * new label is created for the standard which needs to be added
	 * 
	 * @param standardCode
	 *            update standard code
	 * @return instance of {@link DownToolTipWidgetUc}
	 */
	public DownToolTipWidgetUc createStandardLabel(final String standardCode, final String id, String description) {
		CloseLabel closeLabel = new CloseLabel(standardCode) {

			@Override
			public void onCloseLabelClick(ClickEvent event) {
				for(int i=0;i<standardsDo.size();i++){
					if(standardsDo.get(i).toString().equalsIgnoreCase(standardCode)){
						standardsDo.remove(standardsDo.get(i).toString());
					}
				}
				this.getParent().removeFromParent();
			}
		};
		return new DownToolTipWidgetUc(closeLabel, description);
	}
	private static String getCodeIdByCode(String code, List<CodeDo> codes) {
		if (codes != null) {
			for (CodeDo codeDo : codes) {
				if (code.equals(codeDo.getCode())) {
					return codeDo.getCodeId() + "";
				}
			}
		}
		return null;
	}
	@UiHandler("kindergarden")
	public void onKindergardenClick(ClickEvent click){
		if(kindergarden.getStyleName().toString().contains("selected"))
		{
			kindergarden.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			kindergarden.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level1")
	public void onLevel1Click(ClickEvent click){
		if(level1.getStyleName().toString().contains("selected"))
		{
			level1.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level1.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level2")
	public void onLevel2Click(ClickEvent click){
		if(level2.getStyleName().toString().contains("selected"))
		{
			level2.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level2.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level3")
	public void onLevel3Click(ClickEvent click){
		if(level3.getStyleName().toString().contains("selected"))
		{
			level3.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level3.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level4")
	public void onLevel4Click(ClickEvent click){
		if(level4.getStyleName().toString().contains("selected"))
		{
			level4.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level4.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level5")
	public void onLevel5Click(ClickEvent click){
		if(level5.getStyleName().toString().contains("selected"))
		{
			level5.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level5.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level6")
	public void onLevel6Click(ClickEvent click){
		if(level6.getStyleName().toString().contains("selected"))
		{
			level6.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level6.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level7")
	public void onLevel7Click(ClickEvent click){
		if(level7.getStyleName().toString().contains("selected"))
		{
			level7.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level7.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level8")
	public void onLevel8Click(ClickEvent click){
		if(level8.getStyleName().toString().contains("selected"))
		{
			level8.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level8.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level9")
	public void onLevel9Click(ClickEvent click){
		if(level9.getStyleName().toString().contains("selected"))
		{
			level9.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level9.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level10")
	public void onLevel10Click(ClickEvent click){
		if(level10.getStyleName().toString().contains("selected"))
		{
			level10.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level10.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level11")
	public void onLevel11Click(ClickEvent click){
		if(level11.getStyleName().toString().contains("selected"))
		{
			level11.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level11.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("level12")
	public void onLevel12Click(ClickEvent click){
		if(level12.getStyleName().toString().contains("selected"))
		{
			level12.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			level12.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	public void removeClassNameForAllEducationalUse(){
		 activity.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 handout.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 homework.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 game.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 presentation.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 refMaterial.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 quiz.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 currPlan.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 lessonPlan.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 unitPlan.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 projectPlan.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 reading.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());			 
		 textbook.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 article.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		 book.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
	}
	
	@UiHandler("activity")
	public void onactivityClick(ClickEvent click){
		
		if(activity.getStyleName().toString().contains("selected"))
		{
			activity.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			activity.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("handout")
	public void onhandoutClick(ClickEvent click){
		if(handout.getStyleName().toString().contains("selected"))
		{
			handout.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			handout.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("homework")
	public void onhomeworkClick(ClickEvent click){
		if(homework.getStyleName().toString().contains("selected"))
		{
			homework.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			homework.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("game")
	public void ongameClick(ClickEvent click){
		if(game.getStyleName().toString().contains("selected"))
		{
			game.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			game.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("presentation")
	public void onpresentationClick(ClickEvent click){
		if(presentation.getStyleName().toString().contains("selected"))
		{
			presentation.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			presentation.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("refMaterial")
	public void onrefMaterialClick(ClickEvent click){
		if(refMaterial.getStyleName().toString().contains("selected"))
		{
			refMaterial.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			refMaterial.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("quiz")
	public void onquizClick(ClickEvent click){
		if(quiz.getStyleName().toString().contains("selected"))
		{
			quiz.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			quiz.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("currPlan")
	public void oncurrPlanClick(ClickEvent click){
		if(currPlan.getStyleName().toString().contains("selected"))
		{
			currPlan.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			currPlan.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("lessonPlan")
	public void onlessonPlanClick(ClickEvent click){
		if(lessonPlan.getStyleName().toString().contains("selected"))
		{
			lessonPlan.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			lessonPlan.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("unitPlan")
	public void onunitPlanClick(ClickEvent click){
		if(unitPlan.getStyleName().toString().contains("selected"))
		{
			unitPlan.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			unitPlan.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("projectPlan")
	public void onprojectPlanClick(ClickEvent click){
		if(projectPlan.getStyleName().toString().contains("selected"))
		{
			projectPlan.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());

		}
		else
		{
			projectPlan.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("reading")
	public void onreadingClick(ClickEvent click){
		if(reading.getStyleName().toString().contains("selected"))
		{
			reading.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			reading.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("textbook")
	public void ontextbookClick(ClickEvent click){
		if(textbook.getStyleName().toString().contains("selected"))
		{
			textbook.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			textbook.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("article")
	public void onarticleClick(ClickEvent click){
		if(article.getStyleName().toString().contains("selected"))
		{
			article.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			article.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("book")
	public void onbookClick(ClickEvent click){
		if(book.getStyleName().toString().contains("selected"))
		{
			book.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			book.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	public void closeFunction()
	{
		isCancelclicked=false;
		closePoup(isCancelclicked);
	}
	
	@UiHandler("cancelBtn")
	public void onCancelClick(ClickEvent click)
	{
		isCancelclicked=true;
		closePoup(isCancelclicked);
	}
	
	private void OpenMediaFeatureDropdown() {
		isClickedOnDropDwn=true;
		if (spanelMediaFeaturePanel.isVisible()){
			spanelMediaFeaturePanel.setVisible(false);
		}else{
			spanelMediaFeaturePanel.setVisible(true);
		}
	}
	public void removeClassNameForAllAds(){
		noAds.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		modAds.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		aggreAds.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
	}
	@UiHandler("noAds")
	public void onnoAdsClick(ClickEvent click){
		if(noAds.getStyleName().toString().contains("selected"))
		{
			noAds.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(modAds.getStyleName().toString().contains("selected") || aggreAds.getStyleName().toString().contains("selected"))
		{
			removeClassNameForAllAds();
			noAds.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			noAds.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("modAds")
	public void onmodAdsClick(ClickEvent click){
		if(modAds.getStyleName().toString().contains("selected"))
		{
			modAds.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(noAds.getStyleName().toString().contains("selected") || aggreAds.getStyleName().toString().contains("selected"))
		{
			removeClassNameForAllAds();
			modAds.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());

		}
		else
		{
			modAds.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	@UiHandler("aggreAds")
	public void onaggreAdsClick(ClickEvent click){
		if(aggreAds.getStyleName().toString().contains("selected"))
		{
			aggreAds.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(noAds.getStyleName().toString().contains("selected") || modAds.getStyleName().toString().contains("selected"))
		{
			removeClassNameForAllAds();
			aggreAds.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else
		{
			aggreAds.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());		
		}
	}
	
	public void removeClassNamesForAllAccessHazard(){
		soundHazard.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().select());
		flashingHazard.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().select());
		motionSimulationHazard.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().select());
	}

	@UiHandler("flashingHazard")
	public void onflashingHazardClick(ClickEvent click){
		if(flashingHazard.getStyleName().toString().contains("select"))
		{
			flashingHazard.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().select());
		}
		/*else if(soundHazard.getStyleName().toString().contains("select") ||sound.getStyleName().toString().contains("select") ||motionSimulationHazard.getStyleName().toString().contains("select") || flashing.getStyleName().toString().contains("select") || motionSimulation.getStyleName().toString().contains("select"))
		{
			removeClassNamesForAllAccessHazard();
			flashingHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
		}*/
		else
		{
			flashingHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
		}
	}

	@UiHandler("motionSimulationHazard")
	public void onmotionSimulationHazardClick(ClickEvent click){
		if(motionSimulationHazard.getStyleName().toString().contains("select"))
		{
			motionSimulationHazard.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().select());
		}
		/*else if(soundHazard.getStyleName().toString().contains("select") ||sound.getStyleName().toString().contains("select") ||motionSimulation.getStyleName().toString().contains("select") || flashing.getStyleName().toString().contains("select") || flashingHazard.getStyleName().toString().contains("select"))
		{
			removeClassNamesForAllAccessHazard();
			motionSimulationHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
		}*/
		else
		{
			motionSimulationHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
		}
	}

	@UiHandler("soundHazard")
	public void onsoundHazardClick(ClickEvent click){
		if(soundHazard.getStyleName().toString().contains("select"))
		{
			soundHazard.getElement().removeClassName(AddTagesCBundle.INSTANCE.css().select());
		}
		/*else if(sound.getStyleName().toString().contains("select") ||motionSimulationHazard.getStyleName().toString().contains("select") ||motionSimulation.getStyleName().toString().contains("select") || flashing.getStyleName().toString().contains("select") || flashingHazard.getStyleName().toString().contains("select"))
		{
			removeClassNamesForAllAccessHazard();
			soundHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
		}*/
		else
		{
			soundHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
		}
	}
	
	@UiHandler("addTagsBtn")
	public void onaddTagsBtnClick(ClickEvent click)
	{
		String frameTagsStr = "";
		String adsStr = "";
		List<String> tagList = new ArrayList<String>();
		
		String[] educationArr = setEducationalUseString();
		if(educationArr != null)
		{
			for(int i=0;i<educationArr.length;i++)
			{
				tagList.add("\"" + educationArr[i].toString() +"\"");
			}
		}
		//System.out.println(tagList);
		String[] lexileMainarr = setLexileLevel();
		if(lexileMainarr != null)
		{
			for(int i=0;i<lexileMainarr.length;i++)
			{
				tagList.add("\"" + lexileMainarr[i].toString() +"\"");
			}
		}
		adsStr = setAdsString();
		
		if(!adsStr.isEmpty())
		{
			tagList.add("\"" + adsStr +"\"");
		}
		
		String hazardArr[] = setAccessHazards();
		for(int i=0;i<hazardArr.length;i++){
			
			System.out.println(hazardArr[i]);
		}
		if(hazardArr != null)
		{
			for(int i=0;i<hazardArr.length;i++)
			{
				tagList.add("\"" + hazardArr[i].toString() +"\"");
			}
		}
		
		for(final String codeObj:standardsDo){
			tagList.add("\"" +standardsDefaultText.getText()+" : "+codeObj+"\"");
		}
		
		if(!lblMediaPlaceHolder.getText().equalsIgnoreCase("Choose a Media Feature Option:"))
		{
			tagList.add("\"" +mediaLabel.getText()+" : "+lblMediaPlaceHolder.getText() +"\"");
		}
		
		if(mobileYes.getStyleName().contains(AddTagesCBundle.INSTANCE.css().OffButtonsActive()))
		{
			tagList.add("\"" +"Mobile Friendly"+" : "+mobileYes.getText() +"\"");
		}
		else if(mobileNo.getStyleName().contains(AddTagesCBundle.INSTANCE.css().OffButtonsActive()))
		{
			tagList.add("\"" +"Mobile Friendly"+" : "+mobileNo.getText() +"\"");
		}
		
		deleteTagsServiceRequest(tagListGlobal.toString(), tagList.toString());
	}
	
	public void addTagsServiceRequest(String frameTagsStr, String resourceId){
		
		AppClientFactory.getInjector().getResourceService().addTagsToResource(resourceId, frameTagsStr, new SimpleAsyncCallback<List<ResourceTagsDo>>() {
			@Override
			public void onSuccess(List<ResourceTagsDo> result) {
			//	bindObjectsToUI(result);
				closeFunction();
				getAddedResourceTags();
			}
		});
	
	}
	public void deleteTagsServiceRequest(String frameTagsStr, final String addingNewTags)
	{
		AppClientFactory.getInjector().getResourceService().deleteTagsServiceRequest(resourceId, frameTagsStr, new SimpleAsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				addTagsServiceRequest(addingNewTags.toString(), resourceId);
				//getAddedResourceTags();
			}
		});
	}
	public void getTagsServiceRequest(String resourceId)
	{
		AppClientFactory.getInjector().getResourceService().getTagsToResource(resourceId, new SimpleAsyncCallback<List<ResourceTagsDo>>() {
			@Override
			public void onSuccess(List<ResourceTagsDo> result) {
				bindObjectsToUI(result);
			}
		});
	}
	public void bindObjectsToUI(List<ResourceTagsDo> resultResourceTags)
	{
		tagListGlobal.clear();
		standardsDo.clear();
		for(int objVal=0;objVal<resultResourceTags.size();objVal++)
		{
			tagListGlobal.add("\""+resultResourceTags.get(objVal).getLabel()+"\"");
			if(resultResourceTags.get(objVal).getLabel().contains(headerEducationalUse.getText()))
			{
				setEducationalObjectVal(resultResourceTags.get(objVal).getLabel());
			}
			if(resultResourceTags.get(objVal).getLabel().contains(lexileHeader.getText()))
			{
				setLexileObjectVal(resultResourceTags.get(objVal).getLabel());
			}
			if(resultResourceTags.get(objVal).getLabel().contains(AdsHeader.getText()))
			{
				setAdsObjectVal(resultResourceTags.get(objVal).getLabel());
			}
			if(resultResourceTags.get(objVal).getLabel().contains(accessHazard.getText()))
			{
				setAccessHazardObjectVal(resultResourceTags.get(objVal).getLabel());
			}
			if(resultResourceTags.get(objVal).getLabel().contains(standardsDefaultText.getText()))
			{
				setStandardObjectVal(resultResourceTags.get(objVal).getLabel());
			}
			if(resultResourceTags.get(objVal).getLabel().contains(mediaLabel.getText()))
			{
				setMediaFeatureObjectVal(resultResourceTags.get(objVal).getLabel());
			}
			if(resultResourceTags.get(objVal).getLabel().contains("Mobile Friendly"))
			{
				setMobileFriendlyObjectVal(resultResourceTags.get(objVal).getLabel());
			}
		}
	}
	public void setMobileFriendlyObjectVal(String mobileFriendlyVal)
	{
		if(mobileFriendlyVal.contains(mobileYes.getText()))
		{
			mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
			mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
		}
		else if(mobileFriendlyVal.contains(mobileNo.getText()))
		{
			mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
			mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
		}
	}
	public void setMediaFeatureObjectVal(String mediaFeatureVal)
	{
		if(mediaFeatureVal != null)
		{
			mediaFeatureVal = mediaFeatureVal.replace(mediaLabel.getText()+" : ", "");
			lblMediaPlaceHolder.setText(mediaFeatureVal);
		}
	}
	public void setStandardObjectVal(String standardStr)
	{
		String[] standardArray=standardStr.split(":");
		//standardsDo.add(standardArray[1]);
		addStandard(standardArray[1], "0");
	}
	public void setAccessHazardObjectVal(String accessHazardStr)
	{
		String[] stringArry=accessHazardStr.split(":");
		if(stringArry.length!=0){
			if(stringArry[1].trim().equalsIgnoreCase(flashingHazard.getText()))
			{
					flashingHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
			}
			if(stringArry[1].trim().equalsIgnoreCase(motionSimulationHazard.getText()))
			{
				motionSimulationHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
			}
			if(stringArry[1].trim().equalsIgnoreCase(soundHazard.getText()))
			{
				soundHazard.getElement().addClassName(AddTagesCBundle.INSTANCE.css().select());
			}
		}
	}
	public void setAdsObjectVal(String adsStr)
	{
		if(adsStr.contains(noAds.getText()))
		{
			noAds.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(adsStr.contains(modAds.getText()))
		{
			modAds.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(adsStr.contains(aggreAds.getText()))
		{
			aggreAds.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	public void setLexileObjectVal(String lexileStr)
	{
		if(lexileStr.contains(kindergarden.getText()))
		{
			kindergarden.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(lexileStr.contains(level1.getText()))
		{
			String[] stringArry=lexileStr.split(":");
			if(stringArry[1].trim().equalsIgnoreCase(i18n.GL_GRR_NUMERIC_ONE())){
				level1.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
			}
		}
		if(lexileStr.contains(level2.getText()))
		{
			String[] stringArry=lexileStr.split(":");
			if(stringArry[1].trim().equalsIgnoreCase(i18n.GL_GRR_NUMERIC_TWO())){
				level2.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
			}
		}
		if(lexileStr.contains(level3.getText()))
		{
			level3.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(lexileStr.contains(level4.getText()))
		{
			level4.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(lexileStr.contains(level5.getText()))
		{
			level5.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(lexileStr.contains(level6.getText()))
		{
			level6.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(lexileStr.contains(level7.getText()))
		{
			level7.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(lexileStr.contains(level8.getText()))
		{
			level8.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(lexileStr.contains(level9.getText()))
		{
			level9.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(lexileStr.contains(level10.getText()))
		{
			level10.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(lexileStr.contains(level11.getText()))
		{
			level11.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		if(lexileStr.contains(level12.getText()))
		{
			level12.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	
	public void setEducationalObjectVal(String educationalStr)
	{
		if(educationalStr.contains(activity.getText()))
		{
			activity.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(handout.getText()))
		{
			handout.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());	
		}
		else if(educationalStr .contains(homework.getText()))
		{
			homework.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if( educationalStr.contains(game.getText()))
		{
			game.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(presentation.getText()))
		{
			presentation.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(refMaterial.getText()))
		{
			refMaterial.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(quiz.getText()))
		{
			quiz.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(currPlan.getText()))
		{
			currPlan.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(lessonPlan.getText()))
		{
			lessonPlan.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(unitPlan.getText()))
		{
			unitPlan.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(projectPlan.getText()))
		{
			projectPlan.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(reading.getText()))
		{
			reading.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(textbook.getText()))
		{
			textbook.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(article.getText()))
		{
			article.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
		else if(educationalStr.contains(book.getText()))
		{
			book.getElement().addClassName(AddTagesCBundle.INSTANCE.css().selected());
		}
	}
	
	public String setAdsString()
	{
		String adsStr = "";
		
		if(noAds.getElement().getClassName().contains("selected"))
		{
			adsStr = AdsHeader.getText()+" : "+noAds.getText();
		}
		else if(modAds.getElement().getClassName().contains("selected"))
		{
			adsStr = AdsHeader.getText()+" : "+modAds.getText();
		}
		else if(aggreAds.getElement().getClassName().contains("selected"))
		{
			adsStr = AdsHeader.getText()+" : "+aggreAds.getText();
		}
		
		return adsStr;
	}
	
	public String[] setAccessHazards()
	{
		String[] accessHazardsArr = null;
		List<String> accessHazardsSelected = new ArrayList<String>();
		
		if(flashingHazard.getElement().getClassName().contains("select"))
		{
			String hazardsStr = accessHazard.getText()+" : "+flashingHazard.getText();
			accessHazardsSelected.add(hazardsStr);
		}
		if(motionSimulationHazard.getElement().getClassName().contains("select"))
		{
			String hazardsStr = accessHazard.getText()+" : "+motionSimulationHazard.getText();
			accessHazardsSelected.add(hazardsStr);
		}
		if(soundHazard.getElement().getClassName().contains("select"))
		{
			String hazardsStr = accessHazard.getText()+" : "+soundHazard.getText();
			accessHazardsSelected.add(hazardsStr);
		}
		
		accessHazardsArr = accessHazardsSelected.toArray(new String[accessHazardsSelected.size()]);
		return accessHazardsArr;
	}
	
	public String[] setEducationalUseString()
	{
		String[] educationalUseArr = null;
		List<String> educationalUseSelected = new ArrayList<String>();
		if(activity.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + activity.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(handout.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + handout.getText();
			educationalUseSelected.add(educationalUse);
			
		}
		if(homework.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + homework.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(game.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + game.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(presentation.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + presentation.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(refMaterial.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + refMaterial.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(quiz.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + quiz.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(currPlan.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + currPlan.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(lessonPlan.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + lessonPlan.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(unitPlan.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + unitPlan.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(projectPlan.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + projectPlan.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(reading.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + reading.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(textbook.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + textbook.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(article.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + article.getText();
			educationalUseSelected.add(educationalUse);
		}
		if(book.getElement().getClassName().contains("selected"))
		{
			String educationalUse = headerEducationalUse.getText() + " : " + book.getText();
			educationalUseSelected.add(educationalUse);
		}
		educationalUseArr = educationalUseSelected.toArray(new String[educationalUseSelected.size()]);
		return educationalUseArr;
	}
	
	public String[] setLexileLevel()
	{
		String[] lexileLevelArr = null;
		List<String> lexileSelectedOptions = new ArrayList<String>();
		
		if(kindergarden.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + kindergarden.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level1.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level1.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level2.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level2.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level3.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level3.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level4.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level4.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level5.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level5.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level6.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level6.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level7.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level7.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level8.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level8.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level9.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level9.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level10.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level10.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level11.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level11.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		if(level12.getElement().getClassName().contains("selected"))
		{
			String lexileStr = lexileHeader.getText() + " : " + level12.getText();
			lexileSelectedOptions.add(lexileStr);
		}
		lexileLevelArr = lexileSelectedOptions.toArray(new String[lexileSelectedOptions.size()]);
		return lexileLevelArr;
		
	}
	
	@UiHandler("mobileYes")
	public void onmobileYesClick(ClickEvent click)
	{
		mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
		mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
	}
	
	@UiHandler("mobileNo")
	public void onmobileNoClick(ClickEvent click)
	{
		mobileNo.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OffButtonsActive());
		mobileYes.getElement().setClassName(AddTagesCBundle.INSTANCE.css().OnButtonDeActive());
	}
	
	
	
	@Override
	public Widget asWidget() {
		
		return null;
	}
	
	public abstract void closePoup(boolean isCancelclicked);
	
	public void getAddedResourceTags(){
		
	}
}
