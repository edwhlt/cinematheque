/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Results
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db;

import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.db.object.ResultsPage;
import fr.hedwin.db.utils.CompletableFuture;
import fr.hedwin.db.utils.Future;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Results<T> implements TmdbElement {

    public Function<Integer, Future<ResultsPage<T>>> pageFunction;
    public Map<Integer, ResultsPage<T>> memories = new HashMap<>();

    public Results(Function<Integer, Future<ResultsPage<T>>> pageFunction) throws Exception {
        this.pageFunction = pageFunction;
        getPage(1).call();
    }

    public Future<ResultsPage<T>> getPage(int page) {
        return new CompletableFuture<>(() -> {
            if (memories.containsKey(page)) return memories.get(page);
            ResultsPage<T> resultsPage = pageFunction.apply(page).call();
            memories.put(page, resultsPage);
            return resultsPage;
        });
    }

    public int getTotalPage() {
        return memories.get(1).getTotalPages();
    }

    public int getTotalResults() {
        return memories.get(1).getTotalResults();
    }

    public Future<List<T>> getResultsPage(int page) {
        return CompletableFuture.async(() -> getPage(page).call().getResults());
    }

    public ResultsPage<T> getFirstPage(){
        return memories.get(1);
    }

    public Future<T> getElement(int index){
        int page = index/20;
        int i = index%20;
        return CompletableFuture.async(() -> getPage(page).call().getResults().get(i));
    }

}
