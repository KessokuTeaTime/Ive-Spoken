package net.krlite.ive_spoken;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.ModInitializer;
import net.krlite.ive_spoken.config.IveSpokenConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class IveSpoken implements ModInitializer {
	public static final String NAME = "I've Spoken", ID = "ive-spoken";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static final IveSpokenConfig CONFIG = new IveSpokenConfig();
	private static final HashMap<UUID, StampedMessage> dialogs = new HashMap<>();

	@Override
	public void onInitialize() {
		CONFIG.save();
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

	public static long timestamp(UUID uuid) {
		@Nullable StampedMessage message = message(uuid);
		if (message == null) return 0;

		return message.timestamp();
	}

	public static @Nullable Text dialog(UUID uuid) {
		@Nullable StampedMessage message = message(uuid);
		if (message == null) return null;

		Text content = message.message();
		StringBuilder builder = new StringBuilder();

		for (int width = 0, index = 0; index < content.getString().length(); index++) {
			char c = content.getString().charAt(index);
			int charWidth = MinecraftClient.getInstance().textRenderer.getWidth(String.valueOf(c));

			if (width + charWidth > CONFIG.maxWidth()) {
				builder.append("...");
				break;
			}

			width += charWidth;
			builder.append(c);
		}

		return new LiteralText(builder.toString())
					   .setStyle(content.getStyle().withColor(Formatting.GRAY));
	}

	public static void renderDialog(AbstractClientPlayerEntity player, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
		@Nullable Text dialog = dialog(player.getUuid());
		if (dialog == null) return;

		boolean sneaky = !player.isSneaky();
		int y = (player.getName().getString().equals("deadmau5") ? -10 : 0) - 10;

		matrixStack.push();
		matrixStack.translate(0, player.getHeight() + 0.5F, 0);
		matrixStack.multiply(MinecraftClient.getInstance().getEntityRenderDispatcher().getRotation());
		matrixStack.scale(-0.025F, -0.025F, 0.025F);
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

		int backgroundColor = (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
		float x = (float) -MinecraftClient.getInstance().textRenderer.getWidth(dialog) / 2;

		MinecraftClient.getInstance().textRenderer.draw(
				dialog, x, y, 0x20FFFFFF, false, matrix4f, vertexConsumers,
				sneaky, backgroundColor, light
		);

		if (sneaky) {
			MinecraftClient.getInstance().textRenderer.draw(
					dialog, x, y, 0xFFFFFFFF, false, matrix4f, vertexConsumers,
					false, 0, light
			);
		}

		matrixStack.pop();
	}
}
