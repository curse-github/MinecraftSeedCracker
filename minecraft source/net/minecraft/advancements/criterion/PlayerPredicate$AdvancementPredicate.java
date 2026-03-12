/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.AdvancementProgress;
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
/*    */ interface AdvancementPredicate
/*    */   extends Predicate<AdvancementProgress>
/*    */ {
/* 66 */   public static final Codec<AdvancementPredicate> CODEC = Codec.either(PlayerPredicate.AdvancementDonePredicate.CODEC, PlayerPredicate.AdvancementCriterionsPredicate.CODEC).xmap(Either::unwrap, predicate -> {
/*    */ 
/*    */         
/* 69 */         if (predicate instanceof PlayerPredicate.AdvancementDonePredicate) { PlayerPredicate.AdvancementDonePredicate done = (PlayerPredicate.AdvancementDonePredicate)predicate;
/* 70 */           return Either.left(done); }
/* 71 */          if (predicate instanceof PlayerPredicate.AdvancementCriterionsPredicate) { PlayerPredicate.AdvancementCriterionsPredicate criterions = (PlayerPredicate.AdvancementCriterionsPredicate)predicate;
/* 72 */           return Either.right(criterions); }
/*    */         
/* 74 */         throw new UnsupportedOperationException();
/*    */       });
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\PlayerPredicate$AdvancementPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */