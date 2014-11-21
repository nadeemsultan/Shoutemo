/*
 * Copyright 2014 Maximilian Salomon.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package de.msal.shoutemo.ui;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import de.msal.shoutemo.R;
import de.msal.shoutemo.ui.chat.ChatFragment;
import de.msal.shoutemo.ui.onlineusers.OnlineUsersFragment;
import de.msal.shoutemo.ui.preference.PreferenceFragment;


public class MainActivity extends ActionBarActivity implements TitleSetListener {

    private DrawerLayout mDrawerLayout;
    private View mNavigationDrawer;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private static boolean drawerOpen = false; // start with an closed drawer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawer = findViewById(R.id.navigation_drawer);
        mDrawerList = (ListView) mNavigationDrawer.findViewById(android.R.id.list);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolBar, 0, 0) {
            public void onDrawerClosed(View view) {
                drawerOpen = false;
            }

            public void onDrawerOpened(View drawerView) {
                drawerOpen = true;
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.setAdapter(new NavigationDrawerAdapter(getApplicationContext(), 0));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        if (savedInstanceState == null) {
            mDrawerList.performItemClick(mDrawerList, 0,
                    mDrawerList.getAdapter().getItemId(0)); //preselect on start
        }
        if (drawerOpen) {
            mDrawerLayout.openDrawer(mNavigationDrawer);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getFragmentManager();
            /**
             * @see de.msal.shoutemo.ui.NavigationDrawerAdapter.mEntries
             */
            switch (position) {
                case 0: // Chat
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, ChatFragment.newInstance(), "CHAT")
                            .commit();
                    break;
                case 1: // UsersOnline
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, OnlineUsersFragment.newInstance())
                            .commit();
                    break;
                case 3: // Settings
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, PreferenceFragment.newInstance())
                            .commit();
                    break;
            }
            // Highlight the selected item and close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mNavigationDrawer);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(mNavigationDrawer)) {
                mDrawerLayout.closeDrawer(mNavigationDrawer);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
