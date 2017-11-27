package system.management.information.itms.Models;

/**
 * Created by Janescience on 11/23/2017.
 */

public class Chat {
    public String sender;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
    public String profileImage;
    public long timestamp;

    public Chat(){

    }

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message,String profileImage, long timestamp){
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;
        this.profileImage = profileImage;

    }



}
