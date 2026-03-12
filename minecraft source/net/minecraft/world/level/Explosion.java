/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Explosion
/*    */ {
/* 14 */   static DamageSource getDefaultDamageSource(Level level, Entity source) { return level.damageSources().explosion(source, getIndirectSourceEntity(source)); }
/*    */ 
/*    */   
/*    */   static LivingEntity getIndirectSourceEntity(Entity source) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: astore_1
/*    */     //   2: iconst_0
/*    */     //   3: istore_2
/*    */     //   4: aload_1
/*    */     //   5: iload_2
/*    */     //   6: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*    */     //   11: tableswitch default -> 104, -1 -> 104, 0 -> 40, 1 -> 52, 2 -> 63
/*    */     //   40: aload_1
/*    */     //   41: checkcast net/minecraft/world/entity/item/PrimedTnt
/*    */     //   44: astore_3
/*    */     //   45: aload_3
/*    */     //   46: invokevirtual getOwner : ()Lnet/minecraft/world/entity/LivingEntity;
/*    */     //   49: goto -> 108
/*    */     //   52: aload_1
/*    */     //   53: checkcast net/minecraft/world/entity/LivingEntity
/*    */     //   56: astore #4
/*    */     //   58: aload #4
/*    */     //   60: goto -> 108
/*    */     //   63: aload_1
/*    */     //   64: checkcast net/minecraft/world/entity/projectile/Projectile
/*    */     //   67: astore #5
/*    */     //   69: aload #5
/*    */     //   71: invokevirtual getOwner : ()Lnet/minecraft/world/entity/Entity;
/*    */     //   74: astore #7
/*    */     //   76: aload #7
/*    */     //   78: instanceof net/minecraft/world/entity/LivingEntity
/*    */     //   81: ifeq -> 94
/*    */     //   84: aload #7
/*    */     //   86: checkcast net/minecraft/world/entity/LivingEntity
/*    */     //   89: astore #6
/*    */     //   91: goto -> 99
/*    */     //   94: iconst_3
/*    */     //   95: istore_2
/*    */     //   96: goto -> 4
/*    */     //   99: aload #6
/*    */     //   101: goto -> 108
/*    */     //   104: aconst_null
/*    */     //   105: goto -> 108
/*    */     //   108: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     //   #19	-> 40
/*    */     //   #20	-> 52
/*    */     //   #21	-> 63
/*    */     //   #22	-> 104
/*    */     //   #18	-> 108
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   45	7	3	primedTnt	Lnet/minecraft/world/entity/item/PrimedTnt;
/*    */     //   58	5	4	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*    */     //   91	3	6	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*    */     //   69	35	5	projectile	Lnet/minecraft/world/entity/projectile/Projectile;
/*    */     //   99	5	6	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*    */     //   0	109	0	source	Lnet/minecraft/world/entity/Entity; }
/*    */ 
/*    */   
/*    */   ServerLevel level();
/*    */ 
/*    */   
/*    */   BlockInteraction getBlockInteraction();
/*    */ 
/*    */   
/*    */   LivingEntity getIndirectSourceEntity();
/*    */ 
/*    */   
/*    */   Entity getDirectSourceEntity();
/*    */ 
/*    */   
/*    */   float radius();
/*    */ 
/*    */   
/*    */   Vec3 center();
/*    */   
/*    */   boolean canTriggerBlocks();
/*    */   
/*    */   boolean shouldAffectBlocklikeEntities();
/*    */   
/*    */   public enum BlockInteraction
/*    */   {
/* 43 */     KEEP(false),
/* 44 */     DESTROY(true),
/* 45 */     DESTROY_WITH_DECAY(true),
/* 46 */     TRIGGER_BLOCK(false);
/*    */     
/*    */     private final boolean shouldAffectBlocklikeEntities;
/*    */ 
/*    */     
/* 51 */     BlockInteraction(boolean shouldAffectBlocklikeEntities) { this.shouldAffectBlocklikeEntities = shouldAffectBlocklikeEntities; }
/*    */ 
/*    */ 
/*    */     
/* 55 */     public boolean shouldAffectBlocklikeEntities() { return this.shouldAffectBlocklikeEntities; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\Explosion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */