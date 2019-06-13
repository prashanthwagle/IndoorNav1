package com.example.indoornav1;

import android.content.Context;
import android.content.Intent;
import android.os.BaseBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class NavItem {
    String mTitle;
    String mSubtitle;
    int mIcon;

    public NavItem(String title, String subtitle, int icon) {
        mTitle = title;
        mSubtitle = subtitle;
        mIcon = icon;
    }
}

class DrawerListAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<NavItem> mNavItems;
    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems){
        mContext=context;
        mNavItems=navItems;

    }
    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item,null);

        }else{
            view=convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        titleView.setText( mNavItems.get(position).mTitle );
        subtitleView.setText( mNavItems.get(position).mSubtitle );
        iconView.setImageResource(mNavItems.get(position).mIcon);

        return view;
    }
}

public class MainActivity extends AppCompatActivity {


        ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
          RelativeLayout mDrawerPane;
    ListView mDrawerList;
    private DrawerLayout mDrawerLayout;

    private EditText searchBox;
    Button goButton;
    String searchQuery;

    //For HTTPRequest
    String retVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavItems.add(new NavItem("Navigate", "Default Page", R.drawable.ic_launcher_foreground));
        mNavItems.add(new NavItem("Compare and Buy", "Some Thing1", R.drawable.ic_launcher_foreground));
        mNavItems.add(new NavItem("Coupons and Premiums", "Something2", R.drawable.ic_launcher_foreground));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        //Hamburger Icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //EditText
        searchBox=findViewById(R.id.editText);
        goButton=findViewById(R.id.button);
        searchQuery=searchBox.getText().toString();
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    volleyGETRequest(searchQuery);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "RequestSent",Toast.LENGTH_LONG).show();


            }
        });








    }

    private void volleyGETRequest(String searchQuery) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://reqres.in/api/users/2";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        startNavActivity(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Troshos");
                params.put("job", "KREC");
                return params;
            }


        };
        queue.add(putRequest);


    }
    private void startNavActivity(String response) {
        Log.e("In SNA", "Res is "+response);
        Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
        intent.putExtra("response",response);
        startActivity(intent);

    }


    @Override
    protected void onStart() {


        super.onStart();
    }

    private  void selectItemFromDrawer(int position) {

        if(position ==0){
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
        }
       else if (position == 1) {
            Toast.makeText(getApplicationContext(), "Yet to come", Toast.LENGTH_SHORT).show();

        } else if (position == 2) {

        }

        mDrawerLayout.closeDrawer(mDrawerPane);

    }
}


//TODO: Add Hamburger Icon, recolor the toolbar and add icons
//CodeTheory: Add More Functionality to Navigation Bar