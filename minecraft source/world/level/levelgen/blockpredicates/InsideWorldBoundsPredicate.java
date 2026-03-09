/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ public class InsideWorldBoundsPredicate implements BlockPredicate {
/* 10 */   public static final MapCodec<InsideWorldBoundsPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 11 */         Vec3i.offsetCodec(16).optionalFieldOf("offset", BlockPos.ZERO).forGetter(()))
/* 12 */       .apply(i, InsideWorldBoundsPredicate::new));
/*    */   
/*    */   private final Vec3i offset;
/*    */ 
/*    */   
/* 17 */   public InsideWorldBoundsPredicate(Vec3i offset) { this.offset = offset; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public boolean test(WorldGenLevel worldGenLevel, BlockPos blockPos) { return !worldGenLevel.isOutsideBuildHeight(blockPos.offset(this.offset)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public BlockPredicateType<?> type() { return BlockPredicateType.INSIDE_WORLD_BOUNDS; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\InsideWorldBoundsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */