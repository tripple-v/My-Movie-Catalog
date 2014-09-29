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
        if (!seasons.contains(season)) {
            seasons.add(season);
        }
        int index = seasons.indexOf(season);
        List<Episode> ch = seasons.get(index).getEpisodes();
        ch.add(episode);
        seasons.get(index).setEpisodes(ch);
    }

    public Episode getChild(int groupPosition, int childPosition) {
        List<Episode> ch = seasons.get(groupPosition).getEpisodes();
        return ch.get(childPosition);
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
        Episode episode = (Episode) getChild(groupPosition, childPosition);
        TextView childName = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_episode_item, null);
        }
        childName = (TextView) convertView.findViewById(R.id.episode_title);
        childName.setText(episode.getTitle());
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
        Season season = (Season) getGroup(groupPosition);
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
