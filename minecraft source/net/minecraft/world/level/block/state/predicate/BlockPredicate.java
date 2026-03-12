/*    */ package net.minecraft.world.level.block.state.predicate;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockPredicate
/*    */   extends Object
/*    */   implements Predicate<BlockState>
/*    */ {
/*    */   private final Block block;
/*    */   
/* 13 */   public BlockPredicate(Block block) { this.block = block; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static BlockPredicate forBlock(Block block) { return new BlockPredicate(block); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public boolean test(BlockState input) { return (input != null && input.is(this.block)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\predicate\BlockPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */