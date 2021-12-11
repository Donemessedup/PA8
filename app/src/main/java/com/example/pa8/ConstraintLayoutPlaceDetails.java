package com.example.pa8;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class ConstraintLayoutPlaceDetails extends ConstraintLayout {

    public ConstraintLayoutPlaceDetails(@NonNull Context context) {
        super(context);

        //Set attributes
        this.setId(View.generateViewId());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        //Set the constraint set
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        setConstraints(context, constraintSet);

        //Apply constraint set
        constraintSet.applyTo(this);
    }

    void setConstraints(Context context, ConstraintSet constraintSet){

        //Name
        TextView nT = new TextView(context);
        nT.setId(View.generateViewId());
        nT.setText("Name of place PLACEHOLDER");
        addView(nT);

        //Address
        TextView aT = new TextView(context);
        aT.setId(View.generateViewId());
        aT.setText("Address of place PLACEHOLDER");
        addView(aT);

        //Phone numbers
        TextView pT = new TextView(context);
        pT.setId(View.generateViewId());
        pT.setText("Phone Number of place PLACEHOLDER");
        addView(pT);

        //Whether the place is open or not
        TextView oT = new TextView(context);
        oT.setId(View.generateViewId());
        oT.setText("Open of place PLACEHOLDER");
        addView(oT);

        //A review
        TextView rT = new TextView(context);
        rT.setId(View.generateViewId());
        rT.setText("Review of place PLACEHOLDER");
        addView(rT);

        //Image
        ImageView iV = new ImageView(context);
        iV.setId(View.generateViewId());
        iV.setBackgroundColor(Color.rgb(0, 204, 204));
        addView(iV);

        //Name Constraints - nT
        constraintSet.constrainWidth(nT.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(nT.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(nT.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(nT.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(nT.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(nT.getId(), ConstraintSet.BOTTOM, aT.getId(), ConstraintSet.TOP);

        //Address constraints - aT
        constraintSet.constrainWidth(aT.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(aT.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(aT.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(aT.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(aT.getId(), ConstraintSet.TOP, nT.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(aT.getId(), ConstraintSet.BOTTOM, pT.getId(), ConstraintSet.TOP);

        //Phone Number Constraints - pT
        constraintSet.constrainWidth(pT.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(pT.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(pT.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(pT.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(pT.getId(), ConstraintSet.TOP, aT.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(pT.getId(), ConstraintSet.BOTTOM, oT.getId(), ConstraintSet.TOP);

        //Open/Closed Constraints - oT
        constraintSet.constrainWidth(oT.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(oT.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(oT.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(oT.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(oT.getId(), ConstraintSet.TOP, pT.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(oT.getId(), ConstraintSet.BOTTOM, rT.getId(), ConstraintSet.TOP);

        //Review Constraints - rT
        constraintSet.constrainWidth(rT.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(rT.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(rT.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(rT.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(rT.getId(), ConstraintSet.TOP, oT.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(rT.getId(), ConstraintSet.BOTTOM, oT.getId(), ConstraintSet.TOP);

        //Image Constraints - iV
        constraintSet.constrainWidth(iV.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(iV.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.connect(iV.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(iV.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(iV.getId(), ConstraintSet.TOP, rT.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(iV.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);


    }

}
