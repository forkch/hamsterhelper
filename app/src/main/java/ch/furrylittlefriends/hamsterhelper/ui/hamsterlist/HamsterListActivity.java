package ch.furrylittlefriends.hamsterhelper.ui.hamsterlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.modules.HamsterListModule;
import ch.furrylittlefriends.hamsterhelper.ui.BaseListActivity;
import ch.furrylittlefriends.hamsterhelper.ui.addhamster.AddHamsterActivity;

public class HamsterListActivity extends BaseListActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = HamsterListActivity.class.getSimpleName();

    @Inject
    HamsterListPresenter presenter;

    @InjectView(android.R.id.list)
    ListView listView;

    @InjectView(R.id.swipeToRefreshLayout)
    SwipeRefreshLayout pullToRefreshLayout;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMyApplication().createScopedGraphAndInject(this, new HamsterListModule(this));

        setContentView(R.layout.activity_hamster_list);
        ButterKnife.inject(this);

        pullToRefreshLayout.setOnRefreshListener(this);
        pullToRefreshLayout.setColorSchemeResources(R.color.pull_to_refresh_color_1,
                R.color.pull_to_refresh_color_2,
                R.color.pull_to_refresh_color_3,
                R.color.pull_to_refresh_color_4);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.button_floating_action);
        floatingActionButton.attachToListView(listView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
        presenter.loadHamsters();
    }


    @Override
    protected void onStop() {
        presenter.onPause();
        super.onStop();
    }

    @OnClick(R.id.button_floating_action)
    public void onAddClick() {
        startActivity(new Intent(this, AddHamsterActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hamster_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        presenter.syncHamsters();
    }

    public void onHamstersLoaded() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.setRefreshing(false);
            }
        }, 1000);

    }

    public void ensureAddButtonVisibility() {
        if (listView.getCount() == 0) {
            floatingActionButton.show();
        }
    }

    public int getPositionForView(View v) {
        return listView.getPositionForView(v);
    }
}
