package jackdaw.applecrates;

public class Constants {
    public static final String MODID = "applecrates";
    public static final String[] VANILLAWOODS = {"oak", "spruce", "birch", "acacia", "jungle", "dark_oak", "crimson", "warped"};

    /**
     * change to true when generating data.
     */
    public static boolean IS_DATA_GEN = false;
    public static final boolean GEN_VANILLA_CRATES = false;

    public static final String TAGOWNER = "owner";
    public static final String TAGSTOCK = "cratestock";
    public static final String TAGINTERACTABLE = "interactable";
    public static final String TAGPRICESALE = "pricensale";
    public static final String TAGUNLIMITED = "isUnlimited";

    public static final int TOTALCRATESTOCKLOTS = 30;
    public static final int TOTALCRATESLOTS = 31;
    public static final int PAYSLOT = 0;
    public static final int OUTSLOT = 1;
    public static final int SAVEDTRADEPAY = 2;
    public static final int SAVEDTRADEOUT = 3;
    public static final int CRATESTARTSLOT = 4;
    public static final int CRATEENDSLOT = CRATESTARTSLOT + TOTALCRATESTOCKLOTS - 1; //subtract one for obo; first slot inclusive
    public static final int CRATEENDSLOTALL = CRATESTARTSLOT + TOTALCRATESTOCKLOTS;

    public static final int MONEYSLOT = CRATEENDSLOT + 1;
    public static final int PLAYERSTARTSLOT = MONEYSLOT + 1;
    public static final int PLAYERENDSLOT = PLAYERSTARTSLOT + (9 * 4) - 1;//subtract one for obo; first slot inclusive

    public static boolean isInCrateStock(int id) {
        return id >= CRATESTARTSLOT && id <= CRATEENDSLOT;
    }

    public static boolean isInPlayerInventory(int id) {
        return id >= PLAYERSTARTSLOT && id <= PLAYERENDSLOT;
    }

    public static boolean isInInteractables(int id) {
        return id == PAYSLOT || id == OUTSLOT;
    }

    public static boolean isInSavedTradeStock(int id) {
        return id == SAVEDTRADEPAY || id == SAVEDTRADEOUT;
    }
}
