package com.jjunji.android.foolog2;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjunji.android.foolog2.util.ITask;
import com.jjunji.android.foolog2.util.Loader;
import com.jjunji.android.foolog2.util.NetworkService;
import com.jjunji.android.foolog2.util.SharedPreferencesDb;
import com.jjunji.android.foolog2.model.AllList;

import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowListFragment extends Fragment implements ITask.createAllFoolog{
    View view;
    Context context;
    RecyclerView recyclerView;
    String send_token;
    Typeface font;
    ListRecyclerViewAdapter adapter;
    TextView textView;

    public ShowListFragment() {
        // Required empty public constructor
    }

    public static ShowListFragment newInstance(Context mContext) {
        Bundle args = new Bundle();
        ShowListFragment fragment = new ShowListFragment();
        fragment.context = mContext;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_list, container, false);
        //initNetwork();
        initView();
        textView.setText("모든 작성글 보기");
        textView.setTypeface(font);
        //setNetwork();
        Loader.allFoologNetwork(send_token, ShowListFragment.this);

        return view;
    }

    private void initView() {
        textView = (TextView) view.findViewById(R.id.textView);
        send_token = SharedPreferencesDb.sendToken(context, "token");
        font = Typeface.createFromAsset(getActivity().getAssets(), "yaFontBold.ttf");
        recyclerView = view.findViewById(R.id.listRecyclerView);
    }

    @Override
    public void showAllList(List<AllList> allLists) {
        adapter = new ListRecyclerViewAdapter(allLists, context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }
}
