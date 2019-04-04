package com.example.admin.godzillaandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.admin.godzillaandroid.comm.Constants;
import com.example.admin.godzillaandroid.comm.FruitAdapter;
import com.example.admin.godzillaandroid.comm.OkHttp;
import com.example.admin.godzillaandroid.dao.Task;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link taskListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link taskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class taskListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private List<Task> fruitList = new ArrayList<>();

    private ListView listView;

    public taskListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment taskListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static taskListFragment newInstance(String param1, String param2) {
        taskListFragment fragment = new taskListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Fragment 1", "onCreateView");
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onViewCreated(final View view,Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment 1", "onViewCreated");
        final Handler handler1 = new Handler();
        new Thread(){//开启子线程
            @Override
            public void run() {
                try {

                    while (true){
                        Thread.sleep(5000);
                        //子线程中执行
                        OkHttp okHttp = new OkHttp();
                        String url = Constants.findNotesByUserId+Long.valueOf(mParam1);
                        final String reslut = okHttp.getDatasync(url);
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                //执行Ui线程
                                initFruits(reslut); // 初始化水果数据
                                final FruitAdapter adapter = new FruitAdapter(getContext(), R.layout.list_item, fruitList);
                                listView = (ListView) view.findViewById(R.id.listView);
                                listView.setAdapter(adapter);
                                //长按事件
                                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                        Task task = (Task)adapter.getItem(position);
                                        final String noteId = task.getId();
                                        AlertDialog alertDialog2 = new AlertDialog.Builder(getContext())
                                                .setTitle("确定要删除")
                                                .setMessage(""+ task.getName())
                                                .setIcon(R.drawable.gdp)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener(){//添加"Yes"按钮
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        JSONObject jsonObj = new JSONObject();
                                                        jsonObj.put("id", noteId);
                                                        Call call = new OkHttp(Constants.deleteNote + noteId,jsonObj.toString()).getCall();
                                                        call.enqueue(new Callback() {
                                                            @Override
                                                            public void onFailure(Call call, IOException e) {
                                                            }
                                                            @Override
                                                            public void onResponse(Call call, Response response) throws IOException {
                                                                if(response.isSuccessful()){
                                                                    Looper.prepare();
                                                                    Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                                                                    Looper.loop();
                                                                }
                                                            }
                                                        });

                                                    }
                                                })
                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Toast.makeText(getContext(), "这是取消按钮", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .create();
                                        alertDialog2.show();
                                        return true;
                                    }
                                });
                            }
                        });
                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

            }
        }.start();
    }

    private void initFruits(String reslut) {
        JSONArray jsonArray = JSON.parseArray(reslut);
        fruitList.removeAll(fruitList);
        for(int  i= 0;i<jsonArray.size();i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String content = (String) jsonObject.get("content");
            String id = (String) String.valueOf(jsonObject.get("id"));
            String date = String.valueOf(jsonObject.get("date"));
            Task apple = new Task(id,content,date, R.drawable.main);
            fruitList.add(apple);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment 1", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Fragment 1", "onStart");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
