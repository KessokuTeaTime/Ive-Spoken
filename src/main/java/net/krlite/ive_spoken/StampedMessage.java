package net.krlite.ive_spoken;

import net.minecraft.text.Text;

public record StampedMessage(long timestamp, Text message) {
	public boolean expired() {
		return System.currentTimeMillis() - timestamp() >= IveSpoken.CONFIG.lastingTime();
	}
}
