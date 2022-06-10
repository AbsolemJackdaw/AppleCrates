package jackdaw.applecrates.compat.api.exception;

public class WoodException extends RuntimeException {
    public WoodException(String modid, String woodType) {
        super(String.format("The %s for %s was already registered ! Skipping.", woodType, modid));
    }
}
