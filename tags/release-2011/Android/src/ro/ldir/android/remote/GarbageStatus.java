package ro.ldir.android.remote;

public enum GarbageStatus {
	CLEANED("Curățat"), IDENTIFIED("Identificat");
    private String translation;

    GarbageStatus(String translation) {
            this.translation = translation;
    }

    public String getTranslation() {
            return translation;
    }


}
