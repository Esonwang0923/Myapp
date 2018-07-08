package com.example.administrator.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.myapplication.utils.AudioRecoderUtils;

import yalantis.com.sidemenu.interfaces.ScreenShotable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DefaultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DefaultFragment extends Fragment implements ScreenShotable {
    // TODO: Rename parameter arguments, choose names that match
    // TODO: Rename and change types of parameters
    private int res;
    private static String userID;
    private static String tagName;
    private ImageView mImageView,imageView;
    private View containerView;
    private Bitmap bitmap;
    private Button button,endbutton;
    private AudioRecoderUtils audioRecoderUtils;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DefaultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DefaultFragment newInstance(int resId, String userId,String name) {
        userID = userId;
        tagName = name;
        DefaultFragment fragment = new DefaultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        bundle.putString("userId",userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            res = getArguments().getInt(Integer.class.getName());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container_default);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_default, container, false);

        mImageView = (ImageView) rootView.findViewById(R.id.image_detailcontent);
        imageView = (ImageView) rootView.findViewById(R.id.progress);
        button = (Button) rootView.findViewById(R.id.startRecord);
        endbutton = (Button) rootView.findViewById(R.id.endRecord);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }

                setLevel(5400);
                audioRecoderUtils = new AudioRecoderUtils();
                audioRecoderUtils.startRecord();
            }
        });

        endbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setLevel(5400);
                audioRecoderUtils.stopRecord();

            }
        });


        mImageView.setClickable(true);
        mImageView.setFocusable(true);
        mImageView.setImageResource(res);

        return rootView;
    }


    public void setLevel(int level) {
        imageView.getDrawable().setLevel(3000 + 6000 * level / 100);
    }


    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                        containerView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                containerView.draw(canvas);
                DefaultFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
