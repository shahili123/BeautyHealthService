package uk.ac.tees.w9598552.beautyhealthservice.Fragments.UserFragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.w9598552.beautyhealthservice.R;
import uk.ac.tees.w9598552.beautyhealthservice.Recipes;
import uk.ac.tees.w9598552.beautyhealthservice.viewholder_recipes;

public class fragment_user_recipie extends Fragment {


    private RecyclerView recyclerView;
    TextView txt_no_result;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ProgressBar progressBar;
    viewholder_recipes adapter_recipes;

    ImageView btn_search;
    ArrayList<Recipes> list=new ArrayList<>();
    private RequestQueue volley_request_queue;
    private StringRequest volley_stringrequest;
    EditText txt_search;

    public fragment_user_recipie() {
    }

    public static fragment_user_recipie newInstance(String param1, String param2) {
        fragment_user_recipie fragment = new fragment_user_recipie();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_recipie, container, false);
        try {
            recyclerView = root.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            txt_no_result=root.findViewById(R.id.txt_no_result);

            progressBar=root.findViewById(R.id.progress_circular);
            txt_search=root.findViewById(R.id.txt_search);
            btn_search=root.findViewById(R.id.btn_search);
            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    search_recipe(txt_search.getText().toString());
                }
            });



        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return root;
    }

    public void search_recipe(String name){
        volley_request_queue = Volley.newRequestQueue(getActivity());
        volley_stringrequest = new StringRequest(Request.Method.GET, "https://edamam-recipe-search.p.rapidapi.com/search?q="+name, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    try{
                        JSONObject main_json = new JSONObject(response);
                        String hits_configuration=main_json.getString("hits");
                        JSONArray data = new JSONArray(hits_configuration);
                        for (int index = 0; index < data.length(); index++) {
                            JSONObject obj = data.getJSONObject(index);

                            String temp=obj.getString("recipe");

                            JSONObject jsonObject=new JSONObject(temp);

                            String title=jsonObject.getString("label");
                            String image=jsonObject.getString("image");
                            String url=jsonObject.getString("url");

                            list.add(new Recipes(title,image,url));


                        }
                        if(list.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter_recipes = new viewholder_recipes(getContext(), list);
                            recyclerView.setAdapter(adapter_recipes);
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            txt_no_result.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }


                    }
                    catch (Exception e){

                        Log.i("Res",e.toString());


                    }

                } catch (Exception e) {
                    Log.i("Error",e.getMessage());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   Toast.makeText(ActivitySplash.this, error.getMessage(),Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-RapidAPI-Key", "03fb9990b1msh6d385901f97c600p19ff76jsn628843e71c3d");
                return params;
            }
        };
        volley_stringrequest.setRetryPolicy(new DefaultRetryPolicy(60000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volley_request_queue.add(volley_stringrequest);

    }


}


