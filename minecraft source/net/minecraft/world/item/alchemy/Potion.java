/*    */ package net.minecraft.world.item.alchemy;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.flag.FeatureElement;
/*    */ import net.minecraft.world.flag.FeatureFlag;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ import net.minecraft.world.flag.FeatureFlags;
/*    */ 
/*    */ public class Potion implements FeatureElement {
/* 19 */   public static final Codec<Holder<Potion>> CODEC = BuiltInRegistries.POTION.holderByNameCodec();
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Potion>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.POTION);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   public Potion(String name, MobEffectInstance... effects) {
/* 25 */     this.requiredFeatures = FeatureFlags.VANILLA_SET;
/*    */ 
/*    */     
/* 28 */     this.name = name;
/* 29 */     this.effects = List.of(effects);
/*    */   }
/*    */   private final List<MobEffectInstance> effects; private FeatureFlagSet requiredFeatures;
/*    */   public Potion requiredFeatures(FeatureFlag... flags) {
/* 33 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public FeatureFlagSet requiredFeatures() { return this.requiredFeatures; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public List<MobEffectInstance> getEffects() { return this.effects; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public String name() { return this.name; }
/*    */ 
/*    */   
/*    */   public boolean hasInstantEffects() {
/* 51 */     for (MobEffectInstance effect : this.effects) {
/* 52 */       if (((MobEffect)effect.getEffect().value()).isInstantenous()) {
/* 53 */         return true;
/*    */       }
/*    */     } 
/* 56 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\alchemy\Potion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */