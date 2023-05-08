package com.example.directional_step_counter;

import android.view.View;
import android.widget.TextView;

/*
A direction class contains all necessary information in one direction,
in this project , only 8 directions will be used.

class name: direction
component:  int directionID     the internal ID of a direction, easy for finding
            int steps           Count number of steps walked on this direction
            TextView directionNameView      the textView box displays name of this direction (eg. North, South)
            TextView directionNumberView    the textView box displays counted steps of this direction
            Boolean isSelected      true if the directionNameView be clicked
 */
public class Direction {
    int directionID;
    int steps=0;
    public TextView directionNameView;
    public TextView directionNumberView;
    Boolean isSelected=false;

    /*
    initialize value and components into class
    set the onClick event for directionNameView
    run editStepText() to change display value in directionNumberView

    input: Void
    output: Void
    Changes: initialize value for all saved components, and also create a onClickListener for directionNameView
     */
    public void init(int ID, TextView nameView, TextView numberView){
        directionID=ID;
        directionNameView=nameView;
        directionNumberView=numberView;
        directionNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelected=true;
            }
        });
        editStepText();
    }

    /*
    changes steps into inputted value, and refresh textView box's display by editStepText()

    input: int value
    output: Void
    changes: update steps' value
     */
    public void setSteps(int value){
        steps=value;
        editStepText();
    }

    /*
    refresh textView box's display with value in steps

    input: Void
    output: Void
    Changes: value displayed in directionNumberView box
     */
    public void editStepText(){
        directionNumberView.setText(String.valueOf(steps));
    }
}
