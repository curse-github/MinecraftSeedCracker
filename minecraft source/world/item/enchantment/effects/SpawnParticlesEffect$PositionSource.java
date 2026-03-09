/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PositionSource
/*    */   extends Record
/*    */ {
/*    */   private final SpawnParticlesEffect.PositionSourceType type;
/*    */   private final float offset;
/*    */   private final float scale;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 66 */   public PositionSource(SpawnParticlesEffect.PositionSourceType type, float offset, float scale) { this.type = type; this.offset = offset; this.scale = scale; } public SpawnParticlesEffect.PositionSourceType type() { return this.type; } public float offset() { return this.offset; } public float scale() { return this.scale; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public static final MapCodec<PositionSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SpawnParticlesEffect.PositionSourceType.CODEC
/* 72 */         .fieldOf("type").forGetter(PositionSource::type), Codec.FLOAT
/* 73 */         .optionalFieldOf("offset", Float.valueOf(0.0F)).forGetter(PositionSource::offset), ExtraCodecs.POSITIVE_FLOAT
/* 74 */         .optionalFieldOf("scale", Float.valueOf(1.0F)).forGetter(PositionSource::scale))
/* 75 */       .apply(i, PositionSource::new)).validate(positioning -> {
/* 76 */         if (positioning.type() == SpawnParticlesEffect.PositionSourceType.ENTITY_POSITION && positioning.scale() != 1.0F) {
/* 77 */           return DataResult.error(());
/*    */         }
/* 79 */         return DataResult.success(positioning);
/*    */       });
/*    */ 
/*    */   
/* 83 */   public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) { return this.type.getCoordinate(position, center, boundingBoxSpan * this.scale, random) + this.offset; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\SpawnParticlesEffect$PositionSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */