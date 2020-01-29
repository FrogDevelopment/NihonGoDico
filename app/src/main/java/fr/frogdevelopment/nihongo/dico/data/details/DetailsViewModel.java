package fr.frogdevelopment.nihongo.dico.data.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import fr.frogdevelopment.nihongo.dico.data.rest.EntryDetails;

public class DetailsViewModel extends AndroidViewModel {

    private MutableLiveData<EntryDetails> entryDetails = new MutableLiveData<>();

    public DetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDetails(EntryDetails entryDetails) {
        this.entryDetails.postValue(entryDetails);
    }

    public EntryDetails entryDetails() {
        return this.entryDetails.getValue();
    }
}