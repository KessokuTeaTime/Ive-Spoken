package band.kessokuteatime.ivespoken.mixin;

import band.kessokuteatime.ivespoken.IveSpoken;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.EntityTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ChatComponent.class)
public abstract class ChatTracker {
	@Inject(
			method = "addPlayerMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V",
			at = @At("HEAD")
	)
	private void iveSpoken$trackMessage(
			Component message,
			MessageSignature signature,
			GuiMessageTag tag,
			CallbackInfo ci
	) {
		if (!(message.getContents() instanceof TranslatableContents translatable)) {
			return;
		}

		Object[] arguments = translatable.getArgs();
		for (int senderIndex = 0; senderIndex < arguments.length - 1; senderIndex++) {
			if (!(arguments[senderIndex] instanceof Component sender)) {
				continue;
			}

			UUID playerId = findPlayerId(sender);
			if (playerId == null) {
				continue;
			}

			for (int messageIndex = arguments.length - 1; messageIndex > senderIndex; messageIndex--) {
				if (arguments[messageIndex] instanceof Component content) {
					IveSpoken.add(playerId, content);
					return;
				}
			}
		}
	}

	private static UUID findPlayerId(Component component) {
		for (Component part : component.toFlatList()) {
			if (part.getStyle().getHoverEvent() instanceof HoverEvent.ShowEntity showEntity
					&& showEntity.entity().type == EntityTypes.PLAYER) {
				return showEntity.entity().uuid;
			}
		}
		return null;
	}
}
