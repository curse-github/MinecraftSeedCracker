/*     */ package net.minecraft.world.scores;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.numbers.NumberFormat;
/*     */ import net.minecraft.network.chat.numbers.NumberFormatTypes;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ 
/*     */ public class Objective {
/*     */   private final Scoreboard scoreboard;
/*     */   private final String name;
/*     */   private final ObjectiveCriteria criteria;
/*     */   private Component displayName;
/*     */   private Component formattedDisplayName;
/*     */   private ObjectiveCriteria.RenderType renderType;
/*     */   private boolean displayAutoUpdate;
/*     */   private NumberFormat numberFormat;
/*     */   
/*     */   public Objective(Scoreboard scoreboard, String name, ObjectiveCriteria criteria, Component displayName, ObjectiveCriteria.RenderType renderType, boolean displayAutoUpdate, NumberFormat numberFormat) {
/*  28 */     this.scoreboard = scoreboard;
/*  29 */     this.name = name;
/*  30 */     this.criteria = criteria;
/*  31 */     this.displayName = displayName;
/*  32 */     this.formattedDisplayName = createFormattedDisplayName();
/*  33 */     this.renderType = renderType;
/*  34 */     this.displayAutoUpdate = displayAutoUpdate;
/*  35 */     this.numberFormat = numberFormat;
/*     */   }
/*     */ 
/*     */   
/*  39 */   public Packed pack() { return new Packed(this.name, this.criteria, this.displayName, this.renderType, this.displayAutoUpdate, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  45 */         Optional.ofNullable(this.numberFormat)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public Scoreboard getScoreboard() { return this.scoreboard; }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public ObjectiveCriteria getCriteria() { return this.criteria; }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public Component getDisplayName() { return this.displayName; }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public boolean displayAutoUpdate() { return this.displayAutoUpdate; }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public NumberFormat numberFormat() { return this.numberFormat; }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public NumberFormat numberFormatOrDefault(NumberFormat _default) { return (NumberFormat)Objects.requireNonNullElse(this.numberFormat, _default); }
/*     */ 
/*     */   
/*     */   private Component createFormattedDisplayName() {
/*  78 */     return ComponentUtils.wrapInSquareBrackets(this.displayName
/*  79 */         .copy().withStyle(s -> s.withHoverEvent(new HoverEvent.ShowText(Component.literal(this.name)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public Component getFormattedDisplayName() { return this.formattedDisplayName; }
/*     */ 
/*     */   
/*     */   public void setDisplayName(Component name) {
/*  88 */     this.displayName = name;
/*  89 */     this.formattedDisplayName = createFormattedDisplayName();
/*  90 */     this.scoreboard.onObjectiveChanged(this);
/*     */   }
/*     */ 
/*     */   
/*  94 */   public ObjectiveCriteria.RenderType getRenderType() { return this.renderType; }
/*     */ 
/*     */   
/*     */   public void setRenderType(ObjectiveCriteria.RenderType renderType) {
/*  98 */     this.renderType = renderType;
/*  99 */     this.scoreboard.onObjectiveChanged(this);
/*     */   }
/*     */   
/*     */   public void setDisplayAutoUpdate(boolean displayAutoUpdate) {
/* 103 */     this.displayAutoUpdate = displayAutoUpdate;
/* 104 */     this.scoreboard.onObjectiveChanged(this);
/*     */   }
/*     */   
/*     */   public void setNumberFormat(NumberFormat numberFormat) {
/* 108 */     this.numberFormat = numberFormat;
/* 109 */     this.scoreboard.onObjectiveChanged(this);
/*     */   }
/*     */   public static final class Packed extends Record { private final String name; private final ObjectiveCriteria criteria; private final Component displayName;
/* 112 */     public Packed(String name, ObjectiveCriteria criteria, Component displayName, ObjectiveCriteria.RenderType renderType, boolean displayAutoUpdate, Optional<NumberFormat> numberFormat) { this.name = name; this.criteria = criteria; this.displayName = displayName; this.renderType = renderType; this.displayAutoUpdate = displayAutoUpdate; this.numberFormat = numberFormat; } private final ObjectiveCriteria.RenderType renderType; private final boolean displayAutoUpdate; private final Optional<NumberFormat> numberFormat; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/Objective$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/scores/Objective$Packed; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/Objective$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/scores/Objective$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/Objective$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/scores/Objective$Packed;
/* 112 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public ObjectiveCriteria criteria() { return this.criteria; } public Component displayName() { return this.displayName; } public ObjectiveCriteria.RenderType renderType() { return this.renderType; } public boolean displayAutoUpdate() { return this.displayAutoUpdate; } public Optional<NumberFormat> numberFormat() { return this.numberFormat; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 121 */           .fieldOf("Name").forGetter(Packed::name), ObjectiveCriteria.CODEC
/* 122 */           .optionalFieldOf("CriteriaName", ObjectiveCriteria.DUMMY).forGetter(Packed::criteria), ComponentSerialization.CODEC
/* 123 */           .fieldOf("DisplayName").forGetter(Packed::displayName), ObjectiveCriteria.RenderType.CODEC
/* 124 */           .optionalFieldOf("RenderType", ObjectiveCriteria.RenderType.INTEGER).forGetter(Packed::renderType), Codec.BOOL
/* 125 */           .optionalFieldOf("display_auto_update", Boolean.valueOf(false)).forGetter(Packed::displayAutoUpdate), NumberFormatTypes.CODEC
/* 126 */           .optionalFieldOf("format").forGetter(Packed::numberFormat))
/* 127 */         .apply(i, Packed::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\Objective.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */