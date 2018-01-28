package christian.ispyai.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import christian.ispyai.MainActivity;
import christian.ispyai.R;

/**
 * Created by christianmaschka on 27/01/2018.
 */

public class MissionFragment extends BaseFragment {

    public static MissionFragment create() {
        return new MissionFragment();
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_mission, container, false);

        String mission = ((MainActivity) getActivity()).getMission();

        TextView Current_Mission = view.findViewById(R.id.TVMissionText);
        Current_Mission.setText(mission);

        return view;
    }

    public void updateMission(String mission) {
        TextView Current_Mission = view.findViewById(R.id.TVMissionText);
        Current_Mission.setText(mission);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_mission;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }
}
