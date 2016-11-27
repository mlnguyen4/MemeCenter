package teamsylvanmatthew.memecenter.Models;


public class Message {
    private String mUser;
    private String mMessage;


    public Message(String mUser, String mMessage) {
        this.mUser = mUser;
        this.mMessage = mMessage;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String mUser) {
        this.mUser = mUser;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
