package www.mara.android.com.ChatBotAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import www.mara.android.com.Model.ResponseMessage;
import www.mara.android.com.R;

public class MessageAdaptor extends RecyclerView.Adapter<MessageAdaptor.CustomViewHolder>
{

    List<ResponseMessage> responseMessageList;
    Context context;

    //Class to find the views for both the bot and user's messages
    class CustomViewHolder extends RecyclerView.ViewHolder
    {   //Declaring the views
        TextView mTextView;
        public CustomViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textMessage);
        }
    }

    public MessageAdaptor(List<ResponseMessage> responseMessageList, Context context)
    {
        this.responseMessageList = responseMessageList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdaptor.CustomViewHolder holder, int position)
    { //Binding the views
        holder.mTextView.setText(responseMessageList.get(position).getTextMessage());

    }

    @Override
    public int getItemCount() {
        return responseMessageList.size();
    }

    /*
      For positioning the views according to where the message is originating from
      (Either from the user or from the Bot)
     */
    @Override
    public int getItemViewType(int position)
    {
       //Checking if the message is from the user
        if (responseMessageList.get(position).isUser())
        {
            //showing the user message on the screen
            return R.layout.user_message_layout;
        }
        //Showing the bot message on the screen
        return R.layout.bot_text_layout;
    }
}
