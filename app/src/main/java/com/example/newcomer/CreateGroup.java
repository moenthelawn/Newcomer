package com.example.newcomer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.*;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    UserData userData;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private ArrayList<String> data;
    private String holder;
    private int deleteCount = 0;
    private int countrrr =0;
    private ArrayAdapter<String> aradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        userData = (UserData) getApplicationContext();

        recyclerView = findViewById(R.id.recycler);
        adapter = new MyRecyclerViewAdapter(this, userData.getStatistics());

        int numColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), numColumns));
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(CreateGroup.this);

        aradapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,userData.getInterests_AutoComplete());

        //TextView tv = createContactTextView();
        multiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView);
        data = new ArrayList<String>();

        
        multiAutoCompleteTextView.setAdapter(aradapter);

        multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //We need to add this in .
                //We also need to find a way to add the lean meth
                String curr = aradapter.getItem(position);

            }
        });

        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiAutoCompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        multiAutoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String curr = s.toString();
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
            }
        });
        multiAutoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 60){


                    SpannableStringBuilder sb = new SpannableStringBuilder();
                    //THen we have hit the comma
                    String st =multiAutoCompleteTextView.getText().toString(); // multiAutoCompleteTextView.getText().toString();
                    String[] st_arry = st.split(",");

                    if (data.size() == 0){
                        createTextBox(st_arry[0],sb,true);
                        data.add(st_arry[0]);
                    }
                    else{
                        for (int i =0; i < st_arry.length;i++){
                            createTextBox(st_arry[i],sb,true);
                            if (i >= data.size()) {
                                data.add(st_arry[i]);
                            }
                        }
                    }
                    multiAutoCompleteTextView.setSelection(multiAutoCompleteTextView.getText().length());

                }
                else if (keyCode == 67){
                    //We need to update the current data array to be what is currently in teh multiautotextview displau

                    if (multiAutoCompleteTextView.getText().toString().length() != 0){

                    String last = String.valueOf(multiAutoCompleteTextView.getText().toString().charAt(multiAutoCompleteTextView.length() -1));
                    data = getUpdateDataArray(multiAutoCompleteTextView.getText().toString().split(","));

                    if (last.equals(",") == true && data.size() != 0) {
                        int index = getCursorIndex(data);
                        data.remove(index);

                        String[] arr = multiAutoCompleteTextView.getText().toString().split(",");
                        SpannableStringBuilder sb = new SpannableStringBuilder();

                        if (data.size() == 0) {
                            //Then we reset
                            createTextBox("", sb, true);
                        }
                        for (int i = 0; i < data.size(); i++) {
                            createTextBox(data.get(i), sb, true);
                            }

                        }
                    }



                }
                return false;
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        String val = aradapter.getItem(position);


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
            if (data.contains(curr) == false){
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
        TextView tv = createContactTextView(test); //Create the custom textview for the partiular elelment
        BitmapDrawable bd = (BitmapDrawable) convertViewToDrawable(tv);
        bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());
        if (reset == false){
            sb.append(st);
            sb.setSpan(new ImageSpan(bd), sb.length() - (test.length()), sb.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            multiAutoCompleteTextView.setText(sb);

        }else{

            sb.append(st + ",");
            sb.setSpan(new ImageSpan(bd), sb.length() - (test.length()+1), sb.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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
        tv.setTextSize(80);
        tv.setBackgroundResource(R.drawable.interest_button);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.places_ic_search, 0);
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

}
