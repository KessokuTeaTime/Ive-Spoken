package band.kessokuteatime.ivespoken;

import band.kessokuteatime.ivespoken.config.IveSpokenConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class IveSpoken {
	public static final String ID = "ivespoken";

	private static final String ELLIPSIS = "...";
	private static final ConfigHolder<IveSpokenConfig> CONFIG =
			AutoConfig.register(IveSpokenConfig.class, Toml4jConfigSerializer::new);
	private static final Map<UUID, StampedMessage> DIALOGS = new HashMap<>();

	private IveSpoken() {
	}

	public static IveSpokenConfig config() {
		return CONFIG.get();
	}

	public static Screen createConfigScreen(Screen parent) {
		return AutoConfigClient.getConfigScreen(IveSpokenConfig.class, parent).get();
	}

	public static void add(UUID playerId, Component message) {
		long now = Util.getMillis();
		long lastingTime = config().lastingTime;
		DIALOGS.values().removeIf(dialog -> dialog.expired(now, lastingTime));
		DIALOGS.put(playerId, new StampedMessage(now, message));
	}

	public static Component dialog(UUID playerId) {
		IveSpokenConfig config = config();
		if (!config.enabled) {
			return null;
		}

		StampedMessage dialog = DIALOGS.get(playerId);
		if (dialog == null) {
			return null;
		}

		if (dialog.expired(Util.getMillis(), config.lastingTime)) {
			DIALOGS.remove(playerId, dialog);
			return null;
		}

		return dialog.rendered(config.maxWidth);
	}

	static Component fitToWidth(Component message, int maxWidth) {
		Font font = Minecraft.getInstance().font;
		Style style = message.getStyle().withColor(ChatFormatting.GRAY);
		String text = message.getString();
		Component styledMessage = Component.literal(text).setStyle(style);
		if (font.width(styledMessage) <= maxWidth) {
			return styledMessage;
		}

		Component ellipsis = Component.literal(ELLIPSIS).setStyle(style);
		int availableWidth = Math.max(0, maxWidth - font.width(ellipsis));
		String visibleText = font.getSplitter().plainHeadByWidth(text, availableWidth, style);
		return Component.literal(visibleText + ELLIPSIS).setStyle(style);
	}
}
