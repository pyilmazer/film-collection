package example.movies.backend;

import java.util.ArrayList;

/**
 * Created by pinar on 27.05.2016.
 */
public class Collectors {

    String fullName;
    String userId;
    String mail;
    String reference;
    ArrayList<String> followList = new ArrayList<String>();
    ArrayList<String> collectList = new ArrayList<String>();

    public ArrayList<String> getCollectList() {
        return collectList;
    }

    public void setCollectList(ArrayList<String> collectList) {
        this.collectList = collectList;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getFollowList() {
        return followList;
    }

    public void setFollowList(ArrayList<String> followList) {
        this.followList = followList;
    }
}
