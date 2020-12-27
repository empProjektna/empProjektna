package emp.projektna.basketballTraining.AddTraining;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import emp.projektna.basketballTraining.R;


public class SelectPositionFragment extends DialogFragment {
    private  Toolbar toolbar;

    private ArrayList<Integer> shootPositions = new ArrayList<>();
    private Button[] gumbeki = new Button[25];

    static SelectPositionFragment newInstance() {
        return new SelectPositionFragment();
    }

    public static SelectPositionFragment getInstance() {
        SelectPositionFragment fragment = new SelectPositionFragment();
        return fragment;
    }

    private void setOnClick(final Button btn, final int i){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shootPositions.contains(i)) {
                    shootPositions.remove(new Integer(i));
                    gumbeki[i].setBackground(getActivity().getResources().getDrawable(R.drawable.round_pin_button_unselected));
                }
                else {
                    shootPositions.add(i);
                    gumbeki[i].setBackground(getActivity().getResources().getDrawable(R.drawable.round_pin_button_selected));
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        if (mArgs.getIntegerArrayList("selected") != null)
            shootPositions = mArgs.getIntegerArrayList("selected");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_position, container, false);

        ConstraintLayout constraintLayout = view.findViewById(R.id.gumbeki);

        for (int i = 0; i < gumbeki.length; i++) {
            gumbeki[i] = (Button) constraintLayout.getChildAt(i);
            setOnClick(gumbeki[i], i);

            if (shootPositions.contains(new Integer(i))) {
                gumbeki[i].setBackground(getActivity().getResources().getDrawable(R.drawable.round_pin_button_selected));
            }
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
        super.onStop();

        sendResult();
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