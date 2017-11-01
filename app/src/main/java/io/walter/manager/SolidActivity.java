package io.walter.manager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.walter.manager.adapters.ExpandableListAdapter;

public class SolidActivity extends AppCompatActivity {
    ExpandableListView listViewSolid;
    ExpandableListAdapter mAdapter;
    List<String> mlistHeader;
    HashMap<String, List<String>> mListChildren;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solid);
        listViewSolid= (ExpandableListView) findViewById(R.id.listViewSolid);
        prepareListData();
        mAdapter=new ExpandableListAdapter(this,mlistHeader,mListChildren);
        listViewSolid.setAdapter(mAdapter);
        listViewSolid.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    listViewSolid.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

    }

    private void prepareListData() {
        //TODO replace with real data
        mlistHeader=new ArrayList<>();
        mListChildren=new HashMap<>();
        mlistHeader.add("Solid Base For Business");
        mlistHeader.add("Tell A Friend");
        mlistHeader.add("Start A Business");
        mlistHeader.add("Build A business");
        mlistHeader.add("Grow A Business");
        mlistHeader.add("Enlarge A Business");

        List<String> solid = new ArrayList<>();
        solid.add("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti, veniam esse magnam totam aut ab. Corporis nobis facere, vel, impedit optio officia, tempore minima ducimus fuga sequi numquam excepturi omnis.");
        List<String> friend = new ArrayList<>();
        friend.add("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti, veniam esse magnam totam aut ab. Corporis nobis facere, vel, impedit optio officia, tempore minima ducimus fuga sequi numquam excepturi omnis.");
        List<String> start = new ArrayList<>();
        start.add("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti, veniam esse magnam totam aut ab. Corporis nobis facere, vel, impedit optio officia, tempore minima ducimus fuga sequi numquam excepturi omnis.");
        List<String> build = new ArrayList<>();
        build.add("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti, veniam esse magnam totam aut ab. Corporis nobis facere, vel, impedit optio officia, tempore minima ducimus fuga sequi numquam excepturi omnis.");
        List<String> grow = new ArrayList<>();
        grow.add("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti, veniam esse magnam totam aut ab. Corporis nobis facere, vel, impedit optio officia, tempore minima ducimus fuga sequi numquam excepturi omnis.");
        List<String> enlarge = new ArrayList<>();
        enlarge.add("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti, veniam esse magnam totam aut ab. Corporis nobis facere, vel, impedit optio officia, tempore minima ducimus fuga sequi numquam excepturi omnis.");

        mListChildren.put(mlistHeader.get(0),solid);
        mListChildren.put(mlistHeader.get(1),friend);
        mListChildren.put(mlistHeader.get(2),start);
        mListChildren.put(mlistHeader.get(3),build);
        mListChildren.put(mlistHeader.get(4),grow);
        mListChildren.put(mlistHeader.get(5),enlarge);


    }
}
