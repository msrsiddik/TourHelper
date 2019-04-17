package msr.zerone.tourhelper.weather;

import android.content.SearchRecentSuggestionsProvider;

public class RecentQueryProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "msr.zerone.tourhelper.weather.RecentQueryProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentQueryProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}

