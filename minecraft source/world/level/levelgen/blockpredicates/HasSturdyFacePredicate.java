/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ public class HasSturdyFacePredicate implements BlockPredicate {
/*    */   private final Vec3i offset;
/* 14 */   public static final MapCodec<HasSturdyFacePredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 15 */         Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(()), Direction.CODEC
/* 16 */         .fieldOf("direction").forGetter(()))
/* 17 */       .apply(i, HasSturdyFacePredicate::new)); private final Direction direction;
/*    */   
/*    */   public HasSturdyFacePredicate(Vec3i offset, Direction direction) {
/* 20 */     this.offset = offset;
/* 21 */     this.direction = direction;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(WorldGenLevel level, BlockPos origin) {
/* 26 */     BlockPos testPosition = origin.offset(this.offset);
/* 27 */     return level.getBlockState(testPosition).isFaceSturdy(level, testPosition, this.direction);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public BlockPredicateType<?> type() { return BlockPredicateType.HAS_STURDY_FACE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blockpredicates\HasSturdyFacePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */