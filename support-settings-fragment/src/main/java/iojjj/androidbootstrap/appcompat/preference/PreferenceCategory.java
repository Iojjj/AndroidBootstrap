/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package iojjj.androidbootstrap.appcompat.preference;

import android.content.Context;
import android.util.AttributeSet;

import iojjj.androidbootstrap.R;

/**
 * Used to group {@link android.preference.Preference} objects
 * and provide a disabled title above the group.
 * 
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about building a settings UI with Preferences,
 * read the <a href="{@docRoot}guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 */
public class PreferenceCategory extends PreferenceGroup {
    private static final String TAG = "PreferenceCategory";

    PreferenceCategory(Context context, AttributeSet attrs, int defStyle, int defStyleId) {
        super(context, attrs, defStyle, defStyleId);
    }

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, R.style.Preference_DeviceDefault_Category);
    }

    public PreferenceCategory(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreferenceCategory(Context context) {
        this(context, null);
    }
    
    @Override
    protected boolean onPrepareAddPreference(Preference preference) {
        if (preference instanceof PreferenceCategory) {
            throw new IllegalArgumentException(
                    "Cannot add a " + TAG + " directly to a " + TAG);
        }
        
        return super.onPrepareAddPreference(preference);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
    
    @Override
    public boolean shouldDisableDependents() {
        return !super.isEnabled();
    }
}
