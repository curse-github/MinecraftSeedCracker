/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ public static final abstract enum RunningPolicy {
/*     */   RUN_ONE, TRY_ALL;
/*     */   
/*     */   public abstract <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> paramStream, ServerLevel paramServerLevel, E paramE, long paramLong);
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy$1
/*     */     //   3: dup
/*     */     //   4: ldc 'RUN_ONE'
/*     */     //   6: iconst_0
/*     */     //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */     //   10: putstatic net/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy.RUN_ONE : Lnet/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy;
/*     */     //   13: new net/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy$2
/*     */     //   16: dup
/*     */     //   17: ldc 'TRY_ALL'
/*     */     //   19: iconst_1
/*     */     //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */     //   23: putstatic net/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy.TRY_ALL : Lnet/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy;
/*     */     //   26: invokestatic $values : ()[Lnet/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy;
/*     */     //   29: putstatic net/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy.$VALUES : [Lnet/minecraft/world/entity/ai/behavior/GateBehavior$RunningPolicy;
/*     */     //   32: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #113	-> 0
/*     */     //   #122	-> 13
/*     */     //   #112	-> 26
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GateBehavior$RunningPolicy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */