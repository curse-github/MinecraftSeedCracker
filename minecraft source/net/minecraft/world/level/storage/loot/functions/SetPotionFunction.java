/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetPotionFunction extends LootItemConditionalFunction {
/* 16 */   public static final MapCodec<SetPotionFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(Potion.CODEC
/* 17 */         .fieldOf("id").forGetter(()))
/* 18 */       .apply(i, SetPotionFunction::new));
/*    */   
/*    */   private final Holder<Potion> potion;
/*    */   
/*    */   private SetPotionFunction(List<LootItemCondition> predicates, Holder<Potion> potion) {
/* 23 */     super(predicates);
/* 24 */     this.potion = potion;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public LootItemFunctionType<SetPotionFunction> getType() { return LootItemFunctions.SET_POTION; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 34 */     itemStack.update(DataComponents.POTION_CONTENTS, PotionContents.EMPTY, this.potion, PotionContents::withPotion);
/* 35 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 39 */   public static LootItemConditionalFunction.Builder<?> setPotion(Holder<Potion> value) { return simpleBuilder(conditions -> new SetPotionFunction(conditions, value)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetPotionFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */