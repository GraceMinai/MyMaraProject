package www.mara.android.com.Model;

public class ResponseMessage
{

    String textMessage;
    boolean isUser; //Used to check whether the message is from the bot or the user

    public ResponseMessage(String textMessage, boolean isUser) {
        this.textMessage = textMessage;
        this.isUser = isUser;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
}
