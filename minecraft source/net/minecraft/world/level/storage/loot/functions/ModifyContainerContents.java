/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ModifyContainerContents extends LootItemConditionalFunction {
/* 16 */   public static final MapCodec<ModifyContainerContents> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(ContainerComponentManipulators.CODEC
/* 17 */           .fieldOf("component").forGetter(()), LootItemFunctions.ROOT_CODEC
/* 18 */           .fieldOf("modifier").forGetter(())))
/* 19 */       .apply(i, ModifyContainerContents::new));
/*    */   
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   private final LootItemFunction modifier;
/*    */   
/*    */   private ModifyContainerContents(List<LootItemCondition> predicates, ContainerComponentManipulator<?> component, LootItemFunction modifier) {
/* 25 */     super(predicates);
/* 26 */     this.component = component;
/* 27 */     this.modifier = modifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public LootItemFunctionType<ModifyContainerContents> getType() { return LootItemFunctions.MODIFY_CONTENTS; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 37 */     if (itemStack.isEmpty()) {
/* 38 */       return itemStack;
/*    */     }
/*    */     
/* 41 */     this.component.modifyItems(itemStack, c -> (ItemStack)this.modifier.apply(c, context));
/*    */     
/* 43 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 48 */     super.validate(context);
/* 49 */     this.modifier.validate(context.forChild(new ProblemReporter.FieldPathElement("modifier")));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ModifyContainerContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */