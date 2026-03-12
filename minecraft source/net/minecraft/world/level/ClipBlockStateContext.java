/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ClipBlockStateContext
/*    */ {
/*    */   private final Vec3 from;
/*    */   private final Vec3 to;
/*    */   private final Predicate<BlockState> block;
/*    */   
/*    */   public ClipBlockStateContext(Vec3 from, Vec3 to, Predicate<BlockState> block) {
/* 14 */     this.from = from;
/* 15 */     this.to = to;
/* 16 */     this.block = block;
/*    */   }
/*    */ 
/*    */   
/* 20 */   public Vec3 getTo() { return this.to; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public Vec3 getFrom() { return this.from; }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public Predicate<BlockState> isTargetBlock() { return this.block; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ClipBlockStateContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */