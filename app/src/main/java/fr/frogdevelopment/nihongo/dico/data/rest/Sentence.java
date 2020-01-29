package fr.frogdevelopment.nihongo.dico.data.rest;

import java.io.Serializable;

import fr.frogdevelopment.nihongo.dico.to_delete.entity.Result;

public class Sentence implements Serializable {

    private static final long serialVersionUID = 1L;

    public Result japanese;
    public Result translation;

}
