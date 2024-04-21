package band.kessokuteatime.ivespoken;

import net.minecraft.text.Text;

public record StampedMessage(long timestamp, Text message) {
	public boolean expired() {
		return System.currentTimeMillis() - timestamp() >= IveSpoken.CONFIG.get().lastingTime;
	}
}
