package emp.projektna.basketballTraining.AddTraining;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import emp.projektna.basketballTraining.R;

public class AddExerciseFragment extends Fragment {


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final int TARGET_FRAGMENT_REQUEST_CODE = 1;

    private ArrayList<Integer> selected = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_excercise, container, false);
        String trainingId = getArguments().getString("id");
        Spinner spinner = (Spinner) view.findViewById(R.id.exercise_type_sp);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.exercise_type, android.R.layout.simple_spinner_item);


        Toolbar toolbar = view.findViewById(R.id.add_exercise_toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        /*
         *  Text views
         */
        TextView tw_exercise_name = (TextView) view.findViewById(R.id.tw_exercise_name);
        TextView tw_exercise_length = (TextView) view.findViewById(R.id.tw_exercise_length);
        TextView tw_exercise_description = (TextView) view.findViewById(R.id.tw_exercise_description);
        TextView tw_exercise_repeats = (TextView) view.findViewById(R.id.tw_exercise_repeats);

        /*
         * Edit texts
         */

        EditText et_exercise_name = (EditText) view.findViewById(R.id.et_exercise_name);
        EditText et_exercise_length = (EditText) view.findViewById(R.id.et_exercise_length);
        EditText et_exercise_description = (EditText) view.findViewById(R.id.et_exercise_description);
        EditText et_exercise_repeats = (EditText) view.findViewById(R.id.et_exercise_repeats);



        /*
         * Check box
         * */

        CheckBox cb_exercise_timer = (CheckBox) view.findViewById(R.id.cb_exercise_timer);


        /*
         * Buttons
         * */

        Button btn_selectPosition = view.findViewById(R.id.add_position);

        btn_selectPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = SelectPositionFragment.getInstance();
                newFragment.setTargetFragment(AddExerciseFragment.this, 1);

                Bundle args = new Bundle();
                args.putIntegerArrayList("selected", selected);
                newFragment.setArguments(args);

                newFragment.show(getFragmentManager(), "dialog");
            }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                }

                if (position == 0) {
                    tw_exercise_name.setVisibility(View.INVISIBLE);
                    tw_exercise_length.setVisibility(View.INVISIBLE);
                    tw_exercise_description.setVisibility(View.INVISIBLE);
                    tw_exercise_repeats.setVisibility(View.INVISIBLE);
                    et_exercise_name.setVisibility(View.INVISIBLE);
                    et_exercise_length.setVisibility(View.INVISIBLE);
                    et_exercise_description.setVisibility(View.INVISIBLE);
                    et_exercise_repeats.setVisibility(View.INVISIBLE);
                    cb_exercise_timer.setVisibility(View.INVISIBLE);
                    btn_selectPosition.setVisibility(View.INVISIBLE);
                } else {
                    tw_exercise_name.setVisibility(View.VISIBLE);
                    tw_exercise_length.setVisibility(View.VISIBLE);
                    tw_exercise_description.setVisibility(View.VISIBLE);
                    tw_exercise_repeats.setVisibility(View.VISIBLE);
                    et_exercise_name.setVisibility(View.VISIBLE);
                    et_exercise_length.setVisibility(View.VISIBLE);
                    et_exercise_description.setVisibility(View.VISIBLE);
                    et_exercise_repeats.setVisibility(View.VISIBLE);
                    cb_exercise_timer.setVisibility(View.VISIBLE);


                    if(position == 1) {
                        btn_selectPosition.setVisibility(View.VISIBLE);
                    }
                    else {
                        btn_selectPosition.setVisibility(View.GONE);
                    }
                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btn_exercise_save) {
                        if (et_exercise_name.getText().toString().equals(""))
                            Toast.makeText(getContext(), "Exercise name is empty", Toast.LENGTH_SHORT).show();
                        else {
                            String description = et_exercise_description.getText().toString();
                            et_exercise_description.setText("", TextView.BufferType.EDITABLE);

                            String name = et_exercise_name.getText().toString();
                            et_exercise_name.setText("", TextView.BufferType.EDITABLE);

                            long length = 0;
                            if (!et_exercise_length.getText().toString().equals(""))
                                length = Integer.parseInt(et_exercise_length.getText().toString());

                            et_exercise_length.setText("", TextView.BufferType.EDITABLE);

                            long repeats = 0;
                            if (!et_exercise_repeats.getText().toString().equals(""))
                                repeats = Integer.parseInt(et_exercise_repeats.getText().toString());
                            et_exercise_repeats.setText("", TextView.BufferType.EDITABLE);

                            boolean timer = cb_exercise_timer.isSelected();

                           ModelExercise exercise = new ModelExercise(name, length, description, repeats, timer, btn_selectPosition.getVisibility() == 0, selected,  spinner.getSelectedItem().toString());
                            exercise.uploadToFirestore(trainingId);

                            SystemClock.sleep(500);
                            getFragmentManager().popBackStack();
                        }
                }
                return true;

            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode != Activity.RESULT_OK ) {
            return;
        }
        if( requestCode == TARGET_FRAGMENT_REQUEST_CODE ) {
            selected = data.getIntegerArrayListExtra("selected");
        }
    }

    public static Intent newIntent(ArrayList<Integer> selected) {
        Intent intent = new Intent();
        intent.putExtra("selected", selected);
        return intent;
    }
}