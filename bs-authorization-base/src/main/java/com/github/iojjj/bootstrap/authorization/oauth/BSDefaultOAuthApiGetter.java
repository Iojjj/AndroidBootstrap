package com.github.iojjj.bootstrap.authorization.oauth;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.scribejava.apis.AWeberApi;
import com.github.scribejava.apis.DiggApi;
import com.github.scribejava.apis.DoktornaraboteApi;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.apis.Foursquare2Api;
import com.github.scribejava.apis.FoursquareApi;
import com.github.scribejava.apis.FreelancerApi;
import com.github.scribejava.apis.GeniusApi;
import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.apis.HHApi;
import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.apis.KaixinApi20;
import com.github.scribejava.apis.LinkedInApi;
import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.apis.LiveApi;
import com.github.scribejava.apis.MailruApi;
import com.github.scribejava.apis.MeetupApi;
import com.github.scribejava.apis.MisfitApi;
import com.github.scribejava.apis.NaverApi;
import com.github.scribejava.apis.NeteaseWeibooApi;
import com.github.scribejava.apis.OdnoklassnikiApi;
import com.github.scribejava.apis.PinterestApi;
import com.github.scribejava.apis.Px500Api;
import com.github.scribejava.apis.RenrenApi;
import com.github.scribejava.apis.SalesforceApi;
import com.github.scribejava.apis.SinaWeiboApi;
import com.github.scribejava.apis.SinaWeiboApi20;
import com.github.scribejava.apis.SkyrockApi;
import com.github.scribejava.apis.SohuWeiboApi;
import com.github.scribejava.apis.StackExchangeApi;
import com.github.scribejava.apis.TrelloApi;
import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.apis.TutByApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.apis.ViadeoApi;
import com.github.scribejava.apis.VkontakteApi;
import com.github.scribejava.apis.XingApi;
import com.github.scribejava.apis.YahooApi;
import com.github.scribejava.core.builder.api.BaseApi;

/**
 * Default implementation of {@link BSOAuthApiGetterAbstract}.
 *
 * @since 1.0
 */
public class BSDefaultOAuthApiGetter extends BSOAuthApiGetterAbstract {

    public static final Creator<BSDefaultOAuthApiGetter> CREATOR = new Creator<BSDefaultOAuthApiGetter>() {
        @Override
        public BSDefaultOAuthApiGetter createFromParcel(Parcel source) {
            return new BSDefaultOAuthApiGetter(source);
        }

        @Override
        public BSDefaultOAuthApiGetter[] newArray(int size) {
            return new BSDefaultOAuthApiGetter[size];
        }
    };
    @NonNull
    private final BSOAuthApi mApi;

    public BSDefaultOAuthApiGetter(@NonNull BSOAuthApi api) {
        BSAssertions.assertNotNull(api, "api");
        mApi = api;
    }


    private BSDefaultOAuthApiGetter(Parcel in) {
        mApi = BSOAuthApi.values()[in.readInt()];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mApi.ordinal());
    }

    @NonNull
    @Override
    BaseApi getApi() {
        final BaseApi api;
        switch (mApi) {
            case AWEBER:
                api = AWeberApi.instance();
                break;
            case DIGG:
                api = DiggApi.instance();
                break;
            case DOKTORNARABOTE:
                api = DoktornaraboteApi.instance();
                break;
            case FACEBOOK:
                api = FacebookApi.instance();
                break;
            case FLICKR:
                api = FlickrApi.instance();
                break;
            case FOURSQUARE2:
                api = Foursquare2Api.instance();
                break;
            case FOURSQUARE:
                api = FoursquareApi.instance();
                break;
            case FREELANCER:
                api = FreelancerApi.instance();
                break;
            case GENIUS:
                api = GeniusApi.instance();
                break;
            case GITHUB:
                api = GitHubApi.instance();
                break;
            case GOOGLE:
                api = GoogleApi20.instance();
                break;
            case HH:
                api = HHApi.instance();
                break;
            case IMGUR:
                api = ImgurApi.instance();
                break;
            case KAIXIN:
                api = KaixinApi20.instance();
                break;
            case LINKEDIN:
                api = LinkedInApi.instance();
                break;
            case LINKEDIN2:
                api = LinkedInApi20.instance();
                break;
            case LIVE:
                api = LiveApi.instance();
                break;
            case MAILRU:
                api = MailruApi.instance();
                break;
            case MEETUP:
                api = MeetupApi.instance();
                break;
            case MISFIT:
                api = MisfitApi.instance();
                break;
            case NAVER:
                api = NaverApi.instance();
                break;
            case NETEASE_WEIBOO:
                api = NeteaseWeibooApi.instance();
                break;
            case ODNOKLASSNIKI:
                api = OdnoklassnikiApi.instance();
                break;
            case PINTEREST:
                api = PinterestApi.instance();
                break;
            case PX500:
                api = Px500Api.instance();
                break;
            case RENREN:
                api = RenrenApi.instance();
                break;
            case SALESFORCE:
                api = SalesforceApi.instance();
                break;
            case SINA_WEIBO:
                api = SinaWeiboApi.instance();
                break;
            case SINA_WEIBO2:
                api = SinaWeiboApi20.instance();
                break;
            case SKYROCK:
                api = SkyrockApi.instance();
                break;
            case SOHU_WEIBO:
                api = SohuWeiboApi.instance();
                break;
            case STACK_EXCHANGE:
                api = StackExchangeApi.instance();
                break;
            case TRELLO:
                api = TrelloApi.instance();
                break;
            case TUMBLR:
                api = TumblrApi.instance();
                break;
            case TUTBY:
                api = TutByApi.instance();
                break;
            case TWITTER:
                api = TwitterApi.instance();
                break;
            case VIADEO:
                api = ViadeoApi.instance();
                break;
            case VKONTAKTE:
                api = VkontakteApi.instance();
                break;
            case XING:
                api = XingApi.instance();
                break;
            case YAHOO:
                api = YahooApi.instance();
                break;
            default:
                throw new IllegalArgumentException("Unknown Api type: " + mApi);
        }
        return api;
    }

}
