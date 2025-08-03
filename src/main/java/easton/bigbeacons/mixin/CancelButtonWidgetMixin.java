package easton.bigbeacons.mixin;

import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BeaconScreen.CancelButtonWidget.class)
public class CancelButtonWidgetMixin {
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 220))
    private static int resizeGui(int curr) {
        return 234;
    }
}
