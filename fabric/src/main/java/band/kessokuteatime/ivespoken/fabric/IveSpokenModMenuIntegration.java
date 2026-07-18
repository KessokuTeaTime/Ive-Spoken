package band.kessokuteatime.ivespoken.fabric;

import band.kessokuteatime.ivespoken.IveSpoken;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class IveSpokenModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return IveSpoken::createConfigScreen;
	}
}
