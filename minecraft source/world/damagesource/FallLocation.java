/*    */ package net.minecraft.world.damagesource;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class FallLocation extends Record {
/*    */   private final String id;
/*    */   
/* 12 */   public FallLocation(String id) { this.id = id; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/damagesource/FallLocation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/damagesource/FallLocation; } public String id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/damagesource/FallLocation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/damagesource/FallLocation; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/damagesource/FallLocation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/damagesource/FallLocation;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   public static final FallLocation GENERIC = new FallLocation("generic");
/* 14 */   public static final FallLocation LADDER = new FallLocation("ladder");
/* 15 */   public static final FallLocation VINES = new FallLocation("vines");
/* 16 */   public static final FallLocation WEEPING_VINES = new FallLocation("weeping_vines");
/* 17 */   public static final FallLocation TWISTING_VINES = new FallLocation("twisting_vines");
/* 18 */   public static final FallLocation SCAFFOLDING = new FallLocation("scaffolding");
/* 19 */   public static final FallLocation OTHER_CLIMBABLE = new FallLocation("other_climbable");
/* 20 */   public static final FallLocation WATER = new FallLocation("water");
/*    */   
/*    */   public static FallLocation blockToFallLocation(BlockState blockState) {
/* 23 */     if (blockState.is(Blocks.LADDER) || blockState.is(BlockTags.TRAPDOORS))
/* 24 */       return LADDER; 
/* 25 */     if (blockState.is(Blocks.VINE))
/* 26 */       return VINES; 
/* 27 */     if (blockState.is(Blocks.WEEPING_VINES) || blockState.is(Blocks.WEEPING_VINES_PLANT))
/* 28 */       return WEEPING_VINES; 
/* 29 */     if (blockState.is(Blocks.TWISTING_VINES) || blockState.is(Blocks.TWISTING_VINES_PLANT))
/* 30 */       return TWISTING_VINES; 
/* 31 */     if (blockState.is(Blocks.SCAFFOLDING)) {
/* 32 */       return SCAFFOLDING;
/*    */     }
/* 34 */     return OTHER_CLIMBABLE;
/*    */   }
/*    */   
/*    */   public static FallLocation getCurrentFallLocation(LivingEntity mob) {
/* 38 */     Optional<BlockPos> lastClimbablePos = mob.getLastClimbablePos();
/* 39 */     if (lastClimbablePos.isPresent()) {
/* 40 */       BlockState blockState = mob.level().getBlockState((BlockPos)lastClimbablePos.get());
/* 41 */       return blockToFallLocation(blockState);
/*    */     } 
/* 43 */     if (mob.isInWater()) {
/* 44 */       return WATER;
/*    */     }
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/* 50 */   public String languageKey() { return "death.fell.accident." + this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\damagesource\FallLocation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */