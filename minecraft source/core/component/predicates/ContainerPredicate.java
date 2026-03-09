/*    */ package net.minecraft.core.component.predicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.CollectionPredicate;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ItemContainerContents;
/*    */ 
/*    */ public final class ContainerPredicate extends Record implements SingleComponentItemPredicate<ItemContainerContents> {
/*    */   private final Optional<CollectionPredicate<ItemStack, ItemPredicate>> items;
/*    */   
/* 15 */   public ContainerPredicate(Optional<CollectionPredicate<ItemStack, ItemPredicate>> items) { this.items = items; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/ContainerPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/ContainerPredicate; } public Optional<CollectionPredicate<ItemStack, ItemPredicate>> items() { return this.items; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/ContainerPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/ContainerPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/ContainerPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/ContainerPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 16 */   public static final Codec<ContainerPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 17 */         CollectionPredicate.codec(ItemPredicate.CODEC).optionalFieldOf("items").forGetter(ContainerPredicate::items))
/* 18 */       .apply(i, ContainerPredicate::new));
/*    */ 
/*    */ 
/*    */   
/* 22 */   public DataComponentType<ItemContainerContents> componentType() { return DataComponents.CONTAINER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(ItemContainerContents value) {
/* 27 */     if (this.items.isPresent() && !((CollectionPredicate)this.items.get()).test(value.nonEmptyItems())) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\ContainerPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */