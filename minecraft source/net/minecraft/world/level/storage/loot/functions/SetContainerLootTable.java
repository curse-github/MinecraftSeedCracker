/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.SeededContainerLoot;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetContainerLootTable extends LootItemConditionalFunction {
/* 21 */   public static final MapCodec<SetContainerLootTable> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(LootTable.KEY_CODEC
/* 22 */           .fieldOf("name").forGetter(()), Codec.LONG
/* 23 */           .optionalFieldOf("seed", Long.valueOf(0L)).forGetter(()), BuiltInRegistries.BLOCK_ENTITY_TYPE
/* 24 */           .holderByNameCodec().fieldOf("type").forGetter(())))
/* 25 */       .apply(i, SetContainerLootTable::new));
/*    */   
/*    */   private final ResourceKey<LootTable> name;
/*    */   private final long seed;
/*    */   private final Holder<BlockEntityType<?>> type;
/*    */   
/*    */   private SetContainerLootTable(List<LootItemCondition> predicates, ResourceKey<LootTable> name, long seed, Holder<BlockEntityType<?>> type) {
/* 32 */     super(predicates);
/* 33 */     this.name = name;
/* 34 */     this.seed = seed;
/* 35 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public LootItemFunctionType<SetContainerLootTable> getType() { return LootItemFunctions.SET_LOOT_TABLE; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 45 */     if (itemStack.isEmpty()) {
/* 46 */       return itemStack;
/*    */     }
/* 48 */     itemStack.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(this.name, this.seed));
/* 49 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 54 */     super.validate(context);
/*    */ 
/*    */ 
/*    */     
/* 58 */     if (!context.allowsReferences()) {
/* 59 */       context.reportProblem(new ValidationContext.ReferenceNotAllowedProblem(this.name));
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     if (context.resolver().get(this.name).isEmpty()) {
/* 64 */       context.reportProblem(new ValidationContext.MissingReferenceProblem(this.name));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 69 */   public static LootItemConditionalFunction.Builder<?> withLootTable(BlockEntityType<?> type, ResourceKey<LootTable> value) { return simpleBuilder(conditions -> new SetContainerLootTable(conditions, value, 0L, type.builtInRegistryHolder())); }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public static LootItemConditionalFunction.Builder<?> withLootTable(BlockEntityType<?> type, ResourceKey<LootTable> value, long seed) { return simpleBuilder(conditions -> new SetContainerLootTable(conditions, value, seed, type.builtInRegistryHolder())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetContainerLootTable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */