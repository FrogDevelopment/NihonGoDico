package fr.frogdevelopment.nihongo.dico.data.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.data.rest.search.Entry;

public class SearchViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Entry>> entries = new MutableLiveData<>();
    private final MutableLiveData<Boolean> searching = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void setEntries(List<Entry> entries) {
        this.entries.postValue(entries);
    }

    public MutableLiveData<List<Entry>> entries() {
        return this.entries;
    }

    public void isSearching(boolean isSearching) {
        searching.postValue(isSearching);
    }

    public MutableLiveData<Boolean> searching() {
        return this.searching;
    }

    public void setError(String error) {
        this.error.postValue(error);
    }

    public MutableLiveData<String> error() {
        return this.error;
    }
}