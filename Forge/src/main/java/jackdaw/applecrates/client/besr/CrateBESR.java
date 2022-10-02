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
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class CrateBESR implements BlockEntityRenderer<CrateBE> {
    private static final int MAX_RENDERED_ITEMS = 9;
    private static final int ITEMS_PER_ROW = 3;

    public CrateBESR(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CrateBE pBlockEntity, float pPartialTick, PoseStack stack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        float blockRotation = pBlockEntity.getBlockState().getValue(CrateBlock.FACING).toYRot();
        ItemStack selling = pBlockEntity.priceAndSale.getStackInSlot(1);

        if (!selling.isEmpty()) {
            int amount = pBlockEntity.isUnlimitedShop ?
                    MAX_RENDERED_ITEMS :
                    Mth.clamp(pBlockEntity.crateStock.getCountOfItem(selling.getItem()) / selling.getCount(), 1, MAX_RENDERED_ITEMS);

            for (int i = 0; i < amount; i++) {
                stack.pushPose();

                //prepare normalisation of crate rotation in shown itemstacks
                int angleSimp = (int) pBlockEntity.getBlockState().getValue(CrateBlock.FACING).toYRot() / 90;
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
                float randX = (float) calculateOffset(pBlockEntity.getBlockPos().offset(i, 0, 0)).x();
                float randZ = (float) calculateOffset(pBlockEntity.getBlockPos().offset(0, 0, i)).z();

                stack.translate(
                        (i % ITEMS_PER_ROW) * 0.25 - 0.25, // x or crate's left/right
                        0.15f + ((float) (i / ITEMS_PER_ROW) / (float) MAX_RENDERED_ITEMS) * 2.0, //z or crate's up/down
                        0.1f + (float) i * 0.025 //y or crate's higher/lower. In general, don't touch this value
                );

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

    }

    private static Vec3 calculateOffset(BlockPos pPos) {
        long i = Mth.getSeed(pPos.getX(), 0, pPos.getZ());
        float horizontalOffset = 0.2f;
        double d0 = Mth.clamp((((i & 15L) / 15.0F)) * 0.5D, (-horizontalOffset), horizontalOffset);
        double d2 = Mth.clamp((((i >> 8 & 15L) / 15.0F)) * 0.5D, 0, 0.4);
        return new Vec3(d0, 0.0, d2);
    }

    private int getRenderedItemCount(int itemCount, int countPerSale) {
        float countPlusSale = itemCount + countPerSale;
        return (int) (((countPlusSale * countPlusSale) / (float) (countPerSale * countPerSale)) - 1f);
    }
}