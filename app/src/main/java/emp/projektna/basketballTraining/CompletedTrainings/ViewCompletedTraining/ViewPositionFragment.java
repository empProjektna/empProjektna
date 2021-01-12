package emp.projektna.basketballTraining.CompletedTrainings.ViewCompletedTraining;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import emp.projektna.basketballTraining.R;
import emp.projektna.basketballTraining.Trainings.UseTraining.UseTrainingFragment;


public class ViewPositionFragment extends DialogFragment {
    private  Toolbar toolbar;

    private ArrayList<Integer> shootPositions = new ArrayList<>();
    private Map<String, String> inputedShots = new HashMap<>();
    private Map<String, String> inputedScored = new HashMap<>();
    private Button[] gumbeki = new Button[25];
    private Integer currentSelected = -1;
    private int position = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String completedTrainingID;
    private TextView shots, scored;

    static ViewPositionFragment newInstance() {
        return new ViewPositionFragment();
    }

    public static ViewPositionFragment getInstance() {
        ViewPositionFragment fragment = new ViewPositionFragment();
        return fragment;
    }

    private void setOnClick(final Button btn, final int i){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gumbeki[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_pin_button_inputed));
                if (currentSelected != -1)
                    gumbeki[currentSelected].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_pin_button_selected));
                currentSelected = i;
                shots.setText(inputedShots.get(String.valueOf(i)));
                scored.setText(inputedScored.get(String.valueOf(i)));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        if (mArgs != null && mArgs.containsKey("index") && mArgs.containsKey("ID")) {
            position = mArgs.getInt("index");
            completedTrainingID = mArgs.getString("ID");
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_use_position, container, false);

        ConstraintLayout constraintLayout = view.findViewById(R.id.gumbeki);
        shots = view.findViewById(R.id.shots);
        shots.setFocusable(false);
        shots.setClickable(false);


        scored = view.findViewById(R.id.scored);
        scored.setFocusable(false);
        scored.setClickable(false);
        String x;
        db.collection("CompletedTrainings").document(completedTrainingID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Object x = task.getResult().get("scored");
                    Object y = task.getResult().get("shots");
                    if (((HashMap) x).containsKey(String.valueOf(position))) {
                       inputedScored = (Map<String, String>) ((HashMap) x).get(String.valueOf(position));
                       inputedShots = (Map<String, String>) ((HashMap) y).get(String.valueOf(position));

                        for (int i = 0; i < gumbeki.length; i++) {
                            gumbeki[i] = (Button) constraintLayout.getChildAt(i);
                             x = String.valueOf(i);
                            if (!inputedShots.containsKey(x))
                                gumbeki[i].setVisibility(View.INVISIBLE);
                            else {
                                setOnClick(gumbeki[i], i);
                                gumbeki[i].setVisibility(View.VISIBLE);
                                gumbeki[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_pin_button_selected));
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < gumbeki.length; i++) {
                            gumbeki[i] = (Button) constraintLayout.getChildAt(i);
                            gumbeki[i].setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });

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