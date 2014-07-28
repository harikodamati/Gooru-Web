package org.ednovo.gooru.client.mvp.landingpage;

import org.ednovo.gooru.client.AppPlaceKeeper;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.home.register.UserRegistrationPresenter;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class LandingPagePresenter extends BasePlacePresenter<IsLandingPageView, LandingPagePresenter.IsLandingPageProxy> implements LandingPageUiHandlers {

	private static final String LIBRARY_PAGE = "page";
	
	private int rusdLoader = 1;
	
	private static final String CALLBACK = "callback";
	
	SignUpPresenter signUpViewPresenter = null;
	
	@ProxyCodeSplit
	@NameToken(PlaceTokens.COMMUNITY)
	@UseGatekeeper(AppPlaceKeeper.class)
	public interface IsLandingPageProxy extends ProxyPlace<LandingPagePresenter> {
	}

	/**
	 * Class constructor
	 * @param userRegistrationPresenter instance of {@link UserRegistrationPresenter}
	 * @param view {@link View}
	 * @param proxy {@link Proxy}
	 */
	@Inject
	public LandingPagePresenter(IsLandingPageView view, IsLandingPageProxy proxy, SignUpPresenter signUpViewPresenter) {
		super(view, proxy);
		getView().setUiHandlers(this);
		this.signUpViewPresenter = signUpViewPresenter;
	}
	
	@Override
	public void onBind() {
		super.onBind();
	}
	
	@Override
	public void onReveal() {
		super.onReveal();
	}
	
	@Override
	public void onReset() {
		super.onReset();
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		if(AppClientFactory.getLoggedInUser()!=null) {
			getIntoLibrarypage();
		} else {
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.ERROR);
		}
		
		if (getPlaceManager().getRequestParameter(CALLBACK) != null && getPlaceManager().getRequestParameter(CALLBACK).equalsIgnoreCase("signup")) {
			//To show SignUp (Registration popup)
			if (AppClientFactory.isAnonymous()){
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
				String type = getPlaceManager().getRequestParameter("type") ;
				int displayScreen =getPlaceManager().getRequestParameter("type") !=null  ? Integer.parseInt(type) : 1;
				signUpViewPresenter.displayPopup(displayScreen);
				addToPopupSlot(signUpViewPresenter);
			}
		}
	}
	
	/**
	 * 
	 * @function getIntoLibrarypage 
	 * 
	 * @created_date : 26-Dec-2013
	 * 
	 * @description
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	private void getIntoLibrarypage() {
		if (getPlaceManager().getRequestParameter(LIBRARY_PAGE) != null && getPlaceManager().getRequestParameter(LIBRARY_PAGE).equalsIgnoreCase("featured-contributors")) {
			getView().loadFeaturedContributors("featured-contributors",getViewToken());
		} else if (getPlaceManager().getRequestParameter(LIBRARY_PAGE) != null && getPlaceManager().getRequestParameter(LIBRARY_PAGE).equalsIgnoreCase("course-page")) {
			getView().loadFeaturedContributors("course-page",getViewToken());
		} else if (getPlaceManager().getRequestParameter(LIBRARY_PAGE) != null && getPlaceManager().getRequestParameter(LIBRARY_PAGE).equalsIgnoreCase("featured-course")) {
			getView().loadFeaturedContributors("featured-course",getViewToken());
		} else if (getPlaceManager().getRequestParameter(LIBRARY_PAGE) == null) {
			getView().loadFeaturedContributors("featured-course",getViewToken());
		}
	}

	@Override
	public String getViewToken() {
		return PlaceTokens.COMMUNITY;
	}

}