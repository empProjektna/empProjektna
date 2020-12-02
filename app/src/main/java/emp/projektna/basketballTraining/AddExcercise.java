package emp.projektna.basketballTraining;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddExcercise extends Fragment {


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_excercise, container, false);
        String userID = getArguments().getString("id");
        Spinner spinner = (Spinner) view.findViewById(R.id.exercise_type_sp);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.exercise_type, android.R.layout.simple_spinner_item);


        Toolbar toolbar = view.findViewById(R.id.add_exercise_toolbar);
        //toolbar.inflateMenu(R.menu.add_exercise_toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //Button saveButton = (Button) toolbar.findViewById(R.id.btn_exercise_add);
        /*
         *  Text views
         */
        TextView tw_exercise_name = (TextView) view.findViewById(R.id.tw_exercise_name);
        TextView tw_exercise_length = (TextView) view.findViewById(R.id.tw_exercise_length);
        TextView tw_exercise_description = (TextView) view.findViewById(R.id.tw_exercise_description);
        TextView tw_exercise_position = (TextView) view.findViewById(R.id.tw_exercise_position);
        TextView tw_exercise_repeats = (TextView) view.findViewById(R.id.tw_exercise_repeats);

        /*
         * Edit texts
         */

        EditText et_exercise_name = (EditText) view.findViewById(R.id.et_exercise_name);
        EditText et_exercise_length = (EditText) view.findViewById(R.id.et_exercise_length);
        EditText et_exercise_description = (EditText) view.findViewById(R.id.et_exercise_description);
        EditText et_exercise_position = (EditText) view.findViewById(R.id.et_exercise_position);
        EditText et_exercise_repeats = (EditText) view.findViewById(R.id.et_exercise_repeats);


        LinearLayout linearLayout = (LinearLayout) view. findViewById(R.id.position_layout);

        /*
         * Check box
         * */

        CheckBox cb_exercise_timer = (CheckBox) view.findViewById(R.id.cb_exercise_timer);


        /*
         * Buttons
         * */
        Button btn_create_exercise = (Button) view.findViewById(R.id.btn_add_exercise);
        Button btn_finish_training = (Button) view.findViewById(R.id.btn_finish_training);

        Training training = new Training();

        /*
        btn_create_exercise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String description = et_exercise_description.getText().toString();
                et_exercise_description.setText("",TextView.BufferType.EDITABLE);

                String name = et_exercise_name.getText().toString();
                et_exercise_name.setText("",TextView.BufferType.EDITABLE);

                int length = Integer.parseInt(et_exercise_length.getText().toString());
                et_exercise_length.setText("",TextView.BufferType.EDITABLE);

                int position = Integer.parseInt(et_exercise_position.getText().toString());
                et_exercise_position.setText("",TextView.BufferType.EDITABLE);

                int repeats = Integer.parseInt(et_exercise_repeats.getText().toString());
                et_exercise_repeats.setText("",TextView.BufferType.EDITABLE);

                boolean timer = cb_exercise_timer.isSelected();

                Exercise exercise = new Exercise(name,length,position,description,repeats,timer);
                //training.addTraining(exercise);
                spinner.setSelection(0);

            }
        });
        */

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_LONG).show();
                if (position == 0) {
                    tw_exercise_name.setVisibility(View.INVISIBLE);
                    tw_exercise_length.setVisibility(View.INVISIBLE);
                    tw_exercise_description.setVisibility(View.INVISIBLE);
                    tw_exercise_position.setVisibility(View.INVISIBLE);
                    tw_exercise_repeats.setVisibility(View.INVISIBLE);
                    et_exercise_name.setVisibility(View.INVISIBLE);
                    et_exercise_length.setVisibility(View.INVISIBLE);
                    et_exercise_description.setVisibility(View.INVISIBLE);
                    et_exercise_position.setVisibility(View.INVISIBLE);
                    et_exercise_repeats.setVisibility(View.INVISIBLE);
                    cb_exercise_timer.setVisibility(View.INVISIBLE);
                    btn_create_exercise.setVisibility(View.INVISIBLE);
                    //if(training.numberOfExercises() != 0)
                      //  btn_finish_training.setVisibility(View.VISIBLE);
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

                    btn_finish_training.setVisibility(View.INVISIBLE);

                    if(position == 1) {
                        tw_exercise_position.setVisibility(View.VISIBLE);
                        et_exercise_position.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        //tw_exercise_position.setVisibility(View.GONE);
                        //et_exercise_position.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                    }
                    btn_create_exercise.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_exercise_save:
                        String description = et_exercise_description.getText().toString();
                        et_exercise_description.setText("",TextView.BufferType.EDITABLE);

                        String name = et_exercise_name.getText().toString();
                        et_exercise_name.setText("",TextView.BufferType.EDITABLE);

                        int length = Integer.parseInt(et_exercise_length.getText().toString());
                        et_exercise_length.setText("",TextView.BufferType.EDITABLE);

                        int position = Integer.parseInt(et_exercise_position.getText().toString());
                        et_exercise_position.setText("",TextView.BufferType.EDITABLE);

                        int repeats = Integer.parseInt(et_exercise_repeats.getText().toString());
                        et_exercise_repeats.setText("",TextView.BufferType.EDITABLE);

                        boolean timer = cb_exercise_timer.isSelected();

                        ModelExercise exercise = new ModelExercise(name,length,position,description,repeats,timer);
                        training.addExercise(exercise, userID);
                        break;

                }
                return true;

            }
        });



        return view;
    }


}