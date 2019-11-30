package com.jaemo.jaemo.customer.drawer_fragments.your_rides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jaemo.jaemo.R;

public class YourRidesFragment extends Fragment {

    private YourRidesViewModel yourRidesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        yourRidesViewModel =
                ViewModelProviders.of(this).get(YourRidesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_your_rides, container, false);
        final TextView textView = root.findViewById(R.id.text);
        yourRidesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}