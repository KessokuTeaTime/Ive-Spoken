package net.krlite.ivespoken.mixin;

import net.krlite.ivespoken.IveSpoken;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class DialogRenderer extends EntityRenderer<Entity> {
	protected DialogRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Inject(method = "renderLabelIfPresent*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
	private void renderDialog(AbstractClientPlayerEntity player, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (!IveSpoken.CONFIG.enabled) return;

		double distance = this.dispatcher.getSquaredDistanceToCamera(player);
		if (distance > 4096) return;

		IveSpoken.renderDialog(player, matrixStack, vertexConsumers, light);
	}
}
