package com.example.om.basestructure.ui.fragment.base;

import android.support.v4.app.DialogFragment;

/**
 * Created by android on 27.01.18.
 */

public class BaseDialog extends DialogFragment {

    public interface Communicator {
        void onDatePass(String date);
    }
}
