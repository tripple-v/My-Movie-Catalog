package com.vwuilbea.mymoviecatalog.operations.details.series;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.model.Episode;
import com.vwuilbea.mymoviecatalog.model.Season;

import java.util.List;

/**
 * Created by Valentin on 29/09/2014.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String LOG = ExpandableListAdapter.class.getSimpleName();

    LayoutInflater inflater;

    /*list of group */
    private List<Season> seasons;

    public ExpandableListAdapter(Context context, List<Season> seasons) {
        super();
        this.seasons = seasons;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     *   * @param episode
     *   * @param season
     *   *  use for adding item to list view
     *   
     */
    public void addItem(Episode episode, Season season) {
        if(episode==null || season == null) return;
        if (!seasons.contains(season)) {
            seasons.add(season);
        }
        int index = seasons.indexOf(season);
        List<Episode> episodes = seasons.get(index).getEpisodes();
        episodes.add(episode);
        seasons.get(index).setEpisodes(episodes);
    }

    public Episode getChild(int groupPosition, int childPosition) {
        List<Episode> episodes = seasons.get(groupPosition).getEpisodes();
        return episodes.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Episode> ch = seasons.get(groupPosition).getEpisodes();
        return ch.size();
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Episode episode = getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_episode_item, null);
        }
        TextView childName = (TextView) convertView.findViewById(R.id.episode_title);
        childName.setText("\t"+episode.getNumber()+" - "+episode.getTitle());
        return convertView;
    }

    public Season getGroup(int groupPosition) {
        return seasons.get(groupPosition);
    }

    public int getGroupCount() {
        return seasons.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView groupName = null;
        Season season = getGroup(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_season_group, null);
        }
        groupName = (TextView) convertView.findViewById(R.id.season_title);
        groupName.setText("Season " + season.getNumber());
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

}
