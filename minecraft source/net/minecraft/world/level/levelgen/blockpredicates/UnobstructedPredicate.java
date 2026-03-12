/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ final class UnobstructedPredicate extends Record implements BlockPredicate {
/*    */   private final Vec3i offset;
/*    */   
/* 10 */   UnobstructedPredicate(Vec3i offset) { this.offset = offset; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate; } public Vec3i offset() { return this.offset; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 11 */   public static MapCodec<UnobstructedPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Vec3i.CODEC
/* 12 */         .optionalFieldOf("offset", Vec3i.ZERO).forGetter(UnobstructedPredicate::offset))
/* 13 */       .apply(i, UnobstructedPredicate::new));
/*    */ 
/*    */ 
/*    */   
/* 17 */   public BlockPredicateType<?> type() { return BlockPredicateType.UNOBSTRUCTED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public boolean test(WorldGenLevel worldGenLevel, BlockPos pos) { return worldGenLevel.isUnobstructed(null, Shapes.block().move(pos)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\UnobstructedPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */