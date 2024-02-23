package net.krlite.ivespoken.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "ivespoken")
public class IveSpokenConfig implements ConfigData {
	public boolean enabled = true;

	@ConfigEntry.BoundedDiscrete(max = 1000 * 60)
	public long lastingTime = 1000 * 5;

	@ConfigEntry.BoundedDiscrete(max = 1000)
	public int maxWidth = 180;
}
