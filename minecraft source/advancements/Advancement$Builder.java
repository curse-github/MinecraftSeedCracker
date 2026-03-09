/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
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
/* 107 */   private Optional<Identifier> parent = Optional.empty();
/* 108 */   private Optional<DisplayInfo> display = Optional.empty();
/* 109 */   private AdvancementRewards rewards = AdvancementRewards.EMPTY;
/* 110 */   private final ImmutableMap.Builder<String, Criterion<?>> criteria = ImmutableMap.builder();
/* 111 */   private Optional<AdvancementRequirements> requirements = Optional.empty();
/* 112 */   private AdvancementRequirements.Strategy requirementsStrategy = AdvancementRequirements.Strategy.AND;
/*     */   
/*     */   private boolean sendsTelemetryEvent;
/*     */   
/* 116 */   public static Builder advancement() { return (new Builder()).sendsTelemetryEvent(); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static Builder recipeAdvancement() { return new Builder(); }
/*     */ 
/*     */   
/*     */   public Builder parent(AdvancementHolder parent) {
/* 124 */     this.parent = Optional.of(parent.id());
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public Builder parent(Identifier parent) {
/* 131 */     this.parent = Optional.of(parent);
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 136 */   public Builder display(ItemStack icon, Component title, Component description, Identifier background, AdvancementType frame, boolean showToast, boolean announceChat, boolean hidden) { return display(new DisplayInfo(icon, title, description, Optional.ofNullable(background).map(net.minecraft.core.ClientAsset.ResourceTexture::new), frame, showToast, announceChat, hidden)); }
/*     */ 
/*     */ 
/*     */   
/* 140 */   public Builder display(ItemLike icon, Component title, Component description, Identifier background, AdvancementType frame, boolean showToast, boolean announceChat, boolean hidden) { return display(new DisplayInfo(new ItemStack(icon.asItem()), title, description, Optional.ofNullable(background).map(net.minecraft.core.ClientAsset.ResourceTexture::new), frame, showToast, announceChat, hidden)); }
/*     */ 
/*     */   
/*     */   public Builder display(DisplayInfo display) {
/* 144 */     this.display = Optional.of(display);
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 149 */   public Builder rewards(AdvancementRewards.Builder rewards) { return rewards(rewards.build()); }
/*     */ 
/*     */   
/*     */   public Builder rewards(AdvancementRewards rewards) {
/* 153 */     this.rewards = rewards;
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addCriterion(String name, Criterion<?> criterion) {
/* 158 */     this.criteria.put(name, criterion);
/* 159 */     return this;
/*     */   }
/*     */   
/*     */   public Builder requirements(AdvancementRequirements.Strategy strategy) {
/* 163 */     this.requirementsStrategy = strategy;
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public Builder requirements(AdvancementRequirements requirements) {
/* 168 */     this.requirements = Optional.of(requirements);
/* 169 */     return this;
/*     */   }
/*     */   
/*     */   public Builder sendsTelemetryEvent() {
/* 173 */     this.sendsTelemetryEvent = true;
/* 174 */     return this;
/*     */   }
/*     */   
/*     */   public AdvancementHolder build(Identifier id) {
/* 178 */     ImmutableMap immutableMap = this.criteria.buildOrThrow();
/* 179 */     AdvancementRequirements requirements = (AdvancementRequirements)this.requirements.orElseGet(() -> this.requirementsStrategy.create(criteria.keySet()));
/* 180 */     return new AdvancementHolder(id, new Advancement(this.parent, this.display, this.rewards, immutableMap, requirements, this.sendsTelemetryEvent));
/*     */   }
/*     */   
/*     */   public AdvancementHolder save(Consumer<AdvancementHolder> output, String name) {
/* 184 */     AdvancementHolder advancement = build(Identifier.parse(name));
/* 185 */     output.accept(advancement);
/* 186 */     return advancement;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\Advancement$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */