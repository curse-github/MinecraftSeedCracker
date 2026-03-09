/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.numbers.NumberFormat;
/*     */ import net.minecraft.network.chat.numbers.NumberFormatTypes;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
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
/*     */ public final class Packed
/*     */   extends Record
/*     */ {
/*     */   private final String name;
/*     */   private final ObjectiveCriteria criteria;
/*     */   private final Component displayName;
/*     */   private final ObjectiveCriteria.RenderType renderType;
/*     */   private final boolean displayAutoUpdate;
/*     */   private final Optional<NumberFormat> numberFormat;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/Objective$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/scores/Objective$Packed; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/Objective$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/scores/Objective$Packed; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/Objective$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/scores/Objective$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 112 */   public Packed(String name, ObjectiveCriteria criteria, Component displayName, ObjectiveCriteria.RenderType renderType, boolean displayAutoUpdate, Optional<NumberFormat> numberFormat) { this.name = name; this.criteria = criteria; this.displayName = displayName; this.renderType = renderType; this.displayAutoUpdate = displayAutoUpdate; this.numberFormat = numberFormat; } public String name() { return this.name; } public ObjectiveCriteria criteria() { return this.criteria; } public Component displayName() { return this.displayName; } public ObjectiveCriteria.RenderType renderType() { return this.renderType; } public boolean displayAutoUpdate() { return this.displayAutoUpdate; } public Optional<NumberFormat> numberFormat() { return this.numberFormat; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 121 */         .fieldOf("Name").forGetter(Packed::name), ObjectiveCriteria.CODEC
/* 122 */         .optionalFieldOf("CriteriaName", ObjectiveCriteria.DUMMY).forGetter(Packed::criteria), ComponentSerialization.CODEC
/* 123 */         .fieldOf("DisplayName").forGetter(Packed::displayName), ObjectiveCriteria.RenderType.CODEC
/* 124 */         .optionalFieldOf("RenderType", ObjectiveCriteria.RenderType.INTEGER).forGetter(Packed::renderType), Codec.BOOL
/* 125 */         .optionalFieldOf("display_auto_update", Boolean.valueOf(false)).forGetter(Packed::displayAutoUpdate), NumberFormatTypes.CODEC
/* 126 */         .optionalFieldOf("format").forGetter(Packed::numberFormat))
/* 127 */       .apply(i, Packed::new));
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\Objective$Packed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */