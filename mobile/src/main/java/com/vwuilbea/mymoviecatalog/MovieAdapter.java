package com.vwuilbea.mymoviecatalog;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vwuilbea.mymoviecatalog.model.Genre;
import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Video;
import com.vwuilbea.mymoviecatalog.operations.search.SearchResultsActivity;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Valentin on 05/09/2014.
 */
public class MovieAdapter extends ArrayAdapter<Video> {

    public static final String DELETE = "delete";
    public static final String ADD = "add";

    private static final String LOG = MovieAdapter.class.getSimpleName();
    private static final Locale LOCALE = new Locale(MovieCatalog.LANGUAGE);

    private LayoutInflater inflater=null;
    private int resourceId;
    private static String PREFIX_IMAGE = TmdbService.PREFIX_IMAGE+TmdbService.POSTER_SIZES[3];
    private boolean contextSearch=true;
    private View.OnClickListener clickListener;
    private static int[] logosHD =  { R.drawable.logo_720p, R.drawable.logo_1080p, R.drawable.logo_4k};

    public MovieAdapter(Context context, int resourceId) {
        this(context,resourceId,new ArrayList<Video>());
    }

    public MovieAdapter(Context context, int resourceId, View.OnClickListener clickListener) {
        this(context,resourceId,new ArrayList<Video>(), clickListener);
    }

    public MovieAdapter(Context context, int resourceId, List<Video> objects) {
        this(context, resourceId, objects, null);
    }

    public MovieAdapter(Context context, int resourceId, List<Video> objects, View.OnClickListener clickListener) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
        this.inflater = ((Activity)context).getLayoutInflater();
        contextSearch = context.getClass() == SearchResultsActivity.class;
        this.clickListener = clickListener;
        Log.d(LOG, "LOCALE: "+LOCALE);
    }

    private List<Video> getVideos() {
        List<Video> res;
        if(getContext() instanceof  Activity) {
            Activity activity = (Activity) getContext();
            res = ((MyApplication) activity.getApplication()).getAllVideos();
        }
        else {
            res = new ArrayList<Video>();
        }
        return res;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        SearchHolder holder;

        if(vi==null) {
            vi = inflater.inflate(this.resourceId, null);
            holder = new SearchHolder(vi);
            vi.setTag(holder);
        }
        else {
            holder = (SearchHolder) vi.getTag();
        }

        List<Video> videos = getVideos();
        Video video = getItem(position);
        boolean isKnownVideo = videos.contains(video);

        /* Poster image */
        String posterPath = video.getPosterPath();
        if(posterPath!=null && !posterPath.equals("null"))
            Picasso.with(getContext())
                    .load(PREFIX_IMAGE + video.getPosterPath())
                    .fit()
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(holder.thumbPoster);
        /* *********** */

        //Title
        holder.title.setText(video.getTitle());

        /* Rate well format */
        String rateS = "( ";
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(LOCALE);
        rateS += formatter.format(video.getVoteCount()) + " ";
        if(video.getVoteCount()>1) rateS += getContext().getString(R.string.votes);
        else rateS += getContext().getString(R.string.vote);
        rateS += " )";
        holder.rating.setText(rateS);

        holder.ratingBar.setRating(video.getVoteAverage()/2);
        /* ************** */

        /* Date */
        String date = video.getReleaseDate();
        if(date == null || date.equals("null")) date = getContext().getResources().getString(R.string.unknown_year);
        holder.year.setText(date);
        /* **** */

        /* Genres */
        String genres = "";
        String delim = "";
        for(Genre genre:video.getGenres()) {
            if(genre!=null) {
                genres += delim + genre.getName();
                delim = ", ";
            }
        }
        holder.genres.setText(genres);
        /* ****** */

        //Badge to differentiate movie/series
        String type = video instanceof Movie ? getContext().getString(R.string.movie) : getContext().getString(R.string.series);
        holder.videoType.setText(type);

        /* Fields depend on the context */
        if(isKnownVideo){
            holder.cornerButton.setImageResource(android.R.drawable.ic_delete);
            holder.cornerButton.setContentDescription(DELETE);
            if(video.isThreeD()) holder.logo3d.setVisibility(View.VISIBLE);
            else holder.logo3d.setVisibility(View.INVISIBLE);
            //Show quality logo only if HD
            int quality = video.getQuality();
            if(quality > Video.Quality.NORMAL.getId()) {
                int logoId = logosHD[quality- Video.Quality.NORMAL.getId()-1];
                holder.logoHD.setImageResource(logoId);
            }
            else {
                holder.logoHD.setVisibility(View.INVISIBLE);
            }
        }
        else {
            holder.cornerButton.setImageResource(android.R.drawable.ic_input_add);
            holder.cornerButton.setContentDescription(ADD);
            holder.logo3d.setVisibility(View.INVISIBLE);
            holder.logoHD.setVisibility(View.INVISIBLE);
        }
        holder.cornerButton.setTag(video);
        if(clickListener !=null) holder.cornerButton.setOnClickListener(clickListener);
        /* ************************** */

        return vi;
    }

    /**
     * Static class to hold views
     */
    private static class SearchHolder {
        ImageView thumbPoster;
        ImageView cornerButton;
        TextView title;
        TextView rating;
        TextView year;
        RatingBar ratingBar;
        TextView genres;
        TextView videoType;
        ImageView logo3d;
        ImageView logoHD;

        public SearchHolder(View vi) {
            thumbPoster = (ImageView)vi.findViewById(R.id.item_thumb_poster);
            cornerButton = (ImageView) vi.findViewById(R.id.item_button_corner);
            title = (TextView) vi.findViewById(R.id.item_title);
            rating = (TextView) vi.findViewById(R.id.item_rating);
            ratingBar = (RatingBar) vi.findViewById(R.id.rating_bar);
            year = (TextView) vi.findViewById(R.id.item_year);
            genres = (TextView) vi.findViewById(R.id.item_genres);
            videoType = (TextView) vi.findViewById(R.id.item_video_type);
            logo3d = (ImageView) vi.findViewById(R.id.item_3d);
            logoHD = (ImageView) vi.findViewById(R.id.item_quality);
        }
    }
}
