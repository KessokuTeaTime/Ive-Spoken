package band.kessokuteatime.ivespoken.mixin;

import band.kessokuteatime.ivespoken.render.DialogRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public abstract class AvatarRenderStateMixin implements DialogRenderState {
	@Unique
	private Component iveSpoken$dialog;

	@Override
	public Component iveSpoken$getDialog() {
		return iveSpoken$dialog;
	}

	@Override
	public void iveSpoken$setDialog(Component dialog) {
		iveSpoken$dialog = dialog;
	}
}
