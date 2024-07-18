package band.kessokuteatime.ivespoken.config.modmenu;

import band.kessokuteatime.ivespoken.config.IveSpokenConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import band.kessokuteatime.ivespoken.IveSpoken;

public class IveSpokenModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            IveSpoken.CONFIG.load();
            return AutoConfig.getConfigScreen(IveSpokenConfig.class, parent).get();
        };
    }
}
