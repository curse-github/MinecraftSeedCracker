/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DamageReduction
/*     */   extends Record
/*     */ {
/*     */   private final float horizontalBlockingAngle;
/*     */   private final Optional<HolderSet<DamageType>> type;
/*     */   private final float base;
/*     */   private final float factor;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 115 */   public DamageReduction(float horizontalBlockingAngle, Optional<HolderSet<DamageType>> type, float base, float factor) { this.horizontalBlockingAngle = horizontalBlockingAngle; this.type = type; this.base = base; this.factor = factor; } public float horizontalBlockingAngle() { return this.horizontalBlockingAngle; } public Optional<HolderSet<DamageType>> type() { return this.type; } public float base() { return this.base; } public float factor() { return this.factor; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public static final Codec<DamageReduction> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.POSITIVE_FLOAT
/* 123 */         .optionalFieldOf("horizontal_blocking_angle", Float.valueOf(90.0F)).forGetter(DamageReduction::horizontalBlockingAngle), 
/* 124 */         RegistryCodecs.homogeneousList(Registries.DAMAGE_TYPE).optionalFieldOf("type").forGetter(DamageReduction::type), Codec.FLOAT
/* 125 */         .fieldOf("base").forGetter(DamageReduction::base), Codec.FLOAT
/* 126 */         .fieldOf("factor").forGetter(DamageReduction::factor))
/* 127 */       .apply(i, DamageReduction::new));
/* 128 */   public static final StreamCodec<RegistryFriendlyByteBuf, DamageReduction> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, DamageReduction::horizontalBlockingAngle, 
/*     */       
/* 130 */       ByteBufCodecs.holderSet(Registries.DAMAGE_TYPE).apply(ByteBufCodecs::optional), DamageReduction::type, ByteBufCodecs.FLOAT, DamageReduction::base, ByteBufCodecs.FLOAT, DamageReduction::factor, DamageReduction::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float resolve(DamageSource source, float dealtDamage, double angle) {
/* 137 */     if (angle > (0.017453292F * this.horizontalBlockingAngle)) {
/* 138 */       return 0.0F;
/*     */     }
/*     */     
/* 141 */     if (this.type.isPresent() && !((HolderSet)this.type.get()).contains(source.typeHolder())) {
/* 142 */       return 0.0F;
/*     */     }
/* 144 */     return Mth.clamp(this.base + this.factor * dealtDamage, 0.0F, dealtDamage);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\BlocksAttacks$DamageReduction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */