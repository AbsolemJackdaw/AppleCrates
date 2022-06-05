package jackdaw.applecrates.client.besr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class CrateBESR implements BlockEntityRenderer<CrateBE> {

    private static final Quaternion POS_OFFSET = new Quaternion(-45.0f + 22.5f, 180.0f, 0.0f, true);

    public CrateBESR(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CrateBE pBlockEntity, float pPartialTick, PoseStack stack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        Vec3 randomOffset = calculateOffset(pBlockEntity.getBlockPos());
        Quaternion blockRotation = pBlockEntity.getBlockState().getValue(CrateBlock.FACING).getRotation();
        ItemStack selling = pBlockEntity.priceAndSale.getStackInSlot(1);

        if (!selling.isEmpty())
            for (int i = 0; i < 3; i++) {
                stack.pushPose();
                stack.translate(0.5F + (i == 2 ? randomOffset.x() : 0.0), 0.3F + (0.02f * i), 0.6F + (i == 1 ? randomOffset.z() : 0));
                stack.mulPose(blockRotation);
                stack.mulPose(POS_OFFSET);
                Minecraft.getInstance().getItemRenderer().renderStatic(
                        selling,
                        ItemTransforms.TransformType.GROUND,
                        pPackedLight,
                        pPackedOverlay,
                        stack,
                        pBufferSource,
                        0);
                stack.popPose();
            }

    }

    private Vec3 calculateOffset(BlockPos pPos) {
        long i = Mth.getSeed(pPos.getX(), 0, pPos.getZ());
        float horizontalOffset = 0.8f;
        double d0 = Mth.clamp((((i & 15L) / 15.0F) - 0.5D) * 0.5D, (-horizontalOffset), horizontalOffset);
        double d2 = Mth.clamp((((i >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D, (-horizontalOffset), horizontalOffset);
        return new Vec3(d0, 0.0, d2);
    }
}