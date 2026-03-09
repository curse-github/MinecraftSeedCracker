/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFixedCodec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   private final BlockPos pos;
/*     */   private final Holder<PoiType> poiType;
/*     */   private final int freeTickets;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  93 */   public Packed(BlockPos pos, Holder<PoiType> poiType, int freeTickets) { this.pos = pos; this.poiType = poiType; this.freeTickets = freeTickets; } public BlockPos pos() { return this.pos; } public Holder<PoiType> poiType() { return this.poiType; } public int freeTickets() { return this.freeTickets; }
/*  94 */   public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(BlockPos.CODEC
/*  95 */         .fieldOf("pos").forGetter(Packed::pos), 
/*  96 */         RegistryFixedCodec.create(Registries.POINT_OF_INTEREST_TYPE).fieldOf("type").forGetter(Packed::poiType), Codec.INT
/*  97 */         .fieldOf("free_tickets").orElse(Integer.valueOf(0)).forGetter(Packed::freeTickets))
/*  98 */       .apply(i, Packed::new));
/*     */ 
/*     */   
/* 101 */   public PoiRecord unpack(Runnable setDirty) { return new PoiRecord(this.pos, this.poiType, this.freeTickets, setDirty); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\village\poi\PoiRecord$Packed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */