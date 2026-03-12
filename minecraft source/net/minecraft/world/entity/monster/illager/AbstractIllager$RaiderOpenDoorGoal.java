/*    */ package net.minecraft.world.entity.monster.illager;
/*    */ 
/*    */ import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RaiderOpenDoorGoal
/*    */   extends OpenDoorGoal
/*    */ {
/* 50 */   public RaiderOpenDoorGoal(Raider raider) { super(raider, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean canUse() { return (super.canUse() && AbstractIllager.this.hasActiveRaid()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\AbstractIllager$RaiderOpenDoorGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */