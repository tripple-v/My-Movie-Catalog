package com.vwuilbea.mymoviecatalog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vwuilbea.mymoviecatalog.model.Video;
import com.vwuilbea.mymoviecatalog.operations.search.SearchResultsActivity;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentin on 05/09/2014.
 */
public class MovieAdapter extends ArrayAdapter<Video> {

    private static final String LOG = MovieAdapter.class.getSimpleName();

    private LayoutInflater inflater=null;
    private int resourceId;
    private static String PREFIX_IMAGE = TmdbService.PREFIX_IMAGE+TmdbService.POSTER_SIZES[0];
    private boolean contextSearch=true;

    public MovieAdapter(Context context, int resourceId) {
        this(context,resourceId,new ArrayList<Video>());
    }

    public MovieAdapter(Context context, int resourceId, List<Video> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
        this.inflater = ((Activity)context).getLayoutInflater();
        contextSearch = context.getClass() == SearchResultsActivity.class;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        SearchHolder holder;

        if(vi==null) {
            vi = inflater.inflate(this.resourceId, null);
            holder = new SearchHolder();
            holder.thumbPoster = (ImageView)vi.findViewById(R.id.item_thumb_poster);
            holder.title = (TextView) vi.findViewById(R.id.item_title);
            holder.rating = (TextView) vi.findViewById(R.id.item_rating);
            holder.year = (TextView) vi.findViewById(R.id.item_year);
            holder.cornerButton = (ImageView) vi.findViewById(R.id.item_button_add);
            vi.setTag(holder);
        }
        else {
            holder = (SearchHolder) vi.getTag();
        }

        Video video = getItem(position);
        String posterPath = video.getPosterPath();
        if(posterPath!=null && !posterPath.equals("null"))
            Picasso.with(getContext())
                    .load(PREFIX_IMAGE + video.getPosterPath())
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(holder.thumbPoster);
        holder.title.setText(video.getTitle());
        holder.rating.setText(getContext().getResources().getString(R.string.rating) + ":" + String.valueOf(video.getVoteAverage()));
        String date = video.getReleaseDate();
        if(date == null || date.equals("null")) date = getContext().getResources().getString(R.string.unknown_year);
        holder.year.setText(date);

        if(contextSearch) holder.cornerButton.setImageResource(android.R.drawable.ic_input_add);
        else holder.cornerButton.setImageResource(android.R.drawable.ic_delete);

        if(position%2==0) vi.setBackgroundResource(R.drawable.bg_list_even);
        else vi.setBackgroundResource(R.drawable.bg_list_odd);

        return vi;
    }

    private static class SearchHolder {
        ImageView thumbPoster;
        TextView title;
        TextView rating;
        TextView year;
        ImageView cornerButton;
    }
}
