package net.krlite.ive_spoken.config;

import net.fabricmc.loader.api.FabricLoader;
import net.krlite.pierced.annotation.Silent;
import net.krlite.pierced.config.Pierced;

import java.io.File;

public class IveSpokenConfig extends Pierced {
	private static final @Silent File file = FabricLoader.getInstance().getConfigDir().resolve("ive_spoken.toml").toFile();

	public IveSpokenConfig() {
		super(IveSpokenConfig.class, file);
		load();
	}

	private long lastingTime = 1000 * 5;

	public long lastingTime() {
		return lastingTime;
	}

	public void lastingTime(long time) {
		lastingTime = time;
		save();
	}

	private int maxWidth = 180;

	public int maxWidth() {
		return maxWidth;
	}

	public void maxWidth(int width) {
		maxWidth = width;
		save();
	}
}
