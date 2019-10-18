package com.mindera.skeletoid.dialogs;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

/**
 * We want our "show" method to require the tag parameter not to be null. As such we created an intermediate class in java
 * that we annotated with @NotNull. In classes extending this one, Kotlin will assume that the method's parameter is not
 * nullable and overrides will default to String instead of String? . The following is the article used to come to this
 * solution.
 * https://stackoverflow.com/questions/36343280/how-can-i-override-a-java-method-and-change-the-nullability-of-a-parameter
 */
public class IntermediateDialog extends DialogFragment {
    @Override
    public void show(FragmentManager manager, @NotNull String tag) {
        super.show(manager, tag);
    }
}
