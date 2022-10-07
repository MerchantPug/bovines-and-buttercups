package net.merchantpug.bovinesandbuttercups.mixin.client;

import net.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(QuadrupedModel.class)
public abstract class QuadrupedModelMixin<T extends Entity> extends AgeableListModel<T> {
    private boolean bovinesandbuttercups$hasResetFromLaying;

    @Shadow @Final protected ModelPart head;
    @Shadow @Final protected ModelPart body;

    @Shadow @Final protected ModelPart leftFrontLeg;

    @Shadow @Final protected ModelPart leftHindLeg;

    @Shadow @Final protected ModelPart rightFrontLeg;

    @Shadow @Final protected ModelPart rightHindLeg;

    @Inject(method = "setupAnim", at = @At("HEAD"), cancellable = true)
    private void bovinesandbuttercups$addLayingAnimationHeadBody(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof FlowerCow && ((FlowerCow) entity).getStandingStillForBeeTicks() > 0) {
            this.head.y = 6.0F + 7.0F;
            this.head.z = -7.0F;
            this.head.xRot = headPitch * ((float)Math.PI / 180.0F);
            this.head.yRot = netHeadYaw * ((float)Math.PI / 180.0F);
            this.body.y = 9.0F + 7.0F;
            this.leftFrontLeg.x = 5.0F;
            this.leftFrontLeg.y = 15.25F + 7.0F;
            this.leftFrontLeg.z = -10.0F;
            this.leftFrontLeg.xRot = 90.0F * (Mth.PI / 180.0F);
            this.rightFrontLeg.x = -5.0F;
            this.rightFrontLeg.y = 15.25F + 7.0F;
            this.rightFrontLeg.z = -10.0F;
            this.rightFrontLeg.xRot = 90.0F * (Mth.PI / 180.0F);
            this.leftHindLeg.x = 7.0F;
            this.leftHindLeg.y = 15.0F + 7.0F;
            this.leftHindLeg.z = 9.0F;
            this.leftHindLeg.xRot = -90.0F * (Mth.PI / 180.0F);
            this.rightHindLeg.x = -7.0F;
            this.rightHindLeg.y = 15.0F + 7.0F;
            this.rightHindLeg.z = 9.0F;
            this.rightHindLeg.xRot = -90.0F * (Mth.PI / 180.0F);

            if (this.young) {
                this.head.y = 0.0F + 7.0F;
            }
            this.bovinesandbuttercups$hasResetFromLaying = false;
            ci.cancel();
        } else if (!this.bovinesandbuttercups$hasResetFromLaying) {
            this.head.y = this.head.getInitialPose().y;
            this.head.z = this.head.getInitialPose().z;
            this.body.y = this.body.getInitialPose().y;
            this.leftFrontLeg.x = this.leftFrontLeg.getInitialPose().x;
            this.leftFrontLeg.y = this.leftFrontLeg.getInitialPose().y;
            this.leftFrontLeg.z = this.leftFrontLeg.getInitialPose().z;
            this.rightFrontLeg.x = this.rightFrontLeg.getInitialPose().x;
            this.rightFrontLeg.y = this.rightFrontLeg.getInitialPose().y;
            this.rightFrontLeg.z = this.rightFrontLeg.getInitialPose().z;
            this.leftHindLeg.x = this.leftHindLeg.getInitialPose().x;
            this.leftHindLeg.y = this.leftHindLeg.getInitialPose().y;
            this.leftHindLeg.z = this.leftHindLeg.getInitialPose().z;
            this.rightHindLeg.x = this.rightHindLeg.getInitialPose().x;
            this.rightHindLeg.y = this.rightHindLeg.getInitialPose().y;
            this.rightHindLeg.z = this.rightHindLeg.getInitialPose().z;
            this.bovinesandbuttercups$hasResetFromLaying = true;
        }
    }
}
