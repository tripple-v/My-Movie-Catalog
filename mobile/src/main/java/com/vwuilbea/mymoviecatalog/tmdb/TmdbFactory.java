package com.vwuilbea.mymoviecatalog.tmdb;

import com.vwuilbea.mymoviecatalog.model.Actor;
import com.vwuilbea.mymoviecatalog.model.Role;
import com.vwuilbea.mymoviecatalog.model.Video;
import com.vwuilbea.mymoviecatalog.tmdb.responses.credits.Credit;

/**
 * Created by vwuilbea on 12/09/2014.
 */
public class TmdbFactory {

    private static final String LOG = TmdbFactory.class.getSimpleName();

    public static Role getRoleFromCredit(Credit credit, Video video) {
        String roleId = credit.getCreditId();
        String character = credit.getCharacter();
        String[] name = credit.getName().split(" ");
        Actor actor = new Actor(credit.getId());
        if(name.length>1) {
            actor.setFirstname(name[0]);
            actor.setName(name[1]);
        }
        else actor.setName(credit.getName());
        actor.setProfilePath(credit.getProfilePath());
        actor.addVideo(video);
        return new Role(roleId, actor, video, character);
    }

}
