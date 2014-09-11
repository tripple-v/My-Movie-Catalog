package com.vwuilbea.mymoviecatalog.tmdb.responses.credits;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentin on 04/09/2014.
 */
public class CreditsResponse implements Parcelable {

    public static final Parcelable.Creator<CreditsResponse> CREATOR = new Parcelable.Creator<CreditsResponse>()
    {
        @Override
        public CreditsResponse createFromParcel(Parcel source)
        {
            return new CreditsResponse(source);
        }

        @Override
        public CreditsResponse[] newArray(int size)
        {
            return new CreditsResponse[size];
        }
    };

    private static final String LOG = CreditsResponse.class.getSimpleName();

    private static final String PARAM_ID = "id";
    private static final String PARAM_CAST = "cast";

    private int id;
    private List<Credit> credits = new ArrayList<Credit>();

    public CreditsResponse(Parcel in) {
        this.id = in.readInt();
        in.readList(this.credits, Credit.class.getClassLoader());
    }

    public CreditsResponse(String result) {
        try {
            JSONObject object = new JSONObject(result);
            this.id = object.getInt(PARAM_ID);
            JSONArray array = object.getJSONArray(PARAM_CAST);
            for (int i = 0; i < array.length(); i++)
                credits.add(new Credit(array.getJSONObject(i), this.id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public List<Credit> getCredits() {
        return credits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeList(credits);
    }

}
