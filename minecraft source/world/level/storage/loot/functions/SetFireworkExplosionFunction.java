/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetFireworkExplosionFunction extends LootItemConditionalFunction {
/* 17 */   public static final MapCodec<SetFireworkExplosionFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(FireworkExplosion.Shape.CODEC
/* 18 */           .optionalFieldOf("shape").forGetter(()), FireworkExplosion.COLOR_LIST_CODEC
/* 19 */           .optionalFieldOf("colors").forGetter(()), FireworkExplosion.COLOR_LIST_CODEC
/* 20 */           .optionalFieldOf("fade_colors").forGetter(()), Codec.BOOL
/* 21 */           .optionalFieldOf("trail").forGetter(()), Codec.BOOL
/* 22 */           .optionalFieldOf("twinkle").forGetter(())))
/* 23 */       .apply(i, SetFireworkExplosionFunction::new));
/*    */   
/* 25 */   public static final FireworkExplosion DEFAULT_VALUE = new FireworkExplosion(FireworkExplosion.Shape.SMALL_BALL, IntList.of(), IntList.of(), false, false);
/*    */   
/*    */   final Optional<FireworkExplosion.Shape> shape;
/*    */   final Optional<IntList> colors;
/*    */   final Optional<IntList> fadeColors;
/*    */   final Optional<Boolean> trail;
/*    */   final Optional<Boolean> twinkle;
/*    */   
/*    */   public SetFireworkExplosionFunction(List<LootItemCondition> predicates, Optional<FireworkExplosion.Shape> shape, Optional<IntList> colors, Optional<IntList> fadeColors, Optional<Boolean> hasTrail, Optional<Boolean> hasTwinkle) {
/* 34 */     super(predicates);
/* 35 */     this.shape = shape;
/* 36 */     this.colors = colors;
/* 37 */     this.fadeColors = fadeColors;
/* 38 */     this.trail = hasTrail;
/* 39 */     this.twinkle = hasTwinkle;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 44 */     itemStack.update(DataComponents.FIREWORK_EXPLOSION, DEFAULT_VALUE, this::apply);
/* 45 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   private FireworkExplosion apply(FireworkExplosion original) {
/* 50 */     Objects.requireNonNull(original);
/* 51 */     Objects.requireNonNull(original);
/* 52 */     Objects.requireNonNull(original);
/* 53 */     Objects.requireNonNull(original);
/* 54 */     Objects.requireNonNull(original); return new FireworkExplosion((FireworkExplosion.Shape)this.shape.orElseGet(original::shape), (IntList)this.colors.orElseGet(original::colors), (IntList)this.fadeColors.orElseGet(original::fadeColors), ((Boolean)this.trail.orElseGet(original::hasTrail)).booleanValue(), ((Boolean)this.twinkle.orElseGet(original::hasTwinkle)).booleanValue());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public LootItemFunctionType<SetFireworkExplosionFunction> getType() { return LootItemFunctions.SET_FIREWORK_EXPLOSION; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetFireworkExplosionFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */