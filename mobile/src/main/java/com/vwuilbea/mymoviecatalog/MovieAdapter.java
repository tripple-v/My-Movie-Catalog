package com.vwuilbea.mymoviecatalog;

import android.app.Activity;
import android.content.Context;
import android.media.Rating;
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
            holder = new SearchHolder();
            holder.thumbPoster = (ImageView)vi.findViewById(R.id.item_thumb_poster);
            holder.cornerButton = (ImageView) vi.findViewById(R.id.item_button_corner);
            holder.title = (TextView) vi.findViewById(R.id.item_title);
            holder.rating = (TextView) vi.findViewById(R.id.item_rating);
            holder.ratingBar = (RatingBar) vi.findViewById(R.id.rating_bar);
            holder.year = (TextView) vi.findViewById(R.id.item_year);
            holder.genres = (TextView) vi.findViewById(R.id.item_genres);
            vi.setTag(holder);
        }
        else {
            holder = (SearchHolder) vi.getTag();
        }

        List<Video> videos = getVideos();
        Video video = getItem(position);

        String posterPath = video.getPosterPath();
        if(posterPath!=null && !posterPath.equals("null"))
            Picasso.with(getContext())
                    .load(PREFIX_IMAGE + video.getPosterPath())
                    .fit()
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(holder.thumbPoster);
        holder.title.setText(video.getTitle());
        String rateS = "( ";
        //rateS += video.getVoteAverage()+"/10, ";
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(LOCALE);
        rateS += formatter.format(video.getVoteCount()) + " ";
        if(video.getVoteCount()>1) rateS += getContext().getString(R.string.votes);
        else rateS += getContext().getString(R.string.vote);
        rateS += " )";
        holder.rating.setText(rateS);
        holder.ratingBar.setRating((float) video.getVoteAverage() / 2);

        String date = video.getReleaseDate();
        if(date == null || date.equals("null")) date = getContext().getResources().getString(R.string.unknown_year);
        holder.year.setText(date);

        if(videos.contains(video)){
            holder.cornerButton.setImageResource(android.R.drawable.ic_delete);
            holder.cornerButton.setContentDescription(DELETE);
        }
        else {
            holder.cornerButton.setImageResource(android.R.drawable.ic_input_add);
            holder.cornerButton.setContentDescription(ADD);
        }
        if(clickListener !=null) holder.cornerButton.setOnClickListener(clickListener);
        holder.cornerButton.setTag(video);

        String genres = "";
        String delim = "";
        for(Genre genre:video.getGenres()) {
            if(genre!=null) {
                genres += delim + genre.getName();
                delim = ", ";
            }
        }
        holder.genres.setText(genres);

        return vi;
    }

    private static class SearchHolder {
        ImageView thumbPoster;
        ImageView cornerButton;
        TextView title;
        TextView rating;
        TextView year;
        RatingBar ratingBar;
        TextView genres;
    }
}
