package com.vwuilbea.mymoviecatalog.operations.search;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;
import com.vwuilbea.mymoviecatalog.tmdb.responses.SearchResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentin on 05/09/2014.
 */
public class SearchAdapter extends ArrayAdapter<SearchResponse.SearchResult> {

    private static final String LOG = SearchAdapter.class.getSimpleName();

    private LayoutInflater inflater=null;
    private int resourceId;
    private static String PREFIX_IMAGE = TmdbService.PREFIX_IMAGE+TmdbService.POSTER_SIZES[0];

    public SearchAdapter(Context context, int resourceId) {
        this(context,resourceId,new ArrayList<SearchResponse.SearchResult>());
    }

    public SearchAdapter(Context context, int resourceId, List<SearchResponse.SearchResult> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
        this.inflater = ((Activity)context).getLayoutInflater();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        SearchHolder holder = null;

        if(vi==null) {
            vi = inflater.inflate(this.resourceId, null);
            holder = new SearchHolder();
            holder.thumbPoster = (ImageView)vi.findViewById(R.id.item_thumb_poster);
            holder.title = (TextView) vi.findViewById(R.id.item_title);
            holder.rating = (TextView) vi.findViewById(R.id.item_rating);
            holder.year = (TextView) vi.findViewById(R.id.item_year);
            vi.setTag(holder);
        }
        else {
            holder = (SearchHolder) vi.getTag();
        }

        SearchResponse.SearchResult result = getItem(position);
        String posterPath = result.getPosterPath();
        if(posterPath!=null && !posterPath.equals("null"))
            Picasso.with(getContext())
                    .load(PREFIX_IMAGE+result.getPosterPath())
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(holder.thumbPoster);
        holder.title.setText(result.getTitle());
        holder.rating.setText(getContext().getResources().getString(R.string.rating)+":"+String.valueOf(result.getVoteAverage()));
        String year = result.getReleaseDate();
        if(year.length()>4) year = year.substring(0,4);
        else year = getContext().getResources().getString(R.string.unknown_year);
        holder.year.setText(year);

        if(position%2==0) vi.setBackgroundResource(R.drawable.bg_list_even);
        else vi.setBackgroundResource(R.drawable.bg_list_odd);

        return vi;
    }

    private static class SearchHolder {
        ImageView thumbPoster;
        TextView title;
        TextView rating;
        TextView year;
    }
}
