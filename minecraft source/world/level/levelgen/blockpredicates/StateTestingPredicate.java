/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class StateTestingPredicate implements BlockPredicate {
/*    */   protected final Vec3i offset;
/*    */   
/*    */   protected static <P extends StateTestingPredicate> Products.P1<RecordCodecBuilder.Mu<P>, Vec3i> stateTestingCodec(RecordCodecBuilder.Instance<P> instance) {
/* 14 */     return instance.group(
/* 15 */         Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(c -> c.offset));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected StateTestingPredicate(Vec3i offset) { this.offset = offset; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public final boolean test(WorldGenLevel level, BlockPos origin) { return test(level.getBlockState(origin.offset(this.offset))); }
/*    */   
/*    */   protected abstract boolean test(BlockState paramBlockState);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\StateTestingPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */