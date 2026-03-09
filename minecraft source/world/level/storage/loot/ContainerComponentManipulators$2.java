/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BundleContents;
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
/*    */ class null
/*    */   extends Object
/*    */   implements ContainerComponentManipulator<BundleContents>
/*    */ {
/* 43 */   public DataComponentType<BundleContents> type() { return DataComponents.BUNDLE_CONTENTS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public BundleContents empty() { return BundleContents.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public Stream<ItemStack> getContents(BundleContents component) { return component.itemCopyStream(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BundleContents setContents(BundleContents component, Stream<ItemStack> newContents) {
/* 58 */     BundleContents.Mutable builder = (new BundleContents.Mutable(component)).clearItems();
/* 59 */     Objects.requireNonNull(builder); newContents.forEach(builder::tryInsert);
/* 60 */     return builder.toImmutable();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\ContainerComponentManipulators$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */