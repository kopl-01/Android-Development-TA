package com.example.innstant.ui.RoomListed;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.innstant.R;
import com.example.innstant.data.PreferenceHelper;
import com.example.innstant.data.model.Room;
import com.example.innstant.ui.HostRoom.Adapter.AdapterRoomHosting;
import com.example.innstant.ui.HostRoom.Model.ModelHost;
import com.example.innstant.ui.RoomListed.Adapter.adapterListedRoom;
import com.example.innstant.viewmodel.ListerRoomVewModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class ListedRoomActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterRoomHosting.OnItemClickListener, adapterListedRoom.OnItemClickListener {
    private ListerRoomVewModel mViewModel;
    RecyclerView recyclerView;
    adapterListedRoom adapter;
    ArrayList<Room> list;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_room);

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.listroom);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this).get(ListerRoomVewModel.class);
        //fetching data
        GetData();
    }

    @Override
    public void onItemClick(ModelHost item) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onItemClick(Room item) {

    }
    public ArrayList<Room> GetData()  {
        mViewModel.openServerConnection();
        RequestQueue requstQueue = Volley.newRequestQueue(this);
        String url = PreferenceHelper.getBaseUrl() + "/rooms";

        JsonArrayRequest jsonobj = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(ListedRoomActivity.this,"berhasil    :"+response,Toast.LENGTH_LONG).show();
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Room room =new Room();
                              /*  movie.setTitle(jsonObject.getString(
                                        "title"));
                                movie.setRating(jsonObject.getInt(
                                        "rating"));*/
                                room.setName(jsonObject.getString("name"));
                                room.setPrice(jsonObject.getString("price"));
                                room.setType(jsonObject.getString("type"));
                                room.setLocation(jsonObject.getString("location"));
                                list.add(room);
//                                Toast.makeText(ListedRoomActivity.this,"berhasil    :"+list,Toast.LENGTH_LONG).show();
                                //get adapter
                                adapter = new adapterListedRoom(ListedRoomActivity.this,list, ListedRoomActivity.this);
                                recyclerView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                          }
                        }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListedRoomActivity.this,"gagal     :"+error.toString(),Toast.LENGTH_LONG).show();
                      }

                }

        ){
            //here I want to post data to sever
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
//                    headers.put("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzZWN1cmUtYXBpIiwiYXVkIjoic2VjdXJlLWFwcCIsInN1YiI6InJvaG1hdDY2MUBnbWFpbC5jb20iLCJleHAiOjE1NjI2NjM1NjksInJvbGUiOlsiVVNFUiJdfQ.6mGlnlu0lWHuOZLmy_I4IYOD5BJKc-22fbR0sWO-8j_KQ9Jkk4owJZqpP3yPtvBIiRhD_zRYKm-ew3DPqFrK_A");
                return headers;
            }
        };
        requstQueue.add(jsonobj);
        return list;
    }

}
