package band.kessokuteatime.ivespoken.mixin;

import band.kessokuteatime.ivespoken.IveSpoken;
import band.kessokuteatime.ivespoken.render.DialogRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class DialogRenderer {
	private static final float DIALOG_LINE_OFFSET = 9.0F * 1.15F * EntityRenderer.NAMETAG_SCALE;

	@Inject(
			method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
			at = @At("TAIL")
	)
	private void iveSpoken$extractDialog(
			Avatar player,
			AvatarRenderState state,
			float partialTick,
			CallbackInfo ci
	) {
		((DialogRenderState) state).iveSpoken$setDialog(IveSpoken.dialog(player.getUUID()));
	}

	@Inject(
			method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
			at = @At("TAIL")
	)
	private void iveSpoken$submitDialog(
			AvatarRenderState state,
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			CameraRenderState cameraState,
			CallbackInfo ci
	) {
		Component dialog = ((DialogRenderState) state).iveSpoken$getDialog();
		if (dialog == null || state.nameTag == null || state.nameTagAttachment == null) {
			return;
		}

		poseStack.pushPose();
		float lineOffset = state.scoreText == null ? DIALOG_LINE_OFFSET : DIALOG_LINE_OFFSET * 2.0F;
		poseStack.translate(0.0F, lineOffset, 0.0F);
		submitNodeCollector.submitNameTag(
				poseStack,
				state.nameTagAttachment,
				state.showExtraEars ? -10 : 0,
				dialog,
				!state.isDiscrete,
				state.lightCoords,
				cameraState
		);
		poseStack.popPose();
	}
}
