/*    */ package net.minecraft.core;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class BlockBox extends Record implements Iterable<BlockPos> {
/*    */   private final BlockPos min;
/*    */   private final BlockPos max;
/*    */   
/* 10 */   public BlockPos min() { return this.min; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/BlockBox;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/BlockBox; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/BlockBox;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/BlockBox; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/BlockBox;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/BlockBox;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public BlockPos max() { return this.max; }
/* 11 */   public static final StreamCodec<ByteBuf, BlockBox> STREAM_CODEC = new StreamCodec<ByteBuf, BlockBox>()
/*    */     {
/*    */       public BlockBox decode(ByteBuf input) {
/* 14 */         return new BlockBox(
/* 15 */             FriendlyByteBuf.readBlockPos(input), 
/* 16 */             FriendlyByteBuf.readBlockPos(input));
/*    */       }
/*    */ 
/*    */ 
/*    */       
/*    */       public void encode(ByteBuf output, BlockBox value) {
/* 22 */         FriendlyByteBuf.writeBlockPos(output, value.min());
/* 23 */         FriendlyByteBuf.writeBlockPos(output, value.max());
/*    */       }
/*    */     };
/*    */   
/*    */   public BlockBox(BlockPos min, BlockPos max) {
/* 28 */     this.min = BlockPos.min(min, max);
/* 29 */     this.max = BlockPos.max(min, max);
/*    */   }
/*    */ 
/*    */   
/* 33 */   public static BlockBox of(BlockPos pos) { return new BlockBox(pos, pos); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static BlockBox of(BlockPos a, BlockPos b) { return new BlockBox(a, b); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public BlockBox include(BlockPos pos) { return new BlockBox(BlockPos.min(this.min, pos), BlockPos.max(this.max, pos)); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public boolean isBlock() { return this.min.equals(this.max); }
/*    */ 
/*    */   
/*    */   public boolean contains(BlockPos pos) {
/* 49 */     return (pos.getX() >= this.min.getX() && pos.getY() >= this.min.getY() && pos.getZ() >= this.min.getZ() && pos
/* 50 */       .getX() <= this.max.getX() && pos.getY() <= this.max.getY() && pos.getZ() <= this.max.getZ());
/*    */   }
/*    */ 
/*    */   
/* 54 */   public AABB aabb() { return AABB.encapsulatingFullBlocks(this.min, this.max); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public Iterator<BlockPos> iterator() { return BlockPos.betweenClosed(this.min, this.max).iterator(); }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public int sizeX() { return this.max.getX() - this.min.getX() + 1; }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public int sizeY() { return this.max.getY() - this.min.getY() + 1; }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public int sizeZ() { return this.max.getZ() - this.min.getZ() + 1; }
/*    */ 
/*    */   
/*    */   public BlockBox extend(Direction direction, int amount) {
/* 75 */     if (amount == 0) {
/* 76 */       return this;
/*    */     }
/* 78 */     if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
/* 79 */       return of(this.min, BlockPos.max(this.min, this.max.relative(direction, amount)));
/*    */     }
/* 81 */     return of(BlockPos.min(this.min.relative(direction, amount), this.max), this.max);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockBox move(Direction direction, int amount) {
/* 86 */     if (amount == 0) {
/* 87 */       return this;
/*    */     }
/* 89 */     return new BlockBox(this.min
/* 90 */         .relative(direction, amount), this.max
/* 91 */         .relative(direction, amount));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockBox offset(Vec3i offset) {
/* 96 */     return new BlockBox(this.min
/* 97 */         .offset(offset), this.max
/* 98 */         .offset(offset));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\BlockBox.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */