package com.vwuilbea.mymoviecatalog.operations.details.series;

import com.example.android.expandingcells.ExpandableListItem;
import com.example.android.expandingcells.OnSizeChangedListener;
import com.vwuilbea.mymoviecatalog.model.Season;

/**
 * Created by Valentin on 28/09/2014.
 */
public class SeasonItem extends ExpandableListItem {
    private static final String LOG = SeasonItem.class.getSimpleName();

    private Season season;

    public SeasonItem(Season season, int collapsedHeight) {
        super(null,0,collapsedHeight,null);
        this.season = season;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }
}
