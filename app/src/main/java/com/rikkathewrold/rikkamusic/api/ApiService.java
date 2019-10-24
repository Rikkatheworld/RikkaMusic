package com.rikkathewrold.rikkamusic.api;

import com.rikkathewrold.rikkamusic.dj.bean.DjCategoryRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjCatelistBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjDetailBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjPaygiftBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjProgramBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjRecommendTypeBean;
import com.rikkathewrold.rikkamusic.dj.bean.DjSubBean;
import com.rikkathewrold.rikkamusic.login.bean.LoginBean;
import com.rikkathewrold.rikkamusic.main.bean.AlbumSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.ArtistSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.BannerBean;
import com.rikkathewrold.rikkamusic.main.bean.CatlistBean;
import com.rikkathewrold.rikkamusic.main.bean.DailyRecommendBean;
import com.rikkathewrold.rikkamusic.main.bean.HighQualityPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.LikeListBean;
import com.rikkathewrold.rikkamusic.main.bean.LogoutBean;
import com.rikkathewrold.rikkamusic.main.bean.MainEventBean;
import com.rikkathewrold.rikkamusic.main.bean.MainRecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.MvSublistBean;
import com.rikkathewrold.rikkamusic.main.bean.MyFmBean;
import com.rikkathewrold.rikkamusic.main.bean.PlayModeIntelligenceBean;
import com.rikkathewrold.rikkamusic.main.bean.PlaylistDetailBean;
import com.rikkathewrold.rikkamusic.main.bean.RecommendPlayListBean;
import com.rikkathewrold.rikkamusic.main.bean.TopListBean;
import com.rikkathewrold.rikkamusic.manager.bean.MusicCanPlayBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserDetailBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserEventBean;
import com.rikkathewrold.rikkamusic.personal.bean.UserPlaylistBean;
import com.rikkathewrold.rikkamusic.search.bean.AlbumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.FeedSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.HotSearchDetailBean;
import com.rikkathewrold.rikkamusic.search.bean.PlayListSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.RadioSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SimiSingerBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerAblumSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerDescriptionBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SingerSongSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SongSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.SynthesisSearchBean;
import com.rikkathewrold.rikkamusic.search.bean.UserSearchBean;
import com.rikkathewrold.rikkamusic.song.bean.CommentLikeBean;
import com.rikkathewrold.rikkamusic.song.bean.LikeMusicBean;
import com.rikkathewrold.rikkamusic.song.bean.LyricBean;
import com.rikkathewrold.rikkamusic.song.bean.MusicCommentBean;
import com.rikkathewrold.rikkamusic.song.bean.PlayListCommentBean;
import com.rikkathewrold.rikkamusic.song.bean.SongDetailBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created By Rikka on 2019/7/12
 */
public interface ApiService {

    String BASE_URL = "http://192.168.51.189:3000/";

    @GET("login/cellphone")
    Observable<LoginBean> login(@Query("phone") String phone, @Query("password") String password);

    @GET("logout")
    Observable<LogoutBean> logout();

    @GET("banner")
    Observable<BannerBean> getBanner(@Query("type") String type);

    @GET("recommend/resource")
    Observable<MainRecommendPlayListBean> getRecommendPlayList();

    @GET("recommend/songs")
    Observable<DailyRecommendBean> getDailyRecommend();

    @GET("toplist")
    Observable<TopListBean> getTopList();

    @GET("dj/recommend")
    Observable<DjRecommendBean> getRadioRecommend();

    @GET("dj/recommend/type")
    Observable<DjRecommendTypeBean> getDjRecommend(@Query("type") int type);

    @GET("top/playlist")
    Observable<RecommendPlayListBean> getPlayList(@Query("cat") String type, @Query("limit") int limit);

    @GET("top/playlist/highquality")
    Observable<HighQualityPlayListBean> getHighquality(@Query("limit") int limit, @Query("before") long before);

    @GET("playlist/catlist")
    Observable<CatlistBean> getCatlist();

    @GET("playlist/detail")
    Observable<PlaylistDetailBean> getPlaylistDetail(@Query("id") long id);

    @GET("check/music")
    Observable<MusicCanPlayBean> getMusicCanPlay(@Query("id") long id);

    @GET("user/playlist")
    Observable<UserPlaylistBean> getUserPlaylist(@Query("uid") long uid);

    @GET("user/event")
    Observable<UserEventBean> getUserEvent(@Query("uid") long uid, @Query("limit") int limit, @Query("lasttime") long time);

    @GET("user/detail")
    Observable<UserDetailBean> getUserDetail(@Query("uid") long uid);

    @GET("search/hot/detail")
    Observable<HotSearchDetailBean> getSearchHotDetail();

    @GET("search")
    Observable<SongSearchBean> getSongSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<FeedSearchBean> getFeedSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<SingerSearchBean> getSingerSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<AlbumSearchBean> getAlbumSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<PlayListSearchBean> getPlayListSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<RadioSearchBean> getRadioSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<UserSearchBean> getUserSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<SynthesisSearchBean> getSynthesisSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("artists")
    Observable<SingerSongSearchBean> getSingerHotSong(@Query("id") long id);

    @GET("artist/album")
    Observable<SingerAblumSearchBean> getSingerAlbum(@Query("id") long id);

    @GET("artist/desc")
    Observable<SingerDescriptionBean> getSingerDesc(@Query("id") long id);

    @GET("simi/artist")
    Observable<SimiSingerBean> getSimiSinger(@Query("id") long id);

    @GET("likelist")
    Observable<LikeListBean> getLikeList(@Query("uid") long uid);

    @GET("song/detail")
    Observable<SongDetailBean> getSongDetail(@Query("ids") long ids);

    @GET("like")
    Observable<LikeMusicBean> likeMusice(@Query("id") long id);

    @GET("comment/music")
    Observable<MusicCommentBean> getMusicComment(@Query("id") long id);

    @GET("comment/like")
    Observable<CommentLikeBean> likeComment(@Query("id") long id, @Query("cid") long cid, @Query("t") int t, @Query("type") int type);

    @GET("playmode/intelligence/list")
    Observable<PlayModeIntelligenceBean> getIntelligenceList(@Query("id") long id, @Query("pid") long pid);

    @GET("album/sublist")
    Observable<AlbumSublistBean> getAlbumSublist();

    @GET("artist/sublist")
    Observable<ArtistSublistBean> getArtistSublist();

    @GET("mv/sublist")
    Observable<MvSublistBean> getMvSublist();

    @GET("personal_fm")
    Observable<MyFmBean> getMyFm();

    @GET("event")
    Observable<MainEventBean> getMainEvent();

    @GET("lyric")
    Observable<LyricBean> getLyric(@Query("id") long id);

    @GET("comment/playlist")
    Observable<PlayListCommentBean> getPlaylistComment(@Query("id") long id);

    @GET("dj/paygift")
    Observable<DjPaygiftBean> getDjPaygift(@Query("limit") int limit, @Query("offset") int offset);

    @GET("dj/category/recommend")
    Observable<DjCategoryRecommendBean> getDjCategoryRecommend();

    @GET("dj/catelist")
    Observable<DjCatelistBean> getDjCatelist();

    @GET("dj/sub")
    Observable<DjSubBean> subDj(@Query("rid") long rid, @Query("t") int isSub);

    @GET("dj/program")
    Observable<DjProgramBean> getDjProgram(@Query("rid") long rid);

    @GET("dj/detail")
    Observable<DjDetailBean> getDjDetail(@Query("rid") long rid);
}
