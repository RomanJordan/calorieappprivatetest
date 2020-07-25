package com.example.calorieteststuff;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends DialogFragment {

    private EditText foodNameInput, foodCaloriesInput;
    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_item, null);

//        builder.setView(inflater.inflate(R.layout.fragment_add_item, null));
        builder.setView(view);
//        foodNameInput = getView().findViewById(R.id.enterFoodName);
//        foodCaloriesInput = getView().findViewById(R.id.enterFoodCalories);
        foodNameInput = view.findViewById(R.id.enterFoodName);
        foodCaloriesInput = view.findViewById(R.id.enterFoodCalories);
        builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("onclick","on click dialog");
                String foodString = foodNameInput.getText().toString();
                String calories = foodCaloriesInput.getText().toString();
//                Log.d("foodString", ""+foodString);
                listener.applyTexts(foodString, calories);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+
                    "Forgot Implement DialogListener!!!!");
        }
    }

    public interface DialogListener {
        void applyTexts(String foodName, String calories);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_item, container, false);
//    }



}