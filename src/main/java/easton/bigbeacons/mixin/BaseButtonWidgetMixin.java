package easton.bigbeacons.mixin;

import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BeaconScreen.BaseButtonWidget.class)
public class BaseButtonWidgetMixin {
    @ModifyConstant(method = "renderButton", constant = @Constant(intValue = 219))
    private static int resizeGui(int curr) {
        return 233;
    }
}
