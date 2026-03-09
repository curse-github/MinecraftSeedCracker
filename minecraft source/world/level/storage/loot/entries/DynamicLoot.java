/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class DynamicLoot extends LootPoolSingletonContainer {
/* 15 */   public static final MapCodec<DynamicLoot> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 16 */         .fieldOf("name").forGetter(()))
/* 17 */       .and(singletonFields(i)).apply(i, DynamicLoot::new));
/*    */   
/*    */   private final Identifier name;
/*    */   
/*    */   private DynamicLoot(Identifier name, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 22 */     super(weight, quality, conditions, functions);
/* 23 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public LootPoolEntryType getType() { return LootPoolEntries.DYNAMIC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void createItemStack(Consumer<ItemStack> output, LootContext context) { context.addDynamicDrops(this.name, output); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static LootPoolSingletonContainer.Builder<?> dynamicEntry(Identifier name) { return simpleBuilder((weight, quality, conditions, functions) -> new DynamicLoot(name, weight, quality, conditions, functions)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\DynamicLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */