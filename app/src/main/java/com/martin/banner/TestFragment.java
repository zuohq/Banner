package com.martin.banner;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    public static final String ARG_IMAGE_PATH = "arg_image_path";
    private static final String TAG = TestFragment.class.getSimpleName();

    @InjectView(R.id.image)
    ImageView mImage;

    public TestFragment() {
        // Required empty public constructor
    }

    public static TestFragment newInstance(String path) {
        TestFragment fragment = new TestFragment();

        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_PATH, path);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String path = getArguments().getString(ARG_IMAGE_PATH);
        Log.i(TAG, "path =" + path);

        Glide.with(this).load(path).into(mImage);
    }
}
