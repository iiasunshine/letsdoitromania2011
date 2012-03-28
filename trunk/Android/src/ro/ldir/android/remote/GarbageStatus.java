package ro.ldir.android.remote;

public enum GarbageStatus {
	CLEANED("COMPLETELY"), IDENTIFIED("UNALLOCATED"), ALLOCATED("PARTIALLY");
	 
    private String translation;

    GarbageStatus(String translation) {
            this.translation = translation;
    }

    public String getTranslation() {
            return translation;
    }


}
