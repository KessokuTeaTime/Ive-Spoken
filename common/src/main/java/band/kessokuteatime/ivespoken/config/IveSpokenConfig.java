package band.kessokuteatime.ivespoken.config;

import band.kessokuteatime.ivespoken.IveSpoken;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = IveSpoken.ID)
public final class IveSpokenConfig implements ConfigData {
	public boolean enabled = true;

	@ConfigEntry.BoundedDiscrete(max = 60_000)
	public long lastingTime = 5_000L;

	@ConfigEntry.BoundedDiscrete(min = 100, max = 1_000)
	public int maxWidth = 180;

	@Override
	public void validatePostLoad() throws ValidationException {
		if (lastingTime < 0 || lastingTime > 60_000) {
			throw new ValidationException("lastingTime must be between 0 and 60000 milliseconds");
		}
		if (maxWidth < 100 || maxWidth > 1_000) {
			throw new ValidationException("maxWidth must be between 100 and 1000 pixels");
		}
	}
}
