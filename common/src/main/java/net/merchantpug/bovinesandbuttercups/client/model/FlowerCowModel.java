package net.merchantpug.bovinesandbuttercups.client.model;

import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class FlowerCowModel<T extends FlowerCow> extends CowModel<T> {
    private boolean hasResetFromLaying;

    public FlowerCowModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.getStandingStillForBeeTicks() > 0) {
            this.head.y = 6.0F + 7.0F;
            this.head.z = -7.0F;
            this.head.xRot = headPitch * ((float) Math.PI / 180.0F);
            this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
            this.body.y = 9.0F + 7.0F;
            this.leftFrontLeg.x = 5.0F - 1.0F;
            this.leftFrontLeg.y = 15.25F + 7.0F;
            this.leftFrontLeg.z = -10.0F;
            this.leftFrontLeg.xRot = 90.0F * (Mth.PI / 180.0F);
            this.leftFrontLeg.yRot = 15.0F * (Mth.PI / 180.0F);
            this.rightFrontLeg.x = -5.0F + 1.0F;
            this.rightFrontLeg.y = 15.25F + 7.0F;
            this.rightFrontLeg.z = -10.0F;
            this.rightFrontLeg.xRot = 90.0F * (Mth.PI / 180.0F);
            this.rightFrontLeg.yRot = -15.0F * (Mth.PI / 180.0F);
            this.leftHindLeg.x = 7.0F - 4.0F;
            this.leftHindLeg.y = 15.0F + 7.0F;
            this.leftHindLeg.z = 10.0F;
            this.leftHindLeg.xRot = -90.0F * (Mth.PI / 180.0F);
            this.leftHindLeg.yRot = -45.0F * (Mth.PI / 180.0F);
            this.rightHindLeg.x = -7.0F + 4.0F;
            this.rightHindLeg.y = 15.0F + 7.0F;
            this.rightHindLeg.z = 10.0F;
            this.rightHindLeg.xRot = -90.0F * (Mth.PI / 180.0F);
            this.rightHindLeg.yRot = 45.0F * (Mth.PI / 180.0F);

            if (this.young) {
                this.head.y = 7.0F;
            }
            this.hasResetFromLaying = false;
            return;
        } else if (!hasResetFromLaying) {
            this.head.y = this.head.getInitialPose().y;
            this.head.z = this.head.getInitialPose().z;
            this.body.y = this.body.getInitialPose().y;
            this.leftFrontLeg.x = this.leftFrontLeg.getInitialPose().x;
            this.leftFrontLeg.y = this.leftFrontLeg.getInitialPose().y;
            this.leftFrontLeg.z = this.leftFrontLeg.getInitialPose().z;
            this.leftFrontLeg.yRot = 0.0F;
            this.rightFrontLeg.x = this.rightFrontLeg.getInitialPose().x;
            this.rightFrontLeg.y = this.rightFrontLeg.getInitialPose().y;
            this.rightFrontLeg.z = this.rightFrontLeg.getInitialPose().z;
            this.rightFrontLeg.yRot = 0.0F;
            this.leftHindLeg.x = this.leftHindLeg.getInitialPose().x;
            this.leftHindLeg.y = this.leftHindLeg.getInitialPose().y;
            this.leftHindLeg.z = this.leftHindLeg.getInitialPose().z;
            this.leftHindLeg.yRot = 0.0F;
            this.rightHindLeg.x = this.rightHindLeg.getInitialPose().x;
            this.rightHindLeg.y = this.rightHindLeg.getInitialPose().y;
            this.rightHindLeg.z = this.rightHindLeg.getInitialPose().z;
            this.rightHindLeg.yRot = 0.0F;
            this.hasResetFromLaying = true;
        }
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}