package system.management.information.itms.Models;

/**
 * Created by Janescience on 11/23/2017.
 */

public class User {
    public String uid;
    public String image;
    public String name;
    public String firebaseToken;

    public User(){

    }

    public User(String uid, String image, String name){
        this.uid = uid;
        this.image = image;
        this.name = name;
        this.firebaseToken = firebaseToken;
    }
}
