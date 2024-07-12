package band.kessokuteatime.ivespoken;

import band.kessokuteatime.nightautoconfig.config.base.ConfigType;
import com.google.common.collect.ImmutableMap;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.api.ClientModInitializer;
import band.kessokuteatime.ivespoken.config.IveSpokenConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class IveSpoken implements ClientModInitializer {
	public static final String NAME = "I've Spoken", ID = "ivespoken";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static final ConfigHolder<IveSpokenConfig> CONFIG;
	private static final HashMap<UUID, StampedMessage> dialogs = new HashMap<>();

	static {
		AutoConfig.register(IveSpokenConfig.class, ConfigType.DEFAULT_COMMENTED::fileWatcherSerializer);
		CONFIG = AutoConfig.getConfigHolder(IveSpokenConfig.class);
	}

	@Override
	public void onInitializeClient() {
	}

	public static ImmutableMap<UUID, StampedMessage> dialogs() {
		return ImmutableMap.copyOf(dialogs);
	}

	public static void add(UUID uuid, Text message) {
		dialogs.put(uuid, new StampedMessage(System.currentTimeMillis(), message));
	}

	public static void refresh() {
		synchronized (dialogs) {
			dialogs.values().removeIf(StampedMessage::expired);
		}
	}

	public static @Nullable StampedMessage message(UUID uuid) {
		refresh();
		if (!dialogs().containsKey(uuid)) return null;

		return dialogs().get(uuid);
	}

	public static @Nullable Text dialog(UUID uuid) {
		@Nullable StampedMessage message = message(uuid);
		if (message == null) return null;

		Text content = message.message();
		StringBuilder builder = new StringBuilder();

		for (int width = 0, index = 0; index < content.getString().length(); index++) {
			char c = content.getString().charAt(index);
			int charWidth = MinecraftClient.getInstance().textRenderer.getWidth(String.valueOf(c));

			if (width + charWidth > CONFIG.get().maxWidth) {
				builder.append("...");
				break;
			}

			width += charWidth;
			builder.append(c);
		}

		return Text.literal(builder.toString()).setStyle(content.getStyle().withColor(Formatting.GRAY));
	}
}
