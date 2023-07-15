package net.krlite.ive_spoken.mixin;

import net.krlite.ive_spoken.IveSpoken;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatTracker {
	@Inject(method = "addMessage(Lnet/minecraft/text/Text;I)V", at = @At("HEAD"))
	private void addMessage(Text message, int messageId, CallbackInfo ci) {
		TextContent textContent = message.getContent();
		if (textContent instanceof TranslatableTextContent translatableTextContent) {
			Object[] args = translatableTextContent.getArgs();
			if (args.length == 2) {
				MutableText
						prefix = (MutableText) args[0],
						content = (MutableText) args[1];

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
