package band.kessokuteatime.ivespoken;

import net.minecraft.network.chat.Component;

final class StampedMessage {
	private final long timestamp;
	private final Component message;
	private int renderedWidth = -1;
	private Component renderedMessage;

	StampedMessage(long timestamp, Component message) {
		this.timestamp = timestamp;
		this.message = message;
	}

	boolean expired(long now, long lastingTime) {
		return now - timestamp >= lastingTime;
	}

	Component rendered(int maxWidth) {
		if (renderedMessage == null || renderedWidth != maxWidth) {
			renderedMessage = IveSpoken.fitToWidth(message, maxWidth);
			renderedWidth = maxWidth;
		}
		return renderedMessage;
	}
}
