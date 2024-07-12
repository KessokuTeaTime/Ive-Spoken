package band.kessokuteatime.ivespoken.mixin;

import band.kessokuteatime.ivespoken.IveSpoken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatTracker {
	@Inject(
			method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
			at = @At("HEAD")
	)
	private void addMessage(Text message, MessageSignatureData signature, MessageIndicator indicator, CallbackInfo ci) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			IveSpoken.LOGGER.info("Message received: {}", message);
		}

		TextContent textContent = message.getContent();
		if (textContent instanceof TranslatableTextContent translatableTextContent) {
			Object[] args = translatableTextContent.getArgs();
			if (args.length == 2
						&& args[0] instanceof MutableText prefix
						&& args[1] instanceof MutableText content
			) {
				HoverEvent hoverEvent = prefix.getStyle().getHoverEvent();
				if (hoverEvent != null) {
					HoverEvent.EntityContent entityContent = (HoverEvent.EntityContent) hoverEvent.getValue(hoverEvent.getAction());
					if (entityContent != null) {
						IveSpoken.add(entityContent.uuid, content);
					}
				}
			}
		}
	}
}
