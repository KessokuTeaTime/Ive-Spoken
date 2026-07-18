package band.kessokuteatime.ivespoken.neoforge;

import band.kessokuteatime.ivespoken.IveSpoken;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = IveSpoken.ID, dist = Dist.CLIENT)
public final class IveSpokenNeoForge {
	public IveSpokenNeoForge(ModContainer modContainer) {
		IveSpoken.config();
		IConfigScreenFactory configScreenFactory =
				(container, parent) -> IveSpoken.createConfigScreen(parent);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, configScreenFactory);
	}
}
