/*    */ package net.minecraft.core;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class GlobalPos extends Record {
/*    */   private final ResourceKey<Level> dimension;
/*    */   private final BlockPos pos;
/*    */   
/* 12 */   public GlobalPos(ResourceKey<Level> dimension, BlockPos pos) { this.dimension = dimension; this.pos = pos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/GlobalPos;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/core/GlobalPos; } public ResourceKey<Level> dimension() { return this.dimension; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/GlobalPos;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/GlobalPos;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public BlockPos pos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final MapCodec<GlobalPos> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Level.RESOURCE_KEY_CODEC
/* 17 */         .fieldOf("dimension").forGetter(GlobalPos::dimension), BlockPos.CODEC
/* 18 */         .fieldOf("pos").forGetter(GlobalPos::pos))
/* 19 */       .apply(i, GlobalPos::of));
/* 20 */   public static final Codec<GlobalPos> CODEC = MAP_CODEC.codec();
/*    */   
/* 22 */   public static final StreamCodec<ByteBuf, GlobalPos> STREAM_CODEC = StreamCodec.composite(
/* 23 */       ResourceKey.streamCodec(Registries.DIMENSION), GlobalPos::dimension, BlockPos.STREAM_CODEC, GlobalPos::pos, GlobalPos::of);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static GlobalPos of(ResourceKey<Level> dimension, BlockPos pos) { return new GlobalPos(dimension, pos); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public String toString() { return String.valueOf(this.dimension) + " " + String.valueOf(this.dimension); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public boolean isCloseEnough(ResourceKey<Level> dimension, BlockPos pos, int maxDistance) { return (this.dimension.equals(dimension) && this.pos.distChessboard(pos) <= maxDistance); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\GlobalPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */