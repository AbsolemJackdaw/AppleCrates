package jackdaw.applecrates.api.exception;

import jackdaw.applecrates.api.CrateWoodType;

public class WoodException extends RuntimeException {
    public static final WoodException INSTANCE = new WoodException();

    private WoodException() {
    }

    private WoodException(String message) {
        super(message);
    }

    public WoodException existsError(CrateWoodType wood) {
        return new WoodException(String.format("The %s for %s was already registered ! Skipping.", wood.name(), wood.getCompatId()));
    }

    public WoodException resLocNotFound(CrateWoodType wood) {
        return new WoodException(String.format("No ResourceLocation found for %s:%s.", wood.getCompatId(), wood.name()));
    }

    public WoodException noSuchBlockError(CrateWoodType wood) {
        return new WoodException(String.format("The %s for %s was not found in the block registry ! Skipping.", wood.name(), wood.getCompatId()));
    }

    public WoodException alreadyInList(CrateWoodType wood) {
        return new WoodException(String.format("The %s for %s was already registered to the CrateWoodType ! Skipping.", wood.name(), wood.getCompatId()));
    }
}
