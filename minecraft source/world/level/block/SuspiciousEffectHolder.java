/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.item.BlockItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ 
/*    */ public interface SuspiciousEffectHolder
/*    */ {
/*    */   SuspiciousStewEffects getSuspiciousEffects();
/*    */   
/* 17 */   static List<SuspiciousEffectHolder> getAllEffectHolders() { return (List)BuiltInRegistries.ITEM.stream().map(SuspiciousEffectHolder::tryGet).filter(Objects::nonNull).collect(Collectors.toList()); }
/*    */ 
/*    */   
/*    */   static SuspiciousEffectHolder tryGet(ItemLike item) {
/* 21 */     Item item2 = item.asItem(); if (item2 instanceof BlockItem) { BlockItem blockItem = (BlockItem)item2; Block block = blockItem.getBlock(); if (block instanceof SuspiciousEffectHolder) return (SuspiciousEffectHolder)block;
/*    */        }
/*    */     
/* 24 */     Item item1 = item.asItem(); if (item1 instanceof SuspiciousEffectHolder) return (SuspiciousEffectHolder)item1;
/*    */ 
/*    */     
/* 27 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SuspiciousEffectHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */