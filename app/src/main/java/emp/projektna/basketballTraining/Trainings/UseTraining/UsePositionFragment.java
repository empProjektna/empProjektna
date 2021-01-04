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
import java.util.HashMap;
import java.util.Map;

import emp.projektna.basketballTraining.AddTraining.AddExerciseFragment;
import emp.projektna.basketballTraining.R;


public class UsePositionFragment extends DialogFragment {
    private  Toolbar toolbar;

    private ArrayList<Integer> shootPositions = new ArrayList<>();
    private Map<Integer, String> inputedShots = new HashMap<>();
    private Map<Integer, String> inputedScored = new HashMap<>();
    private Button[] gumbeki = new Button[25];
    private int currentSelected = -1;
    private TextView shots, scored;
    static UsePositionFragment newInstance() {
        return new UsePositionFragment();
    }

    public static UsePositionFragment getInstance() {
        UsePositionFragment fragment = new UsePositionFragment();
        return fragment;
    }

    private void setOnClick(final Button btn, final int i){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelected != -1) {
                    inputedShots.put(currentSelected, shots.getText().toString());
                    inputedScored.put(currentSelected, scored.getText().toString());
                    if (inputedScored.containsKey(i) && inputedShots.containsKey(i)) {
                        shots.setText(inputedShots.get(i));
                        scored.setText(inputedScored.get(i));
                    }
                }
                currentSelected = i;
                if (inputedShots.containsKey(i) && inputedScored.containsKey(i)) {
                    gumbeki[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_pin_button_inputed));
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        if (mArgs != null && mArgs.getIntegerArrayList("selected") != null)
            shootPositions = mArgs.getIntegerArrayList("selected");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_use_position, container, false);

        ConstraintLayout constraintLayout = view.findViewById(R.id.gumbeki);
        shots = view.findViewById(R.id.shots);
        scored = view.findViewById(R.id.scored);
        for (int i = 0; i < gumbeki.length; i++) {
            gumbeki[i] = (Button) constraintLayout.getChildAt(i);
            setOnClick(gumbeki[i], i);
            Log.e("bla", shootPositions.toString());
            gumbeki[i].setVisibility(View.INVISIBLE);

        }
        for (int j = 0; j < shootPositions.size(); j++) {
            int x  = Integer.parseInt(String.valueOf(shootPositions.get(j)));
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
                    .setLayout((int) (getScreenWidth(getActivity()) * .93), (int) (getScreenHeight(getActivity()) * .57));
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
        Intent intent = AddExerciseFragment.newIntent(shootPositions);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
}