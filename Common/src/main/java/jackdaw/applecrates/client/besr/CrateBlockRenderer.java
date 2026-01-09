package jackdaw.applecrates.client.besr;

import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.EnumCrateItemRendering;
import jackdaw.applecrates.block.CrateBlockBase;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class CrateBlockRenderer implements BlockEntityRenderer<CrateBlockEntityBase, CrateBlockRenderState> {
    private static final int MAX_RENDERED_ITEMS = 9;
    private static final int ITEMS_PER_ROW = 3;

    public CrateBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public CrateBlockRenderState createRenderState() {
        return new CrateBlockRenderState();
    }

    @Override
    public void extractRenderState(CrateBlockEntityBase blockEntity, CrateBlockRenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.stackHandler = blockEntity.stackHandler;
        renderState.isUnlimitedShop = blockEntity.isUnlimitedShop;
    }

    @Override
    public void submit(CrateBlockRenderState state, PoseStack stack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        float blockRotation = state.blockState.getValue(CrateBlockBase.FACING).toYRot();
        var stackHandler = state.stackHandler;
        ItemStack selling = stackHandler.getSavedTradeSlotsItem(1);
        if (!selling.isEmpty()) {
            boolean one = Content.clientConfig.getCrateItemRenderingValue() == EnumCrateItemRendering.ONE;
            boolean three = Content.clientConfig.getCrateItemRenderingValue() == EnumCrateItemRendering.THREE;
            int amount = one ? 1 : three ? 3 : (state.isUnlimitedShop ? MAX_RENDERED_ITEMS : Mth.clamp(stackHandler.getCratestacksTotalItemCount(selling.getItem()) / selling.getCount(), 1, MAX_RENDERED_ITEMS));

            for (int i = 0; i < amount; i++) {
                stack.pushPose();

                //prepare normalisation of crate rotation in shown itemstacks
                int angleSimp = (int) state.blockState.getValue(CrateBlockBase.FACING).toYRot() / 90;
                float xoff = angleSimp == 1 || angleSimp == 2 ? 1.0f : 0.0f;
                float zoff = angleSimp == 2 || angleSimp == 3 ? 1.0f : 0.0f;
                float zfront = angleSimp % 2 == 1 ? (0.5f * (angleSimp == 3 ? -1 : 1)) : 0f;
                float xfront = angleSimp % 2 == 0 ? (0.5f * (angleSimp == 2 ? -1 : 1)) : 0f;

                stack.translate(xoff + xfront, 0, zoff + zfront);

                float xAngle = (90.0f - 22.5f) * (xfront == 0 ? (zfront * (zfront < 0 ? 2f : -2f)) : (xfront * (xfront < 0 ? 2f : -2f)));
                var angleBlockFacing = new Quaternionf().fromAxisAngleDeg(0, 1, 0, blockRotation + (angleSimp % 2 == 0 ? 180.0f : 1f));
                var angleCrateIncline = new Quaternionf().fromAxisAngleDeg(1, 0, 0, xAngle);
                stack.mulPose(angleBlockFacing);
                stack.mulPose(angleCrateIncline);//do not merge quaternions!!!!

                /////////////do actual translation or offset here./////////////
                //translate is z,x,y
                //or crate's left/right, up/down, and lower/higher
                var offset = calculateOffset(state.blockPos);
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
                ItemStackRenderState itemstackrenderstate = new ItemStackRenderState();
                Minecraft.getInstance().getItemModelResolver().updateForTopItem(
                        itemstackrenderstate,
                        selling,
                        ItemDisplayContext.GROUND,
                        Minecraft.getInstance().level,
                        null,
                        0
                );
                itemstackrenderstate.submit(stack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
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

    @Override
    public boolean shouldRenderOffScreen() {
        return BlockEntityRenderer.super.shouldRenderOffScreen();
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(CrateBlockEntityBase blockEntity, Vec3 cameraPos) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos);
    }
}
