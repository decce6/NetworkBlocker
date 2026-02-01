package me.decce.networkblocker.fabric;

//? if fabric {
import me.decce.networkblocker.NetworkBlockerMod;
import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        NetworkBlockerMod.init();
    }
}
//?}
