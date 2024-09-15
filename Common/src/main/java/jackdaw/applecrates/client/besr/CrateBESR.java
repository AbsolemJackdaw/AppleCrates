package jackdaw.applecrates.client.besr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.EnumCrateItemRendering;
import jackdaw.applecrates.block.CommonCrateBlock;
import jackdaw.applecrates.block.blockentity.CommonCrateBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class CrateBESR implements BlockEntityRenderer<CommonCrateBE> {
    private static final int MAX_RENDERED_ITEMS = 9;
    private static final int ITEMS_PER_ROW = 3;

    public CrateBESR(BlockEntityRendererProvider.Context ctx) {
    }


    @Override
    public void render(CommonCrateBE crateBE, float pPartialTick, PoseStack stack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        float blockRotation = crateBE.getBlockState().getValue(CommonCrateBlock.FACING).toYRot();
        ItemStack selling = crateBE.stackHandler.getPriceAndSaleItem(1);

        if (!selling.isEmpty()) {
            boolean one = Content.clientConfig.getCrateItemRenderingValue() == EnumCrateItemRendering.ONE;
            boolean three = Content.clientConfig.getCrateItemRenderingValue() == EnumCrateItemRendering.THREE;
            int amount = one ? 1 : three ? 3 : (crateBE.isUnlimitedShop ? MAX_RENDERED_ITEMS : Mth.clamp(crateBE.stackHandler.getCratestacksTotalItemCount(selling.getItem()) / selling.getCount(), 1, MAX_RENDERED_ITEMS));

            for (int i = 0; i < amount; i++) {
                stack.pushPose();

                //prepare normalisation of crate rotation in shown itemstacks
                int angleSimp = (int) crateBE.getBlockState().getValue(CommonCrateBlock.FACING).toYRot() / 90;
                float xoff = angleSimp == 1 || angleSimp == 2 ? 1.0f : 0.0f;
                float zoff = angleSimp == 2 || angleSimp == 3 ? 1.0f : 0.0f;
                float zfront = angleSimp % 2 == 1 ? (0.5f * (angleSimp == 3 ? -1 : 1)) : 0f;
                float xfront = angleSimp % 2 == 0 ? (0.5f * (angleSimp == 2 ? -1 : 1)) : 0f;

                stack.translate(xoff + xfront, 0, zoff + zfront);

                float xAngle = (90.0f - 22.5f) * (xfront == 0 ? (zfront * (zfront < 0 ? 2f : -2f)) : (xfront * (xfront < 0 ? 2f : -2f)));

                stack.mulPose(new Quaternion(0f, blockRotation + (angleSimp % 2 == 0 ? 180.0f : 0f), 0f, true));
                stack.mulPose(new Quaternion(xAngle, 0, 0, true));//do not merge quaternions!!!!

                /////////////do actual translation or offset here./////////////
                //translate is z,x,y
                //or crate's left/right, up/down, and lower/higher
                var offset = calculateOffset(crateBE.getBlockPos());
                float randX = (float) offset.x();
                float randZ = (float) offset.z();

                if (Content.clientConfig.getCrateItemRenderingValue() == EnumCrateItemRendering.THREE) {
                    stack.translate((i == 0 ? 0.0f : randX / (float) i * (i == 1 ? -1 : 1)), // x or crate's left/right
                            0.25f + (i == 0 ? 0.0f : randZ / (float) i), //z or crate's up/down
                            0.1f + (float) i * 0.025 //y or crate's higher/lower. In general, don't touch this value
                    );
                } else {
                    stack.translate((i % ITEMS_PER_ROW) * 0.25 - 0.25, // x or crate's left/right
                            0.17f + ((int) (i / ITEMS_PER_ROW) / (float) MAX_RENDERED_ITEMS) * 2.0, //z or crate's up/down
                            0.1f + ((int) (i / ITEMS_PER_ROW) % 2) * 0.025 + randX * 0.02 + (i % 2) * 0.01 //y or crate's higher/lower. In general, don't touch this value
                    );
                }

                Minecraft.getInstance().getItemRenderer().renderStatic(selling, ItemTransforms.TransformType.GROUND, pPackedLight, pPackedOverlay, stack, pBufferSource, 0);
                stack.popPose();
            }
        }

    }

    private static Vec3 calculateOffset(BlockPos pPos) {
        long i = Mth.getSeed(pPos.getX(), 0, pPos.getZ());
        float horizontalOffset = 0.2f;
        double d0 = Mth.clamp((((i & 15L) / 15.0F)) * 0.5D, (-horizontalOffset), horizontalOffset);
        double d2 = Mth.clamp((((i >> 8 & 15L) / 15.0F)) * 0.5D, 0, 0.4);
        return new Vec3(d0, 0.0, d2);
    }

}