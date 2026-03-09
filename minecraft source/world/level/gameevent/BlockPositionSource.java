/*    */ package net.minecraft.world.level.gameevent;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class BlockPositionSource extends Record implements PositionSource {
/*    */   private final BlockPos pos;
/*    */   
/* 13 */   public BlockPositionSource(BlockPos pos) { this.pos = pos; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/BlockPositionSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/BlockPositionSource; } public BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/BlockPositionSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/BlockPositionSource; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/BlockPositionSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/gameevent/BlockPositionSource;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 14 */   public static final MapCodec<BlockPositionSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockPos.CODEC
/* 15 */         .fieldOf("pos").forGetter(BlockPositionSource::pos))
/* 16 */       .apply(i, BlockPositionSource::new));
/*    */   
/* 18 */   public static final StreamCodec<ByteBuf, BlockPositionSource> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, BlockPositionSource::pos, BlockPositionSource::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public Optional<Vec3> getPosition(Level level) { return Optional.of(Vec3.atCenterOf(this.pos)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public PositionSourceType<BlockPositionSource> getType() { return PositionSourceType.BLOCK; }
/*    */   
/*    */   public static class Type
/*    */     extends Object
/*    */     implements PositionSourceType<BlockPositionSource>
/*    */   {
/* 36 */     public MapCodec<BlockPositionSource> codec() { return BlockPositionSource.CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     public StreamCodec<ByteBuf, BlockPositionSource> streamCodec() { return BlockPositionSource.STREAM_CODEC; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\BlockPositionSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */