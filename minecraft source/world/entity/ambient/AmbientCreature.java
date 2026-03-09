/*    */ package net.minecraft.world.entity.ambient;
/*    */ 
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AmbientCreature
/*    */   extends Mob {
/*  9 */   protected AmbientCreature(EntityType<? extends AmbientCreature> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public boolean canBeLeashed() { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ambient\AmbientCreature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */