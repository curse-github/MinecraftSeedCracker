/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public abstract class Goal
/*    */ {
/* 12 */   private final EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
/*    */ 
/*    */   
/*    */   public abstract boolean canUse();
/*    */   
/* 17 */   public boolean canContinueToUse() { return canUse(); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public boolean isInterruptable() { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() {}
/*    */ 
/*    */   
/*    */   public void stop() {}
/*    */ 
/*    */   
/* 31 */   public boolean requiresUpdateEveryTick() { return false; }
/*    */ 
/*    */   
/*    */   public void tick() {}
/*    */ 
/*    */   
/*    */   public void setFlags(EnumSet<Flag> requiredControlFlags) {
/* 38 */     this.flags.clear();
/* 39 */     this.flags.addAll(requiredControlFlags);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public String toString() { return getClass().getSimpleName(); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public EnumSet<Flag> getFlags() { return this.flags; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   protected int adjustedTickDelay(int ticks) { return requiresUpdateEveryTick() ? ticks : reducedTickDelay(ticks); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   protected static int reducedTickDelay(int ticks) { return Mth.positiveCeilDiv(ticks, 2); }
/*    */   
/*    */   public enum Flag
/*    */   {
/* 64 */     MOVE,
/* 65 */     LOOK,
/* 66 */     JUMP,
/* 67 */     TARGET;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   protected static ServerLevel getServerLevel(Entity entity) { return (ServerLevel)entity.level(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 81 */   protected static ServerLevel getServerLevel(Level level) { return (ServerLevel)level; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\Goal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */