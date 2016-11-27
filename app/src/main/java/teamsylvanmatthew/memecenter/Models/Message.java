package teamsylvanmatthew.memecenter.Models;


public class Message {
    private String mUser;
    private String mMessage;


    public Message(String mUser, String mMessage) {
        this.mUser = mUser;
        this.mMessage = mMessage;
    }

    public String getmUser() {
        return mUser;
    }

    public void setmUser(String mUser) {
        this.mUser = mUser;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
