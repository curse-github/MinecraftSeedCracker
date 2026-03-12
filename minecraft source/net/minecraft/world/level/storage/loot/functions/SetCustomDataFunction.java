/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomData;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetCustomDataFunction extends LootItemConditionalFunction {
/* 16 */   public static final MapCodec<SetCustomDataFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(TagParser.LENIENT_CODEC
/* 17 */         .fieldOf("tag").forGetter(()))
/* 18 */       .apply(i, SetCustomDataFunction::new));
/*    */   
/*    */   private final CompoundTag tag;
/*    */   
/*    */   private SetCustomDataFunction(List<LootItemCondition> predicates, CompoundTag tag) {
/* 23 */     super(predicates);
/* 24 */     this.tag = tag;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public LootItemFunctionType<SetCustomDataFunction> getType() { return LootItemFunctions.SET_CUSTOM_DATA; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 34 */     CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> tag.merge(this.tag));
/* 35 */     return itemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/* 43 */   public static LootItemConditionalFunction.Builder<?> setCustomData(CompoundTag value) { return simpleBuilder(conditions -> new SetCustomDataFunction(conditions, value)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetCustomDataFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */