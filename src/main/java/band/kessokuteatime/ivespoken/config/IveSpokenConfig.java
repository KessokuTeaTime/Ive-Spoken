package band.kessokuteatime.ivespoken.config;

import com.electronwill.nightconfig.core.serde.annotations.SerdeComment;
import com.electronwill.nightconfig.core.serde.annotations.SerdeDefault;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.function.Supplier;

@Config(name = "ivespoken")
public class IveSpokenConfig implements ConfigData {
	@ConfigEntry.Gui.Excluded
	private transient final Supplier<Boolean> enabledProvider = () -> true;

	@SerdeDefault(provider = "enabledSupplier")
	public boolean enabled = enabledProvider.get();

	@ConfigEntry.Gui.Excluded
	private transient final Supplier<Long> lastingTimerProvider = () -> 1000L * 5;

	@SerdeDefault(provider = "enabledSupplier")
	@SerdeComment(" The time in milliseconds that the message will be displayed on the screen.")
	@SerdeComment(" Max value: 60000 milliseconds (1 minute)")
	@ConfigEntry.BoundedDiscrete(max = 1000 * 60)
	public long lastingTime = lastingTimerProvider.get();

	@ConfigEntry.Gui.Excluded
	private transient final Supplier<Integer> maxWidthProvider = () -> 180;

	@SerdeComment(" The maximum width of the message on the screen in pixel.")
	@SerdeComment(" Min value: 100 pixels")
	@SerdeComment(" Max value: 1000 pixels")
	@ConfigEntry.BoundedDiscrete(min = 100,max = 1000)
	public int maxWidth = maxWidthProvider.get();
}
