/*   */ package net.minecraft.world.entity.ai.goal;
/*   */ 
/*   */ import java.util.EnumSet;
/*   */ 
/*   */ public abstract class JumpGoal
/*   */   extends Goal {
/* 7 */   public JumpGoal() { setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP)); }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\JumpGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */