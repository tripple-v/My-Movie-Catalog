package com.vwuilbea.mymoviecatalog.operations.details.series;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.model.Episode;
import com.vwuilbea.mymoviecatalog.model.Season;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;

import java.util.HashMap;
import java.util.List;

/**
 * This is a custom array adapter used to populate the listview whose items will
 * expand to display extra content in addition to the default display.
 */
public class SeasonAdapter extends BaseExpandableListAdapter {

    private static final String LOG = SeasonAdapter.class.getSimpleName();
    private static String PREFIX_IMAGE = TmdbService.PREFIX_IMAGE+TmdbService.POSTER_SIZES[3];

    private Context context;
    private List<Season> seasons; // header titles
    // child data in format of header title, child title
    private HashMap<Season, List<Episode>> episodes;

    public SeasonAdapter(Context context, List<Season> seasons,
                                 HashMap<Season, List<Episode>> episodes) {
        this.context = context;
        this.seasons = seasons;
        this.episodes = episodes;
        String msg = "Seasons:\n";
        for(Season season:seasons) {
            msg += "Season "+season.getNumber()+" ("+season.getId()+")\n";
        }
        msg += "\nEpisodes:\n";
        for(Season season:episodes.keySet()) {
            msg += "Season "+season.getNumber()+" ("+season.getId()+")\n";
            for(Episode episode:episodes.get(season)) {
                msg+=episode.getNumber()+"-"+episode.getTitle()+"\n";
            }
        }
        Log.d(LOG,msg);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        Log.d(LOG,"getChild at position "+childPosititon+" of group "+groupPosition);
        return this.episodes.get(this.seasons.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Log.d(LOG,"getChildId: "+childPosition);
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_episode_item, null);
        }
        final Episode episode = (Episode) getChild(groupPosition, childPosition);
        Log.d(LOG,"childView:"+ episode.getTitle());

        TextView txtListChild = (TextView) convertView.findViewById(R.id.episode_title);

        txtListChild.setText(episode.getTitle());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.d(LOG,"getChildrenCount: "+this.episodes.get(this.seasons.get(groupPosition)).size());
        return this.episodes.get(this.seasons.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        Log.d(LOG,"getGroup at position "+groupPosition);
        return this.seasons.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        Log.d(LOG,"getGroupCount: "+this.seasons.size());
        return this.seasons.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        Log.d(LOG,"getGroupId: "+groupPosition);
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_season_group, null);
        }
        final Season season = (Season) getGroup(groupPosition);
        Log.d(LOG,"groupView:"+ context.getString(R.string.season)+" "+season.getNumber());

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.season_title);
        lblListHeader.setText(context.getString(R.string.season)+" "+season.getNumber());
/*
        Picasso.with(context)
                .load(PREFIX_IMAGE+season.getPosterPath())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into((ImageView)convertView.findViewById(R.id.season_poster));
*/
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        Log.d(LOG,"hasStableIds: false");
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        Log.d(LOG,"isChildSelectable: true");
        return true;
    }


}