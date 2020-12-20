package emp.projektna.basketballTraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import emp.projektna.basketballTraining.SearchUser.SearchUserActivity;

public class FeedFragment extends Fragment {


    RecyclerView recyclerView;
    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        Toolbar toolbar = view.findViewById(R.id.top_of_row_feed);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.feed_search:
                        Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                        startActivity(intent);
                }
                return true;

            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapterFeed = new AdapterFeed(getActivity(), modelFeedArrayList);

        recyclerView.setAdapter(adapterFeed);

        populateRecyclerView();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_feed, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    // TODO: iz baze črpaj podatke in nafilaj RecyclerView
    public void  populateRecyclerView() {
        ModelFeed modelFeed = new ModelFeed(1, 5, 100, 100, 60, "50m 16s","Andraž Anderle", "8:00");
        modelFeedArrayList.add(modelFeed);
        modelFeed = new ModelFeed(2, 10, 42, 100, 50, "60m 42s", "Aleksandar Georgiev", "8:00");
        modelFeedArrayList.add(modelFeed);
        adapterFeed.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
