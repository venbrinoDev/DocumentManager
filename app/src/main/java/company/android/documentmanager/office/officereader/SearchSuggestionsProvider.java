/*
 * 文件名称:           SearchSuggestionsProvider.java
 *  
 * 编译器:             android2.2
 * 时间:               下午1:44:04
 */
package company.android.documentmanager.office.officereader;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
    static final String AUTHORITY = "searchprovider";
    static final int MODE = 1;

    public SearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, 1);
    }
}
