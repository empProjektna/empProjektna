package emp.projektna.basketballTraining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TrainingsFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterFeed adapterFeed;
    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trainings, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapterFeed = new AdapterFeed(getActivity(), modelFeedArrayList);

        recyclerView.setAdapter(adapterFeed);

        populateRecyclerView();

        return view;
    }

    // TODO: iz baze črpaj podatke in nafilaj RecyclerView
    public void  populateRecyclerView() {
        ModelFeed modelFeed = new ModelFeed(1, 5, 100, 100, 60, "50m 16s","Andraž Anderle", "8:00");
        modelFeedArrayList.add(modelFeed);
        modelFeed = new ModelFeed(2, 10, 42, 100, 50, "60m 42s", "Aleksandar Georgiev", "8:00");
        modelFeedArrayList.add(modelFeed);
        modelFeed = new ModelFeed(2, 10, 42, 100, 50, "60m 42s", "Aleksandar Georgiev", "8:00");
        modelFeedArrayList.add(modelFeed);
        adapterFeed.notifyDataSetChanged();
    }
}