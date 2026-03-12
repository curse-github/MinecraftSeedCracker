/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ 
/*    */ public static enum InsideBlockEffectType
/*    */ {
/*  8 */   FREEZE(entity -> {
/*  9 */       entity.setIsInPowderSnow(true);
/* 10 */       if (entity.canFreeze()) {
/* 11 */         entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze(), entity.getTicksFrozen() + 1));
/*    */       }
/*    */     }),
/* 14 */   CLEAR_FREEZE(Entity::clearFreeze),
/* 15 */   FIRE_IGNITE(BaseFireBlock::fireIgnite),
/* 16 */   LAVA_IGNITE(Entity::lavaIgnite),
/* 17 */   EXTINGUISH(Entity::clearFire);
/*    */ 
/*    */   
/*    */   private final Consumer<Entity> effect;
/*    */ 
/*    */   
/* 23 */   InsideBlockEffectType(Consumer<Entity> effect) { this.effect = effect; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public Consumer<Entity> effect() { return this.effect; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\InsideBlockEffectType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */