package mrkacafirekcz.doggomod.client.render.entity;

import java.util.HashMap;
import java.util.Map;

import mrkacafirekcz.doggomod.DoggoAction;
import mrkacafirekcz.doggomod.DoggoMod;
import mrkacafirekcz.doggomod.client.render.entity.feature.DoggoWolfCollarFeatureRenderer;
import mrkacafirekcz.doggomod.client.render.entity.feature.DoggoWolfHeldItemFeatureRenderer;
import mrkacafirekcz.doggomod.client.render.entity.model.DoggoWolfModel;
import mrkacafirekcz.doggomod.client.render.entity.model.DoggoWolfModelDigging;
import mrkacafirekcz.doggomod.client.render.entity.model.DoggoWolfModelEatingFromBowl;
import mrkacafirekcz.doggomod.client.render.entity.model.DoggoWolfModelNapping;
import mrkacafirekcz.doggomod.client.render.entity.model.DoggoWolfModelNeutral;
import mrkacafirekcz.doggomod.client.render.entity.model.DoggoWolfModelScratching;
import mrkacafirekcz.doggomod.entity.DoggoWolf;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class DoggoWolfRenderer extends MobEntityRenderer<DoggoWolf, DoggoWolfModel<DoggoWolf>> {
	private static final Identifier WILD_TEXTURE = new Identifier("textures/entity/wolf/wolf.png");
	private static final Identifier TAMED_TEXTURE = new Identifier("textures/entity/wolf/wolf_tame.png");
	private static final Identifier ANGRY_TEXTURE = new Identifier("textures/entity/wolf/wolf_angry.png");
	private static final Identifier SLEEPING_TEXTURE = new Identifier(DoggoMod.MODID, "textures/entity/wolf/wolf_sleeping.png");

	private final Map<DoggoAction, DoggoWolfModel<DoggoWolf>> doggoModels = new HashMap<>();

	public DoggoWolfRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new DoggoWolfModelNeutral(), 0.5F);
		this.addFeature(new DoggoWolfCollarFeatureRenderer(this));
		this.addFeature(new DoggoWolfHeldItemFeatureRenderer(this));

		doggoModels.put(DoggoAction.DIGGING, new DoggoWolfModelDigging());
		doggoModels.put(DoggoAction.EATING, new DoggoWolfModelNeutral());
		doggoModels.put(DoggoAction.EATING_FROM_BOWL, new DoggoWolfModelEatingFromBowl());
		doggoModels.put(DoggoAction.NAPPING, new DoggoWolfModelNapping());
		doggoModels.put(DoggoAction.NEUTRAL, new DoggoWolfModelNeutral());
		doggoModels.put(DoggoAction.SCRATCHING, new DoggoWolfModelScratching());
	}

	protected float getAnimationProgress(DoggoWolf wolfEntity, float f) {
		return wolfEntity.getTailAngle();
	}

	public void render(DoggoWolf doggoWolf, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		this.model = doggoModels.get(doggoWolf.getAction());
		
		if(doggoWolf.isFurWet()) {
			float h = doggoWolf.getFurWetBrightnessMultiplier(g);
			((DoggoWolfModel<DoggoWolf>)this.model).setColorMultiplier(h, h, h);
		}
		
		super.render(doggoWolf, f, g, matrixStack, vertexConsumerProvider, i);
		
		if(doggoWolf.isFurWet()) {
			((DoggoWolfModel<DoggoWolf>)this.model).setColorMultiplier(1.0F, 1.0F, 1.0F);
		}
	}
	
	public Identifier getTexture(DoggoWolf doggoWolf) {
		if(doggoWolf.isTamed()) {
			return doggoWolf.isAction(DoggoAction.NAPPING) ? SLEEPING_TEXTURE : TAMED_TEXTURE;
		} else {
			return doggoWolf.hasAngerTime() ? ANGRY_TEXTURE : WILD_TEXTURE;
		}
	}
}
