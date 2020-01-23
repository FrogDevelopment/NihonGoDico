package fr.frogdevelopment.nihongo.dico.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.search.Entry;
import fr.frogdevelopment.nihongo.dico.search.EntryDetails;

public class MainViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Entry>> entries = new MutableLiveData<>();
    private final MutableLiveData<Boolean> searching = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<EntryDetails> entryDetails = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
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

    public void setDetails(EntryDetails entryDetails) {
        this.entryDetails.postValue(entryDetails);
    }

    public MutableLiveData<EntryDetails> entryDetails() {
        return this.entryDetails;
    }
}