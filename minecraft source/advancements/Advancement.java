/*     */ package net.minecraft.advancements;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.advancements.criterion.CriterionValidator;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public final class Advancement extends Record {
/*     */   private final Optional<Identifier> parent;
/*     */   private final Optional<DisplayInfo> display;
/*     */   private final AdvancementRewards rewards;
/*     */   
/*  29 */   public Advancement(Optional<Identifier> parent, Optional<DisplayInfo> display, AdvancementRewards rewards, Map<String, Criterion<?>> criteria, AdvancementRequirements requirements, boolean sendsTelemetryEvent, Optional<Component> name) { this.parent = parent; this.display = display; this.rewards = rewards; this.criteria = criteria; this.requirements = requirements; this.sendsTelemetryEvent = sendsTelemetryEvent; this.name = name; } private final Map<String, Criterion<?>> criteria; private final AdvancementRequirements requirements; private final boolean sendsTelemetryEvent; private final Optional<Component> name; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/Advancement;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/Advancement; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/Advancement;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/Advancement; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/Advancement;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/Advancement;
/*  29 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Identifier> parent() { return this.parent; } public Optional<DisplayInfo> display() { return this.display; } public AdvancementRewards rewards() { return this.rewards; } public Map<String, Criterion<?>> criteria() { return this.criteria; } public AdvancementRequirements requirements() { return this.requirements; } public boolean sendsTelemetryEvent() { return this.sendsTelemetryEvent; } public Optional<Component> name() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   private static final Codec<Map<String, Criterion<?>>> CRITERIA_CODEC = Codec.unboundedMap(Codec.STRING, Criterion.CODEC)
/*  39 */     .validate(criteria -> criteria.isEmpty() ? DataResult.error(()) : DataResult.success(criteria));
/*     */   
/*  41 */   public static final Codec<Advancement> CODEC = RecordCodecBuilder.create(i -> i.group(Identifier.CODEC
/*  42 */         .optionalFieldOf("parent").forGetter(Advancement::parent), DisplayInfo.CODEC
/*  43 */         .optionalFieldOf("display").forGetter(Advancement::display), AdvancementRewards.CODEC
/*  44 */         .optionalFieldOf("rewards", AdvancementRewards.EMPTY).forGetter(Advancement::rewards), CRITERIA_CODEC
/*  45 */         .fieldOf("criteria").forGetter(Advancement::criteria), AdvancementRequirements.CODEC
/*  46 */         .optionalFieldOf("requirements").forGetter(()), Codec.BOOL
/*  47 */         .optionalFieldOf("sends_telemetry_event", Boolean.valueOf(false)).forGetter(Advancement::sendsTelemetryEvent))
/*  48 */       .apply(i, ()))
/*     */ 
/*     */     
/*  51 */     .validate(Advancement::validate);
/*     */   
/*  53 */   public static final StreamCodec<RegistryFriendlyByteBuf, Advancement> STREAM_CODEC = StreamCodec.ofMember(Advancement::write, Advancement::read);
/*     */ 
/*     */   
/*  56 */   private static DataResult<Advancement> validate(Advancement advancement) { return advancement.requirements().validate(advancement.criteria().keySet()).map(r -> advancement); }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public Advancement(Optional<Identifier> parent, Optional<DisplayInfo> display, AdvancementRewards rewards, Map<String, Criterion<?>> criteria, AdvancementRequirements requirements, boolean sendsTelemetryEvent) { this(parent, display, rewards, Map.copyOf(criteria), requirements, sendsTelemetryEvent, display.map(Advancement::decorateName)); }
/*     */ 
/*     */   
/*     */   private static Component decorateName(DisplayInfo display) {
/*  64 */     Component displayTitle = display.getTitle();
/*  65 */     ChatFormatting color = display.getType().getChatColor();
/*     */     
/*  67 */     MutableComponent mutableComponent1 = ComponentUtils.mergeStyles(displayTitle.copy(), Style.EMPTY.withColor(color)).append("\n").append(display.getDescription());
/*  68 */     MutableComponent mutableComponent2 = displayTitle.copy().withStyle(s -> s.withHoverEvent(new HoverEvent.ShowText(tooltip)));
/*     */     
/*  70 */     return ComponentUtils.wrapInSquareBrackets(mutableComponent2).withStyle(color);
/*     */   }
/*     */ 
/*     */   
/*  74 */   public static Component name(AdvancementHolder holder) { return (Component)holder.value().name().orElseGet(() -> Component.literal(holder.id().toString())); }
/*     */ 
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  78 */     output.writeOptional(this.parent, FriendlyByteBuf::writeIdentifier);
/*  79 */     DisplayInfo.STREAM_CODEC.apply(ByteBufCodecs::optional).encode(output, this.display);
/*  80 */     this.requirements.write(output);
/*  81 */     output.writeBoolean(this.sendsTelemetryEvent);
/*     */   }
/*     */   
/*     */   private static Advancement read(RegistryFriendlyByteBuf input) {
/*  85 */     return new Advancement(input
/*  86 */         .readOptional(FriendlyByteBuf::readIdentifier), (Optional)DisplayInfo.STREAM_CODEC
/*  87 */         .apply(ByteBufCodecs::optional).decode(input), AdvancementRewards.EMPTY, 
/*     */         
/*  89 */         Map.of(), new AdvancementRequirements(input), input
/*     */         
/*  91 */         .readBoolean());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public boolean isRoot() { return this.parent.isEmpty(); }
/*     */ 
/*     */   
/*     */   public void validate(ProblemReporter reporter, HolderGetter.Provider lootData) {
/* 100 */     this.criteria.forEach((name, criterion) -> {
/* 101 */           CriterionValidator validator = new CriterionValidator(reporter.forChild(new ProblemReporter.RootFieldPathElement(name)), lootData);
/* 102 */           criterion.triggerInstance().validate(validator);
/*     */         });
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 107 */     private Optional<Identifier> parent = Optional.empty();
/* 108 */     private Optional<DisplayInfo> display = Optional.empty();
/* 109 */     private AdvancementRewards rewards = AdvancementRewards.EMPTY;
/* 110 */     private final ImmutableMap.Builder<String, Criterion<?>> criteria = ImmutableMap.builder();
/* 111 */     private Optional<AdvancementRequirements> requirements = Optional.empty();
/* 112 */     private AdvancementRequirements.Strategy requirementsStrategy = AdvancementRequirements.Strategy.AND;
/*     */     
/*     */     private boolean sendsTelemetryEvent;
/*     */     
/* 116 */     public static Builder advancement() { return (new Builder()).sendsTelemetryEvent(); }
/*     */ 
/*     */ 
/*     */     
/* 120 */     public static Builder recipeAdvancement() { return new Builder(); }
/*     */ 
/*     */     
/*     */     public Builder parent(AdvancementHolder parent) {
/* 124 */       this.parent = Optional.of(parent.id());
/* 125 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated(forRemoval = true)
/*     */     public Builder parent(Identifier parent) {
/* 131 */       this.parent = Optional.of(parent);
/* 132 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 136 */     public Builder display(ItemStack icon, Component title, Component description, Identifier background, AdvancementType frame, boolean showToast, boolean announceChat, boolean hidden) { return display(new DisplayInfo(icon, title, description, Optional.ofNullable(background).map(net.minecraft.core.ClientAsset.ResourceTexture::new), frame, showToast, announceChat, hidden)); }
/*     */ 
/*     */ 
/*     */     
/* 140 */     public Builder display(ItemLike icon, Component title, Component description, Identifier background, AdvancementType frame, boolean showToast, boolean announceChat, boolean hidden) { return display(new DisplayInfo(new ItemStack(icon.asItem()), title, description, Optional.ofNullable(background).map(net.minecraft.core.ClientAsset.ResourceTexture::new), frame, showToast, announceChat, hidden)); }
/*     */ 
/*     */     
/*     */     public Builder display(DisplayInfo display) {
/* 144 */       this.display = Optional.of(display);
/* 145 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 149 */     public Builder rewards(AdvancementRewards.Builder rewards) { return rewards(rewards.build()); }
/*     */ 
/*     */     
/*     */     public Builder rewards(AdvancementRewards rewards) {
/* 153 */       this.rewards = rewards;
/* 154 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addCriterion(String name, Criterion<?> criterion) {
/* 158 */       this.criteria.put(name, criterion);
/* 159 */       return this;
/*     */     }
/*     */     
/*     */     public Builder requirements(AdvancementRequirements.Strategy strategy) {
/* 163 */       this.requirementsStrategy = strategy;
/* 164 */       return this;
/*     */     }
/*     */     
/*     */     public Builder requirements(AdvancementRequirements requirements) {
/* 168 */       this.requirements = Optional.of(requirements);
/* 169 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sendsTelemetryEvent() {
/* 173 */       this.sendsTelemetryEvent = true;
/* 174 */       return this;
/*     */     }
/*     */     
/*     */     public AdvancementHolder build(Identifier id) {
/* 178 */       ImmutableMap immutableMap = this.criteria.buildOrThrow();
/* 179 */       AdvancementRequirements requirements = (AdvancementRequirements)this.requirements.orElseGet(() -> this.requirementsStrategy.create(criteria.keySet()));
/* 180 */       return new AdvancementHolder(id, new Advancement(this.parent, this.display, this.rewards, immutableMap, requirements, this.sendsTelemetryEvent));
/*     */     }
/*     */     
/*     */     public AdvancementHolder save(Consumer<AdvancementHolder> output, String name) {
/* 184 */       AdvancementHolder advancement = build(Identifier.parse(name));
/* 185 */       output.accept(advancement);
/* 186 */       return advancement;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\Advancement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */