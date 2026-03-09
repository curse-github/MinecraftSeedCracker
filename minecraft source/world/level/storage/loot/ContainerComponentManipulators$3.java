/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ChargedProjectiles;
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
/*    */   implements ContainerComponentManipulator<ChargedProjectiles>
/*    */ {
/* 67 */   public DataComponentType<ChargedProjectiles> type() { return DataComponents.CHARGED_PROJECTILES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   public ChargedProjectiles empty() { return ChargedProjectiles.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   public Stream<ItemStack> getContents(ChargedProjectiles component) { return component.getItems().stream(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   public ChargedProjectiles setContents(ChargedProjectiles component, Stream<ItemStack> newContents) { return ChargedProjectiles.of(newContents.toList()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\ContainerComponentManipulators$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */