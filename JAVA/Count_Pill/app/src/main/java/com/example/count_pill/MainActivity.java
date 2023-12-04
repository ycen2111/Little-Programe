package com.example.count_pill;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.count_pill.file.Memory;
import com.example.count_pill.global_value.ReadPillNumber;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static  String TAG = "MainActivity";
    Memory memory = new Memory("grandpaPill");
    ReadPillNumber readPillNumber = new ReadPillNumber(memory);

    TextView pill1_display;
    TextView pill2_display;
    TextView pill3_display;
    TextView pill4_display;
    TextView pill5_display;
    TextView update_time;

    String modifiedTimeString = "None";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pill_calendar);

        this.pill1_display = findViewById(R.id.pill1_display);
        this.pill2_display = findViewById(R.id.pill2_display);
        this.pill3_display = findViewById(R.id.pill3_display);
        this.pill4_display = findViewById(R.id.pill4_display);
        this.pill5_display = findViewById(R.id.pill5_display);
        this.update_time = findViewById(R.id.update_time);

        if (memory.hasFile()) {
            if (memory.getFile().canWrite())
                this.refresh_Textview();
            else {
                try {
                    delete_file();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void refresh_Textview() {
        long modifiedTime = memory.getFile().lastModified();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(modifiedTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        modifiedTimeString = format.format(calendar.getTime());

        this.readPillNumber.refreshNumber(modifiedTime);
        Integer[] stringArray = this.readPillNumber.getRemainingDays();

        pill1_display.setText("剩余天数: "+stringArray[0]);
        pill2_display.setText("剩余天数: "+stringArray[1]);
        pill3_display.setText("剩余天数: "+stringArray[2]);
        pill4_display.setText("剩余天数: "+stringArray[3]);
        pill5_display.setText("剩余天数: "+stringArray[4]);
        update_time.setText("最后进药时间：" + modifiedTimeString);

        if (stringArray[0] < 8)
            pill1_display.setTextColor(Color.RED);
        else
            pill1_display.setTextColor(Color.BLACK);

        if (stringArray[1] < 8)
            pill2_display.setTextColor(Color.RED);
        else
            pill2_display.setTextColor(Color.BLACK);

        if (stringArray[2] < 8)
            pill3_display.setTextColor(Color.RED);
        else
            pill3_display.setTextColor(Color.BLACK);

        if (stringArray[3] < 8)
            pill4_display.setTextColor(Color.RED);
        else
            pill4_display.setTextColor(Color.BLACK);

        if (stringArray[4] < 8)
            pill5_display.setTextColor(Color.RED);
        else
            pill5_display.setTextColor(Color.BLACK);
    }

    public void delete_file() throws  IOException{
        try{
            if (this.memory.hasFile()){
                this.memory.delete_file();
                Toast.makeText(this,"文件已删除",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"文件未找到",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize_pill_num(View view) throws IOException{
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        View dialogView = inflater.inflate(R.layout.input_dialog, null);
        builder.setView(dialogView);
        builder.setTitle("请输入当前的或需要增加的药片数量");

        EditText input_pill_1 = dialogView.findViewById(R.id.input_pill_1);
        EditText input_pill_2 = dialogView.findViewById(R.id.input_pill_2);
        EditText input_pill_3 = dialogView.findViewById(R.id.input_pill_3);
        EditText input_pill_4 = dialogView.findViewById(R.id.input_pill_4);
        EditText input_pill_5 = dialogView.findViewById(R.id.input_pill_5);
        ImageView modSwitcher = dialogView.findViewById(R.id.modSwitcher);
        TextView switchLabel = dialogView.findViewById(R.id.switchLabel);

        String[] previousDataArray = new String[]{"0", "0", "0", "0", "0"};
        if (memory.hasFile()) {
            try {
                previousDataArray = readPillNumber.getStringArray();
                Log.d(TAG, "initialize_pill_num: "+previousDataArray);
                input_pill_1.setHint("现有"+previousDataArray[0]);
                input_pill_2.setHint("现有"+previousDataArray[1]);
                input_pill_3.setHint("现有"+previousDataArray[2]);
                input_pill_4.setHint("现有"+previousDataArray[3]);
                input_pill_5.setHint("现有"+previousDataArray[4]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final Boolean[] add_mod = {true};
        modSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_mod[0]) {
                    add_mod[0] = false;
                    modSwitcher.setImageResource(R.drawable.off);
                    switchLabel.setText("覆写模式");
                    switchLabel.setTextColor(Color.RED);
                }
                else {
                    add_mod[0] = true;
                    modSwitcher.setImageResource(R.drawable.on);
                    switchLabel.setText("增加模式");
                    switchLabel.setTextColor(Color.BLACK);
                }
            }
        });

        String[] finalPreviousDataArray = previousDataArray;
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pill1, pill2, pill3, pill4, pill5;

                pill1 = input_pill_1.getText().toString().isEmpty() ? "0" : input_pill_1.getText().toString();
                pill2 = input_pill_2.getText().toString().isEmpty() ? "0" : input_pill_2.getText().toString();
                pill3 = input_pill_3.getText().toString().isEmpty() ? "0" : input_pill_3.getText().toString();
                pill4 = input_pill_4.getText().toString().isEmpty() ? "0" : input_pill_4.getText().toString();
                pill5 = input_pill_5.getText().toString().isEmpty() ? "0" : input_pill_5.getText().toString();

                String[] stringArray = {pill1,pill2,pill3,pill4,pill5};
                if (numberDetector(stringArray)){
                    if (add_mod[0]){
                        pill1 = String.valueOf((Integer.parseInt(pill1) + Integer.parseInt(finalPreviousDataArray[0])));
                        pill2 = String.valueOf((Integer.parseInt(pill2) + Integer.parseInt(finalPreviousDataArray[1])));
                        pill3 = String.valueOf((Integer.parseInt(pill3) + Integer.parseInt(finalPreviousDataArray[2])));
                        pill4 = String.valueOf((Integer.parseInt(pill4) + Integer.parseInt(finalPreviousDataArray[3])));
                        pill5 = String.valueOf((Integer.parseInt(pill5) + Integer.parseInt(finalPreviousDataArray[4])));
                    }

                    String text = pill1 + "/" + pill2 + "/" + pill3+ "/" + pill4+ "/" + pill5;

                    try {
                        memory.save_file(text);
                        readPillNumber.refreshNumber(new Date().getTime());
                        refresh_Textview();
                        Toast.makeText(view.getContext(), "药品数量设置完成" + memory.getFile().getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(view.getContext(), "输入失败，请在填入数字后再次尝试", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(view.getContext(),"取消药品数量初始化",Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (memory.hasFile())
            this.refresh_Textview();
    }

    public boolean numberDetector(String[] stringArray){
        for (String string : stringArray){
            if (!string.matches("[0-9]+")){
                return false;
            }
        }
        return true;
    }
}