package com.example.newcomer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.*;
import android.text.style.ImageSpan;
import android.view.KeyEvent;

import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    UserData userData;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private MultiAutoCompleteTextView multiAutoCompleteTextView;

    private ArrayAdapter<String> aradapter;
    private GroupData groupData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        userData = (UserData) getApplicationContext();

        recyclerView = findViewById(R.id.recycler);
        adapter = new MyRecyclerViewAdapter(this, userData.getStatistics());

        int numColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumns));

        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        String[] progressBarSettings = {"Interests", "Timing","Description"};
        stateProgressBar.setStateDescriptionData(progressBarSettings);


        groupData = new GroupData(userData.getMAX()); ///This acts as the bind behind all of the data for the buttons
        aradapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,userData.getInterests_AutoComplete());

        //Add the divider to the app so that we can separate some of our components
        DividerItemDecoration itemDecorator_vertical = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        DividerItemDecoration itemDecoration_horizontal = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);

        itemDecorator_vertical.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        itemDecoration_horizontal.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));

        recyclerView.addItemDecoration(itemDecorator_vertical);

        multiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView);
        multiAutoCompleteTextView.setAdapter(aradapter);
        multiAutoCompleteTextView.setError("Use comma(s) to separate your interests");

        multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //We need to add this in .
                //We also need to find a way to add the lean meth
                String curr = aradapter.getItem(position);

                //1. Ensure that it has not already been aded
                String[] arr = multiAutoCompleteTextView.getText().toString().split(",");
                if (groupData.getArrayList_Data().contains(curr) == false){
                    updateTextBox(arr,"ADD");
                }
            }
        });

        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiAutoCompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        multiAutoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                multiAutoCompleteTextView.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String curr = s.toString();

                multiAutoCompleteTextView.setError(null);

                if (curr.length() > 1){
                    String last = String.valueOf(curr.charAt(curr.length() - 1));
                    String secondLast = String.valueOf(curr.charAt(curr.length() - 2));
                    if (last.equals(",") == true && secondLast.equals(",") == true){
                        //THen we remove
                        curr = curr.replace(",,",",");
                        String[] curr_arry = curr.split(",");
                        SpannableStringBuilder sb = new SpannableStringBuilder();
                        for (int i = 0; i < curr_arry.length;i++) {
                            createTextBox(curr_arry[i], sb, true);
                        }
                    }
                }
                int length_arr = multiAutoCompleteTextView.getText().toString().split(",").length;
                TextView next = findViewById(R.id.next);
                if (length_arr >= 3){
                    next.setTextColor(ContextCompat.getColor(CreateGroup.this,R.color.Red));
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Then we start the next intent which is related to the Timing of the event (when it will take place)
                            Intent intent = new Intent(CreateGroup.this, GroupTiming.class);
                            startActivity(intent);
                        }
                    });
                    //Then we can set hte button color of the next to be that of red
                }
                else{
                    next.setTextColor(ContextCompat.getColor(CreateGroup.this,R.color.LightGrey)); //Then we want to grey it out because it is not important at this phase
                }


            }
        });
        multiAutoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 60){
                    //THen we have hit the comma
                    String st =multiAutoCompleteTextView.getText().toString(); // multiAutoCompleteTextView.getText().toString();
                    String[] st_arry = st.split(",");
                    updateTextBox(st_arry,"ADD");

                }
                else if (keyCode == 67){
                    //We need to update the current data array to be what is currently in teh multiautotextview displau

                    multiAutoCompleteTextView.refreshDrawableState();

                    //if (multiAutoCompleteTextView.getText().toString().length() != 0){
                        String[] arr = multiAutoCompleteTextView.getText().toString().split(",");
                        //updateButtonElements(arr);
                        // ;
                        groupData.setArrayList_Data(getUpdateDataArray(arr));

                        //updateTextBox(arr,"REMOVE");
                    //}
                }
                return false;
            }
        });
    }

    private void updateTextBox(String[] st_arry, String TYPE) {
        SpannableStringBuilder sb = new SpannableStringBuilder();

        if (groupData.getArrayList_Data().size() == 0 && st_arry.length != 0){
            createTextBox(st_arry[0],sb,true);
            groupData.appendArrayList_Data(st_arry[0]);
        }
        else if(TYPE.equals("REMOVE") == true){
                for (int i = 0; i < groupData.getArrayList_Data().size(); i++) {
                    createTextBox(groupData.getElement_Data(i), sb, true);
                }

        }
        else if (TYPE.equals("ADD") == true){
            for (int i =0; i < st_arry.length;i++){
                if (st_arry[i].equalsIgnoreCase(" ") == false) { //We do not want the blank index
                    createTextBox(st_arry[i], sb, true);

                    if (groupData.getArrayList_Data().contains(st_arry[i]) == false) {
                        groupData.appendArrayList_Data(st_arry[i]);
                    }
                }
            }
        }
        multiAutoCompleteTextView.setSelection(multiAutoCompleteTextView.getText().length());
    }

    private int getCursorIndex(ArrayList<String> dataf) {
        //This function will loop through and get the current index that the cursor is
        int cursorPosition = multiAutoCompleteTextView.getSelectionEnd();
        String fullText = multiAutoCompleteTextView.getText().toString();
        //How to know what the cursor position is correlated to what word
        int totalLength = 0;

        for (int i = 0; i < dataf.size();i++){
            String curr = dataf.get(i);
            int curr_length = curr.length();

            String leftIndex = String.valueOf(fullText.charAt(cursorPosition - 1));
            String sub;
            if (leftIndex.equals(",")){
                //Then we need to add one move over
                sub = fullText.substring((cursorPosition) - (curr_length + 1), cursorPosition - 1);
            }else{
                sub = fullText.substring((cursorPosition) - curr_length, cursorPosition);
            }

            //We need to add a use case, lets say there is a comma to the left, then we need to take that into considration

            if (sub.equals(curr) == true){
                //Then we know that we have found the position
                return i;
            }
        }
        return -1;

    }

    private int getArrayIndex(String[] arr, String wordDiscrep) {
        for (int i =0; i < arr.length;i++){
            if (arr[i].equals(wordDiscrep) == true){
                return i;
            }
        }
        return -1;
    }

    private String getWordDiscrepancies(String[] arr, ArrayList<String> data) {
        for (int i= 0; i < arr.length;i++){
            String curr = arr[i];
            if (groupData.getArrayList_Data().contains(curr) == false){
                //Then we know that we do not have this string
                return curr;
            }
        }
        return "";
    }


    private ArrayList<String> getUpdateDataArray(String[] split) {
        ArrayList<String> updated = new ArrayList<String>();
        for (int i = 0; i < split.length;i++)
        {
            updated.add(split[i]);
        }
        return updated;
    }

    private void createTextBox(String st,SpannableStringBuilder sb,boolean reset){

        String test = st;  //sb.toString();
        TextView tv = createContactTextView(test.toUpperCase()); //Create the custom textview for the partiular elelment

        BitmapDrawable bd = (BitmapDrawable) convertViewToDrawable(tv);
        bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());

        if (reset == false){
            sb.append(st);
            sb.setSpan(new ImageSpan(bd), sb.length() - (test.length()), sb.length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            multiAutoCompleteTextView.setText(sb);

        }else{

            sb.append(st + ",");
            sb.setSpan(new ImageSpan(bd), sb.length() - (test.length()+1), sb.length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            multiAutoCompleteTextView.setText(sb);

        }
        multiAutoCompleteTextView.refreshDrawableState();
        multiAutoCompleteTextView.setSelection(multiAutoCompleteTextView.length());

    }

    private int count_string(String name, String var){
        int count =0;
        for (int i = 0; i < name.length();i++){
            if (name.charAt(i) == var.toCharArray()[0]){
                count += 1;

            }
        }
        return count;
    }
    private TextView createContactTextView(String cur) {
        //creating textview dynamically
        TextView tv = new TextView(this);
        tv.setText(cur);
        tv.setTextSize(60);

        tv.setBackgroundResource(R.drawable.interest_button);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
        return tv;
    }
    public static Object convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);

        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);

    }
    public void showInput(){
        String input = multiAutoCompleteTextView.getText().toString();
        String[] singleInputs = input.split("\\s*,\\s*");
     }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onItemClick(View view, int position,Button currButton) {

        //String curr = adapter.getItem(position);
        //Duplicate this datas

        String curr = currButton.getText().toString();
        currButton.setWidth(0);

        //1. Ensure that it has not already been aded
        String text = multiAutoCompleteTextView.getText().toString();

        if (text.length() != 0){
            String lastDigit = String.valueOf(text.charAt(text.length() - 1));
            if (lastDigit.equals(",") == true){
                text += curr;
            }else{
                text = text + "," + curr;
            }
        }else{
            text = curr;
        }
        String[] arr = text.split(",");
        //Now we see if this button is already added
        //if (data.contains(curr) == false) {
        if (groupData.getArrayList_Data().contains(curr) == false){
            multiAutoCompleteTextView.setError(null);
            updateTextBox(arr,"ADD");
        }
    }

    @Override
    public void setStrokeWidth(Button currButton, boolean clicked) {
        GradientDrawable gradientDrawable = (GradientDrawable) currButton.getBackground();

        if (clicked == true){
            gradientDrawable.setStroke(15, ContextCompat.getColor(this,R.color.Black));
        }
        else{
            //Otherwise it hasn't been clicked
            gradientDrawable.setStroke(5,ContextCompat.getColorStateList(this,R.color.LightGrey));
        }
        currButton.setBackground(gradientDrawable);
    }

    private void setStrokeColor(Button currButton,boolean clicked) {

    }
}
