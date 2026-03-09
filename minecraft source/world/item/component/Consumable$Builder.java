/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.item.ItemUseAnimation;
/*     */ import net.minecraft.world.item.consume_effects.ConsumeEffect;
/*     */ import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/* 137 */   private float consumeSeconds = 1.6F;
/* 138 */   private ItemUseAnimation animation = ItemUseAnimation.EAT;
/* 139 */   private Holder<SoundEvent> sound = SoundEvents.GENERIC_EAT;
/*     */   private boolean hasConsumeParticles = true;
/* 141 */   private final List<ConsumeEffect> onConsumeEffects = new ArrayList();
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder consumeSeconds(float consumeSeconds) {
/* 146 */     this.consumeSeconds = consumeSeconds;
/* 147 */     return this;
/*     */   }
/*     */   
/*     */   public Builder animation(ItemUseAnimation animation) {
/* 151 */     this.animation = animation;
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   public Builder sound(Holder<SoundEvent> sound) {
/* 156 */     this.sound = sound;
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 161 */   public Builder soundAfterConsume(Holder<SoundEvent> soundAfterConsume) { return onConsume(new PlaySoundConsumeEffect(soundAfterConsume)); }
/*     */ 
/*     */   
/*     */   public Builder hasConsumeParticles(boolean hasConsumeParticles) {
/* 165 */     this.hasConsumeParticles = hasConsumeParticles;
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public Builder onConsume(ConsumeEffect effect) {
/* 170 */     this.onConsumeEffects.add(effect);
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 175 */   public Consumable build() { return new Consumable(this.consumeSeconds, this.animation, this.sound, this.hasConsumeParticles, this.onConsumeEffects); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\Consumable$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */