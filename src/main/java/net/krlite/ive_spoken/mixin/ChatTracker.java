package net.krlite.ive_spoken.mixin;

import net.krlite.ive_spoken.IveSpoken;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(ChatHud.class)
public class ChatTracker {
	@Inject(method = "addMessage(Lnet/minecraft/text/Text;I)V", at = @At("HEAD"))
	private void addMessage(Text message, int messageId, CallbackInfo ci) {
		if (message instanceof TranslatableText translatableText) {
			Object[] args = translatableText.getArgs();
			if (args.length == 2) {
				LiteralText prefix = (LiteralText) args[0];
				Object content = args[1];

				HoverEvent hoverEvent = prefix.getStyle().getHoverEvent();
				if (hoverEvent != null) {
					HoverEvent.EntityContent entityContent = (HoverEvent.EntityContent) hoverEvent.getValue(hoverEvent.getAction());
					if (entityContent != null) {
						if (content instanceof TranslatableText translatableContent) IveSpoken.add(entityContent.uuid, translatableContent);
						if (content instanceof String stringContent) IveSpoken.add(entityContent.uuid, new LiteralText(stringContent));
					}
				}
			}
		}
	}
}
