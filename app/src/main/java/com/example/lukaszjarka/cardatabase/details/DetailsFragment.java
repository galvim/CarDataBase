package com.example.lukaszjarka.cardatabase.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lukaszjarka.cardatabase.Car;
import com.example.lukaszjarka.cardatabase.MotoDatabaseOpenHelper;
import com.example.lukaszjarka.cardatabase.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DetailsFragment extends Fragment {

    @BindView(R.id.make_and_model_fragment)
    TextView makeAndModel;

    @BindView(R.id.year_fragment)
    TextView year;
    @BindView(R.id.detail_image_view)
    ImageView detailImage;


    private static final String CAR_ID_KEY = "car_id_key" ;



    private MotoDatabaseOpenHelper openHelper;

    private Unbinder unbinder;

    public static Fragment getInstance(String carID) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(CAR_ID_KEY, carID);
        detailsFragment.setArguments(arguments);
        return detailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openHelper = new MotoDatabaseOpenHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
       String carId =  getArguments().getString(CAR_ID_KEY);
        Car car = openHelper.getCarWithId(carId);
        makeAndModel.setText("Marka " + car.getMake() + " Model: " + car.getModel());
        year.setText("Rocznik: " + car.getYear());
        Glide.with(getActivity()).load(car.getImage()).into(detailImage);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}