package emp.projektna.basketballTraining.Trainings.UseTraining;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import emp.projektna.basketballTraining.R;


public class UsePositionFragment extends DialogFragment {
    private  Toolbar toolbar;

    private ArrayList<Integer> shootPositions = new ArrayList<>();
    private Map<String, String> inputedShots = new HashMap<>();
    private Map<String, String> inputedScored = new HashMap<>();
    private Button[] gumbeki = new Button[25];
    private Integer currentSelected = -1;
    private int position = 0;

    private TextView shots, scored;
    static UsePositionFragment newInstance() {
        return new UsePositionFragment();
    }

    public static UsePositionFragment getInstance() {
        UsePositionFragment fragment = new UsePositionFragment();
        return fragment;
    }

    private void setOnClick(final Button btn, final int i) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shots.setFocusable(true);
                scored.setFocusable(true);
                gumbeki[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_pin_button_inputed));
                if (currentSelected != -1) {
                    if (shots.getText().toString().equals("") || scored.getText().toString().equals(""))
                        gumbeki[currentSelected].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_pin_button_selected));
                    else
                        gumbeki[currentSelected].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_pin_button_inputed));
                    inputedShots.put(String.valueOf(currentSelected), shots.getText().toString());
                    inputedScored.put(String.valueOf(currentSelected), scored.getText().toString());


                    if (inputedScored.containsKey(String.valueOf(i)) && inputedShots.containsKey(String.valueOf(i))) {
                        shots.setText(inputedShots.get(String.valueOf(i)));
                        scored.setText(inputedScored.get(String.valueOf(i)));
                    }
                    else {
                        shots.setText("");
                        scored.setText("");
                    }
                    shots.requestFocus();
                }
                currentSelected = i;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        if (mArgs != null && mArgs.getIntegerArrayList("selected") != null) {
            shootPositions = mArgs.getIntegerArrayList("selected");
            Collections.sort(shootPositions);
            Log.e("pos", String.valueOf(position));
            position = mArgs.getInt("index");
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_use_position, container, false);

        ConstraintLayout constraintLayout = view.findViewById(R.id.gumbeki);
        shots = view.findViewById(R.id.shots);
        //shots.setFocusable(false);

        scored = view.findViewById(R.id.scored);
        //scored.setFocusable(false);
        //btnNext = view.findViewById(R.id.btn_next);
        for (int i = 0; i < gumbeki.length; i++) {
            gumbeki[i] = (Button) constraintLayout.getChildAt(i);
            gumbeki[i].setVisibility(View.INVISIBLE);

        }

        for (int j = 0; j < shootPositions.size(); j++) {
            int x  = Integer.parseInt(String.valueOf(shootPositions.get(j)));
            setOnClick(gumbeki[x], x);
            gumbeki[x].setVisibility(View.VISIBLE);
            gumbeki[x].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_pin_button_selected));
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow()
                    .setLayout((int) (getScreenWidth(getActivity()) * .93), (int) (getScreenHeight(getActivity()) * .75));
        }
    }

    public static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    public static int getScreenHeight(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.y;
    }

    @Override
    public void onStop() {

        inputedShots.put(String.valueOf(currentSelected), shots.getText().toString());
        inputedScored.put(String.valueOf(currentSelected), scored.getText().toString());
        PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()) {
            sendResult();
        }
        super.onStop();


    }

    private void sendResult() {
        if( getTargetFragment() == null ) {
            return;
        }
        Intent intent = UseTrainingFragment.newIntent(inputedShots, inputedScored, position);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
}