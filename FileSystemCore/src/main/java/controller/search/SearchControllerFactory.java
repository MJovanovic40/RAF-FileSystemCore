package controller.search;

import enums.SortCriteria;
import enums.SortOrder;

public class SearchControllerFactory {
    public static SearchController createSearchController(String rootStorageLocation, SortCriteria sortCriteria, SortOrder sortOrder){
        return new SearchControllerImplementation(rootStorageLocation, sortCriteria, sortOrder);
    }
}
