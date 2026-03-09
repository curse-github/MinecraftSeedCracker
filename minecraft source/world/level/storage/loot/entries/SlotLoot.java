/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.slot.SlotSource;
/*    */ import net.minecraft.world.item.slot.SlotSources;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SlotLoot extends LootPoolSingletonContainer {
/* 18 */   public static final MapCodec<SlotLoot> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SlotSources.CODEC
/* 19 */         .fieldOf("slot_source").forGetter(()))
/* 20 */       .and(singletonFields(i)).apply(i, SlotLoot::new));
/*    */   
/*    */   private final SlotSource slotSource;
/*    */   
/*    */   private SlotLoot(SlotSource slotSource, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 25 */     super(weight, quality, conditions, functions);
/* 26 */     this.slotSource = slotSource;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public LootPoolEntryType getType() { return LootPoolEntries.SLOTS; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, LootContext context) {
/* 36 */     this.slotSource.provide(context).itemCopies()
/* 37 */       .filter(stack -> !stack.isEmpty())
/* 38 */       .forEach(output);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 43 */     super.validate(context);
/* 44 */     this.slotSource.validate(context.forChild(new ProblemReporter.FieldPathElement("slot_source")));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\SlotLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */