/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetItemFunction extends LootItemConditionalFunction {
/* 14 */   public static final MapCodec<SetItemFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(Item.CODEC
/* 15 */         .fieldOf("item").forGetter(()))
/* 16 */       .apply(i, SetItemFunction::new));
/*    */   
/*    */   private final Holder<Item> item;
/*    */   
/*    */   private SetItemFunction(List<LootItemCondition> predicates, Holder<Item> item) {
/* 21 */     super(predicates);
/* 22 */     this.item = item;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public LootItemFunctionType<SetItemFunction> getType() { return LootItemFunctions.SET_ITEM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public ItemStack run(ItemStack itemStack, LootContext context) { return itemStack.transmuteCopy((ItemLike)this.item.value()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetItemFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */