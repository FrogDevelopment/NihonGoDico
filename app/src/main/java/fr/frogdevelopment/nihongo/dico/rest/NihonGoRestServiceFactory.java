package fr.frogdevelopment.nihongo.dico.rest;

public class NihonGoRestServiceFactory extends RestServiceFactory {

	private static final NihonGoRestServiceFactory INSTANCE = new NihonGoRestServiceFactory();

	public static NihonGoClient getNihonGoClient() {
		return INSTANCE.getService(NihonGoClient.class);
	}

	private NihonGoRestServiceFactory() {
		super("http://frog-development.com/");
	}
}
