package www.mara.android.com.ActivitiesPackage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import www.mara.android.com.ChatBotAdapters.MessageAdaptor;
import www.mara.android.com.Model.ResponseMessage;
import www.mara.android.com.R;

public class SafeSpaces extends AppCompatActivity
{

   private RecyclerView recyclerView;
   private ImageButton btn_actvtPopUp;
   private EditText userInput_edittext;
   List<ResponseMessage> responseMessageList;
   MessageAdaptor messageAdaptor;
   private final int REQ_CODE_SPEECH_INPUT = 100;

   private Dialog talkToBot_popUp;
   private TextView userInput_textView;
   private TextView botMessage_textView;
   private ImageButton btn_mic;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_spaces);

        recyclerView = findViewById(R.id.chat_conversation);
        btn_actvtPopUp = findViewById(R.id.btn_userInput_voice);
        userInput_edittext = findViewById(R.id.userInput_text_id);


        //Initializing the popUp dialog
        talkToBot_popUp = new Dialog(this);

        /****

       //Creating an instance of configuring the AI
        final AIConfiguration aiConfiguration = new AIConfiguration("51bcd1a561c0478188f3be0c1e955ef2",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        //Getting reference to the AIService for making query requests
       aiService = AIService.getService(this,aiConfiguration);

       //Setting a listener to the aiService
        aiService.setListener(this);

         ****/



        responseMessageList = new ArrayList<>();
        messageAdaptor = new MessageAdaptor(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(messageAdaptor);

        //Creating a send button on the keyboard
        userInput_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEND)
                {
                    //Adding the user message to the view
                    ResponseMessage userMessage = new ResponseMessage(userInput_edittext.getText().toString(), true);
                    responseMessageList.add(userMessage);



                    //Adding the bot message to the view
                    ResponseMessage botMessage = new ResponseMessage(userInput_edittext.getText().toString(), false);
                    responseMessageList.add(botMessage);

                    //Detecting the datachange
                    messageAdaptor.notifyDataSetChanged();

                    //Removing the userinput from the edit text
                    userInput_edittext.getText().clear();

                    //Scrolling the screen to focus on the visible message
                    if (!isLastVisible())
                    {
                        recyclerView.smoothScrollToPosition(messageAdaptor.getItemCount() -1);
                    }
                }

                return false;
            }
        });

        //for pop up dialog
       btn_actvtPopUp.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
              //calling the popUp dialog
               botDialog();
           }
       });



    }

    //For the pop up dialog for user to talk to the bot
    private void botDialog()
    {
        talkToBot_popUp.setContentView(R.layout.talk_to_siya);
        //Showing the popUp dialog
        talkToBot_popUp.show();


        userInput_textView = (TextView) talkToBot_popUp.findViewById(R.id.tv_pop_up_userSpeech_input);
        botMessage_textView = (TextView) talkToBot_popUp.findViewById(R.id.tv_pop_up_botMessage_output);
        btn_mic = (ImageButton) talkToBot_popUp.findViewById(R.id.btn_userInput_mic);
        btn_mic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Checking if the permission to record audio has been granted
                if (ContextCompat.checkSelfPermission(SafeSpaces.this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED)
                {
                    //calling the method to get the user input speech
                    //Start listening to the user input through the device microphone
                    promptSpeechInput();
                }
                else
                    {
                        /*
                        /if the permission was not granted earlier
                        request for permission to record audio
                         */

                        Dexter.withActivity(SafeSpaces.this)
                                .withPermission(Manifest.permission.RECORD_AUDIO)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response)
                                    {
                                        /*If the user grants the permission
                                          start the speech input prompt
                                         */
                                        promptSpeechInput();
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response)
                                    {
                                        /*If the permission is permanently denied
                                        Send the user to the settings t allow the permission
                                         */
                                        if (response.isPermanentlyDenied())
                                        {
                                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SafeSpaces.this);
                                            alertBuilder.setTitle("Permission Denied");
                                            alertBuilder.setMessage("Permission to record audio has been permanently denied. To proceed you need to go to settings and allow the permission.");
                                            alertBuilder.setNegativeButton("Cancel", null);
                                            alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                   //Sending the user to the phone settings
                                                   Intent settingsIntent = new Intent();
                                                   settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                   settingsIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                                   startActivity(settingsIntent);

                                                }
                                            })
                                            .show();
                                        }
                                        else
                                            {
                                                /*
                                                If the permission is not allowed
                                                If the permission is not permanently denied
                                                 */
                                                Toast.makeText(SafeSpaces.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                                            }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                                    {
                                             token.continuePermissionRequest();
                                    }
                                }).check();
                    }

            }
        });

    }

    //for grtting the user input through speech
    private void promptSpeechInput()
    {
        //Showing Google Speech input dialog
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk to me...");

        try{
           //start recording the speech
            startActivityForResult(speechIntent, REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException e)
        {
            //Handling the exception in case the user device does not support speech input or there was an error
            Toast.makeText(this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    //Setting a functionality to allow the screen to scroll automatically when a new message gets in
    boolean isLastVisible()
    {
        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int position = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int numberOfItems = recyclerView.getAdapter().getItemCount();
        return (position >= numberOfItems);
    }

    //For obtaining the recorded speech input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT:
                {
                    if (resultCode == RESULT_OK && data != null)
                    {
                        //Getting text array from voice intent
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String userinput_speech = result.get(0);

                        //Setting the user speech input in the textview
                        userInput_textView.setText(userinput_speech);




                    }
                    break;
                }

        }

    }


}
