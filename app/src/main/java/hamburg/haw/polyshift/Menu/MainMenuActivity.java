package hamburg.haw.polyshift.Menu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.analytics.Tracker;

import hamburg.haw.polyshift.Analytics.AnalyticsApplication;
import hamburg.haw.polyshift.BuildConfig;
import hamburg.haw.polyshift.Game.TrainingActivity;
import hamburg.haw.polyshift.R;
import hamburg.haw.polyshift.Tools.AlertDialogs;

/**
 * Created by Andi on 12.03.2015.
 */
public class MainMenuActivity extends Activity {

    Button newGameButton;
    Button myGamesButton;
    Button scoresButton;
    Button tutorialButton;
    Button quitGameButton;
    ImageView backgroundLogo;
    public static ProgressDialog dialog = null;
    private Tracker mTracker = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Bundle data = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if (HandleSharedPreferences.checkfirstStart(MainMenuActivity.this)) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainMenuActivity.this);
            builder.setMessage(R.string.first_time);
            builder = builder.setPositiveButton(MainMenuActivity.this.getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(MainMenuActivity.this, TrainingActivity.class);
                            startActivity(intent);
                            MainMenuActivity.this.finish();
                        }
                    });
            builder.setNegativeButton(R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.show();

        }

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        newGameButton = (Button) findViewById(R.id.new_game_button);
        myGamesButton = (Button) findViewById(R.id.my_games_button);
        scoresButton = (Button) findViewById(R.id.scores_button);
        tutorialButton = (Button) findViewById(R.id.tutorial_button);
        quitGameButton = (Button) findViewById(R.id.quit_game_button);
        backgroundLogo = (ImageView) findViewById(R.id.background_logo);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChooseOpponentActivity.class);
                startActivity(intent);
                MainMenuActivity.this.finish();
            }
        });

        myGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyGamesActivity.class);
                startActivity(intent);
                MainMenuActivity.this.finish();
            }
        });

        scoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ScoresActivity.class);
                startActivity(intent);
                MainMenuActivity.this.finish();
            }
        });

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TrainingActivity.class);
                startActivity(intent);
                MainMenuActivity.this.finish();
            }
        });

        quitGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        if (data != null && data.getBoolean("error_occured")) {
            MainMenuActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainMenuActivity.this);
                    builder.setMessage(R.string.error_during_transmition);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.onclick_menu, menu);
        MenuItem about = menu.findItem(R.id.about);
        about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialogs.showAlert(MainMenuActivity.this,"Über Polyshift", "Version " +  BuildConfig.VERSION_NAME);
                return false;

            }
        });
        MenuItem logout = menu.findItem(R.id.logout);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                HandleSharedPreferences.setUserCredentials(MainMenuActivity.this, "", "");
                Intent intent = new Intent(MainMenuActivity.this, WelcomeActivity.class);
                startActivity(intent);
                MainMenuActivity.this.finish();
                WelcomeActivity.userLogout();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}