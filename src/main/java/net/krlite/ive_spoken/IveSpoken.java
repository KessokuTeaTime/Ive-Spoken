package net.krlite.ive_spoken;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class IveSpoken implements ModInitializer {
	public static final String NAME = "I've Spoken", ID = "ive-spoken";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	private static final HashMap<UUID, StampedMessage> dialogs = new HashMap<>();

	@Override
	public void onInitialize() {
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

		return message.message();
	}

	public static void renderDialog(AbstractClientPlayerEntity player, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
		@Nullable Text dialog = dialog(player.getUuid());
		if (dialog == null) return;

		boolean sneaky = !player.isSneaky();
		int y = (player.getName().getString().equals("deadmau5") ? -10 : 0) - 10;

		matrixStack.push();
		matrixStack.translate(0, player.getNameLabelHeight(), 0);
		matrixStack.multiply(MinecraftClient.getInstance().getEntityRenderDispatcher().getRotation());
		matrixStack.scale(-0.025F, -0.025F, 0.025F);
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

		int color = (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
		float x = (float) -MinecraftClient.getInstance().textRenderer.getWidth(dialog) / 2;
		MinecraftClient.getInstance().textRenderer.draw(
				dialog, x, y - 10, 0x20FFFFFF, false, matrix4f, vertexConsumers,
				sneaky ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL,
				color, light
		);

		if (sneaky) {
			MinecraftClient.getInstance().textRenderer.draw(
					dialog, x, y - 10, 0xFFFFFFFF, false, matrix4f, vertexConsumers,
					TextRenderer.TextLayerType.NORMAL,
					0, light
			);
		}

		matrixStack.pop();
	}
}
