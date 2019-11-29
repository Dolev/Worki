package edu.ariel.SE_project.worki;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * A Helper class for various uses.
 */
public class Helper
{
    /**
     * Create a new that starts {@code intent} on click.
     *
     * @param activity the activity that called this
     * @param intent   the intent you wan to transfer to
     * @return a new {@link View.OnClickListener}  that starts {@code intent} on click.
     */
    public static View.OnClickListener ClickTransfer(final AppCompatActivity activity, final Intent intent)
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View x)
            {
                activity.startActivity(intent);
            }
        };
    }
}
