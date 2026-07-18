package band.kessokuteatime.ivespoken.fabric;

import band.kessokuteatime.ivespoken.IveSpoken;
import net.fabricmc.api.ClientModInitializer;

public final class IveSpokenFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		IveSpoken.config();
	}
}
