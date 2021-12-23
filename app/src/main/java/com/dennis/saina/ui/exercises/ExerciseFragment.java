package com.dennis.saina.ui.exercises;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.dennis.saina.R;

import com.dennis.saina.databinding.FragmentExerciseBinding;
import com.dennis.saina.ui.Lesson;
import com.dennis.saina.ui.LessonData;
import com.dennis.saina.ui.adapters.ExercisesAdapter;
import com.dennis.saina.ui.adapters.MyAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class ExerciseFragment extends Fragment {

    private ExerciseViewModel mViewModel;
    FirebaseFirestore db;
    ArrayList<Lesson> lessonArrayList;
    ExercisesAdapter exercisesAdapter;
    MyAdapter myAdapter;
    FragmentExerciseBinding binding;

    private RecyclerView dataList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentExerciseBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        lessonArrayList = new ArrayList<>();
        myAdapter = new MyAdapter(getContext(), lessonArrayList);
        exercisesAdapter = new ExercisesAdapter(getContext(), lessonArrayList);


        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();


        binding.readyBtn.setOnClickListener(v -> {

            binding.readyBtn.setVisibility(View.GONE);
            binding.Name.setText(R.string.quiz_question);

            //For each lesson separate images and names
            int i = 0;
            for (Lesson lesson : lessonArrayList) {
                names.add(lesson.getName());
                images.add(lesson.getImage());
                i++;
            }

            //generate random numbers
            int[] radioButtonAnswers = generateQuestions();
            //here the first one is correct answer

            Glide.with(getContext())
                    .load(images.get(radioButtonAnswers[0]))
                    .into(binding.Image);


            //randomize answer
            Random random = new Random(3);
            Log.i("random", " " + random.toString());

            binding.firstAnswer.setText("" + names.get(radioButtonAnswers[0]));
            binding.secondAnswer.setText("" + names.get(radioButtonAnswers[1]));
            binding.thirdAnswer.setText("" + names.get(radioButtonAnswers[2]));


//
//            newArr[correctLocated]= radioButtonAnswers[0];
//            newArr[other1]=other1;
//            newArr[other2]=other2;


//            String a;
//
//            binding.firstAnswer.setText(""+names.get(newArr[correctLocated]));
//            binding.secondAnswer.setText(""+names.get(newArr[other1]));
//            binding.thirdAnswer.setText(""+names.get(newArr[other2]));


            Log.i("MSGt", "" + names.get(radioButtonAnswers[0]));

            //


        });

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Fetching Data ...");
        progressDialog.show();

        Thread thread = new Thread() {
            public void run() {
                LessonData.EventChangeListener(getContext(), myAdapter, lessonArrayList, progressDialog);
            }
        };
        thread.start();


        return binding.getRoot();


    }

    private int[] generateQuestions() {
        int[] questions = new int[3];
        Random rand = new Random();
        questions[0] = rand.nextInt(lessonArrayList.size());//correct answer
        questions[1] = rand.nextInt(lessonArrayList.size());
        questions[2] = rand.nextInt(lessonArrayList.size());

        if (questions[1] == questions[0]) {
            questions[1] = rand.nextInt(lessonArrayList.size());
        }
        if (questions[2] == questions[0]) {
            questions[2] = rand.nextInt(lessonArrayList.size());
        }
        if (questions[2] == questions[1]) {
            questions[2] = rand.nextInt(lessonArrayList.size());
        }


        return questions;
    }


}