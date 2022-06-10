package jackdaw.applecrates.util;

import java.util.Objects;

public class ModWood {
    private String modId;
    private String woodName;

    public ModWood(String modId, String woodName) {
        this.modId = modId;
        this.woodName = woodName;
    }

    public String getModId() {
        return modId;
    }

    public String getWoodName() {
        return woodName;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ModWood that && this.woodName.equals(that.woodName) && this.modId.equals(that.modId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modId, woodName);
    }
}
