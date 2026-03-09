/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.SpikeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*    */ 
/*    */ public static final abstract enum DragonRespawnAnimation {
/*    */   START, PREPARING_TO_SUMMON_PILLARS, SUMMONING_PILLARS, SUMMONING_DRAGON, END;
/*    */   
/*    */   public abstract void tick(ServerLevel paramServerLevel, EndDragonFight paramEndDragonFight, List<EndCrystal> paramList, int paramInt, BlockPos paramBlockPos);
/*    */   
/*    */   static  {
/*    */     // Byte code:
/*    */     //   0: new net/minecraft/world/level/dimension/end/DragonRespawnAnimation$1
/*    */     //   3: dup
/*    */     //   4: ldc 'START'
/*    */     //   6: iconst_0
/*    */     //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   10: putstatic net/minecraft/world/level/dimension/end/DragonRespawnAnimation.START : Lnet/minecraft/world/level/dimension/end/DragonRespawnAnimation;
/*    */     //   13: new net/minecraft/world/level/dimension/end/DragonRespawnAnimation$2
/*    */     //   16: dup
/*    */     //   17: ldc 'PREPARING_TO_SUMMON_PILLARS'
/*    */     //   19: iconst_1
/*    */     //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   23: putstatic net/minecraft/world/level/dimension/end/DragonRespawnAnimation.PREPARING_TO_SUMMON_PILLARS : Lnet/minecraft/world/level/dimension/end/DragonRespawnAnimation;
/*    */     //   26: new net/minecraft/world/level/dimension/end/DragonRespawnAnimation$3
/*    */     //   29: dup
/*    */     //   30: ldc 'SUMMONING_PILLARS'
/*    */     //   32: iconst_2
/*    */     //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   36: putstatic net/minecraft/world/level/dimension/end/DragonRespawnAnimation.SUMMONING_PILLARS : Lnet/minecraft/world/level/dimension/end/DragonRespawnAnimation;
/*    */     //   39: new net/minecraft/world/level/dimension/end/DragonRespawnAnimation$4
/*    */     //   42: dup
/*    */     //   43: ldc 'SUMMONING_DRAGON'
/*    */     //   45: iconst_3
/*    */     //   46: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   49: putstatic net/minecraft/world/level/dimension/end/DragonRespawnAnimation.SUMMONING_DRAGON : Lnet/minecraft/world/level/dimension/end/DragonRespawnAnimation;
/*    */     //   52: new net/minecraft/world/level/dimension/end/DragonRespawnAnimation$5
/*    */     //   55: dup
/*    */     //   56: ldc 'END'
/*    */     //   58: iconst_4
/*    */     //   59: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   62: putstatic net/minecraft/world/level/dimension/end/DragonRespawnAnimation.END : Lnet/minecraft/world/level/dimension/end/DragonRespawnAnimation;
/*    */     //   65: invokestatic $values : ()[Lnet/minecraft/world/level/dimension/end/DragonRespawnAnimation;
/*    */     //   68: putstatic net/minecraft/world/level/dimension/end/DragonRespawnAnimation.$VALUES : [Lnet/minecraft/world/level/dimension/end/DragonRespawnAnimation;
/*    */     //   71: return
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     //   #28	-> 13
/*    */     //   #40	-> 26
/*    */     //   #75	-> 39
/*    */     //   #97	-> 52
/*    */     //   #17	-> 65
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\dimension\end\DragonRespawnAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */