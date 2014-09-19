package com.vwuilbea.mymoviecatalog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Series;
import com.vwuilbea.mymoviecatalog.model.Video;

import java.util.ArrayList;
import java.util.List;


public class DisplayVideosFragment extends Fragment
        implements AdapterView.OnItemClickListener,
        View.OnClickListener
{

    private static final String LOG = DisplayVideosFragment.class.getSimpleName();

    //Param used to know what to display
    private static final String ARG_PARAM_CATEGORY = "category";
    private static final String ARG_PARAM_ALL_VIDEOS = "all_videos";

    public static final int CATEGORY_ALL = 0;
    public static final int CATEGORY_MOVIE = 1;
    public static final int CATEGORY_SERIES = 2;

    private int category;
    private  ArrayList<Video> allVideos;
    private Video selectedVideo;

    private  MovieAdapter adapter;

    private OnFragmentInteractionListener mListener;


    public static DisplayVideosFragment newInstance(int category, ArrayList<Video> allVideos) {
        DisplayVideosFragment fragment = new DisplayVideosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_CATEGORY, category);
        args.putParcelableArrayList(ARG_PARAM_ALL_VIDEOS, allVideos);
        fragment.setArguments(args);
        return fragment;
    }

    public DisplayVideosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getInt(ARG_PARAM_CATEGORY);
            mListener.onSectionAttached(category);
            allVideos = getArguments().getParcelableArrayList(ARG_PARAM_ALL_VIDEOS);
            List<Video> displayedVideos;
            if(category==CATEGORY_ALL) displayedVideos = allVideos;
            else {
                displayedVideos = new ArrayList<Video>();
                if(category == CATEGORY_MOVIE) {
                    for(Video video:allVideos)
                        if(video instanceof Movie)
                            displayedVideos.add(video);
                }
                else if(category == CATEGORY_SERIES) {
                    for(Video video:allVideos)
                        if(video instanceof Series)
                            displayedVideos.add(video);
                }
            }
            adapter = new MovieAdapter(getActivity(), R.layout.list_search_result, displayedVideos, this);
            adapter.sort(Video.COMPARATOR_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_display_movies, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.videos_list);
        TextView textView = (TextView) rootView.findViewById(R.id.text_no_content);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        if(adapter.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Video video = adapter.getItem(position);
        if(mListener!=null) mListener.onVideoClicked(video);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_button_corner:
                selectedVideo = (Video) v.getTag();
                if(mListener!=null && MovieAdapter.DELETE.equals(v.getContentDescription()))
                    deleteVideo();
                break;
            default: break;
        }
    }

    private void deleteVideo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.confirmation))
                .setPositiveButton("Yes", removeListener)
                .setNegativeButton("No", removeListener)
                .show();
    }

    private DialogInterface.OnClickListener removeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    int res = ((MyApplication) getActivity().getApplication()).removeVideo(selectedVideo, true);
                    if(res == MyApplication.OK)  adapter.remove(selectedVideo);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
            selectedVideo = null;
        }
        public void aa(){}
    };

    public interface OnFragmentInteractionListener {

        public void onVideoClicked(Video video);
        public void onSectionAttached(int category);
    }

}
