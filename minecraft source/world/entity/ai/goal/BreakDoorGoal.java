/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BreakDoorGoal
/*    */   extends DoorInteractGoal
/*    */ {
/*    */   private static final int DEFAULT_DOOR_BREAK_TIME = 240;
/*    */   private final Predicate<Difficulty> validDifficulties;
/*    */   protected int breakTime;
/* 17 */   protected int lastBreakProgress = -1;
/* 18 */   protected int doorBreakTime = -1;
/*    */   
/*    */   public BreakDoorGoal(Mob mob, Predicate<Difficulty> validDifficulties) {
/* 21 */     super(mob);
/* 22 */     this.validDifficulties = validDifficulties;
/*    */   }
/*    */   
/*    */   public BreakDoorGoal(Mob mob, int seconds, Predicate<Difficulty> validDifficulties) {
/* 26 */     this(mob, validDifficulties);
/* 27 */     this.doorBreakTime = seconds;
/*    */   }
/*    */ 
/*    */   
/* 31 */   protected int getDoorBreakTime() { return Math.max(240, this.doorBreakTime); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 36 */     if (!super.canUse()) {
/* 37 */       return false;
/*    */     }
/* 39 */     if (!((Boolean)getServerLevel(this.mob).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 40 */       return false;
/*    */     }
/* 42 */     return (isValidDifficulty(this.mob.level().getDifficulty()) && !isOpen());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 47 */     super.start();
/* 48 */     this.breakTime = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public boolean canContinueToUse() { return (this.breakTime <= getDoorBreakTime() && !isOpen() && this.doorPos.closerToCenterThan(this.mob.position(), 2.0D) && isValidDifficulty(this.mob.level().getDifficulty())); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop() {
/* 58 */     super.stop();
/* 59 */     this.mob.level().destroyBlockProgress(this.mob.getId(), this.doorPos, -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 64 */     super.tick();
/* 65 */     if (this.mob.getRandom().nextInt(20) == 0) {
/* 66 */       this.mob.level().levelEvent(1019, this.doorPos, 0);
/* 67 */       if (!this.mob.swinging) {
/* 68 */         this.mob.swing(this.mob.getUsedItemHand());
/*    */       }
/*    */     } 
/*    */     
/* 72 */     this.breakTime++;
/*    */     
/* 74 */     int progress = (int)(this.breakTime / getDoorBreakTime() * 10.0F);
/* 75 */     if (progress != this.lastBreakProgress) {
/* 76 */       this.mob.level().destroyBlockProgress(this.mob.getId(), this.doorPos, progress);
/* 77 */       this.lastBreakProgress = progress;
/*    */     } 
/*    */     
/* 80 */     if (this.breakTime == getDoorBreakTime() && isValidDifficulty(this.mob.level().getDifficulty())) {
/* 81 */       this.mob.level().removeBlock(this.doorPos, false);
/* 82 */       this.mob.level().levelEvent(1021, this.doorPos, 0);
/* 83 */       this.mob.level().levelEvent(2001, this.doorPos, Block.getId(this.mob.level().getBlockState(this.doorPos)));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 88 */   private boolean isValidDifficulty(Difficulty difficulty) { return this.validDifficulties.test(difficulty); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\BreakDoorGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */